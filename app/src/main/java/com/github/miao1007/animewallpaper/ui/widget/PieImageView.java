package com.github.miao1007.animewallpaper.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.IntRange;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.github.miao1007.animewallpaper.utils.animation.AnimateUtils;

/**
 * Created by leon on 3/7/16.
 * Image with Pie Progress
 * learned from com.kaopiz.kprogresshud's PieView
 */
public class PieImageView extends ImageView {

  private Paint mWhitePaint;
  private Paint mGreyPaint;
  private RectF mBound;
  private int mMax = 100;
  private int mProgress = 75;

  public PieImageView(Context context) {
    this(context, null, 0);
  }

  public PieImageView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public PieImageView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  public void setProgress(@IntRange(from = 0, to = 100) int mProgress) {
    this.mProgress = mProgress;
    ViewCompat.postInvalidateOnAnimation(this);
  }

  private void init() {
    mWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mWhitePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    mWhitePaint.setStrokeWidth(AnimateUtils.dpToPixel(0.1f, getContext()));
    mWhitePaint.setColor(Color.argb(120, 0xff, 0xff, 0xff));

    mGreyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mGreyPaint.setStyle(Paint.Style.STROKE);
    mGreyPaint.setStrokeWidth(AnimateUtils.dpToPixel(2, getContext()));
    mGreyPaint.setColor(Color.argb(120, 0xff, 0xff, 0xff));

    mBound = new RectF();
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    int min = Math.min(w, h);
    int max = w + h - min;
    int r = min / 4;
    mBound.set(max / 2 - r, min / 2 - r, max / 2 + r, min / 2 + r);
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    float mAngle = mProgress * 360f / mMax;
    canvas.drawArc(mBound, 270, mAngle, true, mWhitePaint);
    canvas.drawCircle(mBound.centerX(), mBound.centerY(), mBound.height() / 2, mGreyPaint);

  }
}
