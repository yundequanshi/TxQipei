package com.zuomei.base;

import com.txsh.model.HxUserLoginData;
import com.zuomei.constants.MLConstants;
import com.zuomei.model.MLLogin;
import com.zuomei.model.UserCityData;

public class MLAppDiskCache {

  /**
   * 用户登陆信息
   */
  public static void setUser(HxUserLoginData data) {
    BaseApplication.aCache.put(MLConstants.ACACHE_CM_USER, data);
  }

  public static HxUserLoginData getUser() {
    Object obj = BaseApplication.aCache.getAsObject(MLConstants.ACACHE_CM_USER);
    if (obj == null) {
      return null;
    }
    return (HxUserLoginData) obj;
  }

  //微信区别
  public static void setWeixin(String data) {
    BaseApplication.aCache.put(MLConstants.ROOM_TRADE_DEVELOPER_PAY_IMAGE, data);
  }

  public static String getWeixin() {
    String obj = BaseApplication.aCache.getAsString(MLConstants.ROOM_TRADE_DEVELOPER_PAY_IMAGE);
    if (obj == null) {
      return null;
    }
    return (String) obj;
  }

  //城市选择
  public static void setCity(UserCityData data) {
    BaseApplication.aCache.put(MLConstants.ACACHE_CM_CITY, data);
  }

  public static UserCityData getCity() {
    Object obj = BaseApplication.aCache.getAsObject(MLConstants.ACACHE_CM_CITY);
    if (obj == null) {
      return null;
    }
    return (UserCityData) obj;
  }

  /**
   * 用户登录信息
   */
  public static void setLoginUser(MLLogin data) {
    BaseApplication.aCache.put(MLConstants.ACACHE_CM_LOGIN_USER, data);
  }

  public static MLLogin getLoginUser() {
    Object obj = BaseApplication.aCache.getAsObject(MLConstants.ACACHE_CM_LOGIN_USER);
    if (obj == null) {
      return null;
    }
    return (MLLogin) obj;
  }
}
