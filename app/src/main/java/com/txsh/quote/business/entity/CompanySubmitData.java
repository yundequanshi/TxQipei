package com.txsh.quote.business.entity;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.List;

/**
 * Created by 汉玉 on 2017/3/18.
 */
public class CompanySubmitData implements Serializable {

  @Expose
  public String id;

  @Expose
  public String name;

  @Expose
  public List<BizQuotedPriceData> partses;

  @Expose
  public String offerPrice;

  @Expose
  public String headPic;

  @Expose
  public String hxUser;

  @Expose
  public String hxPwd;

  @Expose
  public String phone;

  @Expose
  public String offerSheetState;

  @Expose
  public String state;

}
