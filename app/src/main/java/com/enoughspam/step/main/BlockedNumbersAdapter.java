package com.enoughspam.step.main;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.aesthetic.AestheticTextView;
import com.afollestad.sectionedrecyclerview.ItemCoord;
import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.enoughspam.step.R;
import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.localDao.LUserDAO;
import com.enoughspam.step.database.wideDao.UserPhoneDAO;
import com.enoughspam.step.domain.PhoneSection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 01/04/17
 * Time: 23:05
 */

public class BlockedNumbersAdapter extends SectionedRecyclerViewAdapter<BlockedNumbersAdapter.MyViewHolder> {

    private final List<PhoneSection> mBlockedNumbersList;

    public BlockedNumbersAdapter(@NonNull final List<PhoneSection> blockedNumbersList) {
        mBlockedNumbersList = blockedNumbersList;
    }

    public void removeItem(@NonNegative final int absolutePosition) {
        final ItemCoord coord = getRelativePosition(absolutePosition);
        final List<Phone> phoneList = mBlockedNumbersList.get(coord.section()).getPhoneList();

        UserPhoneDAO.delete(LUserDAO.getThisUser().getId(), phoneList.get(coord.relativePos()).getId());
        phoneList.remove(phoneList.get(coord.relativePos()));

        // swipe header when there's no item
        if (phoneList.isEmpty()) {
            mBlockedNumbersList.remove(coord.section());
            notifyItemRemoved(absolutePosition - 1);
        }

        notifyItemRemoved(absolutePosition);
    }

    @Override
    public int getSectionCount() {
        return mBlockedNumbersList.size();
    }

    @Override
    public int getItemCount(int section) {
        return mBlockedNumbersList.get(section).getPhoneList().size();
    }

    List<AestheticTextView> headerList = new ArrayList<>();
    @Override
    public void onBindHeaderViewHolder(MyViewHolder holder, int section, boolean expanded) {
        holder.blocker_or_number.setText(mBlockedNumbersList.get(section).getUserName());
        holder.isSwipeable = false;
        headerList.add(holder.blocker_or_number);
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

        holder.blocker_or_number.setText(formattedNumber);
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

        return new MyViewHolder(view);
    }

    public static class MyViewHolder extends SectionedViewHolder {
        final AestheticTextView blocker_or_number;
        public boolean isSwipeable;

        public MyViewHolder(View itemView) {
            super(itemView);
            blocker_or_number = (AestheticTextView) itemView.findViewById(android.R.id.title);
            isSwipeable = true;
        }
    }
}
