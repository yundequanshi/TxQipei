package com.txsh.quote.deport.model;

import android.app.Activity;
import com.txsh.quote.IBaseInteraction;
import com.txsh.quote.deport.entity.QuotedListData;
import java.util.List;
import java.util.Map;

/**
 * Created by 汉玉 on 2017/3/14.
 */
public interface QuotedListInteraction extends IBaseInteraction {
  void findDepotOfferSheet(Map<String,String> map,Activity activity,BaseListener<List<QuotedListData>> listener);
}
