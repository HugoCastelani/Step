package com.enoughspam.step.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v4.content.ContextCompat;

import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.afollestad.materialdialogs.prefs.MaterialDialogPreference;
import com.afollestad.materialdialogs.prefs.MaterialMultiSelectListPreference;
import com.enoughspam.step.R;
import com.enoughspam.step.settings.preferences.ColorPreference;
import com.enoughspam.step.util.ThemeHandler;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;

/**
 * Created by Hugo Castelani
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
        switchPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            // is checked returns situation before changing
            Aesthetic.get().isDark(!((SwitchPreference) preference).isChecked()).apply();
            return true;
        });

        // accent color preference

        final ColorPreference selectAccentColor = (ColorPreference) findPreference("select_accent_color");
        selectAccentColor.setColor(ThemeHandler.getAccent());
        selectAccentColor.setOnPreferenceClickListener(preference -> {
            new ColorChooserDialog.Builder(getActivity(), R.string.select_color)
                    .preselect(ThemeHandler.getAccent())
                    .customButton(R.string.custom_button)
                    .presetsButton(R.string.presets_button)
                    .cancelButton(R.string.cancel_button)
                    .doneButton(R.string.done_button)
                    .backButton(R.string.back_button)
                    .show((SettingsActivity) getActivity());
            return true;
        });

        // default color preference

        final MaterialDialogPreference defaultColor = (MaterialDialogPreference) findPreference("default_color");
        defaultColor.setBuilder(
                defaultColor.resetBuilder()
                        .backgroundColor(ThemeHandler.getBackground())
                        .positiveText(R.string.done_button)
                        .positiveColor(ThemeHandler.getAccent())
                        .negativeText(R.string.cancel_button)
                        .negativeColor(ThemeHandler.getAccent())
                        .onPositive(((dialog, which) ->
                                ((SettingsActivity) getActivity()).onColorSelection(
                                        null, ContextCompat.getColor(getActivity(), R.color.accent))
                        ))
        );

        // networks preference

        final MaterialMultiSelectListPreference networks = (MaterialMultiSelectListPreference) findPreference("select_networks");
        networks.setBuilder(
                networks.resetBuilder()
                        .backgroundColor(ThemeHandler.getBackground())
                        .positiveText(R.string.done_button)
                        .positiveColor(ThemeHandler.getAccent())
                        .negativeText(R.string.cancel_button)
                        .negativeColor(ThemeHandler.getAccent())
        );

        // services preference

        final MaterialMultiSelectListPreference services = (MaterialMultiSelectListPreference) findPreference("select_services");
        services.setBuilder(
                services.resetBuilder()
                        .backgroundColor(ThemeHandler.getBackground())
                        .positiveText(R.string.done_button)
                        .positiveColor(ThemeHandler.getAccent())
                        .negativeText(R.string.cancel_button)
                        .negativeColor(ThemeHandler.getAccent())
        );

        // denunciation amount preference

        final Preference denunciationAmount = findPreference("select_denunciation_amount");
        denunciationAmount.setOnPreferenceClickListener(preference -> {
            final SharedPreferences sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(getActivity());

            final MaterialNumberPicker numberPicker = new MaterialNumberPicker.Builder(getActivity())
                    .minValue(5)
                    .maxValue(100)
                    .defaultValue(sharedPreferences.getInt("select_denunciation_amount", 20))
                    .backgroundColor(ThemeHandler.getBackground())
                    .separatorColor(ThemeHandler.getSecondaryText())
                    .textColor(ThemeHandler.getPrimaryText())
                    .wrapSelectorWheel(false)
                    .textSize(16F)
                    .build();

            new MaterialDialog.Builder(getActivity())
                    .title(denunciationAmount.getTitle())
                    .customView(numberPicker, false)
                    .backgroundColor(ThemeHandler.getBackground())
                    .positiveText(R.string.done_button)
                    .positiveColor(ThemeHandler.getAccent())
                    .neutralText(R.string.default_button)
                    .neutralColor(ThemeHandler.getAccent())
                    .negativeText(R.string.cancel_button)
                    .negativeColor(ThemeHandler.getAccent())
                    .contentColor(ThemeHandler.getPrimaryText())
                    .titleColor(ThemeHandler.getPrimaryText())
                    .onPositive((dialog, which) ->
                            sharedPreferences.edit().putInt(
                                    "select_denunciation_amount", numberPicker.getValue()).apply())
                    .onNeutral((dialog, which) ->
                            sharedPreferences.edit().putInt(
                                    "select_denunciation_amount", 20).apply()) // 20 is the default value
                    .show();

            return true;
        });
    }
}
