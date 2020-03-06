package com.txsh.quote.deport.present;

import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.baichang.android.widget.BCNoScrollListView;
import com.txsh.quote.IBasePresent;
import com.txsh.quote.deport.entity.PartsData;
import com.txsh.quote.deport.entity.QuotedDetailData.CompanyData;
import com.txsh.quote.deport.entity.QuotedListData;
import java.util.List;

/**
 * Created by 汉玉 on 2017/3/15.
 */
public interface QuotedDetailPresent extends IBasePresent {

  void initListView(BCNoScrollListView lvParts, BCNoScrollListView lvBizQuoted,
      LinearLayout llCompany,
      List<PartsData> parts,
      List<CompanyData> company, String quoteId,String sureCompanyId, Activity activity);

  void getOfferSheetDetail(QuotedListData quotedListData, Activity activity);

  void setState(TextView textView, String state, Activity activity);
}
