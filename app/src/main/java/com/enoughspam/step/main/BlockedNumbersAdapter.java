package com.enoughspam.step.main;

// Created by hugo on 01/04/17, 23:05

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.aesthetic.AestheticCardView;
import com.afollestad.aesthetic.AestheticTextView;
import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.enoughspam.step.R;

import java.util.ArrayList;

public class BlockedNumbersAdapter extends SectionedRecyclerViewAdapter<BlockedNumbersAdapter.MyViewHolder> {

    private ArrayList<ArrayList<String>> blockedNumbersList;

    public BlockedNumbersAdapter(ArrayList<ArrayList<String>> blockedNumbersList, Context context) {
        this.blockedNumbersList = blockedNumbersList;
    }

    @Override
    public int getSectionCount() {
        return blockedNumbersList.size();
    }

    @Override
    public int getItemCount(int section) {
        return blockedNumbersList.get(section).size() - 1;      // -1 porque a primeira posição grava o nome da sessão
    }

    @Override
    public void onBindHeaderViewHolder(MyViewHolder holder, int section, boolean expanded) {
        holder.blocker_or_number.setText(blockedNumbersList.get(section).get(0));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int section, int relativePosition, int absolutePosition) {
        holder.blocker_or_number.setText(blockedNumbersList.get(section).get(relativePosition + 1));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                layout = R.layout.preference_category;
                break;
            case VIEW_TYPE_ITEM:
                layout = R.layout.item_number;
                break;
            default:
                layout = R.layout.item_number;
                break;
        }

        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);

        return new MyViewHolder(view);
    }

    public static class MyViewHolder extends SectionedViewHolder {
        AestheticTextView blocker_or_number;
        AestheticCardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            blocker_or_number = (AestheticTextView) itemView.findViewById(android.R.id.title);
            cardView = (AestheticCardView) itemView.findViewById(R.id.item_number_cardview);
        }
    }
}
