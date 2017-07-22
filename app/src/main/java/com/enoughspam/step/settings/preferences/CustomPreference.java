package com.enoughspam.step.settings.preferences;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import com.enoughspam.step.R;

/**
 * Created by hugo
 * Date: 15/07/17
 * Time: 17:53
 */

public class CustomPreference extends Preference implements IPreference {

    public CustomPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public CustomPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CustomPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomPreference(Context context) {
        super(context);
        init();
    }

    @Override
    public void init() {
        setLayoutResource(R.layout.preference_custom);
    }
}
