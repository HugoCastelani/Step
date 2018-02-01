package com.hugocastelani.ivory.ui.intangible;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;

import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.AestheticActivity;
import com.afollestad.aesthetic.AestheticToolbar;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.hugocastelani.ivory.R;
import com.hugocastelani.ivory.util.ThemeHandler;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static android.os.Build.VERSION_CODES.M;

/**
 * Created by Hugo Castelani
 * Date: 25/07/17
 * Time: 19:09
 */

public abstract class AbstractActivity extends AestheticActivity implements SnackbarTrigger {

    private AestheticToolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Aesthetic.isFirstTime()) initAesthetic();
        else updateTheme();
    }

    protected void initToolbar(final boolean backButtonEnabled) {
        mToolbar = (AestheticToolbar) findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= LOLLIPOP) mToolbar.setElevation(ConvertUtils.dp2px(4));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(backButtonEnabled);
    }

    abstract protected void initViews();

    abstract protected void initActions();

    abstract protected void initFragment();

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
        return Snackbar.make(findViewById(R.id.content), message, Snackbar.LENGTH_LONG);
    }

    @Override
    public Snackbar createSnackbarAndClose(final @NonNull @StringRes Integer message) {
        final Snackbar snackbar = createSnackbar(message);

        snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                ActivityUtils.finishActivity(AbstractActivity.this);
            }
        });

        return snackbar;
    }

    @Nullable
    protected AestheticToolbar getToolbar() {
        return mToolbar;
    }

    protected void initAesthetic() {
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

    protected void updateTheme() {
        if (ThemeHandler.isDark()) {
            Aesthetic.get()
                    .activityTheme(R.style.AppThemeDark)
                    .colorPrimaryRes(R.color.colorPrimaryInverse)
                    .colorWindowBackgroundRes(R.color.colorWindowBackgroundInverse)
                    .textColorPrimaryRes(R.color.textColorPrimaryInverse)
                    .textColorSecondaryRes(R.color.textColorSecondaryInverse)
                    .colorIconTitleActiveRes(R.color.textColorPrimaryInverse)
                    .colorIconTitleInactiveRes(R.color.textColorPrimaryInverse)
                    .colorCardViewBackgroundRes(R.color.colorCardBackgroundInverse)
                    .apply();

            if (Build.VERSION.SDK_INT >= M) {
                Aesthetic.get()
                        .colorStatusBarRes(R.color.colorPrimaryDarkInverse)
                        .apply();
            }

        } else {

            Aesthetic.get()
                    .activityTheme(R.style.AppTheme)
                    .colorPrimaryRes(R.color.colorPrimary)
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
    }

}
