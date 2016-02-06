package com.github.miao1007.animewallpaper.support.api.konachan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by leon on 2/6/16.
 */
public class Tag {
  //"id":14877,"name":"suzu_fujibayashi","count":4,"type":4,"ambiguous":false
  @SerializedName("id") @Expose private int id;
  @SerializedName("name") @Expose private String name;
  @SerializedName("count") @Expose private int count;
  @SerializedName("type") @Expose private int type;
  @SerializedName("ambiguous") @Expose private boolean ambiguous;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public boolean isAmbiguous() {
    return ambiguous;
  }

  public void setAmbiguous(boolean ambiguous) {
    this.ambiguous = ambiguous;
  }
}
