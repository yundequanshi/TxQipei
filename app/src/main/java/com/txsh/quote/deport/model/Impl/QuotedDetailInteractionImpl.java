package com.txsh.quote.deport.model.Impl;

import android.app.Activity;
import com.baichang.android.request.HttpSubscriber;
import com.baichang.android.request.HttpSuccessListener;
import com.txsh.quote.QuoteApi;
import com.txsh.quote.QuoteHttpApi;
import com.txsh.quote.deport.entity.QuotedDetailData;
import com.txsh.quote.deport.model.QuotedDetailInteraction;
import java.util.Map;

/**
 * Created by 汉玉 on 2017/3/15.
 */
public class QuotedDetailInteractionImpl implements QuotedDetailInteraction {

  @Override
  public void getOfferSheetDetail(Map<String, String> map, Activity activity,
      final BaseListener<QuotedDetailData> listener) {
    QuoteApi request = new QuoteHttpApi();
    request.getOfferSheetDetail(map)
        .compose(HttpSubscriber.<QuotedDetailData>applySchedulers(activity))
        .subscribe(
            new HttpSubscriber<QuotedDetailData>(new HttpSuccessListener<QuotedDetailData>() {
              @Override
              public void success(QuotedDetailData quotedDetailData) {
                listener.success(quotedDetailData);
              }
            }));
  }

  @Override
  public void cancel(int key) {

  }
}
