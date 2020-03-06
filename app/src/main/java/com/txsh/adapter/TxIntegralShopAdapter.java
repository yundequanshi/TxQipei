package com.txsh.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.txsh.model.TXIntegralShopRes;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;

import cn.ml.base.MLAdapterBase;

/**
 * Created by Marcello on 2015/6/3.
 */
public class TxIntegralShopAdapter extends MLAdapterBase<TXIntegralShopRes.TXIntegralProduct> {



    public TxIntegralShopAdapter(Context context, int viewXml) {
        super(context, viewXml);
    }


    @ViewInject(R.id.shop_tv_name)
    private TextView mTvName;
    @ViewInject(R.id.shop_tv_jf)
    private TextView mTvJf;
    @ViewInject(R.id.convert_tv_icom)
    private ImageView mIv;



    @Override
    protected void setItemData(View view, final TXIntegralShopRes.TXIntegralProduct d, final int position) {
        ViewUtils.inject(this, view);

        mTvName.setText(d.title);
        mTvJf.setText(Html.fromHtml(String.format("<font color=\"#279efa\">%s</font> 积分",d.price+"")));

        String imgUrl = APIConstants.API_IMAGE_SHOW+d.images;

        mIv.setTag(imgUrl);
        if (!BaseApplication.IMAGE_CACHE.get(imgUrl, mIv)) {
            mIv.setImageResource(R.drawable.sgc_photo);
        }
    }
}