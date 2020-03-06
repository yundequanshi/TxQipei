package com.zuomei.model;

import com.google.gson.annotations.Expose;

import com.qipei.model.ParseInfoData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MLMessageData implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 4910101702857064213L;

  @Expose
  public String hxPwd;

  @Expose
  public String hxUser;

  @Expose
  public MLDepotData user;

  @Expose
  public String userType;

  @Expose
  public MLMessageInfo info;

  @Expose
  public String gradeNum;

  @Expose
  public List<ParseInfoData> praiseInfo = new ArrayList<>();
}
