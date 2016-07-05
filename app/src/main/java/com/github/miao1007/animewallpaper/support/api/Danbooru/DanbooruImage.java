package com.github.miao1007.animewallpaper.support.api.Danbooru;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class DanbooruImage {

  @SerializedName("id") @Expose private int id;
  @SerializedName("created_at") @Expose private String createdAt;
  @SerializedName("uploader_id") @Expose private int uploaderId;
  @SerializedName("score") @Expose private int score;
  @SerializedName("source") @Expose private String source;
  @SerializedName("md5") @Expose private String md5;
  @SerializedName("last_comment_bumped_at") @Expose private Object lastCommentBumpedAt;
  @SerializedName("rating") @Expose private String rating;
  @SerializedName("image_width") @Expose private int imageWidth;
  @SerializedName("image_height") @Expose private int imageHeight;
  @SerializedName("tag_string") @Expose private String tagString;
  @SerializedName("is_note_locked") @Expose private boolean isNoteLocked;
  @SerializedName("fav_count") @Expose private int favCount;
  @SerializedName("file_ext") @Expose private String fileExt;
  @SerializedName("last_noted_at") @Expose private Object lastNotedAt;
  @SerializedName("is_rating_locked") @Expose private boolean isRatingLocked;
  @SerializedName("parent_id") @Expose private Object parentId;
  @SerializedName("has_children") @Expose private boolean hasChildren;
  @SerializedName("approver_id") @Expose private Object approverId;
  @SerializedName("tag_count_general") @Expose private int tagCountGeneral;
  @SerializedName("tag_count_artist") @Expose private int tagCountArtist;
  @SerializedName("tag_count_character") @Expose private int tagCountCharacter;
  @SerializedName("tag_count_copyright") @Expose private int tagCountCopyright;
  @SerializedName("file_size") @Expose private int fileSize;
  @SerializedName("is_status_locked") @Expose private boolean isStatusLocked;
  @SerializedName("fav_string") @Expose private String favString;
  @SerializedName("pool_string") @Expose private String poolString;
  @SerializedName("up_score") @Expose private int upScore;
  @SerializedName("down_score") @Expose private int downScore;
  @SerializedName("is_pending") @Expose private boolean isPending;
  @SerializedName("is_flagged") @Expose private boolean isFlagged;
  @SerializedName("is_deleted") @Expose private boolean isDeleted;
  @SerializedName("tag_count") @Expose private int tagCount;
  @SerializedName("updated_at") @Expose private String updatedAt;
  @SerializedName("is_banned") @Expose private boolean isBanned;
  @SerializedName("pixiv_id") @Expose private int pixivId;
  @SerializedName("last_commented_at") @Expose private Object lastCommentedAt;
  @SerializedName("has_active_children") @Expose private boolean hasActiveChildren;
  @SerializedName("bit_flags") @Expose private int bitFlags;
  @SerializedName("uploader_name") @Expose private String uploaderName;
  @SerializedName("has_large") @Expose private boolean hasLarge;
  @SerializedName("tag_string_artist") @Expose private String tagStringArtist;
  @SerializedName("tag_string_character") @Expose private String tagStringCharacter;
  @SerializedName("tag_string_copyright") @Expose private String tagStringCopyright;
  @SerializedName("tag_string_general") @Expose private String tagStringGeneral;
  @SerializedName("has_visible_children") @Expose private boolean hasVisibleChildren;
  @SerializedName("file_url") @Expose private String fileUrl;
  @SerializedName("large_file_url") @Expose private String largeFileUrl;
  @SerializedName("preview_file_url") @Expose private String previewFileUrl;

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
   * @return The createdAt
   */
  public String getCreatedAt() {
    return createdAt;
  }

  /**
   * @param createdAt The created_at
   */
  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  /**
   * @return The uploaderId
   */
  public int getUploaderId() {
    return uploaderId;
  }

  /**
   * @param uploaderId The uploader_id
   */
  public void setUploaderId(int uploaderId) {
    this.uploaderId = uploaderId;
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
   * @return The lastCommentBumpedAt
   */
  public Object getLastCommentBumpedAt() {
    return lastCommentBumpedAt;
  }

  /**
   * @param lastCommentBumpedAt The last_comment_bumped_at
   */
  public void setLastCommentBumpedAt(Object lastCommentBumpedAt) {
    this.lastCommentBumpedAt = lastCommentBumpedAt;
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
   * @return The imageWidth
   */
  public int getImageWidth() {
    return imageWidth;
  }

  /**
   * @param imageWidth The image_width
   */
  public void setImageWidth(int imageWidth) {
    this.imageWidth = imageWidth;
  }

  /**
   * @return The imageHeight
   */
  public int getImageHeight() {
    return imageHeight;
  }

  /**
   * @param imageHeight The image_height
   */
  public void setImageHeight(int imageHeight) {
    this.imageHeight = imageHeight;
  }

  /**
   * @return The tagString
   */
  public String getTagString() {
    return tagString;
  }

  /**
   * @param tagString The tag_string
   */
  public void setTagString(String tagString) {
    this.tagString = tagString;
  }

  /**
   * @return The isNoteLocked
   */
  public boolean isIsNoteLocked() {
    return isNoteLocked;
  }

  /**
   * @param isNoteLocked The is_note_locked
   */
  public void setIsNoteLocked(boolean isNoteLocked) {
    this.isNoteLocked = isNoteLocked;
  }

  /**
   * @return The favCount
   */
  public int getFavCount() {
    return favCount;
  }

  /**
   * @param favCount The fav_count
   */
  public void setFavCount(int favCount) {
    this.favCount = favCount;
  }

  /**
   * @return The fileExt
   */
  public String getFileExt() {
    return fileExt;
  }

  /**
   * @param fileExt The file_ext
   */
  public void setFileExt(String fileExt) {
    this.fileExt = fileExt;
  }

  /**
   * @return The lastNotedAt
   */
  public Object getLastNotedAt() {
    return lastNotedAt;
  }

  /**
   * @param lastNotedAt The last_noted_at
   */
  public void setLastNotedAt(Object lastNotedAt) {
    this.lastNotedAt = lastNotedAt;
  }

  /**
   * @return The isRatingLocked
   */
  public boolean isIsRatingLocked() {
    return isRatingLocked;
  }

  /**
   * @param isRatingLocked The is_rating_locked
   */
  public void setIsRatingLocked(boolean isRatingLocked) {
    this.isRatingLocked = isRatingLocked;
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
   * @return The approverId
   */
  public Object getApproverId() {
    return approverId;
  }

  /**
   * @param approverId The approver_id
   */
  public void setApproverId(Object approverId) {
    this.approverId = approverId;
  }

  /**
   * @return The tagCountGeneral
   */
  public int getTagCountGeneral() {
    return tagCountGeneral;
  }

  /**
   * @param tagCountGeneral The tag_count_general
   */
  public void setTagCountGeneral(int tagCountGeneral) {
    this.tagCountGeneral = tagCountGeneral;
  }

  /**
   * @return The tagCountArtist
   */
  public int getTagCountArtist() {
    return tagCountArtist;
  }

  /**
   * @param tagCountArtist The tag_count_artist
   */
  public void setTagCountArtist(int tagCountArtist) {
    this.tagCountArtist = tagCountArtist;
  }

  /**
   * @return The tagCountCharacter
   */
  public int getTagCountCharacter() {
    return tagCountCharacter;
  }

  /**
   * @param tagCountCharacter The tag_count_character
   */
  public void setTagCountCharacter(int tagCountCharacter) {
    this.tagCountCharacter = tagCountCharacter;
  }

  /**
   * @return The tagCountCopyright
   */
  public int getTagCountCopyright() {
    return tagCountCopyright;
  }

  /**
   * @param tagCountCopyright The tag_count_copyright
   */
  public void setTagCountCopyright(int tagCountCopyright) {
    this.tagCountCopyright = tagCountCopyright;
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
   * @return The isStatusLocked
   */
  public boolean isIsStatusLocked() {
    return isStatusLocked;
  }

  /**
   * @param isStatusLocked The is_status_locked
   */
  public void setIsStatusLocked(boolean isStatusLocked) {
    this.isStatusLocked = isStatusLocked;
  }

  /**
   * @return The favString
   */
  public String getFavString() {
    return favString;
  }

  /**
   * @param favString The fav_string
   */
  public void setFavString(String favString) {
    this.favString = favString;
  }

  /**
   * @return The poolString
   */
  public String getPoolString() {
    return poolString;
  }

  /**
   * @param poolString The pool_string
   */
  public void setPoolString(String poolString) {
    this.poolString = poolString;
  }

  /**
   * @return The upScore
   */
  public int getUpScore() {
    return upScore;
  }

  /**
   * @param upScore The up_score
   */
  public void setUpScore(int upScore) {
    this.upScore = upScore;
  }

  /**
   * @return The downScore
   */
  public int getDownScore() {
    return downScore;
  }

  /**
   * @param downScore The down_score
   */
  public void setDownScore(int downScore) {
    this.downScore = downScore;
  }

  /**
   * @return The isPending
   */
  public boolean isIsPending() {
    return isPending;
  }

  /**
   * @param isPending The is_pending
   */
  public void setIsPending(boolean isPending) {
    this.isPending = isPending;
  }

  /**
   * @return The isFlagged
   */
  public boolean isIsFlagged() {
    return isFlagged;
  }

  /**
   * @param isFlagged The is_flagged
   */
  public void setIsFlagged(boolean isFlagged) {
    this.isFlagged = isFlagged;
  }

  /**
   * @return The isDeleted
   */
  public boolean isIsDeleted() {
    return isDeleted;
  }

  /**
   * @param isDeleted The is_deleted
   */
  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  /**
   * @return The tagCount
   */
  public int getTagCount() {
    return tagCount;
  }

  /**
   * @param tagCount The tag_count
   */
  public void setTagCount(int tagCount) {
    this.tagCount = tagCount;
  }

  /**
   * @return The updatedAt
   */
  public String getUpdatedAt() {
    return updatedAt;
  }

  /**
   * @param updatedAt The updated_at
   */
  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }

  /**
   * @return The isBanned
   */
  public boolean isIsBanned() {
    return isBanned;
  }

  /**
   * @param isBanned The is_banned
   */
  public void setIsBanned(boolean isBanned) {
    this.isBanned = isBanned;
  }

  /**
   * @return The pixivId
   */
  public int getPixivId() {
    return pixivId;
  }

  /**
   * @param pixivId The pixiv_id
   */
  public void setPixivId(int pixivId) {
    this.pixivId = pixivId;
  }

  /**
   * @return The lastCommentedAt
   */
  public Object getLastCommentedAt() {
    return lastCommentedAt;
  }

  /**
   * @param lastCommentedAt The last_commented_at
   */
  public void setLastCommentedAt(Object lastCommentedAt) {
    this.lastCommentedAt = lastCommentedAt;
  }

  /**
   * @return The hasActiveChildren
   */
  public boolean isHasActiveChildren() {
    return hasActiveChildren;
  }

  /**
   * @param hasActiveChildren The has_active_children
   */
  public void setHasActiveChildren(boolean hasActiveChildren) {
    this.hasActiveChildren = hasActiveChildren;
  }

  /**
   * @return The bitFlags
   */
  public int getBitFlags() {
    return bitFlags;
  }

  /**
   * @param bitFlags The bit_flags
   */
  public void setBitFlags(int bitFlags) {
    this.bitFlags = bitFlags;
  }

  /**
   * @return The uploaderName
   */
  public String getUploaderName() {
    return uploaderName;
  }

  /**
   * @param uploaderName The uploader_name
   */
  public void setUploaderName(String uploaderName) {
    this.uploaderName = uploaderName;
  }

  /**
   * @return The hasLarge
   */
  public boolean isHasLarge() {
    return hasLarge;
  }

  /**
   * @param hasLarge The has_large
   */
  public void setHasLarge(boolean hasLarge) {
    this.hasLarge = hasLarge;
  }

  /**
   * @return The tagStringArtist
   */
  public String getTagStringArtist() {
    return tagStringArtist;
  }

  /**
   * @param tagStringArtist The tag_string_artist
   */
  public void setTagStringArtist(String tagStringArtist) {
    this.tagStringArtist = tagStringArtist;
  }

  /**
   * @return The tagStringCharacter
   */
  public String getTagStringCharacter() {
    return tagStringCharacter;
  }

  /**
   * @param tagStringCharacter The tag_string_character
   */
  public void setTagStringCharacter(String tagStringCharacter) {
    this.tagStringCharacter = tagStringCharacter;
  }

  /**
   * @return The tagStringCopyright
   */
  public String getTagStringCopyright() {
    return tagStringCopyright;
  }

  /**
   * @param tagStringCopyright The tag_string_copyright
   */
  public void setTagStringCopyright(String tagStringCopyright) {
    this.tagStringCopyright = tagStringCopyright;
  }

  /**
   * @return The tagStringGeneral
   */
  public String getTagStringGeneral() {
    return tagStringGeneral;
  }

  /**
   * @param tagStringGeneral The tag_string_general
   */
  public void setTagStringGeneral(String tagStringGeneral) {
    this.tagStringGeneral = tagStringGeneral;
  }

  /**
   * @return The hasVisibleChildren
   */
  public boolean isHasVisibleChildren() {
    return hasVisibleChildren;
  }

  /**
   * @param hasVisibleChildren The has_visible_children
   */
  public void setHasVisibleChildren(boolean hasVisibleChildren) {
    this.hasVisibleChildren = hasVisibleChildren;
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
   * @return The largeFileUrl
   */
  public String getLargeFileUrl() {
    return largeFileUrl;
  }

  /**
   * @param largeFileUrl The large_file_url
   */
  public void setLargeFileUrl(String largeFileUrl) {
    this.largeFileUrl = largeFileUrl;
  }

  /**
   * @return The previewFileUrl
   */
  public String getPreviewFileUrl() {
    return previewFileUrl;
  }

  /**
   * @param previewFileUrl The preview_file_url
   */
  public void setPreviewFileUrl(String previewFileUrl) {
    this.previewFileUrl = previewFileUrl;
  }
}
