package com.github.miao1007.animewallpaper.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.miao1007.animewallpaper.R;
import com.github.miao1007.animewallpaper.support.GlobalContext;
import com.github.miao1007.animewallpaper.support.api.ImageAdapter;
import com.github.miao1007.animewallpaper.support.api.konachan.DanbooruAPI;
import com.github.miao1007.animewallpaper.support.api.konachan.ImageResult;
import com.github.miao1007.animewallpaper.ui.adapter.BaseAdapter;
import com.github.miao1007.animewallpaper.ui.adapter.CardAdapter;
import com.github.miao1007.animewallpaper.ui.widget.ActionSheet;
import com.github.miao1007.animewallpaper.ui.widget.ExitAlertDialog;
import com.github.miao1007.animewallpaper.ui.widget.HistoryActionSheet;
import com.github.miao1007.animewallpaper.ui.widget.NavigationBar;
import com.github.miao1007.animewallpaper.ui.widget.Position;
import com.github.miao1007.animewallpaper.ui.widget.blur.BlurDrawable;
import com.github.miao1007.animewallpaper.utils.FileUtils;
import com.github.miao1007.animewallpaper.utils.LogUtils;
import com.github.miao1007.animewallpaper.utils.SquareUtils;
import com.github.miao1007.animewallpaper.utils.StatusBarUtils;
import com.google.gson.stream.MalformedJsonException;
import com.squareup.picasso.Picasso;
import im.fir.sdk.FIR;
import java.io.File;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity
    implements CardAdapter.OnItemClickListener, BaseAdapter.OnLoadMoreListener {

  private static final String TAG = LogUtils.makeLogTag(MainActivity.class);
  private static final String EXTRA_MAP = "ext";
  private final Map<String, Object> query = new HashMap<>(4);
  @BindView(R.id.navigation_bar) NavigationBar mNavigationBar;
  @BindView(R.id.rv_frag_card) RecyclerView mRvFragCard;
  @BindView(R.id.card_holder) FrameLayout mCardHolder;
  @BindView(R.id.card_error_page) RelativeLayout mCardErrorPage;
  //void multiple dynamic proxy
  private DanbooruAPI repo;
  private boolean isLoadingMore;
  private BlurDrawable drawable;

  public static void startRefreshActivity(Context context, String query) {
    Intent intent = new Intent(context, MainActivity.class);
    //destroy caller activity and this activity and recreate a new activity
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.putExtra(EXTRA_MAP, query);
    context.startActivity(intent);
  }

  private static String parseIntent(Intent intent) {
    return intent.getStringExtra(EXTRA_MAP);
  }

  @OnClick(R.id.iv_history) void settings(View v) {
    final File file = new File(FileUtils.EXT_STORAGE);
    final ActionSheet a =
        new HistoryActionSheet(getWindow(), new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final Intent shareIntent = new Intent(Intent.ACTION_VIEW);
            shareIntent.setDataAndType(Uri.fromFile(file.listFiles()[position]), "image/*");
            startActivity(Intent.createChooser(shareIntent, getString(R.string.view_image_by)));
          }
        }, file);
    a.setDrawable(drawable);
    a.show();
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

    SquareUtils.getDispatcher().executorService().execute(new Runnable() {
      @WorkerThread @Override public void run() {
        //116ms
        GlobalContext.startThirdFrameWork();
        //120ms
        repo = SquareUtils.getRetrofit(DanbooruAPI.KONACHAN).create(DanbooruAPI.class);
        runOnUiThread(new Runnable() {
          @Override public void run() {
            //using repo to load pages
            onRefresh();
          }
        });
      }
    });
    super.onCreate(savedInstanceState);
    //draw views using WMS
    setContentView(R.layout.fragment_card);
    ButterKnife.bind(this);
    StatusBarUtils.from(this)
        .setLightStatusBar(true)
        .setTransparentStatusbar(true)
        .setTransparentNavigationbar(true)
        .setActionbarView(mNavigationBar)
        .process();
    setUpList();
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
    drawable = new BlurDrawable(mCardHolder);

    mNavigationBar.setBackgroundDrawable(drawable);

    String tag = parseIntent(getIntent());
    if (tag != null) {
      query.clear();
      query.put(DanbooruAPI.TAGS, tag + DanbooruAPI.TAG_SAFE);
    }
  }

  private void loadPage(Map<String, Object> query) {
    if (isLoadingMore) {
      return;
    }
    if (mNavigationBar != null) {
      mNavigationBar.setProgressBar(true);
    }
    if (query == null) {
      query = new HashMap<>();
    }
    if ((query.isEmpty()) || (!query.containsKey(DanbooruAPI.TAGS))) {
      query.put(DanbooruAPI.TAGS, DanbooruAPI.TAG_SAFE);
      query.put(DanbooruAPI.LIMIT, 10);
    }
    repo.getImageList(query)
        .subscribeOn(SquareUtils.getRxWorkerScheduler())
        .flatMap(new Func1<List<ImageResult>, Observable<ImageResult>>() {
          @Override public Observable<ImageResult> call(List<ImageResult> imageResults) {
            return Observable.from(imageResults);
          }
        })
        .map(new Func1<ImageResult, ImageAdapter>() {
          @Override public ImageAdapter call(ImageResult result) {
            return ImageAdapter.from(result);
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<ImageAdapter>() {
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
            //fix bugs on chinaNet
            if (e instanceof MalformedJsonException) {
              Toast.makeText(MainActivity.this, R.string.server_err_response, Toast.LENGTH_SHORT)
                  .show();
              return;
            }

            //fix bugs on gfw
            //ConnectException << SocketException
            if (e instanceof SocketException  | e instanceof UnknownHostException) {
              Toast.makeText(MainActivity.this, R.string.please_try_proxy, Toast.LENGTH_SHORT)
                  .show();
              FIR.addCustomizeValue("SOCKET", "can't connect by xxx");
              FIR.sendCrashManually(e);
            }
          }

          @Override public void onNext(ImageAdapter imageResult) {
            ((CardAdapter) mRvFragCard.getAdapter()).getData().add(imageResult);
          }
        });
  }

  @Override public void onItemClick(View v, int position) {
    List<ImageAdapter> imageResult = ((CardAdapter) mRvFragCard.getAdapter()).getData();
    DetailedActivity.startActivity(v.getContext(), Position.from(v), imageResult.get(position));
  }

  //swipe layout refresh callback
  @MainThread private void onRefresh() {
    Log.d(TAG, "onRefresh:query = " + query);
    ((CardAdapter) mRvFragCard.getAdapter()).getData().clear();
    mRvFragCard.getAdapter().notifyDataSetChanged();
    loadMore(1);
  }

  @Override public void loadMore(int page) {
    Log.d(TAG, "loadMore:" + query);
    query.put(DanbooruAPI.PAGE, page);
    loadPage(query);//这里多线程也要手动控制isLoadingMore
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "onDestroy");
    // FIXME: 5/12/16 Calling RS with no Context active.
    //if (drawable != null) {
    //  drawable.onDestroy();
    //}
  }

  /**
   * reuse drawable
   */
  @Override public void onBackPressed() {
    mRvFragCard.stopScroll();

    final ExitAlertDialog dialog = new ExitAlertDialog(this, new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });
    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
      @Override public void onCancel(DialogInterface dialog) {
        drawable.setCornerRadius(0);
        drawable.setDrawOffset(0, 0);
        //restore dialog height to origin
        drawable.setBounds(0, 0, mNavigationBar.getWidth(), mNavigationBar.getHeight());
      }
    });
    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
      @Override public void onShow(DialogInterface dialog) {
        //void random bug on screen
        //mRvFragCard.stopNestedScroll();
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

