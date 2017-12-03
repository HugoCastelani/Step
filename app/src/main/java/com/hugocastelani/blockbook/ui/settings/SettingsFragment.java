package com.hugocastelani.blockbook.ui.settings;

import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v4.content.ContextCompat;
import android.widget.ListView;

import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.afollestad.materialdialogs.prefs.MaterialDialogPreference;
import com.afollestad.materialdialogs.prefs.MaterialMultiSelectListPreference;
import com.hugocastelani.blockbook.R;
import com.hugocastelani.blockbook.persistence.HockeyProvider;
import com.hugocastelani.blockbook.ui.settings.preference.ColorPreference;
import com.hugocastelani.blockbook.util.ThemeHandler;
import com.hugocastelani.blockbook.util.decorator.ListDecorator;
import com.orhanobut.hawk.Hawk;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by Hugo Castelani
 * Date: 14/07/17
 * Time: 21:16
 */

public final class SettingsFragment extends PreferenceFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        final ListView listView = (ListView) getView().findViewById(android.R.id.list);
        listView.setDivider(null);    // remove dividers

        ListDecorator.addAdaptableMargins(listView);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        // theme switch preference

        final SwitchPreference themeSwitch = (SwitchPreference) findPreference("theme_switch");
        themeSwitch.setChecked(Hawk.get(HockeyProvider.IS_DARK, HockeyProvider.IS_DARK_DF));
        themeSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
            // isChecked returns situation before changing
            final Boolean isChecked = !((SwitchPreference) preference).isChecked();

            Hawk.put(HockeyProvider.IS_DARK, isChecked);
            Aesthetic.get().isDark(isChecked).apply();

            updateTheme();
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
                        .onPositive(((dialog, which) -> {
                            if (ThemeHandler.isDark()) {
                                ((SettingsActivity) getActivity()).onColorSelection(
                                        null, ContextCompat.getColor(getActivity(), R.color.colorAccentDark));

                            } else {

                                ((SettingsActivity) getActivity()).onColorSelection(
                                        null, ContextCompat.getColor(getActivity(), R.color.accent));
                            }
                        }))
        );

        // networks preference

        final MaterialMultiSelectListPreference networks = (MaterialMultiSelectListPreference) findPreference("select_networks");
        networks.setBuilder(networks.resetBuilder()
                .backgroundColor(ThemeHandler.getBackground())
                .positiveText(R.string.done_button)
                .positiveColor(ThemeHandler.getAccent())
                .negativeText(R.string.cancel_button)
                .negativeColor(ThemeHandler.getAccent())
                .onPositive((dialog, which) -> {
                    Integer[] indices = dialog.getSelectedIndices();
                    Hawk.put(HockeyProvider.NETWORKS, indices);
                })
        );

        // services preference

        final MaterialMultiSelectListPreference services = (MaterialMultiSelectListPreference) findPreference("select_services");
        services.setBuilder(services.resetBuilder()
                .backgroundColor(ThemeHandler.getBackground())
                .positiveText(R.string.done_button)
                .positiveColor(ThemeHandler.getAccent())
                .negativeText(R.string.cancel_button)
                .negativeColor(ThemeHandler.getAccent())
                .onPositive((dialog, which) -> {
                    Integer[] indices = dialog.getSelectedIndices();
                    Hawk.put(HockeyProvider.SERVICES, indices);
                })
        );

        // theme switch preference

        final SwitchPreference feedbackSwitch = (SwitchPreference) findPreference("select_feedback_options");
        feedbackSwitch.setChecked(Hawk.get(HockeyProvider.SHOW_FEEDBACK, HockeyProvider.SHOW_FEEDBACK_DF));
        feedbackSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
            // isChecked returns situation before changing
            final Boolean isChecked = !((SwitchPreference) preference).isChecked();
            Hawk.put(HockeyProvider.SHOW_FEEDBACK, isChecked);
            return true;
        });

        // denunciation amount preference

        final Preference denunciationAmount = findPreference("select_denunciation_amount");
        denunciationAmount.setOnPreferenceClickListener(preference -> {
            final MaterialNumberPicker numberPicker = new MaterialNumberPicker.Builder(getActivity())
                    .minValue(5)
                    .maxValue(100)
                    .defaultValue(Hawk.get(HockeyProvider.DENUNCIATION_AMOUNT, HockeyProvider.DENUNCIATION_AMOUNT_DF))
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
                            Hawk.put(HockeyProvider.DENUNCIATION_AMOUNT, numberPicker.getValue()))
                    .onNeutral((dialog, which) ->
                            Hawk.put(HockeyProvider.DENUNCIATION_AMOUNT, HockeyProvider.DENUNCIATION_AMOUNT_DF))
                    .show();

            return true;
        });
    }

    private void updateTheme() {
        if (ThemeHandler.isDark()) {
            Aesthetic.get()
                    .activityTheme(R.style.AppThemeDark)
                    .colorPrimaryRes(R.color.colorPrimaryInverse)
                    .colorWindowBackgroundRes(R.color.colorWindowBackgroundInverse)
                    .textColorPrimaryRes(R.color.textColorPrimaryInverse)
                    .textColorSecondaryRes(R.color.textColorSecondaryInverse)
                    .colorIconTitleActiveRes(R.color.textColorPrimaryInverse)
                    .colorIconTitleInactiveRes(R.color.textColorPrimaryInverse)
                    .colorCardViewBackgroundRes(R.color.colorCardBackgroundInverse)
                    .apply();

            if (Build.VERSION.SDK_INT >= M) {
                Aesthetic.get()
                        .colorStatusBarRes(R.color.colorPrimaryInverse)
                        .apply();
            }

        } else {

            Aesthetic.get()
                    .activityTheme(R.style.AppTheme)
                    .colorPrimaryRes(R.color.colorPrimary)
                    .colorWindowBackgroundRes(R.color.colorWindowBackground)
                    .textColorPrimaryRes(R.color.textColorPrimary)
                    .textColorSecondaryRes(R.color.textColorSecondary)
                    .colorIconTitleActiveRes(R.color.textColorPrimary)
                    .colorIconTitleInactiveRes(R.color.textColorPrimary)
                    .colorCardViewBackgroundRes(R.color.colorCardBackground)
                    .apply();

            if (Build.VERSION.SDK_INT >= M) {
                Aesthetic.get()
                        .colorStatusBarRes(R.color.colorPrimary)
                        .apply();
            }
        }
    }
}
