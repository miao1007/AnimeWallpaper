package com.github.miao1007.animewallpaper.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.miao1007.animewallpaper.R;
import com.github.miao1007.animewallpaper.utils.LogUtils;
import java.util.List;

/**
 * Created by leon on 1/27/16.
 * ActionSheet with blur
 */
public class ActionSheet extends Dialog {

  static final String TAG = LogUtils.makeLogTag(ActionSheet.class);
  private final AdapterView.OnItemClickListener listener;
  private final List<String> tags;
  @Bind(R.id.internal_actionsheet_title) TextView mInternalActionsheetTitle;
  @Bind(R.id.internal_actionsheet_list)  ListView listView;
  @Bind(R.id.internal_actionsheet_holder) RelativeLayout mInternalActionsheetHolder;
  @Bind(R.id.internal_sheet_cancel) TextView mInternalSheetCancel;
  @Bind(R.id.internal_actionsheet_bg) LinearLayout mInternalActionsheetBg;

  public ActionSheet(Window window, @Nullable AdapterView.OnItemClickListener listener,
      List<String> tags) {
    super(window.getContext(), android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
    this.listener = listener;
    this.tags = tags;
  }

  @OnClick(R.id.internal_sheet_cancel) void internal_sheet_cancel() {
    dismiss();
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.internal_actionsheet);
    ButterKnife.bind(this);
    listView.setAdapter(new BlueAdapter(getContext(), tags));
    if (listener != null) {
      listView.setOnItemClickListener(listener);
    }
    Window w = getWindow();
    w.setBackgroundDrawableResource(android.R.color.transparent);
    w.setWindowAnimations(R.style.ActionsheetAnimation);
    w.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeightPx());
    w.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
    w.setDimAmount(0.5f);
  }

  private int dialogHeightPx() {
    return (int) getContext().getResources().getDimension(R.dimen.internal_actionsheet_height);
  }

  /**
   * adapter for tags in ActionSheet
   */
  static class BlueAdapter extends ArrayAdapter<String> {

    public BlueAdapter(Context context, int resource) {
      super(context, resource);
    }

    public BlueAdapter(Context context, List<String> objects) {
      super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
      View view = super.getView(position, convertView, parent);
      if (view instanceof TextView) {
        ((TextView) view).setTextColor(
            getContext().getResources().getColor(R.color.ios_internal_blue));
      }
      return view;
    }
  }
}
