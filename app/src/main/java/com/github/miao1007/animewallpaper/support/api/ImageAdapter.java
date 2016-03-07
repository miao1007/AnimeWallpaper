package com.github.miao1007.animewallpaper.support.api;

import android.os.Parcel;
import android.os.Parcelable;
import com.github.miao1007.animewallpaper.support.api.konachan.ImageResult;

/**
 * Created by leon on 2/25/16.
 */
public class ImageAdapter implements Parcelable {

  private String prev_url;
  private String download_url;
  private String tags;

  private ImageAdapter(String prev_url, String download_url, String tags) {
    this.prev_url = prev_url;
    this.download_url = download_url;
    this.tags = tags;
  }

  public String getPrev_url() {
    return prev_url;
  }

  public void setPrev_url(String prev_url) {
    this.prev_url = prev_url;
  }

  public String getDownload_url() {
    return download_url;
  }

  public void setDownload_url(String download_url) {
    this.download_url = download_url;
  }

  public String getTags() {
    return tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.prev_url);
    dest.writeString(this.download_url);
    dest.writeString(this.tags);
  }

  public ImageAdapter() {
  }


  public static ImageAdapter from(ImageResult result){
    return new ImageAdapter(result.getPreviewUrl(),result.getSampleUrl(),result.getTags());
  }

  private ImageAdapter(Parcel in) {
    this.prev_url = in.readString();
    this.download_url = in.readString();
    this.tags = in.readString();
  }

  public static final Parcelable.Creator<ImageAdapter> CREATOR =
      new Parcelable.Creator<ImageAdapter>() {
        public ImageAdapter createFromParcel(Parcel source) {
          return new ImageAdapter(source);
        }

        public ImageAdapter[] newArray(int size) {
          return new ImageAdapter[size];
        }
      };
}
