package com.github.miao1007.animewallpaper.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresPermission;
import android.support.annotation.UiThread;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;

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

  private void setWallPaperCompat(File file) {
    WallpaperManager wm = WallpaperManager.getInstance(context);
    try {
      wm.setBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
    } catch (IOException e) {
      Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
  }

  @TargetApi(Build.VERSION_CODES.KITKAT) private void setWallpaperKitkat(File file) {
    Uri uri = Uri.fromFile(file);
    Intent intent = new Intent(WallpaperManager.ACTION_CROP_AND_SET_WALLPAPER);
    String mime = "image/*";
    intent.setDataAndType(uri, mime);
    try {
      context.startActivity(intent);
    } catch (ActivityNotFoundException e) {
      setWallPaperCompat(file);
      e.printStackTrace();
    }
  }

  @RequiresPermission(android.Manifest.permission.SET_WALLPAPER)
  public void setWallpaper(File file) {
    if (file == null || !file.exists() || !file.canRead()) {
      Log.e(TAG, "file can't read");
      return;
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      setWallpaperKitkat(file);
    } else {
      setWallPaperCompat(file);
    }
  }
}
