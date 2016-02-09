package com.github.miao1007.animewallpaper.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
    setStyle(STYLE_NO_TITLE, R.style.LinkPreviewDialog);
  }

  @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog dialog = super.onCreateDialog(savedInstanceState);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    return dialog;
  }

  @Override public void onStart() {
    super.onStart();
    setWindowLayout();
  }

  protected DisplayMetrics getDisplayMetrics() {
    return getResources().getDisplayMetrics();
  }

  protected int dialogWidthPx() {
    return Math.min(getDisplayMetrics().widthPixels,
        (int) getResources().getDimension(R.dimen.swipeableDialogMaxWidth));
  }

  private int dialogHeightPx() {
    return 360 * 3;
  }

  private void setWindowLayout() {
    // HACK: height _should_ be ViewGroup.LayoutParams.MATCH_PARENT but it doesn't work after
    //       two orientation changes. i.e., turn the device 90 degrees and the height is
    //       correct. Then turn it back -90 degrees, the height is incorrect.
    getDialog().getWindow().setLayout(dialogWidthPx(), dialogHeightPx());
    getDialog().getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
    //getDialog().getWindow().ani
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflateDialogView(inflater, container);
  }

  protected View inflateDialogView(LayoutInflater inflater, ViewGroup container) {
    Log.d(TAG, "inflateDialogView");
    title = getArguments().getString("title");
    tags = Arrays.asList(getArguments().getStringArray("tags"));

    Log.d(TAG, title + "/" + tags.toString());
    View actionsheet = inflater.inflate(R.layout.internal_actionsheet, container, false);
    final ListView listView = ((ListView) actionsheet.findViewById(R.id.internal_actionsheet_list));
    listView.setAdapter(new BlueAdapter(getContext(), android.R.layout.simple_list_item_1, tags));
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MainActivity.startRefreshActivity(getContext(), tags.get(position));
      }
    });
    TextView textView = ((TextView) actionsheet.findViewById(R.id.internal_actionsheet_title));
    textView.setText(title);
    TextView cancel = ((TextView) actionsheet.findViewById(R.id.internal_sheet_cancel));
    final View view = actionsheet.findViewById(R.id.internal_actionsheet_bg);
    listView.setOnTouchListener(new ListView.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
          case MotionEvent.ACTION_DOWN:
            // Disallow ScrollView to intercept touch events.
            v.getParent().requestDisallowInterceptTouchEvent(true);
            break;

          case MotionEvent.ACTION_UP:
            // Allow ScrollView to intercept touch events.
            v.getParent().requestDisallowInterceptTouchEvent(false);
            break;
        }

        // Handle ListView touch events.
        v.onTouchEvent(event);
        return true;
      }
    });
    view.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ActionSheet.this.dismiss();
      }
    });
    cancel.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ActionSheet.this.dismiss();
      }
    });
    final BlurDrawable drawable = new BlurDrawable(getActivity().getWindow().getDecorView());
    drawable.setOverlayColor(Color.TRANSPARENT);
    //drawable.setBlurRadius(1);
    //drawable.setDownsampleFactor(2);
    // TODO: 2/9/16 wtf magic number
    drawable.setDrawOffset(0, -912);
    drawable.setOverlayColor(Color.argb(0xae, 0xff, 0xff, 0xff));
    view.setBackgroundDrawable(drawable);
    return actionsheet;
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
