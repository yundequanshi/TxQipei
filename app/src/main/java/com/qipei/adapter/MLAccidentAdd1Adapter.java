package com.qipei.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;

import cn.ml.base.MLAdapterBase;
import cn.ml.base.utils.photo.MLPicData;

/**
 * Created by Marcello on 2015/6/3.
 */
public class MLAccidentAdd1Adapter extends MLAdapterBase<MLPicData> {



    public MLAccidentAdd1Adapter(Context context, int viewXml) {
        super(context, viewXml);
    }

    @ViewInject(R.id.photo_iv)
    private ImageView mIvIcon;

    @Override
    protected void setItemData(View view, final MLPicData d, final int position) {
        ViewUtils.inject(this, view);

        mIvIcon.setImageBitmap(d.mBitMap);
    }
}