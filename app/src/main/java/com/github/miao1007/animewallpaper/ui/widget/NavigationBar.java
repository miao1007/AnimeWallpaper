package com.github.miao1007.animewallpaper.ui.widget;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.github.miao1007.animewallpaper.R;
import com.github.miao1007.animewallpaper.utils.LogUtils;
import com.github.miao1007.animewallpaper.utils.StatusbarUtils;

/**
 * Created by leon on 1/18/16.
 */
public class NavigationBar extends RelativeLayout {

  static final String TAG = LogUtils.makeLogTag(NavigationBar.class);
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

  public void setProgressBar(boolean isLoading) {
    if (mProgress != null) {
      mProgress.setVisibility(isLoading ? VISIBLE : GONE);
    }
  }

  public void setProgress(@IntRange(from = 0, to = 100) int progress) {
    mProgress.setProgress(progress);
  }

  public void setTitle(@NonNull CharSequence title) {
    if (mNaviTitle != null) {
      mNaviTitle.setText(title);
    }
  }

  public void setTextColor(@ColorInt int color) {
    if (mNaviTitle != null) {
      mNaviTitle.setTextColor(color);
    }
  }

  View view;
  boolean doing = false;

  /**
   * Create a new relativelayout and inflate xml in it,
   * remember use merge instead of RelativeLayout
   */
  private void initView() {
    View.inflate(getContext(), R.layout.internal_navigationbar, this);  //correct way to inflate..
    ButterKnife.bind(this);
    setFitTranslucent(true);
  }

  public void setFitTranslucent(final boolean translucent) {
    post(new Runnable() {
      @Override public void run() {
        if (StatusbarUtils.isLessKitkat() || !translucent) {
          return;
        }
        int height = StatusbarUtils.getStatusBarOffsetPx(getContext());
        setPadding(getPaddingLeft(), height + getPaddingTop(), getPaddingRight(),
            getPaddingBottom());
        getLayoutParams().height += height;
        view = LayoutInflater.from(getContext()).inflate(R.layout.settings, null);
      }
    });
  }

  public void setPopView(final boolean in) {
    if (doing) {
      return;
    }
    this.setClipChildren(false);
    //this.setClipToOutline(false);
    final int h = ((int) getResources().getDimension(R.dimen.internal_navigation_pop_height));
    final int init = getMeasuredHeight();
    Animation animation = new Animation() {

      @Override protected void applyTransformation(float interpolatedTime, Transformation t) {
        getLayoutParams().height = init + (in ? (1) : (-1)) * (int) (h * interpolatedTime);
        requestLayout();
      }

      @Override public boolean willChangeBounds() {
        return true;
      }
    };
    animation.setDuration(250);
    view.setAnimation(animation);
    if (in) {
      LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h);
      //params.addRule(ALIGN_BOTTOM, RelativeLayout.TRUE);
      //params.ma
      params.addRule(RelativeLayout.BELOW, mNaviTitle.getId());
      if (view.getParent() == null) {
        addView(view, params);
        doing = false;
      }
    } else {
      removeView(view);
      doing = false;
    }
  }
}


