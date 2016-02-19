package com.github.miao1007.animewallpaper.ui.widget.blur;

import android.graphics.Color;
import android.view.ViewGroup;
import android.view.Window;
import com.github.miao1007.animewallpaper.R;
import com.github.miao1007.animewallpaper.ui.widget.blur.BlurDialog;
import com.github.miao1007.animewallpaper.ui.widget.blur.BlurDrawable;

/**
 * Created by leon on 2/10/16.
 */
public abstract class FullScreenBlurAlertDialog extends BlurDialog {

  public FullScreenBlurAlertDialog(Window window) {
    super(window);
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
}
