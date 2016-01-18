package com.github.miao1007.myapplication.ui.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.github.miao1007.myapplication.R;
import com.github.miao1007.myapplication.support.service.konachan.ImageRepo;
import com.github.miao1007.myapplication.support.service.konachan.ImageResult;
import com.github.miao1007.myapplication.ui.widget.NavigationBar;
import com.github.miao1007.myapplication.utils.LogUtils;
import com.github.miao1007.myapplication.utils.StatusbarUtils;
import com.github.miao1007.myapplication.utils.animation.AnimateUtils;
import com.github.miao1007.myapplication.utils.picasso.Blur;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.io.File;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class DetailedActivity extends AppCompatActivity {

  public static final String EXTRA_IMAGE = "URL";
  public static final String EXTRA_HEIGHT = "hei";
  public static final String TAG = LogUtils.makeLogTag(DetailedActivity.class);

  @InjectView(R.id.iv_detailed_card) ImageView ivDetailedCard;
  @InjectView(R.id.blur_bg) ImageView ivDetailedCardBlur;
  @InjectView(R.id.image_holder) LinearLayout mImageHolder;

  int height;
  ImageResult imageResult;
  @InjectView(R.id.navigation_bar) NavigationBar mNavigationBar;

  public static void startActivity(Context context, Parcelable url, int height) {
    Intent intent = new Intent(context, DetailedActivity.class);
    //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    intent.putExtra(EXTRA_IMAGE, url);
    intent.putExtra(EXTRA_HEIGHT, height);
    context.startActivity(intent);
  }

  @OnClick(R.id.iv_detailed_card) void download( final ImageView v) {
    File file = getFilesDir();
    Log.d(TAG, file.toString());
    //file.isDirectory()
    Picasso.with(this).load(imageResult.getSampleUrl()).into(v, new Callback() {
      @Override public void onSuccess() {
        //for (View view = v.getParent(); v)
        ((ViewGroup) v.getParent()).setClipChildren(false);
        //float delx = getWindow().getDecorView().w
        Animation anim = new ScaleAnimation(1f, 1.5f, // Start and end values for the X axis scaling
            1f, 1.5f, // Start and end values for the Y axis scaling
            Animation.RELATIVE_TO_SELF, 0.5f, // scale from mid of x
            Animation.RELATIVE_TO_SELF, 0.5f); // scale from mid of y
        anim.setDuration(400);
        anim.setFillAfter(true); // Needed to keep the result of the animation
        v.startAnimation(anim);
      }

      @Override public void onError() {

      }
    });
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragment_image_detailed_card);
    ButterKnife.inject(this);
    StatusbarUtils.setTranslucentAndFit(this, mNavigationBar);
    mNavigationBar.setProgress(true);
    mNavigationBar.setTextColor(Color.WHITE);
    imageResult = getIntent().getParcelableExtra(EXTRA_IMAGE);
    height = getIntent().getIntExtra(EXTRA_HEIGHT, 0);
    ivDetailedCard.setVisibility(View.GONE);
    Picasso.with(this)
        .load(imageResult.getPreviewUrl().replace(ImageRepo.END_POINT, ImageRepo.END_POINT_CDN))
        //.transform(new CircleTransformation())
        .config(Bitmap.Config.ARGB_8888)
        .into(ivDetailedCard, new Callback.EmptyCallback() {
          @Override public void onSuccess() {
            //ivDetailedCard.setVisibility(View.VISIBLE);
            //Animation animation = new TranslateAnimation(0, 0, height, 0);
            //animation.setDuration(400);
            //ivDetailedCard.startAnimation(animation);
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
                  public void call(final Bitmap toColor) {
                    ivDetailedCard.setVisibility(View.VISIBLE);
                    Animation animation = new TranslateAnimation(0, 0, height, 0);
                    animation.setDuration(AnimateUtils.ANIM_DORITION);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                      @Override public void onAnimationStart(Animation animation) {

                      }

                      @Override public void onAnimationEnd(Animation animation) {
                        AnimateUtils.animateViewBitmap(ivDetailedCardBlur, toColor);
                      }

                      @Override public void onAnimationRepeat(Animation animation) {

                      }
                    });
                    ivDetailedCard.startAnimation(animation);
                  }
                });
          }
        });
  }

  /**
   * Cancel all exit animation
   */
  @Override public void finish() {

    Animation animation = new TranslateAnimation(0, 0, 0, height);
    animation.setDuration(AnimateUtils.ANIM_DORITION);
    animation.setFillEnabled(true);
    animation.setFillAfter(true);
    animation.setAnimationListener(new Animation.AnimationListener() {
      @Override public void onAnimationStart(Animation animation) {
      }

      @Override public void onAnimationEnd(Animation animation) {
        DetailedActivity.super.finish();
        //overridePendingTransition(0, 0);
      }

      @Override public void onAnimationRepeat(Animation animation) {

      }
    });
    AnimateUtils.animateViewBitmap(ivDetailedCardBlur, null);
    ivDetailedCard.startAnimation(animation);
  }
}
