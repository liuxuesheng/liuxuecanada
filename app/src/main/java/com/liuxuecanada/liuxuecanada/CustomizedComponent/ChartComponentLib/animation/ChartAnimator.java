package com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.animation;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;

@SuppressLint("NewApi")
public class ChartAnimator {

    /**
     * the phase that is animated and influences the drawn values on the y-axis
     */
    protected float mPhaseY = 1f;
    /**
     * the phase that is animated and influences the drawn values on the x-axis
     */
    protected float mPhaseX = 1f;
    /**
     * object that is updated upon animation update
     */
    private AnimatorUpdateListener mListener;

    /**
     * ################ ################ ################ ################
     */
    /** CODE BELOW THIS RELATED TO ANIMATION */

    public ChartAnimator() {

    }

    public ChartAnimator(AnimatorUpdateListener listener) {
        mListener = listener;
    }

    /**
     * Animates the rendering of the chart on the x-axis with the specified
     * animation time. If animate(...) is called, no further calling of
     * invalidate() is necessary to refresh the chart.
     *
     * @param durationMillis
     */
    public void animateX(int durationMillis) {

        if (android.os.Build.VERSION.SDK_INT < 11)
            return;

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "phaseX", 0f, 1f);
        animatorX.setDuration(durationMillis);
        animatorX.addUpdateListener(mListener);
        animatorX.start();
    }

    /**
     * Animates the rendering of the chart on the y-axis with the specified
     * animation time. If animate(...) is called, no further calling of
     * invalidate() is necessary to refresh the chart.
     *
     * @param durationMillis
     */
    public void animateY(int durationMillis) {

        if (android.os.Build.VERSION.SDK_INT < 11)
            return;

        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "phaseY", 0f, 1f);
        animatorY.setDuration(durationMillis);
        animatorY.addUpdateListener(mListener);
        animatorY.start();
    }

    /**
     * This gets the y-phase that is used to animate the values.
     *
     * @return
     */
    public float getPhaseY() {
        return mPhaseY;
    }


    /**
     * This gets the x-phase that is used to animate the values.
     *
     * @return
     */
    public float getPhaseX() {
        return mPhaseX;
    }

}
