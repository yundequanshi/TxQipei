package com.txsh.quote.deport.entity;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.List;

/**
 * Created by 汉玉 on 2017/3/17.
 */
public class QuotedDetailData implements Serializable {

  @Expose
  public String id;

  @Expose
  public String title;

  @Expose
  public String created;

  @Expose
  public String state;

  @Expose
  public String sureCompanyId;

  @Expose
  public List<CompanyData> companys;

  @Expose
  public List<PartsData> parts;

  public class CompanyData implements Serializable {

    @Expose
    public String id;

    @Expose
    public String name;

    @Expose
    public String hxUser;

    @Expose
    public String phone;

    @Expose
    public String hxPwd;

    @Expose
    public String headPic;

    @Expose
    public String offerPrice;

    @Expose
    public String state;

    @Expose
    public String isPurchase = "0";

  }
}
