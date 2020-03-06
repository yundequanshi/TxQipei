package com.txsh.quote.deport.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION_CODES;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.baichang.android.common.BCAdapterBase;
import com.baichang.android.utils.BCStringUtil;
import com.bumptech.glide.Glide;
import com.lidroid.xutils.ViewUtils;
import com.txsh.R;
import com.txsh.quote.deport.entity.QuotedDetailData.CompanyData;
import com.zuomei.constants.APIConstants;


public class BizQuotedAdapter extends BCAdapterBase<CompanyData> {

  private ImageView ivHeadPic;
  private TextView tvName;
  private TextView tvQuotePrice;
  private TextView tvState;
  private String sureCompanyId = "";

  public BizQuotedAdapter(Context context, int viewXml, String sureCompanyId) {
    super(context, viewXml);
    this.sureCompanyId = sureCompanyId;
  }

  @Override
  protected void setItemData(View view, final CompanyData data, int position) {
    ivHeadPic = (ImageView) view.findViewById(R.id.iv_head_pic);
    tvName = (TextView) view.findViewById(R.id.tv_name);
    tvQuotePrice = (TextView) view.findViewById(R.id.tv_quote_price);
    tvState = (TextView) view.findViewById(R.id.tv_state);
    Glide.with(mContext)
        .load(APIConstants.API_IMAGE + "?id=" + data.headPic)
        .asBitmap()
        .error(R.mipmap.bj_default)
        .into(ivHeadPic);
    tvName.setText(data.name);
    tvQuotePrice.setText("报价：" + data.offerPrice + "元");
    if (!BCStringUtil.isEmpty(data.state)) {
      tvState.setVisibility(View.VISIBLE);
      if (data.state.equals("1")) {//待报价
        tvState.setText(R.string.com_state_2);
        tvState.setTextColor(mContext.getResources().getColor(R.color.bj_state1));
      } else if (data.state.equals("2")) {//已报价
        tvState.setText(R.string.com_state_3);
        tvState.setTextColor(mContext.getResources().getColor(R.color.bj_state2));
      } else if (data.state.equals("3")) {//已采购
        if (sureCompanyId.equals(data.id)) {
          tvState.setText(R.string.com_state_6);
          tvState.setTextColor(mContext.getResources().getColor(R.color.bj_state2));
        } else {
          tvState.setText(R.string.biz_state_3);
          tvState.setTextColor(mContext.getResources().getColor(R.color.bj_biz_state1));
        }
      } else if (data.state.equals("4")) {//已确认
        tvState.setText(R.string.com_state_1);
        tvState.setTextColor(mContext.getResources().getColor(R.color.bj_state2));
      } else if (data.state.equals("5")) {//待发货
        tvState.setText(R.string.com_state_4);
        tvState.setTextColor(mContext.getResources().getColor(R.color.bj_state4));
      } else if (data.state.equals("6")) {//已发货
        tvState.setText(R.string.com_state_5);
        tvState.setTextColor(mContext.getResources().getColor(R.color.bj_state4));
      }
    }
  }

}
