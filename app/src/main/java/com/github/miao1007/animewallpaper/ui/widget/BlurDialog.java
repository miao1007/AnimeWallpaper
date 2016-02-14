package com.github.miao1007.animewallpaper.ui.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import com.github.miao1007.animewallpaper.utils.LogUtils;

/**
 * Created by leon on 2/10/16.
 */
public abstract class BlurDialog extends Dialog {

  static final String TAG = LogUtils.makeLogTag(BlurDialog.class.getSimpleName());
  View blurredView;
  boolean isAnimating;
  private BlurDrawable drawable;
  private long duration = 300;

  public BlurDialog(Activity activity, int themeResId) {
    this(activity.getWindow(), themeResId);
  }

  public BlurDialog(Window blurredWindow, int themeResId) {
    this(blurredWindow.getDecorView(), themeResId);
  }

  public BlurDialog(View view, int themeResId) {
    super(view.getContext(), themeResId);
    this.blurredView = view;
    this.drawable = new BlurDrawable(blurredView);
  }

  public BlurDialog(View view) {
    this(view, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
  }

  public void setOverlayColor(int color) {
    drawable.setOverlayColor(color);
  }

  public void setBlurRadius(int radius) {
    drawable.setBlurRadius(radius);
  }

  public void setDownsampleFactor(int sample) {
    drawable.setDownsampleFactor(sample);
  }

  public void setduration(long duration) {
    this.duration = duration;
  }

  protected BlurDrawable getBlurDrawable() {
    return drawable;
  }

  /**
   * Called when the dialog is starting.
   * set dialog window attrs.
   * Note this window is a new window, not belongs to Activity
   */
  @Override public void onStart() {
    super.onStart();
    onSetWindowAttrs(getWindow());
    //getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
    //  @Override public boolean onTouch(View view, MotionEvent event) {
    //
    //    float dX = 0f, dY = 0f;
    //
    //    switch (event.getActionMasked()) {
    //
    //      case MotionEvent.ACTION_DOWN:
    //        Log.d(TAG, "onTouch: DOWN: " + event.getRawY());
    //
    //        //dX = view.getX() - event.getRawX();
    //        //dY = view.getY() - event.getRawY();
    //        break;
    //
    //      case MotionEvent.ACTION_MOVE:
    //        Log.d(TAG, "onTouch: MOVE: " + event.getRawY());
    //        //view.setY(dY + event.getRawY());
    //        //view.animate().x(event.getRawX() + dX).y(event.getRawY() + dY).setDuration(0).start();
    //        break;
    //      default:
    //        return false;
    //    }
    //    return true;
    //  }
    //});
  }

  /**
   * Set height/width/dim/gravity etc.
   */
  protected abstract void onSetWindowAttrs(Window window);

  public void setEnableBlur(boolean enableBlur) {
    drawable.setEnabled(enableBlur);
  }

  /**
   * Load content view
   */
  @Override public void show() {
    if (isAnimating) {
      return;
    }
    setContentView(inflateDialogView());
    getContentView().setBackgroundDrawable(drawable);
    Animator animator = loadAnimation(getContentView(), true);
    animator.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animation) {
        BlurDialog.super.show();
        isAnimating = true;
      }

      @Override public void onAnimationEnd(Animator animation) {
        isAnimating = false;
      }

      @Override public void onAnimationCancel(Animator animation) {
      }

      @Override public void onAnimationRepeat(Animator animation) {

      }
    });
    animator.start();

    BlurDialog.super.show();

  }

  /**
   * FrameLayout: android.R.id.content
   * set/getContentView: your view defined in xml
   */
  private View getContentView() {
    return findViewById(android.R.id.content);
  }

  /**
   * Called when the dialog is dismiss. clean work should be done when #onStop
   */
  //@Override public void dismiss() {
  //  if (isAnimating) {
  //    return;
  //  }
  //  Animator dismiss = loadAnimation(getContentView(), false);
  //  dismiss.addListener(new Animator.AnimatorListener() {
  //    @Override public void onAnimationStart(Animator animation) {
  //      isAnimating = true;
  //    }
  //
  //    @Override public void onAnimationEnd(Animator animation) {
  //      isAnimating = false;
  //      BlurDialog.super.dismiss();
  //    }
  //
  //    @Override public void onAnimationCancel(Animator animation) {
  //
  //    }
  //
  //    @Override public void onAnimationRepeat(Animator animation) {
  //
  //    }
  //  });
  //  dismiss.start();
  //
  //  BlurDialog.super.dismiss();
  //
  //}

  /**
   * do clean work when stop
   *
   * @see #dismiss
   */
  @Override protected void onStop() {
    super.onStop();
    if (drawable != null) {
      drawable.onDestroy();
    }
  }


  /**
   * Load views from xml
   */
  protected abstract View inflateDialogView();

  /**
   * the offset between blurredView and dialog
   */
  protected int getWindowOffset() {
    return blurredView.getHeight() - getWindow().getDecorView().getHeight();
  }

  /**
   * Translation animation for root view
   * do not use a window animation in style, because it can't be listened
   */
  protected ObjectAnimator loadAnimation(final View view, boolean in) {
    ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.ALPHA, in ? 0f : 1f, in ? 1f : 0f);
    //animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
    //  @Override public void onAnimationUpdate(ValueAnimator animation) {
    //    drawable.setDrawOffset(0, getWindowOffset() + view.getTranslationY());
    //    view.invalidate();
    //  }
    //});
    animator.setDuration(duration);
    return animator;
  }
}
