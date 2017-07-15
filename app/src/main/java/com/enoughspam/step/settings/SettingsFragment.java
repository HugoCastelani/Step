package com.enoughspam.step.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.enoughspam.step.R;

/**
 * Created by hugo
 * Date: 14/07/17
 * Time: 21:16
 */

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
