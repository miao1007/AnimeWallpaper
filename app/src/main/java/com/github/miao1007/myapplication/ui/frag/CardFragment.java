package com.github.miao1007.myapplication.ui.frag;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.github.miao1007.myapplication.R;
import com.github.miao1007.myapplication.service.Query;
import com.github.miao1007.myapplication.service.konachan.ImageResult;
import com.github.miao1007.myapplication.service.konachan.KService;
import com.github.miao1007.myapplication.ui.adapter.SampleAdapter;
import com.github.miao1007.myapplication.utils.RetrofitUtils;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/*
* Feature:
* 1. RecyclerView LayoutAnimation Animation
* 2. Endless RecyclerView
* 3. CardView Supported
* */
public class CardFragment extends Fragment
    implements SwipeRefreshLayout.OnRefreshListener, Callback<List<ImageResult>> {

  @InjectView(R.id.list) SuperRecyclerView mRecyclerView;

  private SampleAdapter mAdapter;
  private RecyclerView.LayoutManager mLayoutManager;
  private boolean isLoadingMore = false;
  private int currentPage = 1;
  private HashMap<String, Object> query = new HashMap<>(4);
  ;

  public static final String TAG = "CardFragment";

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
    mAdapter = new SampleAdapter(imageResults);

    mLayoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(mLayoutManager);

    mRecyclerView.setAdapter(mAdapter);

    mRecyclerView.setRefreshListener(this);
    mRecyclerView.setupMoreListener(new OnMoreListener() {
      @Override
      public void onMoreAsked(int numberOfItems, int numberBeforeMore, int currentItemPos) {
        // Fetch more from Api or DB
        loadPage(query);
      }
    }, 10);
    query = new Query().build();
    loadPage(query);
    return view;
  }

  void loadPage(HashMap<String, Object> query) {

    RestAdapter adapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL)
        .setEndpoint(KService.END_PONIT_KONACHAN)
        .build();

    adapter.create(KService.class).getImageList(query, this);
  }

  //retrofit callback
  @Override public void success(List<ImageResult> newimageResults, Response response) {

    int currentPage = ((int) query.get(KService.PAGE));
    query.put(KService.PAGE, currentPage + 1);

    imageResults.addAll(newimageResults);
    mAdapter.notifyDataSetChanged();
  }

  //retrofit callback
  @Override public void failure(RetrofitError error) {
    RetrofitUtils.disErr(CardFragment.this.getActivity(), error);
  }

  //swipe layout
  @Override public void onRefresh() {
    query = new Query().build();
    loadPage(query);
  }

  @Override public void onDetach() {
    super.onDetach();
    ButterKnife.reset(this);
  }
}


