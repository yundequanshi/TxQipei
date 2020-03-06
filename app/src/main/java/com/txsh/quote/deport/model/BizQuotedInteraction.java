package com.txsh.quote.deport.model;

import android.app.Activity;
import com.txsh.quote.IBaseInteraction;
import com.txsh.quote.deport.entity.CompanyDetailData;
import java.util.Map;

/**
 * Created by 汉玉 on 2017/3/15.
 */
public interface BizQuotedInteraction extends IBaseInteraction {

  void getCompanyOfferSheetDetail(Map<String, String> map, Activity activity,
      BaseListener<CompanyDetailData> listener);

  void purchase(Map<String, String> map, Activity activity,
      BaseListener<Boolean> listener);
}
