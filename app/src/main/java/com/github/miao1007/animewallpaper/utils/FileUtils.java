package com.github.miao1007.animewallpaper.utils;

import android.os.Environment;
import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import com.github.miao1007.animewallpaper.R;
import com.github.miao1007.animewallpaper.support.GlobalContext;
import java.io.File;
import java.io.IOException;
import okhttp3.ResponseBody;
import okhttp3.internal.io.FileSystem;
import okio.BufferedSink;
import okio.Okio;

/**
 * Created by leon on 2/6/16.
 */
@WorkerThread public final class FileUtils {

  public static String EXT_STORAGE = Environment.getExternalStorageDirectory().getPath()
      + File.separator
      + GlobalContext.getInstance().getString(R.string.app_name)
      + File.separator;

  /**
   * Save bitmap to /sdcard/${name}
   */
  @CheckResult @Nullable public static File saveBodytoFile(ResponseBody body, String name) {
    final File wallpaper = new File(EXT_STORAGE, name);
    final FileSystem fileSystem = FileSystem.SYSTEM;
    try {
      final BufferedSink sink = Okio.buffer(fileSystem.sink(wallpaper));
      sink.writeAll(body.source());
      sink.close();
      return wallpaper;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}
