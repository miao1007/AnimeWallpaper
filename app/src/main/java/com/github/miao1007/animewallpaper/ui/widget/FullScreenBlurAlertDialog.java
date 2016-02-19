package com.github.miao1007.animewallpaper.ui.widget;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.github.miao1007.animewallpaper.R;

/**
 * Created by leon on 2/10/16.
 */
public class FullScreenBlurAlertDialog extends BlurDialog {

  View.OnClickListener okListener;

  public FullScreenBlurAlertDialog(Window window, View.OnClickListener okListener) {
    super(window);
    this.okListener = okListener;
  }

  @Override protected void onSetupBlur(BlurDrawable drawable) {
    drawable.setBlurRadius(12);
    drawable.setDownsampleFactor(8);
    drawable.setOverlayColor(Color.argb(180, 0x00, 0x00, 0x00));
  }

  @Override protected void onSetWindowAttrs(Window window) {
    getWindow().setWindowAnimations(R.style.AlphaDialogAnimation);
    //机智如我，不用再写一遍
    window.getAttributes().flags = getBlurredWindow().getAttributes().flags;
    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    window.setBackgroundDrawableResource(android.R.color.transparent);
  }

  @Override protected View inflateDialogView() {
    View view = getLayoutInflater().inflate(R.layout.internal_dialog, null, false);
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
