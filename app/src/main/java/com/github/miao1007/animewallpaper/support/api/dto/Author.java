package com.github.miao1007.animewallpaper.support.api.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Author {

  @SerializedName("name") @Expose private String name;

  /**
   * No args constructor for use in serialization
   */
  public Author() {
  }

  /**
   *
   * @param name
   */
  public Author(String name) {
    this.name = name;
  }

  /**
   * @return The name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name The name
   */
  public void setName(String name) {
    this.name = name;
  }
}
