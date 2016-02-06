package com.github.miao1007.animewallpaper.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.github.miao1007.animewallpaper.R;
import com.github.miao1007.animewallpaper.support.api.konachan.ImageResult;
import com.github.miao1007.animewallpaper.utils.picasso.SquareUtils;
import java.util.ArrayList;
import java.util.List;

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
    View v =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sample, parent, false);
    vh = new MyViewHolder(v);
    return vh;
  }

  @Override void onBindItemViewHolder(BaseViewHolder baseViewHolder, int position) {
    final MyViewHolder holder = (MyViewHolder) baseViewHolder;
    final Context context = holder.itemView.getContext();
    ImageResult result = getData().get(position);
    SquareUtils.getPicasso(context).load(result.getPreviewUrl())
        .placeholder(R.drawable.place_holder)
        //.transform(new BlurTransformation(getContext()))
        .into(holder.imageView);
  }

  public static class MyViewHolder extends BaseViewHolder {

    public @Bind(R.id.iv_card_preview) ImageView imageView;

    public MyViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}