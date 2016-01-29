package com.github.miao1007.animewallpaper.support;

import android.app.Application;
import com.github.miao1007.animewallpaper.support.eventbus.MainThreadBus;

/**
 * Created by leon on 4/19/15.
 */
public class GlobalContext extends Application {


  public static GlobalContext instance = null;

  public static final MainThreadBus BUS = new MainThreadBus();

  @Override public void onCreate() {
    super.onCreate();
    instance = this;
  }

  public static GlobalContext getInstance(){
    return instance;
  }
}
