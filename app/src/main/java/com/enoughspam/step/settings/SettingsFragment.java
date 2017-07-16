package com.enoughspam.step.settings;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.notRelated.ThemeDAO;
import com.enoughspam.step.settings.customPreferences.ColorPreference;

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

        final String accentColor = new ThemeDAO(getActivity()).getThemeData().getAccentColor();

        final ColorPreference colorPreference = (ColorPreference) findPreference("accent_color");
        colorPreference.setColor(accentColor);
        colorPreference.setOnPreferenceClickListener(preference -> {
            new ColorChooserDialog.Builder((SettingsActivity) getActivity(), R.string.select_color)
                    .preselect(Color.parseColor(accentColor))
                    .customButton(R.string.custom_button)
                    .presetsButton(R.string.presets_button)
                    .cancelButton(R.string.cancel_button)
                    .doneButton(R.string.done_button)
                    .backButton(R.string.back_button)
                    .show();
            return true;
        });
    }
}
