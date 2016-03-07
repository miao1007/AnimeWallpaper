package com.github.miao1007.animewallpaper.utils;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

/**
 * Created by leon on 2/16/15.
 * Download update apk with SystemService.
 */
class DownloadUtils {

  private static final String MINETYPE_APPLCATION = "application/vnd.android.package-archive";

  @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
  public static long DownloadApkWithProgress(Context context, String url) {

    DownloadManager downloadManager =
        (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
    request.setDestinationInExternalPublicDir("/appupdate", "update.apk");
    request.setTitle("Updating: " + context.getPackageName());
    request.setMimeType(MINETYPE_APPLCATION);
    long downloadId = 0;
    //fix crash on SecurityException.
    try {
      downloadId = downloadManager.enqueue(request);
      //this register may cause memory leak if installation failed.
      context.registerReceiver(new DownloadCompleteReceiver(),
          new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    } catch (SecurityException e) {
      Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }
    return downloadId;
  }

  /**
   * Created by leon on 2/17/15.
   * Called when download update apk complete.
   */
  public static class DownloadCompleteReceiver extends BroadcastReceiver {


    @Override public void onReceive(Context context, Intent intent) {

      if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
        long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        DownloadManager downloadManager =
            (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        installIntent.setDataAndType(downloadManager.getUriForDownloadedFile(completeDownloadId),
            MINETYPE_APPLCATION);
        try {
          context.startActivity(installIntent);
        } catch (ActivityNotFoundException e) {
          Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
          LocalBroadcastManager.getInstance(context).unregisterReceiver(DownloadCompleteReceiver.this);
        }
      }
    }
  }
}
