package com.enoughspam.step.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

// Created by Hugo on 02/04/17, 02:45

public class LeftRightOffsetItemDecoration extends RecyclerView.ItemDecoration {

    private int idealMargin;

    public LeftRightOffsetItemDecoration(int idealMargin) {
        this.idealMargin = idealMargin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.left = idealMargin;
        outRect.right = idealMargin;
    }
}
