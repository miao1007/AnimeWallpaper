package com.github.miao1007.myapplication.utils;

import android.content.Context;
import android.widget.Toast;
import com.github.miao1007.myapplication.support.GlobalContext;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import java.io.File;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by leon on 14/11/2.
 */
public class RetrofitUtils {
  public static void disErr(Context context, RetrofitError e) {
    switch (e.getKind()) {
      case NETWORK:
        Toast.makeText(context, "NETWORK ERR", Toast.LENGTH_SHORT).show();
        break;
      case CONVERSION:
        Toast.makeText(context, "CONVERSION ERR", Toast.LENGTH_SHORT).show();
        break;
      case HTTP:
        Toast.makeText(context, "SERVER ERR" + " : " +
            e.getResponse().getReason(), Toast.LENGTH_SHORT).show();
        break;
      case UNEXPECTED:
        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        //TODO:using some bug-feedback plugins
        break;
    }
  }

  public static void disSucc(Context context, Response response) {
    if (context != null) {
      Toast.makeText(context, response.getReason(), Toast.LENGTH_SHORT).show();
    }
  }

  public static void disMsg(Context context, String msg) {
    if (context != null) {
      Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
  }

  public static RestAdapter getCachedAdapter(String endpoint) {
    Cache cache = null;
    OkHttpClient okHttpClient = null;
    RestAdapter adapter;
    try {
      File cacheDir =
          new File(GlobalContext.getInstance().getCacheDir().getPath(), "pictures.json");
      cache = new Cache(cacheDir, 10 * 1024 * 1024);
      okHttpClient = new OkHttpClient();
      okHttpClient.setCache(cache);
    } catch (Exception e) {
      e.printStackTrace();
    }

    adapter = new RestAdapter.Builder().setEndpoint(endpoint)
        .setLogLevel(RestAdapter.LogLevel.FULL)
        .setClient(new OkClient(okHttpClient))
        .setRequestInterceptor(new RequestInterceptor() {
          @Override public void intercept(RequestFacade request) {
            //request.addHeader("Cache-Control", "public, max-age=" + 60 * 60 * 4);
          }
        })
        .build();
    return adapter;
  }
}
