package com.txsh.quote.business.entity;

import com.google.gson.annotations.Expose;
import java.io.Serializable;

/**
 * Created by 汉玉 on 2017/3/18.
 */
public class BizQuotedListData implements Serializable {


  @Expose
  public String title;

  @Expose
  public String number;

  @Expose
  public String id;

  @Expose
  public String companyNumber;

  @Expose
  public String sureCompanyId;

  @Expose
  public String created;

  @Expose
  public String state;


  //修理厂
  //1.待报价 （黄）    2.已报价 （绿）     3.已采购  （绿）  4.已确认（绿）   5.待发货 （蓝）  6.已发货（蓝）

  //商家
  //1.待报价     2.已报价 （绿）     3.未选中或被采购 (根据商家的ID判断)（红）（绿）   4.已确认 （绿）  5.待发货  （蓝） 6.已发货（蓝）

}
