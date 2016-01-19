package com.github.miao1007.myapplication.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import java.lang.reflect.Field;

/**
 * Created by leon on 10/31/15.
 */
public final class StatusbarUtils {

  static final String TAG = "StatusbarUtils";
  boolean lightStatusBar;
  //透明且背景不占用控件的statusbar
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
   * 让statusbar与window重叠，表明window不再padding于statusbar
   * 在Bugme下，默认是透明的
   * 在
   */
  @TargetApi(19) public static void setTranslucent(Activity activity) {
    //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
    //  activity.getWindow()
    //      .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
    //          WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    //}

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      Window window = activity.getWindow();
      //window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
      window.setStatusBarColor(Color.TRANSPARENT);
    }
  }

  /**
   * Default status dp = 24 or 25
   * mhdpi = dp * 1
   * hdpi = dp * 1.5
   * xhdpi = dp * 2
   * xxhdpi = dp * 3
   * eg : 1920x1080, xxhdpi, => status/all = 25/640(dp) = 75/1080(px)
   *
   * and toolbar's dp = 48
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
   * (24dp ＋ 48dp)
   * 对于Kitkat的设备，首先让statusbar与window重叠，然后设置toolbar父元素的padding，最终statusbar的颜色由父元素back决定
   * 全透明方案：自定义的view一般是透明的，所以全部由parent决定颜色，不需要进行任何设置
   * Lollipop方案：自定义的view设置为primary，parent设置为primaryDark，就是谷歌推荐的
   * Bugme方案：自定义的view透明，parent设置为primary/primaryDark，就是全部一种颜色
   */
  @Deprecated public static void setTranslucentAndFit(Activity activity, View view) {

    if (view == null) {
      throw new NullPointerException("The Holder view is NULL!");
    }

    //对于Lollipop 的设备，只需要在style.xml中设置colorPrimaryDark即可

    //对于4.4的设备，如下设置padding即可，颜色同样在style.xml中配置
    if (isKitkat()) {
      setTranslucent(activity);
      //((View) view.getParent()).setPadding(0, getStatusBarHeight(activity), 0, 0);
      view.getLayoutParams().height += getStatusBarHeight(activity);
      view.setPadding(0, getStatusBarHeight(activity), 0, 0);
    }
    if (isMoreLollipop()) {
      setTranslucent(activity);
      ((ViewGroup.MarginLayoutParams) ((View) view.getParent()).getLayoutParams()).setMargins(0,
          getStatusBarHeight(activity), 0, 0);
    }
  }

  public void process() {
    int current = Build.VERSION.SDK_INT;

    if (current == Build.VERSION_CODES.KITKAT) {
      processKitkat();
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      processM();
    }
  }

  @TargetApi(Build.VERSION_CODES.KITKAT) void processKitkat() {
    //int current = activity.getWindow().gef
    int flag = 0;
    if (transparentStatusbar) {
      activity.getWindow()
          .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
              WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    } else {
      activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }
    processFlymelightStatusBar(lightStatusBar);
  }

  private void processFlymelightStatusBar(boolean isLightStatusBar) {
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

  @TargetApi(Build.VERSION_CODES.M) void processM() {
    Window window = activity.getWindow();
    int flag = 0;
    if (lightStatusBar) {
      flag |= (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
          | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
    if (transparentStatusbar) {
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