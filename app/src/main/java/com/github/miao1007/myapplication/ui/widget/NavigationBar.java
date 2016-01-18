package com.github.miao1007.myapplication.ui.widget;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.github.miao1007.myapplication.R;

/**
 * Created by leon on 1/18/16.
 */
public class NavigationBar extends RelativeLayout {

  @InjectView(R.id.internal_navi_title) TextView mNaviTitle;
  @InjectView(R.id.internal_navi_progress) ProgressBar mProgress;

  private boolean isLoading = false;

  public NavigationBar(Context context) {
    this(context, null, 0);
  }

  public NavigationBar(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initView(context);
  }

  public void setProgress(boolean isLoading) {
    if (mProgress != null) {
      mProgress.setVisibility(isLoading ? VISIBLE : GONE);
    }
  }

  public void setTitle(@NonNull String title) {
    if (mNaviTitle != null) {
      mNaviTitle.setText(title);
    }
  }

  public void setTextColor(@ColorInt int color) {
    for (int i = 0; i < getChildCount(); i++) {
      if (getChildAt(i) instanceof TextView && getChildAt(i) != null) {
        ((TextView) getChildAt(i)).setTextColor(color);
      }
    }
  }

  /**
   * Create a new relativelayout and inflate xml in it,
   * remember use merge instead
   */
  private void initView(Context context) {
    View.inflate(context, R.layout.internal_navi_root, this);  //correct way to inflate..
    ButterKnife.inject(this);
  }
}
