package com.enoughspam.step.settings;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.content.ContextCompat;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.notRelated.ThemeDAO;
import com.enoughspam.step.database.domains.ThemeData;
import com.enoughspam.step.settings.preferences.ColorPreference;
import com.enoughspam.step.settings.preferences.CustomPreference;
import com.enoughspam.step.settings.preferences.SwitchPreference;

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

        final ThemeDAO themeDAO = new ThemeDAO(getActivity());
        final ThemeData themeData = themeDAO.getThemeData();

        // theme switch preference

        final SwitchPreference switchPreference = (SwitchPreference) findPreference("theme_switch");
        if (themeData.isDark()) {
            switchPreference.setChecked(true);
        }
        switchPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            if (((SwitchPreference) preference).isChecked()) {
                themeData.setIsDark(true);
            } else {
                themeData.setIsDark(false);
            }
            themeDAO.setThemeData(themeData);

            return true;
        });

        // accent color preference

        final ColorPreference selectAccentColor = (ColorPreference) findPreference("select_accent_color");
        selectAccentColor.setColor(themeData.getAccentColor());
        selectAccentColor.setOnPreferenceClickListener(preference -> {
            new ColorChooserDialog.Builder((SettingsActivity) getActivity(), R.string.select_color)
                    .preselect(Color.parseColor(themeData.getAccentColor()))
                    .customButton(R.string.custom_button)
                    .presetsButton(R.string.presets_button)
                    .cancelButton(R.string.cancel_button)
                    .doneButton(R.string.done_button)
                    .backButton(R.string.back_button)
                    .show();
            return true;
        });

        // default color preference

        final CustomPreference defaultColor = (CustomPreference) findPreference("default_color");
        defaultColor.setOnPreferenceClickListener(preference -> {
            new MaterialDialog.Builder(getActivity())
                    .title(R.string.restore_default_colors_dialog)
                    .content(R.string.restore_default_colors_dialog_substring)
                    .positiveText(R.string.yes_button)
                    .positiveColor(Color.parseColor(themeData.getAccentColor()))
                    .negativeText(R.string.cancel_button)
                    .negativeColor(Color.parseColor(themeData.getAccentColor()))
                    .onPositive(((dialog, which) ->
                        ((SettingsActivity) getActivity()).onColorSelection(
                                null, ContextCompat.getColor(getActivity(), R.color.accent))
                    ))
                    .show();
            return true;
        });
    }
}
