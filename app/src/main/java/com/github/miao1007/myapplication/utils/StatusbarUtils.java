package com.github.miao1007.myapplication.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by leon on 10/31/15.
 */
public class StatusbarUtils {

  public static boolean isKitkat() {
    return Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT;
  }

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

  @TargetApi(19) public static void setTranslucent(Activity activity) {
    if (isKitkat()) {
      activity.getWindow()
          .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
              WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }
  }

  public static void setStatusbarColor(Activity activity, View view) {

    //对于Lollipop 的设备，只需要在style.xml中设置colorPrimaryDark即可

    //对于4.4的设备，如下设置padding即可，颜色同样在style.xml中配置
    setTranslucent(activity);
    if (isKitkat()) {
      view.setPadding(0, getStatusBarHeight(activity), 0, 0);
    }
  }
}