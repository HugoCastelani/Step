package com.enoughspam.step.myprofile;

import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.afollestad.aesthetic.AestheticTextView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.enoughspam.step.R;
import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.dao.local.LUserDAO;
import com.enoughspam.step.database.dao.wide.UserDAO;
import com.enoughspam.step.database.dao.wide.UserFollowerDAO;
import com.enoughspam.step.database.dao.wide.UserPhoneDAO;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.domain.UserSection;
import com.enoughspam.step.profile.ProfileActivity;
import com.enoughspam.step.profile.ProfileFragment;
import com.enoughspam.step.util.AnimUtils;
import com.enoughspam.step.util.Listeners;
import com.enoughspam.step.util.ThemeHandler;
import com.enoughspam.step.viewholder.ProfileViewHolder;
import com.enoughspam.step.viewholder.SimplePhoneHeaderViewHolder;
import com.enoughspam.step.viewholder.ToolbarViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Hugo Castelani
 * Date: 15/11/17
 * Time: 11:00
 */

public class MyProfileAdapter extends SectionedRecyclerViewAdapter<SectionedViewHolder> {
    private static final int VIEW_TYPE_TOOLBAR = -4;

    private ProfileFragment mFragment;
    private User mUser;

    private ArrayList<UserSection> mUserSectionList;

    private Listeners.ListListener<User> mFollowerListListener;
    private Listeners.ListListener<User> mFollowingListListener;

    private Listeners.AnswerListener mAnswerListener;

    public MyProfileAdapter(@NonNull final User user, @NonNull final ProfileFragment fragment) {
        mUser = user;
        mFragment = fragment;

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
                    mFragment.showSnackAndClose(R.string.something_went_wrong);
                }
            };
        }

        return mAnswerListener;
    }

    public void removeItem(@NonNegative final int absolutePosition) {
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
        final AestheticTextView blockerOrNumber = ((SimplePhoneHeaderViewHolder) holder).mBlockerOrNumber;

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
        if (holder instanceof ProfileViewHolder) {
            final ProfileViewHolder viewHolder = (ProfileViewHolder) holder;

            final User user = mUserSectionList.get(section - 1).getUserList().get(relativePosition);

            Picasso.with(mFragment.getContext()).load(user.getPicURL())
                    .into(viewHolder.mUserPic, new Callback() {
                        @Override
                        public void onSuccess() {
                            AnimUtils.fadeOutFadeIn(viewHolder.mProgressBar, viewHolder.mUserPic);
                        }

                        @Override public void onError() {}
                    });

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

            CharSequence socialMedia = "";
            switch (mUser.getSocialKey().charAt(mUser.getSocialKey().length() - 1)) {    // last char
                case '1':    // google code
                    socialMedia = mFragment.getResources().getText(R.string.profile_signed_via_google);
                    break;
                default: viewHolder.mSocialMedia.setVisibility(View.GONE);
            }

            viewHolder.mSocialMedia.setText(socialMedia);

            // init user photo and progress bar actions
            Picasso.with(mFragment.getContext()).load(mUser.getPicURL()).into(viewHolder.mUserPic, new Callback() {
                @Override public void onSuccess() {}

                @Override public void onError() {
                    mFragment.showSnackbar(R.string.profile_error_user_pic);
                }
            });

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
                                    AppUtils.exitApp();
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
                return new ProfileViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.search_item_profile, parent, false));

            case VIEW_TYPE_HEADER:
                return new SimplePhoneHeaderViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.preference_category, parent, false));

            case VIEW_TYPE_TOOLBAR:
                if (ScreenUtils.isTablet() || ScreenUtils.isLandscape()) {
                    return new ToolbarViewHolder(
                            LayoutInflater.from(parent.getContext()).inflate(
                                    R.layout.profile_item_toolbar_landscape, parent, false));

                } else {

                    return new ToolbarViewHolder(
                            LayoutInflater.from(parent.getContext()).inflate(
                                    R.layout.profile_item_toolbar_portrait, parent, false));
                }

            default:
                return new ProfileViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.search_item_profile, parent, false));
        }
    }
}
