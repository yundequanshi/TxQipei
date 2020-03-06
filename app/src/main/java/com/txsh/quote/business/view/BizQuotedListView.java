package com.txsh.quote.business.view;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.txsh.quote.IBaseView;
import com.txsh.quote.business.entity.BizQuotedTransferData;

/**
 * Created by 汉玉 on 2017/3/15.
 */
public interface BizQuotedListView extends IBaseView {

  void startToDetail(BizQuotedTransferData bizQuotedTransferData);

  void setRefreshDirection(SwipyRefreshLayoutDirection direction);
}
