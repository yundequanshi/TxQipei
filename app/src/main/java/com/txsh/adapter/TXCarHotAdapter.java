package com.txsh.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;
import com.zuomei.model.MLHomeCatalogData;

import cn.ml.base.MLAdapterBase;

/**
 * Created by Marcello on 2015/6/3.
 */
public class TXCarHotAdapter extends MLAdapterBase<MLHomeCatalogData> {



    public TXCarHotAdapter(Context context, int viewXml) {
        super(context, viewXml);
    }



    @ViewInject(R.id.car_icon)
    private ImageView mTvIcon;

    @ViewInject(R.id.car_name)
    private TextView mTvName;

    @Override
    public int getCount() {
        if( super.getCount()>10){
            return 10;
        }
        return super.getCount();
    }

    @Override
    protected void setItemData(View view, final MLHomeCatalogData d, final int position) {
        ViewUtils.inject(this, view);
        mTvName.setText(d.name);
        String imgUrl = APIConstants.API_IMAGE + "?id=" + d.imageId;
        //		bitmapUtils.display(ivHead, imgUrl);
        mTvIcon.setTag(imgUrl);
        if (!BaseApplication.IMAGE_CACHE.get(imgUrl, mTvIcon)) {
         //   mTvIcon.setImageResource(R.drawable.test1);
            mTvIcon.setImageDrawable(null);
        }
    }
}