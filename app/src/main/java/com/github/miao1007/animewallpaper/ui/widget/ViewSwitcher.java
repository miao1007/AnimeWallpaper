package com.github.miao1007.animewallpaper.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.github.miao1007.animewallpaper.R;

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

  boolean s = false;
  private float reserved;
  private ObjectAnimator animator_hide;
  private ObjectAnimator animator_show;
  private AnimatorSet set;

  public ViewSwitcher(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    final TypedArray ta =
        attrs == null ? null : getContext().obtainStyledAttributes(attrs, R.styleable.ViewSwitcher);
    if (ta != null) {
      reserved = ta.getDimension(R.styleable.ViewSwitcher_vsReserve, reserved);
      ta.recycle();
    }
    post(new Runnable() {
      @Override public void run() {
        showCancel(true);
      }
    });
  }

  public View getButton() {
    return getChildAt(1);
  }

  public ViewSwitcher(Context context) {
    this(context, null);
  }

  public ViewSwitcher(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  private void showCancel(boolean in) {

    if (getChildCount() != 2) {
      throw new IllegalStateException("Switch can only have two child views");
    }
    //看哥写的自带混淆代码
    float[] anim = { 0, -getWidth() + reserved, getWidth(), reserved };
    //0 ios灰色背景 需要左滑动
    animator_hide =
        ObjectAnimator.ofFloat(getChildAt(1), View.TRANSLATION_X, in ? anim[0] : anim[1],
            in ? anim[1] : anim[0]);
    animator_show =
        ObjectAnimator.ofFloat(getChildAt(0), View.TRANSLATION_X, in ? anim[2] : anim[3],
            in ? anim[3] : anim[2]);
    set = new AnimatorSet();
    set.playTogether(animator_hide, animator_show);
    set.start();
  }

  public void showCancel(boolean in, Animator.AnimatorListener listener) {

    if (getChildCount() != 2) {
      throw new IllegalStateException("Switch can only have two child views");
    }
    //看哥写的自带混淆代码
    float[] anim = { 0, -getWidth() + reserved, getWidth(), reserved };
    //0 ios灰色背景 需要左滑动
    animator_hide =
        ObjectAnimator.ofFloat(getChildAt(1), View.TRANSLATION_X, in ? anim[0] : anim[1],
            in ? anim[1] : anim[0]);
    animator_show =
        ObjectAnimator.ofFloat(getChildAt(0), View.TRANSLATION_X, in ? anim[2] : anim[3],
            in ? anim[3] : anim[2]);
    set = new AnimatorSet();
    set.playTogether(animator_hide, animator_show);
    set.addListener(listener);
    set.start();
  }

  public void setReserved(int px) {
    this.reserved = px;
  }
}
