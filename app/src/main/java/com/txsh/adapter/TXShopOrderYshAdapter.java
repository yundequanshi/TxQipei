package com.txsh.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.txsh.model.TXEventModel;
import com.txsh.model.TXShopOrderRes;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;

import cn.ml.base.MLAdapterBase;
import cn.ml.base.utils.MLStrUtil;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by Marcello on 2015/6/3.
 */
public class TXShopOrderYshAdapter extends MLAdapterBase<TXShopOrderRes.TXShopOrderData> {



    public TXShopOrderYshAdapter(Context context, int viewXml) {
        super(context, viewXml);
    }


    @ViewInject(R.id.order_iv)
    private ImageView mTvIcon;

    @ViewInject(R.id.order_tv_name)
    private TextView mTvName;
    @ViewInject(R.id.order_tv_price)
    private TextView mTvPrice;
    @ViewInject(R.id.tv_orderid)
    private TextView mTvOrderId;
    @ViewInject(R.id.tv_ordertime)
    private TextView mTvOrderTime;

    @ViewInject(R.id.shop_tv_count)
    private TextView mTvOrderCount;
    @ViewInject(R.id.shop_tv_fright)
    private TextView mTvOrderFright;
    @ViewInject(R.id.shop_tv_priceall)
    private TextView mTvOrderPrice;
    //评价
    @ViewInject(R.id.shop_btn_1)
    private Button mBtnEvaluate;

    //退货
    @ViewInject(R.id.shop_btn_2)
    private Button mBtnQuit;

    @ViewInject(R.id.order_tv_state)
    private TextView mTvState;
    @ViewInject(R.id.rl_dfk)
    private RelativeLayout mRl;


    @Override
    protected void setItemData(View view, final TXShopOrderRes.TXShopOrderData data, final int position) {
        ViewUtils.inject(this, view);

        String imgUrl = APIConstants.API_IMAGE_SHOW +data.productPicture;
        mTvIcon.setTag(imgUrl);
        if (!BaseApplication.IMAGE_CACHE.get(imgUrl, mTvIcon)) {
            mTvIcon.setImageResource(R.drawable.shop_default);
        }
        mTvName.setText(data.productName);
        mTvPrice.setText("¥"+data.productPrice);
        mTvOrderId.setText("订单号："+data.orderId);
        mTvOrderTime.setText("时间：" + data.orderTime);


        double p = Double.parseDouble(data.productPrice);
        double fright = Double.parseDouble(data.productFreight);

        //数量
        SpannableString msp = new SpannableString("合计：¥"+ data.sumPrice);
        msp.setSpan(new AbsoluteSizeSpan(14,true), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置字体前景色
        msp.setSpan(new ForegroundColorSpan(Color.parseColor("#3c3c3c")), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvOrderPrice.setText(msp);


        mTvOrderCount.setText(Html.fromHtml(String.format("<font color=\"#979797\">数量：</font>%s", data.productNumber + "")));
        mTvOrderFright.setText(Html.fromHtml(String.format("<font color=\"#979797\">运费：</font>%s 元", data.productFreight + "")));

        mBtnEvaluate.setText("评价");
        mBtnQuit.setText("退货");


        mBtnEvaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TXEventModel model = new TXEventModel(MLConstants.EVENT_PARAM_ORDER_EVALUATE,data);
                EventBus.getDefault().post(model);
            }
        });

        mBtnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TXEventModel model = new TXEventModel(MLConstants.EVENT_PARAM_ORDER_QUIT,data);
                EventBus.getDefault().post(model);
            }
        });

        if(!MLStrUtil.compare(data.orderFormState,"待评论")){
            mTvState.setText("["+data.orderFormState+"]");
        }else{
            mTvState.setText("");
        }

        if(MLStrUtil.compare(data.orderFormState,"待退货")||MLStrUtil.compare(data.orderFormState,"同意退货")||MLStrUtil.compare(data.orderFormState,"拒绝退货")){
            mBtnQuit.setVisibility(View.GONE);
        }else{
            mBtnQuit.setVisibility(View.VISIBLE);
        }

        if(MLStrUtil.compare(data.orderFormState,"已评论")||MLStrUtil.compare(data.orderFormState,"待退货")||MLStrUtil.compare(data.orderFormState,"同意退货")||MLStrUtil.compare(data.orderFormState,"拒绝退货")){
            mRl.setVisibility(View.GONE);
        }else{
            mRl.setVisibility(View.VISIBLE);
        }
    }
}