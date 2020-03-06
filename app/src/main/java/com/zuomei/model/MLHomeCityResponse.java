package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public class MLHomeCityResponse extends MLBaseResponse {

  @Expose
  public MainCityData datas;


  public class MainCityData implements Serializable {

    private static final long serialVersionUID = 7005751198198729413L;

    @Expose
    public List<MLHomeCityData> cityList;

    @Expose
    public List<MLHomeCityData> hotCityList;
  }

}
