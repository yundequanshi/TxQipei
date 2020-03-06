package com.txsh.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.txsh.model.TXShopPjData;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;

import cn.ml.base.MLAdapterBase;
import cn.ml.base.widget.roundedimageview.RoundedImageView;

/**
 * Created by Administrator on 2015/7/23.
 */
public class TXShopPJAdapter extends MLAdapterBase<TXShopPjData.TXShopPjDataDetail> {
    public TXShopPJAdapter(Context context, int viewXml) {
        super(context, viewXml);
    }
    @ViewInject(R.id.tx_item_pj_iv)
    private RoundedImageView mshopIv;
    @ViewInject(R.id.tx_item_pj_name)
    private TextView userName;
    @ViewInject(R.id.tx_item_pj_time)
    private TextView createTime;
    @ViewInject(R.id.tx_item_pj_content)
    private TextView detail;


    @Override
    protected void setItemData(View view, TXShopPjData.TXShopPjDataDetail data, int position) {
        ViewUtils.inject(this, view);
        String imageUrl = APIConstants.API_IMAGE+"?id="+data.UserImage;
        mshopIv.setTag(imageUrl);
        if (!BaseApplication.IMAGE_CACHE.get(imageUrl, mshopIv)) {
            mshopIv.setImageResource(R.drawable.default_message_header);
        }
        userName.setText(data.userName);
        createTime.setText(data.createTime);
        detail.setText(data.detail);
    }
}
