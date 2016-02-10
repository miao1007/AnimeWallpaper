package com.github.miao1007.animewallpaper.ui.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
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
import com.github.miao1007.animewallpaper.utils.LogUtils;
import java.util.List;

/**
 * Created by leon on 1/27/16.
 */
public class ActionSheet extends Dialog {


  String title;
  static final String TAG = LogUtils.makeLogTag(ActionSheet.class);
  AdapterView.OnItemClickListener listener;
  List<String> tags;
  BlurDrawable drawable;
  View actionsheet;
  Window blurredWindow;
  private int[] anim = { dialogHeightPx(), 0 };

  public ActionSheet(Window blurredWindow, String title,
      @Nullable AdapterView.OnItemClickListener listener, List<String> tags) {
    super(blurredWindow.getContext(), android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
    this.title = title;
    this.listener = listener;
    this.tags = tags;
    this.blurredWindow = blurredWindow;
  }

  /**
   * Called when the dialog is starting.
   * set dialog window attrs.
   * Note this window is a new window, not belongs to Activity
   */
  @Override public void onStart() {
    super.onStart();
    Window w = getWindow();
    w.setBackgroundDrawableResource(android.R.color.transparent);
    w.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeightPx());
    w.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
    w.setDimAmount(0.5f);
    w.setWindowAnimations(0);
  }

  /**
   * Load content view
   */
  @Override public void show() {
    getWindow().setContentView(inflateDialogView(getLayoutInflater(), null));
    super.show();
  }

  @Override public void dismiss() {
    Animator dismiss = loadAnimation(actionsheet, false);
    dismiss.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animation) {

      }

      @Override public void onAnimationEnd(Animator animation) {
        ActionSheet.super.dismiss();
      }

      @Override public void onAnimationCancel(Animator animation) {

      }

      @Override public void onAnimationRepeat(Animator animation) {

      }
    });
    dismiss.start();
  }

  protected DisplayMetrics getDisplayMetrics() {
    return getContext().getResources().getDisplayMetrics();
  }

  private int dialogHeightPx() {
    return (int) getContext().getResources().getDimension(R.dimen.internal_actionsheet_height);
  }

  /**
   * Load views from xml
   */
  protected View inflateDialogView(LayoutInflater inflater, ViewGroup container) {
    Log.d(TAG, "inflateDialogView");

    actionsheet = inflater.inflate(R.layout.internal_actionsheet, container, false);
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
    drawable = new BlurDrawable(blurredWindow.getDecorView());
    //drawable.setBlurRadius(2);
    //drawable.setDownsampleFactor(2);
    drawable.setOverlayColor(Color.argb(0xae, 0xff, 0xff, 0xff));
    actionsheet.setBackgroundDrawable(drawable);
    loadAnimation(actionsheet, true).start();
    return actionsheet;
  }

  /**
   * the offset between blurredWindow and dialog
   */
  protected int getWindowOffset() {
    return blurredWindow.getDecorView().getHeight() - getWindow().getDecorView().getHeight();
  }

  /**
   * Translation animation of root view
   */
  protected ObjectAnimator loadAnimation(View view, boolean in) {
    ObjectAnimator animator =
        ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, in ? (anim[0]) : (anim[1]),
            in ? (anim[1]) : (anim[0]));
    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        drawable.setDrawOffset(0, getWindowOffset() + actionsheet.getTranslationY());
        actionsheet.invalidate();
      }
    });
    return animator;
  }

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
