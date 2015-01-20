package com.github.miao1007.myapplication.ui.frag;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.miao1007.myapplication.R;

public class NavigationFragment extends Fragment {

    private NavigationDrawerCallbacks mCallback;

    private ListView mDrawerListView;
    private int mCurrentSelectedPosition = 0;


    public NavigationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        mDrawerListView = (ListView) mView.findViewById(R.id.listview);
        String[] frags = new String[]{
                ViewPagerfragment.class.getSimpleName(),
                CardFragment.class.getSimpleName()
        };
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, frags);
        mDrawerListView.setAdapter(arrayAdapter);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCallback.onNavigationDrawerItemSelected(i);
            }
        });
        //initial setelcted item
        selectItem(mCurrentSelectedPosition);
        return mView;
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mCallback != null) {
            mCallback.onNavigationDrawerItemSelected(position);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public interface NavigationDrawerCallbacks {
        public void onNavigationDrawerItemSelected(int position);
    }


}
