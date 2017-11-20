package com.enoughspam.step.ui.viewholder;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.enoughspam.step.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Hugo Castelani
 * Date: 15/11/17
 * Time: 15:14
 */

public final class UserViewHolder extends SectionedViewHolder {
    public final CardView mCardView;
    public final ProgressBar mProgressBar;
    public final CircleImageView mUserPic;
    public final TextView mUsername;
    public Boolean mIsSwipeable;

    public UserViewHolder(View itemView) {
        super(itemView);
        mCardView = (CardView) itemView.findViewById(R.id.iu_card);
        mProgressBar = (ProgressBar) itemView.findViewById(R.id.iu_progress_bar);
        mUserPic = (CircleImageView) itemView.findViewById(R.id.iu_pic);
        mUsername = (TextView) itemView.findViewById(R.id.iu_username);
        mIsSwipeable = false;
    }
}
