package com.github.miao1007.myapplication.ui.frag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.github.miao1007.myapplication.R;
import com.github.miao1007.myapplication.support.service.konachan.ImageRepo;
import com.github.miao1007.myapplication.support.service.konachan.ImageResult;
import com.github.miao1007.myapplication.ui.activity.DetailedActivity;
import com.github.miao1007.myapplication.ui.adapter.BaseAdapter;
import com.github.miao1007.myapplication.ui.adapter.CardAdapter;
import com.github.miao1007.myapplication.utils.LogUtils;
import com.github.miao1007.myapplication.utils.RetrofitUtils;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class CardFragment extends Fragment
    implements SwipeRefreshLayout.OnRefreshListener, CardAdapter.OnItemClickListener,
    BaseAdapter.OnLoadMoreListener {

  public static final String TAG = LogUtils.makeLogTag(CardFragment.class);
  //@InjectView(R.id.btn_retry_card) Button mButton;
  //@InjectView(R.id.pgb_loading_card) ContentLoadingProgressBar mProgressBar;
  @InjectView(R.id.rv_frag_card) RecyclerView mRecyclerView;
  @InjectView(R.id.swipe) SwipeRefreshLayout mSwipe;
  boolean isLoadingMore;
  private Map<String, Object> query = new HashMap<>(4);

  public CardFragment() {
    // Required empty public constructor
  }

  @Override public void onDetach() {
    super.onDetach();
    query.clear();
    ButterKnife.reset(this);
  }

  //todo load config from sp
  @Override public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_card, container, false);
    ButterKnife.inject(this, view);
    setUpList();
    return view;
  }

  private void setUpList() {
    mSwipe.setOnRefreshListener(this);
    CardAdapter mAdapter = new CardAdapter();
    mAdapter.setLoadMoreListener(this);
    mAdapter.setItemClickListener(this);
    final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
      }
    });
    onRefresh();
  }

  void loadPage(Map<String, Object> query) {
    if (isLoadingMore) {
      return;
    }
    if (query == null) {
      query = new HashMap<>();
    }
    if ((query.isEmpty()) || (!query.containsKey(ImageRepo.TAGS))) {
      query.put(ImageRepo.TAGS, ImageRepo.TAG_SAFE);
      query.put(ImageRepo.LIMIT, 10);
    }
    RetrofitUtils.getCachedAdapter().create(ImageRepo.class)
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
            if (mRecyclerView != null) {
              mRecyclerView.getAdapter().notifyDataSetChanged();
            }
            if (mSwipe != null) {
              mSwipe.setRefreshing(false);
            }
            isLoadingMore = false;
          }

          @Override public void onError(Throwable e) {
            e.printStackTrace();
            if (mSwipe != null) {
              mSwipe.setRefreshing(false);
            }
            isLoadingMore = false;
          }

          @Override public void onNext(ImageResult imageResult) {
            ((CardAdapter) mRecyclerView.getAdapter()).getData().add(imageResult);
          }
        });
  }

  @Override public void onItemClick(View v, int position) {
    Intent intent = new Intent(v.getContext(), DetailedActivity.class);
    intent.putExtra(DetailedActivity.EXTRA_IMAGE,
        ((CardAdapter) mRecyclerView.getAdapter()).getData().get(position));
    v.getContext().startActivity(intent);
  }

  //swipe layout refresh callback
  @Override public void onRefresh() {
    Log.d(TAG, "onRefresh:query = " + query);
    if (mSwipe != null && !mSwipe.isRefreshing()) {
      mSwipe.setRefreshing(true);
    }
    ((CardAdapter) mRecyclerView.getAdapter()).getData().clear();
    mRecyclerView.getAdapter().notifyDataSetChanged();
    loadMore(1);
  }

  @Override public void loadMore(int page) {
    Log.d(TAG, "loadMore:" + query);
    query.put(ImageRepo.PAGE, page);
    loadPage(query);//这里多线程也要手动控制isLoadingMore
  }
}


