package com.qipei.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;
import com.zuomei.model.MLHomeBusinessData;

import cn.ml.base.MLAdapterBase;

/**
 * Created by Marcello on 2015/6/3.
 */
public class MLPartBusinessAdapter extends MLAdapterBase<MLHomeBusinessData> {


    public MLPartBusinessAdapter(Context context, int viewXml) {
        super(context, viewXml);
    }


    @ViewInject(R.id.part_tv_name)
    private TextView mTvName;

    @ViewInject(R.id.part_tv_major)
    private TextView mTvMajor;

    @ViewInject(R.id.part_tv_address)
    private TextView mTvAddress;

    @ViewInject(R.id.part_iv_icon)
    private ImageView nIvIcon;

    @Override
    protected void setItemData(View view, final MLHomeBusinessData data, final int position) {
        ViewUtils.inject(this, view);

        if(data==null){
            return;
        }
        String imgUrl = APIConstants.API_IMAGE+"?id="+data.logo;

        nIvIcon.setTag(imgUrl);
        if (!BaseApplication.IMAGE_CACHE.get(imgUrl, nIvIcon)) {
            nIvIcon.setImageDrawable(null);
        }

        //	BaseApplication.IMAGE_CACHE.get(imgUrl, _headIv);
        //	bitmapUtils.display(_headIv, imgUrl);

        String majorOperate = "";



/*		if(data.majorOperate.size()>1){

		}*/
        for(int i=0;i<data.majorOperate.size();i++){
            if(i==data.majorOperate.size()-1){
                majorOperate=majorOperate+	data.majorOperate.get(i);
            }else{
                majorOperate=majorOperate+	data.majorOperate.get(i)+"、";
            }
        }


        mTvName.setText(data.compayName);
        mTvMajor.setText("主营:"+majorOperate);
        mTvAddress.setText("地址:"+data.address);


    }
}