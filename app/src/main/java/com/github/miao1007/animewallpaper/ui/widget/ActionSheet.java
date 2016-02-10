package com.github.miao1007.animewallpaper.ui.widget;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.github.miao1007.animewallpaper.R;
import com.github.miao1007.animewallpaper.utils.LogUtils;
import java.util.List;

/**
 * Created by leon on 1/27/16.
 * ActionSheet with blur
 */
public class ActionSheet extends BlurDialog {


  String title;
  static final String TAG = LogUtils.makeLogTag(ActionSheet.class);
  AdapterView.OnItemClickListener listener;
  List<String> tags;

  private int[] anim = { dialogHeightPx(), 0 };

  public ActionSheet(Activity activity, String title,
      @Nullable AdapterView.OnItemClickListener listener, List<String> tags) {
    super(activity, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
    this.title = title;
    this.listener = listener;
    this.tags = tags;

  }

  /**
   * Load views from xml
   */
  @Override protected View inflateDialogView() {
    Log.d(TAG, "inflateDialogView");
    View actionsheet = getLayoutInflater().inflate(R.layout.internal_actionsheet, null, false);
    final ListView listView = ((ListView) actionsheet.findViewById(R.id.internal_actionsheet_list));
    listView.setAdapter(new BlueAdapter(getContext(), android.R.layout.simple_list_item_1, tags));
    TextView tv_title = ((TextView) actionsheet.findViewById(R.id.internal_actionsheet_title));
    TextView tv_cancel = ((TextView) actionsheet.findViewById(R.id.internal_sheet_cancel));

    tv_title.setText(title);
    if (listener != null) {
      listView.setOnItemClickListener(listener);
    }
    tv_cancel.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ActionSheet.this.dismiss();
      }
    });
    return actionsheet;
  }

  @Override protected void onSetWindowAttrs(Window w) {
    w.setBackgroundDrawableResource(android.R.color.transparent);
    w.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeightPx());
    w.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
    w.setDimAmount(0.5f);
    w.setWindowAnimations(0);
  }

  protected ObjectAnimator animationFactory(final View view, boolean in) {
    return ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, in ? (anim[0]) : (anim[1]),
        !in ? (anim[0]) : (anim[1]));
  }

  @Override protected ObjectAnimator getInAnimator(View view) {
    return animationFactory(view, true);
  }

  @Override protected ObjectAnimator getOutAnimator(View view) {
    return animationFactory(view, false);
  }

  /**
   * adapter for tags in ActionSheet
   */
  static class BlueAdapter extends ArrayAdapter {

    public BlueAdapter(Context context, int resource) {
      super(context, resource);
    }

    public BlueAdapter(Context context, int resource, List<String> objects) {
      super(context, resource, objects);
    }

    public BlueAdapter(Context context, int resource, Object[] objects) {
      super(context, resource, objects);
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
