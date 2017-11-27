package com.enoughspam.step.ui.intangible;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.afollestad.sectionedrecyclerview.ItemCoord;
import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.enoughspam.step.R;
import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.dao.DAOHandler;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.domain.UserPhone;
import com.enoughspam.step.domain.PhoneSection;
import com.enoughspam.step.ui.viewholder.PhoneHeaderViewHolder;
import com.enoughspam.step.util.Listeners;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 21/11/17
 * Time: 15:02
 */

public abstract class UsersNumbersAdapter extends SectionedRecyclerViewAdapter<SectionedViewHolder> {
    protected AsynchronousContentFragment mFragment;
    protected AbstractActivity mActivity;

    protected List<PhoneSection> mNumbersList;

    private Listeners.ListListener mListListener;
    private Listeners.AnswerListener mAnswerListener;

    public UsersNumbersAdapter(@NonNull final AsynchronousContentFragment fragment) {
        mNumbersList = new ArrayList<>();
        mFragment = fragment;
        mActivity = (AbstractActivity) fragment.getActivity();
    }

    protected Listeners.ListListener getListListener() {
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
                    for (i = 0; i < mNumbersList.size(); i++) {
                        final PhoneSection phoneSection = mNumbersList.get(i);
                        if (phoneSection.getUsername().equals(newPhoneSectionUsername)) {
                            phoneSection.addPhone(userPhone.getPhone(null));
                            break;
                        }
                    }

                    if (i == mNumbersList.size()) {    // user isn't in list
                        final ArrayList<Phone> newPhoneList = new ArrayList<>();
                        newPhoneList.add(userPhone.getPhone(null));

                        mNumbersList.add(new PhoneSection(
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

    protected Listeners.AnswerListener getAnswerListener() {
        if (mAnswerListener == null) {
            mAnswerListener = new Listeners.AnswerListener() {
                @Override
                public void onAnswerRetrieved() {
                    if (mNumbersList.isEmpty()) {
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

    public void removeItem(@NonNegative @NonNull final Integer absolutePosition) {
        final ItemCoord coord = getRelativePosition(absolutePosition);
        final List<Phone> phoneList = mNumbersList.get(coord.section()).getPhoneList();

        final Resources resources = mFragment.getContext().getResources();

        // store information for undo case
        Phone removedPhone = phoneList.get(coord.relativePos());
        PhoneSection removedPhoneSection = mNumbersList.get(coord.section());

        phoneList.remove(phoneList.get(coord.relativePos()));
        notifyItemRemoved(absolutePosition);

        // remove header when there's no item
        if (phoneList.isEmpty()) {
            mNumbersList.remove(coord.section());
            notifyItemRemoved(absolutePosition - 1);

            Integer i;
            for (i = 0; i < mNumbersList.size(); i++) {
                if (!mNumbersList.get(i).getPhoneList().isEmpty()) {
                    break;
                }
            }

            if (i == mNumbersList.size()) mFragment.showPlaceHolder();
        }

        Snackbar.make(mFragment.getView(), resources.getString(R.string.removed_number), Snackbar.LENGTH_LONG)
                .setAction(resources.getString(R.string.undo), view -> {
                    // add items to adapter and list again
                    mFragment.showRecyclerView();
                    phoneList.add(coord.relativePos(), removedPhone);
                    notifyItemInserted(absolutePosition);

                    if (!mNumbersList.contains(removedPhoneSection)) {
                        mNumbersList.add(coord.section(), removedPhoneSection);
                        notifyItemInserted(absolutePosition - 1);
                    }
                })
                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        if (event != DISMISS_EVENT_ACTION) {    // if equals, it was manually dismissed
                            // finally remove from database
                            removeItemFromDatabase(removedPhone.getKey());
                        }
                    }
                })
                .show();
    }

    protected abstract void removeItemFromDatabase(@NonNull final String phoneKey);

    @Override
    public int getSectionCount() {
        return mNumbersList.size();
    }

    @Override
    public int getItemCount(int section) {
        return mNumbersList.get(section).getPhoneList().size();
    }

    @Override
    public void onBindHeaderViewHolder(SectionedViewHolder holder, int section, boolean expanded) {
        ((PhoneHeaderViewHolder) holder).mBlockerOrNumber
                .setText(mNumbersList.get(section).getUsername());
    }

    @Override public void onBindFooterViewHolder(SectionedViewHolder holder, int section) {}

    @Override
    public void onBindViewHolder(SectionedViewHolder holder, int section, int relativePosition, int absolutePosition) {
        final PhoneHeaderViewHolder viewHolder = (PhoneHeaderViewHolder) holder;

        final Phone phone = mNumbersList.get(section).getPhoneList().get(relativePosition);

        final int countryCode = phone.getArea().getState().getCountry().getCode();
        final int areaCode = phone.getArea().getCode();
        final long number = phone.getNumber();

        final StringBuilder formattedNumber = new StringBuilder(50);
        formattedNumber.append("+" + countryCode);
        formattedNumber.append(" " + areaCode);
        formattedNumber.append(" " + number);

        viewHolder.mBlockerOrNumber.setText(formattedNumber);

        viewHolder.mIsSwipeable = isSwipeable();
        viewHolder.mParent.setOnClickListener(view ->
                onClick(mNumbersList.get(section), viewHolder));
        viewHolder.mParent.setOnLongClickListener(view ->
                onLongClick(mNumbersList.get(section), viewHolder));
    }

    protected abstract Boolean isSwipeable();

    protected abstract void onClick(@NonNull final PhoneSection phoneSection,
                                    @NonNull final PhoneHeaderViewHolder viewHolder);

    protected abstract boolean onLongClick(@NonNull final PhoneSection phoneSection,
                                           @NonNull final PhoneHeaderViewHolder viewHolder);

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

            default:
                return new PhoneHeaderViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.item_phone, parent, false));
        }
    }
}
