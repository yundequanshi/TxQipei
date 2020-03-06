package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public class MLMessageInfo implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 4910101702857064213L;

  @Expose
  public String id;

  @Expose
  public String content;

  @Expose
  public String createTime;

  @Expose
  public String userPhoto;

  @Expose
  public String voice;

  @Expose
  public String images;

  @Expose
  public boolean isReport;
  @Expose
  public String length;

  @Expose
  public String video;

  @Expose
  public String videoPic;

  @Expose
  public String isCollection = "0";

  @Expose
  public String isPraise;

  @Expose
  public String praiseNum;

  @Expose
  public List<MLMessageCommentData> interactionComment;
}
