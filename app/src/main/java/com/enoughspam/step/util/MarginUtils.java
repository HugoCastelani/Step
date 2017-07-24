package com.enoughspam.step.util;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.Utils;

/**
 * Created by Hugo Castelani
 * Date: 02/04/17
 * Time: 01:50
 */

public class MarginUtils {

    private MarginUtils() {
        throw new UnsupportedOperationException("You can't instantiate me.");
    }

    public static void init(@NonNull final Context context) {
        if (Utils.getContext() == null) Utils.init(context);
    }

    public static boolean isTablet() {
        return (Utils.getContext().getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static int getFabAlignMargin() {
        return Math.round(ConvertUtils.dp2px(44));  // fab radius (28dp) + margin (16dp)
    }

    public static int getTenPercentageMargin() {
        return ((int) Math.round(ScreenUtils.getScreenWidth() * 0.10));
    }

}