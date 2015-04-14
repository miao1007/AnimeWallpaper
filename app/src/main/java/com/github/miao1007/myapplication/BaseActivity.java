package com.github.miao1007.myapplication;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import com.github.miao1007.myapplication.utils.ScreenUtils;

/**
 * Created by leon on 4/14/15.
 */
public class BaseActivity extends ActionBarActivity {

  protected void setToolbarColor(Toolbar mToolbar, int defaultColor) {
    mToolbar.setBackgroundColor(defaultColor);

    Window w = getWindow();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      w.setStatusBarColor(defaultColor);
    }

    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
      w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
          WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      int statusBarHeight = ScreenUtils.getStatusBarHeight(this);
      mToolbar.setPadding(0, statusBarHeight, 0, 0);
    }
  }

  protected void setToolbarColor(Toolbar mToolbar) {
    ColorDrawable colorDrawable = (ColorDrawable) mToolbar.getBackground();
    setToolbarColor(mToolbar, colorDrawable.getColor());
  }
}
