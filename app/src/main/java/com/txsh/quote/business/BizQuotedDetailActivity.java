package com.txsh.quote.business;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.baichang.android.widget.BCNoScrollListView;
import com.bumptech.glide.Glide;
import com.txsh.R;
import com.txsh.quote.CommonActivity;
import com.txsh.quote.business.entity.BizQuotedDetailData;
import com.txsh.quote.business.entity.BizQuotedTransferData;
import com.txsh.quote.business.present.BizQuotedDetailPresent;
import com.txsh.quote.business.present.Impl.BizQuotedDetialPresentImpl;
import com.txsh.quote.business.view.BizQuotedDetailView;
import com.zuomei.constants.APIConstants;

public class BizQuotedDetailActivity extends CommonActivity implements BizQuotedDetailView {

  private BCNoScrollListView lvParts;
  private TextView tvAllPrice;
  private ImageView ivQuoteDetail;
  private TextView tvName;
  private TextView tvQuotePrice;
  private TextView tvState;
  private TextView tvSureAndSend;
  private LinearLayout llLogistics;
  private TextView tvLogisticsName;
  private TextView tvLogisticsNumber;

  private BizQuotedDetailPresent mPresent;
  private BizQuotedTransferData bizQuotedTransferData = new BizQuotedTransferData();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_biz_quoted_detail);
    if (getIntentData() != null) {
      bizQuotedTransferData = (BizQuotedTransferData) getIntentData();
    }
    init();
  }

  private void init() {
    lvParts = (BCNoScrollListView) findViewById(R.id.lv_parts);
    tvAllPrice = (TextView) findViewById(R.id.tv_all_price);
    tvQuotePrice = (TextView) findViewById(R.id.tv_quote_price);
    tvState = (TextView) findViewById(R.id.tv_state);
    tvSureAndSend = (TextView) findViewById(R.id.btn_send_and_sure);
    tvName = (TextView) findViewById(R.id.tv_name);
    ivQuoteDetail = (ImageView) findViewById(R.id.iv_quote_detail);
    llLogistics = (LinearLayout) findViewById(R.id.ll_logistics);
    tvLogisticsName = (TextView) findViewById(R.id.tv_logistics_name);
    tvLogisticsNumber = (TextView) findViewById(R.id.tv_logistics_number);
    mPresent = new BizQuotedDetialPresentImpl(this);
    mPresent.getBizQuotedDetailData(bizQuotedTransferData, this);
  }

  public void allOnClick(View view) {
    switch (view.getId()) {
      //发货 确认 提交
      case R.id.btn_send_and_sure:
        mPresent.showSendOrder(findViewById(R.id.rl_main), this);
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
  public void setAllPrice(String allPrice) {
    tvAllPrice.setText(allPrice);
  }

  @Override
  public void setDetailData(BizQuotedDetailData bizQuotedDetailData) {
    mPresent.initListView(lvParts, findViewById(R.id.rl_main), this);
    mPresent.adapterSetDatas(bizQuotedDetailData.parts);
    mPresent.detailSetDatas(bizQuotedDetailData,lvParts, tvName, ivQuoteDetail, tvQuotePrice, tvState,
        llLogistics, tvLogisticsName, tvLogisticsNumber, tvSureAndSend, this);
  }

  @Override
  public void setBtnFont(String font) {
    tvSureAndSend.setText(font);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mPresent.onDestroy();
  }
}
