package com.txsh.quote.deport.present.Impl;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build.VERSION_CODES;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.ml.base.exception.MLParserException;
import cn.ml.base.utils.MLJsonParser;
import com.baichang.android.utils.BCStringUtil;
import com.baichang.android.widget.BCNoScrollListView;
import com.google.gson.Gson;
import com.txsh.R;
import com.txsh.quote.IBaseInteraction.BaseListener;
import com.txsh.quote.deport.adapter.BizQuotedAdapter;
import com.txsh.quote.deport.adapter.PartsPriceAdapter;
import com.txsh.quote.deport.entity.PartsData;
import com.txsh.quote.deport.entity.QuotedDetailData;
import com.txsh.quote.deport.entity.QuotedDetailData.CompanyData;
import com.txsh.quote.deport.entity.QuotedListData;
import com.txsh.quote.deport.entity.QuotedTransferData;
import com.txsh.quote.deport.model.Impl.QuotedDetailInteractionImpl;
import com.txsh.quote.deport.model.QuotedDetailInteraction;
import com.txsh.quote.deport.present.QuotedDetailPresent;
import com.txsh.quote.deport.view.QuotedDetailView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 汉玉 on 2017/3/15.
 */
public class QuotedDetailPresentImpl implements QuotedDetailPresent {

  private QuotedDetailInteraction mInt;
  private QuotedDetailView mView;

  private PartsPriceAdapter partsPriceAdapter;
  private BizQuotedAdapter bizQuotedAdapter;

  public QuotedDetailPresentImpl(QuotedDetailView quotedDetailView) {
    this.mView = quotedDetailView;
    this.mInt = new QuotedDetailInteractionImpl();
  }

  @Override
  public void initListView(BCNoScrollListView lvParts, BCNoScrollListView lvBizQuoted,
      LinearLayout llCompany, List<PartsData> parts, List<CompanyData> company,
      final String quoteId,String sureCompanyId, Activity activity) {
    partsPriceAdapter = new PartsPriceAdapter(activity, R.layout.bj_item_com_biz_quote);
    bizQuotedAdapter = new BizQuotedAdapter(activity, R.layout.bj_item_quoted_detail_biz_list,sureCompanyId);
    lvParts.setAdapter(partsPriceAdapter);
    lvBizQuoted.setAdapter(bizQuotedAdapter);
    lvBizQuoted.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CompanyData companyData = (CompanyData) parent.getItemAtPosition(position);
        QuotedTransferData quotedTransferData = new QuotedTransferData();
        quotedTransferData.quoteId = quoteId;
        quotedTransferData.companyId = companyData.id;
        quotedTransferData.companyName = companyData.name;
        quotedTransferData.companyPhone = companyData.phone;
//        quotedTransferData.isPurchase = companyData.isPurchase;
        mView.startToBizQuoted(quotedTransferData);
      }
    });
    partsPriceAdapter.setData(parts);
    bizQuotedAdapter.setData(company);
    if (company.isEmpty()) {
      llCompany.setVisibility(View.GONE);
    }
  }

  /**
   * 获取报价详情
   */
  @Override
  public void getOfferSheetDetail(QuotedListData quotedListData, Activity activity) {
    Map<String, String> map = new HashMap<>();
    map.put("id", quotedListData.id);
    mInt.getOfferSheetDetail(map, activity, new BaseListener<QuotedDetailData>() {
      @Override
      public void success(QuotedDetailData quotedDetailData) {
        mView.setDetailData(quotedDetailData.title, quotedDetailData.state,
            quotedDetailData.created,
            quotedDetailData.parts, quotedDetailData.companys,quotedDetailData.sureCompanyId);
      }

      @Override
      public void error(String error) {

      }
    });
  }

  /**
   * 设置报价状态
   */
  @TargetApi(VERSION_CODES.JELLY_BEAN)
  @Override
  public void setState(TextView tvState, String state, Activity activity) {
    if (!BCStringUtil.isEmpty(state)) {
      if (state.equals("1")) {//待报价
        tvState.setText(R.string.com_state_2);
        tvState.setBackground(activity.getResources().getDrawable(R.drawable.bj_state_bg1));
        tvState.setTextColor(activity.getResources().getColor(R.color.bj_state1));
      } else if (state.equals("2")) {//已报价
        tvState.setText(R.string.com_state_3);
        tvState.setBackground(activity.getResources().getDrawable(R.drawable.bj_state_bg2));
        tvState.setTextColor(activity.getResources().getColor(R.color.bj_state2));
      } else if (state.equals("3")) {//已采购
        tvState.setText(R.string.com_state_6);
        tvState.setBackground(activity.getResources().getDrawable(R.drawable.bj_state_bg2));
        tvState.setTextColor(activity.getResources().getColor(R.color.bj_state2));
      } else if (state.equals("4")) {//已确认
        tvState.setText(R.string.com_state_1);
        tvState.setBackground(activity.getResources().getDrawable(R.drawable.bj_state_bg2));
        tvState.setTextColor(activity.getResources().getColor(R.color.bj_state2));
      } else if (state.equals("5")) {//待发货
        tvState.setText(R.string.com_state_4);
        tvState.setBackground(activity.getResources().getDrawable(R.drawable.bj_state_bg3));
        tvState.setTextColor(activity.getResources().getColor(R.color.bj_state4));
      } else if (state.equals("6")) {//已发货
        tvState.setText(R.string.com_state_5);
        tvState.setBackground(activity.getResources().getDrawable(R.drawable.bj_state_bg3));
        tvState.setTextColor(activity.getResources().getColor(R.color.bj_state4));
      }
    }
  }

  @Override
  public void onDestroy() {
    mView = null;
  }

  @Override
  public void onStart() {

  }
}
