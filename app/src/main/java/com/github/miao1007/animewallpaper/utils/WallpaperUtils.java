package com.github.miao1007.animewallpaper.utils;

import android.annotation.TargetApi;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresPermission;
import android.support.annotation.UiThread;
import android.widget.Toast;
import com.github.miao1007.animewallpaper.R;
import java.io.File;
import java.io.IOException;

/**
 * Created by leon on 1/30/16.
 */
@UiThread final public class WallpaperUtils {

  private static final String TAG = "WallpaperUtils";

  private WallpaperUtils() {
    throw new IllegalStateException("Can't be a instance");
  }

  //public static void onActivityResult(int requestCode, int resultCode, Intent data) {
  //  if (requestCode != TAG) {
  //    return;
  //  }
  //  setWallPaperCompat();
  //}

  /**
   * Kitkat lower can't effectively use planet wallpaper
   * so crop is not required
   */
  private static void setWallPaperCompat(Context context, File file) {
    WallpaperManager wm = WallpaperManager.getInstance(context);
    try {
      wm.setBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
    } catch (IOException | OutOfMemoryError e) {
      Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
      previewImage(context, file);
    }
  }

  @TargetApi(Build.VERSION_CODES.KITKAT)
  private static void setWallpaperKitkat(Context context, File file) {
    Uri uri = Uri.fromFile(file);
    Intent intent = new Intent(WallpaperManager.ACTION_CROP_AND_SET_WALLPAPER);
    String mime = "image/*";
    intent.setDataAndType(uri, mime);
    try {
      context.startActivity(intent);
    } catch (ActivityNotFoundException e) {
      setWallPaperCompat(context, file);
      e.printStackTrace();
    }
  }

  public static void previewImage(Context context, File file) {
    final Intent shareIntent = new Intent(Intent.ACTION_VIEW);
    shareIntent.setDataAndType(Uri.fromFile(file), "image/*");
    context.startActivity(
        Intent.createChooser(shareIntent, context.getString(R.string.view_image_by)));
  }

  @RequiresPermission(android.Manifest.permission.SET_WALLPAPER)
  public static void setWallpaper(Context context, File file) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      setWallpaperKitkat(context, file);
    } else {
      setWallPaperCompat(context, file);
    }
  }
}
