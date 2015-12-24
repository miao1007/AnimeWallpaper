package com.github.miao1007.myapplication.ui.activity;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.github.miao1007.myapplication.R;
import com.github.miao1007.myapplication.support.service.konachan.ImageResult;
import com.github.miao1007.myapplication.utils.StatusbarUtils;
import com.github.miao1007.myapplication.utils.animation.AnimateUtils;
import com.github.miao1007.myapplication.utils.picasso.Blur;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class DetailedActivity extends AppCompatActivity {

  public static final String EXTRA_IMAGE = "URL";
  @InjectView(R.id.iv_detailed_card) ImageView ivDetailedCard;
  @InjectView(R.id.blur_holder) ImageView ivDetailedCardBlur;

  private ImageResult imageResult;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragment_image_detailed_card);
    ButterKnife.inject(this);
    StatusbarUtils.setTranslucent(this);
    imageResult = getIntent().getParcelableExtra(EXTRA_IMAGE);
    Picasso.with(this).load(imageResult.getPreviewUrl())
        //.transform(new CircleTransformation())
        .config(Bitmap.Config.ARGB_8888).into(ivDetailedCard, new Callback.EmptyCallback() {
      @Override public void onSuccess() {
        Observable.just(ivDetailedCard)
            .map(new Func1<ImageView, Bitmap>() {
              @Override public Bitmap call(ImageView imageView) {
                return ((BitmapDrawable) imageView.getDrawable()).getBitmap();
              }
            })
            .map(new Func1<Bitmap, Bitmap>() {
              @Override public Bitmap call(Bitmap bitmap) {
                return Blur.fastblur(DetailedActivity.this, bitmap, 20);
              }
            })
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Bitmap>() {
              @TargetApi(Build.VERSION_CODES.JELLY_BEAN) @Override
              public void call(Bitmap toColor) {
                AnimateUtils.animateViewBitmap(ivDetailedCardBlur, toColor);
              }
            });
      }
    });
  }
}
