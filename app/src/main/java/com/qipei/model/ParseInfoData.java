package com.qipei.model;

import com.google.gson.annotations.Expose;
import java.io.Serializable;

/**
 * Created by 汉玉 on 2017/6/21.
 */
public class ParseInfoData implements Serializable {

  @Expose
  public String praiseId;

  @Expose
  public String flag;

  @Expose
  public String praiseName;

  @Expose
  public String hxUser;

  @Expose
  public String headPic;

  @Expose
  public String phone;

  public ParseInfoData(String flag, String praiseId, String praiseName, String hxUser,
      String headPic, String phone) {
    this.flag = flag;
    this.praiseId = praiseId;
    this.praiseName = praiseName;
    this.hxUser = hxUser;
    this.headPic = headPic;
    this.phone = phone;
  }

  @Override
  public boolean equals(Object o) {
    ParseInfoData data = (ParseInfoData) o;
    return this.praiseId.equals(data.praiseId)
        && this.praiseName.equals(data.praiseName);
  }
}
