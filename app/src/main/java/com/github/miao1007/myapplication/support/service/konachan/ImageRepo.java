package com.github.miao1007.myapplication.support.service.konachan;

import java.util.List;
import java.util.Map;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by leon on 4/14/15.
 */
public interface ImageRepo {

  //qiniu CDN
  String END_POINT_CDN = "http://7xq3s7.com1.z0.glb.clouddn.com";
  String END_POINT = "http://www.konachan.net";
  String END_PONIT_YANDE = "https://yande.re/post.json/";
  String TAGS = "tags";
  String LIMIT = "limit";
  String PAGE = "page";
  String RATING = "rating";

  String TAG_SAFE = " rating:s";

  //@Header("")
  @GET("post.json") Observable<List<ImageResult>> getImageList(@QueryMap Map<String, Object> query);
}
