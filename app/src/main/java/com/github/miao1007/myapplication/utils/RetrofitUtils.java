package com.github.miao1007.myapplication.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.github.miao1007.myapplication.support.service.konachan.ImageRepo;
import com.github.miao1007.myapplication.utils.network.HttpLoggingInterceptor;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

/**
 * Created by leon on 14/11/2.
 */
public class RetrofitUtils {

  public static void disMsg(Context context, String msg) {
    if (context != null) {
      Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
  }

  public static Retrofit getCachedAdapter() {
    Cache cache = null;
    OkHttpClient okHttpClient = null;
    Retrofit retrofit;

    HttpLoggingInterceptor interceptor =
        new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
          @Override public void log(String message) {
            Log.d("retrofit", message);
          }
        });
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    okHttpClient = new OkHttpClient.Builder()
        .addNetworkInterceptor(interceptor)
        .build();
    retrofit = new Retrofit.Builder().baseUrl(ImageRepo.END_POINT)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();
    return retrofit;
  }
}
