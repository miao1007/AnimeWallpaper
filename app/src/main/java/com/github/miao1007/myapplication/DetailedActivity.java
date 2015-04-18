package com.github.miao1007.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.github.miao1007.myapplication.service.konachan.ImageResult;
import com.github.miao1007.myapplication.ui.frag.ImageDetailedCard;

public class DetailedActivity extends BaseActivity {

  @InjectView(R.id.detailed_viewpager) ViewPager mViewPager;

  public static final String EXTRA_IMAGE = "URL";
  public static final int MAX_VIEWPAGER_COUNT = 500;
  public String url;

  private ImageResult imageResult;
  private FragmentManager fragmentManager;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detailed);
    ButterKnife.inject(this);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    imageResult = getIntent().getParcelableExtra(EXTRA_IMAGE);
    fragmentManager = getSupportFragmentManager();
    mViewPager.setAdapter(new ScreenSlidePagerAdapter(fragmentManager));

  }

  public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    public ScreenSlidePagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override public Fragment getItem(int position) {
      return ImageDetailedCard.newInstance(imageResult.getId() - position);
    }

    @Override public int getCount() {
      return MAX_VIEWPAGER_COUNT;
    }
  }
}
