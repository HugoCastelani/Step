package com.enoughspam.step.addnumber;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.afollestad.aesthetic.AestheticCardView;
import com.afollestad.aesthetic.AestheticTextView;
import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.enoughspam.step.R;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.domain.Call;
import com.enoughspam.step.singlefragments.NumberFormFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 25/07/17
 * Time: 21:33
 */

public class AddNumberAdapter extends SectionedRecyclerViewAdapter<SectionedViewHolder> {

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
        final AestheticTextView view = ((OrdinaryViewHolder) holder).mSectionOrName;

        if (section == 0) {
            view.setText(view.getContext().getResources().getString(R.string.add_manually));
        } else {
            view.setText(view.getContext().getResources().getString(R.string.add_call_history));
        }
    }

    @Override
    public void onBindFooterViewHolder(SectionedViewHolder holder, int section) {}

    boolean mSelectionMode = false;
    int mSelectedViews = 0;

    private final List<OrdinaryViewHolder> mItemList = new ArrayList<>();

    @Override
    public void onBindViewHolder(SectionedViewHolder holder, int section, int relativePosition, int absolutePosition) {
        if (holder instanceof OrdinaryViewHolder) {
            final OrdinaryViewHolder aViewHolder = ((OrdinaryViewHolder) holder);
            final Call call = mCallList.get(relativePosition);

            mItemList.add(aViewHolder);

            final StringBuilder formattedNumber = new StringBuilder(50);

            int countryCode;
            int areaCode;
            long number;

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

            aViewHolder.mSectionOrName.setText(call.getName());
            aViewHolder.mNumber.setText(formattedNumber.toString());

            // actions to multiple items selection
            aViewHolder.mCardView.setOnLongClickListener(view -> {
                if (mSelectionMode) {
                    if (aViewHolder.mParent.isSelected()) {
                        if (mSelectedViews == 1) {
                            mSelectionMode = false;
                            mActivity.hideFAB();
                        }
                        aViewHolder.mParent.setSelected(false);
                        mSelectedViews--;

                    } else {

                        aViewHolder.mParent.setSelected(true);
                        mSelectedViews++;
                    }

                } else {

                    mSelectionMode = true;
                    aViewHolder.mParent.setSelected(true);
                    mSelectedViews++;
                    mActivity.showFAB();
                }

                return true;
            });

            // actions to single item
            aViewHolder.mCardView.setOnClickListener(view -> {
                if (mSelectionMode) {
                    if (aViewHolder.mParent.isSelected()) {
                        if (mSelectedViews == 1) {
                            mSelectionMode = false;
                            mActivity.hideFAB();
                        }
                        aViewHolder.mParent.setSelected(false);
                        mSelectedViews--;

                    } else {

                        aViewHolder.mParent.setSelected(true);
                        mSelectedViews++;
                    }

                } else {

                    mActivity.showProgressDialog();
                    if (areaCode != -1) {
                        mFragment.saveNumber(new Phone(number, call.getPhone().getArea().getKey(), "-1"));
                    } else {
                        mFragment.saveNumber(new Phone(number, "-1", call.getPhone().getCountry().getKey()));
                    }
                }
            });
        }
    }

    public List<Call> getSelectedItems() {
        final List<Call> callList = new ArrayList<>();

        for (int i = 0; i < mItemList.size(); i++) {
            if (mItemList.get(i).mParent.isSelected()) {
                callList.add(mCallList.get(i));
            }
        }

        return callList;
    }

    @Override
    public SectionedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new OrdinaryViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.preference_category, parent, false)
                );

            case VIEW_TYPE_FORM:
                return new FormViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.add_number_item_form, parent, false), mFragment
                );

            default:
                return new OrdinaryViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.add_number_item_call, parent, false)
                );
        }
    }

    protected static class OrdinaryViewHolder extends SectionedViewHolder {
        final AestheticCardView mCardView;
        final LinearLayout mParent;
        final AestheticTextView mSectionOrName;
        final AestheticTextView mNumber;

        public OrdinaryViewHolder(View itemView) {
            super(itemView);
            mCardView = (AestheticCardView) itemView.findViewById(R.id.add_number_item_call_card);
            mParent = (LinearLayout) itemView.findViewById(R.id.add_number_item_call_parent);
            mSectionOrName = (AestheticTextView) itemView.findViewById(android.R.id.title);
            mNumber = (AestheticTextView) itemView.findViewById(android.R.id.summary);
        }
    }

    protected static class FormViewHolder extends SectionedViewHolder {

        public FormViewHolder(View itemView, Fragment fragment) {
            super(itemView);

            NumberFormFragment numberFormFragment = (NumberFormFragment) fragment.getChildFragmentManager()
                    .findFragmentByTag("numberFormFragmentTag");

            if (numberFormFragment == null) {
                numberFormFragment = new NumberFormFragment();
                final FragmentTransaction fragmentTransaction = fragment.getChildFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.number_form_fragment_container, numberFormFragment, "numberFormFragmentTag");
                fragmentTransaction.commit();
            }
        }
    }
}
