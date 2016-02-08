package com.github.miao1007.animewallpaper.ui.widget;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import java.util.Arrays;

/**
 * Created by leon on 1/19/16.
 */
public class Position implements Parcelable {

  static final String TAG = "Position";

  public static final Creator<Position> CREATOR = new Creator<Position>() {
    public Position createFromParcel(Parcel source) {
      return new Position(source);
    }

    public Position[] newArray(int size) {
      return new Position[size];
    }
  };
  public int left;
  public int right;
  public int top;
  public int bottom;
  public int width;
  public int height;

  public Position() {
  }

  public Position(int left, int right, int top, int bottom, int width, int height) {
    this.left = left;
    this.right = right;
    this.top = top;
    this.bottom = bottom;
    this.width = width;
    this.height = height;
  }

  protected Position(Parcel in) {
    this.left = in.readInt();
    this.right = in.readInt();
    this.top = in.readInt();
    this.bottom = in.readInt();
    this.width = in.readInt();
    this.height = in.readInt();
  }

  public static Position from(View view) {
    //int top = ((int) view.getY());
    //int[] location = new int[2];
    //view.getLocationOnScreen(location);
    //Log.d(TAG, "y:" + view.getY());
    //Log.d(TAG, "location:" + Arrays.toString(location));
    //Log.d(TAG, position.toString());
    return new Position(view.getLeft(), view.getRight(), view.getTop(), view.getBottom(),
        view.getWidth(), view.getHeight());
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.left);
    dest.writeInt(this.right);
    dest.writeInt(this.top);
    dest.writeInt(this.bottom);
    dest.writeInt(this.width);
    dest.writeInt(this.height);
  }

  @Override public String toString() {
    return "Position{" +
        "left=" + left +
        ", right=" + right +
        ", top=" + top +
        ", bottom=" + bottom +
        ", width=" + width +
        ", height=" + height +
        '}';
  }
}