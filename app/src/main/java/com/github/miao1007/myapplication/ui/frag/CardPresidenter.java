package com.github.miao1007.myapplication.ui.frag;

import android.view.View;
import com.github.miao1007.myapplication.support.GlobalContext;
import com.github.miao1007.myapplication.support.eventbus.MainThreadBus;
import com.github.miao1007.myapplication.support.service.konachan.ImageResult;
import com.github.miao1007.myapplication.support.service.konachan.AnimeImageRepo;
import com.github.miao1007.myapplication.utils.RetrofitUtils;
import com.squareup.otto.Subscribe;
import java.util.HashMap;
import java.util.List;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by leon on 15/6/12.
 */
public final class CardPresidenter {

  private final MainThreadBus bus;
  private final AnimeImageRepo service;

  public CardPresidenter() {
    this.bus = GlobalContext.BUS;
    service = RetrofitUtils.getCachedAdapter(
        AnimeImageRepo.END_PONIT_KONACHAN).create(AnimeImageRepo.class);
  }

  private Callback<List<ImageResult>> callback = new Callback<List<ImageResult>>() {
    @Override public void success(List<ImageResult> imageResults, Response response) {
      GlobalContext.BUS.post(imageResults);
    }

    @Override public void failure(RetrofitError error) {
      GlobalContext.BUS.post(error);
    }
  };

  void onItemClick(View v, int position){

  }

  void register() {
    GlobalContext.BUS.register(this);
  }

  void unRegister() {
    GlobalContext.BUS.unregister(this);
  }

  void onRefresh(HashMap<String, Object> query) {
    service.getImageList(query, callback);
  }

  void onLoadMore(HashMap<String, Object> query) {
    RetrofitUtils.getCachedAdapter(AnimeImageRepo.END_PONIT_KONACHAN)
        .create(AnimeImageRepo.class)
        .getImageList(query, callback);
  }

  @Subscribe public void onRetrofitCallback(List<ImageResult> callback) {

  }
}
