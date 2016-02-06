package com.github.miao1007.animewallpaper.ui.activity;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
  Uri imageUri;
  PhotoViewAttacher mAttacher;

  @OnClick(R.id.photoview_iv_setwallpaper) void photoview_iv_setwallpaper(ImageView view) {
    //BitmapDrawable drawable = (BitmapDrawable) mIvPhoto.getDrawable();
    final Bitmap bitmap = mAttacher.getVisibleRectangleBitmap();
    if (bitmap != null) {
      final WallpaperManager manager = WallpaperManager.getInstance(this);
      Snackbar.make(((View) mIvPhoto.getParent()), "SnackbarTest", Snackbar.LENGTH_LONG)
          .setActionTextColor(Color.WHITE)
          .show();
      new Thread(new Runnable() {
        @Override public void run() {
          try {
            manager.setBitmap(bitmap);
          } catch (Exception e) {
            e.printStackTrace();
          } finally {
            runOnUiThread(new Runnable() {
              @Override public void run() {
                finish();
              }
            });
          }
        }
      }).start();
    }
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_photoview);
    ButterKnife.bind(this);
    imageUri = getIntent().getData();
    if (imageUri == null) {
      Toast.makeText(WallpaperSetActivity.this,
          "No URI passed in intent, exiting WallpaperCropActivity", Toast.LENGTH_SHORT).show();
      Log.e(TAG, "No URI passed in intent, exiting WallpaperCropActivity");
      finish();
      return;
    }

    Log.d(TAG, getIntent().getData().toString());
    SquareUtils.getPicasso(this).load(getIntent().getData()).into(mIvPhoto, new Callback() {
      @Override public void onSuccess() {
        mAttacher = new PhotoViewAttacher(mIvPhoto);
        mAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mAttacher.update();
      }

      @Override public void onError() {

      }
    });
  }

  @Override protected void onDestroy() {
    if (mAttacher != null) {
      mAttacher.cleanup();
    }
    super.onDestroy();
  }
}
