package com.enoughspam.step.intro;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.afollestad.aesthetic.Aesthetic;
import com.blankj.utilcode.util.Utils;
import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.DAOHandler;
import com.enoughspam.step.main.MainActivity;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import static android.os.Build.VERSION_CODES.M;

public class MainIntroActivity extends IntroActivity {

    private static final String KEY_ALL_SET = "intro_all_set";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Aesthetic.attach(this);
        super.onCreate(savedInstanceState);

        Utils.init(this);
        DAOHandler.init(this);
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
            final Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

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