package com.enoughspam.step.settings.preferences;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.prefs.MaterialDialogPreference;
import com.enoughspam.step.R;
import com.enoughspam.step.util.ThemeHandler;

/**
 * Created by hugo
 * Date: 20/07/17
 * Time: 10:09
 */

public class DialogPreference extends MaterialDialogPreference {

    public DialogPreference(Context context) {
        super(context);
        init(context, null);
    }

    public DialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DialogPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public DialogPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        setLayoutResource(R.layout.preference_custom);
    }

    private MaterialDialog.SingleButtonCallback mListener;

    @Override
    public MaterialDialog.Builder getBuilder() {
        MaterialDialog.Builder builder = super.getBuilder();

        builder.backgroundColor(ThemeHandler.getBackground())
                .positiveColor(ThemeHandler.getAccent())
                .negativeColor(ThemeHandler.getAccent())
                .onPositive(mListener); // don't gotta check if it's not null because own class does it

        return builder;
    }

    public void setOnPositiveClickListener(MaterialDialog.SingleButtonCallback listener) {
        mListener = listener;
    }
}
