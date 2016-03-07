package com.github.miao1007.animewallpaper.utils.animation;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by leon on 4/18/15.
 */
public class AnimateUtils {

  private static final int ANIM_DORITION = 400;

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

  public static void animateViewBitmap(@NonNull final ImageView root, Bitmap bitmap) {
    if (bitmap == null && root.getDrawable()!=null){
      ObjectAnimator.ofFloat(root, View.ALPHA, 1f, 0f).setDuration(ANIM_DORITION).start();
      return;
    }

    Drawable[] layers = new Drawable[2];
    layers[0] = root.getDrawable();
    layers[1] = new BitmapDrawable(root.getResources(), bitmap);
    if (layers[0] == null) {//if null then alpha
      root.setImageDrawable(layers[1]);
      ObjectAnimator.ofFloat(root, View.ALPHA, 0.0f, 1.0f).setDuration(ANIM_DORITION).start();
      return;
    }
    TransitionDrawable transitionDrawable = new TransitionDrawable(layers);
    root.setImageDrawable(transitionDrawable);
    transitionDrawable.startTransition(ANIM_DORITION);
  }

  private static float scale;

  public static int dpToPixel(float dp, Context context) {
    if (scale == 0) {
      scale = context.getResources().getDisplayMetrics().density;
    }
    return (int) (dp * scale);
  }

}
