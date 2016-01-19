package com.github.miao1007.myapplication.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by leon on 10/31/15.
 */
public final class StatusbarUtils {

  static final String TAG = "StatusbarUtils";
  boolean lightStatusBar;
  //透明且背景不占用控件的statusbar，这里姑且叫做沉浸
  boolean transparentStatusbar;
  Activity activity;
  public StatusbarUtils(Activity activity, boolean lightStatusBar, boolean transparentStatusbar) {
    this.lightStatusBar = lightStatusBar;
    this.transparentStatusbar = transparentStatusbar;
    this.activity = activity;
  }

  public static boolean isKitkat() {
    return Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT;
  }

  public static boolean isMoreLollipop() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
  }

  public static Builder from(Activity activity) {
    return new StatusbarUtils.Builder().setActivity(activity);
  }

  /**
   * Default status dp = 24 or 25
   * mhdpi = dp * 1
   * hdpi = dp * 1.5
   * xhdpi = dp * 2
   * xxhdpi = dp * 3
   * eg : 1920x1080, xxhdpi, => status/all = 25/640(dp) = 75/1080(px)
   *
   * don't forget toolbar's dp = 48
   *
   * @return px
   */
  public static int getStatusBarHeight(Context context) {
    Context appContext = context.getApplicationContext();
    int result = 0;
    int resourceId =
        appContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
      result = appContext.getResources().getDimensionPixelSize(resourceId);
    }
    Log.d("ScreenUtils", result + "");
    return result;
  }

  /**
   * 调用私有API处理颜色
   */
  public void processPrivateAPI() {
    processFlyme(lightStatusBar);
    processMIUI(lightStatusBar);
  }

  public void process() {
    int current = Build.VERSION.SDK_INT;
    //处理4.4沉浸
    if (current == Build.VERSION_CODES.KITKAT) {
      processKitkat();
    }
    //6.0处理沉浸与颜色，5.0只可以处理沉浸(不建议用白色背景)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      processLollipopAbove();
    }
    //调用私有API处理颜色
    processPrivateAPI();
  }

  /**
   * 处理4.4沉浸
   */
  @TargetApi(Build.VERSION_CODES.KITKAT) void processKitkat() {
    //int current = activity.getWindow().gef
    Window win = activity.getWindow();
    WindowManager.LayoutParams winParams = win.getAttributes();
    final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
    if (transparentStatusbar) {
      winParams.flags |= bits;
    } else {
      winParams.flags &= ~bits;
    }
    win.setAttributes(winParams);
  }

  /**
   * 改变小米的状态栏字体颜色为黑色, 要求MIUI6以上
   * Tested on: MIUIV7 5.0 Redmi-Note3
   */
  void processMIUI(boolean lightStatusBar) {
    Class<? extends Window> clazz = activity.getWindow().getClass();
    try {
      int darkModeFlag = 0;
      Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
      Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
      darkModeFlag = field.getInt(layoutParams);
      Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
      extraFlagField.invoke(activity.getWindow(), lightStatusBar ? darkModeFlag : 0, darkModeFlag);
    } catch (Exception ignored) {

    }
  }

  /**
   * 改变魅族的状态栏字体为黑色，要求FlyMe4以上
   */
  private void processFlyme(boolean isLightStatusBar) {
    WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
    try {
      Class<?> instance = Class.forName("android.view.WindowManager$LayoutParams");
      int value = instance.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON").getInt(lp);
      Field field = instance.getDeclaredField("meizuFlags");
      field.setAccessible(true);
      int origin = field.getInt(lp);
      if (isLightStatusBar) {
        field.set(lp, origin | value);
      } else {
        field.set(lp, (~value) & origin);
      }
    } catch (Exception ignored) {
      //
    }
  }

  /**
   * 处理Lollipop以上
   * Lollipop可以设置为沉浸，不能设置字体颜色
   * M(API23)可以设定
   */
  @TargetApi(Build.VERSION_CODES.LOLLIPOP) void processLollipopAbove() {
    Window window = activity.getWindow();
    int flag = window.getDecorView().getSystemUiVisibility();
    if (lightStatusBar) {
      /**
       * see {@link <a href="https://developer.android.com/reference/android/R.attr.html#windowLightStatusBar"></a>}
       */
      flag |= (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
          | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
    if (transparentStatusbar) {
      //改变字体颜色
      flag |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
    }
    window.getDecorView().setSystemUiVisibility(flag);
    window.setStatusBarColor(Color.TRANSPARENT);
  }

  final public static class Builder {
    private Activity activity;
    private boolean lightStatusBar = false;
    private boolean transparentStatusbar = false;

    Builder setActivity(Activity activity) {
      this.activity = activity;
      return this;
    }

    public Builder setLightStatusBar(boolean lightStatusBar) {
      this.lightStatusBar = lightStatusBar;
      return this;
    }

    public Builder setTransparentStatusbar(boolean transparentStatusbar) {
      this.transparentStatusbar = transparentStatusbar;
      return this;
    }

    public void process() {
      new StatusbarUtils(activity, lightStatusBar, transparentStatusbar).process();
    }
  }
}