package com.hugocastelani.blockbook.ui.viewholder;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;

import com.afollestad.aesthetic.AestheticTextView;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.hugocastelani.blockbook.R;

/**
 * Created by Hugo Castelani
 * Date: 15/11/17
 * Time: 15:19
 */

public final class PhoneHeaderViewHolder extends SectionedViewHolder {
    public final LinearLayout mParent;
    public final AestheticTextView mBlockerOrNumber;

    public Boolean mIsSwipeable;

    public PhoneHeaderViewHolder(View itemView) {
        super(itemView);
        mParent = (LinearLayout) itemView.findViewById(R.id.ip_parent);
        mBlockerOrNumber = (AestheticTextView) itemView.findViewById(android.R.id.title);

        mIsSwipeable = false;
    }

    public PhoneHeaderViewHolder setSelected(@NonNull final Boolean isSelected) {
        if (isSelected) {
            mParent.setBackgroundColor(ContextCompat.getColor(mParent.getContext(), R.color.md_grey_200));
        } else {
            mParent.setBackgroundColor(ContextCompat.getColor(mParent.getContext(), R.color.md_white_1000));
        }
        return this;
    }
}
