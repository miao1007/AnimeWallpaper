package com.github.miao1007.myapplication.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.github.miao1007.myapplication.R;
import com.github.miao1007.myapplication.utils.LogUtils;
import com.github.miao1007.myapplication.utils.StatusbarUtils;
import com.github.miao1007.myapplication.utils.picasso.Blur;

/**
 * Created by leon on 1/18/16.
 */
public class NavigationBar extends RelativeLayout {

  @Bind(R.id.internal_navi_title) TextView mNaviTitle;
  @Bind(R.id.internal_navi_progress) ProgressBar mProgress;

  View viewToBlur;
  Bitmap blurred;
  Bitmap scaleBitmap;
  Paint paint;

  static final String TAG = LogUtils.makeLogTag(NavigationBar.class);
  @Bind(R.id.internal_navi_bg) ImageView mInternalNaviBg;
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
      Log.d(TAG, Position.from(mProgress).toString());
    }
  }

  public void setViewToBlur(View viewToBlur) {
    this.viewToBlur = viewToBlur;
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
    View.inflate(context, R.layout.internal_navigationbar, this);  //correct way to inflate..
    ButterKnife.bind(this);
    paint = new Paint();
    paint.setAntiAlias(true);
    paint.setFilterBitmap(true);
  }

  @Override public void draw(Canvas canvas) {
    super.draw(canvas);
    onUpdate();
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    viewToBlur = null;
    if (blurred != null) {
      blurred.recycle();
      blurred = null;
    }
  }


  public static Bitmap loadBitmapFromView(View v) {
    Bitmap b = Bitmap.createBitmap( 360, 640, Bitmap.Config.ARGB_8888);
    Canvas c = new Canvas(b);
    v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
    v.draw(c);
    return b;
  }

  public void onUpdate() {
    Log.d(TAG, "onUpdate:" + (viewToBlur == null));
    if (viewToBlur != null) {
      scaleBitmap = loadBitmapFromView(viewToBlur);
      if (scaleBitmap == null) {
        Log.d(TAG, "onUpdate:" + "null bitmap");

        return;
      }
      blurred = Blur.fastblur(getContext(), scaleBitmap, 10);
      mInternalNaviBg.setImageBitmap(blurred);
    }
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
    if (getChildAt(0) != null
        && ((MarginLayoutParams) getChildAt(0).getLayoutParams()).topMargin < height) {
      getLayoutParams().height += height;
    } else {
      //have set padding
      return;
    }
    for (int i = 0; i < getChildCount(); i++) {
      //Log.d(TAG, "before=" + getChildAt(i).toString());
      if (getChildAt(i) != null) {
        ((MarginLayoutParams) getChildAt(i).getLayoutParams()).setMargins(0, height, 0, 0);
      }
      //Log.d(TAG, "after=" + getChildAt(i).toString());
    }
  }

  //@Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
  //  super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  //  if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
  //    return;
  //  }
  //  int height = StatusbarUtils.getStatusBarHeight(this.getContext());
  //  //en.. it's a hard code
  //  //getChildAt(0) is define as title, so always paddingTop = 0
  //  if (getChildAt(0) != null && getChildAt(0).getPaddingTop() < height) {
  //    getLayoutParams().height += height;
  //  } else {
  //    //have set padding
  //    return;
  //  }
  //  for (int i = 0; i < getChildCount(); i++) {
  //    Log.d(TAG, "before=" + getChildAt(i).toString());
  //    if (getChildAt(i) != null) {
  //      getChildAt(i).setPadding(getChildAt(i).getPaddingLeft(),
  //          height + getChildAt(i).getPaddingTop(), getChildAt(i).getPaddingRight(),
  //          getChildAt(i).getPaddingBottom());
  //    }
  //    Log.d(TAG, "after=" + getChildAt(i).toString());
  //  }
  //}
}


