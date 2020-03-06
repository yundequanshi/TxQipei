package com.txsh.quote.business.model;

import android.app.Activity;
import com.txsh.quote.IBaseInteraction;
import com.txsh.quote.business.entity.BizQuotedDetailData;
import com.txsh.quote.business.entity.BizQuotedPriceData;
import java.util.List;
import java.util.Map;

/**
 * Created by 汉玉 on 2017/3/15.
 */
public interface BizQuotedDetailInteraction extends IBaseInteraction {

  void getCompanyOfferSheetDetail(Map<String, String> map, Activity activity,
      BaseListener<BizQuotedDetailData> listener);

  void companyOffer(Map<String, String> map, Activity activity,
      BaseListener<Boolean> listener);

  void companySure(Map<String, String> map, Activity activity,
      BaseListener<Boolean> listener);

  void companySend(Map<String, String> map, Activity activity,
      BaseListener<Boolean> listener);
}
