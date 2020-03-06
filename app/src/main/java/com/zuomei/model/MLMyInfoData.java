package com.zuomei.model;

import com.google.gson.annotations.Expose;

public class MLMyInfoData extends MLBaseResponse {

  @Expose
  public String id;
  @Expose
  public String address;
  @Expose
  public String depotName;
  @Expose
  public String location;
  @Expose
  public String realName;
  @Expose
  public String userName;
  @Expose
  public String userPhone;
  @Expose
  public String userPhone2;
  @Expose
  public String userPhone3;
  @Expose
  public String userPhone4;
  @Expose
  public String userPhone5;
  @Expose
  public int userPhoto;
  @Expose
  public String alipay;
  @Expose
  public String voipTime;
  @Expose
  public String integral;
  @Expose
  public String money;

  @Expose
  public String level;

  @Expose
  public String gradeIntegral;

  //==二期新增====

  @Expose
  public String startDate;
  @Expose
  public String endDate;

  @Expose
  public String declaration;

  @Expose
  public String concurrenOperate;

  @Expose
  public String cardNo;

  @Expose
  public String openBank;
}
