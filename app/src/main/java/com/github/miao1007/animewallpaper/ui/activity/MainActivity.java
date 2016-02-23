package com.github.miao1007.animewallpaper.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.miao1007.animewallpaper.R;
import com.github.miao1007.animewallpaper.support.api.konachan.ImageRepo;
import com.github.miao1007.animewallpaper.support.api.konachan.ImageResult;
import com.github.miao1007.animewallpaper.ui.adapter.BaseAdapter;
import com.github.miao1007.animewallpaper.ui.adapter.CardAdapter;
import com.github.miao1007.animewallpaper.ui.widget.ExitAlertDialog;
import com.github.miao1007.animewallpaper.ui.widget.NavigationBar;
import com.github.miao1007.animewallpaper.ui.widget.Position;
import com.github.miao1007.animewallpaper.ui.widget.SettingDialog;
import com.github.miao1007.animewallpaper.ui.widget.blur.BlurDrawable;
import com.github.miao1007.animewallpaper.utils.LogUtils;
import com.github.miao1007.animewallpaper.utils.SquareUtils;
import com.github.miao1007.animewallpaper.utils.StatusBarUtils;
import com.squareup.picasso.Picasso;
import java.net.SocketException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
    implements CardAdapter.OnItemClickListener, BaseAdapter.OnLoadMoreListener {

  public static final String TAG = LogUtils.makeLogTag(MainActivity.class);
  static final String EXTRA_MAP = "ext";
  //@Bind(R.id.btn_retry_card) Button mButton;
  //@Bind(R.id.pgb_loading_card) ContentLoadingProgressBar mProgressBar;
  boolean isLoadingMore;
  @Bind(R.id.navigation_bar) NavigationBar mNavigationBar;
  @Bind(R.id.rv_frag_card) RecyclerView mRvFragCard;
  BlurDrawable drawable;
  //void multiple dynamic proxy
  ImageRepo repo = SquareUtils.getRetrofit().create(ImageRepo.class);
  @Bind(R.id.card_holder) FrameLayout mCardHolder;
  @Bind(R.id.card_error_page) RelativeLayout mCardErrorPage;
  //@Bind(R.id.search_bar) SearchBar mSearchBar;
  private Map<String, Object> query = new HashMap<>(4);

  public static void startRefreshActivity(Context context, String query) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.putExtra(EXTRA_MAP, query);
    context.startActivity(intent);
  }

  static String parseIntent(Intent intent) {
    return intent.getStringExtra(EXTRA_MAP);
  }

  boolean in = true;

  @OnClick(R.id.iv_github) void settings(View v) {
    //String url = "http://github.com/miao1007/AnimeWallpaper";
    //Intent i = new Intent(Intent.ACTION_VIEW);
    //i.setData(Uri.parse(url));
    //try {
    //  startActivity(i);
    //  //some device don't have a browser
    //} catch (ActivityNotFoundException e) {
    //  Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
    //}
    //in = !in;
    new SettingDialog(getWindow()).show();
  }

  @OnClick(R.id.iv_search) void iv_search(View v) {
    startActivity(new Intent(this, SearchActivity.class));
  }

  @OnClick(R.id.error_page_refresh) void error_page_refresh() {
    mCardErrorPage.setVisibility(View.GONE);
    mRvFragCard.setVisibility(View.VISIBLE);
    onRefresh();
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    StatusBarUtils.from(this).setTransparentStatusbar(true).setLightStatusBar(true).process();
    setContentView(R.layout.fragment_card);
    ButterKnife.bind(this);
    setUpList();
    String tag = parseIntent(getIntent());
    if (tag != null) {
      query.clear();
      query.put(ImageRepo.TAGS, tag + ImageRepo.TAG_SAFE);
    }
  }

  private void setUpList() {
    CardAdapter mAdapter = new CardAdapter();
    mAdapter.setLoadMoreListener(this);
    mAdapter.setItemClickListener(this);
    final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    mRvFragCard.setLayoutManager(mLayoutManager);
    mRvFragCard.setAdapter(mAdapter);
    mRvFragCard.post(new Runnable() {
      @Override public void run() {
        int padding = mNavigationBar.getHeight() + StatusBarUtils.getStatusBarOffsetPx(
            getApplicationContext());
        mRvFragCard.setPadding(0, padding, 0, 0);
      }
    });
    mRvFragCard.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
          Picasso.with(recyclerView.getContext()).resumeTag(TAG);
        } else {
          Picasso.with(recyclerView.getContext()).pauseTag(TAG);
        }
      }

      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        ViewCompat.postInvalidateOnAnimation(mNavigationBar);
      }
    });
    drawable = new BlurDrawable(mRvFragCard);
    mNavigationBar.setBackgroundDrawable(drawable);
    onRefresh();
  }

  void loadPage(Map<String, Object> query) {
    if (isLoadingMore) {
      return;
    }
    if (mNavigationBar != null) {
      mNavigationBar.setProgressBar(true);
    }
    if (query == null) {
      query = new HashMap<>();
    }
    if ((query.isEmpty()) || (!query.containsKey(ImageRepo.TAGS))) {
      query.put(ImageRepo.TAGS, ImageRepo.TAG_SAFE);
      query.put(ImageRepo.LIMIT, 10);
    }
    repo.getImageList(query)
        .subscribeOn(Schedulers.io())
        .flatMap(new Func1<List<ImageResult>, Observable<ImageResult>>() {
          @Override public Observable<ImageResult> call(List<ImageResult> imageResults) {
            return Observable.from(imageResults);
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<ImageResult>() {
          @Override public void onCompleted() {
            if (mRvFragCard != null && mRvFragCard.getAdapter() != null) {
              mRvFragCard.getAdapter().notifyDataSetChanged();
            }
            if (mNavigationBar != null) {
              mNavigationBar.setProgressBar(false);
            }
            isLoadingMore = false;
            mCardErrorPage.setVisibility(View.INVISIBLE);
          }

          @Override public void onError(Throwable e) {
            e.printStackTrace();
            if (mNavigationBar != null) {
              mNavigationBar.setProgressBar(false);
            }
            isLoadingMore = false;
            mRvFragCard.setVisibility(View.GONE);
            mCardErrorPage.setVisibility(View.VISIBLE);
            //fix bugs on gfw
            if (e instanceof SocketException) {
              Toast.makeText(MainActivity.this, R.string.please_try_proxy, Toast.LENGTH_SHORT)
                  .show();
              return;
            }
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
          }

          @Override public void onNext(ImageResult imageResult) {
            ((CardAdapter) mRvFragCard.getAdapter()).getData().add(imageResult);
          }
        });
  }

  @Override public void onItemClick(View v, int position) {
    List<ImageResult> imageResult = ((CardAdapter) mRvFragCard.getAdapter()).getData();
    if (imageResult == null || imageResult.isEmpty() || imageResult.get(position) == null) {
      //mi ui bug
      return;
    }
    DetailedActivity.startActivity(v.getContext(), Position.from(v), imageResult.get(position));
  }

  //swipe layout refresh callback
  public void onRefresh() {
    Log.d(TAG, "onRefresh:query = " + query);
    ((CardAdapter) mRvFragCard.getAdapter()).getData().clear();
    mRvFragCard.getAdapter().notifyDataSetChanged();
    loadMore(1);
  }

  @Override public void loadMore(int page) {
    Log.d(TAG, "loadMore:" + query);
    query.put(ImageRepo.PAGE, page);
    loadPage(query);//这里多线程也要手动控制isLoadingMore
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (drawable != null) {
      drawable.onDestroy();
    }
  }

  /**
   * 加载渲染引擎比较耗时，但是把引擎单例话，非常不好管理
   */
  @Override public void onBackPressed() {
    final ExitAlertDialog dialog = new ExitAlertDialog(this, new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });
    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
      @Override public void onCancel(DialogInterface dialog) {
        drawable.setCornerRadius(0);
        drawable.setDrawOffset(0, 0);
      }
    });
    dialog.getWindow().getDecorView().post(new Runnable() {
      @Override public void run() {
        drawable.setCornerRadius(getResources().getDimension(R.dimen.internal_searchbar_radius));
        drawable.setDrawOffset(
            (getWindow().getDecorView().getWidth() - dialog.getWindow().getDecorView().getWidth())
                / 2,
            (getWindow().getDecorView().getHeight() - dialog.getWindow().getDecorView().getHeight()
                + StatusBarUtils.getStatusBarOffsetPx(getApplicationContext())) / 2/*Gravity.Center contains statusbar*/);
        //equals to dialog.getWindow().geDecoView.setBackgroundDrawable(drawable);
        dialog.getWindow().setBackgroundDrawable(drawable);
      }
    });
    dialog.show();
  }
}

