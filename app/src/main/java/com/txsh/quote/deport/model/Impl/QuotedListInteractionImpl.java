package com.txsh.quote.deport.model.Impl;

import android.app.Activity;
import com.baichang.android.request.HttpSubscriber;
import com.baichang.android.request.HttpSuccessListener;
import com.txsh.quote.QuoteApi;
import com.txsh.quote.QuoteHttpApi;
import com.txsh.quote.deport.entity.QuotedListData;
import com.txsh.quote.deport.model.QuotedListInteraction;
import java.util.List;
import java.util.Map;

/**
 * Created by 汉玉 on 2017/3/15.
 */
public class QuotedListInteractionImpl implements QuotedListInteraction {

  @Override
  public void findDepotOfferSheet(Map<String, String> map, Activity activity,
      final BaseListener<List<QuotedListData>> listener) {
    QuoteApi request = new QuoteHttpApi();
    request.findDepotOfferSheet(map)
        .compose(HttpSubscriber.<List<QuotedListData>>applySchedulers(activity))
        .subscribe(new HttpSubscriber<List<QuotedListData>>(
            new HttpSuccessListener<List<QuotedListData>>() {
              @Override
              public void success(List<QuotedListData> quotedListDatas) {
                listener.success(quotedListDatas);
              }
            }));

  }

  @Override
  public void cancel(int key) {

  }
}
