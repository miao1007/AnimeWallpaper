package com.github.miao1007.animewallpaper.ui.adapter;

import android.support.annotation.IntRange;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.github.miao1007.animewallpaper.utils.LogUtils;
import java.util.List;

/**
 * Created by leon on 1/14/16.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter {

  static final String TAG = LogUtils.makeLogTag(BaseViewHolder.class);
  public static OnItemClickListener onItemClickListener;
  public OnLoadMoreListener loadMoreListener;
  int page = 1;
  List<T> data;

  public BaseAdapter(List<T> data) {
    this.data = data;
  }

  public List<T> getData() {
    return data;
  }

  public void setData(List<T> data) {
    this.data = data;
  }

  public void setItemClickListener(OnItemClickListener onItemClickListener) {
    BaseAdapter.onItemClickListener = onItemClickListener;
  }


  public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
    this.loadMoreListener = loadMoreListener;
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return null;
  }

  @Override final public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    //check for last item
    if ((position >= getItemCount() - 1)) {
      page++;
      Log.d(TAG, "loadMore:" + page);
      loadMoreListener.loadMore(page);
    }
    onBindItemViewHolder(((BaseViewHolder) holder), position);
  }

  abstract void onBindItemViewHolder(BaseViewHolder holder, int position);

  @Override public int getItemCount() {
    return data.size();
  }

  public interface OnItemClickListener {
    void onItemClick(View v, @IntRange(from = 0) int position);
  }

  public interface OnLoadMoreListener {
    void loadMore(int page);
  }

  //holder has a strong reference to host

  /**
   * see {@link <a href="https://youtu.be/imsr8NrIAMs?t=2163"></a>}
   */
  public static class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
      super(itemView);
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
