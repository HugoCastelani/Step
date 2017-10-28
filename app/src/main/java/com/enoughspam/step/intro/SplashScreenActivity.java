package com.enoughspam.step.intro;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;

import com.enoughspam.step.R;
import com.enoughspam.step.abstracts.AbstractActivity;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.main.MainActivity;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        DAOHandler.init(getBaseContext());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (isConnected()) {
                    DAOHandler.AnswerListener listener = new DAOHandler.AnswerListener() {
                        int count = 0;

                        @Override
                        public void onAnswerRetrieved() {
                            if (++count == 2) {
                                startMainActivity();
                            }
                        }
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

    private boolean isConnected() {
        final Runtime runtime = Runtime.getRuntime();
        try {
            final Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e) { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }

    @Override
    protected void initViews() {}

    @Override
    protected void initActions() {}

    @Override
    protected void initFragment() {}
}
