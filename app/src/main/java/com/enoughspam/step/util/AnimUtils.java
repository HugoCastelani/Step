package com.enoughspam.step.util;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import java.util.ArrayList;

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

    public static void fadeOutFadeIn(@NonNull final ArrayList<View> fadeOutViews,
                                     @NonNull final ArrayList<View> fadeInViews) {

        for (int i = 0; i < fadeOutViews.size(); i++) {
            if (fadeOutViews.get(i).getVisibility() != View.VISIBLE) {
                fadeOutViews.remove(i);
            }
        }

        for (int i = 0; i < fadeInViews.size(); i++) {
            if (fadeInViews.get(i).getVisibility() != View.VISIBLE) {
                fadeInViews.remove(i);
            }
        }

        ArrayList<AlphaAnimation> fadeOutAnimations = new ArrayList<>();
        ArrayList<AlphaAnimation> fadeInAnimations = new ArrayList<>();

        for (int i = 0; i < fadeOutViews.size(); i++) {
            final AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            animation.setDuration(200);

            final Integer thisIndex = i;
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    fadeOutViews.get(thisIndex).setVisibility(View.GONE);
                }

                @Override public void onAnimationStart(Animation animation) {}
                @Override public void onAnimationRepeat(Animation animation) {}
            });

            fadeOutAnimations.add(animation);
        }

        for (int i = 0; i < fadeInViews.size(); i++) {
            final AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            animation.setDuration(200);

            final Integer thisIndex = i;
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    fadeInViews.get(thisIndex).setVisibility(View.VISIBLE);
                }

                @Override public void onAnimationStart(Animation animation) {}
                @Override public void onAnimationRepeat(Animation animation) {}
            });

            fadeInAnimations.add(animation);
        }

        for (int i = 0; i < fadeOutViews.size(); i++) {
            fadeOutViews.get(i).startAnimation(fadeOutAnimations.get(i));
        }

        for (int i = 0; i < fadeInViews.size(); i++) {
            fadeInViews.get(i).startAnimation(fadeInAnimations.get(i));
        }
    }
}
