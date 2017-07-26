package com.enoughspam.step.addNumber;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.afollestad.aesthetic.AestheticTextView;
import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.enoughspam.step.R;
import com.enoughspam.step.domain.Call;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 25/07/17
 * Time: 21:33
 */

public class AddNumberAdapter extends SectionedRecyclerViewAdapter<AddNumberAdapter.MyViewHolder> {

    private static final int SECTIONS_AMOUNT = 2;
    private final List<Call> mCallList;

    public AddNumberAdapter() {
        mCallList = new ArrayList<>();
        mCallList.add(new Call("Ana", "+55 31 98682 0544"));
        mCallList.add(new Call("Ana", "+55 31 98682 0544"));
        mCallList.add(new Call("Ana", "+55 31 98682 0544"));
        mCallList.add(new Call("Ana", "+55 31 98682 0544"));
        mCallList.add(new Call("Ana", "+55 31 98682 0544"));
        mCallList.add(new Call("Ana", "+55 31 98682 0544"));
    }

    @Override
    public int getSectionCount() {
        return SECTIONS_AMOUNT;
    }

    @Override
    public int getItemCount(int section) {
        if (section == 0) return 1;
        else return mCallList.size();
    }

    @Override
    public void onBindHeaderViewHolder(MyViewHolder holder, int section, boolean expanded) {
        if (section == 0) {
            holder.section_or_number.setText("Adicionar manualmente");
        } else {
            holder.section_or_number.setText("Adicionar a partir do histórico de chamadas");
        }
    }

    @Override
    public void onBindFooterViewHolder(MyViewHolder holder, int section) {}

    @Override
    public void onBindViewHolder(MyViewHolder holder, int section, int relativePosition, int absolutePosition) {
        if (section == 0) {
            holder.section_or_number.setVisibility(View.GONE);
            holder.form.setLayoutResource(R.layout.number_form_layout);

            View layout = holder.form.inflate();

        } else {

            holder.section_or_number.setText("" + mCallList.get(relativePosition).getNumber());
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

    protected static class MyViewHolder extends SectionedViewHolder {
        final AestheticTextView section_or_number;
        final ViewStub form;

        public MyViewHolder(View itemView) {
            super(itemView);
            section_or_number = (AestheticTextView) itemView.findViewById(android.R.id.title);
            form = (ViewStub) itemView.findViewById(R.id.add_number_form);
        }
    }
}
