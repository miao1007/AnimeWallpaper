package com.github.miao1007.animewallpaper.support.eventbus;

import android.os.Handler;
import android.os.Looper;
import com.squareup.otto.Bus;

/**
 * Created by leon on 15/5/27.
 * 主线程事件总线，方便在异步任务中回掉
 */
public class MainThreadBus extends Bus {
  private final Handler handler = new Handler(Looper.getMainLooper());

  @Override public void post(final Object event) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
      super.post(event);
    } else {
      handler.post(new Runnable() {
        @Override
        public void run() {
          MainThreadBus.super.post(event);
        }
      });
    }
  }
}