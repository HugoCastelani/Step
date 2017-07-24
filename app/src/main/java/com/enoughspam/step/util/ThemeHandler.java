package com.enoughspam.step.util;

import com.afollestad.aesthetic.Aesthetic;

/**
 * Created by Hugo Castelani
 * Date: 19/07/17
 * Time: 15:32
 */

public class ThemeHandler {
    private ThemeHandler() {}

    private static boolean sIsDark;
    public static boolean isDark() {
        Aesthetic.get().isDark().take(1).subscribe(dark -> sIsDark = dark);
        return sIsDark;
    }

    private static int sPrimaryColor;
    public static int getPrimary() {
        Aesthetic.get().colorPrimary().take(1).subscribe(color -> sPrimaryColor = color);
        return sPrimaryColor;
    }

    private static int sAccentColor;
    public static int getAccent() {
        Aesthetic.get().colorAccent().take(1).subscribe(color -> sAccentColor = color);
        return sAccentColor;
    }

    private static int sBackgroundColor;
    public static int getBackground() {
        Aesthetic.get().colorWindowBackground().take(1).subscribe(color -> sBackgroundColor = color);
        return sBackgroundColor;
    }

    private static int sPrimaryTextColor;
    public static int getPrimaryText() {
        Aesthetic.get().textColorPrimary().take(1).subscribe(color -> sPrimaryTextColor = color);
        return sPrimaryTextColor;
    }

    private static int sSecondaryTextColor;
    public static int getSecondaryText() {
        Aesthetic.get().textColorSecondary().take(1).subscribe(color -> sSecondaryTextColor = color);
        return sSecondaryTextColor;
    }
}