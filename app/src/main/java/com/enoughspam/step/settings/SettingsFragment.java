package com.enoughspam.step.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.content.ContextCompat;

import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.enoughspam.step.R;
import com.enoughspam.step.settings.preferences.ColorPreference;
import com.enoughspam.step.settings.preferences.CustomPreference;
import com.enoughspam.step.settings.preferences.SwitchPreference;
import com.enoughspam.step.util.ThemeHandler;

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

        // theme switch preference

        final SwitchPreference switchPreference = (SwitchPreference) findPreference("theme_switch");
        if (ThemeHandler.isDark()) {
            switchPreference.setChecked(true);
        }
        switchPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            if (((SwitchPreference) preference).isChecked()) {
                Aesthetic.get().isDark(true).apply();
            } else {
                Aesthetic.get().isDark(false).apply();
            }

            return true;
        });

        // accent color preference

        final ColorPreference selectAccentColor = (ColorPreference) findPreference("select_accent_color");
        selectAccentColor.setColor(ThemeHandler.getAccent());
        selectAccentColor.setOnPreferenceClickListener(preference -> {
            new ColorChooserDialog.Builder((SettingsActivity) getActivity(), R.string.select_color)
                    .preselect(ThemeHandler.getAccent())
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
                    .backgroundColor(ThemeHandler.getBackground())
                    .positiveText(R.string.yes_button)
                    .positiveColor(ThemeHandler.getAccent())
                    .negativeText(R.string.cancel_button)
                    .negativeColor(ThemeHandler.getAccent())
                    .onPositive(((dialog, which) ->
                        ((SettingsActivity) getActivity()).onColorSelection(
                                null, ContextCompat.getColor(getActivity(), R.color.accent))
                    ))
                    .show();
            return true;
        });
    }
}
