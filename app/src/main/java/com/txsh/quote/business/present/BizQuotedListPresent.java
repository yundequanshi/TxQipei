package com.txsh.quote.business.present;

import android.app.Activity;
import android.widget.ListView;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.txsh.quote.IBasePresent;

/**
 * Created by 汉玉 on 2017/3/15.
 */
public interface BizQuotedListPresent extends IBasePresent {

  void showWaitPrice(ListView listview, Activity activity);

  void showAlreadyPrice(ListView listview, Activity activity);

  void refreshDirection(SwipyRefreshLayoutDirection direction, Activity activity);

  void refreshData(Activity activity);
}
