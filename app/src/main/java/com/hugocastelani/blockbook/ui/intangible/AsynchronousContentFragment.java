package com.hugocastelani.blockbook.ui.intangible;

import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.afollestad.aesthetic.AestheticRecyclerView;
import com.hugocastelani.blockbook.util.AnimUtils;

/**
 * Created by Hugo Castelani
 * Date: 21/11/17
 * Time: 16:09
 */

public abstract class AsynchronousContentFragment extends AbstractFragment {
    protected ProgressBar mProgressBar;
    protected ImageView mPlaceHolder;
    protected AestheticRecyclerView mRecyclerView;

    @NonNull
    public AsynchronousContentFragment showRecyclerView() {
        AnimUtils.fadeOutFadeIn(mProgressBar, mRecyclerView);
        return this;
    }

    @NonNull
    public AsynchronousContentFragment showPlaceHolder() {
        AnimUtils.fadeOutFadeIn(mProgressBar, mPlaceHolder);
        return this;
    }
}
