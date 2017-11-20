package com.enoughspam.step.ui.splashscreen;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.Utils;
import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.DAOHandler;
import com.enoughspam.step.ui.abstracts.AbstractActivity;
import com.enoughspam.step.ui.main.MainActivity;
import com.enoughspam.step.util.Listeners;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        DAOHandler.init(getBaseContext());
        Utils.init(getApplication());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (NetworkUtils.isConnected()) {
                    Listeners.AnswerListener listener = new Listeners.AnswerListener() {
                        int count = 0;

                        @Override
                        public void onAnswerRetrieved() {
                            if (++count == 2) {
                                startMainActivity();
                            }
                        }

                        @Override public void onError() {}
                    };

                    DAOHandler.syncStaticTables(listener);
                    DAOHandler.syncDynamicTables(listener);

                } else {

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            startMainActivity();
                        }

                    }, 2000);

                    Snackbar.make(findViewById(android.R.id.content),
                            getResources().getString(R.string.splash_screen_no_internet_connection),
                            2000).show();
                }

                return null;
            }
        }.execute();
    }

    private void startMainActivity() {
        final Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void initViews() {}

    @Override
    protected void initActions() {}

    @Override
    protected void initFragment() {}
}
