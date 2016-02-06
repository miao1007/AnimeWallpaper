package com.github.miao1007.animewallpaper.utils;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.RequiresPermission;
import android.support.annotation.UiThread;
import android.util.Log;
import java.io.File;

/**
 * Created by leon on 1/30/16.
 */
@UiThread final public class WallpaperUtils {

  private static final String TAG = "WallpaperUtils";

  Activity context;

  private WallpaperUtils(Activity context) {
    this.context = context;
  }

  public static WallpaperUtils from(Activity activity) {
    return new WallpaperUtils(activity);
  }

  //public static void onActivityResult(int requestCode, int resultCode, Intent data) {
  //  if (requestCode != TAG) {
  //    return;
  //  }
  //  setWallPaperCompat();
  //}


  @RequiresPermission(android.Manifest.permission.SET_WALLPAPER)
  public void setWallpaper(File file) {
    if (file == null || !file.exists() || !file.canRead()) {
      Log.e(TAG, "file can't read");
      return;
    }
    Uri uri = Uri.fromFile(file);
    Intent intent = new Intent(WallpaperManager.ACTION_CROP_AND_SET_WALLPAPER);
    String mime = "image/*";
    intent.setDataAndType(uri, mime);
    try {
      context.startActivity(intent);
    } catch (ActivityNotFoundException e) {
      e.printStackTrace();
    }
  }
}
