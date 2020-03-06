package com.txsh.quote.deport.view;

import com.txsh.quote.IBaseView;
import com.txsh.quote.business.entity.BizQuotedDetailData;
import com.txsh.quote.deport.entity.CompanyDetailData;

/**
 * Created by 汉玉 on 2017/3/14.
 */
public interface BizQuotedView extends IBaseView {

  void setDetailData(CompanyDetailData bizQuotedDetailData);


}
