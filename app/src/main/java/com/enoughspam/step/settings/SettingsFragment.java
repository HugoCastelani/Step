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
import com.enoughspam.step.settings.customPreferences.ColorPreference;
import com.enoughspam.step.settings.customPreferences.CustomPreference;

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

        ThemeDAO themeDAO = new ThemeDAO(getActivity());
        ThemeData themeData = themeDAO.getThemeData();

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

        final CustomPreference defaultColor = (CustomPreference) findPreference("default_color");
        defaultColor.setOnPreferenceClickListener(preference -> {
            new MaterialDialog.Builder(getActivity())
                    .title(R.string.restore_default_colors_dialog)
                    .positiveText(R.string.yes_button)
                    .negativeText(R.string.cancel_button)
                    .onPositive(((dialog, which) ->
                        ((SettingsActivity) getActivity()).onColorSelection(
                                null, ContextCompat.getColor(getActivity(), R.color.accent))
                    ))
                    .show();
            return true;
        });
    }
}
