package com.enoughspam.step.settings;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.AestheticActivity;
import com.afollestad.aesthetic.AestheticToolbar;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.Utils;
import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.notRelated.ThemeDAO;
import com.enoughspam.step.database.domains.ThemeData;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class SettingsActivity extends AestheticActivity
        implements ColorChooserDialog.ColorCallback {

    ThemeDAO themeDAO;
    ThemeData themeData;

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        final String color = String.format("#%06X", 0xFFFFFF & selectedColor);

        themeData.setAccentColor(color);
        themeDAO.setThemeData(themeData);
        setUpTheme();

        recreate();
    }

    @Override
    public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        Utils.init(this);

        themeDAO = new ThemeDAO(this);
        themeData = themeDAO.getThemeData();

        AestheticToolbar toolbar = (AestheticToolbar) findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= LOLLIPOP) toolbar.setElevation(ConvertUtils.dp2px(4));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction().replace(R.id.settings_fragment_container, new SettingsFragment()).commit();
    }

    private void setUpTheme() {
        Aesthetic.get()
                .colorAccent(Color.parseColor(themeData.getAccentColor()))
                .apply();
    }
}
