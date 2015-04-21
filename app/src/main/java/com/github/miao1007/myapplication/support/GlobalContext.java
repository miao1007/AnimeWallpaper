package com.github.miao1007.myapplication.support;

import android.app.Application;

/**
 * Created by leon on 4/19/15.
 */
public class GlobalContext extends Application {

  public static GlobalContext instance = null;

  @Override public void onCreate() {
    super.onCreate();
    instance = this;
  }

  public static GlobalContext getInstance(){
    return instance;
  }
}
