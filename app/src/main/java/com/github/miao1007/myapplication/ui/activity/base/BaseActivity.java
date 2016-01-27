package com.github.miao1007.myapplication.ui.activity.base;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import com.github.miao1007.myapplication.utils.StatusbarUtils;
import com.github.miao1007.myapplication.utils.animation.AnimateUtils;

/**
 * Created by leon on 4/14/15.
 */
public class BaseActivity extends ActionBarActivity {

  public static final int ANIM_DORITION = 1000;

  @TargetApi(21) protected void setUpToolbarColor(final Toolbar mToolbar, int toColor) {
    ColorDrawable colorDrawable = (ColorDrawable) mToolbar.getBackground();

    int from = colorDrawable.getColor();

    final Window w = getWindow();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

      ValueAnimator colorAnimation = ValueAnimator.ofArgb(from, toColor);
      ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
        @Override public void onAnimationUpdate(ValueAnimator animation) {
          mToolbar.invalidate();
          w.setStatusBarColor((Integer) animation.getAnimatedValue());
          mToolbar.setBackgroundColor((Integer) animation.getAnimatedValue());
        }
      };
      colorAnimation.addUpdateListener(listener);
      colorAnimation.setDuration(ANIM_DORITION).start();
      return;
    }

    //compat for lower API 20
    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
      int statusBarHeight = StatusbarUtils.getStatusBarHeightPx(this);
      mToolbar.setPadding(0, statusBarHeight, 0, 0);
      w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
          WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    AnimateUtils.animateViewColor(mToolbar, toColor);
  }

  protected void setUpToolbarColor(Toolbar mToolbar) {
    ColorDrawable colorDrawable = (ColorDrawable) mToolbar.getBackground();
    setUpToolbarColor(mToolbar, colorDrawable == null ? 0x00 : colorDrawable.getColor());
  }


  //make statusbar translucent when api-19+
  @TargetApi(19)
  protected void requstTranslucentStatusbar(){

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){

      getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
          WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
      getWindow().setStatusBarColor(Color.TRANSPARENT);
    }
  }
}
