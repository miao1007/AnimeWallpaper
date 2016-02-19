package com.github.miao1007.animewallpaper.ui.widget;

import android.view.View;
import android.view.Window;
import com.github.miao1007.animewallpaper.R;
import com.github.miao1007.animewallpaper.ui.widget.blur.FullScreenBlurAlertDialog;

/**
 * Created by leon on 2/19/16.
 */
public class ExitAlertDialog extends FullScreenBlurAlertDialog {


  View.OnClickListener okListener;

  public ExitAlertDialog(Window window, View.OnClickListener okListener) {
    super(window);
    this.okListener = okListener;
  }

  @Override protected View inflateDialogView() {
    View view = getLayoutInflater().inflate(R.layout.internal_fullscreen_dialog, null, false);
    view.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dismiss();
      }
    });
    view.findViewById(R.id.internal_alert_ok).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dismiss();
        okListener.onClick(v);
      }
    });
    view.findViewById(R.id.internal_alert_cancel).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dismiss();
      }
    });
    return view;
  }
}
