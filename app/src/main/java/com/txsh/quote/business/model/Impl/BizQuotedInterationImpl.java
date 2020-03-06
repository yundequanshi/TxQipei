package com.txsh.quote.business.model.Impl;


import android.app.Activity;
import com.baichang.android.request.HttpSubscriber;
import com.baichang.android.request.HttpSuccessListener;
import com.txsh.quote.QuoteApi;
import com.txsh.quote.QuoteHttpApi;
import com.txsh.quote.business.entity.BizQuotedListData;
import com.txsh.quote.business.model.BizQuotedInteraction;
import java.util.List;
import java.util.Map;

/**
 * Created by 汉玉 on 2017/3/15.
 */
public class BizQuotedInterationImpl implements BizQuotedInteraction {

  @Override
  public void findCompanyOfferSheet(Map<String, String> map, Activity activity,
      final BaseListener<List<BizQuotedListData>> listener) {
    QuoteApi request = new QuoteHttpApi();
    request.findCompanyOfferSheet(map)
        .compose(HttpSubscriber.<List<BizQuotedListData>>applySchedulers(activity))
        .subscribe(new HttpSubscriber<List<BizQuotedListData>>(new HttpSuccessListener<List<BizQuotedListData>>() {
          @Override
          public void success(List<BizQuotedListData> bizQuotedListData) {
            listener.success(bizQuotedListData);
          }
        }));

  }

  @Override
  public void cancel(int key) {

  }
}
