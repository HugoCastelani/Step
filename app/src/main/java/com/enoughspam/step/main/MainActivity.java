package com.enoughspam.step.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.AestheticActivity;
import com.afollestad.aesthetic.NavigationViewMode;
import com.enoughspam.step.R;
import com.enoughspam.step.settings.SettingsActivity;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import static android.os.Build.VERSION_CODES.M;

// Created by Hugo on 01/04/17, 21:30

public class MainActivity extends AestheticActivity {

    int currentSelectedPosition = 1;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // theming everything
        themeIt();

        // instancing and setting toolbar as actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // setting fragment
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag("mainFragmentTag");
        if(mainFragment == null) {
            mainFragment = new MainFragment();
            FragmentTransaction infoFragmentT = getSupportFragmentManager().beginTransaction();
            infoFragmentT.replace(R.id.fragment_container, mainFragment, "mainFragmentTag");
            infoFragmentT.commit();
        }

        // getting navigation drawer ready
        prepareNavDrawer();

    }

    private void themeIt() {
        Aesthetic.get()
                .primaryColor(ContextCompat.getColor(this, R.color.md_grey_50))
                .accentColor(ContextCompat.getColor(this, R.color.md_cyan_500))
                .windowBgColor(ContextCompat.getColor(this, R.color.md_grey_100))
                .primaryTextColor(ContextCompat.getColor(this, R.color.md_black_1000))
                .secondaryTextColor(ContextCompat.getColor(this, R.color.md_grey_700))
                .primaryTextInverseColor(ContextCompat.getColor(this, R.color.md_white_1000))
                .secondaryTextInverseColor(ContextCompat.getColor(this, R.color.md_grey_400))
                .iconTitleActiveColorRes(R.color.md_black_1000)
                .iconTitleInactiveColorRes(R.color.md_black_1000)
                .navViewMode(NavigationViewMode.SELECTED_ACCENT)
                .apply();

        if (Build.VERSION.SDK_INT >= M) {
            Aesthetic.get()
                    .statusBarColor(ContextCompat.getColor(this, R.color.md_grey_50))
                    .apply();
        }
    }

    private void prepareNavDrawer() {
        // navigation drawer header
        AccountHeader navDrawerHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.md_cyan_500)
                .build();

        // navigation drawer itself
        final Drawer navDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(navDrawerHeader)
                .build();

        // navigation drawer items
        navDrawer.addItem(new PrimaryDrawerItem().withIdentifier(0)
                .withName(R.string.main_fragment_label).withIcon(ContextCompat.getDrawable(this, R.drawable.ic_home)));
        navDrawer.addItem(new PrimaryDrawerItem().withIdentifier(1)
                .withName(R.string.settings_activity_label).withIcon(ContextCompat.getDrawable(this, R.drawable.ic_settings)));

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
                                startActivity(intent);
                                //navDrawer.setSelectionAtPosition(currentSelectedPosition);
                            }
                        }, 250);

                        break;
                }

                return false;
            }
        });
    }
}