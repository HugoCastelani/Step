package com.enoughspam.step.settings;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.enoughspam.step.R;
import com.enoughspam.step.abstracts.AbstractActivity;

public class SettingsActivity extends AbstractActivity
        implements ColorChooserDialog.ColorCallback {

    @Override
    public void onColorSelection(ColorChooserDialog dialog, @ColorInt int selectedColor) {
        Aesthetic.get().colorAccent(selectedColor).apply();
        recreate();
    }

    @Override
    public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        initToolbar(true);
        initFragment();
    }

    @Override
    protected void initViews() {}

    @Override
    protected void initActions() {}

    @Override
    protected void initFragment() {
        SettingsFragment fragment = (SettingsFragment) getFragmentManager().findFragmentByTag("settingsFragmentTag");
        if (fragment == null) {
            fragment = new SettingsFragment();
            final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.settings_fragment_container, fragment, "settingsFragmentTag");
            fragmentTransaction.commit();
        }
    }
}
