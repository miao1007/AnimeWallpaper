package com.github.miao1007.myapplication.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by leon on 10/31/15.
 */
public class StatusbarUtils {

  static final String TAG = "StatusbarUtils";

  public static boolean isKitkat() {
    return Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT;
  }

  public static boolean isMoreLollipop() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
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
   * 让statusbar与window重叠，表明window不再padding于statusbar
   * @param activity
   */
  @TargetApi(19) public static void setTranslucent(Activity activity) {
    activity.getWindow()
        .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
  }

  /**
   * (24dp ＋ 48dp)
   * 对于Kitkat的设备，首先让statusbar与window重叠，然后设置toolbar父元素的padding，最终statusbar的颜色由父元素back决定
   * 全透明方案：自定义的view一般是透明的，所以全部由parent决定颜色，不需要进行任何设置
   * Lollipop方案：自定义的view设置为primary，parent设置为primaryDark，就是谷歌推荐的
   * Bugme方案：自定义的view透明，parent设置为primary/primaryDark，就是全部一种颜色
   * @param activity
   * @param view
   */
  public static void setTranslucentAndFit(Activity activity, View view) {

    if (view == null){
      throw new NullPointerException("The Holder view is NULL!");
    }

    //对于Lollipop 的设备，只需要在style.xml中设置colorPrimaryDark即可

    //对于4.4的设备，如下设置padding即可，颜色同样在style.xml中配置
    if (isKitkat()) {
      setTranslucent(activity);
      ((View) view.getParent()).setPadding(0, getStatusBarHeight(activity), 0, 0);
    }
    if (isMoreLollipop()) {
      setTranslucent(activity);
      ((View) view.getParent()).setPadding(0, getStatusBarHeight(activity), 0, 0);
    }
  }
}