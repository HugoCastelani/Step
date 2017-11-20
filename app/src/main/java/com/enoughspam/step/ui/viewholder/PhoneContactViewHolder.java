package com.enoughspam.step.ui.viewholder;

import android.view.View;
import android.widget.LinearLayout;

import com.afollestad.aesthetic.AestheticCardView;
import com.afollestad.aesthetic.AestheticTextView;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.enoughspam.step.R;

/**
 * Created by Hugo Castelani
 * Date: 19/11/17
 * Time: 22:10
 */

public final class PhoneContactViewHolder extends SectionedViewHolder {
    public final AestheticCardView mCardView;
    public final LinearLayout mParent;
    public final AestheticTextView mSectionOrName;
    public final AestheticTextView mNumber;

    public PhoneContactViewHolder(View itemView) {
        super(itemView);
        mCardView = (AestheticCardView) itemView.findViewById(R.id.ipc_card);
        mParent = (LinearLayout) itemView.findViewById(R.id.icp_parent);
        mSectionOrName = (AestheticTextView) itemView.findViewById(android.R.id.title);
        mNumber = (AestheticTextView) itemView.findViewById(android.R.id.summary);
    }
}
