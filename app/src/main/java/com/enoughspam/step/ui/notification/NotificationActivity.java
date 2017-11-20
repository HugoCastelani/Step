package com.enoughspam.step.ui.notification;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.enoughspam.step.R;
import com.enoughspam.step.ui.abstracts.AbstractActivity;

/**
 * Created by Hugo Castelani
 * Date: 19/11/17
 * Time: 23:35
 */

public final class NotificationActivity extends AbstractActivity {

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

    }

    @Override
    protected void initActions() {

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
}
