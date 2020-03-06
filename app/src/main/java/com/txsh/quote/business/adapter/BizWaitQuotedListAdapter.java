package com.txsh.quote.business.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.baichang.android.common.BCAdapterBase;
import com.lidroid.xutils.ViewUtils;
import com.txsh.R;
import com.txsh.quote.business.entity.BizQuotedListData;


public class BizWaitQuotedListAdapter extends BCAdapterBase<BizQuotedListData> {

  private TextView tvName;
  private TextView tvNumber;
  private TextView tvTime;

  public BizWaitQuotedListAdapter(Context context, int viewXml) {
    super(context, viewXml);
  }

  @Override
  protected void setItemData(View view, final BizQuotedListData data, int position) {
    tvName = (TextView) view.findViewById(R.id.tv_name);
    tvNumber = (TextView) view.findViewById(R.id.tv_number);
    tvTime = (TextView) view.findViewById(R.id.tv_time);
    tvName.setText(data.title);
    tvNumber.setText("件数：" + data.number);
    tvTime.setText(data.created);
  }

}
