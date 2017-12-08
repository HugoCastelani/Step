package com.hugocastelani.blockbook.ui.intro.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.blankj.utilcode.util.ConvertUtils;
import com.hugocastelani.blockbook.R;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

/**
 * Created by Hugo Castelani
 * Date: 04/12/17
 * Time: 21:45
 */

public final class CustomTwitterLoginButton extends TwitterLoginButton {
    public CustomTwitterLoginButton(Context context) {
        super(context);
        init();
    }

    public CustomTwitterLoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTwitterLoginButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (isInEditMode()) return;
        setTextSize(20);
        setPadding(12, 0, 12, 0);
        setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        setTextSize(14);
        setMinHeight(ConvertUtils.dp2px(30));
        setMinimumHeight(ConvertUtils.dp2px(30));
        setText(getResources().getText(R.string.continue_with_twitter));
    }
}
