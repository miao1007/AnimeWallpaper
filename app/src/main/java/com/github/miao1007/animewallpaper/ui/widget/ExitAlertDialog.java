package com.github.miao1007.animewallpaper.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.miao1007.animewallpaper.R;

/**
 * Created by leon on 2/19/16.
 */
public class ExitAlertDialog extends Dialog {

  private final View.OnClickListener okListener;

  @OnClick(R.id.internal_alert_ok) void internal_alert_ok(View v) {
    dismiss();
    okListener.onClick(v);
  }

  @OnClick(R.id.internal_alert_cancel) void internal_alert_cancel(View v) {
    dismiss();
  }

  public ExitAlertDialog(Context context, View.OnClickListener okListener) {
    super(context,android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
    this.okListener = okListener;
    getWindow().setWindowAnimations(R.style.iosalertdialog);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.internal_fullscreen_dialog);
    ButterKnife.bind(this);
  }
}
