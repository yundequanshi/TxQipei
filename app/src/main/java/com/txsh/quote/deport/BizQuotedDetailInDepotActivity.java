package com.txsh.quote.deport;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.baichang.android.widget.BCNoScrollListView;
import com.txsh.R;
import com.txsh.quote.CommonActivity;
import com.txsh.quote.business.entity.BizQuotedDetailData;
import com.txsh.quote.deport.entity.CompanyDetailData;
import com.txsh.quote.deport.entity.QuotedDetailData;
import com.txsh.quote.deport.entity.QuotedDetailData.CompanyData;
import com.txsh.quote.deport.entity.QuotedTransferData;
import com.txsh.quote.deport.present.BizQuotedPresent;
import com.txsh.quote.deport.present.Impl.BizQuotedPresentImpl;
import com.txsh.quote.deport.view.BizQuotedView;

public class BizQuotedDetailInDepotActivity extends CommonActivity implements BizQuotedView {

  private BCNoScrollListView lvParts;
  private ImageView ivQuoteDetail;
  private TextView tvName;
  private TextView tvQuotePrice;
  private TextView tvState;
  private TextView tvSureAndSend;
  private LinearLayout llLogistics;
  private TextView tvLogisticsName;
  private TextView tvLogisticsNumber;

  private BizQuotedPresent mPresent;
  private QuotedTransferData quotedTransferData = new QuotedTransferData();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_biz_quoted);
    if (getIntentData() != null) {
      quotedTransferData = (QuotedTransferData) getIntentData();
    }
    init();
  }

  private void init() {
    mPresent = new BizQuotedPresentImpl(this);
    lvParts = (BCNoScrollListView) findViewById(R.id.lv_peijian);
    tvQuotePrice = (TextView) findViewById(R.id.tv_quote_price);
    tvState = (TextView) findViewById(R.id.tv_state);
    tvSureAndSend = (TextView) findViewById(R.id.btn_accept);
    tvName = (TextView) findViewById(R.id.tv_name);
    ivQuoteDetail = (ImageView) findViewById(R.id.iv_quote_detail);
    llLogistics = (LinearLayout) findViewById(R.id.ll_logistics);
    tvLogisticsName = (TextView) findViewById(R.id.tv_logistics_name);
    tvLogisticsNumber = (TextView) findViewById(R.id.tv_logistics_number);
    mPresent.initListView(lvParts, this);
    mPresent.getBizQuotedDetailData(quotedTransferData, this);
  }

  public void allOnClick(View view) {
    switch (view.getId()) {
      //确认采购
      case R.id.btn_accept:
        mPresent.sureAccept(quotedTransferData, this);
        break;
      //一键拨打
      case R.id.ll_call:
        mPresent.call(this);
        break;
      //私信
      case R.id.ll_talk:
        mPresent.talk(this);
        break;
    }
  }

  @Override
  public void showProgressBar() {

  }

  @Override
  public void hideProgressBar() {

  }

  @Override
  public void showMsg(String msg) {
    showMessage(msg);
  }

  @Override
  public void setDetailData(CompanyDetailData companyDetailData) {
    mPresent.initListView(lvParts, this);
    mPresent.adapterSetDatas(companyDetailData.parts);
    mPresent.detailSetDatas(companyDetailData, tvName, ivQuoteDetail, tvQuotePrice, tvState,
        llLogistics, tvLogisticsName, tvLogisticsNumber, tvSureAndSend, this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mPresent.onDestroy();
  }
}
