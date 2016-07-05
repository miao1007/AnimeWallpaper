package com.github.miao1007.animewallpaper.utils;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.WorkerThread;
import android.util.Log;
import com.github.miao1007.animewallpaper.support.GlobalContext;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import im.fir.sdk.FIR;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import okhttp3.Cache;
import okhttp3.Dispatcher;
import okhttp3.Dns;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by leon on 1/26/16.
 * Singleton Utils
 */
public abstract class SquareUtils {

  static Dispatcher dispatcher;
  static private Picasso picasso;
  static private OkHttpClient client;
  static private OkHttpClient httpDnsclient;
  static Dns HTTP_DNS = new Dns() {
    @Override public List<InetAddress> lookup(String hostname) throws UnknownHostException {
      if (hostname == null) throw new UnknownHostException("hostname == null");
      HttpUrl httpUrl = new HttpUrl.Builder().scheme("http")
          .host("119.29.29.29")
          .addPathSegment("d")
          .addQueryParameter("dn", hostname)
          .build();
      Request dnsRequest = new Request.Builder().url(httpUrl).get().build();
      try {
        String s = getHTTPDnsClient().newCall(dnsRequest).execute().body().string();
        //free server may down, will return loopback ip address
        if (!s.matches("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b")) {
          return Dns.SYSTEM.lookup(hostname);
        }
        return Arrays.asList(InetAddress.getAllByName(s));
      } catch (IOException e) {
        FIR.addCustomizeValue("DNS", "err:" + dnsRequest.toString());
        FIR.sendCrashManually(e);
        return Dns.SYSTEM.lookup(hostname);
      }
    }
  };
  static private HttpLoggingInterceptor loggingInterceptor;
  static private Scheduler scheduler;

  private SquareUtils() {
    throw new AssertionError("Utils can't be an instance!");
  }

  public static synchronized Dispatcher getDispatcher() {
    if (dispatcher == null) {
      dispatcher = new Dispatcher();
    }
    return dispatcher;
  }

  public static synchronized Scheduler getRxWorkerScheduler() {
    if (scheduler == null) {
      scheduler = Schedulers.from(getDispatcher().executorService());
    }
    return scheduler;
  }

  /**
   * OkHttp client for httpDNS, shared Executor
   */
  static public synchronized OkHttpClient getHTTPDnsClient() {
    if (httpDnsclient == null) {
      final File cacheDir = GlobalContext.getInstance().getExternalCacheDir();
      httpDnsclient = new OkHttpClient.Builder().dispatcher(getDispatcher())
          //.addInterceptor(getLogger())
          .addNetworkInterceptor(new Interceptor() {
            //REWRITE_CACHE_CONTROL_INTERCEPTOR
            @Override public Response intercept(Chain chain) throws IOException {
              Response originalResponse = chain.proceed(chain.request());
              return originalResponse.newBuilder()
                  //dns default cache time
                  .header("Cache-Control", "max-age=600").build();
            }
          }).cache(new Cache(new File(cacheDir, "httpdns"), 5 * 1024 * 1024)).build();
    }
    return httpDnsclient;
  }

  static public synchronized OkHttpClient getClient() {
    if (client == null) {
      final File cacheDir = GlobalContext.getInstance().getExternalCacheDir();
      client = new OkHttpClient.Builder()
          //Interceptor -> cache -> NetworkInterceptor
          .addNetworkInterceptor(getLogger())
          .cache(new Cache(new File(cacheDir, "okhttp"), 60 * 1024 * 1024))
          .dispatcher(getDispatcher())
          .dns(HTTP_DNS)
          .build();
    }
    return client;
  }

  private static synchronized Interceptor getLogger() {
    if (loggingInterceptor == null) {
      loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override public void log(String message) {
          Log.d("okhttp", message);
        }
      }).setLevel(HttpLoggingInterceptor.Level.HEADERS);
    }
    return loggingInterceptor;
  }

  /**
   * Not singleton
   */
  private static OkHttpClient getProgressBarClient(final ProgressListener listener) {
    return getClient().newBuilder().addNetworkInterceptor(new Interceptor() {
      @Override public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
            .body(new ProgressResponseBody(originalResponse.body(), listener))
            .build();
      }
    }).build();
  }

  /**
   * Singleton Picasso shared cache with OkH ttp/Retrofit
   */
  static public Picasso getPicasso(Context context) {

    if (picasso == null) {
      synchronized (SquareUtils.class) {
        picasso =
            new Picasso.Builder(context).downloader(new OkHttp3Downloader(getClient())).build();
      }
    }
    return picasso;
  }

  /**
   * Download Big Image only, Not singleton but shared cache
   */
  static public Picasso getPicasso(Context context, ProgressListener listener) {
    OkHttpClient client = getProgressBarClient(listener);
    OkHttp3Downloader downloader = new OkHttp3Downloader(client);
    return new Picasso.Builder(context).downloader(downloader)
        .memoryCache(com.squareup.picasso.Cache.NONE)
        .build();
  }

  /**
   * There is no need to let retrofit singleton
   * BuilderTime + DynamicProxyTime == 0.6ms
   */
  public static Retrofit getRetrofit(String url) {
    return new Retrofit.Builder().baseUrl(url)
        .client(getClient())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();
  }

  public interface ProgressListener {
    @WorkerThread void update(@IntRange(from = 0, to = 100) int percent);
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
          if (progressListener != null) {
            progressListener.update(
                ((int) ((100 * totalBytesRead) / responseBody.contentLength())));
          }
          return bytesRead;
        }
      };
    }
  }
}
