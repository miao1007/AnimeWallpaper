package com.github.miao1007.myapplication.utils;

import android.content.Context;
import android.widget.Toast;
import retrofit.RetrofitError;
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
        //TODO:写入日志,发送给作者
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
}
