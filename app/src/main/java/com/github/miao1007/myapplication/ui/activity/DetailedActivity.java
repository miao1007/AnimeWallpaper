package com.github.miao1007.myapplication.ui.activity;

import android.support.v4.app.Fragment;
import com.github.miao1007.myapplication.support.service.konachan.ImageResult;
import com.github.miao1007.myapplication.ui.activity.base.BaseFragmentActivity;
import com.github.miao1007.myapplication.ui.frag.ImagePreviewCard;

public class DetailedActivity extends BaseFragmentActivity implements ImagePreviewCard.OnImageLoadedListener {

  public static final String EXTRA_IMAGE = "URL";

  private ImageResult imageResult;

  @Override public Fragment getSupportFragment() {
    imageResult = getIntent().getParcelableExtra(EXTRA_IMAGE);
    return ImagePreviewCard.newInstance(imageResult);
  }

  @Override public void onArticleSelected(int color) {
    //setUpToolbarColor(mToolbar,color);
  }
}
