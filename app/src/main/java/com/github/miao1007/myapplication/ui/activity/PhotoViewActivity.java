package com.github.miao1007.myapplication.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.github.miao1007.myapplication.R;
import com.github.miao1007.myapplication.ui.widget.Position;
import com.github.miao1007.myapplication.utils.LogUtils;
import com.github.miao1007.myapplication.utils.StatusbarUtils;
import com.github.miao1007.myapplication.utils.picasso.SquareUtils;
import com.squareup.picasso.Callback;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by leon on 1/19/16.
 */
public class PhotoViewActivity extends AppCompatActivity {

  static final String TAG = LogUtils.makeLogTag(PhotoViewActivity.class);
  static final public String EXTRA_POSI = "EXTRA_POSI";
  static final public String EXTRA_URL = "EXTRA_URL";

  @Bind(R.id.iv_photo) ImageView mIvPhoto;
  //static final public String EXTRA_RIGHT = "EXTRA_RIGHT";
  //static final public String EXTRA_TOP = "EXTRA_TOP";
  //static final public String EXTRA_BOTTOM = "EXTRA_BOTTOM";
  //static final public String EXTRA_WIDTH = "EXTRA_WIDTH";
  //static final public String EXTRA_POSITION = "EXTRA_POSITION";

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
