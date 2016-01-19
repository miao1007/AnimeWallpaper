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
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
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
import com.github.miao1007.myapplication.ui.widget.Position;
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

/**
 * 用户默认的交互区主要在左下角
 */
public class DetailedActivity extends AppCompatActivity {

  public static final String EXTRA_IMAGE = "URL";
  public static final String EXTRA_HEIGHT = "EXTRA_HEIGHT";
  public static final String EXTRA_TOP = "EXTRA_TOP";
  public static final String EXTRA_WIDTH = "EXTRA_WIDTH";
  public static final String TAG = LogUtils.makeLogTag(DetailedActivity.class);

  @InjectView(R.id.iv_detailed_card) ImageView ivDetailedCard;
  @InjectView(R.id.blur_bg) ImageView ivDetailedCardBlur;
  @InjectView(R.id.image_holder) LinearLayout mImageHolder;

  int height;
  int width;
  int top;
  ImageResult imageResult;
  @InjectView(R.id.navigation_bar) NavigationBar mNavigationBar;
  @InjectView(R.id.ll_detailed_downloads) LinearLayout mLlDetailedDownloads;

  public static void startActivity(Context context, Parcelable parcelable, int top, int height,
      int width) {
    Intent intent = new Intent(context, DetailedActivity.class);
    //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    intent.putExtra(EXTRA_IMAGE, parcelable);
    intent.putExtra(EXTRA_HEIGHT, height);
    intent.putExtra(EXTRA_WIDTH, width);
    intent.putExtra(EXTRA_TOP, top);
    context.startActivity(intent);
  }

  @OnClick(R.id.detailed_back) void back() {
    onBackPressed();
  }

  @OnClick(R.id.detailed_tags) void tags() {
    //todo
  }

  @OnClick(R.id.iv_detailed_card) void download(final ImageView v) {
    final File file = getFilesDir();
    Log.d(TAG, file.toString());
    PhotoViewActivity.startScaleActivity(v.getContext(), Position.from(v));
    //file.isDirectory()
    //Picasso.with(this)
    //    .load(imageResult.getSampleUrl())
    //    .placeholder(v.getDrawable())
    //    .into(v, new Callback() {
    //      @Override public void onSuccess() {
    //        //for (View view = v.getParent(); v)
    //        ((ViewGroup) v.getParent()).setClipChildren(false);
    //        float del_scale = ((float) getWindowManager().getDefaultDisplay().getHeight())
    //            / ((float) v.getHeight());
    //        float del_y = ((float) v.getTop()) / ((float) v.getBottom());
    //
    //        Animation anim =
    //            new ScaleAnimation(1f, del_scale, // Start and end values for the X axis scaling
    //                1f, del_scale, // Start and end values for the Y axis scaling
    //                Animation.RELATIVE_TO_SELF, 0.5f, // scale from mid of x
    //                Animation.RELATIVE_TO_SELF, del_y); // scale from mid of y
    //        anim.setDuration(AnimateUtils.ANIM_DORITION);
    //        AnimateUtils.animateViewBitmap(ivDetailedCardBlur, null);
    //        anim.setFillAfter(true); // Needed to keep the result of the animation
    //        v.startAnimation(anim);
    //      }
    //
    //      @Override public void onError() {
    //
    //      }
    //    });
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    StatusbarUtils.from(this).setTransparentStatusbar(true).setLightStatusBar(false).process();
    setContentView(R.layout.fragment_image_detailed_card);
    ButterKnife.inject(this);
    mNavigationBar.setProgress(true);
    mNavigationBar.setTextColor(Color.WHITE);
    imageResult = getIntent().getParcelableExtra(EXTRA_IMAGE);

    Picasso.with(this)
        //we use qiniu CDN
        .load(imageResult.getPreviewUrl().replace(ImageRepo.END_POINT, ImageRepo.END_POINT_CDN))
        .config(Bitmap.Config.ARGB_8888)
        .into(ivDetailedCard, new Callback.EmptyCallback() {
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
                  public void call(final Bitmap bitmap) {

                    height = getIntent().getIntExtra(EXTRA_HEIGHT, 1);
                    width = getIntent().getIntExtra(EXTRA_WIDTH, 1);
                    top = getIntent().getIntExtra(EXTRA_TOP, 1);
                    Log.d(TAG, "TOP=" + top + " HEIGHT=" + height + " WIDTH=" + width);
                    float delta = ((float) width) / ((float) height);
                    Log.d(TAG, delta + "");
                    anim(ivDetailedCard, top, height, width, true,
                        new Animation.AnimationListener() {
                          @Override public void onAnimationStart(Animation animation) {
                            Animation trans =
                                new TranslateAnimation(0, 0, mLlDetailedDownloads.getHeight(), 0);
                            Animation scale = new ScaleAnimation(1.5f,1f,1.5f,1f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0f);

                            AnimationSet set = new AnimationSet(true);
                            set.addAnimation(scale);
                            set.addAnimation(trans);
                            set.setDuration(animation.getDuration() *  2);
                            mLlDetailedDownloads.startAnimation(set);
                          }

                          @Override public void onAnimationEnd(Animation animation) {
                            AnimateUtils.animateViewBitmap(ivDetailedCardBlur, bitmap);
                          }

                          @Override public void onAnimationRepeat(Animation animation) {

                          }
                        });
                  }
                });
          }
        });
  }

  /**
   * 动画封装，千万不要剁手改正负
   */
  void anim(View view, int top, int height, int width, boolean isEnter,
      Animation.AnimationListener listener) {
    //记住括号哦，我这里调试了一小时
    float delta = ((float) width) / ((float) height);
    float fromDelta, toDelta, fromY, toY;
    if (isEnter) {
      fromDelta = 1f;
      toDelta = delta;
      fromY = top;
      toY = 0;
    } else {
      fromDelta = delta;
      toDelta = 1f;
      fromY = 0;
      toY = top;
    }
    Animation anim = new ScaleAnimation(fromDelta, toDelta,
        // Start and end values for the X axis scaling
        fromDelta, toDelta, // Start and end values for the Y axis scaling
        Animation.RELATIVE_TO_SELF, 0.5f, // scale from mid of x
        Animation.RELATIVE_TO_SELF, 0f); // scale from start of y
    Animation trans = new TranslateAnimation(0, 0f, fromY, toY);
    AnimationSet set = new AnimationSet(true);
    //添加并行动画
    set.addAnimation(anim);
    set.addAnimation(trans);
    //动画结束后保持原样
    set.setFillEnabled(true);
    set.setFillAfter(true);
    //监听器
    set.setAnimationListener(listener);
    set.setDuration(AnimateUtils.ANIM_DORITION);
    view.startAnimation(set);
  }

  /**
   * Cancel all exit animation
   */
  @Override public void finish() {
    anim(ivDetailedCard, top, height, width, false, new Animation.AnimationListener() {
      @Override public void onAnimationStart(Animation animation) {
        mLlDetailedDownloads.animate().alpha(0f).setDuration(AnimateUtils.ANIM_DORITION).start();
        AnimateUtils.animateViewBitmap(ivDetailedCardBlur, null);
      }

      @Override public void onAnimationEnd(Animation animation) {
        DetailedActivity.super.finish();
        overridePendingTransition(0, 0);
      }

      @Override public void onAnimationRepeat(Animation animation) {

      }
    });
  }
}
