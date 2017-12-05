package com.hugocastelani.blockbook.ui.myprofile;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.afollestad.aesthetic.AestheticTextView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.blankj.utilcode.util.ScreenUtils;
import com.hugocastelani.blockbook.R;
import com.hugocastelani.blockbook.annotation.NonNegative;
import com.hugocastelani.blockbook.database.dao.local.LUserDAO;
import com.hugocastelani.blockbook.database.dao.wide.UserDAO;
import com.hugocastelani.blockbook.database.dao.wide.UserFollowerDAO;
import com.hugocastelani.blockbook.database.dao.wide.UserPhoneDAO;
import com.hugocastelani.blockbook.database.domain.User;
import com.hugocastelani.blockbook.domain.UserSection;
import com.hugocastelani.blockbook.ui.profile.ProfileActivity;
import com.hugocastelani.blockbook.ui.profile.ProfileFragment;
import com.hugocastelani.blockbook.ui.viewholder.PhoneHeaderViewHolder;
import com.hugocastelani.blockbook.ui.viewholder.ToolbarViewHolder;
import com.hugocastelani.blockbook.ui.viewholder.UserViewHolder;
import com.hugocastelani.blockbook.util.Listeners;
import com.hugocastelani.blockbook.util.ThemeHandler;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by Hugo Castelani
 * Date: 15/11/17
 * Time: 11:00
 */

public final class MyProfileAdapter extends SectionedRecyclerViewAdapter<SectionedViewHolder> {
    private static final int VIEW_TYPE_TOOLBAR = -4;

    private ProfileFragment mFragment;
    private ProfileActivity mActivity;
    private User mUser;

    private ArrayList<UserSection> mUserSectionList;

    private Listeners.ListListener<User> mFollowerListListener;
    private Listeners.ListListener<User> mFollowingListListener;

    private Listeners.AnswerListener mAnswerListener;

    public MyProfileAdapter(@NonNull final User user, @NonNull final ProfileFragment fragment) {
        mUser = user;
        mFragment = fragment;
        mActivity = (ProfileActivity) fragment.getActivity();

        mUserSectionList = new ArrayList<>();

        final String followers = fragment.getResources().getString(R.string.profile_followers);
        mUserSectionList.add(new UserSection(followers, new ArrayList<>()));

        UserFollowerDAO.get().getUserList(mUser.getKey(), UserFollowerDAO.NODE_FOLLOWERS,
                getFollowerListListener(), getAnswerListener());
    }

    private Listeners.ListListener getFollowerListListener() {
        if (mFollowerListListener == null) {
            mFollowerListListener = new Listeners.ListListener<User>() {
                @Override
                public void onItemAdded(@NonNull final User user) {
                    mUserSectionList.get(0).addUser(user);
                }

                @Override public void onItemRemoved(@NonNull final User user) {}
            };
        }

        return mFollowerListListener;
    }

    private Listeners.ListListener getFollowingListListener() {
        if (mFollowingListListener == null) {
            mFollowingListListener = new Listeners.ListListener<User>() {
                @Override
                public void onItemAdded(@NonNull final User user) {
                    mUserSectionList.get(1).addUser(user);
                }

                @Override public void onItemRemoved(@NonNull final User user) {}
            };
        }

        return mFollowingListListener;
    }

    private Listeners.AnswerListener getAnswerListener() {
        if (mAnswerListener == null) {
            mAnswerListener = new Listeners.AnswerListener() {
                Integer count = 0;

                @Override
                public void onAnswerRetrieved() {
                    if (++count == 1) {
                        final String following = mFragment.getResources().getString(R.string.profile_following);
                        mUserSectionList.add(new UserSection(following, new ArrayList<>()));

                        UserFollowerDAO.get().getUserList(mUser.getKey(), UserFollowerDAO.NODE_FOLLOWING,
                                getFollowingListListener(), getAnswerListener());

                    } else if (count == 2) {

                        if (mUserSectionList.get(0).getUserList().isEmpty() &&
                            mUserSectionList.get(1).getUserList().isEmpty()) {
                            mFragment.showPlaceHolder();
                        } else {
                            mFragment.showRecyclerView();
                        }
                    }
                }

                @Override
                public void onError() {
                    mActivity.createSnackbarAndClose(R.string.something_went_wrong).show();
                }
            };
        }

        return mAnswerListener;
    }

    public void removeItem(@NonNegative @NonNull final Integer absolutePosition) {
        final Integer section = 1;
        final Integer position = getRelativePosition(absolutePosition).relativePos();
        final ArrayList<User> userList = mUserSectionList.get(section).getUserList();

        final Resources resources = mFragment.getContext().getResources();

        // store information for undo case
        User removedUser = userList.get(position);
        UserSection removedPhoneSection = mUserSectionList.get(section);

        userList.remove(removedUser);
        notifyItemRemoved(absolutePosition);

        // remove header when there's no item
        if (userList.isEmpty()) {
            mUserSectionList.remove(section);
            notifyItemRemoved(absolutePosition - 1);
            // absolutePosition - 1 because removed item is, obviously, last item

            if (mUserSectionList.get(0).getUserList().isEmpty()) {
                mFragment.showPlaceHolder();
            }
        }

        Snackbar.make(mFragment.getView(), resources.getString(R.string.removed_number), Snackbar.LENGTH_LONG)
                .setAction(resources.getString(R.string.undo), view -> {
                    // add items to adapter and list again
                    mFragment.showRecyclerView();
                    userList.add(position, removedUser);
                    notifyItemInserted(absolutePosition);

                    if (!mUserSectionList.contains(removedPhoneSection)) {
                        mUserSectionList.add(section, removedPhoneSection);
                        notifyItemInserted(absolutePosition - 1);
                    }
                })
                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        if (event != DISMISS_EVENT_ACTION) {    // if equals, it has just undone
                            // finally remove from database
                            UserPhoneDAO.get().deleteOfUser(removedUser.getKey(), new Listeners.AnswerListener() {
                                @Override
                                public void onAnswerRetrieved() {
                                    UserFollowerDAO.get().delete(removedUser.getKey(), new Listeners.AnswerListener() {
                                        @Override public void onAnswerRetrieved() {}
                                        @Override public void onError() {}
                                    });
                                }

                                @Override public void onError() {}
                            });
                        }
                    }
                })
                .show();
    }

    @Override
    public int getSectionCount() {
        return mUserSectionList.size() + 1;
    }

    @Override
    public int getItemCount(int section) {
        return section == 0 ? 1 : mUserSectionList.get(section - 1).getUserList().size();
    }

    @Override
    public int getItemViewType(int section, int relativePosition, int absolutePosition) {
        if (section == 0) {
            return VIEW_TYPE_TOOLBAR;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    @Override
    public void onBindHeaderViewHolder(SectionedViewHolder holder, int section, boolean expanded) {
        final AestheticTextView blockerOrNumber = ((PhoneHeaderViewHolder) holder).mBlockerOrNumber;

        if (section == 0) {
            final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 0);
            blockerOrNumber.setLayoutParams(layoutParams);

        } else {

            blockerOrNumber.setText(
                    mUserSectionList.get(section - 1).getSectionName()
            );
        }
    }

    @Override
    public void onBindFooterViewHolder(SectionedViewHolder holder, int section) {}

    @Override
    public void onBindViewHolder(SectionedViewHolder holder, int section, int relativePosition, int absolutePosition) {
        if (holder instanceof UserViewHolder) {
            final UserViewHolder viewHolder = (UserViewHolder) holder;

            final User user = mUserSectionList.get(section - 1).getUserList().get(relativePosition);

            if (mUser.getPicURL().isEmpty()) {
                viewHolder.mUserPic.setBackground(ContextCompat.getDrawable(mFragment.getContext(), R.drawable.intro_3));

            } else {

                Picasso.with(mFragment.getContext()).load(mUser.getPicURL()).into(viewHolder.mUserPic, new Callback() {
                    @Override public void onSuccess() {}

                    @Override public void onError() {
                        mActivity.createSnackbar(R.string.profile_error_user_pic).show();
                    }
                });
            }
            viewHolder.mIsSwipeable = section > 1;

            viewHolder.mCardView.setOnClickListener(view -> {
                final Intent intent = new Intent(mFragment.getActivity(), ProfileActivity.class);
                intent.putExtra("user", user);
                mFragment.getActivity().startActivity(intent);
            });

            viewHolder.mUsername.setText("@" + user.getUsername());

        } else {

            final ToolbarViewHolder viewHolder = (ToolbarViewHolder) holder;

            viewHolder.mUsername.setText("@" + mUser.getUsername());

            CharSequence socialMedia = mUser.getSocialMedia(mFragment.getResources());
            if (socialMedia == null) {
                viewHolder.mSocialMedia.setVisibility(View.GONE);
            } else {
                viewHolder.mSocialMedia.setText(socialMedia);
            }


            if (mUser.getPicURL().isEmpty()) {
                viewHolder.mUserPic.setBackground(ContextCompat.getDrawable(mFragment.getContext(), R.drawable.intro_3));

            } else {

                Picasso.with(mFragment.getContext()).load(mUser.getPicURL()).into(viewHolder.mUserPic, new Callback() {
                    @Override public void onSuccess() {}

                    @Override public void onError() {
                        mActivity.createSnackbar(R.string.profile_error_user_pic).show();
                    }
                });
            }

            viewHolder.mButton.setText(mFragment.getResources().getString(R.string.profile_delete_account));
            viewHolder.mButton.setOnClickListener(view ->
                new MaterialDialog.Builder(mFragment.getContext())
                    .title(R.string.profile_delete_account_title)
                    .content(R.string.profile_delete_account_description)
                    .positiveText(R.string.yes_button)
                    .negativeText(R.string.cancel_button)
                    .positiveColor(ThemeHandler.getAccent())
                    .negativeColor(ThemeHandler.getAccent())
                    .onPositive((dialog, which) -> {
                        mFragment.showDeletingProgressDialog();

                        final Listeners.AnswerListener answerListener = new Listeners.AnswerListener() {
                            Integer count = 0;

                            @Override
                            public void onAnswerRetrieved() {
                                if (++count == 2) {
                                    final ActivityManager manager = (ActivityManager) mFragment
                                            .getContext().getSystemService(ACTIVITY_SERVICE);
                                    manager.clearApplicationUserData();
                                } else if (count == 1) {
                                    UserDAO.get().delete(LUserDAO.get().getThisUserKey(), this);
                                }
                            }

                            @Override
                            public void onError() {
                                mFragment.hideDeletingProgressDialog();
                                new MaterialDialog.Builder(mFragment.getContext())
                                        .title(R.string.profile_delete_account_error_title)
                                        .content(R.string.profile_delete_account_error_description)
                                        .build().show();
                            }
                        };

                        UserFollowerDAO.get().deleteAllOfUser(LUserDAO.get().getThisUserKey(), answerListener);
                    })
                    .build().show()
            );
        }
    }

    @Override
    public SectionedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                return new UserViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.item_user, parent, false));

            case VIEW_TYPE_HEADER:
                return new PhoneHeaderViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.preference_category, parent, false));

            case VIEW_TYPE_TOOLBAR:
                if (ScreenUtils.isTablet() || ScreenUtils.isLandscape()) {
                    return new ToolbarViewHolder(
                            LayoutInflater.from(parent.getContext()).inflate(
                                    R.layout.item_profile_toolbar_landscape, parent, false));

                } else {

                    return new ToolbarViewHolder(
                            LayoutInflater.from(parent.getContext()).inflate(
                                    R.layout.item_profile_toolbar_portrait, parent, false));
                }

            default:
                return new UserViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.item_user, parent, false));
        }
    }
}
