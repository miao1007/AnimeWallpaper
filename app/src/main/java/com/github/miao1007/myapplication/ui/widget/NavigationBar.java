package com.github.miao1007.myapplication.ui.widget;

import android.content.Context;
import android.os.Build;
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
import com.github.miao1007.myapplication.utils.LogUtils;
import com.github.miao1007.myapplication.utils.StatusbarUtils;

/**
 * Created by leon on 1/18/16.
 */
public class NavigationBar extends RelativeLayout {

  @InjectView(R.id.internal_navi_title) TextView mNaviTitle;
  @InjectView(R.id.internal_navi_progress) ProgressBar mProgress;

  static final String TAG = LogUtils.makeLogTag(NavigationBar.class);
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
   * remember use merge instead of RelativeLayout
   */
  private void initView(Context context) {
    View.inflate(context, R.layout.internal_navi_root, this);  //correct way to inflate..
    ButterKnife.inject(this);
  }

  /**
   * Set padding on Kitkat-above device
   */
  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
      return;
    }
    int height = StatusbarUtils.getStatusBarHeight(this.getContext());
    //en.. it's a hard code
    //getChildAt(0) is define as title, so always paddingTop = 0
    if (getChildAt(0) != null && getChildAt(0).getPaddingTop() < height) {
      getLayoutParams().height += height;
    } else {
      //have set padding
      return;
    }
    for (int i = 0; i < getChildCount(); i++) {
      if (getChildAt(i) != null) {
        getChildAt(i).setPadding(getChildAt(i).getPaddingLeft(),
            height + getChildAt(i).getPaddingTop(), getChildAt(i).getPaddingRight(),
            getChildAt(i).getPaddingBottom());
      }
    }
  }
}
