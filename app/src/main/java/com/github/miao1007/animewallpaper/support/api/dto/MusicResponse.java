package com.github.miao1007.animewallpaper.support.api.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class MusicResponse {

  @SerializedName("count") @Expose private int count;
  @SerializedName("start") @Expose private int start;
  @SerializedName("total") @Expose private int total;
  @SerializedName("musics") @Expose private List<Music> musics = new ArrayList<Music>();

  /**
   * No args constructor for use in serialization
   */
  public MusicResponse() {
  }

  /**
   *
   * @param musics
   * @param total
   * @param count
   * @param start
   */
  public MusicResponse(int count, int start, int total, List<Music> musics) {
    this.count = count;
    this.start = start;
    this.total = total;
    this.musics = musics;
  }

  /**
   * @return The count
   */
  public int getCount() {
    return count;
  }

  /**
   * @param count The count
   */
  public void setCount(int count) {
    this.count = count;
  }

  /**
   * @return The start
   */
  public int getStart() {
    return start;
  }

  /**
   * @param start The start
   */
  public void setStart(int start) {
    this.start = start;
  }

  /**
   * @return The total
   */
  public int getTotal() {
    return total;
  }

  /**
   * @param total The total
   */
  public void setTotal(int total) {
    this.total = total;
  }

  /**
   * @return The musics
   */
  public List<Music> getMusics() {
    return musics;
  }

  /**
   * @param musics The musics
   */
  public void setMusics(List<Music> musics) {
    this.musics = musics;
  }
}
