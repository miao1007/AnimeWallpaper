package com.github.miao1007.myapplication.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.github.miao1007.myapplication.R;
import com.github.miao1007.myapplication.ui.frag.CardFragment;
import com.github.miao1007.myapplication.ui.frag.ViewPagerfragment;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  @InjectView(R.id.toolbar) Toolbar mToolbar;
  @InjectView(R.id.toolbar_holder) RelativeLayout mToolbarHolder;
  @InjectView(R.id.navigation_drawer) NavigationView mNavigationDrawer;
  @InjectView(R.id.drawer_layout) DrawerLayout mDrawerLayout;

  private ActionBarDrawerToggle mDrawerToggle;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.inject(this);
    //LollipopUtils.setStatusbarColor(this, mToolbarHolder);
    setUpToolbar();
    setUpDrawer();
  }

  private void setUpDrawer() {
    mDrawerToggle =
        new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
    mDrawerToggle.syncState();
    mDrawerLayout.setDrawerListener(mDrawerToggle);
    mNavigationDrawer.setNavigationItemSelectedListener(this);
    transactionFragment(new CardFragment());
  }

  @Override public boolean onNavigationItemSelected(MenuItem menuItem) {
    menuItem.setChecked(true);
    mDrawerLayout.closeDrawers();
    switch (menuItem.getItemId()) {
      case R.id.navigation_item_1:
        transactionFragment(new CardFragment());
        return true;
      case R.id.navigation_item_2:
        transactionFragment(new ViewPagerfragment());
        return true;
      case R.id.navigation_item_settings:
        startActivity(new Intent(this, SettingsActivity.class));
        return true;
      default:
        return true;
    }
  }

  private void transactionFragment(Fragment fragment) {
    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
  }

  private void setUpToolbar() {
    if (mToolbar != null) {
      setSupportActionBar(mToolbar);
    }
  }
}

