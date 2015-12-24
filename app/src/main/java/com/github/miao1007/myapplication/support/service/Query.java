package com.github.miao1007.myapplication.support.service;

import java.util.HashMap;

/**
 * Created by leon on 4/18/15.
 */
public class Query extends HashMap<String,Object> {

  public static final String TAGS = "tags";
  public static final String LIMIT = "limit";
  public static final String PAGE = "page";

  public Query() {
  }

  public Query(int capacity) {
    super(capacity);
  }

  public Query setTAGS(String tags) {
    this.put(TAGS, tags);
    return this;
  }

  public Query setLIMIT(int limit) {
    this.put(LIMIT, limit);
    return this;
  }

  public Query setPAGE(int page) {
    this.put(PAGE, page);
    return this;
  }


  public void addPage() {
    this.put(PAGE, ((int) this.get(PAGE)) + 1);
  }


  public Query init() {
    ensureSaneDefaults();
    return this;
  }

  private void ensureSaneDefaults() {
    if (!this.containsKey(TAGS)) {
      this.put(TAGS, "rating:s suzumiya_haruhi");

    }

    if (!this.containsKey(LIMIT)) {
      this.put(LIMIT, 10);
    }

    if (!this.containsKey(PAGE)) {
      this.put(PAGE, 1);
    }
  }

}
