package com.txsh.quote.business.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION_CODES;
import android.view.View;
import android.widget.TextView;
import com.baichang.android.common.BCAdapterBase;
import com.baichang.android.utils.BCStringUtil;
import com.lidroid.xutils.ViewUtils;
import com.txsh.R;
import com.txsh.quote.business.entity.BizQuotedListData;
import com.zuomei.base.MLAppDiskCache;


public class BizAlreadyQuotedListAdapter extends BCAdapterBase<BizQuotedListData> {


  private TextView tvName;
  private TextView tvNumber;
  private TextView tvTime;
  private TextView tvState;

  private String companyId = "";

  public BizAlreadyQuotedListAdapter(Context context, int viewXml) {
    super(context, viewXml);
    companyId = MLAppDiskCache.getLoginUser().Id;
  }

  @TargetApi(VERSION_CODES.JELLY_BEAN)
  @Override
  protected void setItemData(View view, final BizQuotedListData data, int position) {
    tvName = (TextView) view.findViewById(R.id.tv_name);
    tvNumber = (TextView) view.findViewById(R.id.tv_number);
    tvTime = (TextView) view.findViewById(R.id.tv_time);
    tvState = (TextView) view.findViewById(R.id.tv_state);
    tvName.setText(data.title);
    tvNumber.setText("件数：" + data.number);
    tvTime.setText(data.created);
    if (!BCStringUtil.isEmpty(data.state)) {
      if (data.state.equals("1")) {//待报价
        tvState.setText(R.string.biz_state_1);
        tvState.setTextColor(mContext.getResources().getColor(R.color.bj_biz_state2));
      } else if (data.state.equals("2")) {//已报价
        tvState.setText(R.string.biz_state_2);
        tvState.setTextColor(mContext.getResources().getColor(R.color.bj_biz_state3));
      } else if (data.state.equals("3")) {//被采购 未选中 （根据商家ID判断）
        if (data.sureCompanyId.equals(companyId)) {
          tvState.setText(R.string.biz_state_4);
          tvState.setTextColor(mContext.getResources().getColor(R.color.bj_biz_state3));
        }else {
          tvState.setText(R.string.biz_state_3);
          tvState.setTextColor(mContext.getResources().getColor(R.color.bj_biz_state1));
        }
      } else if (data.state.equals("4")) {//已确认
        tvState.setText(R.string.biz_state_5);
        tvState.setTextColor(mContext.getResources().getColor(R.color.bj_biz_state3));
      } else if (data.state.equals("5")) {//待发货
        tvState.setText(R.string.biz_state_6);
        tvState.setTextColor(mContext.getResources().getColor(R.color.bj_biz_state4));
      } else if (data.state.equals("6")) {//已发货
        tvState.setText(R.string.biz_state_7);
        tvState.setTextColor(mContext.getResources().getColor(R.color.bj_biz_state4));
      }
    }

  }

}
