package com.hugocastelani.blockbook.util.decorator;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.hugocastelani.blockbook.annotation.NonNegative;

/**
 * Created by Hugo Castelani
 * Date: 02/04/17
 * Time: 02:10
 */

public final class EndOffsetItemDecoration extends RecyclerView.ItemDecoration {

    private int mOffsetPx;

    public EndOffsetItemDecoration(@NonNegative @NonNull final Integer offsetPx) {
        mOffsetPx = offsetPx;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        final int viewPosition = parent.getChildAdapterPosition(view);
        final int itemCount = state.getItemCount();

        if (viewPosition == itemCount - 1) {
            outRect.bottom = mOffsetPx;
        }
    }
}

