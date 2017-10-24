package com.enoughspam.step.intro;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.enoughspam.step.R;
import com.enoughspam.step.abstracts.AbstractActivity;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.main.MainActivity;

public class SplashScreenActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        DAOHandler.init(getBaseContext());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                DAOHandler.AnswerListener listener = new DAOHandler.AnswerListener() {
                    int count = 0;

                    @Override
                    public void onAnswerRetrieved() {
                        if (++count == 2) {
                            final Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                };

                DAOHandler.syncStaticTables(listener);
                DAOHandler.syncDynamicTables(listener);

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
