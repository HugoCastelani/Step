package com.enoughspam.step.main;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.aesthetic.AestheticCardView;
import com.afollestad.aesthetic.AestheticTextView;
import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.enoughspam.step.R;

import java.util.ArrayList;

/**
 * Created by Hugo Castelani
 * Date: 01/04/17
 * Time: 23:05
 */

public class BlockedNumbersAdapter extends SectionedRecyclerViewAdapter<BlockedNumbersAdapter.MyViewHolder> {

    private ArrayList<ArrayList<String>> mBlockedNumbersList;

    public BlockedNumbersAdapter(@NonNull final ArrayList<ArrayList<String>> blockedNumbersList) {
        mBlockedNumbersList = blockedNumbersList;
    }

    @Override
    public int getSectionCount() {
        return mBlockedNumbersList.size();
    }

    @Override
    public int getItemCount(int section) {
        return mBlockedNumbersList.get(section).size() - 1;      // -1 porque a primeira posição grava o nome da sessão
    }

    @Override
    public void onBindHeaderViewHolder(MyViewHolder holder, int section, boolean expanded) {
        holder.blocker_or_number.setText(mBlockedNumbersList.get(section).get(0));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int section, int relativePosition, int absolutePosition) {
        holder.blocker_or_number.setText(mBlockedNumbersList.get(section).get(relativePosition + 1));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int layout;
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

        final View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        return new MyViewHolder(view);
    }

    public static class MyViewHolder extends SectionedViewHolder {
        final AestheticTextView blocker_or_number;
        final AestheticCardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            blocker_or_number = (AestheticTextView) itemView.findViewById(android.R.id.title);
            cardView = (AestheticCardView) itemView.findViewById(R.id.item_number_card_view);
        }
    }
}
