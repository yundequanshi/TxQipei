package com.txsh.quote.deport.entity;

import com.google.gson.annotations.Expose;
import com.txsh.quote.business.entity.BizQuotedPriceData;
import java.io.Serializable;
import java.util.List;

/**
 * Created by 汉玉 on 2017/3/17.
 */
public class CompanyDetailData implements Serializable {

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
  public String companyId;

  @Expose
  public String companyName;

  @Expose
  public BizInfoData company;

  @Expose
  public List<BizQuotedPriceData> parts;
}
