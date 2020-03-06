package com.txsh.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.txsh.model.TXInfoRes;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;

import cn.ml.base.MLAdapterBase;

/**
 * Created by Marcello on 2015/6/3.
 */
public class TXInfoAdapter extends MLAdapterBase<TXInfoRes.TXInfoData> {



    public TXInfoAdapter(Context context, int viewXml) {
        super(context, viewXml);
    }



    @ViewInject(R.id.groupon_tv_time)
    private TextView mTvTime;

    @ViewInject(R.id.groupon_iv)
    private ImageView mTvIcon;

    @ViewInject(R.id.groupon_tv_title)
    private TextView mTvTitle;



    @Override
    protected void setItemData(View view, final TXInfoRes.TXInfoData d, final int position) {
        ViewUtils.inject(this, view);

        mTvTitle.setText(d.title);
        mTvTime.setText(d.createTime);

        String imgUrl = APIConstants.API_IMAGE_SHOW+d.imagePath;
        mTvIcon.setTag(imgUrl);
        if (!BaseApplication.IMAGE_CACHE.get(imgUrl, mTvIcon)) {
            mTvIcon.setImageResource(R.drawable.sgc_photo);
        }
    }
}