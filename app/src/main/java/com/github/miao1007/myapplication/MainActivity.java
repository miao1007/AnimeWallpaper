package com.github.miao1007.myapplication;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;


public class MainActivity extends ActionBarActivity implements NavigationFragment.NavigationDrawerCallbacks {

    private String TAG = getClass().getSimpleName();

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.inflateMenu(R.menu.menu_main);
            // Set Navigation Toggle
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            //throw new NullPointerException("Toolbar must be <include> in activity's layout!");
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        //TODO : change
        changeFragment(position);
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawers();
        }

    }

    void changeFragment(int position) {
        FragmentManager fragmentManager = getFragmentManager();
        switch (position) {
            case 0:
                fragmentManager.beginTransaction().replace(R.id.container, Testfragment1.newInstance("eg1", "eg1"), "FRAG1").commit();
                break;
            case 1:
                fragmentManager.beginTransaction().replace(R.id.container, CardFragment.newInstance("eg2", "eg2"), "FRAG2").commit();
                break;
        }

    }


}

