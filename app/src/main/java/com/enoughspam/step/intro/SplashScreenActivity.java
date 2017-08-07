package com.enoughspam.step.intro;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.enoughspam.step.R;
import com.enoughspam.step.abstracts.AbstractActivity;
import com.enoughspam.step.main.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                // UPDATE DATABASE

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        final Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 1000);   // this activity must be shown for at least 1 second

                return null;
            }
        }.execute();
    }

    @Override
    protected void initViews() {}

    @Override
    protected void initActions() {}

    @Override
    protected void initFragment() {}
}
