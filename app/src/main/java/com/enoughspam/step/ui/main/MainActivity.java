package com.enoughspam.step.ui.main;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.aesthetic.AestheticFab;
import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.local.LUserDAO;
import com.enoughspam.step.ui.addnumber.AddNumberActivity;
import com.enoughspam.step.ui.intangible.AbstractActivity;
import com.enoughspam.step.ui.intangible.SnackbarTrigger;
import com.enoughspam.step.ui.notification.NotificationActivity;
import com.enoughspam.step.ui.profile.ProfileActivity;
import com.enoughspam.step.ui.search.SearchActivity;
import com.enoughspam.step.ui.settings.SettingsActivity;
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

public final class MainActivity extends AbstractActivity implements SnackbarTrigger {

    private static final int REQUEST_CODE_SETTINGS = 2;
    private static final int REQUEST_CODE_ADD_NUMBER = 3;
    private static final int REQUEST_CODE_ACCOUNT = 4;
    private static final int REQUEST_CODE_SEARCH = 5;
    private static final int REQUEST_CODE_NOTIFICATION = 6;

    private AestheticFab mFAB;
    private Snackbar mSnackbar;
    private Drawer mNavDrawer;
    private MainFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        initToolbar(false);
        initViews();
        initActions();
        initFragment();
    }

    @Override
    protected void initViews() {
        mFAB = (AestheticFab) findViewById(R.id.ma_fab);
        mSnackbar = createSnackbar(R.string.press_back_again);
        initNavDrawer();
    }

    @Override
    protected void initActions() {
        mFAB.setOnClickListener(fab -> {
            final Intent intent = new Intent(MainActivity.this, AddNumberActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_NUMBER);
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
        mFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag("mainFragmentTag");
        if (mFragment == null) {
            mFragment = new MainFragment();
            final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.ma_fragment_container, mFragment, "mainFragmentTag");
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
        mNavDrawer = new DrawerBuilder()
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
        final Drawable profileDraw = ContextCompat.getDrawable(this, R.drawable.ic_profile);
        profileDraw.setColorFilter(ThemeHandler.getPrimaryText(), PorterDuff.Mode.SRC_IN);

        // navigation drawer items
        mNavDrawer.addItem(new PrimaryDrawerItem().withIdentifier(0).withName(R.string.main_activity_label).withIcon(homeDraw));
        mNavDrawer.addItem(new PrimaryDrawerItem().withIdentifier(1).withName(R.string.settings_activity_label).withIcon(settingsDraw));
        mNavDrawer.addItem(new PrimaryDrawerItem().withIdentifier(2).withName(R.string.profile_activity_label).withIcon(profileDraw));

        mNavDrawer.setSelection(0);

        // navigation drawer actions
        mNavDrawer.setOnDrawerItemClickListener((view, position, drawerItem) -> {
            switch (position) {
                case 1: break;

                case 2:
                    final Intent intent1 = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivityForResult(intent1, REQUEST_CODE_SETTINGS);
                    break;

                case 3:
                    final Intent intent2 = new Intent(MainActivity.this, ProfileActivity.class);
                    intent2.putExtra("user", LUserDAO.get().getThisUser());
                    startActivityForResult(intent2, REQUEST_CODE_ACCOUNT);
                    break;
            }

            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                final Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SEARCH);
                return true;
            case R.id.action_notifications:
                final Intent intent2 = new Intent(MainActivity.this, NotificationActivity.class);
                startActivityForResult(intent2, REQUEST_CODE_NOTIFICATION);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (mNavDrawer.isDrawerOpen()) {
            mNavDrawer.closeDrawer();
        } else if (mSnackbar.isShown()) {
            super.onBackPressed();
        } else {
            mSnackbar.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            default: recreate();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFragment.isPhoneSectionListEmpty()) {
            createSnackbar(R.string.no_registered_number).show();
        }
    }
}