package com.enoughspam.step.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.afollestad.aesthetic.Aesthetic;
import com.enoughspam.step.R;


public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        /*Aesthetic.get().primaryColor().take(1).subscribe(color -> {

        });*/

        return view;
    }
}
