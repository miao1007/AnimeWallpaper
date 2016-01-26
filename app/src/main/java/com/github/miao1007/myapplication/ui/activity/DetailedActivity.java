package com.github.miao1007.myapplication.ui.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bigkoo.alertview.AlertView;
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
import java.util.Arrays;
import java.util.List;
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

  public static final String TAG = LogUtils.makeLogTag(DetailedActivity.class);

  @Bind(R.id.iv_detailed_card) ImageView ivDetailedCard;
  @Bind(R.id.blur_bg) ImageView ivDetailedCardBlur;
  @Bind(R.id.image_holder) LinearLayout mImageHolder;

  ImageResult imageResult;
  @Bind(R.id.navigation_bar) NavigationBar mNavigationBar;
  @Bind(R.id.ll_detailed_downloads) LinearLayout mLlDetailedDownloads;

  AlertView alertView;

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
    //先用这个，省时间
    final List<String> tags = Arrays.asList(imageResult.getTags().split(" "));
    System.out.println("tags = " + tags);
    String tilte = "Relevant tags";

    View actionsheet = LayoutInflater.from(this).inflate(R.layout.internal_actionsheet, null);
    ListView listView = ((ListView) actionsheet.findViewById(R.id.internal_actionsheet_list));
    listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tags));
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MainActivity.startActivity(DetailedActivity.this, tags.get(position));
      }
    });
    TextView textView = ((TextView) actionsheet.findViewById(R.id.internal_actionsheet_title));
    textView.setText(tilte);
    TextView cancel = ((TextView) actionsheet.findViewById(R.id.internal_sheet_cancel));
    View view = actionsheet.findViewById(R.id.internal_actionsheet_bg);

    final AlertDialog builder =
        new AlertDialog.Builder(this, R.style.ActionSheet).setCancelable(true).setView(actionsheet).create();

    view.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (builder != null) {
          builder.dismiss();
        }
      }
    });
    cancel.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (builder != null) {
          builder.dismiss();
        }
      }
    });

    //builder.getWindow().
    builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    //WindowManager.LayoutParams params = builder.getWindow().getAttributes();
    //params.gravity = Gravity.BOTTOM;
    //params.horizontalMargin = 0;
    //params.verticalMargin = 0;
    //params.width = WindowManager.LayoutParams.MATCH_PARENT;
    //params.height = 1200;
    builder.show();
  }

  @OnClick(R.id.iv_detailed_card) void download(final ImageView v) {
    final File file = getFilesDir();
    Log.d(TAG, file.toString());
    PhotoViewActivity.startScaleActivity(v.getContext(), Position.from(v));
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

                    anim(ivDetailedCard, getPosition(getIntent()), true,
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
  void anim(View view, Position position, boolean isEnter,
      Animation.AnimationListener listener) {
    //记住括号哦，我这里调试了一小时
    float delta = ((float) (position.width)) / ((float) (position.heigth));
    float fromDelta, toDelta, fromY, toY;
    float delt_Y=position.top ;
    float delt_X = position.left - view.getLeft();
    if (isEnter) {
      fromDelta = 1f;
      toDelta = delta;
      fromY = delt_Y;
      toY = 0;
    } else {
      fromDelta = delta;
      toDelta = 1f;
      fromY = 0;
      toY = delt_Y;
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

  @Override public void onBackPressed() {
    if (alertView != null && alertView.isShowing()) {
      alertView.dismiss();
      return;
    }

    anim(ivDetailedCard, getPosition(getIntent()), false, new Animation.AnimationListener() {
      @Override public void onAnimationStart(Animation animation) {
        mLlDetailedDownloads.animate().alpha(0f).setDuration(AnimateUtils.ANIM_DORITION).start();
        AnimateUtils.animateViewBitmap(ivDetailedCardBlur, null);
      }

      @Override public void onAnimationEnd(Animation animation) {
        DetailedActivity.super.onBackPressed();
        overridePendingTransition(0, 0);
      }

      @Override public void onAnimationRepeat(Animation animation) {

      }
    });
  }
}
