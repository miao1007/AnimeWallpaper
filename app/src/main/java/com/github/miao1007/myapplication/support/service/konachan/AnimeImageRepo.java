package com.github.miao1007.myapplication.support.service.konachan;

import java.util.List;
import java.util.Map;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.QueryMap;

/**
 * Created by leon on 4/14/15.
 */
public interface AnimeImageRepo {


  String END_PONIT_KONACHAN = "http://konachan.net/post.json";
  String END_PONIT_YANDE = "https://yande.re/post.json";
  String TAGS = "tags";
  String LIMIT = "limit";
  String PAGE = "page";
  String RATING = "rating";

  //@Header("")
  @GET("/") void getImageList(@QueryMap Map<String, Object> option,
      Callback<List<ImageResult>> callback);


}
