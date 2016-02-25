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
 *
 * konachan
 *
 * yande.re
 *
 * Moebooru offers API which is mostly compatible with Danbooru API (version 1.13.0) to make
 * scripting easy. All you need is a way to GET and POST to URLs. The ability to parse XML or
 * JSON responses is nice, but not critical. The simplicity of the API means you can write
 * scripts using JavaScript, Perl, Python, Ruby, even shell languages like bash or tcsh.
 *
 * @link <a href="http://konachan.net/help/api">api help</a>
 */
public interface DanbooruAPI {

  //qiniu CDN
  String KONACHAN = "http://konachan.net";
  String YANDE = "https://yande.re";

  String TAGS = "tags";
  String LIMIT = "limit";
  String PAGE = "page";
  String RATING = "rating";

  String TAG_SAFE = " rating:s";

  Interceptor CDN = new Interceptor() {
    @Override public Response intercept(Chain chain) throws IOException {
      Request originRequest = chain.request();

      HttpUrl url = originRequest.url().newBuilder().host("7xq3s7.com1.z0.glb.clouddn.com").build();

      Request newReq = originRequest.newBuilder().url(url).build();
      return chain.proceed(newReq);
    }
  };

  //@Header("")
  @GET("post.json") Observable<List<ImageResult>> getImageList(@QueryMap Map<String, Object> query);

  //http://konachan.net/tag.json?limit=10&name=suzu*
  @GET("tag.json") Observable<List<Tag>> getTags(@Query("limit") int limit,
      @Query("name") String name);
}
