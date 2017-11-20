package com.enoughspam.step.ui.settings.preference;

import android.content.Context;
import android.preference.Preference;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.afollestad.materialdialogs.color.CircleView;
import com.enoughspam.step.R;

/**
 * Created by Hugo Castelani
 * Date: 15/07/17
 * Time: 19:20
 */

public final class ColorPreference extends Preference {

    public ColorPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ColorPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ColorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorPreference(Context context) {
        super(context);
    }

    private View mView;
    private int mColor;

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        mView = view;
        invalidateColor();
    }

    public void setColor(final int color) {
        mColor = color;
        invalidateColor();
    }

    private void invalidateColor() {
        if (mView != null) {
            final CircleView circle = (CircleView) mView.findViewById(R.id.pc_circle);
            if (mColor != 0) {
                circle.setBackgroundColor(mColor);
            } else {
                circle.setBackgroundColor(ContextCompat.getColor(mView.getContext(), R.color.accent));
            }
        }
    }
}
