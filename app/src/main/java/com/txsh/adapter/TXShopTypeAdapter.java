package com.txsh.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.ml.base.MLAdapterBase;
import cn.ml.base.widget.roundedimageview.RoundedImageView;
import com.bumptech.glide.Glide;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.txsh.model.TXShopTypeListRes.TXHomeGoodsTypeImageData;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;
import com.zuomei.model.MLAccidentDetailData;

/**
 * Created by Marcello on 2015/6/3.
 */
public class TXShopTypeAdapter extends MLAdapterBase<TXHomeGoodsTypeImageData> {

  @ViewInject(R.id.iv_type)
  private RoundedImageView ivType;
  @ViewInject(R.id.tv_name)
  private TextView tvName;

  public TXShopTypeAdapter(Context context, int viewXml) {
    super(context, viewXml);
  }


  @Override
  protected void setItemData(View view, final TXHomeGoodsTypeImageData d, final int position) {
    ViewUtils.inject(this, view);
    tvName.setText(d.name);
    if (d.id.equals("-1")) {
      ivType.setImageResource(R.mipmap.icon_more);
    } else {
      String imgUrl = APIConstants.API_IMAGE + "?id=" + d.img;
      ivType.setTag(imgUrl);
      if (!BaseApplication.IMAGE_CACHE.get(imgUrl, ivType)) {
        ivType.setImageDrawable(null);
      }
    }
  }
}