package com.enoughspam.step.settings;


import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.AestheticActivity;
import com.afollestad.aesthetic.AestheticToolbar;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.Utils;
import com.enoughspam.step.R;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class SettingsActivity extends AestheticActivity
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
        Utils.init(this);

        AestheticToolbar toolbar = (AestheticToolbar) findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= LOLLIPOP) toolbar.setElevation(ConvertUtils.dp2px(4));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SettingsFragment fragment = (SettingsFragment) getFragmentManager().findFragmentByTag("settingsFragmentTag");
        if (fragment == null) {
            fragment = new SettingsFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.settings_fragment_container, fragment, "settingsFragmentTag");
            fragmentTransaction.commit();
        }
    }
}
