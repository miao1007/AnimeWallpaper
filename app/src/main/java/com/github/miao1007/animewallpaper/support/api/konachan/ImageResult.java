package com.github.miao1007.animewallpaper.support.api.konachan;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class ImageResult implements Parcelable {

  @SerializedName("id") @Expose private int id;
  @Expose @SerializedName("tags") private String tags;
  @SerializedName("created_at") @Expose private int createdAt;
  @SerializedName("creator_id") @Expose private int creatorId;
  @SerializedName("author") @Expose private String author;
  @SerializedName("change") @Expose private int change;
  @SerializedName("source") @Expose private String source;
  @SerializedName("score") @Expose private int score;
  @SerializedName("md5") @Expose private String md5;
  @SerializedName("file_size") @Expose private int fileSize;
  @SerializedName("file_url") @Expose private String fileUrl;
  @SerializedName("is_shown_in_index") @Expose private boolean isShownInIndex;
  @SerializedName("preview_url") @Expose private String previewUrl;
  @SerializedName("preview_width") @Expose private int previewWidth;
  @SerializedName("preview_height") @Expose private int previewHeight;
  @SerializedName("actual_preview_width") @Expose private int actualPreviewWidth;
  @SerializedName("actual_preview_height") @Expose private int actualPreviewHeight;
  @SerializedName("sample_url") @Expose private String sampleUrl;
  @SerializedName("sample_width") @Expose private int sampleWidth;
  @SerializedName("sample_height") @Expose private int sampleHeight;
  @SerializedName("sample_file_size") @Expose private int sampleFileSize;
  @SerializedName("jpeg_url") @Expose private String jpegUrl;
  @SerializedName("jpeg_width") @Expose private int jpegWidth;
  @SerializedName("jpeg_height") @Expose private int jpegHeight;
  @SerializedName("jpeg_file_size") @Expose private int jpegFileSize;
  @Expose private String rating;
  @SerializedName("has_children") @Expose private boolean hasChildren;
  @SerializedName("parent_id") @Expose private Object parentId;
  @Expose private String status;
  @Expose private int width;
  @Expose private int height;
  @SerializedName("is_held") @Expose private boolean isHeld;
  @SerializedName("frames_pending_string") @Expose private String framesPendingString;
  @SerializedName("frames_pending") @Expose private List<Object> framesPending = new ArrayList<>();
  @SerializedName("frames_string") @Expose private String framesString;
  @Expose private List<Object> frames = new ArrayList<>();
  @SerializedName("flag_detail") @Expose private Object flagDetail;

  public ImageResult() {
  }

  /**
   * @return The id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id The id
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return The tags
   */
  public String getTags() {
    return tags;
  }

  /**
   * @param tags The tags
   */
  public void setTags(String tags) {
    this.tags = tags;
  }

  /**
   * @return The createdAt
   */
  public int getCreatedAt() {
    return createdAt;
  }

  /**
   * @param createdAt The created_at
   */
  public void setCreatedAt(int createdAt) {
    this.createdAt = createdAt;
  }

  /**
   * @return The creatorId
   */
  public int getCreatorId() {
    return creatorId;
  }

  /**
   * @param creatorId The creator_id
   */
  public void setCreatorId(int creatorId) {
    this.creatorId = creatorId;
  }

  /**
   * @return The author
   */
  public String getAuthor() {
    return author;
  }

  /**
   * @param author The author
   */
  public void setAuthor(String author) {
    this.author = author;
  }

  /**
   * @return The change
   */
  public int getChange() {
    return change;
  }

  /**
   * @param change The change
   */
  public void setChange(int change) {
    this.change = change;
  }

  /**
   * @return The source
   */
  public String getSource() {
    return source;
  }

  /**
   * @param source The source
   */
  public void setSource(String source) {
    this.source = source;
  }

  /**
   * @return The score
   */
  public int getScore() {
    return score;
  }

  /**
   * @param score The score
   */
  public void setScore(int score) {
    this.score = score;
  }

  /**
   * @return The md5
   */
  public String getMd5() {
    return md5;
  }

  /**
   * @param md5 The md5
   */
  public void setMd5(String md5) {
    this.md5 = md5;
  }

  /**
   * @return The fileSize
   */
  public int getFileSize() {
    return fileSize;
  }

  /**
   * @param fileSize The file_size
   */
  public void setFileSize(int fileSize) {
    this.fileSize = fileSize;
  }

  /**
   * @return The fileUrl
   */
  public String getFileUrl() {
    return fileUrl;
  }

  /**
   * @param fileUrl The file_url
   */
  public void setFileUrl(String fileUrl) {
    this.fileUrl = fileUrl;
  }

  /**
   * @return The isShownInIndex
   */
  public boolean isIsShownInIndex() {
    return isShownInIndex;
  }

  /**
   * @param isShownInIndex The is_shown_in_index
   */
  public void setIsShownInIndex(boolean isShownInIndex) {
    this.isShownInIndex = isShownInIndex;
  }

  /**
   * @return The previewUrl
   */
  public String getPreviewUrl() {
    return previewUrl;
  }

  /**
   * @param previewUrl The preview_url
   */
  public void setPreviewUrl(String previewUrl) {
    this.previewUrl = previewUrl;
  }

  /**
   * @return The previewWidth
   */
  public int getPreviewWidth() {
    return previewWidth;
  }

  /**
   * @param previewWidth The preview_width
   */
  public void setPreviewWidth(int previewWidth) {
    this.previewWidth = previewWidth;
  }

  /**
   * @return The previewHeight
   */
  public int getPreviewHeight() {
    return previewHeight;
  }

  /**
   * @param previewHeight The preview_height
   */
  public void setPreviewHeight(int previewHeight) {
    this.previewHeight = previewHeight;
  }

  /**
   * @return The actualPreviewWidth
   */
  public int getActualPreviewWidth() {
    return actualPreviewWidth;
  }

  /**
   * @param actualPreviewWidth The actual_preview_width
   */
  public void setActualPreviewWidth(int actualPreviewWidth) {
    this.actualPreviewWidth = actualPreviewWidth;
  }

  /**
   * @return The actualPreviewHeight
   */
  public int getActualPreviewHeight() {
    return actualPreviewHeight;
  }

  /**
   * @param actualPreviewHeight The actual_preview_height
   */
  public void setActualPreviewHeight(int actualPreviewHeight) {
    this.actualPreviewHeight = actualPreviewHeight;
  }

  /**
   * @return The sampleUrl
   */
  public String getSampleUrl() {
    return sampleUrl;
  }

  /**
   * @param sampleUrl The sample_url
   */
  public void setSampleUrl(String sampleUrl) {
    this.sampleUrl = sampleUrl;
  }

  /**
   * @return The sampleWidth
   */
  public int getSampleWidth() {
    return sampleWidth;
  }

  /**
   * @param sampleWidth The sample_width
   */
  public void setSampleWidth(int sampleWidth) {
    this.sampleWidth = sampleWidth;
  }

  /**
   * @return The sampleHeight
   */
  public int getSampleHeight() {
    return sampleHeight;
  }

  /**
   * @param sampleHeight The sample_height
   */
  public void setSampleHeight(int sampleHeight) {
    this.sampleHeight = sampleHeight;
  }

  /**
   * @return The sampleFileSize
   */
  public int getSampleFileSize() {
    return sampleFileSize;
  }

  /**
   * @param sampleFileSize The sample_file_size
   */
  public void setSampleFileSize(int sampleFileSize) {
    this.sampleFileSize = sampleFileSize;
  }

  /**
   * @return The jpegUrl
   */
  public String getJpegUrl() {
    return jpegUrl;
  }

  /**
   * @param jpegUrl The jpeg_url
   */
  public void setJpegUrl(String jpegUrl) {
    this.jpegUrl = jpegUrl;
  }

  /**
   * @return The jpegWidth
   */
  public int getJpegWidth() {
    return jpegWidth;
  }

  /**
   * @param jpegWidth The jpeg_width
   */
  public void setJpegWidth(int jpegWidth) {
    this.jpegWidth = jpegWidth;
  }

  /**
   * @return The jpegHeight
   */
  public int getJpegHeight() {
    return jpegHeight;
  }

  /**
   * @param jpegHeight The jpeg_height
   */
  public void setJpegHeight(int jpegHeight) {
    this.jpegHeight = jpegHeight;
  }

  /**
   * @return The jpegFileSize
   */
  public int getJpegFileSize() {
    return jpegFileSize;
  }

  /**
   * @param jpegFileSize The jpeg_file_size
   */
  public void setJpegFileSize(int jpegFileSize) {
    this.jpegFileSize = jpegFileSize;
  }

  /**
   * @return The rating
   */
  public String getRating() {
    return rating;
  }

  /**
   * @param rating The rating
   */
  public void setRating(String rating) {
    this.rating = rating;
  }

  /**
   * @return The hasChildren
   */
  public boolean isHasChildren() {
    return hasChildren;
  }

  /**
   * @param hasChildren The has_children
   */
  public void setHasChildren(boolean hasChildren) {
    this.hasChildren = hasChildren;
  }

  /**
   * @return The parentId
   */
  public Object getParentId() {
    return parentId;
  }

  /**
   * @param parentId The parent_id
   */
  public void setParentId(Object parentId) {
    this.parentId = parentId;
  }

  /**
   * @return The status
   */
  public String getStatus() {
    return status;
  }

  /**
   * @param status The status
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * @return The width
   */
  public int getWidth() {
    return width;
  }

  /**
   * @param width The width
   */
  public void setWidth(int width) {
    this.width = width;
  }

  /**
   * @return The height
   */
  public int getHeight() {
    return height;
  }

  /**
   * @param height The height
   */
  public void setHeight(int height) {
    this.height = height;
  }

  /**
   * @return The isHeld
   */
  public boolean isIsHeld() {
    return isHeld;
  }

  /**
   * @param isHeld The is_held
   */
  public void setIsHeld(boolean isHeld) {
    this.isHeld = isHeld;
  }

  /**
   * @return The framesPendingString
   */
  public String getFramesPendingString() {
    return framesPendingString;
  }

  /**
   * @param framesPendingString The frames_pending_string
   */
  public void setFramesPendingString(String framesPendingString) {
    this.framesPendingString = framesPendingString;
  }

  /**
   * @return The framesPending
   */
  public List<Object> getFramesPending() {
    return framesPending;
  }

  /**
   * @param framesPending The frames_pending
   */
  public void setFramesPending(List<Object> framesPending) {
    this.framesPending = framesPending;
  }

  /**
   * @return The framesString
   */
  public String getFramesString() {
    return framesString;
  }

  /**
   * @param framesString The frames_string
   */
  public void setFramesString(String framesString) {
    this.framesString = framesString;
  }

  /**
   * @return The frames
   */
  public List<Object> getFrames() {
    return frames;
  }

  /**
   * @param frames The frames
   */
  public void setFrames(List<Object> frames) {
    this.frames = frames;
  }

  /**
   * @return The flagDetail
   */
  public Object getFlagDetail() {
    return flagDetail;
  }

  /**
   * @param flagDetail The flag_detail
   */
  public void setFlagDetail(Object flagDetail) {
    this.flagDetail = flagDetail;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.id);
    dest.writeString(this.tags);
    dest.writeInt(this.createdAt);
    dest.writeInt(this.creatorId);
    dest.writeString(this.author);
    dest.writeString(this.source);
    dest.writeInt(this.score);
    dest.writeInt(this.fileSize);
    dest.writeString(this.fileUrl);
    dest.writeString(this.previewUrl);
    dest.writeInt(this.previewWidth);
    dest.writeInt(this.previewHeight);
    dest.writeInt(this.actualPreviewWidth);
    dest.writeInt(this.actualPreviewHeight);
    dest.writeString(this.sampleUrl);
    dest.writeInt(this.sampleWidth);
    dest.writeInt(this.sampleHeight);
    dest.writeInt(this.sampleFileSize);
    dest.writeString(this.jpegUrl);
    dest.writeInt(this.jpegWidth);
    dest.writeInt(this.jpegHeight);
    dest.writeInt(this.jpegFileSize);
    dest.writeString(this.rating);
    dest.writeString(this.status);
    dest.writeInt(this.width);
    dest.writeInt(this.height);
  }

  private ImageResult(Parcel in) {
    this.id = in.readInt();
    this.tags = in.readString();
    this.createdAt = in.readInt();
    this.creatorId = in.readInt();
    this.author = in.readString();
    this.source = in.readString();
    this.score = in.readInt();
    this.fileSize = in.readInt();
    this.fileUrl = in.readString();
    this.previewUrl = in.readString();
    this.previewWidth = in.readInt();
    this.previewHeight = in.readInt();
    this.actualPreviewWidth = in.readInt();
    this.actualPreviewHeight = in.readInt();
    this.sampleUrl = in.readString();
    this.sampleWidth = in.readInt();
    this.sampleHeight = in.readInt();
    this.sampleFileSize = in.readInt();
    this.jpegUrl = in.readString();
    this.jpegWidth = in.readInt();
    this.jpegHeight = in.readInt();
    this.jpegFileSize = in.readInt();
    this.rating = in.readString();
    this.status = in.readString();
    this.width = in.readInt();
    this.height = in.readInt();
  }

  public static final Creator<ImageResult> CREATOR = new Creator<ImageResult>() {
    public ImageResult createFromParcel(Parcel source) {
      return new ImageResult(source);
    }

    public ImageResult[] newArray(int size) {
      return new ImageResult[size];
    }
  };
}
