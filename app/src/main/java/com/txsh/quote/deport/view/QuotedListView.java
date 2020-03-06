package com.txsh.quote.deport.view;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.txsh.quote.IBaseView;
import com.txsh.quote.deport.entity.QuotedListData;

/**
 * Created by 汉玉 on 2017/3/14.
 */
public interface QuotedListView extends IBaseView {

  void startToOtherActivity(Class cls,QuotedListData quotedListData);

  void setRefreshDirection(SwipyRefreshLayoutDirection direction);
}
