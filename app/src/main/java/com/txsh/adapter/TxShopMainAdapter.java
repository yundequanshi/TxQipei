package com.txsh.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.txsh.model.TXShopListRes;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;

import cn.ml.base.MLAdapterBase;
import cn.ml.base.utils.MLStrUtil;

/**
 * Created by Marcello on 2015/6/3.
 */
public class TxShopMainAdapter extends MLAdapterBase<TXShopListRes.TXShopListData> {



    public TxShopMainAdapter(Context context, int viewXml) {
        super(context, viewXml);
    }


    @ViewInject(R.id.shop_tv_name)
    private TextView mTvName;
    @ViewInject(R.id.shop_tv_price)
    private TextView mTvPrice;
    @ViewInject(R.id.shop_tv_price_old)
    private TextView mTvPriceOld;
    @ViewInject(R.id.shop_iv_icom)
    private ImageView mIv;



    @Override
    protected void setItemData(View view, final TXShopListRes.TXShopListData data, final int position) {
        ViewUtils.inject(this, view);


        mTvPriceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        if(MLStrUtil.isEmpty(data.oldprivce)||MLStrUtil.compare(data.oldprivce, "0.00")){
            mTvPriceOld.setText("");
        }else{
            mTvPriceOld.setText("¥" + data.oldprivce);
        }

        if(MLStrUtil.compare(data.privce,"0.00")){
            mTvPrice.setText("电话联系");
        }else{
            mTvPrice.setText("¥"+data.privce);
        }

        mTvName.setText(data.name);
        String imgUrl = APIConstants.API_IMAGE_SHOW + data.imgPath;

        mIv.setTag(imgUrl);
        if (!BaseApplication.IMAGE_CACHE.get(imgUrl, mIv)) {
            mIv.setImageResource(R.drawable.shop_default);
        }
    }
}