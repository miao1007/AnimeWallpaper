package com.github.miao1007.myapplication.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.github.miao1007.myapplication.R;
import com.github.miao1007.myapplication.support.service.konachan.ImageResult;
import com.github.miao1007.myapplication.utils.animation.AnimateUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.List;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by leon on 6/30/15.
 */
public class CardAdapter extends RecyclerView.Adapter {
  static public OnItemClickListener onItemClickListener;
  private final int VIEW_ITEM = 1;
  private final int VIEW_PROG = 0;
  private List<ImageResult> mDataset;
  // The minimum amount of items to have below your current scroll position before loading more.
  private int visibleThreshold = 2;
  private int lastVisibleItem, totalItemCount;
  private boolean loading;
  private OnLoadMoreListener onLoadMoreListener;

  public CardAdapter(List<ImageResult> myDataSet, RecyclerView recyclerView) {
    mDataset = myDataSet;

    if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

      final LinearLayoutManager linearLayoutManager =
          (LinearLayoutManager) recyclerView.getLayoutManager();
      recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);
          totalItemCount = linearLayoutManager.getItemCount();
          lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
          if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
            // End has been reached
            // Do something
            if (onLoadMoreListener != null) {
              onLoadMoreListener.onLoadMore(totalItemCount);
            }
            loading = true;
          }
        }
      });
    }
  }

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  @Override public int getItemViewType(int position) {
    return mDataset.get(position) != null ? VIEW_ITEM : VIEW_PROG;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    RecyclerView.ViewHolder vh;
    if (viewType == VIEW_ITEM) {
      return onCreateItemViewHolder(parent, viewType);
    } else {
      View v = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.include_progressbar, parent, false);

      vh = new ProgressViewHolder(v);
      return vh;
    }
  }

  public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
    RecyclerView.ViewHolder vh;
    View v =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sample, parent, false);
    vh = new MyViewHolder(v);
    return vh;
  }

  public void onBindItemViewHolder(final MyViewHolder holder, int position) {
    final Context context = holder.itemView.getContext();
    ImageResult result = mDataset.get(position);
    Picasso.with(context)
        .load(mDataset.get(position).getPreviewUrl())
        .placeholder(R.drawable.lorempixel)
        //.transform(new BlurTransformation(getContext()))
        .into(holder.imageView, new Callback.EmptyCallback() {
          @Override public void onSuccess() {
            Observable.just(holder.imageView)
                .map(new Func1<ImageView, Bitmap>() {
                  @Override public Bitmap call(ImageView imageView) {
                    return ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                  }
                })
                .map(new Func1<Bitmap, Integer>() {
                  @Override public Integer call(Bitmap bitmap) {
                    return Palette.from(bitmap).generate().getMutedColor(Color.WHITE);
                  }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                  @Override public void call(Integer toColor) {
                    //set background color
                    AnimateUtils.animateViewColor(holder.bg, toColor);
                  }
                });
          }
        });
    holder.textView_author.setText(result.getAuthor());
    holder.textView_tag.setText(result.getTags());
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof MyViewHolder) {
      onBindItemViewHolder((MyViewHolder) holder, position);
    } else {
      ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
    }
  }

  public void setLoaded() {
    loading = false;
  }

  @Override public int getItemCount() {
    return mDataset.size();
  }

  public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
    this.onLoadMoreListener = onLoadMoreListener;
  }

  public interface OnItemClickListener {
    void onItemClick(View v, int position);
  }

  public interface OnLoadMoreListener {
    void onLoadMore(int pos);
  }

  public static class MyViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.iv_card_preview) ImageView imageView;
    @InjectView(R.id.tv_card_author) TextView textView_author;
    @InjectView(R.id.tv_card_tags) TextView textView_tag;
    @InjectView(R.id.item_bg) LinearLayout bg;

    public MyViewHolder(View itemView) {
      super(itemView);
      ButterKnife.inject(this, itemView);
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          //Intent intent = new Intent(v.getContext(), DetailedActivity.class);
          //intent.putExtra(DetailedActivity.EXTRA_IMAGE, imageResults.get(getPosition()));
          //v.getContext().startActivity(intent);
          if (onItemClickListener != null) {
            onItemClickListener.onItemClick(v, getPosition());
          }
        }
      });
    }
  }

  public static class ProgressViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;

    public ProgressViewHolder(View v) {
      super(v);
      progressBar = (ProgressBar) v.findViewById(R.id.include_progressbar);
    }
  }
}