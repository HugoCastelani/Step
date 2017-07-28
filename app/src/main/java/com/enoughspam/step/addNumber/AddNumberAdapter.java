package com.enoughspam.step.addNumber;

import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.aesthetic.AestheticTextView;
import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.enoughspam.step.R;
import com.enoughspam.step.domain.Call;
import com.enoughspam.step.numberForm.NumberFormFragment;

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

    public AddNumberAdapter(@NonNull final AddNumberFragment fragment) {
        Uri calls = Uri.parse("content://call_log/calls");
        Cursor cursor = fragment.getContext().getContentResolver().query(calls, null, null, null, null);

        mCallList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();

        while (cursor.moveToNext()) {
            Call call = new Call(
                    cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)),
                    cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))
            );

            if (!nameList.contains(call.getName())) {
                mCallList.add(call);
                nameList.add(call.getName());
            }
        }

        mFragment = fragment;
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
        final AestheticTextView view = ((OrdinaryViewHolder) holder).mSectionOrNumber;

        if (section == 0) {
            view.setText(view.getContext().getResources().getString(R.string.add_manually));
        } else {
            view.setText(view.getContext().getResources().getString(R.string.add_call_history));
        }
    }

    @Override
    public void onBindFooterViewHolder(SectionedViewHolder holder, int section) {}

    @Override
    public void onBindViewHolder(SectionedViewHolder holder, int section, int relativePosition, int absolutePosition) {
        if (holder instanceof OrdinaryViewHolder) {
            ((OrdinaryViewHolder) holder).mSectionOrNumber
                    .setText("" + mCallList.get(relativePosition).getNumber());
        }
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
        final AestheticTextView mSectionOrNumber;

        public OrdinaryViewHolder(View itemView) {
            super(itemView);
            mSectionOrNumber = (AestheticTextView) itemView.findViewById(android.R.id.title);
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
