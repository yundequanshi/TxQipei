package com.txsh.quote.business.view;

import com.txsh.quote.IBaseView;
import com.txsh.quote.business.entity.BizQuotedDetailData;

/**
 * Created by 汉玉 on 2017/3/15.
 */
public interface BizQuotedDetailView extends IBaseView {

  void setAllPrice(String allPrice);

  void setDetailData(BizQuotedDetailData bizQuotedDetailData);

  void setBtnFont(String font);
}
