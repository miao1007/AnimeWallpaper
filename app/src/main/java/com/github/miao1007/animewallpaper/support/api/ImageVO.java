package com.github.miao1007.animewallpaper.support.api;

import android.os.Parcel;
import android.os.Parcelable;
import com.github.miao1007.animewallpaper.support.api.konachan.ImageResult;
import java.util.Arrays;
import java.util.List;
import rx.functions.Func1;

/**
 * Created by leon on 7/21/16.
 */
public class ImageVO implements Parcelable {
  public static final Creator<ImageVO> CREATOR = new Creator<ImageVO>() {
    @Override public ImageVO createFromParcel(Parcel source) {
      return new ImageVO(source);
    }

    @Override public ImageVO[] newArray(int size) {
      return new ImageVO[size];
    }
  };
  public static final Func1<ImageResult, ImageVO> FROM_IMAGE_RESULT =
      new Func1<ImageResult, ImageVO>() {
        @Override public ImageVO call(ImageResult value) {
          final List<String> tags = Arrays.asList(value.getTags().split(" "));
          //fixed on api changed
          final String HTTP = "http:";
          return new ImageVO(HTTP + value.getPreviewUrl(), HTTP + value.getSampleUrl(), tags);
        }
      };
  private String prevurl;
  private String downloadUrl;
  private List<String> tags;

  public ImageVO(String prevurl, String downloadUrl, List<String> tags) {
    this.prevurl = prevurl;
    this.downloadUrl = downloadUrl;
    this.tags = tags;
  }

  public ImageVO(String prevurl) {
    this.prevurl = prevurl;
  }

  public ImageVO() {
  }

  protected ImageVO(Parcel in) {
    this.prevurl = in.readString();
    this.downloadUrl = in.readString();
    this.tags = in.createStringArrayList();
  }

  public String getDownload_url() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public String getPrevurl() {
    return prevurl;
  }

  public void setPrevurl(String prevurl) {
    this.prevurl = prevurl;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.prevurl);
    dest.writeString(this.downloadUrl);
    dest.writeStringList(this.tags);
  }
}
