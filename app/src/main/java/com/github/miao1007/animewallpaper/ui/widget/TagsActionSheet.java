package com.github.miao1007.animewallpaper.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.github.miao1007.animewallpaper.R;
import java.util.List;

/**
 * Created by leon on 3/30/16.
 */
public class TagsActionSheet extends ActionSheet {

  private List<String> tags;

  public TagsActionSheet(Window window, @Nullable AdapterView.OnItemClickListener listener,
      List<String> tags) {
    super(window, listener);
    this.tags = tags;
  }

  @Override public int getTitle() {
    return R.string.searching_for_relevant_tags;
  }

  @Override public BaseAdapter getAdapter() {
    return new BlueAdapter(getContext(), tags);
  }

  /**
   * adapter for tags in ActionSheet
   */
  class BlueAdapter extends ArrayAdapter<String> {

    public BlueAdapter(Context context, List<String> tags) {
      super(context, android.R.layout.simple_list_item_1, tags);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
      View view = super.getView(position, convertView, parent);
      if (view instanceof TextView) {
        ((TextView) view).setText(tags.get(position));
        ((TextView) view).setTextColor(
            getContext().getResources().getColor(R.color.ios_internal_blue));
      }
      return view;
    }
  }
}
