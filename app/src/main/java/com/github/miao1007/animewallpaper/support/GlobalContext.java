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
    /*
    * 替换为你自己的key,本项目key不再公开
    * see {@link <a href="http://bughd.com/doc/android">BUGHD</a>}
    * */
    FIR.init(instance,BuildConfig.BUG_HD_SDK_GENERAL_KEY);
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
