package com.enoughspam.step.util.decorator;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Hugo Castelani
 * Date: 02/04/17
 * Time: 02:45
 */

public class LeftRightOffsetItemDecoration extends RecyclerView.ItemDecoration {

    private final Integer mIdealMargin;
    private final Integer mToolbarPosition;

    public LeftRightOffsetItemDecoration(@NonNull final Integer idealMargin,
                                         @NonNull final Integer toolbarPosition) {
        mIdealMargin = idealMargin;
        mToolbarPosition = toolbarPosition;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        final Integer position = parent.getChildAdapterPosition(view);
        if (position != mToolbarPosition) {
            outRect.left = mIdealMargin;
            outRect.right = mIdealMargin;
        }
    }
}
