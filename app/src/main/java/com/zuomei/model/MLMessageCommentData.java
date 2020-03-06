package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MLMessageCommentData implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -3932204850502668494L;


  @Expose
  public String content;

  @Expose
  public String userName;

  @Expose
  public String id;
}
