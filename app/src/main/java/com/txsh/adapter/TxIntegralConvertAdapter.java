package com.txsh.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.txsh.model.TXIntegralShopConvertRes;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;

import cn.ml.base.MLAdapterBase;

/**
 * Created by Marcello on 2015/6/3.
 */
public class TxIntegralConvertAdapter extends MLAdapterBase<TXIntegralShopConvertRes.TXIntegralConvert> {



    public TxIntegralConvertAdapter(Context context, int viewXml) {
        super(context, viewXml);
    }


    @ViewInject(R.id.convert_tv_name)
    private TextView mTvName;
    @ViewInject(R.id.convert_tv_jf)
    private TextView mTvJf;
    @ViewInject(R.id.convert_tv_icom)
    private ImageView mIv;

    @ViewInject(R.id.shop_tv_time)
    private TextView mTvTime;
/*    @ViewInject(R.id.convert_tv_state)
    private TextView mTvState;*/



    @Override
    protected void setItemData(View view, final TXIntegralShopConvertRes.TXIntegralConvert d, final int position) {
        ViewUtils.inject(this, view);

        mTvName.setText(""+d.title);
        mTvJf.setText(Html.fromHtml(String.format("<font color=\"#279efa\">%s</font> 分", d.price + "")));

        String imgUrl = APIConstants.API_IMAGE_SHOW+d.images;

        mIv.setTag(imgUrl);
        if (!BaseApplication.IMAGE_CACHE.get(imgUrl, mIv)) {
            mIv.setImageResource(R.drawable.sgc_photo);
        }
        mTvTime.setText(d.createTime);
   //     mTvState.setText(d.);
    }
}