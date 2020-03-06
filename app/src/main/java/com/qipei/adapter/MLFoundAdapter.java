package com.qipei.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.qipei.model.MLFoundItem;
import com.txsh.R;

import cn.ml.base.MLAdapterBase;

/**
 * Created by Marcello on 2015/6/3.
 */
public class MLFoundAdapter extends MLAdapterBase<MLFoundItem> {



    public MLFoundAdapter(Context context, int viewXml) {
        super(context, viewXml);
    }


    @ViewInject(R.id.found_iv_icon)
    private ImageView mIvIcon;

    @ViewInject(R.id.found_tv_content)
    private TextView mTvContent;

    @Override
    protected void setItemData(View view, final MLFoundItem data, final int position) {
        ViewUtils.inject(this, view);

        if(data.imageRes==0){
            mIvIcon.setImageDrawable(null);
        }else{
            mIvIcon.setImageDrawable(mContext.getResources().getDrawable(data.imageRes));
        }

        mTvContent.setText(data.content);
    }
}