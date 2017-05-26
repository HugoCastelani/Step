package com.enoughspam.step.settings;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import com.afollestad.aesthetic.AestheticActivity;
import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.notRelated.ThemeDAO;
import com.enoughspam.step.generalClasses.ScreenInfo;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class SettingsActivity extends AestheticActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ScreenInfo screenInfo = new ScreenInfo(this);

        // getting main_toolbar ready
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (Build.VERSION.SDK_INT >= LOLLIPOP) toolbar.setElevation(screenInfo.getPixelDensity() * 4);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // setting fragment
        SettingsFragment settingsFragment= (SettingsFragment) getSupportFragmentManager().findFragmentByTag("settingsFragmentTag");
        if(settingsFragment == null) {
            settingsFragment = new SettingsFragment();
            FragmentTransaction settingsFragmentT = getSupportFragmentManager().beginTransaction();
            settingsFragmentT.replace(R.id.settings_fragment_container, settingsFragment, "mainFragmentTag");
            settingsFragmentT.commit();
        }

        themeIt();
    }

    private void themeIt() {

        if (Build.VERSION.SDK_INT >= LOLLIPOP) {

            if (new ThemeDAO(this).getThemeData().isDark())
                toolbar.setElevation(0);

            else
                toolbar.setElevation(new ScreenInfo(this).getPixelDensity() * 4);

        }

    }

}
