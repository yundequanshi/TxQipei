package com.zuomei.home;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.ml.base.MLAdapterBase;
import com.bumptech.glide.Glide;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.constants.APIConstants;
import com.zuomei.model.ProMangerData;

public class TxProductMangerAdapter extends MLAdapterBase<ProMangerData> {

  @ViewInject(R.id.iv_photo)
  private ImageView ivPhoto;
  @ViewInject(R.id.tv_name)
  private TextView tvName;

  public TxProductMangerAdapter(Context context, int viewXml) {
    super(context, viewXml);
  }

  @Override
  protected void setItemData(View view, final ProMangerData data, int position) {
    ViewUtils.inject(this, view);
    tvName.setText(data.title);
    Glide.with(mContext)
        .load(APIConstants.API_IMAGE_SHOW + data.image)
        .error(R.drawable.sc_photo)
        .into(ivPhoto);
  }
}
