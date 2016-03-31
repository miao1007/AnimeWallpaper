package com.github.miao1007.animewallpaper.ui.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.github.miao1007.animewallpaper.R;

/**
 * Created by leon on 1/18/16.
 * iOS like UINavigationBar
 */
public class NavigationBar extends RelativeLayout {

  @Bind(R.id.internal_navi_title) TextView mNaviTitle;
  @Bind(R.id.internal_navi_progress) ProgressBar mProgress;

  public NavigationBar(Context context) {
    this(context, null, 0);
  }

  public NavigationBar(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initView();
  }

  public boolean getProgress() {
    return mProgress.getVisibility() == VISIBLE;
  }

  public void setProgressBar(boolean isLoading) {
    if (mProgress != null) {
      mProgress.setVisibility(isLoading ? VISIBLE : GONE);
    }
  }

  public void setTitle(@NonNull CharSequence title) {
    if (mNaviTitle != null) {
      mNaviTitle.setText(title);
    }
  }

  public void setTitle(@StringRes int title) {
    if (mNaviTitle != null) {
      mNaviTitle.setText(title);
    }
  }

  public void setTextColor(@ColorInt int color) {
    if (mNaviTitle != null) {
      mNaviTitle.setTextColor(color);
    }
  }

  /**
   * Create a new relativelayout and inflate xml in it,
   * remember use **merge** instead of RelativeLayout
   */
  private void initView() {
    View.inflate(getContext(), R.layout.internal_navigationbar, this);  //correct way to inflate..
    ButterKnife.bind(this);
  }

  //
  //private void setFitTranslucent(final boolean translucent) {
  //  post(new Runnable() {
  //    @Override public void run() {
  //      if (StatusBarUtils.isLessKitkat() || !translucent) {
  //        return;
  //      }
  //      int height = StatusBarUtils.getStatusBarOffsetPx(getContext());
  //      setPadding(getPaddingLeft(), height + getPaddingTop(), getPaddingRight(),
  //          getPaddingBottom());
  //      getLayoutParams().height += height;
  //    }
  //  });
  //}
}


