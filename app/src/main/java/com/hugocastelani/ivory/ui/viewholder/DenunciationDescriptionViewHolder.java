package com.hugocastelani.ivory.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afollestad.aesthetic.AestheticTextView;
import com.hugocastelani.ivory.R;

/**
 * Created by Hugo Castelani
 * Date: 01/12/17
 * Time: 17:57
 */

public final class DenunciationDescriptionViewHolder extends RecyclerView.ViewHolder {
    public final AestheticTextView description;

    public DenunciationDescriptionViewHolder(View itemView) {
        super(itemView);
        description = (AestheticTextView) itemView.findViewById(R.id.pe_item_text_view);
    }
}
