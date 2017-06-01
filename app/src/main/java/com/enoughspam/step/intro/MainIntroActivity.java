package com.enoughspam.step.intro;

import android.Manifest;
import android.os.Bundle;
import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.notRelated.IntroDAO;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class MainIntroActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(new SimpleSlide.Builder()
                .title(R.string.title_1)
                .description(R.string.description_1)
                .background(R.color.md_grey_50)
                .backgroundDark(R.color.md_grey_500)
                .scrollable(false)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.title_2)
                .description(R.string.description_2)
                .background(R.color.md_blue_grey_500)
                .backgroundDark(R.color.md_blue_grey_700)
                .scrollable(false)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.title_3)
                .description(R.string.description_3)
                .background(R.color.colorPrimaryInverse)
                .backgroundDark(R.color.colorWindowBackgroundInverse)
                .scrollable(false)
                .build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IntroDAO introDAO = new IntroDAO(this);

        introDAO.setShowIntro(false);
    }
}
