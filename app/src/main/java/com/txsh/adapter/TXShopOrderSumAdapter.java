package com.txsh.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.txsh.model.TXShopBuyRes;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;

import cn.ml.base.MLAdapterBase;

/**
 * Created by Marcello on 2015/6/3.
 */
public class TXShopOrderSumAdapter extends MLAdapterBase<TXShopBuyRes.TXShopProductData> {



    public TXShopOrderSumAdapter(Context context, int viewXml) {
        super(context, viewXml);
    }



    @ViewInject(R.id.order_iv)
    private ImageView mTvIcon;

    @ViewInject(R.id.order_tv_name)
    private TextView mTvName;

    @ViewInject(R.id.order_tv_price)
    private TextView mTvPrice;


    @Override
    protected void setItemData(View view, final TXShopBuyRes.TXShopProductData data, final int position) {
        ViewUtils.inject(this, view);
        mTvName.setText(data.productName);
        String imgUrl = APIConstants. API_IMAGE_SHOW+ data.productPicture;
        //		bitmapUtils.display(ivHead, imgUrl);
        mTvIcon.setTag(imgUrl);
        if (!BaseApplication.IMAGE_CACHE.get(imgUrl, mTvIcon)) {
               mTvIcon.setImageResource(R.drawable.shop_default);
        }

        mTvPrice.setText(data.productPrice);
    }
}