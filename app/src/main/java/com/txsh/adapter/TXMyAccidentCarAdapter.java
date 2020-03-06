package com.txsh.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.txsh.model.TXEventModel;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.model.MLAccidentDetailData;
import com.zuomei.model.MLAccidentInfo;
import com.zuomei.utils.MLStringUtils;

import cn.ml.base.MLAdapterBase;
import cn.ml.base.utils.MLStrUtil;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by Marcello on 2015/6/3.
 */
public class TXMyAccidentCarAdapter extends MLAdapterBase<MLAccidentDetailData> {



    public TXMyAccidentCarAdapter(Context context, int viewXml) {
        super(context, viewXml);
    }



    @ViewInject(R.id.accident_tv_type)
    private TextView _state;
    @ViewInject(R.id.accident_tv_title)
    private TextView _name;

    @ViewInject(R.id.accident_tv_info)
    private TextView _info;

    @ViewInject(R.id.accident_tv_price)
    private TextView _price;

    @ViewInject(R.id.accident_tv_time)
    private TextView _time;


    @ViewInject(R.id.accident_ib_del)
    private ImageButton mBtnDel;

    @ViewInject(R.id.accident_ib_sell)
    private ImageButton mBtnSell;

    @ViewInject(R.id.accident_iv_icon)
    private ImageView _imageIv;


    @Override
    protected void setItemData(View view, final MLAccidentDetailData d, final int position) {
        ViewUtils.inject(this, view);
        MLAccidentInfo data = d.info;
        if(data==null) return;
        String name = data.accidentName;
        if(!MLStrUtil.isEmpty(name)){
            name = name.replaceAll("\\s*", "");
        }
        _name.setText(name);

        String time = MLStringUtils.time_day(data.MCtime);
        _info.setText(data.city+"|"+data.mileage+"万公里 ");
        _time.setText(time);
        _price.setText(Html.fromHtml(String.format("<font color=\"#ff0000\" >%s%s</font><font color=\"#000000\">%s</font>", "¥ ", data.price, "  万")));


        if(MLStrUtil.compare(d.info.state,"正常")){
            _state.setVisibility(View.GONE);
        }else{
            _state.setVisibility(View.VISIBLE);
        }

        String id[]={"0"};
        if(!MLStrUtil.isEmpty(data.image)){
            id =  data.image.split(",");
        }

        String imgUrl = APIConstants.API_IMAGE+"?id="+id[0];


        //	BaseApplication.IMAGE_CACHE.get(imgUrl, _imageIv);

        _imageIv.setTag(imgUrl);
        if (!BaseApplication.IMAGE_CACHE.get(imgUrl, _imageIv)) {
            _imageIv.setImageResource(R.drawable.default_accidenta_header);
        }


        mBtnDel.setVisibility(View.VISIBLE);
        mBtnSell.setVisibility(View.VISIBLE);

        mBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TXEventModel model = new TXEventModel(MLConstants.EVENT_PARAM_ACCIDENT_DEL,d);
                EventBus.getDefault().post(model);
            }
        });

        mBtnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TXEventModel model = new TXEventModel(MLConstants.EVENT_PARAM_ACCIDENT_SELL,d);
                EventBus.getDefault().post(model);
            }
        });
    }
}