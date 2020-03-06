package com.txsh.shop;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.model.TXShopLogisticalRes;
import com.txsh.model.TXShopOrderRes;
import com.txsh.services.MLShopServices;
import com.zuomei.base.BaseActivity;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;

import cn.ml.base.utils.MLToolUtil;

/**
 * Created by Marcello on 2015/7/24.
 */
public class TXShopLogisticalAty extends BaseActivity {

    @ViewInject(R.id.tv_name)
    private TextView mTvName;
    @ViewInject(R.id.tv_order)
    private TextView mTvOrder;
    @ViewInject(R.id.tv_phone)
    private TextView mTvPhone;

    @ViewInject(R.id.tv_phone_end)
    private TextView mTvPhoneEnd;


    private TXShopOrderRes.TXShopOrderData mDataInfo;
    private TXShopLogisticalRes.TXShopLogisticalData mDataLogistical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tx_shop_logistical);
        ViewUtils.inject(this);
        mDataInfo = (TXShopOrderRes.TXShopOrderData) getIntentData();
        initData();
        mTvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDataLogistical != null) {
                    MLToolUtil.call(TXShopLogisticalAty.this, mDataLogistical.mobile);
                }

            }
        });

        mTvPhoneEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDataLogistical != null) {
                    MLToolUtil.call(TXShopLogisticalAty.this, mDataLogistical.endMobile);
                }
            }
        });
    }

    private void initData() {
        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("orderId", mDataInfo.orderId);

        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_SHOP_ORDER_LOGISTICAL, null, params, _handler,
                HTTP_RESPONSE , MLShopServices.getInstance());
        loadDataWithMessage(TXShopLogisticalAty.this, "正在加载，请稍等...", message2);
    }


    @OnClick(R.id.top_btn_left)
    public void backOnClick(View view){
        finish();
    }


    private static final int HTTP_RESPONSE= 4;

    private Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismissProgressDialog();
            if (msg == null || msg.obj == null) {
                showMessage(R.string.loading_data_failed);
                return;
            }
            if (msg.obj instanceof ZMHttpError) {
                ZMHttpError error = (ZMHttpError) msg.obj;
                showMessage(error.errorMessage);
                return;
            }
            switch (msg.what) {
                case HTTP_RESPONSE:{
                    TXShopLogisticalRes ret = (TXShopLogisticalRes) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){
                        mDataLogistical = ret.datas;
                        mTvName.setText(ret.datas.logisName);
                        mTvOrder.setText(ret.datas.logisOrderId);
                     //   mTvPhone.setText(ret.datas.mobile);
                        mTvPhoneEnd.setText(ret.datas.endMobile);
                    }else{
                        showMessageError("初始化失败!");
                    }

                    break;
                }

                default:
                    break;
            }

        }
    };

}
