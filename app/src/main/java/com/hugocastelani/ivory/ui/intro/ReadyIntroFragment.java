package com.hugocastelani.ivory.ui.intro;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.hugocastelani.ivory.R;
import com.hugocastelani.ivory.ui.splashscreen.SplashScreenActivity;

public final class ReadyIntroFragment extends SlideFragment {

    private MainIntroActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.intro_fragment_ready, container, false);
        mActivity = (MainIntroActivity) getIntroActivity();

        mActivity.setReadySlide(this);

        return view;
    }

    public void prepareNextButton() {
        getIntroActivity().setButtonNextOnClickListener(view1 -> {
            final Intent intent = new Intent(getActivity(), SplashScreenActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
    }

    @Override
    public boolean canGoBackward() {
        return false;
    }
}
