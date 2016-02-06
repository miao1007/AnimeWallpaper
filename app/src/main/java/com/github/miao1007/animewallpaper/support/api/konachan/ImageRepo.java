package com.github.miao1007.animewallpaper.support.api.konachan;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by leon on 4/14/15.
 */
public interface ImageRepo {

  //qiniu CDN
  String CDN_HOST = "7xq3s7.com1.z0.glb.clouddn.com";
  String END_POINT_KONACHAN = "http://www.konachan.net";
  String END_POINT = CDN_HOST;
  String END_PONIT_YANDE = "https://yande.re/post.json/";
  String TAGS = "tags";
  String LIMIT = "limit";
  String PAGE = "page";
  String RATING = "rating";

  String TAG_SAFE = " rating:s";

  Interceptor CDN = new Interceptor() {
    @Override public Response intercept(Chain chain) throws IOException {
      Request originRequest = chain.request();

      HttpUrl url = originRequest.url().newBuilder().host(CDN_HOST).build();

      Request newReq = originRequest.newBuilder().url(url).build();
      return chain.proceed(newReq);
    }
  };

  //@Header("")
  @GET("post.json") Observable<List<ImageResult>> getImageList(@QueryMap Map<String, Object> query);

  //http://konachan.net/tag.json?limit=10&name=suzu*
  @GET("tag.json") Observable<List<Tag>> searchHint(@Query("limit") int limit,
      @Query("name") String name);
}
