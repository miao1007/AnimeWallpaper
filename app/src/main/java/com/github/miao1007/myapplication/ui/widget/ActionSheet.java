package com.github.miao1007.myapplication.ui.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.miao1007.myapplication.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by leon on 1/19/16.
 */
public class ActionSheet extends LinearLayout{

  @Bind(R.id.internal_actionsheet_title) TextView mTvActionsheetTitle;
  //@Bind(R.id.internal_actionsheet_list)
  ListView mRvActionsheetList;
  String title;
  String[] items;
  @Bind(R.id.internal_sheet_cancel) TextView mInternalSheetCancel;
  ArrayAdapter<String> arrayAdapter;
  AdapterView.OnItemClickListener listener;

  @OnClick(R.id.internal_sheet_cancel) void cancel() {
    if(listener != null){
      listener.onItemClick(null, null, -1, 0);
    }
  }

  public ActionSheet(Context context) {
    this(context, null, 0);
  }

  public ActionSheet(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ActionSheet(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initView();
  }

  //public static void show(Context context,String title,String... items) {
  //  if (context instanceof Activity) {
  //    //top activity
  //    ViewGroup deco = (ViewGroup) ((Activity) context).getWindow()
  //        .getDecorView()
  //        .findViewById(android.R.id.content);
  //    View view = LayoutInflater.from(context).inflate(R.layout.internal_actionsheet, deco, false);
  //    ((TextView) view.findViewById(R.id.internal_actionsheet_title)).setText(title);
  //    ((ListView) view.findViewById(R.id.internal_actionsheet_list)).setAdapter(
  //        new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,items));
  //    deco.addView(view);
  //  }
  //}

  public void setTitle(@NonNull String title) {
    if (mTvActionsheetTitle != null) {
      mTvActionsheetTitle.setText(title);
    }
  }

  public void setTitle(@StringRes int text) {
    if (mTvActionsheetTitle != null) {
      mTvActionsheetTitle.setText(text);
    }
  }

  public void setItems(@Nullable List<String> strings) {
    if (arrayAdapter != null && strings != null) {
      this.arrayAdapter.clear();
      this.arrayAdapter.addAll(strings);
      arrayAdapter.notifyDataSetChanged();
    }
  }

  //public void dismiss(final Runnable runnable){
  //  this.post(new Runnable() {
  //    @Override public void run() {
  //      //ActionSheet.this.getParent()
  //      AlertDialog
  //     runnable.run();
  //    }
  //  });
  //}

  public void setOnItemClicklistener(AdapterView.OnItemClickListener listener) {
    if (mRvActionsheetList != null) {
      this.listener = listener;
      mRvActionsheetList.setOnItemClickListener(listener);
    }
  }

  void initView() {
    View.inflate(getContext(), R.layout.internal_actionsheet, this);
    ButterKnife.bind(this);
    arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, new ArrayList<String>());
    mRvActionsheetList.setAdapter(arrayAdapter);
  }
}
