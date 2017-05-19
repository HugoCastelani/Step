package com.enoughspam.step.database.domains;

// created by Hugo on 19/05/17 at 14:40

public class ThemeData {

    private boolean isDark;
    private String lightAccentColor;
    private String darkAccentColor;

    public ThemeData(boolean isDark, String lightAccentColor, String darkAccentColor) {
        this.isDark = isDark;
        this.lightAccentColor = lightAccentColor;
        this.darkAccentColor = darkAccentColor;
    }

    public boolean isDark() {
        return isDark;
    }

    public void setIsDark(boolean dark) {
        isDark = dark;
    }

    public String getLightAccentColor() {
        return lightAccentColor;
    }

    public void setLightAccentColor(String lightAccentColor) {
        this.lightAccentColor = lightAccentColor;
    }

    public String getDarkAccentColor() {
        return darkAccentColor;
    }

    public void setDarkAccentColor(String darkAccentColor) {
        this.darkAccentColor = darkAccentColor;
    }
}
