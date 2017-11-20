package com.enoughspam.step.ui.profile;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.afollestad.aesthetic.AestheticButton;
import com.afollestad.aesthetic.AestheticTextView;
import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.blankj.utilcode.util.ScreenUtils;
import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.DAOHandler;
import com.enoughspam.step.database.dao.wide.UserFollowerDAO;
import com.enoughspam.step.database.dao.wide.UserPhoneDAO;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.database.domain.UserPhone;
import com.enoughspam.step.domain.PhoneSection;
import com.enoughspam.step.ui.viewholder.PhoneHeaderViewHolder;
import com.enoughspam.step.ui.viewholder.ToolbarViewHolder;
import com.enoughspam.step.util.Listeners;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 14/10/17
 * Time: 20:34
 */

public final class ProfileAdapter extends SectionedRecyclerViewAdapter<SectionedViewHolder> {
    private static final int VIEW_TYPE_TOOLBAR = -4;

    private ProfileFragment mFragment;
    private ProfileActivity mActivity;

    private User mUser;
    private Boolean isFollowing;

    private List<PhoneSection> mBlockedNumbersList;

    private Listeners.ListListener mListListener;
    private Listeners.AnswerListener mAnswerListener;

    public ProfileAdapter(@NonNull final User user, @NonNull final ProfileFragment fragment) {
        mBlockedNumbersList = new ArrayList<>();
        mUser = user;
        mFragment = fragment;
        mActivity = (ProfileActivity) fragment.getActivity();

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

    private Listeners.ListListener getListListener() {
        if (mListListener == null) {
            mListListener = new Listeners.ListListener<UserPhone>() {
                final String sPrefix = DAOHandler.getContext().getResources().getString(R.string.numbers_prefix);
                final String sSuffix = DAOHandler.getContext().getResources().getString(R.string.numbers_suffix);

                @Override
                public void onItemAdded(@NonNull UserPhone userPhone) {
                    String newPhoneSectionUsername;
                    if (sPrefix.isEmpty()) {
                        newPhoneSectionUsername = userPhone.getUser(null).getUsername() + sSuffix;
                    } else {
                        newPhoneSectionUsername = sPrefix + userPhone.getUser(null).getUsername();
                    }

                    int i;
                    for (i = 0; i < mBlockedNumbersList.size(); i++) {
                        final PhoneSection phoneSection = mBlockedNumbersList.get(i);
                        if (phoneSection.getUsername().equals(newPhoneSectionUsername)) {
                            phoneSection.addPhone(userPhone.getPhone(null));
                            break;
                        }
                    }

                    if (i == mBlockedNumbersList.size()) {    // user isn't in list
                        final ArrayList<Phone> newPhoneList = new ArrayList<>();
                        newPhoneList.add(userPhone.getPhone(null));

                        mBlockedNumbersList.add(new PhoneSection(
                                newPhoneSectionUsername,
                                newPhoneList
                        ));
                    }
                }

                @Override public void onItemRemoved(@NonNull UserPhone userPhone) {}
            };
        }

        return mListListener;
    }

    private Listeners.AnswerListener getAnswerListener() {
        if (mAnswerListener == null) {
            mAnswerListener = new Listeners.AnswerListener() {
                @Override
                public void onAnswerRetrieved() {
                    if (mBlockedNumbersList.isEmpty()) {
                        mFragment.showPlaceHolder();
                    } else {
                        mFragment.showRecyclerView();
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

    @Override
    public int getSectionCount() {
        return mBlockedNumbersList.size() + 1;
    }

    @Override
    public int getItemCount(int section) {
        return section == 0 ? 1 : mBlockedNumbersList.get(section - 1).getPhoneList().size();
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
                    mBlockedNumbersList.get(section - 1).getUsername()
            );
        }
    }

    @Override
    public void onBindFooterViewHolder(SectionedViewHolder holder, int section) {}

    @Override
    public void onBindViewHolder(SectionedViewHolder holder, int section, int relativePosition, int absolutePosition) {
        if (holder instanceof PhoneHeaderViewHolder) {
            final PhoneHeaderViewHolder viewHolder = (PhoneHeaderViewHolder) holder;

            final Phone phone = mBlockedNumbersList.get(section - 1).getPhoneList().get(relativePosition);

            final int countryCode = phone.getArea().getState().getCountry().getCode();
            final int areaCode = phone.getArea().getCode();
            final long number = phone.getNumber();

            final StringBuilder formattedNumber = new StringBuilder(50);
            formattedNumber.append("+" + countryCode);
            formattedNumber.append(" " + areaCode);
            formattedNumber.append(" " + number);

            viewHolder.mBlockerOrNumber.setText(formattedNumber);

            viewHolder.mIsSwipeable = false;

            viewHolder.mCardView.setOnLongClickListener(view -> {
                showBottomSheet();
                return true;
            });

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
                    mActivity.createSnackbar(R.string.profile_error_user_pic).show();
                }
            });

            // init button actions
            if (isFollowing) {
                setButtonAsRemovable(viewHolder.mButton);
            } else {
                setButtonAsAddable(viewHolder.mButton);
            }
        }
    }

    private void setButtonAsAddable(@NonNull final AestheticButton button) {
        button.setText(mFragment.getResources().getString(R.string.profile_follow));
        button.setOnClickListener(view -> {
            mFragment.showAddingProgressDialog();

            UserFollowerDAO.get().create(mUser, new Listeners.AnswerListener() {
                @Override
                public void onAnswerRetrieved() {
                    setButtonAsRemovable(button);
                    mFragment.hideAddingProgressDialog();
                }

                @Override
                public void onError() {
                    mFragment.hideAddingProgressDialog();
                    mActivity.createSnackbar(R.string.profile_error_add_user).show();
                }
            });
        });
    }

    private void setButtonAsRemovable(@NonNull final AestheticButton button) {
        button.setText(mFragment.getResources().getString(R.string.profile_unfollow));
        button.setOnClickListener(view -> {
            mFragment.showRemovingProgressDialog();

            final Listeners.AnswerListener answerListener = new Listeners.AnswerListener() {
                Integer count = 0;

                @Override
                public void onAnswerRetrieved() {
                    if (++count == 2) {
                        setButtonAsAddable(button);
                        mFragment.hideRemovingProgressDialog();
                    }
                }

                @Override
                public void onError() {
                    mFragment.hideRemovingProgressDialog();
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

            case VIEW_TYPE_HEADER:
                return new PhoneHeaderViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.preference_category, parent, false));

            default:
                return new PhoneHeaderViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.item_phone, parent, false));
        }
    }

    private void showBottomSheet() {
    }
}
