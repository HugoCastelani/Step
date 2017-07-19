package com.enoughspam.step.settings.preferences;

import android.content.Context;
import android.graphics.Color;
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

    private View mView;
    private String color;

    public ColorPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public ColorPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public ColorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ColorPreference(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        setLayoutResource(R.layout.preference_custom);
        setWidgetLayoutResource(R.layout.preference_color);
        setPersistent(false);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        mView = view;
        invalidateColor();
    }

    public void setColor(String color) {
        this.color = color;
        invalidateColor();
    }

    private void invalidateColor() {
        if (mView != null) {
            CircleView circle = (CircleView) mView.findViewById(R.id.circle);
            if (!this.color.isEmpty()) {
                circle.setBackgroundColor(Color.parseColor(this.color));
            } else {
                circle.setBackgroundColor(ContextCompat.getColor(mView.getContext(), R.color.accent));
            }
        }
    }
}
