package com.github.miao1007.animewallpaper.ui.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

/**
 * Created by leon on 2/8/16.
 * Real time Blur on Android, For API17 above
 * port Blur from @link https://github.com/500px/500px-android-blur
 */
public class BlurDrawable extends Drawable {

  private int mDownsampleFactor;
  private int mOverlayColor;
  private View mBlurredBgView;
  private int mBlurredViewWidth, mBlurredViewHeight;
  private boolean mDownsampleFactorChanged;
  private Bitmap mBitmapToBlur, mBlurredBitmap;
  private Canvas mBlurringCanvas;
  private RenderScript mRenderScript;
  private ScriptIntrinsicBlur mBlurScript;
  private Allocation mBlurInput, mBlurOutput;
  private float offsetX;
  private boolean enabled;

  public BlurDrawable(View mBlurredBgView) {
    this.mBlurredBgView = mBlurredBgView;
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
      enabled = false;
      return;
    }
    initializeRenderScript(mBlurredBgView.getContext());
    //设置blur半径, iOS中默认为12px
    setBlurRadius(15);
    //图片缩放等级，缩放越大越节约性能，理论要在100px^2以内
    setDownsampleFactor(8);
    setOverlayColor(Color.argb(175, 0xff, 0xff, 0xff));
  }

  /**
   * used for dialog/fragment/popWindow
   *
   * @param activity the activity attached
   * @see #setDrawOffset
   */
  public BlurDrawable(Activity activity) {
    this(activity.getWindow().getDecorView());
  }

  public void setBlurRadius(int radius) {
    if (!enabled) {
      return;
    }
    mBlurScript.setRadius(radius);
  }

  public void setDownsampleFactor(int factor) {
    if (factor <= 0) {
      throw new IllegalArgumentException("Downsample factor must be greater than 0.");
    }

    if (mDownsampleFactor != factor) {
      mDownsampleFactor = factor;
      mDownsampleFactorChanged = true;
    }
  }

  public void setOverlayColor(int color) {
    mOverlayColor = color;
  }

  @TargetApi(17) private void initializeRenderScript(Context context) {
    mRenderScript = RenderScript.create(context);
    mBlurScript = ScriptIntrinsicBlur.create(mRenderScript, Element.U8_4(mRenderScript));
  }

  /**
   * 相当于一个单例的初始化
   */
  protected boolean prepare() {
    //assume a 1080 x 1920 RecyclerView
    final int width = mBlurredBgView.getWidth();
    final int height = mBlurredBgView.getHeight();
    if (mBlurringCanvas == null
        || mDownsampleFactorChanged
        || mBlurredViewWidth != width
        || mBlurredViewHeight != height) {
      mDownsampleFactorChanged = false;

      mBlurredViewWidth = width;
      mBlurredViewHeight = height;

      int scaledWidth = width / mDownsampleFactor;
      int scaledHeight = height / mDownsampleFactor;

      // The following manipulation is to avoid some RenderScript artifacts at the edge.
      // 136 x 244
      scaledWidth = scaledWidth - scaledWidth % 4 + 4;
      scaledHeight = scaledHeight - scaledHeight % 4 + 4;

      if (mBlurredBitmap == null
          || mBlurredBitmap.getWidth() != scaledWidth
          || mBlurredBitmap.getHeight() != scaledHeight) {
        mBitmapToBlur = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
        if (mBitmapToBlur == null) {
          return false;
        }

        mBlurredBitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
        if (mBlurredBitmap == null) {
          return false;
        }
      }
      //创建了一个136 x 244的画板
      //当画板调用draw是，将画到mBitmapToBlur上
      mBlurringCanvas = new Canvas(mBitmapToBlur);

      mBlurringCanvas.scale(1f / mDownsampleFactor, 1f / mDownsampleFactor);
      mBlurInput = Allocation.createFromBitmap(mRenderScript, mBitmapToBlur,
          Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
      mBlurOutput = Allocation.createTyped(mRenderScript, mBlurInput.getType());
    }
    return true;
  }

  private float offsetY;

  /**
   * 渲染任务，可以在16ms完成，可以调用多核
   * 将mBitmapToBlur渲染为mBlurredBitmap输出
   */
  protected void blur(Bitmap mBitmapToBlur, Bitmap mBlurredBitmap) {
    if (!enabled) {
      return;
    }
    //类似于c中的alloc，这里是栈内存，这样就把bitmap放入了c的栈中
    mBlurInput.copyFrom(mBitmapToBlur);
    //滤镜加入输入源
    mBlurScript.setInput(mBlurInput);
    //滤镜进行渲染并输出到output
    mBlurScript.forEach(mBlurOutput);
    //将栈内存复制到bitmap
    mBlurOutput.copyTo(mBlurredBitmap);
  }

  /**
   * force enable blur, however it will only works on API 17 or higher
   */
  public void setEnabled(boolean enabled) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
      enabled = false;
    }
    this.enabled = enabled;
  }

  public void onDestroy() {
    if (mRenderScript != null) {
      mRenderScript.destroy();
    }
  }

  @Override public void draw(Canvas canvas) {
    if (!enabled) {
      return;
    }
    if (mBlurredBgView != null) {
      if (prepare()) {
        // If the background of the blurred view is a color drawable, we use it to clear
        // the blurring canvas, which ensures that edges of the child views are blurred
        // as well; otherwise we clear the blurring canvas with a transparent color.
        if (mBlurredBgView.getBackground() != null
            && mBlurredBgView.getBackground() instanceof ColorDrawable) {
          mBitmapToBlur.eraseColor(((ColorDrawable) mBlurredBgView.getBackground()).getColor());
        } else {
          mBitmapToBlur.eraseColor(Color.TRANSPARENT);
        }
        //在1920x1080中，只画一个大小为 136 x 244 的RecyclerView，这个View绘制了两次
        //类似于开发者选项中的多显示输出
        //将bitmaptoblur进行赋值
        mBlurredBgView.draw(mBlurringCanvas);
        //进行模糊渲染，生成mBlurredBitmap
        blur(mBitmapToBlur, mBlurredBitmap);
        //
        canvas.save();
        canvas.translate(mBlurredBgView.getX() - offsetX, mBlurredBgView.getY() - offsetY);
        //实际输出的只有 136 x 244的像素，缩放后就和当前view一样大了
        canvas.scale(mDownsampleFactor, mDownsampleFactor);
        canvas.drawBitmap(mBlurredBitmap, 0, 0, null);
        canvas.restore();
      }
      canvas.drawColor(mOverlayColor);
    }
  }


  public void setDrawOffset(float x, float y) {
    this.offsetX = x;
    this.offsetY = y;
  }

  @Override public void setAlpha(int alpha) {

  }

  @Override public void setColorFilter(ColorFilter colorFilter) {

  }

  @Override public int getOpacity() {
    return 0;
  }
}
