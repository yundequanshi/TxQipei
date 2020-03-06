package com.txsh.quote.business.entity;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.List;

/**
 * Created by 汉玉 on 2017/3/18.
 */
public class BizQuotedDetailData implements Serializable {

  @Expose
  public String picture;

  @Expose
  public String id;

  @Expose
  public String logosticsName;

  @Expose
  public String logisticsNo;

  @Expose
  public String allMoney;

  @Expose
  public String title;

  @Expose
  public String state;

  @Expose
  public String sureCompanyId;

  @Expose
  public String phone;

  @Expose
  public String hxPwd;

  @Expose
  public String hxUser;

  @Expose
  public String depotName;

  @Expose
  public String depotId;

  @Expose
  public String depotInfo;

  @Expose
  public String allCompanys;

  @Expose
  public List<BizQuotedPriceData> parts;
}
