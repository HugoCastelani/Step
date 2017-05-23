package com.enoughspam.step.main;

// Created by hugo on 01/04/17, 23:05

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.notRelated.ThemeDAO;

import java.util.ArrayList;

public class BlockedNumbersAdapter extends SectionedRecyclerViewAdapter<BlockedNumbersAdapter.MyViewHolder> {

    private ArrayList<ArrayList<String>> blockedNumbersList;
    private Context context;
    private boolean isDark;

    public BlockedNumbersAdapter(ArrayList<ArrayList<String>> blockedNumbersList, Context context) {
        this.blockedNumbersList = blockedNumbersList;
        this.context = context;

        this.isDark = new ThemeDAO(context).getThemeData().isDark();
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
                layout = R.layout.item_blocker;
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
        TextView blocker_or_number;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            blocker_or_number = (TextView) itemView.findViewById(R.id.blocked_recylerview_blocker_or_number);
            cardView = (CardView) itemView.findViewById(R.id.item_number_cardview);
        }
    }
}
