package com.github.miao1007.myapplication.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import com.github.miao1007.myapplication.R;
import java.lang.reflect.Field;

/**
 * Created by leon on 10/31/15.
 */
public class FlyMeUtils {

  /**
   * setDarkStatusBar on FlyMe
   * 设置状态栏字体为暗色 仅魅族有效
   */
  public static void setDarkStatusBar(Activity activity, boolean isDark) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      //activity.getWindow().setBackgroundDrawableResource(R.mipmap.window_bg);
      //activity.getWindow().getDecorView().getSystemUiVisibility()
      //int flag = isDark?View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR:View.DARK
      activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
      //activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
    }
    WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
    try {
      Class<?> instance = Class.forName("android.view.WindowManager$LayoutParams");
      int value = instance.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON").getInt(lp);
      Field field = instance.getDeclaredField("meizuFlags");
      field.setAccessible(true);
      int origin = field.getInt(lp);
      if (isDark) {
        field.set(lp, origin | value);
      } else {
        field.set(lp, (~value) & origin);
      }
    } catch (Exception ignored) {
      //
    }
  }
}
