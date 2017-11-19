package com.enoughspam.step.main;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.sectionedrecyclerview.ItemCoord;
import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.enoughspam.step.R;
import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.dao.wide.UserPhoneDAO;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.domain.PhoneSection;
import com.enoughspam.step.util.Listeners;
import com.enoughspam.step.viewholder.SimplePhoneHeaderViewHolder;

import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 01/04/17
 * Time: 23:05
 */

public class MainAdapter extends SectionedRecyclerViewAdapter<SimplePhoneHeaderViewHolder> {

    private final List<PhoneSection> mBlockedNumbersList;
    private final MainFragment mFragment;

    public MainAdapter(@NonNull final List<PhoneSection> blockedNumbersList,
                       @NonNull final MainFragment fragment) {
        mBlockedNumbersList = blockedNumbersList;
        mFragment = fragment;
    }

    public void removeItem(@NonNegative final int absolutePosition) {
        final ItemCoord coord = getRelativePosition(absolutePosition);
        final List<Phone> phoneList = mBlockedNumbersList.get(coord.section()).getPhoneList();

        final Resources resources = mFragment.getContext().getResources();

        // store information for undo case
        Phone removedPhone = phoneList.get(coord.relativePos());
        PhoneSection removedPhoneSection = mBlockedNumbersList.get(coord.section());

        phoneList.remove(phoneList.get(coord.relativePos()));
        notifyItemRemoved(absolutePosition);

        // remove header when there's no item
        if (phoneList.isEmpty()) {
            mBlockedNumbersList.remove(coord.section());
            notifyItemRemoved(absolutePosition - 1);

            Integer i;
            for (i = 0; i < mBlockedNumbersList.size(); i++) {
                if (!mBlockedNumbersList.get(i).getPhoneList().isEmpty()) {
                    break;
                }
            }

            if (i == mBlockedNumbersList.size()) mFragment.showPlaceHolder();
        }

        Snackbar.make(mFragment.getView(), resources.getString(R.string.removed_number), Snackbar.LENGTH_LONG)
                .setAction(resources.getString(R.string.undo), view -> {
                    // add items to adapter and list again
                    mFragment.showRecyclerView();
                    phoneList.add(coord.relativePos(), removedPhone);
                    notifyItemInserted(absolutePosition);

                    if (!mBlockedNumbersList.contains(removedPhoneSection)) {
                        mBlockedNumbersList.add(coord.section(), removedPhoneSection);
                        notifyItemInserted(absolutePosition - 1);
                    }
                })
                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        if (event != DISMISS_EVENT_ACTION) {    // if equals, it has just undone
                            // finally remove from database
                            UserPhoneDAO.get().delete(removedPhone.getKey(), new Listeners.AnswerListener() {
                                @Override public void onAnswerRetrieved() {}
                                @Override public void onError() {}
                            });
                        }
                    }
                })
                .show();
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
    public void onBindHeaderViewHolder(SimplePhoneHeaderViewHolder holder, int section, boolean expanded) {
        holder.mBlockerOrNumber.setText(mBlockedNumbersList.get(section).getUsername());
    }

    @Override
    public void onBindFooterViewHolder(SimplePhoneHeaderViewHolder holder, int section) {}

    @Override
    public void onBindViewHolder(SimplePhoneHeaderViewHolder holder, int section, int relativePosition, int absolutePosition) {
        final Phone phone = mBlockedNumbersList.get(section).getPhoneList().get(relativePosition);

        final StringBuilder formattedNumber = new StringBuilder(50);

        int countryCode;
        int areaCode;
        long number;

        if (phone.getCountry() == null) {
            countryCode = phone.getArea().getState().getCountry().getCode();
            areaCode = phone.getArea().getCode();
            number = phone.getNumber();

            formattedNumber.append("+" + countryCode);
            formattedNumber.append(" " + areaCode);

        } else {

            countryCode = phone.getCountry().getCode();
            number = phone.getNumber();

            formattedNumber.append("+" + countryCode);
        }

        formattedNumber.append(" " + number);

        holder.mIsSwipeable = true;
        holder.mBlockerOrNumber.setText(formattedNumber);

        holder.mCardView.setOnLongClickListener(view -> {
            showBottomSheet();
            return true;
        });
    }

    @Override
    public SimplePhoneHeaderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int layout;
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                layout = R.layout.main_item_number;
                break;
            case VIEW_TYPE_HEADER:
                layout = R.layout.preference_category;
                break;
            default:
                layout = R.layout.main_item_number;
                break;
        }

        final View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        return new SimplePhoneHeaderViewHolder(view);
    }

    private void showBottomSheet() {
    }
}
