package com.txsh.quote.deport.model;

import android.app.Activity;
import com.txsh.quote.IBaseInteraction;
import com.txsh.quote.deport.entity.QuotedDetailData;
import java.util.Map;

/**
 * Created by 汉玉 on 2017/3/15.
 */
public interface QuotedDetailInteraction extends IBaseInteraction {

  void getOfferSheetDetail(Map<String, String> map, Activity activity,BaseListener<QuotedDetailData> listener);
}
