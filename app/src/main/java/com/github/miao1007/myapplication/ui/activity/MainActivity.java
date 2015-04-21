package com.github.miao1007.myapplication.ui.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.github.miao1007.myapplication.R;
import com.github.miao1007.myapplication.ui.activity.base.BaseActivity;
import com.github.miao1007.myapplication.ui.frag.CardFragment;
import com.github.miao1007.myapplication.ui.frag.NavigationFragment;

public class MainActivity extends BaseActivity
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
    setUpToolbarColor(mToolbar);
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
        fragmentManager.beginTransaction().replace(R.id.container, new CardFragment()).commit();
        break;
      case 1:
        startActivity(new Intent(this,SettingsActivity.class));
        break;
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    return super.onOptionsItemSelected(item);
  }
}

