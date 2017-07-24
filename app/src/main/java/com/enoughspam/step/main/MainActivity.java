package com.enoughspam.step.main;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;

import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.AestheticActivity;
import com.afollestad.aesthetic.AestheticToolbar;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.Utils;
import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.DAOHandler;
import com.enoughspam.step.settings.SettingsActivity;
import com.enoughspam.step.util.ThemeHandler;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static android.os.Build.VERSION_CODES.M;

/**
 * Created by Hugo Castelani
 * Date: 01/04/17
 * Time: 21:30
 */

public class MainActivity extends AestheticActivity {

    private static final int REQUEST_CODE_SETTINGS = 2;

    private AestheticToolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Utils.init(this);
        DAOHandler.init(this);
        initAesthetic();

        mToolbar = (AestheticToolbar) findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= LOLLIPOP) mToolbar.setElevation(ConvertUtils.dp2px(4));
        setSupportActionBar(mToolbar);

        // fragment
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag("mainFragmentTag");
        if (mainFragment == null) {
            mainFragment = new MainFragment();
            final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment_container, mainFragment, "mainFragmentTag");
            fragmentTransaction.commit();
        }

        // navigation drawer
        setUpNavDrawer();
    }

    private void setUpNavDrawer() {
        final Drawable header = ContextCompat.getDrawable(getBaseContext(), R.drawable.header);
        header.setColorFilter(ThemeHandler.getAccent(), PorterDuff.Mode.SRC_IN);

        // navigation drawer header
        final AccountHeader navDrawerHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(header)
                .build();

        // navigation drawer itself
        final Drawer navDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withSliderBackgroundColorRes(ThemeHandler.isDark() ? R.color.colorWindowBackgroundInverse : R.color.md_white_1000)
                .withAccountHeader(navDrawerHeader)
                .withDelayDrawerClickEvent(250)
                .build();

        // adapting drawable colors
        final Drawable homeDraw = ContextCompat.getDrawable(this, R.drawable.ic_home);
        homeDraw.setColorFilter(ThemeHandler.getPrimaryText(), PorterDuff.Mode.SRC_IN);
        final Drawable settingsDraw = ContextCompat.getDrawable(this, R.drawable.ic_settings);
        settingsDraw.setColorFilter(ThemeHandler.getPrimaryText(), PorterDuff.Mode.SRC_IN);

        // navigation drawer items
        navDrawer.addItem(new PrimaryDrawerItem().withIdentifier(0).withName(R.string.main_fragment_label).withIcon(homeDraw));
        navDrawer.addItem(new PrimaryDrawerItem().withIdentifier(1).withName(R.string.settings_activity_label).withIcon(settingsDraw));

        // navigation drawer actions
        navDrawer.setOnDrawerItemClickListener((view, position, drawerItem) -> {
            switch (position) {
                case 1: break;

                case 2:
                    final Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_SETTINGS);

                    break;
            }

            return false;
        });
    }

    private void initAesthetic() {
        if (Aesthetic.isFirstTime()) {
            Aesthetic.get()
                    .activityTheme(R.style.AppTheme)
                    .isDark(false)
                    .colorPrimaryRes(R.color.colorPrimary)
                    .colorAccentRes(R.color.colorAccent)
                    .colorWindowBackgroundRes(R.color.colorWindowBackground)
                    .textColorPrimaryRes(R.color.textColorPrimary)
                    .textColorSecondaryRes(R.color.textColorSecondary)
                    .colorIconTitleActiveRes(R.color.textColorPrimary)
                    .colorIconTitleInactiveRes(R.color.textColorPrimary)
                    .colorCardViewBackgroundRes(R.color.colorCardBackground)
                    .apply();

        } else {

            if (ThemeHandler.isDark()) {

                Aesthetic.get()
                        .activityTheme(R.style.AppThemeDark)
                        .colorPrimaryRes(R.color.colorPrimaryInverse)
                        .colorWindowBackgroundRes(R.color.colorWindowBackgroundInverse)
                        .textColorPrimaryRes(R.color.textColorPrimaryInverse)
                        .textColorSecondaryRes(R.color.textColorSecondaryInverse)
                        .colorIconTitleActiveRes(R.color.textColorPrimaryInverse)
                        .colorIconTitleInactiveRes(R.color.textColorPrimaryInverse)
                        .colorCardViewBackgroundRes(R.color.colorCardBackgroundInverse)
                        .apply();

                if (Build.VERSION.SDK_INT >= M) {
                    Aesthetic.get()
                            .colorStatusBarRes(R.color.colorPrimaryInverse)
                            .apply();
                }

            } else {

                Aesthetic.get()
                        .activityTheme(R.style.AppTheme)
                        .colorPrimaryRes(R.color.colorPrimary)
                        .colorWindowBackgroundRes(R.color.colorWindowBackground)
                        .textColorPrimaryRes(R.color.textColorPrimary)
                        .textColorSecondaryRes(R.color.textColorSecondary)
                        .colorIconTitleActiveRes(R.color.textColorPrimary)
                        .colorIconTitleInactiveRes(R.color.textColorPrimary)
                        .colorCardViewBackgroundRes(R.color.colorCardBackground)
                        .apply();

                if (Build.VERSION.SDK_INT >= M) {
                    Aesthetic.get()
                            .colorStatusBarRes(R.color.colorPrimary)
                            .apply();
                }
            }
        }
    }
}