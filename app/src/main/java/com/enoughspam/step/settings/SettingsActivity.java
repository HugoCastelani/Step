package com.enoughspam.step.settings;


import android.os.Build;
import android.os.Bundle;

import com.afollestad.aesthetic.AestheticActivity;
import com.afollestad.aesthetic.AestheticToolbar;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.Utils;
import com.enoughspam.step.R;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class SettingsActivity extends AestheticActivity {

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

        getFragmentManager().beginTransaction().replace(R.id.settings_fragment_container, new SettingsFragment()).commit();
    }
}
