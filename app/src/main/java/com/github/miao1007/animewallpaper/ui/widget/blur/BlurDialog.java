package com.github.miao1007.animewallpaper.ui.widget.blur;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import com.github.miao1007.animewallpaper.R;

/**
 * Created by leon on 2/10/16.
 *
 * lifecycle:
 *
 * @see #Dialog
 *
 * @see #onCreate(Bundle) (setContentView here)
 *
 * @see #onStart() (setUp window)
 *
 * mWindowManager.addView(mDecor, l);
 *
 * @see #dismiss()
 *
 * mWindowManager.removeViewImmediate(mDecor);
 *
 * @see #onStop() (clean work)
 *
 *
 * getWindow().getDecoView() will depent on window attrs
 * findViewById(android.R.id.content) will depent on child xml
 *
 * do not use  getWindow().getAttributes().height(will return -2 defalut)
 * use getWindow().getDecorView() instead
 */
public abstract class BlurDialog extends Dialog {

  protected Window blurredWindow;
  protected BlurDrawable drawable;

  public Window getBlurredWindow() {
    return blurredWindow;
  }

  public BlurDialog(Window blurredWindow, int themeResId) {
    super(blurredWindow.getDecorView().getContext(), themeResId);
    this.blurredWindow = blurredWindow;
    drawable = new BlurDrawable(blurredWindow.getDecorView());
    onSetupBlur(drawable);
  }

  public BlurDialog(Window window) {
    this(window,android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
  }

  /**
   * Called when the dialog is starting.
   * set dialog window attrs.
   * Note this window is a new window, not belongs to Activity
   */
  @Override public void onStart() {
    super.onStart();
    onSetWindowAttrs(getWindow());
    //post is required or the w/h will be 0
    getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
      @Override
      public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
          int oldTop, int oldRight, int oldBottom) {
        onSetupBlur(drawable);
        getWindow().getDecorView().findViewById(Window.ID_ANDROID_CONTENT).setBackgroundDrawable(drawable);
      }
    });
  }

  /**
   * Set content's height/width/dim/gravity to DecoView(FullScreen).
   */
  protected void onSetWindowAttrs(Window window) {
    window.setBackgroundDrawableResource(android.R.color.transparent);
    window.setDimAmount(0.7f);
  }

  protected void onSetupBlur(BlurDrawable drawable) {
    drawable.setDrawOffset(0, getWindowOffset());
  }


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
   * the offset between blurredView and dialog
   */
  protected int getWindowOffset() {
    return 0;
  }
}
