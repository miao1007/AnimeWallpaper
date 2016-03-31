package com.github.miao1007.animewallpaper.ui.widget;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.github.miao1007.animewallpaper.R;
import com.github.miao1007.animewallpaper.utils.SquareUtils;
import com.squareup.picasso.Transformation;
import java.io.File;

/**
 * Created by leon on 3/30/16.
 */
public class HistoryActionSheet extends ActionSheet {

  private File[] files;

  public HistoryActionSheet(Window window, @Nullable AdapterView.OnItemClickListener listener,
      File file) {
    super(window, listener);
    files = file.listFiles();
  }

  @Override public int getTitle() {
    return R.string.history;
  }

  @Override public BaseAdapter getAdapter() {
    return new ImgAdapter();
  }

  class ImgAdapter extends BaseAdapter {

    @Override public int getCount() {
      if (files == null || files.length == 0) {
        return 0;
      }
      return files.length;
    }

    @Override public Object getItem(int position) {
      return files[position];
    }

    @Override public long getItemId(int position) {
      return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
      ImageView view = new ImageView(getContext());
      view.setAdjustViewBounds(true);
      SquareUtils.getPicasso(getContext()).load(files[position]).transform(new Transformation() {
        @Override public Bitmap transform(Bitmap source) {
          Bitmap bitmap =
              ThumbnailUtils.extractThumbnail(source, source.getWidth() / 4, source.getWidth() / 4);
          source.recycle();
          return bitmap;
        }

        @Override public String key() {
          return "ThumbnailUtils";
        }
      }).config(Bitmap.Config.RGB_565).into(view);
      return view;
    }

    class ViewHolder {
      ImageView imageView;
    }
  }
}
