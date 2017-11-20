package com.enoughspam.step.ui.addnumber.extra;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * Created by Hugo Castelani
 * Date: 29/07/17
 * Time: 12:35
 */

public class CustomLinearLayoutManager extends LinearLayoutManager {
    private boolean mIsScrollEnabled = true;

    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public CustomLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setScrollEnabled(boolean flag) {
        mIsScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return mIsScrollEnabled && super.canScrollVertically();
    }
}
