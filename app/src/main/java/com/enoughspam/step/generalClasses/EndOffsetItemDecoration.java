package com.enoughspam.step.generalClasses;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

// Created by Hugo on 02/04/17, 02:10

public class EndOffsetItemDecoration extends RecyclerView.ItemDecoration {

    private int mOffsetPx;

    public EndOffsetItemDecoration(int offsetPx) {
        mOffsetPx = offsetPx;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int viewPosition = parent.getChildAdapterPosition(view);
        int itemCount = state.getItemCount();

        if(viewPosition == itemCount - 1) {
            outRect.bottom = mOffsetPx;

        }
    }
}

