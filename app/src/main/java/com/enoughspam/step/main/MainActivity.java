package com.enoughspam.step.main;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.AestheticActivity;
import com.afollestad.aesthetic.AestheticToolbar;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.Utils;
import com.enoughspam.step.R;
import com.enoughspam.step.settings.SettingsActivity;
import com.enoughspam.step.util.ThemeHandler;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static android.os.Build.VERSION_CODES.M;

// Created by Hugo on 01/04/17, 21:30

public class MainActivity extends AestheticActivity {

    private static final int REQUEST_CODE_SETTINGS = 2;

    private int currentSelectedPosition = 1;
    private AestheticToolbar toolbar;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Utils.init(this);
        initAesthetic();

        toolbar = (AestheticToolbar) findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= LOLLIPOP) toolbar.setElevation(ConvertUtils.dp2px(4));
        setSupportActionBar(toolbar);

        // fragment
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag("mainFragmentTag");
        if (mainFragment == null) {
            mainFragment = new MainFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment_container, mainFragment, "mainFragmentTag");
            fragmentTransaction.commit();
        }

        // search view
        searchView = (SearchView) findViewById(R.id.search_view);

        /* Sets searchable configuration defined in searchable.xml for this SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        setUpSearchView();*/

        // navigation drawer
        setUpNavDrawer();
    }

    private void setUpSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getBaseContext(), "Submit", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                Toast.makeText(getBaseContext(), "Change", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setUpNavDrawer() {
        // navigation drawer header
        AccountHeader navDrawerHeader = new AccountHeaderBuilder()
                .withActivity(this)
                //.withHeaderBackground(ThemeHandler.getAccent())
                .withHeaderBackground(R.color.md_cyan_500)
                .build();

        // navigation drawer itself
        final Drawer navDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withSliderBackgroundColorRes(ThemeHandler.isDark() ? R.color.colorWindowBackgroundInverse : R.color.md_white_1000)
                .withAccountHeader(navDrawerHeader)
                .build();

        // adapting drawable colors
        Drawable homeDraw;
        Drawable settingsDraw;

        if (ThemeHandler.isDark()) {
            homeDraw = ContextCompat.getDrawable(this, R.drawable.ic_home_inverse);
            settingsDraw = ContextCompat.getDrawable(this, R.drawable.ic_settings_inverse);

        } else {

            homeDraw = ContextCompat.getDrawable(this, R.drawable.ic_home);
            settingsDraw = ContextCompat.getDrawable(this, R.drawable.ic_settings);
        }

        // navigation drawer items
        navDrawer.addItem(new PrimaryDrawerItem().withIdentifier(0).withName(R.string.main_fragment_label).withIcon(homeDraw));
        navDrawer.addItem(new PrimaryDrawerItem().withIdentifier(1).withName(R.string.settings_activity_label).withIcon(settingsDraw));

        // navigation drawer actions
        navDrawer.setOnDrawerItemClickListener((view, position, drawerItem) -> {
            switch (position) {
                case 1: break;

                case 2:
                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_SETTINGS);
                    }, 250);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SETTINGS) {
            recreate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        /*if (id == R.id.search_view) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}