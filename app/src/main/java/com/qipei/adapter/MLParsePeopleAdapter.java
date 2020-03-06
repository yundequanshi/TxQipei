package com.qipei.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import cn.ml.base.MLAdapterBase;
import com.baichang.android.widget.roundedImageView.RoundedImageView;
import com.bumptech.glide.Glide;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.qipei.model.ParseInfoData;
import com.txsh.R;
import com.zuomei.constants.APIConstants;

public class MLParsePeopleAdapter extends MLAdapterBase<ParseInfoData> {

  @ViewInject(R.id.roundedImageView)
  RoundedImageView icon;
  @ViewInject(R.id.tv_name)
  TextView tvName;
  @ViewInject(R.id.line)
  View line;

  public MLParsePeopleAdapter(Context context, int viewXml) {
    super(context, viewXml);
  }


  @Override
  protected void setItemData(View view, final ParseInfoData data, final int position) {
    ViewUtils.inject(this, view);
    Glide.with(mContext)
        .load(APIConstants.API_IMAGE + "?id=" + data.headPic)
        .error(R.drawable.default_message_header)
        .into(icon);
    tvName.setText(data.praiseName);
    if (position == getCount() - 1) {
      line.setVisibility(View.GONE);
    } else {
      line.setVisibility(View.VISIBLE);
    }
  }
}