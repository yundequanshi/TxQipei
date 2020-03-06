package com.txsh.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by lingyun on 2016/5/9.
 */
public class HxUserLoginData implements Serializable {

  @Expose
  public String hxPwd = "";

  @Expose
  public String userId = "";

  @Expose
  public String hxUser = "";
  @Expose
  public String hxKid = "";
  @Expose
  public String userType = "";
}
