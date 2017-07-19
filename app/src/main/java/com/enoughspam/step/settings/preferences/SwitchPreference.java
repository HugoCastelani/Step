package com.enoughspam.step.settings.preferences;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.afollestad.aesthetic.AestheticSwitch;
import com.enoughspam.step.R;

/**
 * Created by hugo
 * Date: 16/07/17
 * Time: 21:25
 */

public class SwitchPreference extends android.preference.SwitchPreference {

    public SwitchPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public SwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public SwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SwitchPreference(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        setLayoutResource(R.layout.preference_custom);
        setWidgetLayoutResource(R.layout.preference_switch);
        setPersistent(true);
    }

    private AestheticSwitch mSwitch;

    @Override
    public void onBindView(View view) {
        super.onBindView(view);

        mSwitch = (AestheticSwitch) view.findViewById(android.R.id.switch_widget);
        mSwitch.setChecked(isChecked());
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);

        if (mSwitch != null) {
            mSwitch.setChecked(checked);
        }
    }
}
