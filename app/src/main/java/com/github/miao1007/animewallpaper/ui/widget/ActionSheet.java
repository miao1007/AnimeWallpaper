package com.github.miao1007.animewallpaper.ui.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.github.miao1007.animewallpaper.R;
import com.github.miao1007.animewallpaper.ui.activity.MainActivity;
import com.github.miao1007.animewallpaper.utils.LogUtils;
import java.util.Arrays;
import java.util.List;

/**
 * Created by leon on 1/27/16.
 */
public class ActionSheet extends DialogFragment {


  String title;
  static final String TAG = LogUtils.makeLogTag(ActionSheet.class);
  AdapterView.OnItemClickListener listener;
  List<String> tags;
  BlurDrawable drawable;
  View actionsheet;

  //public ActionSheet(String title, List<String> tags) {
  //  this.title = title;
  //  this.tags = tags;
  //}

  public static ActionSheet newInstance(String title, String[] tags) {
    ActionSheet dialog = new ActionSheet();
    Bundle args = new Bundle();
    args.putString("title", title);
    args.putStringArray("tags", tags);
    dialog.setArguments(args);
    return dialog;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setStyle(STYLE_NO_TITLE, android.R.style.Theme_Dialog);
  }

  /**
   * wtf: settings won't work on onCreateDialog or xml
   */
  @Override public void onStart() {
    super.onStart();
    Window w = getDialog().getWindow();
    w.setBackgroundDrawableResource(android.R.color.transparent);
    w.setLayout(dialogWidthPx(), dialogHeightPx());
    w.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
    w.setDimAmount(0.5f);
    w.setWindowAnimations(0);
  }

  protected DisplayMetrics getDisplayMetrics() {
    return getResources().getDisplayMetrics();
  }

  protected int dialogWidthPx() {
    return Math.min(getDisplayMetrics().widthPixels,
        (int) getResources().getDimension(R.dimen.internal_actionsheet_height));
  }

  private int dialogHeightPx() {
    return (int) getResources().getDimension(R.dimen.internal_actionsheet_height);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflateDialogView(inflater, container);
  }

  /**
   * 这里的window与activity中的windows不一样，虽然在底部显示，但是从0开始
   */
  protected View inflateDialogView(LayoutInflater inflater, ViewGroup container) {
    Log.d(TAG, "inflateDialogView");
    title = getArguments().getString("title");
    tags = Arrays.asList(getArguments().getStringArray("tags"));

    actionsheet = inflater.inflate(R.layout.internal_actionsheet, container, false);
    final ListView listView = ((ListView) actionsheet.findViewById(R.id.internal_actionsheet_list));
    listView.setAdapter(new BlueAdapter(getContext(), android.R.layout.simple_list_item_1, tags));
    TextView tv_title = ((TextView) actionsheet.findViewById(R.id.internal_actionsheet_title));
    TextView tv_cancel = ((TextView) actionsheet.findViewById(R.id.internal_sheet_cancel));

    tv_title.setText(title);

    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MainActivity.startRefreshActivity(getContext(), tags.get(position));
      }
    });
    tv_cancel.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ActionSheet.this.dismiss();
      }
    });
    drawable = new BlurDrawable(getActivity());
    //eg. 720 - 360 = space top
    final int del = getActivity().getWindow().getDecorView().getHeight() - dialogHeightPx();

    //drawable.setDrawOffset(0, del);
    drawable.setOverlayColor(Color.argb(0xae, 0xff, 0xff, 0xff));
    actionsheet.setBackgroundDrawable(drawable);
    ObjectAnimator animator =
        ObjectAnimator.ofFloat(actionsheet, View.TRANSLATION_Y, dialogHeightPx(), 0);
    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        //360dp -> 0
        //(720 -360) -> (720 - 0)
        drawable.setDrawOffset(0, del + actionsheet.getTranslationY());
        actionsheet.invalidate();
      }
    });
    animator.start();
    return actionsheet;
  }

  @Override public void onDismiss(DialogInterface dialog) {
    if (actionsheet != null) {
      ObjectAnimator animator =
          ObjectAnimator.ofFloat(actionsheet, View.TRANSLATION_Y, 0, dialogHeightPx());
      final int del = getActivity().getWindow().getDecorView().getHeight() - dialogHeightPx();

      animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        @Override public void onAnimationUpdate(ValueAnimator animation) {
          //360dp -> 0
          //(720 -360) -> (720 - 0)
          drawable.setDrawOffset(0, del + actionsheet.getTranslationY());
          actionsheet.invalidate();
        }
      });
      animator.start();
    }
    super.onDismiss(dialog);
  }

  @Override public void onDetach() {
    super.onDetach();
    if (drawable != null) {
      drawable.onDestroy();
    }
  }

  static class BlueAdapter extends ArrayAdapter {

    public BlueAdapter(Context context, int resource) {
      super(context, resource);
    }

    public BlueAdapter(Context context, int resource, List objects) {
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
