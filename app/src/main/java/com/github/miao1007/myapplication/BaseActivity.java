package com.github.miao1007.myapplication;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import com.github.miao1007.myapplication.utils.ScreenUtils;
import com.github.miao1007.myapplication.utils.animation.AnimateUtils;

/**
 * Created by leon on 4/14/15.
 */
public class BaseActivity extends ActionBarActivity {

  public static final int ANIM_DORITION = 1000;

  @TargetApi(21) protected void setToolbarColor(final Toolbar mToolbar, int toColor) {
    ColorDrawable colorDrawable = (ColorDrawable) mToolbar.getBackground();

    int from = colorDrawable.getColor();

    final Window w = getWindow();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

      ValueAnimator colorAnimation = ValueAnimator.ofArgb(from, toColor);
      colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

        @Override public void onAnimationUpdate(ValueAnimator animator) {
          mToolbar.invalidate();
          w.setStatusBarColor((Integer) animator.getAnimatedValue());
          mToolbar.setBackgroundColor((Integer) animator.getAnimatedValue());
        }
      });
      colorAnimation.setDuration(ANIM_DORITION).start();
      return;
    }

    //compat for lower API 20
    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
      int statusBarHeight = ScreenUtils.getStatusBarHeight(this);
      mToolbar.setPadding(0, statusBarHeight, 0, 0);
      w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
          WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    AnimateUtils.animateViewColor(mToolbar, toColor);
  }

  protected void setToolbarColor(Toolbar mToolbar) {
    ColorDrawable colorDrawable = (ColorDrawable) mToolbar.getBackground();
    setToolbarColor(mToolbar, colorDrawable.getColor());
  }

}
