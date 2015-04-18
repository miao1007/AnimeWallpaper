package com.github.miao1007.myapplication.ui.frag;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.github.miao1007.myapplication.R;
import com.github.miao1007.myapplication.service.Query;
import com.github.miao1007.myapplication.service.konachan.ImageResult;
import com.github.miao1007.myapplication.service.konachan.KService;
import com.github.miao1007.myapplication.utils.RetrofitUtils;
import com.github.miao1007.myapplication.utils.animation.AnimateUtils;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.List;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

public class ImageDetailedCard extends Fragment implements retrofit.Callback<List<ImageResult>> {

  @InjectView(R.id.container) LinearLayout mLinearLayout;

  @InjectView(R.id.imageView_detail) ImageView mImageview;

  public static final String EXTRA_IMAGE = "URL";
  private int id;
  private RestAdapter adapter;

  //use Picasso target to get bitmap
  int defalutColor;

  public ImageDetailedCard() {
  }

  public static ImageDetailedCard newInstance(int imageID) {
    ImageDetailedCard card = new ImageDetailedCard();
    Bundle bundle = new Bundle();
    bundle.putInt(EXTRA_IMAGE, imageID);
    card.setArguments(bundle);
    return card;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    defalutColor = getResources().getColor(R.color.accent_material_light);
    id = getArguments().getInt(EXTRA_IMAGE);
    //https://konachan.com/post.json/?limit=10&tags=rating:s%20id:199240&page=1

    Cache cache = null;
    OkHttpClient okHttpClient = null;

    try {
      File cacheDir = new File(getActivity().getCacheDir().getPath(), "pictures.json");
      cache = new Cache(cacheDir, 10 * 1024 * 1024);
      okHttpClient = new OkHttpClient();
      okHttpClient.setCache(cache);
    } catch (Exception e) {
      e.printStackTrace();
    }

    adapter = new RestAdapter.Builder().setEndpoint(KService.END_PONIT_KONACHAN)
        .setLogLevel(RestAdapter.LogLevel.FULL)
        .setClient(new OkClient(okHttpClient))
        .setRequestInterceptor(new RequestInterceptor() {
          @Override public void intercept(RequestFacade request) {
            request.addHeader("Cache-Control", "public, max-age=" + 60 * 60 * 4);
          }
        })
        .build();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_image_detailed_card, container, false);
    ButterKnife.inject(this, view);

    return view;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    Query query = new Query(4);
    query.setTAGS("id:" + id + " rating:s");
    adapter.create(KService.class).getImageList(query, this);
  }

  @Override public void success(List<ImageResult> imageResults, Response response) {
    if (imageResults.isEmpty()) {
      RetrofitUtils.disMsg(getActivity(), "H PIC! CLEAN");
      return;
    }
    Picasso.with(getActivity())
        .load(imageResults.get(0).getPreviewUrl())
        .placeholder(R.drawable.gradient_bg)
        .into(mImageview, new Callback() {
          @Override public void onSuccess() {

            Palette
                palette = Palette.generate(((BitmapDrawable) mImageview.getDrawable()).getBitmap());
            //setToolbarColor(mToolbar, palette.getMutedColor(defalutColor));
            AnimateUtils.animateViewColor(mLinearLayout, palette.getMutedColor(defalutColor));
          }

          @Override public void onError() {

          }
        });
  }

  @Override public void failure(RetrofitError error) {
    RetrofitUtils.disErr(getActivity(), error);
  }

  @Override public void onDetach() {
    super.onDetach();
    ButterKnife.reset(this);
  }
}
