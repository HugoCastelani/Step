package com.hugocastelani.ivory.ui.viewholder;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;

import com.afollestad.aesthetic.AestheticTextView;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.hugocastelani.ivory.R;

/**
 * Created by Hugo Castelani
 * Date: 19/11/17
 * Time: 22:10
 */

public final class PhoneContactViewHolder extends SectionedViewHolder {
    public final LinearLayout mParent;
    public final AestheticTextView mSectionOrName;
    public final AestheticTextView mNumber;

    public PhoneContactViewHolder(View itemView) {
        super(itemView);
        mParent = (LinearLayout) itemView.findViewById(R.id.icp_parent);
        mSectionOrName = (AestheticTextView) itemView.findViewById(android.R.id.title);
        mNumber = (AestheticTextView) itemView.findViewById(android.R.id.summary);
    }

    public PhoneContactViewHolder setSelected(@NonNull final Boolean isSelected) {
        if (isSelected) {
            mParent.setBackgroundColor(ContextCompat.getColor(mParent.getContext(), R.color.md_grey_200));
        } else {
            mParent.setBackgroundColor(ContextCompat.getColor(mParent.getContext(), R.color.md_white_1000));
        }

        return this;
    }
}
