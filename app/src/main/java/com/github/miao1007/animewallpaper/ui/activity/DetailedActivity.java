package com.github.miao1007.animewallpaper.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bigkoo.alertview.AlertView;
import com.github.miao1007.animewallpaper.R;
import com.github.miao1007.animewallpaper.ui.widget.NavigationBar;
import com.github.miao1007.animewallpaper.utils.picasso.SquareUtils;
import com.github.miao1007.animewallpaper.support.api.konachan.ImageRepo;
import com.github.miao1007.animewallpaper.support.api.konachan.ImageResult;
import com.github.miao1007.animewallpaper.ui.widget.ActionSheet;
import com.github.miao1007.animewallpaper.ui.widget.Position;
import com.github.miao1007.animewallpaper.utils.LogUtils;
import com.github.miao1007.animewallpaper.utils.StatusbarUtils;
import com.github.miao1007.animewallpaper.utils.animation.AnimateUtils;
import com.github.miao1007.animewallpaper.utils.picasso.Blur;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 用户默认的交互区主要在左下角
 */
public class DetailedActivity extends AppCompatActivity {

  private static final String EXTRA_IMAGE = "URL";
  private static final String EXTRA_POSITION = "EXTRA_POSITION";
  private static final String FRAGMENT_TAG = "FRAGMENT_TAG";

  public static final String TAG = LogUtils.makeLogTag(DetailedActivity.class);

  @Bind(R.id.iv_detailed_card) ImageView ivDetailedCard;
  @Bind(R.id.blur_bg) ImageView ivDetailedCardBlur;

  ImageResult imageResult;
  @Bind(R.id.navigation_bar) NavigationBar mNavigationBar;
  @Bind(R.id.ll_detailed_downloads) LinearLayout mLlDetailedDownloads;

  AlertView alertView;
  boolean isPlaying = false;

  public static void startActivity(Context context, Position position, Parcelable parcelable) {
    Intent intent = new Intent(context, DetailedActivity.class);
    //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    intent.putExtra(EXTRA_IMAGE, parcelable);
    intent.putExtra(EXTRA_POSITION, position);
    context.startActivity(intent);
  }

  public static Position getPosition(Intent intent) {
    return intent.getParcelableExtra(EXTRA_POSITION);
  }

  @OnClick(R.id.detailed_back) void back() {
    onBackPressed();
  }

  @OnClick(R.id.detailed_tags) void tags() {
    String tilte = "Relevant tags";
    if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG) == null) {
      ActionSheet actionSheet = ActionSheet.newInstance(tilte, imageResult.getTags().split(" "));
      actionSheet.show(getSupportFragmentManager(), FRAGMENT_TAG);
    }
  }

  @OnClick(R.id.iv_detailed_card) void download(final ImageView v) {
    //final File file = getFilesDir();
    //Log.d(TAG, file.toString());
    //PhotoViewActivity.startScaleActivity(v.getContext(), Position.from(v));
    Toast.makeText(DetailedActivity.this, "iv_detailed_card", Toast.LENGTH_SHORT).show();
    //DownloadService.startService(v.getContext(), imageResult.getSampleUrl());
    final String str =
        imageResult.getSampleUrl().replaceAll("konachan.net", "7xq3s7.com1.z0.glb.clouddn.com");
    Log.d(TAG, str);
    SquareUtils.getProgressPicasso(this, new SquareUtils.ProgressListener() {
      @Override public void update(long bytesRead, long contentLength, boolean done) {
        System.out.format("%d%% done\n", (100 * bytesRead) / contentLength);
        DetailedActivity.this.runOnUiThread(new Runnable() {
          @Override public void run() {

          }
        });
      }
    }).load(str).placeholder(v.getDrawable()).into(v, new Callback() {
      @Override public void onSuccess() {
        PhotoViewActivity.startScaleActivity(v.getContext(), Position.from(v), str);
      }

      @Override public void onError() {

      }
    });
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    StatusbarUtils.from(this).setTransparentStatusbar(true).setLightStatusBar(false).process();
    setContentView(R.layout.fragment_image_detailed_card);
    ButterKnife.bind(this);
    mNavigationBar.setProgress(true);
    mNavigationBar.setTextColor(Color.WHITE);
    //mNavigationBar.setLeftClickListener(new View.OnClickListener() {
    //  @Override public void onClick(View v) {
    //    onBackPressed();
    //  }
    //});
    //mNavigationBar.setRightClickListener(new O);
    imageResult = getIntent().getParcelableExtra(EXTRA_IMAGE);
    Picasso.with(this)
        //we use qiniu CDN
        .load(imageResult.getPreviewUrl()
            .replace(ImageRepo.END_POINT_KONACHAN, ImageRepo.END_POINT_CDN))
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

                    AnimateUtils.animateViewBitmap(ivDetailedCardBlur, bitmap);

                    anim(getPosition(getIntent()), true, new Animator.AnimatorListener() {
                      @Override public void onAnimationStart(Animator animation) {

                      }

                      @Override public void onAnimationEnd(Animator animation) {
                        AnimateUtils.animateViewBitmap(ivDetailedCardBlur, bitmap);
                      }

                      @Override public void onAnimationCancel(Animator animation) {

                      }

                      @Override public void onAnimationRepeat(Animator animation) {

                      }
                    }, ivDetailedCard, mLlDetailedDownloads);
                  }
                });
          }
        });
  }

  /**
   * 动画封装，千万不要剁手改正负
   */
  void anim(final Position position, final boolean isEnter,
      final Animator.AnimatorListener listener, View... views) {
    if (isPlaying) {
      return;
    }
    //记住括号哦，我这里调试了一小时
    float delta = ((float) (position.width)) / ((float) (position.height));
    final float fromY, toY;
    float[] toDelta = new float[2];
    float[] fromDelta = new float[2];

    View view = views[0];
    float delt_Y = position.top;
    float delt_X = position.left - view.getLeft();
    if (isEnter) {
      fromDelta[0] = 1f;
      toDelta[0] = delta;
      fromDelta[1] = 4f;
      toDelta[1] = 1f;
      fromY = delt_Y;
      toY = 0;
    } else {
      fromDelta[0] = delta;
      toDelta[0] = 1f;
      fromDelta[1] = 1f;
      toDelta[1] = 4f;
      fromY = 0;
      toY = delt_Y;
    }
    view.setPivotX(view.getWidth() / 2);
    ObjectAnimator trans_Y = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, fromY, toY);
    ObjectAnimator scale_X = ObjectAnimator.ofFloat(view, View.SCALE_X, fromDelta[0], toDelta[0]);
    ObjectAnimator scale_Y = ObjectAnimator.ofFloat(view, View.SCALE_Y, fromDelta[0], toDelta[0]);
    ObjectAnimator scale_icn_X =
        ObjectAnimator.ofFloat(views[1], View.SCALE_X, fromDelta[1], toDelta[1]);
    ObjectAnimator scale_icn_Y =
        ObjectAnimator.ofFloat(views[1], View.SCALE_Y, fromDelta[1], toDelta[1]);

    AnimatorSet set = new AnimatorSet();

    set.playTogether(trans_Y, scale_X, scale_Y);
    set.playTogether(scale_icn_X, scale_icn_Y);
    set.setDuration(400);
    set.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animation) {
        listener.onAnimationStart(animation);
        isPlaying = true;
      }

      @Override public void onAnimationEnd(Animator animation) {
        listener.onAnimationEnd(animation);
        isPlaying = false;
      }

      @Override public void onAnimationCancel(Animator animation) {
        listener.onAnimationCancel(animation);
        isPlaying = false;
      }

      @Override public void onAnimationRepeat(Animator animation) {
        listener.onAnimationRepeat(animation);
      }
    });
    set.setInterpolator(new AccelerateInterpolator());
    set.start();
  }

  @Override public void onBackPressed() {
    if (alertView != null && alertView.isShowing()) {
      alertView.dismiss();
      return;
    }
    anim(getPosition(getIntent()), false, new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animation) {
        //mLlDetailedDownloads.animate().alpha(0f).setDuration(AnimateUtils.ANIM_DORITION).start();
        AnimateUtils.animateViewBitmap(ivDetailedCardBlur, null);
      }

      @Override public void onAnimationEnd(Animator animation) {
        DetailedActivity.super.onBackPressed();
        overridePendingTransition(0, 0);
      }

      @Override public void onAnimationCancel(Animator animation) {

      }

      @Override public void onAnimationRepeat(Animator animation) {

      }
    }, ivDetailedCard, mLlDetailedDownloads);
  }
}
