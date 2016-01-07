package com.github.miao1007.myapplication.support.service.konachan;

import java.util.List;
import java.util.Map;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by leon on 4/14/15.
 */
public interface DoubanRepo {

  String MUSIC = "https://konachan.com/";
  String END_PONIT_YANDE = "https://yande.re/post.json/";
  String TAGS = "tags";
  String LIMIT = "limit";
  String PAGE = "page";
  String RATING = "rating";

  //@Header("")
  @GET("post.json") Observable<List<ImageResult>> getImageList(@QueryMap Map<String, Object> query);
}
