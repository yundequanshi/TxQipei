package com.qipei.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.model.MLMyStockDetail;

import cn.ml.base.MLAdapterBase;

/**
 * Created by Marcello on 2015/6/3.
 */
public class MLBusinessOfferAdapter extends MLAdapterBase<MLMyStockDetail> {


    public MLBusinessOfferAdapter(Context context, int viewXml) {
        super(context, viewXml);
    }

  @ViewInject(R.id.add_tv_name)
    private TextView mTvName;

    @ViewInject(R.id.add_tv_count)
    private TextView mTvCount;

    @Override
    protected void setItemData(View view, final MLMyStockDetail data, final int position) {
        ViewUtils.inject(this, view);

        mTvName.setText(data.goodName);
        mTvCount.setText(data.goodNum);
    }
}