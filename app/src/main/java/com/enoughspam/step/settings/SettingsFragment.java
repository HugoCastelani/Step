package com.enoughspam.step.settings;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.NavigationViewMode;
import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.notRelated.ThemeDAO;
import com.enoughspam.step.database.domains.ThemeData;

import static android.os.Build.VERSION_CODES.M;


public class SettingsFragment extends Fragment {

    // fragment view
    private View view;

    // data access objects
    private ThemeDAO themeDAO;
    private ThemeData themeData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        themeDAO = new ThemeDAO(view.getContext());
        themeData = themeDAO.getThemeData();

        themeCard();

        return view;
    }

    private void themeCard() {

        Switch isDarkSwitch = (Switch) view.findViewById(R.id.settings_is_dark_switch);

        if (themeData.isDark())
            isDarkSwitch.setChecked(true);

        isDarkSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    themeData.setIsDark(true);
                    themeDAO.setThemeData(themeData);

                } else {

                    themeData.setIsDark(false);
                    themeDAO.setThemeData(themeData);
                }

                themeIt();
            }
        });
    }

    private void themeIt() {
        int accentColor = Color.parseColor(themeData.getAccentColor());

        if (themeData.isDark()) {

            Aesthetic.get()
                    .activityTheme(R.style.AppThemeDark)
                    .isDark(true)
                    .colorPrimaryRes(R.color.colorPrimaryInverse)
                    .colorAccent(accentColor)
                    .colorWindowBackgroundRes(R.color.colorWindowBackgroundInverse)
                    .textColorPrimaryRes(R.color.textColorPrimaryInverse)
                    .textColorSecondaryRes(R.color.textColorSecondaryInverse)
                    .colorIconTitleActiveRes(R.color.textColorPrimaryInverse)
                    .colorIconTitleInactiveRes(R.color.textColorPrimaryInverse)
                    .colorCardViewBackgroundRes(R.color.colorCardBackgoundInverse)
                    .navigationViewMode(NavigationViewMode.SELECTED_ACCENT)
                    .apply();

            Aesthetic.get()
                    .colorStatusBarRes(R.color.colorPrimaryInverse)
                    .apply();

        } else {

            Aesthetic.get()
                    .activityTheme(R.style.AppTheme)
                    .isDark(false)
                    .colorPrimaryRes(R.color.colorPrimary)
                    .colorAccent(accentColor)
                    .colorWindowBackgroundRes(R.color.colorWindowBackground)
                    .textColorPrimaryRes(R.color.textColorPrimary)
                    .textColorSecondaryRes(R.color.textColorSecondary)
                    .colorIconTitleActiveRes(R.color.textColorPrimary)
                    .colorIconTitleInactiveRes(R.color.textColorPrimary)
                    .colorCardViewBackgroundRes(R.color.colorCardBackgound)
                    .navigationViewMode(NavigationViewMode.SELECTED_ACCENT)
                    .apply();

            if (Build.VERSION.SDK_INT >= M) {
                Aesthetic.get()
                        .colorStatusBar(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
                        .apply();
            }
        }

        getActivity().recreate();

    }
}
