package com.github.miao1007.myapplication.ui.frag;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.github.miao1007.myapplication.R;
import com.github.miao1007.myapplication.support.service.Query;
import com.github.miao1007.myapplication.support.service.konachan.AnimeImageRepo;
import com.github.miao1007.myapplication.support.service.konachan.ImageResult;
import com.github.miao1007.myapplication.ui.activity.DetailedActivity;
import com.github.miao1007.myapplication.ui.adapter.CardAdapter;
import com.github.miao1007.myapplication.utils.LogUtils;
import com.github.miao1007.myapplication.utils.RetrofitUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/*
* Feature:
* 1. RecyclerView LayoutAnimation Animation
* 2. Endless RecyclerView
* 3. CardView Supported
* */
public class CardFragment extends Fragment
    implements SwipeRefreshLayout.OnRefreshListener, Callback<List<ImageResult>>,
    CardAdapter.OnItemClickListener, CardAdapter.OnLoadMoreListener {

  @InjectView(R.id.rv_frag_card) RecyclerView mRecyclerView;
  //@InjectView(R.id.btn_retry_card) Button mButton;
  //@InjectView(R.id.pgb_loading_card) ContentLoadingProgressBar mProgressBar;

  private CardAdapter mAdapter;
  private RecyclerView.LayoutManager mLayoutManager;
  private boolean isLoadingMore = false;
  private int currentPage = 1;
  private Query query = new Query();
  private Toolbar mToolbar;
  private Spinner mSpinner;


  public static final String TAG = LogUtils.makeLogTag(CardFragment.class);

  private List<ImageResult> imageResults = new ArrayList<ImageResult>();

  public CardFragment() {
    // Required empty public constructor
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //getActivity().getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_card, container, false);
    ButterKnife.inject(this, view);
    setUpList();
    return view;
  }

  private void setUpList() {
    mAdapter = new CardAdapter(imageResults, mRecyclerView);
    mAdapter.setOnLoadMoreListener(this);
    mAdapter.setOnItemClickListener(this);
    mLayoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setAdapter(mAdapter);
    query.init();
    loadPage(query);
  }


  @Override
  public void onDestroyView() {
    super.onDestroyView();
    Log.d(TAG,"onDestroyView");
    if (mToolbar != null && mSpinner != null) {
      mToolbar.removeView(mSpinner);
    }
  }

  @Override public void onDetach() {
    super.onDetach();
    ButterKnife.reset(this);
  }


  @Override public void onLoadMore(int pos) {
    Log.d(TAG, "onLoadMore " + pos);
    loadPage(query);
  }

  void loadPage(HashMap<String, Object> query) {
    RetrofitUtils.getCachedAdapter(AnimeImageRepo.END_PONIT_KONACHAN)
        .create(AnimeImageRepo.class)
        .getImageList(query, this);
  }

  @Override public void onItemClick(View v, int position) {
    Log.d(TAG, "onItemClick" + position);
    Intent intent = new Intent(v.getContext(), DetailedActivity.class);
    intent.putExtra(DetailedActivity.EXTRA_IMAGE, imageResults.get(position));
    v.getContext().startActivity(intent);
  }

  //retrofit callback
  @Override public void success(List<ImageResult> newimageResults, Response response) {

    int currentPage = ((int) query.get(AnimeImageRepo.PAGE));
    query.put(AnimeImageRepo.PAGE, currentPage + 1);
    imageResults.addAll(newimageResults);
    mAdapter.notifyDataSetChanged();
  }

  //retrofit failure callback
  @Override public void failure(RetrofitError error) {
    RetrofitUtils.disErr(CardFragment.this.getActivity(), error);
  }

  //swipe layout refresh callback
  @Override public void onRefresh() {
    query.init();
    loadPage(query);
  }

}


