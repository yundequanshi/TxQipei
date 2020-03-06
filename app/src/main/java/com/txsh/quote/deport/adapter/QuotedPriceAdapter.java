package com.txsh.quote.deport.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION_CODES;
import android.view.View;
import android.widget.TextView;
import com.baichang.android.common.BCAdapterBase;
import com.baichang.android.utils.BCStringUtil;
import com.lidroid.xutils.ViewUtils;
import com.txsh.R;
import com.txsh.quote.deport.entity.QuotedListData;


public class QuotedPriceAdapter extends BCAdapterBase<QuotedListData> {

  private TextView tvName;
  private TextView tvState;
  private TextView tvNumber;
  private TextView tvCompanyNumber;
  private TextView tvTime;

  public QuotedPriceAdapter(Context context, int viewXml) {
    super(context, viewXml);
  }

  @TargetApi(VERSION_CODES.JELLY_BEAN)
  @Override
  protected void setItemData(View view, final QuotedListData data, int position) {
    tvName = (TextView) view.findViewById(R.id.tv_name);
    tvState = (TextView) view.findViewById(R.id.tv_state);
    tvNumber = (TextView) view.findViewById(R.id.tv_number);
    tvCompanyNumber = (TextView) view.findViewById(R.id.tv_company_number);
    tvTime = (TextView) view.findViewById(R.id.tv_time);
    tvName.setText(data.title);
    tvNumber.setText("件数：" + data.number);
    tvCompanyNumber.setText("报价商家：" + data.companyNumber);
    tvTime.setText(data.created);
    if (!BCStringUtil.isEmpty(data.state)) {
      if (data.state.equals("1")) {//待报价
        tvState.setText(R.string.com_state_2);
        tvState.setBackground(mContext.getResources().getDrawable(R.drawable.bj_state_bg1));
        tvState.setTextColor(mContext.getResources().getColor(R.color.bj_state1));
      } else if (data.state.equals("2")) {//已报价
        tvState.setText(R.string.com_state_3);
        tvState.setBackground(mContext.getResources().getDrawable(R.drawable.bj_state_bg2));
        tvState.setTextColor(mContext.getResources().getColor(R.color.bj_state2));
      } else if (data.state.equals("3")) {//已采购
        tvState.setText(R.string.com_state_6);
        tvState.setBackground(mContext.getResources().getDrawable(R.drawable.bj_state_bg2));
        tvState.setTextColor(mContext.getResources().getColor(R.color.bj_state2));
      } else if (data.state.equals("4")) {//已确认
        tvState.setText(R.string.com_state_1);
        tvState.setBackground(mContext.getResources().getDrawable(R.drawable.bj_state_bg2));
        tvState.setTextColor(mContext.getResources().getColor(R.color.bj_state2));
      } else if (data.state.equals("5")) {//待发货
        tvState.setText(R.string.com_state_4);
        tvState.setBackground(mContext.getResources().getDrawable(R.drawable.bj_state_bg3));
        tvState.setTextColor(mContext.getResources().getColor(R.color.bj_state4));
      } else if (data.state.equals("6")) {//已发货
        tvState.setText(R.string.com_state_5);
        tvState.setBackground(mContext.getResources().getDrawable(R.drawable.bj_state_bg3));
        tvState.setTextColor(mContext.getResources().getColor(R.color.bj_state4));
      }
    }
  }

}
