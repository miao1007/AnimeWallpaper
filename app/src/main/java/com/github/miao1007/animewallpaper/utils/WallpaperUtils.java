package com.github.miao1007.animewallpaper.utils;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.UiThread;
import com.github.miao1007.animewallpaper.R;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;

/**
 * Created by leon on 1/30/16.
 */
@UiThread final public class WallpaperUtils {

  private static final String TAG = "WallpaperUtils";

  private WallpaperUtils() {
    throw new IllegalStateException("Can't be a instance");
  }

  /**
   * Open with setWallpaper or set contact
   */
  @TargetApi(Build.VERSION_CODES.KITKAT) private static void setWallpaperKitkat(Context context,
      File file) {
    Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    intent.putExtra("mimeType", "image/jpg");
    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(),
        BitmapFactory.decodeFile(file.getAbsolutePath()), null, null));
    intent.setData(uri);
    try {
      if (Build.VERSION.SDK_INT >= 24) {
        try {
          Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
          m.invoke(null);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      context.startActivity(intent);
    } catch (ActivityNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * call system image viewer to share
   */
  public static void previewImage(Context context, File file) {
    final Intent shareIntent = new Intent(Intent.ACTION_VIEW);
    shareIntent.setDataAndType(Uri.fromFile(file), "image/*");
    context.startActivity(
        Intent.createChooser(shareIntent, context.getString(R.string.view_image_by)));
  }

  public static void refreshAlbum(Context context, File file, String fileName) {
    // 其次把文件插入到系统图库
    try {
      MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getPath(), fileName,
          null);
      // 最后通知图库更新
      //        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
      context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static void setWallpaper(Context context, File file) {
    setWallpaperKitkat(context, file);
  }
}
