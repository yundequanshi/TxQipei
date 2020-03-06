package com.txsh.quote.business.model.Impl;

import android.app.Activity;
import com.baichang.android.request.HttpSubscriber;
import com.baichang.android.request.HttpSuccessListener;
import com.txsh.quote.QuoteApi;
import com.txsh.quote.QuoteHttpApi;
import com.txsh.quote.business.entity.BizQuotedDetailData;
import com.txsh.quote.business.entity.BizQuotedListData;
import com.txsh.quote.business.entity.BizQuotedPriceData;
import com.txsh.quote.business.model.BizQuotedDetailInteraction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by 汉玉 on 2017/3/15.
 */
public class BizQuotedDetailInteractionImpl implements BizQuotedDetailInteraction {

  @Override
  public void getCompanyOfferSheetDetail(Map<String, String> map, Activity activity,
      final BaseListener<BizQuotedDetailData> listener) {
    QuoteApi request = new QuoteHttpApi();
    request.getCompanyOfferSheetDetail2(map)
        .compose(HttpSubscriber.<BizQuotedDetailData>applySchedulers(activity))
        .subscribe(new HttpSubscriber<BizQuotedDetailData>(
            new HttpSuccessListener<BizQuotedDetailData>() {
              @Override
              public void success(BizQuotedDetailData bizQuotedDetailData) {
                listener.success(bizQuotedDetailData);
              }
            }));

  }

  @Override
  public void companyOffer(Map<String, String> map, Activity activity,
      final BaseListener<Boolean> listener) {
    QuoteApi request = new QuoteHttpApi();
    request.companyOffer(map)
        .compose(HttpSubscriber.<Boolean>applySchedulers(activity))
        .subscribe(new HttpSubscriber<Boolean>(
            new HttpSuccessListener<Boolean>() {
              @Override
              public void success(Boolean bo) {
                listener.success(bo);
              }
            }));
  }

  @Override
  public void companySure(Map<String, String> map, Activity activity,
      final BaseListener<Boolean> listener) {
    QuoteApi request = new QuoteHttpApi();
    request.companySure(map)
        .compose(HttpSubscriber.<Boolean>applySchedulers(activity))
        .subscribe(new HttpSubscriber<Boolean>(
            new HttpSuccessListener<Boolean>() {
              @Override
              public void success(Boolean bo) {
                listener.success(bo);
              }
            }));
  }

  @Override
  public void companySend(Map<String, String> map, Activity activity,
      final BaseListener<Boolean> listener) {
    QuoteApi request = new QuoteHttpApi();
    request.companySend(map)
        .compose(HttpSubscriber.<Boolean>applySchedulers(activity))
        .subscribe(new HttpSubscriber<Boolean>(
            new HttpSuccessListener<Boolean>() {
              @Override
              public void success(Boolean bo) {
                listener.success(bo);
              }
            }));
  }

  @Override
  public void cancel(int key) {

  }
}
