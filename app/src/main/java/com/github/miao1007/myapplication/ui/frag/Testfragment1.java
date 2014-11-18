package com.github.miao1007.myapplication.ui.frag;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.internal.widget.TintSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.github.miao1007.myapplication.R;

public class Testfragment1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Toolbar mToolbar;
    TintSpinner mSpinner;

    ImageView imageView;

    public static Testfragment1 newInstance(String param1, String param2) {
        Testfragment1 fragment = new Testfragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Testfragment1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_test_fragment1, container, false);
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mSpinner = new TintSpinner(getActivity());
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
        return mView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mToolbar != null && mSpinner != null) {
            mToolbar.removeView(mSpinner);
        }
    }

}
