package com.github.miao1007.animewallpaper.support;

import android.app.Application;

/**
 * Created by leon on 4/19/15.
 */
public class GlobalContext extends Application {

  private static GlobalContext instance = null;

  static public void startThirdFrameWork() {

  }

  public static GlobalContext getInstance() {
    return instance;
  }

  @Override public void onCreate() {
    super.onCreate();
    instance = this;
  }
}
