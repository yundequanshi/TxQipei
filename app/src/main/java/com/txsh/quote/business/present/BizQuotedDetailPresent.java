package com.txsh.quote.business.present;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.baichang.android.widget.BCNoScrollListView;
import com.txsh.quote.IBasePresent;
import com.txsh.quote.business.entity.BizQuotedDetailData;
import com.txsh.quote.business.entity.BizQuotedPriceData;
import com.txsh.quote.business.entity.BizQuotedTransferData;
import java.util.List;

/**
 * Created by 汉玉 on 2017/3/15.
 */
public interface BizQuotedDetailPresent extends IBasePresent {

  void initListView(BCNoScrollListView lvParts, View mainView, Activity activity);

  void showSendOrder(View view, Activity activity);

  void adapterSetDatas(List<BizQuotedPriceData> parts);

  void detailSetDatas(
      BizQuotedDetailData bizQuotedDetailData,
      ListView lvParts,
      TextView tvName,
      ImageView ivQuoteDetail,
      TextView tvQuotePrice,
      TextView tvState,
      LinearLayout llLogistics,
      TextView tvLogisticsName,
      TextView tvLogisticsNumber,
      TextView tvSureAndSend,
      Activity activity);

  void getBizQuotedDetailData(BizQuotedTransferData bizQuotedTransferData, Activity activity);

  void call(Activity activity);

  void talk(Activity activity);
}
