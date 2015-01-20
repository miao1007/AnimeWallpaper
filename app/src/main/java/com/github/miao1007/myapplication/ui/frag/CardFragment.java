package com.github.miao1007.myapplication.ui.frag;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import com.github.miao1007.myapplication.R;
import com.github.miao1007.myapplication.ui.adapter.SampleAdapter;
import com.github.miao1007.myapplication.utils.animation.AnimationBox;
import java.util.ArrayList;
import java.util.List;

/*
* Feature:
* 1. RecyclerView LayoutAnimation Animation
* 2. Endless RecyclerView
* 3. CardView Supported
* */
public class CardFragment extends Fragment {

  private RecyclerView mRecyclerView;
  private SampleAdapter mAdapter;
  private RecyclerView.LayoutManager mLayoutManager;

  private List<String> stringList = new ArrayList<String>();

  public CardFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //getActivity().getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_card, container, false);
    mAdapter = new SampleAdapter(getActivity(), stringList);
    mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
    mLayoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(mLayoutManager);

    //RecyclerView LayoutAnimation Animation showcase
    LayoutAnimationController layoutAnimationController = AnimationBox.FadeInController();
    mRecyclerView.setLayoutAnimation(layoutAnimationController);
    mRecyclerView.setAnimationCacheEnabled(true);
    mRecyclerView.startLayoutAnimation();

    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.setHasFixedSize(true);
    //TODO : it seem to no use....
    mRecyclerView.setItemAnimator(new DefaultItemAnimator());

    mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
      //Endless RecyclerView showcase
      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        int totalItemCount = mLayoutManager.getItemCount();
        //lastVisibleItem >= totalItemCount - 4 ： 4 item left
        // dy>0 : you are pulling down
        if (lastVisibleItem >= totalItemCount - 4 && dy > 0) {
          new MockAsynctask().execute();
        }
      }
    });
    new MockAsynctask().execute();
    return view;
  }

  private class MockAsynctask extends AsyncTask<Void, Void, List<String>> {
    @Override
    protected List<String> doInBackground(Void... voids) {
      List<String> tmpurl = new ArrayList<String>();
      for (int i = 0; i < 10; i++) {
        String imageUrl = "http://lorempixel.com/400/200/sports/" + i;
        tmpurl.add(imageUrl);
      }
      stringList.addAll(tmpurl);
      return stringList;
    }

    @Override
    protected void onPostExecute(List<String> strings) {
      super.onPostExecute(strings);
      mAdapter.notifyDataSetChanged();
    }
  }
}


