package com.github.miao1007.animewallpaper.ui.widget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.miao1007.animewallpaper.R;
import com.github.miao1007.animewallpaper.ui.widget.blur.BlurDrawable;
import com.github.miao1007.animewallpaper.utils.StatusBarUtils;

/**
 * Created by leon on 1/27/16.
 * ActionSheet with blur
 */
public abstract class ActionSheet extends Dialog {

  private final AdapterView.OnItemClickListener listener;
  @BindView(R.id.internal_actionsheet_title) TextView mInternalActionsheetTitle;
  @BindView(R.id.internal_actionsheet_list) ListView listView;
  private BlurDrawable drawable;
  //some device(Huawei honor) does not support android.R.color.transparnet
  private static ColorDrawable TRANSPARENT = new ColorDrawable(Color.TRANSPARENT);

  public ActionSheet(Window window, @Nullable AdapterView.OnItemClickListener listener) {
    super(window.getContext(), android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
    this.listener = listener;
  }

  public void setDrawable(BlurDrawable drawable) {
    this.drawable = drawable;
  }

  @OnClick(R.id.internal_sheet_cancel) void internal_sheet_cancel() {
    dismiss();
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.internal_actionsheet);
    ButterKnife.bind(this);
    listView.setAdapter(getAdapter());
    if (listener != null) {
      listView.setOnItemClickListener(listener);
    }
    Window w = getWindow();
    mInternalActionsheetTitle.setText(getTitle());
    w.setBackgroundDrawable(TRANSPARENT);
    w.setWindowAnimations(R.style.ActionsheetAnimation);
    w.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeightPx());
    w.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
    w.setDimAmount(0.5f);

    if (drawable == null) {
      return;
    }
    getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
      @Override
      public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
          int oldTop, int oldRight, int oldBottom) {
        drawable.setDrawOffset(0, drawable.getmBlurredBgView().getHeight()
            - getWindow().getDecorView().getHeight()
            - StatusBarUtils.getNavigationBarOffsetPx(getWindow().getContext()));
        getWindow().getDecorView()
            .findViewById(Window.ID_ANDROID_CONTENT)
            .setBackgroundDrawable(drawable);
        getWindow().setBackgroundDrawable(TRANSPARENT);
      }
    });
    setOnCancelListener(new DialogInterface.OnCancelListener() {
      @Override public void onCancel(DialogInterface dialog) {
        drawable.setCornerRadius(0);
        drawable.setDrawOffset(0, 0);
        //restore dialog height to origin
        //drawable.setBounds(0, 0, mNavigationBar.getWidth(), mNavigationBar.getHeight());
      }
    });
  }

  private int dialogHeightPx() {
    return (int) getContext().getResources().getDimension(R.dimen.internal_actionsheet_height);
  }

  @StringRes abstract public int getTitle();

  abstract public BaseAdapter getAdapter();
}
