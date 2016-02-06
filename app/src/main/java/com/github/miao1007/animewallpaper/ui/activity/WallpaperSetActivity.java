package com.github.miao1007.animewallpaper.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.github.miao1007.animewallpaper.R;
import com.github.miao1007.animewallpaper.utils.LogUtils;
import com.github.miao1007.animewallpaper.utils.picasso.SquareUtils;
import com.squareup.picasso.Callback;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by leon on 1/19/16.
 */

public class WallpaperSetActivity extends AppCompatActivity {

  static final String TAG = LogUtils.makeLogTag(WallpaperSetActivity.class);
  @Bind(R.id.iv_photo) ImageView mIvPhoto;
  @Bind(R.id.photoview_iv_setwallpaper) ImageView mPhotoviewIvSetwallpaper;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_photoview);
    ButterKnife.bind(this);
    Log.d(TAG, getIntent().getData().toString());
    SquareUtils.getPicasso(this).load(getIntent().getData()).into(mIvPhoto, new Callback() {
      @Override public void onSuccess() {
        PhotoViewAttacher mAttacher = new PhotoViewAttacher(mIvPhoto);
        mAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mAttacher.update();
      }

      @Override public void onError() {

      }
    });
  }
}
