package com.txsh.quote;

/**
 * Created by iCong on 2017/3/13.
 */

public class Flag {

  public static final int DEFAULT_PAGE_SIZE = 20;


  //AppDiskCache
  public static final String CACHE_TOKEN = "CACHE_TOKEN";

  //EventBus
  public static final int EVENT_QUOTED_LIST_REFRESH = 10000000;
  public static final int EVENT_CAR_TYPE = 10000001;
  public static final int EVENT_PUBLISH_PRODUCT_MANGER = 10000002;

  public static final int EVENT_BIZ_QUOTED_LIST_REFRESH = 10000001;

  //Activity Intent
  public static final String ACTION_NEWS_ID = "ACTION_NEWS_ID";
}
