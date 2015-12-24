package com.github.miao1007.myapplication.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.github.miao1007.myapplication.R;
import com.github.miao1007.myapplication.support.service.konachan.ImageResult;
import com.github.miao1007.myapplication.utils.FlyMeUtils;
import com.github.miao1007.myapplication.utils.LollipopUtils;
import com.github.miao1007.myapplication.utils.animation.AnimateUtils;
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
  @InjectView(R.id.container) LinearLayout container;

  private ImageResult imageResult;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragment_image_detailed_card);
    ButterKnife.inject(this);
    LollipopUtils.setStatusbarColor(this, getWindow().getDecorView());
    imageResult = getIntent().getParcelableExtra(EXTRA_IMAGE);
    Picasso.with(this).load(imageResult.getJpegUrl()).placeholder(R.drawable.lorempixel)
        //.transform(new BlurTransformation(getContext()))
        .into(ivDetailedCard, new Callback.EmptyCallback() {
          @Override public void onSuccess() {
            Observable.just(ivDetailedCard)
                .map(new Func1<ImageView, Bitmap>() {
                  @Override public Bitmap call(ImageView imageView) {
                    return ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                  }
                })
                .map(new Func1<Bitmap, Integer>() {
                  @Override public Integer call(Bitmap bitmap) {
                    return Palette.from(bitmap).generate().getMutedColor(Color.WHITE);
                  }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                  @Override public void call(Integer toColor) {
                    AnimateUtils.animateViewColor(getWindow().getDecorView(), toColor);

                  }
                });
          }
        });
  }
}
