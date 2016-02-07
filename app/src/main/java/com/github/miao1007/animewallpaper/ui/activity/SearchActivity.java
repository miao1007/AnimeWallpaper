package com.github.miao1007.animewallpaper.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.github.miao1007.animewallpaper.R;
import com.github.miao1007.animewallpaper.support.api.konachan.ImageRepo;
import com.github.miao1007.animewallpaper.support.api.konachan.Tag;
import com.github.miao1007.animewallpaper.ui.widget.SearchBar;
import com.github.miao1007.animewallpaper.utils.StatusbarUtils;
import com.github.miao1007.animewallpaper.utils.picasso.SquareUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {

  @Bind(R.id.search_bar) SearchBar mSearchbar;
  @Bind(R.id.search_list) ListView mSearchListView;

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
    mSearchbar.setTextListener(new SearchBar.TextListener() {
      @Override public void onTextInput(Editable editable) {
        Observable.just(editable)
            .debounce(2, TimeUnit.SECONDS)
            .map(new Func1<Editable, String>() {
              @Override public String call(Editable editable) {
                //return editable.toString() + '*';
                return "suzumiya";
              }
            })
            .flatMap(new Func1<String, Observable<List<Tag>>>() {
              @Override public Observable<List<Tag>> call(String s) {
                return SquareUtils.getRetrofit().create(ImageRepo.class).searchHint(10, s);
              }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<List<Tag>>() {
              @Override public void onCompleted() {

              }

              @Override public void onError(Throwable e) {

              }

              @Override public void onNext(List<Tag> tags) {

                arrayList.clear();
                arrayList.addAll(tags);
                adapter.notifyDataSetChanged();
              }
            });
      }
    });
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
