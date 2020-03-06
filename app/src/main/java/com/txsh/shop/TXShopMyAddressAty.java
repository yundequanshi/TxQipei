package com.txsh.shop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.adapter.TXMyAddressAdapter;
import com.txsh.model.TXShopAddressRes;
import com.txsh.services.MLShopServices;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;

/**
 * 我的收获地址
 * Created by Marcello on 2015/7/16.
 */
public class TXShopMyAddressAty extends BaseActivity {

    @ViewInject(R.id.address_lv)
    private ListView mListView;

    private TXMyAddressAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tx_address_my);
        ViewUtils.inject(this);

        initView();
        initData();
    }

    private void initView() {
        mAdapter = new TXMyAddressAdapter(TXShopMyAddressAty.this,R.layout.tx_item_address_my);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TXShopAddressRes.TXShopAddressData data = (TXShopAddressRes.TXShopAddressData) mAdapter.getItem(position);
                startAct(TXShopMyAddressAty.this,TXShopAddressUpdateAty.class,data,1);
            }
        });
    }

    private void initData(){
        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("depotId", BaseApplication.getInstance().get_user().Id);
        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_SHOP_ADDRESS_MY, null, params, _handler
                ,HTTP_RESPONSE_ADDRESS , MLShopServices.getInstance());
        loadDataWithMessage(TXShopMyAddressAty.this, "正在加载，请稍等...", message2);
    }

    @OnClick(R.id.top_btn_left)
    public void backOnClick(View view){
        finish();
    }


    @OnClick(R.id.btn_add)
    public void addOnClick(View view){
        startAct(TXShopMyAddressAty.this,TXShopAddressAddAty.class,"",1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            initData();
        }
    }

    private static final int HTTP_RESPONSE_ADDRESS = 2;
    private Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // dismissProgressDialog();
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
                case HTTP_RESPONSE_ADDRESS:{
                    TXShopAddressRes ret = (TXShopAddressRes) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){

                        mAdapter.setData(ret.datas);
                    }else{
                        showMessage("获取商家失败!");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
}
