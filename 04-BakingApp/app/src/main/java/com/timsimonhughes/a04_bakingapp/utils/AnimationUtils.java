package com.timsimonhughes.a04_bakingapp.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;

public class AnimationUtils {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void createCircularReveal(final View view) {

        int centreX = view.getRight() / 2;
        int centreY = view.getBottom() / 2;
        int finalRadius = Math.max(view.getWidth(), view.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(view, centreX, centreY, 0 , finalRadius);
        anim.setDuration(500);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        anim.start();
    }

    public static void hideView(View view) {
        int centreX = view.getRight() / 2;
        int centreY = view.getBottom() / 2;
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
    }

    public static void showView(View view) {
        int centreX = view.getRight() / 2;
        int centreY = view.getBottom() / 2;
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
    }

}
