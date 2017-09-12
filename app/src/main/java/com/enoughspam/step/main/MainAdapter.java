package com.enoughspam.step.main;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.aesthetic.AestheticCardView;
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

public class MainAdapter extends SectionedRecyclerViewAdapter<MainAdapter.MyViewHolder> {

    private final List<PhoneSection> mBlockedNumbersList;
    private final View mView;

    public MainAdapter(@NonNull final List<PhoneSection> blockedNumbersList,
                       @NonNull final View view) {
        mBlockedNumbersList = blockedNumbersList;
        mView = view;
    }

    public void removeItem(@NonNegative final int absolutePosition) {
        final ItemCoord coord = getRelativePosition(absolutePosition);
        final List<Phone> phoneList = mBlockedNumbersList.get(coord.section()).getPhoneList();

        final Resources resources = mView.getContext().getResources();

        // store information for undo case
        Phone removedPhone = phoneList.get(coord.relativePos());
        PhoneSection removedPhoneSection = mBlockedNumbersList.get(coord.section());

        phoneList.remove(phoneList.get(coord.relativePos()));
        notifyItemRemoved(absolutePosition);

        // remove header when there's no item
        if (phoneList.isEmpty()) {
            mBlockedNumbersList.remove(coord.section());
            notifyItemRemoved(absolutePosition - 1);
        }

        Snackbar.make(mView, resources.getString(R.string.removed_number), Snackbar.LENGTH_SHORT)
                .setAction(resources.getString(R.string.undo), view -> {
                    // add items to adapter and list again
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
                            UserPhoneDAO.delete(LUserDAO.getThisUser().getID(), removedPhone.getID());
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

    List<AestheticTextView> headerList = new ArrayList<>();
    @Override
    public void onBindHeaderViewHolder(MyViewHolder holder, int section, boolean expanded) {
        holder.mBlockerOrNumber.setText(mBlockedNumbersList.get(section).getUserName());
        holder.mIsSwipeable = false;
        headerList.add(holder.mBlockerOrNumber);
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

        return new MyViewHolder(view);
    }

    private void showBottomSheet() {

    }

    public static class MyViewHolder extends SectionedViewHolder {
        final AestheticCardView mCardView;
        final AestheticTextView mBlockerOrNumber;
        public boolean mIsSwipeable;

        public MyViewHolder(View itemView) {
            super(itemView);
            mCardView = (AestheticCardView) itemView.findViewById(R.id.main_item_number_card);
            mBlockerOrNumber = (AestheticTextView) itemView.findViewById(android.R.id.title);
            mIsSwipeable = true;
        }
    }
}
