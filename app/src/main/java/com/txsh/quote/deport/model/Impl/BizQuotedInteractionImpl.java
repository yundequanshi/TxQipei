package com.txsh.quote.deport.model.Impl;

import android.app.Activity;
import com.baichang.android.request.HttpSubscriber;
import com.baichang.android.request.HttpSuccessListener;
import com.txsh.quote.QuoteApi;
import com.txsh.quote.QuoteHttpApi;
import com.txsh.quote.deport.entity.CompanyDetailData;
import com.txsh.quote.deport.model.BizQuotedInteraction;
import java.util.Map;

/**
 * Created by 汉玉 on 2017/3/15.
 */
public class BizQuotedInteractionImpl implements BizQuotedInteraction {

  @Override
  public void getCompanyOfferSheetDetail(Map<String, String> map, Activity activity,
      final BaseListener<CompanyDetailData> listener) {

    QuoteApi request = new QuoteHttpApi();
    request.getCompanyOfferSheetDetail(map)
        .compose(HttpSubscriber.<CompanyDetailData>applySchedulers(activity))
        .subscribe(new HttpSubscriber<CompanyDetailData>(new HttpSuccessListener<CompanyDetailData>() {
          @Override
          public void success(CompanyDetailData companyDetailData) {
            listener.success(companyDetailData);
          }
        }));

  }

  @Override
  public void purchase(Map<String, String> map, Activity activity, final BaseListener<Boolean> listener) {
    QuoteApi request = new QuoteHttpApi();
    request.purchase(map)
        .compose(HttpSubscriber.<Boolean>applySchedulers(activity))
        .subscribe(new HttpSubscriber<Boolean>(new HttpSuccessListener<Boolean>() {
          @Override
          public void success(Boolean aBoolean) {
            listener.success(aBoolean);
          }
        }));
  }

  @Override
  public void cancel(int key) {

  }
}
