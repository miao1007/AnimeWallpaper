package com.github.miao1007.myapplication.utils.animation;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

/**
 * Created by leon on 4/18/15.
 */
public class AnimateUtils {

  public static final int ANIM_DORITION = 1000;

  public static void animateViewColor(final View view, int toColor) {

    ColorDrawable colorDrawable = (ColorDrawable) view.getBackground();
    int from = (colorDrawable == null) ? 0xffffff : colorDrawable.getColor();
    ValueAnimator anim = new ValueAnimator();
    anim.setIntValues(from, toColor);
    anim.setEvaluator(new ArgbEvaluator());
    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
        view.invalidate();
        view.setBackgroundColor((Integer) valueAnimator.getAnimatedValue());
      }
    });
    anim.setDuration(ANIM_DORITION).start();
  }


}
