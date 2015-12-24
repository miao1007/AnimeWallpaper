package com.github.miao1007.myapplication.utils;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;
import com.github.miao1007.myapplication.BuildConfig;
import im.fir.sdk.FIR;
import im.fir.sdk.callback.VersionCheckCallback;
import im.fir.sdk.version.AppVersion;

/**
 * Created by leon on 15/5/16.
 * Check for update via Fir.im or wandoujia.You need to use a Theme.AppCompat theme (or descendant)
 * with this activity
 */
public class FIRUtils {

  public final static void checkForUpdate(Context context, boolean isShowToast) {
    VersionCheckCallback versionCheckCallback = callback(context, isShowToast);
    try {
      System.out.println("isShowToast = " + isShowToast);
      System.out.println("versionCheckCallback = " + versionCheckCallback);
      System.out.println("FIR.GENERAL_KEY = " + FIR.GENERAL_KEY);
      if (BuildConfig.DEBUG) {
        FIR.checkForUpdateInFIR(context, FIR.GENERAL_KEY, versionCheckCallback);
      } else {
        FIR.checkForUpdateInAppStore(context, versionCheckCallback);
      }
    } catch (Exception e) {
      e.printStackTrace();
      Toast.makeText(context, "检查更新失败", Toast.LENGTH_SHORT).show();
    }
  }

  static VersionCheckCallback callback(final Context context, final boolean isShowToast) {
    return new VersionCheckCallback() {
      @Override public void onSuccess(final AppVersion appVersion, boolean b) {

        if (appVersion.getVersionCode() <= BuildConfig.VERSION_CODE) {
          if (isShowToast) Toast.makeText(context, "你已经是最新版本", Toast.LENGTH_SHORT).show();

          return;
        }
        try {
          new AlertDialog.Builder(context).setTitle("有新版本可用")
              .setMessage(appVersion.getChangeLog())
              .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                  if (ActivityCompat.checkSelfPermission(context,
                      Manifest.permission.WRITE_EXTERNAL_STORAGE)
                      != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                  }
                  DownloadUtils.DownloadApkWithProgress(context.getApplicationContext(),
                      appVersion.getUpdateUrl());
                }
              })
              .create()
              .show();
          //IllegalStateException when using appcompAlertDialog or NullException when windows leak
        } catch (Exception e) {
          Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
      }

      @Override public void onFail(String s, int i) {
        if (isShowToast) Toast.makeText(context, "检查更新失败，code = " + i, Toast.LENGTH_SHORT).show();
        if (isShowToast) {
          Toast.makeText(context, "请在网页中下载", Toast.LENGTH_SHORT).show();
          Intent intent = new Intent();
          intent.setAction(Intent.ACTION_VIEW);
          intent.setData(Uri.parse("http://fir.im/fqxr"));
          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          context.startActivity(intent);
        }
      }

      @Override public void onError(Exception e) {
        if (isShowToast) Toast.makeText(context, "检查更新失败", Toast.LENGTH_SHORT).show();
      }

      @Override public void onStart() {
        if (isShowToast) Toast.makeText(context, "开始检查更新", Toast.LENGTH_SHORT).show();
      }

      @Override public void onFinish() {

      }
    };
  }
}
