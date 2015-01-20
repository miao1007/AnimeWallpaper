package com.github.miao1007.myapplication;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.github.miao1007.myapplication.ui.frag.CardFragment;
import com.github.miao1007.myapplication.ui.frag.NavigationFragment;
import com.github.miao1007.myapplication.ui.frag.ViewPagerfragment;

public class MainActivity extends ActionBarActivity
    implements NavigationFragment.NavigationDrawerCallbacks {

  private String TAG = getClass().getSimpleName();

  private ActionBarDrawerToggle mDrawerToggle;

  @InjectView(R.id.toolbar) Toolbar mToolbar;

  @InjectView(R.id.drawer) DrawerLayout mDrawerLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.inject(this);
    if (mToolbar != null) {
      setSupportActionBar(mToolbar);
      mToolbar.inflateMenu(R.menu.menu_main);
      // Set Navigation Toggle
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    } else {
      //throw new NullPointerException("Toolbar must be <include> in activity's layout!");
    }
    mDrawerToggle =
        new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
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
        fragmentManager.beginTransaction()
            .replace(R.id.container, new ViewPagerfragment())
            .commit();
        break;
      case 1:
        fragmentManager.beginTransaction().replace(R.id.container, new CardFragment()).commit();
        break;
    }
  }
}

