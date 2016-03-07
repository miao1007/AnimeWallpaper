package com.github.miao1007.animewallpaper.ui.adapter;

import android.support.annotation.IntRange;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Created by leon on 1/14/16.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter {

  static OnItemClickListener onItemClickListener;
  private OnLoadMoreListener loadMoreListener;
  private int page = 1;
  List<T> data;

  BaseAdapter(List<T> data) {
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
      loadMoreListener.loadMore(page);
    }
    onBindItemViewHolder(holder, position);
  }

  abstract void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position);

  @Override public int getItemCount() {
    return data.size();
  }

  public interface OnItemClickListener {
    void onItemClick(View v, @IntRange(from = 0) int position);
  }

  public interface OnLoadMoreListener {
    void loadMore(int page);
  }

}
