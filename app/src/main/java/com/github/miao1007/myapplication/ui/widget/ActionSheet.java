package com.github.miao1007.myapplication.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.InjectView;
import com.github.miao1007.myapplication.R;

/**
 * Created by leon on 1/19/16.
 */
public class ActionSheet extends LinearLayout {

  @InjectView(R.id.internal_actionsheet_title) TextView mTvActionsheetTitle;
  @InjectView(R.id.internal_actionsheet_list) ListView mRvActionsheetList;
  String title;
  String[] items;

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

  public static void show(Context context,String title,String... items) {
    if (context instanceof Activity) {
      //top activity
      ViewGroup deco = (ViewGroup) ((Activity) context).getWindow()
          .getDecorView()
          .findViewById(android.R.id.content);
      View view = LayoutInflater.from(context).inflate(R.layout.internal_actionsheet, deco, false);
      ((TextView) view.findViewById(R.id.internal_actionsheet_title)).setText(title);
      ((ListView) view.findViewById(R.id.internal_actionsheet_list)).setAdapter(
          new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,items));

      deco.addView(view);
    }
  }

  public void setTitle(String title) {
    if (mTvActionsheetTitle != null) {
      mTvActionsheetTitle.setText(title);
    }
  }

  void initView() {
    LayoutInflater.from(getContext()).inflate(R.layout.internal_actionsheet, this, true);
    setTitle("Title");
    //setItems(new String[] { "11", "22", "33" });
  }
}
