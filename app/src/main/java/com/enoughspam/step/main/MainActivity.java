package com.enoughspam.step.main;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.AestheticActivity;
import com.afollestad.aesthetic.AestheticToolbar;
import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.notRelated.ThemeDAO;
import com.enoughspam.step.database.dao.related.PersonalDAO;
import com.enoughspam.step.database.domains.ThemeData;
import com.enoughspam.step.generalClasses.ScreenInfo;
import com.enoughspam.step.intro.MainIntroActivity;
import com.enoughspam.step.settings.SettingsActivity;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static android.os.Build.VERSION_CODES.M;

// Created by Hugo on 01/04/17, 21:30

public class MainActivity extends AestheticActivity {

    private final int REQUEST_CODE_INTRO = 6;

    private int currentSelectedPosition = 1;
    private AestheticToolbar toolbar;

    // data access objects
    private ThemeDAO themeDAO;
    private ThemeData themeData;

    private SearchView searchView;

    // theme vars
    private int accentColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        PersonalDAO personalDAO = new PersonalDAO(this);

        if (personalDAO.get() == null) {
            Intent intent = new Intent(MainActivity.this, MainIntroActivity.class);
            startActivityForResult(intent, REQUEST_CODE_INTRO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // theme
        themeDAO = new ThemeDAO(this);
        themeData = themeDAO.getThemeData();

        accentColor = Color.parseColor(themeData.getAccentColor());

        // toolbar
        ScreenInfo screenInfo = new ScreenInfo(this);

        toolbar = (AestheticToolbar) findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= LOLLIPOP) toolbar.setElevation(screenInfo.getPixelDensity() * 4);
        setSupportActionBar(toolbar);

        // fragment
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag("mainFragmentTag");
        if(mainFragment == null) {
            mainFragment = new MainFragment();
            FragmentTransaction infoFragmentT = getSupportFragmentManager().beginTransaction();
            infoFragmentT.replace(R.id.main_fragment_container, mainFragment, "mainFragmentTag");
            infoFragmentT.commit();
        }

        // search view
        searchView = (SearchView) findViewById(R.id.search_view);

        /* Sets searchable configuration defined in searchable.xml for this SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        setUpSearchView();*/

        // navigation drawer
        setUpNavDrawer();

        // theme
        setUpTheme();
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
                //.withHeaderBackground(accentColor)
                .withHeaderBackground(R.color.md_cyan_500)
                .build();

        // navigation drawer itself
        final Drawer navDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withSliderBackgroundColorRes(themeData.isDark() ? R.color.colorPrimaryInverse : R.color.md_white_1000)
                .withAccountHeader(navDrawerHeader)
                .build();

        // adapting drawable colors
        Drawable homeDraw;
        Drawable settingsDraw;

        if (themeData.isDark()) {
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
        navDrawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                switch (position) {
                    case 1: break;

                    case 2:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                                startActivityForResult(intent, 1);
                                //navDrawer.setSelectionAtPosition(currentSelectedPosition);
                            }
                        }, 250);

                        break;
                }

                return false;
            }
        });
    }

    private void setUpTheme() {

        if (themeData.isDark()) {

            Aesthetic.get()
                    .activityTheme(R.style.AppThemeDark)
                    .isDark(true)
                    .colorPrimaryRes(R.color.colorPrimaryInverse)
                    .colorAccent(accentColor)
                    .colorWindowBackgroundRes(R.color.colorWindowBackgroundInverse)
                    .textColorPrimaryRes(R.color.textColorPrimaryInverse)
                    .textColorSecondaryRes(R.color.textColorSecondaryInverse)
                    .colorIconTitleActiveRes(R.color.textColorPrimaryInverse)
                    .colorIconTitleInactiveRes(R.color.textColorPrimaryInverse)
                    .colorCardViewBackgroundRes(R.color.colorCardBackgoundInverse)
                    .apply();

            Aesthetic.get()
                    .colorStatusBarRes(R.color.colorPrimaryInverse)
                    .apply();

        } else {

            Aesthetic.get()
                    .activityTheme(R.style.AppTheme)
                    .isDark(false)
                    .colorPrimaryRes(R.color.colorPrimary)
                    .colorAccent(accentColor)
                    .colorWindowBackgroundRes(R.color.colorWindowBackground)
                    .textColorPrimaryRes(R.color.textColorPrimary)
                    .textColorSecondaryRes(R.color.textColorSecondary)
                    .colorIconTitleActiveRes(R.color.textColorPrimary)
                    .colorIconTitleInactiveRes(R.color.textColorPrimary)
                    .colorCardViewBackgroundRes(R.color.colorCardBackgound)
                    .apply();

            if (Build.VERSION.SDK_INT >= M) {
                Aesthetic.get()
                        .colorStatusBar(ContextCompat.getColor(this, R.color.colorPrimary))
                        .apply();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_INTRO) {
            if (resultCode != RESULT_OK) {
                finish();
            }
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