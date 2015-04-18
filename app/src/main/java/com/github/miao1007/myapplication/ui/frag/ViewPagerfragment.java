package com.github.miao1007.myapplication.ui.frag;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.internal.widget.TintSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.github.miao1007.myapplication.R;
import com.github.miao1007.myapplication.ui.adapter.HorizenAdapter;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerfragment extends Fragment {

    Toolbar mToolbar;
    TintSpinner mSpinner;

    RecyclerView mRecyclerView;
    ViewPager mViewPager;
    HorizenAdapter mAdapter;
    List<String> mockData = new ArrayList<String>();

    ImageView imageView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_test_fragment1, container, false);


        mRecyclerView = (RecyclerView) mView.findViewById(R.id.pager_tap);
        mViewPager = (ViewPager) mView.findViewById(R.id.pager_content);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        for (int i = 0; i < 10; i++) {
            mockData.add("position " + i);
        }
        mAdapter = new HorizenAdapter(mockData);
        mAdapter.setOnTabClickListener(new HorizenAdapter.onTabClickListener() {
            @Override
            public void onTabClick(int position) {

            }
        });
        mRecyclerView.setAdapter(mAdapter);

        List<View> views = new ArrayList<View>();
        //views.add();
        //mViewPager.setAdapter(new PagerAdapter());

        trySetupToolbar();
        return mView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mToolbar != null && mSpinner != null) {
            mToolbar.removeView(mSpinner);
        }
    }

    void trySetupToolbar() {
        mSpinner = new TintSpinner(getActivity());
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        String[] frags = new String[]{
                "category1",
                "category2",
                "category3",
        };
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, frags);
        mSpinner.setAdapter(arrayAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.w("on click ", "clcik");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mToolbar.addView(mSpinner);
    }

}
