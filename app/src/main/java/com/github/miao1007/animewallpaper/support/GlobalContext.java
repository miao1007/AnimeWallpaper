package com.github.miao1007.animewallpaper.support;

import android.app.Application;
import com.github.miao1007.animewallpaper.BuildConfig;
import im.fir.sdk.FIR;

/**
 * Created by leon on 4/19/15.
 */
public class GlobalContext extends Application {

  private static GlobalContext instance = null;

  static public void startThirdFrameWork() {
    FIR.init(instance);
    FIR.addCustomizeValue("DEBUG", BuildConfig.DEBUG + "");
  }

  public static GlobalContext getInstance() {
    return instance;
  }

  @Override public void onCreate() {
    super.onCreate();
    instance = this;
  }
}
