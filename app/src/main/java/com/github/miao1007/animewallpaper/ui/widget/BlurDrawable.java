package com.github.miao1007.animewallpaper.ui.widget;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.github.miao1007.animewallpaper.utils.picasso.Blur;
import java.lang.ref.WeakReference;
import java.util.InputMismatchException;

/**
 * A drawable that draws the target view as blurred using fast blur
 * <p/>
 * <p/>
 * TODO:we might use setBounds() to draw only part a of the target view
 * <p/>
 * Created by 10uR on 24.5.2014.
 */
public class BlurDrawable extends Drawable {

  private WeakReference<View> targetRef;
  private Bitmap blurred;
  private Paint paint;
  private int radius;

  public BlurDrawable(View target) {
    this(target, 10);
  }

  public BlurDrawable(View target, int radius) {
    this.targetRef = new WeakReference<View>(target);
    setRadius(radius);
    target.setDrawingCacheEnabled(true);
    target.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
    paint = new Paint();
    paint.setAntiAlias(true);
    paint.setFilterBitmap(true);
  }

  @Override public void draw(Canvas canvas) {
    if (blurred == null) {
      View target = targetRef.get();
      if (target != null) {
        Bitmap bitmap = target.getDrawingCache(true);
        if (bitmap == null) return;
        blurred = Blur.fastblur(target.getContext(), bitmap, radius);
      }
    }
    if (blurred != null && !blurred.isRecycled()) {
      canvas.drawBitmap(blurred, 0, 0, paint);
    }
  }

  public int getRadius() {
    return radius;
  }

  /**
   * Set the bluring radius that will be applied to target view's scaleBitmap
   *
   * @param radius should be 0-100
   */
  public void setRadius(int radius) {
    if (radius < 0 || radius > 100) {
      throw new InputMismatchException("Radius must be 0 <= radius <= 100 !");
    }
    this.radius = radius;
    if (blurred != null) {
      blurred.recycle();
      blurred = null;
    }
    invalidateSelf();
  }

  @Override public void setAlpha(int alpha) {
  }

  @Override public void setColorFilter(ColorFilter cf) {

  }

  @Override public int getOpacity() {
    return PixelFormat.TRANSLUCENT;
  }
}