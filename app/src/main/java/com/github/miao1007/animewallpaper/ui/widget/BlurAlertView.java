package com.github.miao1007.animewallpaper.ui.widget;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.github.miao1007.animewallpaper.R;
import com.github.miao1007.animewallpaper.utils.StatusbarUtils;

/**
 * Created by leon on 2/10/16.
 */
public class BlurAlertView extends BlurDialog {

  Window w;
  View.OnClickListener okListener;

  public BlurAlertView(View view, View.OnClickListener okListener) {
    super(view);
    this.w = ((Activity) view.getContext()).getWindow();
    this.okListener = okListener;
  }

  @Override protected void onSetWindowAttrs(Window window) {
    getWindow().setWindowAnimations(R.style.AlphaDialogAnimation);
    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    window.setBackgroundDrawableResource(android.R.color.transparent);
  }

  @Override protected int getWindowOffset() {
    //return (blurredView.getDecorView().getHeight() - getWindow().getDecorView().getHeight()) / 2;
    return 0;
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

  @Nullable @Override protected ObjectAnimator getInAnimator(View view) {
    return null;
  }

  @Nullable @Override protected ObjectAnimator getOutAnimator(View view) {
    return null;
  }
}
