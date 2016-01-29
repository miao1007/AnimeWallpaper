package com.github.miao1007.animewallpaper.ui.frag;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.github.miao1007.animewallpaper.R;
import com.github.miao1007.animewallpaper.ui.adapter.HorizenAdapter;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerfragment extends Fragment {

  Toolbar mToolbar;

  RecyclerView mRecyclerView;
  ViewPager mViewPager;
  HorizenAdapter mAdapter;
  List<String> mockData = new ArrayList<String>();

  ImageView imageView;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View mView = inflater.inflate(R.layout.fragment_test_fragment1, container, false);

    mRecyclerView = (RecyclerView) mView.findViewById(R.id.pager_tap);
    mViewPager = (ViewPager) mView.findViewById(R.id.pager_content);
    mRecyclerView.setLayoutManager(
        new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    for (int i = 0; i < 10; i++) {
      mockData.add("position " + i);
    }
    mAdapter = new HorizenAdapter(mockData);
    mAdapter.setOnTabClickListener(new HorizenAdapter.onTabClickListener() {
      @Override public void onTabClick(int position) {

      }
    });
    mRecyclerView.setAdapter(mAdapter);

    List<View> views = new ArrayList<View>();
    //views.add();
    //mViewPager.setAdapter(new PagerAdapter());

    return mView;
  }
}
