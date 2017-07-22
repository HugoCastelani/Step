package com.enoughspam.step.settings.preferences;

import android.content.Context;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;

import com.enoughspam.step.R;

/**
 * Created by hugo
 * Date: 16/07/17
 * Time: 20:42
 */

public class CategoryPreference extends PreferenceCategory implements IPreference {

    public CategoryPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public CategoryPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CategoryPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CategoryPreference(Context context) {
        super(context);
        init();
    }

    @Override
    public void init() {
        setLayoutResource(R.layout.preference_category);
    }
}
