package com.enoughspam.step.ui.viewholder;

import android.view.View;

import com.afollestad.aesthetic.AestheticButton;
import com.afollestad.aesthetic.AestheticTextView;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.enoughspam.step.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Hugo Castelani
 * Date: 15/11/17
 * Time: 15:16
 */

public final class ToolbarViewHolder extends SectionedViewHolder {
    public final CircleImageView mUserPic;
    public final AestheticTextView mUsername;
    public final AestheticTextView mSocialMedia;
    public final AestheticButton mButton;

    public ToolbarViewHolder(View itemView) {
        super(itemView);
        mUserPic = (CircleImageView) itemView.findViewById(R.id.ipt_pic);
        mUsername = (AestheticTextView) itemView.findViewById(R.id.ipt_username);
        mSocialMedia = (AestheticTextView) itemView.findViewById(R.id.ipt_social_media);
        mButton = (AestheticButton) itemView.findViewById(R.id.ipt_delete_account);
    }
}
