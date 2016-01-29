package com.github.miao1007.myapplication.ui.widget;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.github.miao1007.myapplication.R;
import com.github.miao1007.myapplication.ui.activity.MainActivity;
import com.github.miao1007.myapplication.utils.LogUtils;
import java.util.Arrays;
import java.util.List;

/**
 * Created by leon on 1/27/16.
 */
public class ActionSheet extends SwipeableBottomDialog {


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

  @Override protected View inflateDialogView(LayoutInflater inflater, ViewGroup container) {
    Log.d(TAG, "inflateDialogView");
    title = getArguments().getString("title");
    tags = Arrays.asList(getArguments().getStringArray("tags"));

    Log.d(TAG, title + "/" + tags.toString());
    View actionsheet = inflater.inflate(R.layout.internal_actionsheet, container, false);
    ListView listView = ((ListView) actionsheet.findViewById(R.id.internal_actionsheet_list));
    listView.setAdapter(new BlueAdapter(getContext(), android.R.layout.simple_list_item_1, tags));
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MainActivity.startRefreshActivity(getContext(), tags.get(position));
      }
    });
    TextView textView = ((TextView) actionsheet.findViewById(R.id.internal_actionsheet_title));
    textView.setText(title);
    TextView cancel = ((TextView) actionsheet.findViewById(R.id.internal_sheet_cancel));
    View view = actionsheet.findViewById(R.id.internal_actionsheet_bg);
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
