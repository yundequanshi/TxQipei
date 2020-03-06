package com.zuomei.model;

import com.google.gson.annotations.Expose;
import com.txsh.model.TXProductModel;

import java.io.Serializable;
import java.util.List;

public class MLHomeBusiness1Data implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 3575463679999420536L;

  //===20个商家个别字段====
  @Expose
  public String id;

  @Expose
  public String companyName;

  @Expose
  public String compayName;
  //=======

  @Expose
  public String logo;
  @Expose
  public String phone;
  @Expose
  public double lon;
  @Expose
  public String barGainNum;
  @Expose
  public boolean isCollect;
  @Expose
  public String phone2;
  @Expose
  public String userID;
  @Expose
  public String majorOperate;
  @Expose
  public String concurrenOperate;
  @Expose
  public String address;
  @Expose
  public String satisfaction;
  @Expose
  public double lan;
  @Expose
  public String userName;
  @Expose
  public String phone1;
  @Expose
  public String declaration;
  @Expose
  public List<TXProductModel> products;
  @Expose
  public List<Integer> imageIds;
  @Expose
  public String isLikeRecord;
  @Expose
  public int likeRecodCount;
  @Expose
  public String weixinUrl;
  //public  List<MLHomeProductData> imageIds;

  //============

  @Expose
  public String commentCount;
  @Expose
  public String allCount;

  @Expose
  public double redEnvelopeMoney;

  @Expose
  public int redEnvelopeNum;


  @Expose
  public double tradingLimit;

  @Expose
  public String phone3;

  @Expose
  public String phone4;

  @Expose
  public String hxUser;

  @Expose
  public String hxPwd;

  @Expose
  public String gradeNum;
}
