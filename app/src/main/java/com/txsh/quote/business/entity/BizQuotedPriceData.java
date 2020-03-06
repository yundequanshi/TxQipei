package com.txsh.quote.business.entity;

import com.google.gson.annotations.Expose;
import java.io.Serializable;

/**
 * Created by 汉玉 on 2017/3/16.
 */
public class BizQuotedPriceData implements Serializable {

  @Expose
  public String typeName;

  @Expose
  public String number;

  @Expose
  public String typeId;

  @Expose
  public String name;

  @Expose
  public String money;
}
