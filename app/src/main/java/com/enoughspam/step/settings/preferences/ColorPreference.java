package com.enoughspam.step.settings.preferences;

import android.content.Context;
import android.preference.Preference;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.afollestad.materialdialogs.color.CircleView;
import com.enoughspam.step.R;

/**
 * Created by hugo
 * Date: 15/07/17
 * Time: 19:20
 */


public class ColorPreference extends Preference {

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

    public void setColor(int color) {
        mColor = color;
        invalidateColor();
    }

    private void invalidateColor() {
        if (mView != null) {
            CircleView circle = (CircleView) mView.findViewById(R.id.circle);
            if (this.mColor != 0) {
                circle.setBackgroundColor(this.mColor);
            } else {
                circle.setBackgroundColor(ContextCompat.getColor(mView.getContext(), R.color.accent));
            }
        }
    }
}
