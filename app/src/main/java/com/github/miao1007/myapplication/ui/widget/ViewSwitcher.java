package com.github.miao1007.myapplication.ui.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.github.miao1007.myapplication.R;

/**
 * Created by leon on 1/28/16.
 *
 * First:    After:
 *
 * |--||--|  |--||--|
 * |  ||\\|  |\\||  |
 * |--||--|  |--||--|
 * show      show
 *
 * [0] background
 * [1] TextView
 */
public class ViewSwitcher extends FrameLayout {

  static final String TAG = "ViewSwitcher";

  boolean s = true;
  float reserved;
  ObjectAnimator animator_hide;
  ObjectAnimator animator_show;
  AnimatorSet set;
  ToggleCallbackListener listener;

  public ViewSwitcher(Context context) {
    this(context, null);
  }

  public ViewSwitcher(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ViewSwitcher(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    TypedArray ta =
        attrs == null ? null : getContext().obtainStyledAttributes(attrs, R.styleable.ViewSwitcher);
    if (ta != null) {
      reserved = ta.getDimension(R.styleable.ViewSwitcher_vsReserve, reserved);
      ta.recycle();
    }
  }

  public void setListener(ToggleCallbackListener listener) {
    this.listener = listener;
  }

  public void setReserved(int px) {
    this.reserved = px;
  }

  public void toggle() {
    if (getChildCount() != 2) {
      throw new IllegalStateException("Switch only have two views");
    }
    //看哥写的自带混淆代码
    float[] anim = { 0, -this.getWidth() + reserved, this.getWidth(), reserved };
    //0 ios灰色背景 需要左滑动
    animator_hide = ObjectAnimator.ofFloat(getChildAt(1), View.TRANSLATION_X, s ? anim[0] : anim[1],
        s ? anim[1] : anim[0]);
    animator_show = ObjectAnimator.ofFloat(getChildAt(0), View.TRANSLATION_X, s ? anim[2] : anim[3],
        s ? anim[3] : anim[2]);
    set = new AnimatorSet();
    set.playTogether(animator_hide, animator_show);
    set.start();
    s = !s;
  }

  public void toggle(ToggleCallbackListener listener) {
    toggle();
    listener.toggle();
  }

  interface ToggleCallbackListener {
    void toggle();
  }
}
