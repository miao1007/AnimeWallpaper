package com.github.miao1007.animewallpaper.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
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
 * Blur from @link https://github.com/500px/500px-android-blur
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
      Log.d(TAG, Position.from(mProgress).toString());
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

  public void setFitTranslucent(final boolean translucent) {
    post(new Runnable() {
      @Override public void run() {
        if (StatusbarUtils.isLessKitkat() || !translucent) {
          return;
        }
        int height = StatusbarUtils.getStatusBarOffsetPx(getContext());
        setPadding(getPaddingLeft(), height, getPaddingRight(), getPaddingBottom());
        getLayoutParams().height += height;
      }
    });
  }

  /**
   * Create a new relativelayout and inflate xml in it,
   * remember use merge instead of RelativeLayout
   */
  private void initView() {
    View.inflate(getContext(), R.layout.internal_navigationbar, this);  //correct way to inflate..
    ButterKnife.bind(this);
    setFitTranslucent(true);
    initBlur();
  }

  private void initBlur() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
      return;
    }
    initializeRenderScript(getContext());
    //设置blur半径, iOS中默认为12px
    setBlurRadius(12);
    //图片缩放等级，缩放越大越节约性能，理论要在100px^2以内
    setDownsampleFactor(8);
    setOverlayColor(Color.parseColor("#B9ffffff"));
  }

  public void setBlurredView(@NonNull View blurredView) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
      return;
    }
    mBlurredBgView = blurredView;
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
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

        mBlurredBgView.draw(mBlurringCanvas);
        blur();

        canvas.save();
        canvas.translate(mBlurredBgView.getX() - getX(), mBlurredBgView.getY() - getY());
        //实际输出的只有 136 x 244的像素，缩放后就和当前view一样大了
        canvas.scale(mDownsampleFactor, mDownsampleFactor);
        canvas.drawBitmap(mBlurredBitmap, 0, 0, null);
        canvas.restore();
      }
      canvas.drawColor(mOverlayColor);
    }
  }

  public void setBlurRadius(int radius) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
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

  protected boolean prepare() {
    //1080 x 1920
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

      mBlurringCanvas = new Canvas(mBitmapToBlur);
      mBlurringCanvas.scale(1f / mDownsampleFactor, 1f / mDownsampleFactor);
      mBlurInput = Allocation.createFromBitmap(mRenderScript, mBitmapToBlur,
          Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
      mBlurOutput = Allocation.createTyped(mRenderScript, mBlurInput.getType());
    }
    return true;
  }

  protected void blur() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
      return;
    }
    mBlurInput.copyFrom(mBitmapToBlur);
    mBlurScript.setInput(mBlurInput);
    mBlurScript.forEach(mBlurOutput);
    mBlurOutput.copyTo(mBlurredBitmap);
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (mRenderScript != null) {
      mRenderScript.destroy();
    }
  }
}


