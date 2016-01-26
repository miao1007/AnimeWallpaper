package com.github.miao1007.myapplication.support.api.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rating {

  @SerializedName("max") @Expose private int max;
  @SerializedName("average") @Expose private String average;
  @SerializedName("numRaters") @Expose private int numRaters;
  @SerializedName("min") @Expose private int min;

  /**
   * No args constructor for use in serialization
   */
  public Rating() {
  }

  /**
   *
   * @param min
   * @param max
   * @param numRaters
   * @param average
   */
  public Rating(int max, String average, int numRaters, int min) {
    this.max = max;
    this.average = average;
    this.numRaters = numRaters;
    this.min = min;
  }

  /**
   * @return The max
   */
  public int getMax() {
    return max;
  }

  /**
   * @param max The max
   */
  public void setMax(int max) {
    this.max = max;
  }

  /**
   * @return The average
   */
  public String getAverage() {
    return average;
  }

  /**
   * @param average The average
   */
  public void setAverage(String average) {
    this.average = average;
  }

  /**
   * @return The numRaters
   */
  public int getNumRaters() {
    return numRaters;
  }

  /**
   * @param numRaters The numRaters
   */
  public void setNumRaters(int numRaters) {
    this.numRaters = numRaters;
  }

  /**
   * @return The min
   */
  public int getMin() {
    return min;
  }

  /**
   * @param min The min
   */
  public void setMin(int min) {
    this.min = min;
  }
}
