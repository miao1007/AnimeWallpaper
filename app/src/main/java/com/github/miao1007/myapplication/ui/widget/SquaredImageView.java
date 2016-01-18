package com.github.miao1007.myapplication.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by leon on 1/17/16.
 */
public class SquaredImageView extends ImageView {

  public SquaredImageView(Context context) {
    super(context);
  }

  public SquaredImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SquaredImageView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    setImageDrawable(null);
  }
}
