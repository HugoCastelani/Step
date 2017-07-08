package com.enoughspam.step.intro;

import android.content.Intent;
import android.os.Bundle;

import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.related.PersonalDAO;
import com.enoughspam.step.main.MainActivity;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

public class MainIntroActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        PersonalDAO personalDAO = new PersonalDAO(this);
        if (personalDAO.get() != null) {
            Intent intent = new Intent(this, MainActivity.class);
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

        addSlide(new SimpleSlide.Builder()
                .title(R.string.title_5)
                .image(R.drawable.intro_5)
                .description(R.string.description_5)
                .background(R.color.md_grey_50)
                .backgroundDark(R.color.md_grey_50)
                .canGoBackward(false)
                .scrollable(false)
                .build());
    }
}
