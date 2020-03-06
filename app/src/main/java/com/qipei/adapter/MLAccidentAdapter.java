package com.qipei.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;
import com.zuomei.model.MLAccidentDetailData;
import com.zuomei.model.MLAccidentInfo;
import com.zuomei.utils.MLStringUtils;

import cn.ml.base.MLAdapterBase;

/**
 * Created by Marcello on 2015/6/3.
 */
public class MLAccidentAdapter extends MLAdapterBase<MLAccidentDetailData> {



    public MLAccidentAdapter(Context context, int viewXml) {
        super(context, viewXml);
    }


    @ViewInject(R.id.accident_iv_name)
    private TextView mTvName;

    @ViewInject(R.id.accident_tv_mileage)
    private TextView mTvMileage;

    @ViewInject(R.id.accident_tv_time)
    private TextView mTvTime;

    @ViewInject(R.id.accident_tv_price)
    private TextView mTvPrice;

    @ViewInject(R.id.accident_iv_icon)
    private ImageView mTvIcon;

    @Override
    protected void setItemData(View view, final MLAccidentDetailData d, final int position) {
        ViewUtils.inject(this, view);

        MLAccidentInfo data = d.info;
        if(data==null) return;
        mTvName.setText(data.accidentName);
        String time = MLStringUtils.time_year(data.MCtime);
        mTvMileage.setText(data.mileage+"公里 | "+data.city+"   "+time);


        String price = String.format("<font color=\"#ff0000\">%s</font> 万元",data.price+"");

        mTvPrice.setText(Html.fromHtml(price));

        mTvTime.setText(time);
        String id ="";
        if(data.companyLogo.equalsIgnoreCase("0")){
            id = data.depotLogo;
        }else{
            id = data.companyLogo;
        }

       /* if(data.state.equalsIgnoreCase("已售")){
            _state.setText("已售");
        }else{
            _state.setText("");
        }*/

        String imgUrl = APIConstants.API_IMAGE+"?id="+id;
        //	BaseApplication.IMAGE_CACHE.get(imgUrl, _imageIv);

        mTvIcon.setTag(imgUrl);
        if (!BaseApplication.IMAGE_CACHE.get(imgUrl, mTvIcon)) {
            mTvIcon.setImageResource(R.drawable.sgc_photo);
        }
    }
}