package com.github.miao1007.animewallpaper.support.api.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Attrs {

  @SerializedName("publisher") @Expose private List<String> publisher = new ArrayList<String>();
  @SerializedName("singer") @Expose private List<String> singer = new ArrayList<String>();
  @SerializedName("discs") @Expose private List<String> discs = new ArrayList<String>();
  @SerializedName("pubdate") @Expose private List<String> pubdate = new ArrayList<String>();
  @SerializedName("title") @Expose private List<String> title = new ArrayList<String>();
  @SerializedName("media") @Expose private List<String> media = new ArrayList<String>();
  @SerializedName("tracks") @Expose private List<String> tracks = new ArrayList<String>();
  @SerializedName("version") @Expose private List<String> version = new ArrayList<String>();

  /**
   * No args constructor for use in serialization
   */
  public Attrs() {
  }

  /**
   *
   * @param title
   * @param discs
   * @param pubdate
   * @param singer
   * @param tracks
   * @param media
   * @param version
   * @param publisher
   */
  public Attrs(List<String> publisher, List<String> singer, List<String> discs,
      List<String> pubdate, List<String> title, List<String> media, List<String> tracks,
      List<String> version) {
    this.publisher = publisher;
    this.singer = singer;
    this.discs = discs;
    this.pubdate = pubdate;
    this.title = title;
    this.media = media;
    this.tracks = tracks;
    this.version = version;
  }

  /**
   * @return The publisher
   */
  public List<String> getPublisher() {
    return publisher;
  }

  /**
   * @param publisher The publisher
   */
  public void setPublisher(List<String> publisher) {
    this.publisher = publisher;
  }

  /**
   * @return The singer
   */
  public List<String> getSinger() {
    return singer;
  }

  /**
   * @param singer The singer
   */
  public void setSinger(List<String> singer) {
    this.singer = singer;
  }

  /**
   * @return The discs
   */
  public List<String> getDiscs() {
    return discs;
  }

  /**
   * @param discs The discs
   */
  public void setDiscs(List<String> discs) {
    this.discs = discs;
  }

  /**
   * @return The pubdate
   */
  public List<String> getPubdate() {
    return pubdate;
  }

  /**
   * @param pubdate The pubdate
   */
  public void setPubdate(List<String> pubdate) {
    this.pubdate = pubdate;
  }

  /**
   * @return The title
   */
  public List<String> getTitle() {
    return title;
  }

  /**
   * @param title The title
   */
  public void setTitle(List<String> title) {
    this.title = title;
  }

  /**
   * @return The media
   */
  public List<String> getMedia() {
    return media;
  }

  /**
   * @param media The media
   */
  public void setMedia(List<String> media) {
    this.media = media;
  }

  /**
   * @return The tracks
   */
  public List<String> getTracks() {
    return tracks;
  }

  /**
   * @param tracks The tracks
   */
  public void setTracks(List<String> tracks) {
    this.tracks = tracks;
  }

  /**
   * @return The version
   */
  public List<String> getVersion() {
    return version;
  }

  /**
   * @param version The version
   */
  public void setVersion(List<String> version) {
    this.version = version;
  }
}
