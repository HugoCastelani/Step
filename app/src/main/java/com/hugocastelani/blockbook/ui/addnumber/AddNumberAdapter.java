package com.hugocastelani.blockbook.ui.addnumber;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.afollestad.aesthetic.AestheticTextView;
import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.hugocastelani.blockbook.R;
import com.hugocastelani.blockbook.database.domain.Phone;
import com.hugocastelani.blockbook.domain.Call;
import com.hugocastelani.blockbook.ui.viewholder.NumberFormViewHolder;
import com.hugocastelani.blockbook.ui.viewholder.PhoneContactViewHolder;
import com.hugocastelani.blockbook.util.Listeners;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 25/07/17
 * Time: 21:33
 */

public final class AddNumberAdapter extends SectionedRecyclerViewAdapter<SectionedViewHolder> {

    private static final int VIEW_TYPE_FORM = -4;

    private final List<Call> mCallList;
    private final AddNumberFragment mFragment;
    private final AddNumberActivity mActivity;

    public AddNumberAdapter(@NonNull final List<Call> callList,
                            @NonNull final AddNumberFragment fragment) {
        mCallList = callList;
        mFragment = fragment;
        mActivity = (AddNumberActivity) fragment.getActivity();
    }

    @Override
    public int getSectionCount() {
        return mCallList.isEmpty() ? 1 : 2;
    }

    @Override
    public int getItemCount(int section) {
        return section == 0 ? 1 : mCallList.size();
    }

    @Override
    public int getItemViewType(int section, int relativePosition, int absolutePosition) {
        switch (section) {
            case 0:
                return VIEW_TYPE_FORM;
            default:
                return VIEW_TYPE_ITEM;
        }
    }

    @Override
    public void onBindHeaderViewHolder(SectionedViewHolder holder, int section, boolean expanded) {
        final AestheticTextView view = ((PhoneContactViewHolder) holder).mSectionOrName;

        if (section == 0) {
            view.setText(view.getContext().getResources().getString(R.string.add_manually));
        } else {
            view.setText(view.getContext().getResources().getString(R.string.add_call_history));
        }
    }

    @Override
    public void onBindFooterViewHolder(SectionedViewHolder holder, int section) {}

    Boolean mSelectionMode = false;
    ArrayList<PhoneContactViewHolder> mSelectedViews = new ArrayList<>();

    @Override
    public void onBindViewHolder(SectionedViewHolder holder, int section, int relativePosition, int absolutePosition) {
        if (holder instanceof PhoneContactViewHolder) {
            final PhoneContactViewHolder viewHolder = ((PhoneContactViewHolder) holder);
            final Call call = mCallList.get(relativePosition);

            final StringBuilder formattedNumber = new StringBuilder(50);

            Integer countryCode;
            Integer areaCode;
            Long number;

            if (call.getPhone().getCountry() == null) {
                countryCode = call.getPhone().getArea().getState().getCountry().getCode();
                areaCode = call.getPhone().getArea().getCode();
                number = call.getPhone().getNumber();

                formattedNumber.append("+" + countryCode);
                formattedNumber.append(" " + areaCode);

            } else {

                countryCode = call.getPhone().getCountry().getCode();
                areaCode = -1;
                number = call.getPhone().getNumber();

                formattedNumber.append("+" + countryCode);
            }

            formattedNumber.append(" " + number);

            viewHolder.mSectionOrName.setText(call.getName());
            viewHolder.mNumber.setText(formattedNumber.toString());
            viewHolder.setSelected(call.isSelected());

            // actions to multiple items selection
            viewHolder.mParent.setOnLongClickListener(view -> {
                if (mSelectionMode) {
                    onClickMutualAction(call, viewHolder);

                } else {

                    mSelectionMode = true;
                    call.setSelected(true);
                    viewHolder.setSelected(true);
                    mSelectedViews.add(viewHolder);
                }

                mActivity.setSelectedItems(mSelectedViews.size());
                return true;
            });

            viewHolder.mParent.setOnClickListener(view -> {
                if (mSelectionMode) {
                    onClickMutualAction(call, viewHolder);
                    mActivity.setSelectedItems(mSelectedViews.size());

                } else {

                    mActivity.showNumberProgressDialog();

                    final Listeners.AnswerListener listener = new Listeners.AnswerListener() {
                        @Override
                        public void onAnswerRetrieved() {
                            mActivity.hideNumberProgressDialog();
                            mActivity.onBackPressed();
                        }

                        @Override
                        public void onError() {
                            mActivity.createSnackbarAndClose(R.string.adding_number_error).show();
                        }
                    };

                    if (areaCode != -1) {
                        mFragment.saveNumber(new Phone(number,
                                call.getPhone().getArea().getKey(), "-1"), listener);
                    } else {
                        mFragment.saveNumber(new Phone(number,
                                "-1", call.getPhone().getCountry().getKey()), listener);
                    }
                }
            });
        }
    }

    private void onClickMutualAction(@NonNull final Call call,
                                     @NonNull final PhoneContactViewHolder viewHolder) {
        if (call.isSelected()) {
            call.setSelected(false);
            viewHolder.setSelected(false);
            mSelectedViews.remove(viewHolder);

            if (mSelectedViews.size() == 0) {
                mSelectionMode = false;
            } else {
                mActivity.setSelectedItems(mSelectedViews.size());
            }


        } else {

            call.setSelected(true);
            viewHolder.setSelected(true);
            mSelectedViews.add(viewHolder);
        }
    }

    public void deselectAllViews() {
        for (final PhoneContactViewHolder viewHolder : mSelectedViews) {
            viewHolder.setSelected(false);
        }

        mSelectedViews.clear();
        mSelectionMode = false;

        for (final Call call : mCallList) {
            if (call.isSelected()) {
                call.setSelected(false);
            }
        }
    }

    public List<Call> getSelectedItems() {
        final List<Call> callList = new ArrayList<>();

        for (final Call call : mCallList) {
            if (call.isSelected()) {
                callList.add(call);
            }
        }

        return callList;
    }

    @Override
    public SectionedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new PhoneContactViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.preference_category, parent, false)
                );

            case VIEW_TYPE_FORM:
                return new NumberFormViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.item_number_form, parent, false), mFragment
                );

            default:
                return new PhoneContactViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.item_phone_contact, parent, false)
                );
        }
    }
}
