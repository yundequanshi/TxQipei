package com.txsh.quote.deport.present;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.baichang.android.widget.BCNoScrollListView;
import com.txsh.quote.IBasePresent;
import com.txsh.quote.business.entity.BizQuotedDetailData;
import com.txsh.quote.business.entity.BizQuotedPriceData;
import com.txsh.quote.deport.BizQuotedDetailInDepotActivity;
import com.txsh.quote.deport.entity.CompanyDetailData;
import com.txsh.quote.deport.entity.QuotedTransferData;
import java.util.List;

/**
 * Created by 汉玉 on 2017/3/15.
 */
public interface BizQuotedPresent extends IBasePresent {

  void initListView(BCNoScrollListView lvParts, Activity activity);

  void sureAccept(QuotedTransferData quotedTransferData, Activity activity);

  void getBizQuotedDetailData(QuotedTransferData quotedTransferData,
      Activity activity);

  void call(Activity activity);

  void talk(Activity activity);

  void adapterSetDatas(List<BizQuotedPriceData> parts);

  void detailSetDatas(CompanyDetailData bizQuotedDetailData, TextView tvName,
      ImageView ivQuoteDetail, TextView tvQuotePrice, TextView tvState, LinearLayout llLogistics,
      TextView tvLogisticsName, TextView tvLogisticsNumber, TextView tvSureAndSend,
      Activity activity);
}
