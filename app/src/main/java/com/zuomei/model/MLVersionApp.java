package com.zuomei.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2015/7/15.
 */
public class MLVersionApp extends MLBaseResponse {

  @Expose
  public Data datas;

  public class Data extends MLBaseResponse {

    @Expose
    public String version;
    @Expose
    public String path;
  }

}
