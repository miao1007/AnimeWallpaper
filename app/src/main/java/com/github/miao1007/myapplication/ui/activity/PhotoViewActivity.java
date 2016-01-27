package com.github.miao1007.myapplication.ui.activity;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.annotation.WorkerThread;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.miao1007.myapplication.R;
import com.github.miao1007.myapplication.ui.widget.Position;
import com.github.miao1007.myapplication.utils.LogUtils;
import com.github.miao1007.myapplication.utils.StatusbarUtils;
import com.github.miao1007.myapplication.utils.picasso.SquareUtils;
import com.squareup.picasso.Callback;
import java.io.File;
import java.io.IOException;
import okhttp3.CacheControl;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.io.FileSystem;
import okio.BufferedSink;
import okio.Okio;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by leon on 1/19/16.
 */

public class PhotoViewActivity extends AppCompatActivity {

  static final String TAG = LogUtils.makeLogTag(PhotoViewActivity.class);
  static final public String EXTRA_POSI = "EXTRA_POSI";
  static final public String EXTRA_URL = "EXTRA_URL";

  @Bind(R.id.iv_photo) ImageView mIvPhoto;
  @Bind(R.id.photoview_iv_setwallpaper) ImageView mPhotoviewIvSetwallpaper;
  @Bind(R.id.photoview_iv_download) ImageView mPhotoviewIvDownload;
  @Bind(R.id.photoview_iv_share) ImageView mPhotoviewIvShare;

  static String getUrl(Intent intent) {
    return intent.getStringExtra(EXTRA_URL);
  }


  @RequiresPermission(android.Manifest.permission.SET_WALLPAPER)
  @OnClick(R.id.photoview_iv_setwallpaper) void photoview_iv_setwallpaper() {
    Request request = new Request.Builder().cacheControl(CacheControl.FORCE_CACHE)
        .url(getUrl(getIntent()))
        .get()
        .build();
    SquareUtils.getClient().newCall(request).enqueue(new okhttp3.Callback() {
      @Override @WorkerThread public void onFailure(Request request, IOException e) {

      }

      @WorkerThread @Override public void onResponse(Response response) throws IOException {
        try {
          final File file = new File(Environment.getExternalStorageDirectory(),
              Uri.parse(getUrl(getIntent())).getLastPathSegment());
          final FileSystem fileSystem = FileSystem.SYSTEM;
          final BufferedSink sink = Okio.buffer(fileSystem.sink(file));
          sink.writeAll(response.body().source());
          sink.close();
          new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override public void run() {
              //Uri uri = Uri.fromFile(file);
              //Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
              //String mime = "image/*";
              //intent.setDataAndType(uri, mime);
              //startActivity(intent);
              //PhotoViewActivity.this.startActivity(intent);
              WallpaperManager wm = WallpaperManager.getInstance(PhotoViewActivity.this);
              try {
                wm.setBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
              } catch (IOException e) {
                Toast.makeText(PhotoViewActivity.this, "Cannot set image as wallpaper",
                    Toast.LENGTH_SHORT).show();
              }
            }
          });
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }

  public static Position getPosition(Intent intent) {
    return intent.getParcelableExtra(EXTRA_POSI);
  }

  static void startScaleActivity(Context context, Position position, String url) {
    Intent intent = new Intent(context, PhotoViewActivity.class);
    intent.putExtra(EXTRA_POSI, position);
    intent.putExtra(EXTRA_URL, url);
    context.startActivity(intent);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    StatusbarUtils.from(this).setTransparentStatusbar(true).setLightStatusBar(false).process();
    setContentView(R.layout.activity_photoview);
    ButterKnife.bind(this);
    SquareUtils.getProgressPicasso(this).load(getIntent().getStringExtra(EXTRA_URL))
        .into(mIvPhoto, new Callback() {
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
