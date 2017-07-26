package com.enoughspam.step.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import com.blankj.utilcode.util.ScreenUtils;

/**
 * Created by Hugo Castelani
 * Date: 21/07/17
 * Time: 14:39
 */

public class ListDecorator {

    private static Context mContext;

    private ListDecorator() {
        throw new UnsupportedOperationException("You can't instantiate me.");
    }

    public static void init(@NonNull final Context context) {
        mContext = context.getApplicationContext();
    }

    public static void addAdaptableMargins(@NonNull final RecyclerView recyclerView) {
        MarginUtils.init(mContext);
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
    }

    public static void addAdaptableMargins(@NonNull final ListView listView) {
        MarginUtils.init(mContext);
        int padding = -1;

        // TODO: isTablet method must be replaced with proper UtilCode's method
        if (MarginUtils.isTablet()) {
            if (ScreenUtils.isPortrait()) padding = MarginUtils.getFabAlignMargin();
            else padding = MarginUtils.getTenPercentageMargin();

        } else {

            if (ScreenUtils.isLandscape()) padding = MarginUtils.getFabAlignMargin();
        }

        if (padding != -1) listView.setPadding(padding, 0, padding, 0);
    }
}
