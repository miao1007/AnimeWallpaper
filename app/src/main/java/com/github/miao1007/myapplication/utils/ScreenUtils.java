package com.github.miao1007.myapplication.utils;

import android.content.Context;
import android.util.Log;

/**
 * Created by leon on 4/14/15.
 */
public class ScreenUtils {
  public static int getStatusBarHeight(Context context) {
    Context appContext = context.getApplicationContext();
    int result = 0;
    int resourceId = appContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
      result = appContext.getResources().getDimensionPixelSize(resourceId);
    }
    Log.d("ScreenUtils", result + "");
    return result;
  }
}
