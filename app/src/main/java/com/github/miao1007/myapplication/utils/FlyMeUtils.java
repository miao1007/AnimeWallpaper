package com.github.miao1007.myapplication.utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import java.lang.reflect.Field;

/**
 * Created by leon on 10/31/15.
 */
public class FlyMeUtils {

  /**
   * setLightStatusBar on FlyMe
   * 设置状态栏字体为暗色 仅魅族有效
   */
  public static void setLightStatusBar(Activity activity, boolean isLight) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      //1. 让状态栏与window重叠
      //2. 设置状态栏字体颜色
      //activity.getWindow()
      //    .setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,
      //        WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      View window = activity.getWindow().getDecorView();
      if (isLight) {
        window.setSystemUiVisibility(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
      } else {
        window.setSystemUiVisibility(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
            & (~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR));
      }

      return;
    }

    WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
    try {
      Class<?> instance = Class.forName("android.view.WindowManager$LayoutParams");
      int value = instance.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON").getInt(lp);
      Field field = instance.getDeclaredField("meizuFlags");
      field.setAccessible(true);
      int origin = field.getInt(lp);
      if (isLight) {
        field.set(lp, origin | value);
      } else {
        field.set(lp, (~value) & origin);
      }
    } catch (Exception ignored) {
      //
    }
  }
}
