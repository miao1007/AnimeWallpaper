package com.github.miao1007.myapplication.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.github.miao1007.myapplication.R;
import com.github.miao1007.myapplication.support.service.konachan.ImageRepo;
import com.github.miao1007.myapplication.support.service.konachan.ImageResult;
import com.github.miao1007.myapplication.utils.animation.AnimateUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by leon on 6/30/15.
 */
public class CardAdapter extends BaseAdapter<ImageResult> {

  private final int VIEW_ITEM = 1;

  public CardAdapter() {
    super(new ArrayList<ImageResult>());
  }

  public CardAdapter(List<ImageResult> mDataset) {
    super(mDataset);
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return onCreateItemViewHolder(parent, viewType);
  }

  public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
    RecyclerView.ViewHolder vh;
    Log.d(TAG, "onCreateItemViewHolder");
    View v =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sample, parent, false);
    vh = new MyViewHolder(v);
    return vh;
  }

  @Override void onBindItemViewHolder(BaseViewHolder baseViewHolder, int position) {
    final MyViewHolder holder = (MyViewHolder) baseViewHolder;
    final Context context = holder.itemView.getContext();
    ImageResult result = getData().get(position);
    Picasso.with(context)
        .load(result.getPreviewUrl().replace(ImageRepo.END_POINT, ImageRepo.END_POINT_CDN))
        .placeholder(R.drawable.place_holder)
        //.transform(new BlurTransformation(getContext()))
        .into(holder.imageView);
  }

  public static class MyViewHolder extends BaseViewHolder {

    public @InjectView(R.id.iv_card_preview) ImageView imageView;

    public MyViewHolder(View itemView) {
      super(itemView);
      ButterKnife.inject(this, itemView);
    }
  }
}