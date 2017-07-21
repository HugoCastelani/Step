package com.enoughspam.step.settings.preferences;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.prefs.MaterialMultiSelectListPreference;
import com.enoughspam.step.R;
import com.enoughspam.step.util.ThemeHandler;

/**
 * Created by hugo
 * Date: 19/07/17
 * Time: 21:10
 */


public class MultiSelectListPreference extends MaterialMultiSelectListPreference {

    public MultiSelectListPreference(Context context) {
        super(context);
        init(context, null);
    }

    public MultiSelectListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MultiSelectListPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public MultiSelectListPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        setLayoutResource(R.layout.preference_custom);
    }

    @Override
    public MaterialDialog.Builder getBuilder() {
        MaterialDialog.Builder builder = super.getBuilder();

        builder.backgroundColor(ThemeHandler.getBackground())
                .positiveColor(ThemeHandler.getAccent())
                .negativeColor(ThemeHandler.getAccent());

        return builder;
    }
}
