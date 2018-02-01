package com.hugocastelani.ivory.util;

import android.content.res.Configuration;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.Utils;

/**
 * Created by Hugo Castelani
 * Date: 02/04/17
 * Time: 01:50
 */

public final class MarginUtils {

    private MarginUtils() {
        throw new UnsupportedOperationException("You can't instantiate me.");
    }

    public static boolean isTablet() {
        return (Utils.getApp().getResources().getConfiguration().screenLayout
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