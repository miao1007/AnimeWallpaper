package com.github.miao1007.animewallpaper.ui.widget;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.miao1007.animewallpaper.R;
import com.github.miao1007.animewallpaper.ui.widget.blur.BlurDialog;
import com.github.miao1007.animewallpaper.ui.widget.blur.BlurDrawable;
import com.github.miao1007.animewallpaper.utils.LogUtils;
import com.github.miao1007.animewallpaper.utils.StatusBarUtils;

/**
 * Created by leon on 2/19/16.
 */
public class ExitAlertDialog extends BlurDialog {

  static final String TAG = LogUtils.makeLogTag(ExitAlertDialog.class);

  View.OnClickListener okListener;

  @OnClick(R.id.internal_alert_ok) void internal_alert_ok(View v) {
    dismiss();
    okListener.onClick(v);
  }

  @OnClick(R.id.internal_alert_cancel) void internal_alert_cancel(View v) {
    dismiss();
  }

  public ExitAlertDialog(Window window, View.OnClickListener okListener) {
    super(window);
    this.okListener = okListener;
  }

  @Override protected void onSetupBlur(BlurDrawable drawable) {
    super.onSetupBlur(drawable);
    drawable.setCornerRadius(
        getContext().getResources().getDimension(R.dimen.internal_searchbar_radius));
    drawable.setBlurRadius(12);
    drawable.setDownsampleFactor(8);
    drawable.setOverlayColor(Color.argb(200, 0xff, 0xff, 0xff));
    drawable.setDrawOffset(
        (blurredWindow.getDecorView().getWidth() - getWindow().getDecorView().getWidth()) / 2, (
            blurredWindow.getDecorView().getHeight() - getWindow().getDecorView().getHeight()
                + StatusBarUtils.getStatusBarOffsetPx(getContext())) / 2);
  }

  @Override protected void onSetWindowAttrs(Window window) {
    super.onSetWindowAttrs(window);
    window.setWindowAnimations(R.style.AlphaDialogAnimation);
    //window.getAttributes().

  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.internal_fullscreen_dialog);
    ButterKnife.bind(this);
  }
}
