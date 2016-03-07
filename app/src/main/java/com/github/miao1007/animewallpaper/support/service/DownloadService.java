package com.github.miao1007.animewallpaper.support.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import com.github.miao1007.animewallpaper.utils.SquareUtils;
import java.io.File;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

public class DownloadService extends Service {

  private static final String EXTRA_URL = "url";

  public DownloadService() {

  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    final String url = intent.getStringExtra(EXTRA_URL);
    final OkHttpClient client = SquareUtils.getClient();
    //getClient.dispatcher().
    final Request request = new Request.Builder().url(url).get().build();
    client.newCall(request).enqueue(new Callback() {
      @Override public void onFailure(Call call, IOException e) {

      }

      @Override public void onResponse(Call call, Response response) throws IOException {
        try {
          File file = new File(getExternalCacheDir(), Uri.parse(url).getLastPathSegment());
          BufferedSink sink = Okio.buffer(Okio.sink(file));
          sink.writeAll(response.body().source());
          sink.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });

    return super.onStartCommand(intent, flags, startId);
  }

  @Override public IBinder onBind(Intent intent) {
    // TODO: Return the communication channel to the service.
    throw new UnsupportedOperationException("Not yet implemented");
  }
}
