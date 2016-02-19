package com.github.miao1007.animewallpaper.ui.widget.blur;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import com.github.miao1007.animewallpaper.utils.LogUtils;

/**
 * Created by leon on 2/10/16.
 *
 * @see #onCreate(Bundle)
 *
 * @see #onStart()
 *
 * mWindowManager.addView(mDecor, l);
 *
 * @see #dismiss()
 *
 * mWindowManager.removeViewImmediate(mDecor);
 *
 * @see #onStop()
 *
 */
public abstract class BlurDialog extends Dialog {

  static final String TAG = LogUtils.makeLogTag(BlurDialog.class.getSimpleName());
  protected Window blurredWindow;
  private BlurDrawable drawable;
  private View dialogView;

  public Window getBlurredWindow() {
    return blurredWindow;
  }

  public BlurDialog(Window blurredWindow, int themeResId) {
    super(blurredWindow.getDecorView().getContext(), themeResId);
    this.blurredWindow = blurredWindow;
    this.drawable = new BlurDrawable(blurredWindow.getDecorView());
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
    dialogView = inflateDialogView();
    onSetupBlur(drawable);
    drawable.setDrawOffset(0, getWindowOffset());
    getWindow().setContentView(dialogView);
    dialogView.setBackgroundDrawable(drawable);
    onSetWindowAttrs(getWindow());
  }

  /**
   * Set height/width/dim/gravity etc.
   */
  protected abstract void onSetWindowAttrs(Window window);

  protected abstract void onSetupBlur(BlurDrawable drawable);

  public void setEnableBlur(boolean enableBlur) {
    drawable.setEnabled(enableBlur);
  }

  /**
   * FrameLayout: android.R.id.content
   * set/getContentView: your view defined in xml
   */
  private View getContentView() {
    return findViewById(android.R.id.content);
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
   * Load views from xml
   */
  protected abstract View inflateDialogView();

  /**
   * the offset between blurredView and dialog
   */
  protected int getWindowOffset() {
    return 0;
  }
}
