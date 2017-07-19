package com.enoughspam.step.util;

import com.afollestad.aesthetic.Aesthetic;

/**
 * Created by hugo
 * Date: 19/07/17
 * Time: 15:32
 */

public class ThemeHandler {
    private ThemeHandler() {}

    private static boolean isDark;
    public static boolean isDark() {
        Aesthetic.get().isDark().take(1).subscribe(dark -> isDark = dark);
        return isDark;
    }

    private static int accentColor;
    public static int getAccent() {
        Aesthetic.get().colorAccent().take(1).subscribe(color -> accentColor = color);
        return accentColor;
    }

    private static int backgroundColor;
    public static int getBackground() {
        Aesthetic.get().colorWindowBackground().take(1).subscribe(color -> backgroundColor = color);
        return backgroundColor;
    }
}