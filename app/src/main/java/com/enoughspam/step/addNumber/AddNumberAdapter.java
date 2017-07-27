package com.enoughspam.step.addNumber;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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

public class AddNumberAdapter extends SectionedRecyclerViewAdapter<AddNumberAdapter.MyViewHolder> {

    private final List<Call> mCallList;
    private final AddNumberFragment mFragment;

    public AddNumberAdapter(@NonNull final AddNumberFragment fragment) {
        mCallList = new ArrayList<>();
        mCallList.add(new Call("Ana", "+55 31 98682 0544"));

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
    public void onBindHeaderViewHolder(MyViewHolder holder, int section, boolean expanded) {
        final AestheticTextView view = holder.mSectionOrNumber;

        if (section == 0) {
            view.setText(view.getContext().getResources().getString(R.string.add_manually));
        } else {
            view.setText(view.getContext().getResources().getString(R.string.add_call_history));
        }
    }

    @Override
    public void onBindFooterViewHolder(MyViewHolder holder, int section) {}

    @Override
    public void onBindViewHolder(MyViewHolder holder, int section, int relativePosition, int absolutePosition) {
        if (section == 0) {
            holder.mSectionOrNumber.setVisibility(View.GONE);
            initFragment();

        } else {

            holder.mSectionOrNumber.setText("" + mCallList.get(relativePosition).getNumber());
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int layout;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                layout = R.layout.preference_category;
                break;
            default:
                layout = R.layout.add_number_item_call;
                break;
        }

        final View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        return new MyViewHolder(view);
    }

    private void initFragment() {
        NumberFormFragment numberFormFragment = (NumberFormFragment) mFragment.getChildFragmentManager()
                .findFragmentByTag("numberFormFragmentTag");
        if (numberFormFragment == null) {
            numberFormFragment = new NumberFormFragment();
            final FragmentTransaction fragmentTransaction = mFragment.getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.number_form_fragment_container, numberFormFragment, "numberFormFragmentTag");
            fragmentTransaction.commit();
        }
    }

    protected static class MyViewHolder extends SectionedViewHolder {
        final AestheticTextView mSectionOrNumber;
        final LinearLayout mNumberForm;

        public MyViewHolder(View itemView) {
            super(itemView);
            mSectionOrNumber = (AestheticTextView) itemView.findViewById(android.R.id.title);
            mNumberForm = (LinearLayout) itemView.findViewById(R.id.number_form_fragment_container);
        }
    }
}
