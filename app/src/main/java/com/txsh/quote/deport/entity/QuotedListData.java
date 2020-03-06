package com.txsh.quote.deport.entity;

import com.google.gson.annotations.Expose;
import java.io.Serializable;

/**
 * Created by 汉玉 on 2017/3/17.
 */
public class QuotedListData implements Serializable {

  @Expose
  public String title;

  @Expose
  public String number;

  @Expose
  public String id;

  @Expose
  public String companyNumber;

  @Expose
  public String created;

  @Expose
  public String state;

  //修理厂
  //1.待报价     2.已报价      3.已采购    4.已确认   5.待发货   6.已发货

  //商家
  //1.待报价     2.已报价      3.未选中或被采购 (根据商家的ID判断)   4.已确认   5.待发货   6.已发货
}
