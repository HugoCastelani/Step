package com.enoughspam.step.profile;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.aesthetic.AestheticCardView;
import com.afollestad.aesthetic.AestheticTextView;
import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.DAOHandler;
import com.enoughspam.step.database.dao.wide.UserPhoneDAO;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.database.domain.UserPhone;
import com.enoughspam.step.domain.PhoneSection;
import com.enoughspam.step.util.Listeners;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 14/10/17
 * Time: 20:34
 */

public class ProfileAdapter extends SectionedRecyclerViewAdapter<ProfileAdapter.MyViewHolder> {
    private final List<PhoneSection> mBlockedNumbersList;

    private Listeners.ListListener mListListener;
    private Listeners.AnswerListener mAnswerListener;

    public ProfileAdapter(@NonNull final User user) {
        mBlockedNumbersList = new ArrayList<>();

        UserPhoneDAO.get().getUserPhoneList(user.getKey(), getListListener(), getAnswerListener());
    }

    private Listeners.ListListener getListListener() {
        if (mListListener == null) {
            mListListener = new Listeners.ListListener<UserPhone>() {
                final String sPrefix = DAOHandler.getContext().getResources().getString(R.string.numbers_of);

                @Override
                public void onItemAdded(@NonNull UserPhone userPhone) {
                    final String newPhoneSectionUsername = sPrefix + userPhone.getUser(null).getUsername();

                    int i;
                    for (i = 0; i < mBlockedNumbersList.size(); i++) {
                        final PhoneSection phoneSection = mBlockedNumbersList.get(i);
                        if (phoneSection.getUsername().equals(newPhoneSectionUsername)) {
                            phoneSection.addPhone(userPhone.getPhone(null));
                            break;
                        }
                    }

                    if (i == mBlockedNumbersList.size()) {    // user isn't in list
                        final List<Phone> newPhoneList = new ArrayList<>();
                        newPhoneList.add(userPhone.getPhone(null));

                        mBlockedNumbersList.add(new PhoneSection(
                                newPhoneSectionUsername,
                                newPhoneList
                        ));
                    }

                    notifyDataSetChanged();
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
                    // hide progressbardialog and show rv
                }

                @Override
                public void onError() {
                    // show snackbar with error and close activity
                }
            };
        }

        return mAnswerListener;
    }

    @Override
    public int getSectionCount() {
        return mBlockedNumbersList.size();
    }

    @Override
    public int getItemCount(int section) {
        return mBlockedNumbersList.get(section).getPhoneList().size();
    }

    @Override
    public void onBindHeaderViewHolder(ProfileAdapter.MyViewHolder holder, int section, boolean expanded) {
        holder.mBlockerOrNumber.setText(mBlockedNumbersList.get(section).getUsername());
    }

    @Override
    public void onBindFooterViewHolder(MyViewHolder holder, int section) {}

    @Override
    public void onBindViewHolder(MyViewHolder holder, int section, int relativePosition, int absolutePosition) {
        final Phone phone = mBlockedNumbersList.get(section).getPhoneList().get(relativePosition);

        final int countryCode = phone.getArea().getState().getCountry().getCode();
        final int areaCode = phone.getArea().getCode();
        final long number = phone.getNumber();

        final StringBuilder formattedNumber = new StringBuilder(50);
        formattedNumber.append("+" + countryCode);
        formattedNumber.append(" " + areaCode);
        formattedNumber.append(" " + number);

        holder.mBlockerOrNumber.setText(formattedNumber);

        holder.mCardView.setOnLongClickListener(view -> {
            showBottomSheet();
            return true;
        });
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int layout;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                layout = R.layout.preference_category;
                break;
            case VIEW_TYPE_ITEM:
                layout = R.layout.main_item_number;
                break;
            default:
                layout = R.layout.main_item_number;
                break;
        }

        final View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        return new ProfileAdapter.MyViewHolder(view);
    }

    private void showBottomSheet() {
    }

    protected static class MyViewHolder extends SectionedViewHolder {
        final AestheticCardView mCardView;
        final AestheticTextView mBlockerOrNumber;

        public MyViewHolder(View itemView) {
            super(itemView);
            mCardView = (AestheticCardView) itemView.findViewById(R.id.main_item_number_card);
            mBlockerOrNumber = (AestheticTextView) itemView.findViewById(android.R.id.title);
        }
    }
}
