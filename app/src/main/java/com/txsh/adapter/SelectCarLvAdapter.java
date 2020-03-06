package com.txsh.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.ml.base.MLAdapterBase;
import com.bumptech.glide.Glide;
import com.easemob.easeui.utils.APIConstants;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.txsh.model.SelectCarTypeData;
import com.zuomei.base.BaseApplication;
import com.zuomei.model.MLHomeCatalogData;


/**
 * Created by xuhanyu on 2016/6/29.
 */
public class SelectCarLvAdapter extends MLAdapterBase<MLHomeCatalogData> {

  @ViewInject(R.id.tv_name)
  private TextView tvName;
  @ViewInject(R.id.iv_icon)
  ImageView icon;

  public SelectCarLvAdapter(Context context, int viewXml) {
    super(context, viewXml);
  }

  @Override
  protected void setItemData(View view, final MLHomeCatalogData data, int position) {
    ViewUtils.inject(this, view);
    tvName.setText(data.name);
    String imgUrl = com.zuomei.constants.APIConstants.API_IMAGE + "?id=" + data.imageId;
    icon.setTag(imgUrl);
    if (!BaseApplication.IMAGE_CACHE.get(imgUrl, icon)) {
      icon.setImageDrawable(null);
    }
  }
}
