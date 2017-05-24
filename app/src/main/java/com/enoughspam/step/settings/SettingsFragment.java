package com.enoughspam.step.settings;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.enoughspam.step.generalClasses.ScreenInfo;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
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

        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (themeData.isDark()) {

            int darkAccentColor = Color.parseColor(themeData.getAccentColor());

            Aesthetic.get()
                    .isDark(true)
                    .colorPrimaryRes(R.color.md_grey_900)
                    .colorAccent(darkAccentColor)
                    .colorWindowBackgroundRes(R.color.md_grey_900)
                    .textColorPrimaryRes(R.color.md_white_1000)
                    .textColorSecondaryRes(R.color.md_grey_400)
                    .colorIconTitleActiveRes(R.color.md_white_1000)
                    .colorIconTitleInactiveRes(R.color.md_white_1000)
                    .colorCardViewBackgroundRes(R.color.md_grey_850)
                    .navigationViewMode(NavigationViewMode.SELECTED_ACCENT)
                    .apply();

            Aesthetic.get()
                    .colorStatusBarRes(R.color.md_grey_900)
                    .apply();

            if (Build.VERSION.SDK_INT >= LOLLIPOP) {
                if (actionBar != null)
                    actionBar.setElevation(0);
            }

        } else {

            int lightAccentColor = Color.parseColor(themeData.getAccentColor());

            Aesthetic.get()
                    .isDark(false)
                    .colorPrimaryRes(R.color.md_grey_50)
                    .colorAccent(lightAccentColor)
                    .colorWindowBackgroundRes(R.color.md_grey_100)
                    .textColorPrimaryRes(R.color.md_black_1000)
                    .textColorSecondaryRes(R.color.md_grey_700)
                    .colorIconTitleActiveRes(R.color.md_black_1000)
                    .colorIconTitleInactiveRes(R.color.md_black_1000)
                    .colorCardViewBackgroundRes(R.color.md_white_1000)
                    .navigationViewMode(NavigationViewMode.SELECTED_ACCENT)
                    .apply();

            if (Build.VERSION.SDK_INT >= LOLLIPOP) {
                if (actionBar != null)
                    actionBar.setElevation(new ScreenInfo(getActivity()).getPixelDensity() * 4);

                if (Build.VERSION.SDK_INT >= M) {
                    Aesthetic.get()
                            .colorStatusBar(ContextCompat.getColor(getActivity(), R.color.md_grey_50))
                            .apply();
                }
            }

        }

        getActivity().recreate();

    }
}
