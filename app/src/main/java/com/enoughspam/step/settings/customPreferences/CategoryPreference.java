package com.enoughspam.step.settings.customPreferences;

import android.content.Context;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;

import com.enoughspam.step.R;

/**
 * Created by hugo
 * Date: 16/07/17
 * Time: 20:42
 */

public class CategoryPreference extends PreferenceCategory {

    public CategoryPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public CategoryPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public CategoryPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CategoryPreference(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        setLayoutResource(R.layout.preference_category);
    }
}
