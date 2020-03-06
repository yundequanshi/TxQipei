package com.txsh.quote.deport.present;

import android.app.Activity;
import android.widget.ListView;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.txsh.quote.IBasePresent;

/**
 * Created by 汉玉 on 2017/3/15.
 */
public interface QuotedListPresent extends IBasePresent {

  void initListView(ListView listView, Activity activity);

  void refreshFindDepotOfferSheet(Activity activity);

  void refresh(SwipyRefreshLayoutDirection direction, Activity activity);
}
