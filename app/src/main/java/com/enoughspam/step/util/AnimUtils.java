package com.enoughspam.step.util;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * Created by Hugo Castelani
 * Date: 05/10/17
 * Time: 21:41
 */

public class AnimUtils {
    public static void fadeOutFadeIn(@NonNull final View fadeOutView, @NonNull final View fadeInView) {
        final AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        final AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);

        fadeOut.setInterpolator(new AccelerateDecelerateInterpolator());
        fadeIn.setInterpolator(new AccelerateDecelerateInterpolator());

        fadeOut.setDuration(200);
        fadeIn.setDuration(200);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                fadeOutView.setVisibility(View.GONE);
            }
        });

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                fadeInView.setVisibility(View.VISIBLE);
            }
        });

        fadeOutView.startAnimation(fadeOut);
        fadeInView.startAnimation(fadeIn);
    }
}
