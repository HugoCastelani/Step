package com.hugocastelani.ivory.ui.profile;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.afollestad.aesthetic.AestheticButton;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.blankj.utilcode.util.ScreenUtils;
import com.hugocastelani.ivory.R;
import com.hugocastelani.ivory.database.dao.wide.UserFollowerDAO;
import com.hugocastelani.ivory.database.dao.wide.UserPhoneDAO;
import com.hugocastelani.ivory.database.domain.User;
import com.hugocastelani.ivory.domain.PhoneSection;
import com.hugocastelani.ivory.ui.intangible.UsersNumbersAdapter;
import com.hugocastelani.ivory.ui.viewholder.PhoneHeaderViewHolder;
import com.hugocastelani.ivory.ui.viewholder.ToolbarViewHolder;
import com.hugocastelani.ivory.util.Listeners;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by Hugo Castelani
 * Date: 14/10/17
 * Time: 20:34
 */

public final class ProfileAdapter extends UsersNumbersAdapter {
    private static final int VIEW_TYPE_TOOLBAR = -4;

    private User mUser;
    private Boolean isFollowing;

    public ProfileAdapter(@NonNull final User user, @NonNull final ProfileFragment fragment) {
        super(fragment);
        mUser = user;

        UserFollowerDAO.get().exists(mUser, new Listeners.ObjectListener<Boolean>() {
            @Override
            public void onObjectRetrieved(@NonNull Boolean retrievedBoolean) {
                isFollowing = retrievedBoolean;
            }

            @Override
            public void onError() {
                mActivity.createSnackbarAndClose(R.string.profile_error_loading_user).show();
            }
        });

        UserPhoneDAO.get().getUserPhoneList(user.getKey(), getListListener(), getAnswerListener());
    }

    @Override protected void removeItemFromDatabase(@NonNull String phoneKey) {}

    @Override
    public int getSectionCount() {
        return super.getSectionCount() + 1;
    }

    @Override
    public int getItemCount(int section) {
        if (section == 0) return 1;
        else return super.getItemCount(section - 1);
        // we're sending section - 1 because toolbar takes one sectionz
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
        if (section == 0) {
            final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 0);
            ((PhoneHeaderViewHolder) holder).mBlockerOrNumber.setLayoutParams(layoutParams);
        } else {
            super.onBindHeaderViewHolder(holder, section - 1, expanded);
        }
    }

    @Override
    public void onBindViewHolder(SectionedViewHolder holder, int section, int relativePosition, int absolutePosition) {
        if (holder instanceof PhoneHeaderViewHolder) {
            super.onBindViewHolder(holder, section - 1, relativePosition, absolutePosition);

        } else {

            final ToolbarViewHolder viewHolder = (ToolbarViewHolder) holder;

            viewHolder.mUsername.setText("@" + mUser.getUsername());

            CharSequence socialMedia = mUser.getSocialMedia(mFragment.getResources());
            if (socialMedia == null) {
                viewHolder.mSocialMedia.setVisibility(View.GONE);
            } else {
                viewHolder.mSocialMedia.setText(socialMedia);
            }

            // init user photo and progress bar actions
            if (mUser.getPicURL().isEmpty()) {
                viewHolder.mUserPic.setBackground(ContextCompat.getDrawable(mFragment.getContext(), R.drawable.intro_login));

            } else {

                Picasso.with(mFragment.getContext()).load(mUser.getPicURL()).into(viewHolder.mUserPic, new Callback() {
                    @Override public void onSuccess() {}

                    @Override public void onError() {
                        mActivity.createSnackbar(R.string.profile_error_user_pic).show();
                    }
                });
            }

            // init button actions
            if (isFollowing) {
                setButtonAsRemovable(viewHolder.mButton);
            } else {
                setButtonAsAddable(viewHolder.mButton);
            }
        }
    }

    @Override
    protected Boolean isSwipeable() {
        return false;
    }

    @Override
    protected void onClick(@NonNull PhoneSection phoneSection, @NonNull PhoneHeaderViewHolder viewHolder) {
    }

    @Override
    protected boolean onLongClick(@NonNull PhoneSection phoneSection, @NonNull PhoneHeaderViewHolder viewHolder) {
        return false;
    }

    private void setButtonAsAddable(@NonNull final AestheticButton button) {
        button.setText(mFragment.getResources().getString(R.string.profile_follow));
        button.setOnClickListener(view -> {
            ((ProfileFragment) mFragment).showAddingProgressDialog();

            UserFollowerDAO.get().create(mUser, new Listeners.AnswerListener() {
                @Override
                public void onAnswerRetrieved() {
                    setButtonAsRemovable(button);
                    ((ProfileFragment) mFragment).hideAddingProgressDialog();
                }

                @Override
                public void onError() {
                    ((ProfileFragment) mFragment).hideAddingProgressDialog();
                    mActivity.createSnackbar(R.string.profile_error_add_user).show();
                }
            });
        });
    }

    private void setButtonAsRemovable(@NonNull final AestheticButton button) {
        button.setText(mFragment.getResources().getString(R.string.profile_unfollow));
        button.setOnClickListener(view -> {
            ((ProfileFragment) mFragment).showRemovingProgressDialog();

            final Listeners.AnswerListener answerListener = new Listeners.AnswerListener() {
                Integer count = 0;

                @Override
                public void onAnswerRetrieved() {
                    if (++count == 2) {
                        setButtonAsAddable(button);
                        ((ProfileFragment) mFragment).hideRemovingProgressDialog();
                    }
                }

                @Override
                public void onError() {
                    ((ProfileFragment) mFragment).hideRemovingProgressDialog();
                    mActivity.createSnackbar(R.string.profile_error_remove_user).show();
                }
            };

            UserPhoneDAO.get().deleteOfUser(mUser.getKey(), answerListener);

            UserFollowerDAO.get().delete(mUser.getKey(), answerListener);
        });
    }

    @Override
    public SectionedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                return new PhoneHeaderViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.item_phone, parent, false));

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
                return new PhoneHeaderViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.item_phone, parent, false));
        }
    }
}
