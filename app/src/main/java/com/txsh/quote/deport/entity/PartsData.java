package com.txsh.quote.deport.entity;

import com.google.gson.annotations.Expose;
import java.io.Serializable;

/**
 * Created by 汉玉 on 2017/3/14.
 */
public class PartsData implements Serializable {


  public PartsData() {
  }

  public PartsData(String name, String num, String typeName, String typeId) {
    this.name = name;
    this.number = num;
    this.typeName = typeName;
    this.typeId = typeId;
  }

  @Expose
  public String name;

  @Expose
  public String number;

  @Expose
  public String typeName;

  @Expose
  public String money;

  @Expose
  public String typeId;
}
