package com.enoughspam.step.intro;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enoughspam.step.R;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;

public class ReadyIntroFragment extends SlideFragment {

    private boolean mCanGoForward = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.intro_fragment_ready, container, false);
        mCanGoForward = true;
        return view;
    }

    // temporary
    @Override
    public void onDestroy() {
        final Intent intent = new Intent(getActivity(), SplashScreenActivity.class);
        startActivity(intent);
        getActivity().finish();
        super.onDestroy();
    }

    @Override
    public boolean canGoForward() {
        return mCanGoForward;
    }

    @Override
    public boolean canGoBackward() {
        return false;
    }
}
