package com.enoughspam.step.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ScreenUtils;

/**
 * Created by hugo
 * Date: 21/07/17
 * Time: 14:39
 */

public class RecyclerViewDecorator {

    private static Context context;

    private RecyclerViewDecorator() {
        throw new UnsupportedOperationException("You can't instantiate me.");
    }

    public static void init(@NonNull final Context context) {
        RecyclerViewDecorator.context = context.getApplicationContext();
    }

    public static void decorate(@NonNull RecyclerView recyclerView) {
        MarginUtils.init(context);
        RecyclerView.ItemDecoration decoration = null;

        // TODO: isTablet method must be replaced with proper UtilCode's method
        if (MarginUtils.isTablet()) {

            if (ScreenUtils.isPortrait())
                decoration = new LeftRightOffsetItemDecoration(MarginUtils.getFabAlignMargin());
            else
                decoration = new LeftRightOffsetItemDecoration(MarginUtils.getTenPercentageMargin());

        } else {

            if (ScreenUtils.isLandscape())
                decoration = new LeftRightOffsetItemDecoration(MarginUtils.getFabAlignMargin());
        }

        if (decoration != null) recyclerView.addItemDecoration(decoration);
        recyclerView.addItemDecoration(new EndOffsetItemDecoration(ConvertUtils.dp2px(16)));
    }
}
