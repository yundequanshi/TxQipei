package com.zuomei.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 汉玉 on 2017/3/9.
 */
public class UserCityData implements Serializable {

  public List<UserCity> datas = new ArrayList<>();

  public class UserCity implements Serializable {

    public String userId;

    public String cityId;

    public String username;

    public String cityName;
  }
}
