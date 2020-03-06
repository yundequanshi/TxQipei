package com.txsh.quote.business.entity;

import com.google.gson.annotations.Expose;
import java.io.Serializable;

/**
 * Created by 汉玉 on 2017/3/20.
 */
public class DeportInfoData implements Serializable{

  @Expose
  public String headPic;

  @Expose
  public String hxPwd;

  @Expose
  public String hxUser;

  @Expose
  public String id;

  @Expose
  public String name;

  @Expose
  public String phone;
}
