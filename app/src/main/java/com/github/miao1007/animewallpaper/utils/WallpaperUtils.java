package com.github.miao1007.animewallpaper.utils;

import android.annotation.TargetApi;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresPermission;
import android.support.annotation.UiThread;
import android.widget.Toast;
import com.github.miao1007.animewallpaper.R;
import java.io.File;
import java.io.IOException;
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
   * @param context
   * @param file
   */
  @TargetApi(Build.VERSION_CODES.KITKAT)
  private static void setWallpaperKitkat(Context context, File file) {
    Uri uri = Uri.fromFile(file);
    Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
    String mime = "image/*";
    intent.putExtra("mimeType", "image/jpg");
    intent.setDataAndType(uri, mime);
    try {
      if(Build.VERSION.SDK_INT>=24){
        try{
          Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
          m.invoke(null);
        }catch(Exception e){
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
   * @param context
   * @param file
   */
  public static void previewImage(Context context, File file) {
    final Intent shareIntent = new Intent(Intent.ACTION_VIEW);
    shareIntent.setDataAndType(Uri.fromFile(file), "image/*");
    context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.view_image_by)));
  }

  public static void setWallpaper(Context context, File file) {
    setWallpaperKitkat(context, file);
  }
}
