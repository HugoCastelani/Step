package com.enoughspam.step.util;

import android.os.Build;

/**
 * Created by Hugo Castelani
 * Date: 23/08/17
 * Time: 15:09
 */

public class VariousUtils {
    private static Boolean sIsAboveMarshmallow;

    public static boolean isAboveMarshmallow() {
        if (sIsAboveMarshmallow == null) {
            sIsAboveMarshmallow = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
        }

        return sIsAboveMarshmallow.booleanValue();
    }
}
