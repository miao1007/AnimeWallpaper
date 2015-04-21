package com.github.miao1007.myapplication.ui.activity.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.github.miao1007.myapplication.R;

/**
 * Created by leon on 4/20/15.
 */
public abstract class BaseFragmentActivity extends BaseActivity {

  @InjectView(R.id.base_frag_toolbar) public Toolbar mToolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Init the swipe back
    setContentView(R.layout.activity_base_fragment);
    ButterKnife.inject(this);
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportFragmentManager().beginTransaction().replace(R.id.base_fragment_container, getFragment()).commit();
  }

  abstract protected Fragment getFragment();

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home){
      onBackPressed();
    }
    return super.onOptionsItemSelected(item);
  }

}
