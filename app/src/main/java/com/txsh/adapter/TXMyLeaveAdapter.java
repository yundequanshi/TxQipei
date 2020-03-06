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
import com.zuomei.model.MLLeaveData;

import cn.ml.base.MLAdapterBase;
import cn.ml.base.utils.MLDateUtil;
import cn.ml.base.utils.MLStrUtil;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by Marcello on 2015/6/3.
 */
public class TXMyLeaveAdapter extends MLAdapterBase<MLLeaveData> {



    public TXMyLeaveAdapter(Context context, int viewXml) {
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


    @ViewInject(R.id.accident_iv_icon)
    private ImageView _imageIv;

    @ViewInject(R.id.accident_ib_del)
    private ImageButton mBtnDel;
    @ViewInject(R.id.accident_ib_sell)
    private ImageButton mBtnSell;

    @Override
    protected void setItemData(View view, final MLLeaveData data, final int position) {
        ViewUtils.inject(this, view);

        if(data.info==null){
            return;
        }


        String id[]={"0"};
        if(!MLStrUtil.isEmpty(data.info.images)){
            id =  data.info.images.split(",");
        }
       /* if(data.companyLogo.equalsIgnoreCase("0")){
            id = data.depotLogo;
        }else{
            id = data.companyLogo;
        }*/

       /* if(data.state.equalsIgnoreCase("已售")){
            _state.setText("已售");
        }else{
            _state.setText("");
        }*/

        String imgUrl = APIConstants.API_IMAGE+"?id="+id[0];

        _imageIv.setTag(imgUrl);
        if (!BaseApplication.IMAGE_CACHE.get(imgUrl, _imageIv)) {
            _imageIv.setImageResource(R.drawable.default_accidenta_header);
        }

        String name = data.info.name;
        if(!MLStrUtil.isEmpty(name)){
            name = name.replaceAll("\\s*", "");
        }
        _name.setText(name);
        _info.setText(data.info.cityName+"|"+data.info.quality);
        _price.setText(Html.fromHtml(String.format("<font color=\"#ff0000\" >%s%s</font><font color=\"#000000\">%s</font>", "¥ ", data.info.currentCost, " 元")));
//			_price.setText("￥"+data.info.currentCost+"元");
        String time = MLDateUtil.getStringByFormat(data.info.createTime, "yyyy-MM-dd HH:mm");
        _time.setText(time);

        if(MLStrUtil.compare(data.info.state, "正常")){
            _state.setVisibility(View.GONE);
        }else{
            _state.setVisibility(View.VISIBLE);
        }

        mBtnDel.setVisibility(View.VISIBLE);
        mBtnSell.setVisibility(View.VISIBLE);

        mBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TXEventModel model = new TXEventModel(MLConstants.EVENT_PARAM_LEAVE_DEL,data);
                EventBus.getDefault().post(model);
            }
        });

        mBtnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TXEventModel model = new TXEventModel(MLConstants.EVENT_PARAM_LEAVE_SELL,data);
                EventBus.getDefault().post(model);
            }
        });

    }
}