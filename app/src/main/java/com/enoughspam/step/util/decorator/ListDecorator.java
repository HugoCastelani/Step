package com.enoughspam.step.util.decorator;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import com.blankj.utilcode.util.ScreenUtils;
import com.enoughspam.step.util.MarginUtils;

/**
 * Created by Hugo Castelani
 * Date: 21/07/17
 * Time: 14:39
 */

public class ListDecorator {

    private ListDecorator() {
        throw new UnsupportedOperationException("You can't instantiate me.");
    }

    public static void addAdaptableMargins(@NonNull final RecyclerView recyclerView,
                                           @NonNull final Integer toolbarPosition) {
        RecyclerView.ItemDecoration decoration = null;

        if (ScreenUtils.isTablet()) {

            if (ScreenUtils.isPortrait())
                decoration = new LeftRightOffsetItemDecoration(
                        MarginUtils.getFabAlignMargin(), toolbarPosition);
            else
                decoration = new LeftRightOffsetItemDecoration(
                        MarginUtils.getTenPercentageMargin(), toolbarPosition);

        } else {

            if (ScreenUtils.isLandscape())
                decoration = new LeftRightOffsetItemDecoration(
                        MarginUtils.getFabAlignMargin(), toolbarPosition);
        }

        if (decoration != null) recyclerView.addItemDecoration(decoration);
    }

    public static void addAdaptableMargins(@NonNull final ListView listView) {
        int padding = -1;

        if (ScreenUtils.isTablet()) {
            if (ScreenUtils.isPortrait()) padding = MarginUtils.getFabAlignMargin();
            else padding = MarginUtils.getTenPercentageMargin();

        } else {

            if (ScreenUtils.isLandscape()) padding = MarginUtils.getFabAlignMargin();
        }

        if (padding != -1) listView.setPadding(padding, 0, padding, 0);
    }
}
