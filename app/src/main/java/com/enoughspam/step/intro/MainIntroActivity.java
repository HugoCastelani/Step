package com.enoughspam.step.intro;

import android.os.Bundle;
import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.notRelated.IntroDAO;
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
                .fragment(R.layout.about_intro_slide)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.md_grey_50)
                .backgroundDark(R.color.md_grey_50)
                .fragment(R.layout.permission_intro_slide)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.title_3)
                .image(R.drawable.intro_3)
                .background(R.color.md_grey_50)
                .backgroundDark(R.color.md_grey_50)
                .scrollable(false)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.title_4)
                .image(R.drawable.intro_4)
                .description(R.string.description_4)
                .background(R.color.md_grey_50)
                .backgroundDark(R.color.md_grey_50)
                .canGoBackward(false)
                .scrollable(false)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.title_5)
                .image(R.drawable.intro_5)
                .description(R.string.description_5)
                .background(R.color.md_cyan_600)
                .backgroundDark(R.color.md_cyan_600)
                .canGoBackward(false)
                .scrollable(false)
                .build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IntroDAO introDAO = new IntroDAO(this);
        introDAO.setShowIntro(false);
    }

    @Override
    public void onBackPressed() {

        /*
         * Android creates previous, actual and posterior fragment,
         * so I must test third fragment visibility as well
         */

        if (getSlide(0).getFragment().isVisible() && !(getSlide(2).getFragment().isVisible())) {
            System.exit(1);
        } else {
            super.onBackPressed();
        }
    }
}
