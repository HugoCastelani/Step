package com.enoughspam.step.settings;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import com.afollestad.aesthetic.AestheticActivity;
import com.enoughspam.step.R;

public class SettingsActivity extends AestheticActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // getting toolbar ready
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
