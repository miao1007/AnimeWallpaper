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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.miao1007.animewallpaper.R;
import com.github.miao1007.animewallpaper.support.api.konachan.ImageResult;
import com.github.miao1007.animewallpaper.ui.widget.ActionSheet;
import com.github.miao1007.animewallpaper.ui.widget.NavigationBar;
import com.github.miao1007.animewallpaper.ui.widget.Position;
import com.github.miao1007.animewallpaper.utils.FileUtils;
import com.github.miao1007.animewallpaper.utils.LogUtils;
import com.github.miao1007.animewallpaper.utils.SquareUtils;
import com.github.miao1007.animewallpaper.utils.StatusbarUtils;
import com.github.miao1007.animewallpaper.utils.WallpaperUtils;
import com.github.miao1007.animewallpaper.utils.animation.AnimateUtils;
import com.github.miao1007.animewallpaper.utils.picasso.Blur;
import com.squareup.picasso.Callback;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 用户默认的交互区主要在左下角
 */
public class DetailedActivity extends AppCompatActivity {

  public static final String TAG = LogUtils.makeLogTag(DetailedActivity.class);
  private static final String EXTRA_IMAGE = "URL";
  private static final String EXTRA_POSITION = "EXTRA_POSITION";
  private static final String FRAGMENT_TAG = "FRAGMENT_TAG";
  @Bind(R.id.iv_detailed_card) ImageView ivDetailedCard;
  @Bind(R.id.blur_bg) ImageView ivDetailedCardBlur;
  @Bind(R.id.navigation_bar) NavigationBar mNavigationBar;
  @Bind(R.id.ll_detailed_downloads) LinearLayout mLlDetailedDownloads;

  ImageResult imageResult;
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
    final List<String> tags = Arrays.asList(imageResult.getTags().split(" "));
    ActionSheet a = new ActionSheet(this.getWindow(), tilte, new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MainActivity.startRefreshActivity(DetailedActivity.this, tags.get(position));
      }
    }, tags);
    a.show();
  }

  @OnClick(R.id.image_setwallpaper) void image_setwallpaper() {
    mNavigationBar.setProgressBar(true);

    fileDownload().subscribe(new Subscriber<File>() {
      @Override public void onCompleted() {
        mNavigationBar.setProgressBar(false);
      }

      @Override public void onError(Throwable e) {
        mNavigationBar.setProgressBar(false);
      }

      @Override public void onNext(File file) {
        mNavigationBar.setProgressBar(false);
        WallpaperUtils.from(DetailedActivity.this).setWallpaper(file);
      }
    });
  }

  @OnClick(R.id.image_download) void image_download() {
    mNavigationBar.setProgressBar(true);

    fileDownload().subscribe(new Subscriber<File>() {
      @Override public void onCompleted() {
        mNavigationBar.setProgressBar(false);
      }

      @Override public void onError(Throwable e) {
        mNavigationBar.setProgressBar(false);
      }

      @Override public void onNext(File file) {
        mNavigationBar.setProgressBar(false);

        final Intent shareIntent = new Intent(Intent.ACTION_VIEW);
        shareIntent.setDataAndType(Uri.fromFile(file), "image/*");
        startActivity(Intent.createChooser(shareIntent, "View image using"));
      }
    });
  }

  @OnClick(R.id.image_share) void image_share() {
    fileDownload().subscribe(new Subscriber<File>() {
      @Override public void onCompleted() {
        mNavigationBar.setProgressBar(false);
      }

      @Override public void onError(Throwable e) {
        mNavigationBar.setProgressBar(false);
      }

      @Override public void onNext(File file) {
        mNavigationBar.setProgressBar(false);

        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        startActivity(Intent.createChooser(shareIntent, "Share image using"));
      }
    });
    //WallpaperUtils.from(DetailedActivity.this).setWallpaper(file);

  }

  //@OnClick(R.id.ll_detailed_downloads)
  Observable<File> fileDownload() {
    final Request request = new Request.Builder().url(imageResult.getSampleUrl()).get().build();
    return Observable.create(new Observable.OnSubscribe<Response>() {
      @Override public void call(Subscriber<? super Response> subscriber) {
        try {
          subscriber.onNext(SquareUtils.getClient().newCall(request).execute());
        } catch (IOException e) {
          subscriber.onError(e);
        }
      }
    }).map(new Func1<Response, File>() {
      @Override public File call(Response response) {
        return FileUtils.saveBodytoFile(response.body(),
            Uri.parse(imageResult.getSampleUrl()).getLastPathSegment());
      }
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
  }

  @OnClick(R.id.iv_detailed_card) void download(final ImageView v) {
    Toast.makeText(DetailedActivity.this, "Starting preview large image", Toast.LENGTH_SHORT)
        .show();
    mNavigationBar.setProgressBar(true);
    SquareUtils.getPicasso(this)
        .load(imageResult.getSampleUrl())
        .placeholder(v.getDrawable())
        .into(v, new Callback() {
          @Override public void onSuccess() {
            mNavigationBar.setProgressBar(false);
          }

          @Override public void onError() {
            mNavigationBar.setProgressBar(false);
          }
        });
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    SquareUtils.getPicasso(this).cancelRequest(ivDetailedCard);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    StatusbarUtils.from(this).setTransparentStatusbar(true).setLightStatusBar(false).process();
    setContentView(R.layout.fragment_image_detailed_card);
    ButterKnife.bind(this);
    mNavigationBar.setTextColor(Color.WHITE);
    //mNavigationBar.setLeftClickListener(new View.OnClickListener() {
    //  @Override public void onClick(View v) {
    //    onBackPressed();
    //  }
    //});
    //mNavigationBar.setRightClickListener(new O);
    imageResult = getIntent().getParcelableExtra(EXTRA_IMAGE);
    SquareUtils.getPicasso(this)
        .load(imageResult.getPreviewUrl())
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
                    return Blur.apply(DetailedActivity.this, bitmap, 20);
                  }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                  @TargetApi(Build.VERSION_CODES.JELLY_BEAN) @Override
                  public void call(final Bitmap bitmap) {
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
  void anim(final Position position, final boolean in, final Animator.AnimatorListener listener,
      View... views) {
    if (isPlaying) {
      return;
    }
    //记住括号哦，我这里调试了一小时
    float delta = ((float) (position.width)) / ((float) (position.height));
    //243 - 168(navi) = 75 = status_bar
    float[] y_img = {
        position.top - (views[0].getY() + (in ? (StatusbarUtils.getStatusBarOffsetPx(this)) : 0)), 0
    };
    float[] s_img = { 1f, delta };

    float[] y_icn = { views[1].getHeight() * 4, 0 };
    float[] s_icn = { 3f, 1f };

    views[0].setPivotX(views[0].getWidth() / 2);
    views[0].setPivotY(0);
    views[1].setPivotX(views[1].getWidth() / 2);
    views[1].setPivotY(0);

    Animator trans_Y =
        ObjectAnimator.ofFloat(views[0], View.TRANSLATION_Y, in ? y_img[0] : y_img[1],
            in ? y_img[1] : y_img[0]);
    Animator scale_X = ObjectAnimator.ofFloat(views[0], View.SCALE_X, in ? s_img[0] : s_img[1],
        in ? s_img[1] : s_img[0]);
    Animator scale_Y = ObjectAnimator.ofFloat(views[0], View.SCALE_Y, in ? s_img[0] : s_img[1],
        in ? s_img[1] : s_img[0]);
    Animator scale_icn_X = ObjectAnimator.ofFloat(views[1], View.SCALE_X, in ? s_icn[0] : s_icn[1],
        in ? s_icn[1] : s_icn[0]);
    Animator scale_icn_Y = ObjectAnimator.ofFloat(views[1], View.SCALE_Y, in ? s_icn[0] : s_icn[1],
        in ? s_icn[1] : s_icn[0]);

    Animator trans_icn_Y =
        ObjectAnimator.ofFloat(views[1], View.TRANSLATION_Y, in ? y_icn[0] : y_icn[1],
            in ? y_icn[1] : y_icn[0]);

    AnimatorSet set = new AnimatorSet();

    set.playTogether(trans_Y, scale_X, scale_Y);
    set.playTogether(scale_icn_X, scale_icn_Y, trans_icn_Y);
    set.setDuration(300);
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

    anim(getPosition(getIntent()), false, new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animation) {
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
