package com.github.miao1007.myapplication.support.api.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Music {

  @SerializedName("rating") @Expose private Rating rating;
  @SerializedName("author") @Expose private List<Author> author = new ArrayList<Author>();
  @SerializedName("alt_title") @Expose private String altTitle;
  @SerializedName("image") @Expose private String image;
  @SerializedName("tags") @Expose private List<Tag> tags = new ArrayList<Tag>();
  @SerializedName("mobile_link") @Expose private String mobileLink;
  @SerializedName("attrs") @Expose private Attrs attrs;
  @SerializedName("title") @Expose private String title;
  @SerializedName("alt") @Expose private String alt;
  @SerializedName("id") @Expose private String id;

  /**
   * No args constructor for use in serialization
   */
  public Music() {
  }

  /**
   *
   * @param id
   * @param tags
   * @param author
   * @param title
   * @param mobileLink
   * @param alt
   * @param attrs
   * @param altTitle
   * @param image
   * @param rating
   */
  public Music(Rating rating, List<Author> author, String altTitle, String image, List<Tag> tags,
      String mobileLink, Attrs attrs, String title, String alt, String id) {
    this.rating = rating;
    this.author = author;
    this.altTitle = altTitle;
    this.image = image;
    this.tags = tags;
    this.mobileLink = mobileLink;
    this.attrs = attrs;
    this.title = title;
    this.alt = alt;
    this.id = id;
  }

  /**
   * @return The rating
   */
  public Rating getRating() {
    return rating;
  }

  /**
   * @param rating The rating
   */
  public void setRating(Rating rating) {
    this.rating = rating;
  }

  /**
   * @return The author
   */
  public List<Author> getAuthor() {
    return author;
  }

  /**
   * @param author The author
   */
  public void setAuthor(List<Author> author) {
    this.author = author;
  }

  /**
   * @return The altTitle
   */
  public String getAltTitle() {
    return altTitle;
  }

  /**
   * @param altTitle The alt_title
   */
  public void setAltTitle(String altTitle) {
    this.altTitle = altTitle;
  }

  /**
   * @return The image
   */
  public String getImage() {
    return image;
  }

  /**
   * @param image The image
   */
  public void setImage(String image) {
    this.image = image;
  }

  /**
   * @return The tags
   */
  public List<Tag> getTags() {
    return tags;
  }

  /**
   * @param tags The tags
   */
  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }

  /**
   * @return The mobileLink
   */
  public String getMobileLink() {
    return mobileLink;
  }

  /**
   * @param mobileLink The mobile_link
   */
  public void setMobileLink(String mobileLink) {
    this.mobileLink = mobileLink;
  }

  /**
   * @return The attrs
   */
  public Attrs getAttrs() {
    return attrs;
  }

  /**
   * @param attrs The attrs
   */
  public void setAttrs(Attrs attrs) {
    this.attrs = attrs;
  }

  /**
   * @return The title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title The title
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return The alt
   */
  public String getAlt() {
    return alt;
  }

  /**
   * @param alt The alt
   */
  public void setAlt(String alt) {
    this.alt = alt;
  }

  /**
   * @return The id
   */
  public String getId() {
    return id;
  }

  /**
   * @param id The id
   */
  public void setId(String id) {
    this.id = id;
  }
}
