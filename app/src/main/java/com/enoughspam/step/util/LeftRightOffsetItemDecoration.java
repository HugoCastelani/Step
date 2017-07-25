package com.enoughspam.step.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Hugo Castelani
 * Date: 02/04/17
 * Time: 02:45
 */

public class LeftRightOffsetItemDecoration extends RecyclerView.ItemDecoration {

    private int mIdealMargin;

    public LeftRightOffsetItemDecoration(int idealMargin) {
        mIdealMargin = idealMargin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.left = mIdealMargin;
        outRect.right = mIdealMargin;
    }
}
