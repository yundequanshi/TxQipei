package com.txsh.quote.deport.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.baichang.android.common.BCAdapterBase;
import com.txsh.R;
import com.txsh.quote.deport.entity.PartsData;


public class PeijianAdapter extends BCAdapterBase<PartsData> {


  private TextView tvName;
  private TextView tvTypeAndNum;
  private ImageView ivDelete;

  public PeijianAdapter(Context context, int viewXml) {
    super(context, viewXml);
  }


  @Override
  protected void setItemData(View view, final PartsData data, final int position) {
    tvName = (TextView) view.findViewById(R.id.tv_name);
    tvTypeAndNum = (TextView) view.findViewById(R.id.tv_type_and_num);
    ivDelete = (ImageView) view.findViewById(R.id.iv_delete);

    tvName.setText(data.name);
    tvTypeAndNum.setText(data.typeName + "   ×" + data.number);
    ivDelete.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        getList().remove(position);
        notifyDataSetChanged();
      }
    });
  }

}
