package com.hugocastelani.blockbook.ui.splashscreen;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.Utils;
import com.hugocastelani.blockbook.R;
import com.hugocastelani.blockbook.database.dao.DAOHandler;
import com.hugocastelani.blockbook.ui.intangible.AbstractActivity;
import com.hugocastelani.blockbook.ui.main.MainActivity;
import com.hugocastelani.blockbook.util.Listeners;
import com.orhanobut.hawk.Hawk;

import java.util.Timer;
import java.util.TimerTask;

public final class SplashScreenActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        DAOHandler.init(getApplicationContext());
        Utils.init(getApplication());
        Hawk.init(getApplicationContext()).build();

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

                    createSnackbar(R.string.splash_screen_no_internet_connection).show();
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
