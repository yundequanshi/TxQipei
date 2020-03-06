package com.txsh.quote.deport.view;

import com.txsh.quote.IBaseView;
import com.txsh.quote.deport.entity.PartsData;
import com.txsh.quote.deport.entity.QuotedDetailData.CompanyData;
import com.txsh.quote.deport.entity.QuotedTransferData;
import java.util.List;

/**
 * Created by 汉玉 on 2017/3/14.
 */
public interface QuotedDetailView extends IBaseView {

  void startToBizQuoted(QuotedTransferData quotedTransferData);

  void setDetailData(String name, String state, String time, List<PartsData> parts,
      List<CompanyData> company,String sureCompanyId);
}
