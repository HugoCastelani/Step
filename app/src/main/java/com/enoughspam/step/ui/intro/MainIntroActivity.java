package com.enoughspam.step.ui.intro;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;

import com.afollestad.aesthetic.Aesthetic;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.DAOHandler;
import com.enoughspam.step.ui.abstracts.SnackbarTrigger;
import com.enoughspam.step.ui.splashscreen.SplashScreenActivity;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import static android.os.Build.VERSION_CODES.M;

public final class MainIntroActivity extends IntroActivity implements SnackbarTrigger {

    private static final String KEY_ALL_SET = "intro_all_set";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Aesthetic.attach(this);
        super.onCreate(savedInstanceState);

        Utils.init(getApplication());
        DAOHandler.init(getBaseContext());

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

        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(KEY_ALL_SET, false)) {
            final Intent intent = new Intent(this, SplashScreenActivity.class);
            startActivity(intent);
            finish();
        }

        setButtonBackFunction(BUTTON_BACK_FUNCTION_BACK);

        addSlide(new FragmentSlide.Builder()
                .background(R.color.md_grey_50)
                .backgroundDark(R.color.md_grey_200)
                .fragment(new AboutIntroFragment())
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
                .fragment(new NumberIntroFragment())
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
}