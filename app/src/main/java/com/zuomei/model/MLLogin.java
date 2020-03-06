package com.zuomei.model;

import com.google.gson.annotations.Expose;

public class MLLogin extends MLBaseResponse {

  @Expose
  public String Id;

  @Expose
  public String name;

  @Expose
  public String clientNumber;

  @Expose
  public String hxUser;

  @Expose
  public String hxPwd;

  @Expose
  public String headPic;

  @Expose
  public String phone;

  @Expose
  public String clientPwd;

  public boolean isDepot = false;

  public String nowVersion = "";
}
