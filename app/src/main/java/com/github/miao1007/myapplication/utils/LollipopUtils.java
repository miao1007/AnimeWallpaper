package com.github.miao1007.myapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by leon on 4/14/15.
 */
public class LollipopUtils {

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

  public static void setStatusbarColor(Activity activity, View holder) {

    //对于Lollipop 的设备，只需要在style.xml中设置colorPrimaryDark即可

    //对于4.4的设备，如下设置padding即可，颜色同样在style.xml中配置
    Window w = activity.getWindow();
    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
      w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
          WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      int statusBarHeight = getStatusBarHeight(activity);
      holder.setPadding(0, statusBarHeight, 0, 0);
      return;
    }
  }

  public static void hideStatusbar(Activity activity) {

    //对于Lollipop的设备，只需要在style.xml中设置colorPrimaryDark即可

    //对于4.4的设备，如下即可
    Window w = activity.getWindow();

    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
      w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
          WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      return;
    }
  }
}
