package com.txsh.quote.business.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.baichang.android.common.BCAdapterBase;
import com.baichang.android.utils.BCStringUtil;
import com.baichang.android.utils.BCToolsUtil;
import com.txsh.R;
import com.txsh.quote.business.entity.BizQuotedPriceData;


public class BizQuotedDetailAdapter extends BCAdapterBase<BizQuotedPriceData> {


  private TextView tvPrice;
  private TextView tvName;
  private TextView tvTypeAndNum;

  public BizQuotedDetailAdapter(Context context, int viewXml) {
    super(context, viewXml);
  }

  @Override
  protected void setItemData(View view, final BizQuotedPriceData data, int position) {
    tvPrice = (TextView) view.findViewById(R.id.tv_price);
    tvName = (TextView) view.findViewById(R.id.tv_name);
    tvTypeAndNum = (TextView) view.findViewById(R.id.tv_type_and_num);
    tvName.setText(data.name);
    tvTypeAndNum.setText(data.typeName + "   ×" + data.number);
    if (!BCStringUtil.isEmpty(data.money)) {
      tvPrice.setText(BCToolsUtil.numberFormat(Double.parseDouble(data.money), "0.00") + " 元");
    } else {
      tvPrice.setText("0.00 元");
    }
  }

}
