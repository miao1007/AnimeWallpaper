package com.github.miao1007.myapplication.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.github.miao1007.myapplication.R;
import com.github.miao1007.myapplication.support.service.konachan.ImageRepo;
import com.github.miao1007.myapplication.support.service.konachan.ImageResult;
import com.github.miao1007.myapplication.ui.adapter.BaseAdapter;
import com.github.miao1007.myapplication.ui.adapter.CardAdapter;
import com.github.miao1007.myapplication.ui.widget.BlurDrawable;
import com.github.miao1007.myapplication.ui.widget.NavigationBar;
import com.github.miao1007.myapplication.ui.widget.Position;
import com.github.miao1007.myapplication.utils.LogUtils;
import com.github.miao1007.myapplication.utils.RetrofitUtils;
import com.github.miao1007.myapplication.utils.StatusbarUtils;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
    implements CardAdapter.OnItemClickListener, BaseAdapter.OnLoadMoreListener {

  public static final String TAG = LogUtils.makeLogTag(MainActivity.class);
  static final String EXTRA_MAP = "ext";
  //@Bind(R.id.btn_retry_card) Button mButton;
  //@Bind(R.id.pgb_loading_card) ContentLoadingProgressBar mProgressBar;
  boolean isLoadingMore;
  @Bind(R.id.navigation_bar) NavigationBar mNavigationBar;
  @Bind(R.id.rv_frag_card) RecyclerView mRvFragCard;
  @Bind(R.id.iv_card_search) ImageView mIvCardSearch;
  private Map<String, Object> query = new HashMap<>(4);

  static void startActivity(Context context, String query) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    intent.putExtra(EXTRA_MAP, query);
    context.startActivity(intent);
  }

  static String parseIntent(Intent intent) {
    return intent.getStringExtra(EXTRA_MAP);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    StatusbarUtils.from(this).setTransparentStatusbar(true).setLightStatusBar(true).process();
    setContentView(R.layout.fragment_card);
    ButterKnife.bind(this);
    setUpList();
    String tag = parseIntent(getIntent());
    if (tag != null) {
      query.clear();
      query.put(ImageRepo.TAGS, tag + ImageRepo.TAG_SAFE);
    }
    onRefresh();
  }

  private void setUpList() {
    //mSwipe.setOnRefreshListener(this);
    CardAdapter mAdapter = new CardAdapter();
    mAdapter.setLoadMoreListener(this);
    mAdapter.setItemClickListener(this);
    final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    mRvFragCard.setLayoutManager(mLayoutManager);
    mRvFragCard.setAdapter(mAdapter);
    mNavigationBar.setViewToBlur(mRvFragCard);
    mRvFragCard.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {

          Picasso.with(recyclerView.getContext()).resumeTag(TAG);
        } else {
          Picasso.with(recyclerView.getContext()).pauseTag(TAG);
        }
      }

      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        //if (Math.abs(dy))
      }
    });
    onRefresh();
  }

  void loadPage(Map<String, Object> query) {
    if (isLoadingMore) {
      return;
    }
    if (mNavigationBar != null) {
      mNavigationBar.setProgress(true);
    }
    if (query == null) {
      query = new HashMap<>();
    }
    if ((query.isEmpty()) || (!query.containsKey(ImageRepo.TAGS))) {
      query.put(ImageRepo.TAGS, ImageRepo.TAG_SAFE);
      query.put(ImageRepo.LIMIT, 10);
    }
    RetrofitUtils.getCachedAdapter()
        .create(ImageRepo.class)
        .getImageList(query)
        .subscribeOn(Schedulers.io())
        .flatMap(new Func1<List<ImageResult>, Observable<ImageResult>>() {
          @Override public Observable<ImageResult> call(List<ImageResult> imageResults) {
            return Observable.from(imageResults);
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<ImageResult>() {
          @Override public void onCompleted() {
            if (mRvFragCard != null && mRvFragCard.getAdapter() != null) {
              mRvFragCard.getAdapter().notifyDataSetChanged();
            }
            if (mNavigationBar != null) {
              mNavigationBar.setProgress(false);
            }
            isLoadingMore = false;
          }

          @Override public void onError(Throwable e) {
            e.printStackTrace();
            if (mNavigationBar != null) {
              mNavigationBar.setProgress(false);
            }
            isLoadingMore = false;
          }

          @Override public void onNext(ImageResult imageResult) {
            ((CardAdapter) mRvFragCard.getAdapter()).getData().add(imageResult);
          }
        });
  }

  @Override public void onItemClick(View v, int position) {
    Parcelable imgInfo = ((CardAdapter) mRvFragCard.getAdapter()).getData().get(position);

    DetailedActivity.startActivity(v.getContext(), Position.from(v), imgInfo);
  }

  //swipe layout refresh callback
  public void onRefresh() {
    Log.d(TAG, "onRefresh:query = " + query);
    ((CardAdapter) mRvFragCard.getAdapter()).getData().clear();
    mRvFragCard.getAdapter().notifyDataSetChanged();
    loadMore(1);
  }

  @Override public void loadMore(int page) {
    Log.d(TAG, "loadMore:" + query);
    query.put(ImageRepo.PAGE, page);
    loadPage(query);//这里多线程也要手动控制isLoadingMore
  }
}

