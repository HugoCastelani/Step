package com.hugocastelani.blockbook.ui.intro;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;

import com.afollestad.aesthetic.Aesthetic;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.hugocastelani.blockbook.R;
import com.hugocastelani.blockbook.database.dao.DAOHandler;
import com.hugocastelani.blockbook.ui.intangible.SnackbarTrigger;
import com.orhanobut.hawk.Hawk;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterConfig;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import static android.os.Build.VERSION_CODES.M;

public final class MainIntroActivity extends IntroActivity implements SnackbarTrigger {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Aesthetic.attach(this);
        super.onCreate(savedInstanceState);

        Hawk.init(getApplicationContext()).build();
        DAOHandler.init(getApplicationContext());
        Utils.init(getApplication());

        Twitter.initialize(new TwitterConfig.Builder(getApplicationContext()).build());

        FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));
        FacebookSdk.setApplicationName(getResources().getString(R.string.facebook_app_name));
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        if (Aesthetic.isFirstTime()) initAesthetic();

        KeyboardVisibilityEvent.setEventListener(
                this,
                isOpen -> {
                    if (isOpen) {
                        setButtonBackVisible(false);
                        setButtonNextVisible(false);

                    } else {

                        setButtonBackVisible(true);
                        setButtonNextVisible(true);
                    }
                }
        );

        setButtonBackFunction(BUTTON_BACK_FUNCTION_BACK);

        addSlide(new FragmentSlide.Builder()
                .background(R.color.md_grey_50)
                .backgroundDark(R.color.md_grey_200)
                .fragment(R.layout.intro_fragment_about)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.md_grey_50)
                .backgroundDark(R.color.md_grey_200)
                .fragment(new PermissionIntroFragment())
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.md_grey_50)
                .backgroundDark(R.color.md_grey_200)
                .fragment(new LoginIntroFragment())
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.md_grey_50)
                .backgroundDark(R.color.md_grey_200)
                .fragment(new PhoneIntroFragment())
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.md_grey_50)
                .backgroundDark(R.color.md_grey_200)
                .fragment(new ConfirmationIntroFragment())
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.md_grey_50)
                .backgroundDark(R.color.md_grey_200)
                .fragment(new ReadyIntroFragment())
                .build());
    }

    private LoginIntroFragment mLoginIntroFragment;
    private PhoneIntroFragment mPhoneIntroFragment;
    private ConfirmationIntroFragment mConfirmationIntroFragment;
    private ReadyIntroFragment mReadyIntroFragment;

    public MainIntroActivity setLoginSlide(@NonNull final LoginIntroFragment loginIntroFragment) {
        mLoginIntroFragment = loginIntroFragment;
        return this;
    }

    public LoginIntroFragment getLoginSlide() {
        return mLoginIntroFragment;
    }

    public MainIntroActivity setPhoneSlide(@NonNull final PhoneIntroFragment phoneIntroFragment) {
        mPhoneIntroFragment = phoneIntroFragment;
        return this;
    }

    public PhoneIntroFragment getPhoneSlide() {
        return mPhoneIntroFragment;
    }

    public MainIntroActivity setConfirmationSlide(@NonNull final ConfirmationIntroFragment confirmationIntroFragment) {
        mConfirmationIntroFragment = confirmationIntroFragment;
        return this;
    }

    public ConfirmationIntroFragment getConfirmationSlide() {
        return mConfirmationIntroFragment;
    }

    public MainIntroActivity setReadySlide(@NonNull final ReadyIntroFragment readyIntroFragment) {
        mReadyIntroFragment = readyIntroFragment;
        return this;
    }

    public ReadyIntroFragment getReadySlide() {
        return mReadyIntroFragment;
    }

    private void initAesthetic() {
        Aesthetic.get()
                .activityTheme(R.style.AppTheme)
                .isDark(false)
                .colorPrimaryRes(R.color.colorPrimary)
                .colorStatusBarRes(R.color.colorPrimaryDark)
                .colorAccentRes(R.color.colorAccent)
                .colorWindowBackgroundRes(R.color.colorWindowBackground)
                .textColorPrimaryRes(R.color.textColorPrimary)
                .textColorSecondaryRes(R.color.textColorSecondary)
                .colorIconTitleActiveRes(R.color.textColorPrimary)
                .colorIconTitleInactiveRes(R.color.textColorPrimary)
                .colorCardViewBackgroundRes(R.color.colorCardBackground)
                .apply();

        if (Build.VERSION.SDK_INT >= M) {
            Aesthetic.get()
                    .colorStatusBarRes(R.color.colorPrimaryDark)
                    .apply();
        }
    }

    @Override
    public Snackbar createSnackbar(final @NonNull @StringRes Integer message) {
        return buildSnackbar(getResources().getString(message));
    }

    @Override
    public Snackbar createSnackbar(final @NonNull String message) {
        return buildSnackbar(message);
    }

    @NonNull
    public Snackbar buildSnackbar(final @NonNull String message) {
        return Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
    }

    @Override
    public Snackbar createSnackbarAndClose(final @NonNull @StringRes Integer message) {
        final Snackbar snackbar = createSnackbar(message);

        snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                ActivityUtils.finishActivity(MainIntroActivity.this);
            }
        });

        return snackbar;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Aesthetic.resume(this);
    }

    @Override
    protected void onPause() {
        Aesthetic.pause(this);
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLoginIntroFragment.onActivityResult(requestCode, resultCode, data);
    }
}