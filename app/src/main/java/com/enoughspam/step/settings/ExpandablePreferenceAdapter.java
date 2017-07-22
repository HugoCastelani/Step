package com.enoughspam.step.settings;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.aesthetic.AestheticTextView;
import com.enoughspam.step.R;
import com.enoughspam.step.database.domains.Description;

import java.util.List;

/**
 * Created by hugo
 * Date: 21/07/17
 * Time: 14:34
 */

public class ExpandablePreferenceAdapter extends RecyclerView.Adapter<ExpandablePreferenceAdapter.MyViewHolder> {

    private final List<Description> list;

    public ExpandablePreferenceAdapter(@NonNull final List<Description> list) {
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.preference_expandable_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.description.setText(list.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        final AestheticTextView description;

        public MyViewHolder(View itemView) {
            super(itemView);
            description = (AestheticTextView) itemView.findViewById(R.id.preference_expandable_item_textview);
        }
    }
}
