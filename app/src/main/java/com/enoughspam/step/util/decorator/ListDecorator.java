package com.enoughspam.step.util.decorator;

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

public final class ListDecorator {

    private ListDecorator() {
        throw new UnsupportedOperationException("You can't instantiate me.");
    }

    private static Integer getIdealMargin(@NonNull final Context context) {
        final Integer screenWidth = ScreenUtils.getScreenWidth();
        final Integer idealWidth = context.getResources().getDimensionPixelSize(
                de.mrapp.android.bottomsheet.R.dimen.default_width);

        return (screenWidth - idealWidth) / 2;
    }

    public static void addAdaptableMargins(@NonNull final RecyclerView recyclerView,
                                           @NonNull final Integer toolbarPosition) {
        RecyclerView.ItemDecoration decoration = null;
        final Integer idealMargin = getIdealMargin(recyclerView.getContext());

        if (ScreenUtils.isLandscape() || ScreenUtils.isTablet()) {
            decoration = new LeftRightOffsetItemDecoration(idealMargin, toolbarPosition);
        }

        if (decoration != null) recyclerView.addItemDecoration(decoration);
    }

    public static void addAdaptableMargins(@NonNull final ListView listView) {
        Integer padding = -1;

        if (ScreenUtils.isLandscape() || ScreenUtils.isTablet()) {
            padding = getIdealMargin(listView.getContext());
        }

        if (padding != -1) listView.setPadding(padding, 0, padding, 0);
    }
}
