package com.enoughspam.step.ui.viewholder;

import android.view.View;

import com.afollestad.aesthetic.AestheticCardView;
import com.afollestad.aesthetic.AestheticTextView;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.enoughspam.step.R;

/**
 * Created by Hugo Castelani
 * Date: 15/11/17
 * Time: 15:19
 */

public final class PhoneHeaderViewHolder extends SectionedViewHolder {
    public final AestheticCardView mCardView;
    public final AestheticTextView mBlockerOrNumber;
    public Boolean mIsSwipeable;

    public PhoneHeaderViewHolder(View itemView) {
        super(itemView);
        mCardView = (AestheticCardView) itemView.findViewById(R.id.main_item_number_card);
        mBlockerOrNumber = (AestheticTextView) itemView.findViewById(android.R.id.title);
        mIsSwipeable = false;
    }
}
