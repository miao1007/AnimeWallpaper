package com.github.miao1007.animewallpaper.utils.picasso;

import android.content.Context;
import android.support.annotation.WorkerThread;
import android.util.Log;
import com.github.miao1007.animewallpaper.support.GlobalContext;
import com.github.miao1007.animewallpaper.support.api.konachan.ImageRepo;
import com.github.miao1007.animewallpaper.utils.network.HttpLoggingInterceptor;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by leon on 1/26/16.
 * Singleton Utils
 */
public class SquareUtils {

  static final String TAG = "SquareUtils";

  static private Picasso picasso;

  static private OkHttpClient client;

  static private Retrofit retrofit;

  /**
   * Not singleton
   */
  static public OkHttpClient getProgressBarClient(final ProgressListener listener) {

    return getClient().newBuilder().addNetworkInterceptor(new Interceptor() {
      @Override public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
            .body(new ProgressResponseBody(originalResponse.body(), listener))
            .build();
      }
    }).build();
  }

  static public Interceptor getLogger() {
    HttpLoggingInterceptor loggingInterceptor =
        new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
          @Override public void log(String message) {
            Log.d("retrofit", message);
          }
        });
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
    return loggingInterceptor;
  }

  static public synchronized OkHttpClient getClient() {
    if (client == null) {
      final File cacheDir = GlobalContext.getInstance().getExternalCacheDir();
      client = new OkHttpClient.Builder().addNetworkInterceptor(getLogger())
          //.addInterceptor(ImageRepo.CDN)
          .cache(new Cache(new File(cacheDir, "okhttp"), 60 * 1024 * 1024)).build();
    }
    return client;
  }

  /**
   * Singleton Picasso shared cache with OkH ttp/Retrofit
   */
  static public Picasso getPicasso(Context context) {

    if (picasso == null) {
      synchronized (SquareUtils.class) {
        picasso = new Picasso.Builder(context).downloader(new OkHttp3Downloader(getClient()))
            .build();
      }
    }
    return picasso;
  }

  /**
   * Download Big Image only, Not singleton but shared cache
   */
  static public Picasso getPicasso(Context context, ProgressListener listener) {

    return new Picasso.Builder(context).downloader(
        new OkHttp3Downloader(getProgressBarClient(listener)))
        .loggingEnabled(true)
        .memoryCache(com.squareup.picasso.Cache.NONE)
        .build();
  }

  public interface ProgressListener {
    @WorkerThread void update(long bytesRead, long contentLength, boolean done);
  }

  private static class ProgressResponseBody extends ResponseBody {

    private final ResponseBody responseBody;
    private final ProgressListener progressListener;
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
      this.responseBody = responseBody;
      this.progressListener = progressListener;
    }

    @Override public MediaType contentType() {
      return responseBody.contentType();
    }

    @Override public long contentLength() {
      return responseBody.contentLength();
    }

    @Override public BufferedSource source() {
      if (bufferedSource == null) {
        bufferedSource = Okio.buffer(source(responseBody.source()));
      }
      return bufferedSource;
    }

    private Source source(Source source) {

      return new ForwardingSource(source) {
        long totalBytesRead = 0L;

        @Override public long read(Buffer sink, long byteCount) throws IOException {
          long bytesRead = super.read(sink, byteCount);
          // read() returns the number of bytes read, or -1 if this source is exhausted.
          totalBytesRead += bytesRead != -1 ? bytesRead : 0;
          progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
          return bytesRead;
        }
      };
    }
  }

  public static Retrofit getRetrofit() {
    if (retrofit == null) {
      synchronized (SquareUtils.class) {
        retrofit = new Retrofit.Builder().baseUrl(ImageRepo.END_POINT_KONACHAN).client(getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();
      }
    }
    return retrofit;
  }
}
