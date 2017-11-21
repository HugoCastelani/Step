package com.enoughspam.step.ui.notification;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.aesthetic.AestheticFab;
import com.enoughspam.step.R;
import com.enoughspam.step.ui.intangible.AbstractActivity;

/**
 * Created by Hugo Castelani
 * Date: 19/11/17
 * Time: 23:35
 */

public final class NotificationActivity extends AbstractActivity {
    private AestheticFab mFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);

        initToolbar(true);
        initViews();
        initActions();
        initFragment();
    }

    @Override
    protected void initViews() {
        mFAB = (AestheticFab) findViewById(R.id.na_fab);
    }

    @Override
    protected void initActions() {
        mFAB.setOnClickListener(view -> {});
    }

    @Override
    protected void initFragment() {
        NotificationFragment notificationFragment = (NotificationFragment) getSupportFragmentManager()
                .findFragmentByTag("notificationFragmentTag");

        if (notificationFragment == null) {
            notificationFragment = new NotificationFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.na_fragment_container, notificationFragment, "notificationFragmentTag");
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_all: return true;
            case R.id.action_clear_all: return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showFAB() {
        mFAB.show();
    }

    public void hideFAB() {
        mFAB.hide();
    }
}
