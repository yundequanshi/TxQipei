package com.txsh.quote.business.model;

import android.app.Activity;
import com.txsh.quote.IBaseInteraction;
import com.txsh.quote.business.entity.BizQuotedListData;
import java.util.List;
import java.util.Map;

/**
 * Created by 汉玉 on 2017/3/15.
 */
public interface BizQuotedInteraction extends IBaseInteraction {

  void findCompanyOfferSheet(Map<String, String> map, Activity activity,
      BaseListener<List<BizQuotedListData>> listener);
}
