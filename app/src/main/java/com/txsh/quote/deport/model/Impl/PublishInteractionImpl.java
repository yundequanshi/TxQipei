package com.txsh.quote.deport.model.Impl;

import android.app.Activity;
import com.baichang.android.request.HttpErrorListener;
import com.baichang.android.request.HttpSubscriber;
import com.baichang.android.request.HttpSuccessListener;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.txsh.quote.QuoteApi;
import com.txsh.quote.QuoteHttpApi;
import com.txsh.quote.deport.model.PublishInteraction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 汉玉 on 2017/3/14.
 */
public class PublishInteractionImpl implements PublishInteraction {

  @Override
  public void addOfferSheet(Map<String,String> map, Activity activity, final BaseListener<Boolean> listener) {
    QuoteApi request = new QuoteHttpApi();
    request.addOfferSheet(map)
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
