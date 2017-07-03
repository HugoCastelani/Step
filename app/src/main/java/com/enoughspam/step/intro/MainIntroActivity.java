package com.enoughspam.step.intro;

import android.os.Bundle;
import com.enoughspam.step.R;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class MainIntroActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
