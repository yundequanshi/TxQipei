package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MLBill2List implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 3079226482468613286L;

  @Expose
  public String id;

  @Expose
  public String logo;

  @Expose
  public String billDate;
  @Expose
  public String companyId;
  @Expose
  public String companyName;
  @Expose
  public String billState;
  @Expose
  public String source;
  @Expose
  public String refuseReason;

}
