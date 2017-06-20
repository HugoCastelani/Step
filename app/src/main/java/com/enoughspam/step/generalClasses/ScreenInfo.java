package com.enoughspam.step.generalClasses;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

// Created by Hugo on 02/04/17, 01:50

public class ScreenInfo {

    private Context mContext;
    private DisplayMetrics mDisplayMetrics;

    public ScreenInfo(Context context) {
        mContext = context;
        mDisplayMetrics = context.getResources().getDisplayMetrics();
    }

    public int getWidth() {
        return mDisplayMetrics.widthPixels;
    }

    public int getHeight() {
        return mDisplayMetrics.heightPixels;
    }

    public float getPixelDensity() {
        return mDisplayMetrics.density;
    }

    public int getOrientarion() {
        return mContext.getResources().getConfiguration().orientation;      // 1 - Vertical; 2 - Horizontal
    }

    public boolean isScreenLarge() {
        return (mContext.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public int getFabAlignMargin() {
        return Math.round(getPixelDensity() * 44);      // fab radius (28dp) + margin (16dp)
    }

    public int getBottomSpaceForFab() {
        return Math.round(getPixelDensity() * 88);      // fab radius (56dp) + margin * 2 (32dp)
    }

    public int getTenPercentageMargin() {
        return Math.round((getWidth() - Math.round(getWidth() * 0.80)) / 2);    // width - 80% of width) / 2
    }

}