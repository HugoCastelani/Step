package com.hugocastelani.blockbook.ui.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;

import com.afollestad.aesthetic.AestheticButton;
import com.afollestad.aesthetic.AestheticProgressBar;
import com.afollestad.aesthetic.AestheticTextView;
import com.blankj.utilcode.util.Utils;
import com.hugocastelani.blockbook.R;
import com.hugocastelani.blockbook.database.dao.DAOHandler;
import com.hugocastelani.blockbook.persistence.HockeyProvider;
import com.hugocastelani.blockbook.ui.intangible.AbstractActivity;
import com.hugocastelani.blockbook.ui.intro.MainIntroActivity;
import com.hugocastelani.blockbook.ui.main.MainActivity;
import com.hugocastelani.blockbook.util.AnimUtils;
import com.hugocastelani.blockbook.util.Listeners;
import com.hugocastelani.blockbook.util.NetworkUtils;
import com.orhanobut.hawk.Hawk;

import java.util.Timer;
import java.util.TimerTask;

public final class SplashScreenActivity extends AbstractActivity {
    private AestheticTextView mTextView;
    private AestheticButton mButton;
    private AestheticProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        DAOHandler.init(getApplicationContext());
        Utils.init(getApplication());
        Hawk.init(getApplicationContext()).build();
        NetworkUtils.init();

        initViews();
        initActions();

        testConnection();
    }

    @Override
    protected void initViews() {
        mTextView = (AestheticTextView) findViewById(R.id.ssa_text_view);
        mButton = (AestheticButton) findViewById(R.id.ssa_button);
        mProgressBar = (AestheticProgressBar) findViewById(R.id.ssa_progress_bar);
    }

    @Override
    protected void initActions() {
        mButton.setOnClickListener(view -> {
            AnimUtils.fadeOutFadeIn(mButton, mProgressBar);
            mTextView.setText(getResources().getText(R.string.connecting));

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    testConnection();
                }
            }, 1000);
        });
    }

    @Override
    protected void initFragment() {}

    private void testConnection() {
        if (NetworkUtils.isConnectedToInternet()) {
            final Integer[] listenerAmount = {1};

            Listeners.AnswerListener listener = new Listeners.AnswerListener() {
                int count = 0;

                @Override
                public void onAnswerRetrieved() {
                    if (++count == listenerAmount[0]) {
                        if (listenerAmount[0] == 1) {
                            startMainIntroActivity();
                        } else {
                            startMainActivity();
                        }
                    }
                }

                @Override public void onError() {}
            };

            if (Hawk.get(HockeyProvider.IS_INTRO_ALL_SET, HockeyProvider.IS_INTRO_ALL_SET_DF)) {
                DAOHandler.syncDynamicTables(listener);
                listenerAmount[0] = 2;
            }

            DAOHandler.syncStaticTables(listener);

        } else {

            AnimUtils.fadeOutFadeIn(mProgressBar, mButton);
            mTextView.setText(getResources().getText(R.string.no_internet_connection));
        }
    }

    private void startMainActivity() {
        final Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void startMainIntroActivity() {
        final Intent intent = new Intent(SplashScreenActivity.this, MainIntroActivity.class);
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getBaseContext(),
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        startActivity(intent, bundle);
        finish();
    }
}
