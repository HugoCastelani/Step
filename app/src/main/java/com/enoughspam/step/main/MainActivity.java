package com.enoughspam.step.main;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;

import com.enoughspam.step.R;
import com.enoughspam.step.abstracts.AbstractActivity;
import com.enoughspam.step.addNumber.AddNumberActivity;
import com.enoughspam.step.database.dao.PersonalDAO;
import com.enoughspam.step.database.dao.UserDAO;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.settings.SettingsActivity;
import com.enoughspam.step.util.ThemeHandler;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

/**
 * Created by Hugo Castelani
 * Date: 01/04/17
 * Time: 21:30
 */

public class MainActivity extends AbstractActivity {

    private static final int REQUEST_CODE_SETTINGS = 2;

    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if (PersonalDAO.get() == null) {
            UserDAO.create(new User(-1, "-1", "Developer"));
            PersonalDAO.create(new User(-1, "-1", "Developer"));
        }

        initToolbar(false);
        initViews();
        initActions();
        initFragment();
        initNavDrawer();
    }

    @Override
    protected void initViews() {
        mFab = (FloatingActionButton) findViewById(R.id.main_fab);
    }

    @Override
    protected void initActions() {
        mFab.setOnClickListener(fab -> {
            final Intent intent = new Intent(MainActivity.this, AddNumberActivity.class);
            startActivity(intent);
        });

        /*TapTargetView.showFor(getActivity(),
                TapTarget.forView(fab,
                        "O quê esse FAB faz?",
                        "No momento, só chama uma snackbar inútil...")
                        .outerCircleColorInt(ThemeHandler.getAccent())
                        .textTypeface(Typeface.DEFAULT)
                        .targetCircleColor(ThemeHandler.isDark() ? R.color.colorPrimaryInverse : R.color.colorPrimary)
                        .transparentTarget(true),
                null);*/
    }

    @Override
    protected void initFragment() {
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag("mainFragmentTag");
        if (mainFragment == null) {
            mainFragment = new MainFragment();
            final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment_container, mainFragment, "mainFragmentTag");
            fragmentTransaction.commit();
        }
    }

    private void initNavDrawer() {
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
                .withToolbar(getToolbar())
                .withSliderBackgroundColorRes(ThemeHandler.isDark() ?
                        R.color.colorWindowBackgroundInverse : R.color.md_white_1000)
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SETTINGS) {
            recreate();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}