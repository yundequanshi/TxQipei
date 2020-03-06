package com.txsh.quote.deport.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.baichang.android.common.BCAdapterBase;
import com.txsh.R;
import com.txsh.quote.deport.entity.PartsData;


public class PartsPriceAdapter extends BCAdapterBase<PartsData> {

  private TextView tvName;
  private TextView tvTypeAndNum;

  public PartsPriceAdapter(Context context, int viewXml) {
    super(context, viewXml);
  }

  @Override
  protected void setItemData(View view, final PartsData data, final int position) {
    tvName = (TextView) view.findViewById(R.id.tv_name);
    tvTypeAndNum = (TextView) view.findViewById(R.id.tv_type_and_num);
    tvName.setText(data.name);
    tvTypeAndNum.setText(data.typeName + "   ×" + data.number);
  }

}
