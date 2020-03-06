package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MLDepotData implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 9144554929327102937L;

  @Expose
  public String address;

  @Expose
  public String depotName;

  @Expose
  public String id;

  @Expose
  public double latitude;

  @Expose
  public String location;

  @Expose
  public double longitude;

  @Expose
  public String userPhone;

  @Expose
  public String userPhoto;

  //商家
  @Expose
  public String logo;

  @Expose
  public String companyName;
}
