package com.txsh.quote.deport.model;

import android.app.Activity;
import com.baichang.android.request.HttpSubscriber;
import com.txsh.quote.IBaseInteraction;
import java.util.List;
import java.util.Map;

/**
 * Created by 汉玉 on 2017/3/14.
 */
public interface PublishInteraction extends IBaseInteraction {

  void addOfferSheet(Map<String,String> map, Activity activity, BaseListener<Boolean> listener);
}
