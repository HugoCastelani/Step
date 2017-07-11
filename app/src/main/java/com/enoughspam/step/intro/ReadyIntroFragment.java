package com.enoughspam.step.intro;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enoughspam.step.R;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;

public class ReadyIntroFragment extends SlideFragment {

    private boolean canGoForward = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intro_fragment_ready, container, false);

        /*Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();*/

        return view;
    }

    @Override
    public boolean canGoForward() {
        return canGoForward;
    }

    @Override
    public boolean canGoBackward() {
        return false;
    }
}
