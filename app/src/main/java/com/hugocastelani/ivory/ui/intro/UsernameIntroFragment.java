package com.hugocastelani.ivory.ui.intro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.hugocastelani.ivory.R;

/**
 * Created by Hugo Castelani
 * Date: 08/12/17
 * Time: 01:32
 */

public class UsernameIntroFragment extends SlideFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.intro_fragment_username, container, false);
        return view;
    }
}
