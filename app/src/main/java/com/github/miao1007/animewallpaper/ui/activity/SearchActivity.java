package com.github.miao1007.animewallpaper.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.github.miao1007.animewallpaper.R;
import com.github.miao1007.animewallpaper.support.api.konachan.ImageRepo;
import com.github.miao1007.animewallpaper.support.api.konachan.Tag;
import com.github.miao1007.animewallpaper.ui.widget.SearchBar;
import com.github.miao1007.animewallpaper.utils.LogUtils;
import com.github.miao1007.animewallpaper.utils.SquareUtils;
import com.github.miao1007.animewallpaper.utils.StatusbarUtils;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {

  static final String TAG = LogUtils.makeLogTag(SearchActivity.class);

  @Bind(R.id.search_bar) SearchBar mSearchbar;
  @Bind(R.id.search_list) ListView mSearchListView;
  ImageRepo repo = SquareUtils.getRetrofit().create(ImageRepo.class);

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);
    ButterKnife.bind(this);
    StatusbarUtils.from(this)
        .setActionbarView(mSearchbar)
        .setTransparentStatusbar(true)
        .setLightStatusBar(true)
        .process();
    final ArrayList<Tag> arrayList = new ArrayList<>();
    final ResultAdapter adapter = new ResultAdapter(this, arrayList);
    mSearchListView.setAdapter(adapter);
    mSearchListView.post(new Runnable() {
      @Override public void run() {
        mSearchListView.setPadding(0,
            mSearchbar.getHeight() + StatusbarUtils.getStatusBarOffsetPx(getApplicationContext()),
            0, 0);
      }
    });
    mSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MainActivity.startRefreshActivity(SearchActivity.this, arrayList.get(position).getName());
      }
    });
    //mSearchbar.toggle(true);
    mSearchbar.setOnButton(new Runnable() {
      @Override public void run() {
        finish();
      }
    });
    /**
     * Port from {@link https://github.com/ReactiveX/RxSwift}
     */
    RxTextView.textChanges(mSearchbar.getmInternalEtSearch())
        //delay 500ms
        .throttleWithTimeout(300, TimeUnit.MICROSECONDS)
        .distinctUntilChanged()
        .filter(new Func1<CharSequence, Boolean>() {
          @Override public Boolean call(CharSequence charSequence) {
            //void unnecessary request
            return charSequence.length() != 0;
          }
        })
        .map(new Func1<CharSequence, String>() {
          @Override public String call(CharSequence charSequence) {
            //fit network api require
            return charSequence + "*";
          }
        })
        .subscribeOn(AndroidSchedulers.mainThread())
        .flatMap(
            new Func1<String, Observable<List<Tag>>>() {
          @Override public Observable<List<Tag>> call(String s) {
            return repo.getTags(10, s);
          }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<Tag>>() {
          @Override public void onCompleted() {

          }

          @Override public void onError(Throwable e) {
            e.printStackTrace();
            if (!(e instanceof InterruptedException)) {
              Toast.makeText(SearchActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }

          @Override public void onNext(List<Tag> tags) {

            arrayList.clear();
            arrayList.addAll(tags);
            adapter.notifyDataSetChanged();
          }
        });

  }

  @Override protected void onPause() {
    super.onPause();
    if (isFinishing()) {
      overridePendingTransition(0, 0);
    }
  }

  static class ResultAdapter extends BaseAdapter {

    Context context;
    List<Tag> tags;

    public ResultAdapter(Context context, List<Tag> tags) {
      this.context = context;
      this.tags = tags;
    }

    @Override public int getCount() {
      return tags.size();
    }

    @Override public Object getItem(int position) {
      return tags.get(position);
    }

    @Override public long getItemId(int position) {
      return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
      //if (convertView == null) {
      //  convertView =
      //      LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, null, false);
      //}
      convertView =
          LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, null, false);
      ((TextView) convertView).setText(tags.get(position).getName());
      return convertView;
    }
  }
}
