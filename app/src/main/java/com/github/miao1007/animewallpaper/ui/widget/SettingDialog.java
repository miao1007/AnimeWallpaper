package com.github.miao1007.animewallpaper.ui.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import butterknife.Bind;
import com.github.miao1007.animewallpaper.R;
import com.github.miao1007.animewallpaper.ui.widget.blur.FullScreenBlurAlertDialog;

/**
 * Created by leon on 2/19/16.
 */
public class SettingDialog extends FullScreenBlurAlertDialog {

  @Bind(R.id.settings_list) GridView mSettingsList;

  public SettingDialog(Window window) {
    super(window);
  }

  //@Override protected View inflateDialogView(LayoutInflater layoutInflater) {
  //  View view = layoutInflater.inflate(R.layout.internal_fullscreen_dialog, null, false);
  //
  //  return view;
  //}


  static class GridAdapter  extends BaseAdapter{
    @Override public int getCount() {
      return 0;
    }

    @Override public Object getItem(int position) {
      return position;
    }

    @Override public long getItemId(int position) {
      return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
      return null;
    }
  }
}
