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
import com.github.miao1007.animewallpaper.support.api.ImageAdapter;
import com.github.miao1007.animewallpaper.utils.SquareUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leon on 6/30/15.
 */
public class CardAdapter extends BaseAdapter<ImageAdapter> {

  public CardAdapter() {
    super(new ArrayList<ImageAdapter>());
  }

  public CardAdapter(List<ImageAdapter> mDataset) {
    super(mDataset);
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return onCreateItemViewHolder(parent, viewType);
  }

  private RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
    RecyclerView.ViewHolder vh;
    View v =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sample, parent, false);
    vh = new MyViewHolder(v);
    return vh;
  }

  @Override void onBindItemViewHolder(RecyclerView.ViewHolder baseViewHolder, int position) {
    final MyViewHolder holder = (MyViewHolder) baseViewHolder;
    final Context context = holder.itemView.getContext();
    SquareUtils.getPicasso(context).load(data.get(position).getPrev_url())
        .placeholder(R.drawable.place_holder)
        .into(holder.imageView);
  }

  /**
   * see  <a href="https://youtu.be/imsr8NrIAMs?t=2163">Official Video</a>
   */
  public static class MyViewHolder extends RecyclerView.ViewHolder {

    public @Bind(R.id.iv_card_preview) ImageView imageView;

    public MyViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View itemView) {
          if (getAdapterPosition() != RecyclerView.NO_POSITION && onItemClickListener != null) {
            onItemClickListener.onItemClick(itemView, getAdapterPosition());
          }
        }
      });
    }
}
}