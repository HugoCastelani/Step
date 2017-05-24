package com.enoughspam.step.database.domains;

// created by Hugo on 19/05/17 at 14:40

public class ThemeData {

    private boolean isDark;
    private String accentColor;

    public ThemeData(boolean isDark, String accentColor) {
        this.isDark = isDark;
        this.accentColor = accentColor;
    }

    public boolean isDark() {
        return isDark;
    }

    public void setIsDark(boolean dark) {
        isDark = dark;
    }

    public String getAccentColor() {
        return accentColor;
    }

    public void setAccentColor(String accentColor) {
        this.accentColor = accentColor;
    }
}
