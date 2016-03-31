package com.github.miao1007.animewallpaper.support;

import android.app.Application;
import im.fir.sdk.FIR;

/**
 * Created by leon on 4/19/15.
 */
public class GlobalContext extends Application {

  private static GlobalContext instance = null;

  @Override public void onCreate() {
    super.onCreate();
    instance = this;
  }

  static public void startThirdFrameWork(){
    FIR.init(instance);
  }

  public static GlobalContext getInstance(){
    return instance;
  }
}
