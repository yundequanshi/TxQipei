package com.txsh.shop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.model.TXShopAddressRes;
import com.txsh.services.MLShopServices;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.login.MLLoginCityPop;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLRegister;

import cn.ml.base.utils.IEvent;
import cn.ml.base.utils.MLStrUtil;

/**
 * 我的收获地址-修改
 * Created by Marcello on 2015/7/16.
 */
public class TXShopAddressUpdateAty extends BaseActivity {

    @ViewInject(R.id.et_name)
    private EditText mEtName;
    @ViewInject(R.id.et_phone)
    private EditText mEtPhone;
    @ViewInject(R.id.et_address)
    private EditText mEtAddress;
    @ViewInject(R.id.tv_area)
    private TextView mTvArea;

    private TXShopAddressRes.TXShopAddressData mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tx_address_update);
        ViewUtils.inject(this);

        mData = (TXShopAddressRes.TXShopAddressData) getIntentData();
        initView();
       // initData();
    }

    private void initView() {
        mEtName.setText(mData.name);
        mEtPhone.setText(mData.mobile);
        mEtAddress.setText(mData.address);
        mTvArea.setText(mData.location);
    }

    private void initData(){

    }

    @OnClick(R.id.top_btn_left)
    public void backOnClick(View view){
        finish();
    }

    @OnClick(R.id.address_rl_area)
    public void areaOnClick(View view){
        MLLoginCityPop menuWindow = new MLLoginCityPop(TXShopAddressUpdateAty.this,
                new IEvent<String>() {
                    @Override
                    public void onEvent(Object source, String eventArg) {
                        mTvArea.setText(eventArg);
                    }
                });
        menuWindow.showAtLocation(((ViewGroup) (findViewById(android.R.id.content))).getChildAt(0), Gravity.CENTER, 0, 0);
    }

    @OnClick(R.id.tv_default)
    public void defaultOnClick(View view){
        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("id", mData.id);
        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_SHOP_ADDRESS_SET, null, params, _handler
                ,HTTP_RESPONSE_SET , MLShopServices.getInstance());
        loadDataWithMessage(TXShopAddressUpdateAty.this, "正在设置，请稍等...", message2);
    }

    @OnClick(R.id.detail_tv_del)
    public void delOnClick(View view){

        AlertDialog builder = new AlertDialog.Builder(TXShopAddressUpdateAty.this,AlertDialog.THEME_HOLO_LIGHT).setTitle("提示")
                .setMessage("确定删除？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ZMRequestParams params = new ZMRequestParams();
                        params.addParameter("id", mData.id);
                        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_SHOP_ADDRESS_DEL, null, params, _handler
                                ,HTTP_RESPONSE_DEL , MLShopServices.getInstance());
                        loadDataWithMessage(TXShopAddressUpdateAty.this, "正在删除，请稍等...", message2);
                    }
                })
                .setNegativeButton("取消", null)
                .show();


    }





    @OnClick(R.id.btn_save)
    public void saveOnClick(View view){
        String name =mEtName.getText().toString();
        String phone =mEtPhone.getText().toString();
        String address =mEtAddress.getText().toString();
        String area = mTvArea.getText().toString();

        if(MLStrUtil.isEmpty(name)){
            showMessage("姓名不能为空!");
            return;
        }

        if(MLStrUtil.isEmpty(phone)){
            showMessage("电话不能为空!");
            return;
        }

        if(MLStrUtil.isEmpty(area)){
            showMessage("地区不能为空!");
            return;
        }

        if(MLStrUtil.isEmpty(address)){
            showMessage("详细地址不能为空!");
            return;
        }
        MLLogin mUser = BaseApplication.getInstance().get_user();
        ZMRequestParams params = new ZMRequestParams();

        params.addParameter("id", mData.id);
        params.addParameter("depotUser.id", mUser.Id);
        params.addParameter("name", name);
        params.addParameter("mobile", phone);
        params.addParameter("address", address);
        params.addParameter("location", area);
        params.addParameter("postalcode", "");


        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_SHOP_ADDRESS_UPDATE, null, params, _handler
                ,HTTP_RESPONSE_ADDRESS , MLShopServices.getInstance());
        loadDataWithMessage(TXShopAddressUpdateAty.this, "正在加载，请稍等...", message2);
    }



    private static final int HTTP_RESPONSE_ADDRESS = 2;
    private static final int HTTP_RESPONSE_DEL = 3;
    private static final int HTTP_RESPONSE_SET = 4;
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
                    MLRegister ret = (MLRegister) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){

                        showMessage("修改成功!");
                        setResult(1);
                        finish();
                    }else{
                        showMessage("修改失败!");
                    }
                    break;
                }

                case HTTP_RESPONSE_DEL:{
                    MLRegister ret = (MLRegister) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){

                        showMessage("删除成功!");
                        setResult(1);
                        finish();
                    }else{
                        showMessage("删除失败!");
                    }
                    break;
                }

                case HTTP_RESPONSE_SET:{
                    MLRegister ret = (MLRegister) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){

                        showMessage("设置成功!");
                        setResult(1);
                        finish();
                    }else{
                        showMessage("设置失败!");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
}
