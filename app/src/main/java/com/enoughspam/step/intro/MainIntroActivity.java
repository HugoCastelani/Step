package com.enoughspam.step.intro;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.blankj.utilcode.util.Utils;
import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.DAOHandler;
import com.enoughspam.step.main.MainActivity;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

public class MainIntroActivity extends IntroActivity {

    private static final String KEY_ALL_SET = "intro_all_set";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.init(this);
        DAOHandler.init(this);

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
                .backgroundDark(R.color.md_grey_50)
                .fragment(R.layout.intro_fragment_about)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.md_grey_50)
                .backgroundDark(R.color.md_grey_50)
                .fragment(new PermissionIntroFragment())
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.md_grey_50)
                .backgroundDark(R.color.md_grey_50)
                .fragment(new LoginIntroFragment())
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.md_grey_50)
                .backgroundDark(R.color.md_grey_50)
                .fragment(new NumberIntroFragment())
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.md_grey_50)
                .backgroundDark(R.color.md_grey_50)
                .fragment(new ConfirmationIntroFragment())
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.md_grey_50)
                .backgroundDark(R.color.md_grey_50)
                .fragment(new ReadyIntroFragment())
                .build());
    }
}