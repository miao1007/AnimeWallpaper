package com.github.miao1007.myapplication.service;

import java.util.HashMap;

/**
 * Created by leon on 4/18/15.
 */
public class Query extends HashMap<String,Object> {

  String TAGS = "tags";
  String LIMIT = "limit";
  String PAGE = "page";

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


  public Query build() {
    ensureSaneDefaults();
    return this;
  }

  private void ensureSaneDefaults() {
    if (!this.containsKey(TAGS)) {
      this.put(TAGS, "rating:s");
    }

    if (!this.containsKey(LIMIT)) {
      this.put(LIMIT, 10);
    }

    if (!this.containsKey(PAGE)) {
      this.put(PAGE, 1);
    }
  }

}
