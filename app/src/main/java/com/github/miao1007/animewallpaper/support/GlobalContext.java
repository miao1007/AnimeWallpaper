package com.github.miao1007.animewallpaper.support;

import android.app.Application;
import android.os.Build;
import im.fir.sdk.FIR;

/**
 * Created by leon on 4/19/15.
 */
public class GlobalContext extends Application {


  public static GlobalContext instance = null;


  @Override public void onCreate() {
    super.onCreate();
    instance = this;
    FIR.init(this);
  }

  public static GlobalContext getInstance(){
    return instance;
  }
}
