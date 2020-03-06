package com.qipei.me;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.exception.ZMParserException;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLMessagePublishResponse;
import com.zuomei.model.MLMyInfoData;
import com.zuomei.model.MLMyInfoResponse;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLMyServices;
import com.zuomei.utils.MLToolUtil;
import com.zuomei.utils.ZMJsonParser;

import java.io.File;

import cn.ml.base.utils.IEvent;
import cn.ml.base.utils.MLStrUtil;
import cn.ml.base.utils.photo.MLPhotoUtil;
import cn.ml.base.widget.citypop.MLCityPop;
import cn.ml.base.widget.roundedimageview.RoundedImageView;

/**
 * Created by Marcello on 2015/6/11.
 */
public class MLMePersonAty extends BaseActivity {

    @ViewInject(R.id.me_iv_icon)
    private RoundedImageView mIcon;



    //姓名
    @ViewInject(R.id.my_tv_name)
    private TextView mTvName;

    //地区
    @ViewInject(R.id.my_tv_province)
    private TextView mTvProvince;

    //详细地址
    @ViewInject(R.id.my_tv_address)
    private TextView mTvAddress;

    //手机号
    @ViewInject(R.id.my_tv_phone)
    private TextView mTvPhone;

    //业务电话
    @ViewInject(R.id.my_tv_mobile)
    private TextView mTvMobile;

    public static MLMyInfoData _info;
    private 	MLLogin user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qp_me_person);
        ViewUtils.inject(this);

        user = 	BaseApplication.getInstance().get_user();
        initData();
    }


    //城市选择
    @OnClick(R.id.my_tv_province)
    public void cityOnClick(View view){
        MLCityPop menuWindow = new MLCityPop(MLMePersonAty.this, new IEvent<String>() {
            @Override
            public void onEvent(Object source, String eventArg) {
                mTvProvince.setText(eventArg);
            }
        });
        menuWindow.showAtLocation(((ViewGroup) (findViewById(android.R.id.content))).getChildAt(0), Gravity.CENTER, 0, 0);

    }


    //头像
    @OnClick(R.id.me_iv_icon)
    public void iconOnClick(View view){
        String s[] = {"相册", "拍照"};

        AlertDialog builder = new AlertDialog.Builder(MLMePersonAty.this,AlertDialog.THEME_HOLO_LIGHT)
                .setItems(s, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MLPhotoUtil.choose(MLMePersonAty.this, 0);
                    }
                })
                .create();
        builder.show();
    }


    //提交
    @OnClick(R.id.accident_btn_next)
    public void submitOnClick(View view){
        String name =mTvName.getText().toString();
        String phone =mTvPhone.getText().toString();
        String provice = mTvProvince.getText().toString();

        if(MLStrUtil.isEmpty(name)){
            showMessage("姓名不能为空!");
            return;
        }
        if(MLStrUtil.isEmpty(phone)){
            showMessage("电话不能为空!");
            return;
        }
        if(MLStrUtil.isEmpty(provice)){
            showMessage("地区不能为空!");
            return;
        }
        updateRealName(name);



    }


    private void initView() {
        //头像
        if(_info.userPhoto<=0){
            mIcon.setImageResource(R.drawable.default_my_info_head);
        }else{
            String iconUrl = APIConstants.API_IMAGE+"?id="+_info.userPhoto;
            mIcon.setTag(iconUrl);
            if (!BaseApplication.IMAGE_CACHE.get(iconUrl, mIcon)) {
                mIcon.setImageDrawable(null);
            }
        }
        mTvName.setText(_info.realName);
  //      _niceTv.setText(_info.depotName);
        mTvPhone.setText(_info.userPhone);
        mTvMobile.setText(_info.userPhone2);
        mTvProvince.setText(_info.location);
        mTvAddress.setText(_info.address);
      //  _alipay.setText(_info.alipay);
    }


    @Override

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(MLPhotoUtil.IsCancel()&&requestCode!=100){
            showMessage("操作已取消!");
            return;
        }
        Bitmap b = MLPhotoUtil.gePhotoBitmap();
        if (requestCode == 100 && data != null) {
//相册选择返回
            MLPhotoUtil.photoZoom(data.getData());
        } else if (requestCode == 101) {
//拍照返回 调用裁剪
            MLPhotoUtil.photoZoom(null);
        }
        else if(requestCode == 102 && resultCode != 0) {
//裁剪返回
            mIcon.setImageBitmap(MLPhotoUtil.gePhotoBitmap());
        }
    }


    //上传头像
    private void uploadImage() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("file", new File(MLPhotoUtil.getPhotoPath()));

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                APIConstants.API_IMAGE_UPLOAD,
                params,
                new RequestCallBack<String>() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            MLMessagePublishResponse ret = ZMJsonParser.fromJsonString(MLMessagePublishResponse.class, responseInfo.result);
                            if(ret.state.equalsIgnoreCase("1")){
                                setHead(ret.datas);
                            }else{
                                showMessage("图片上传失败!");
                            }
                        } catch (ZMParserException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        showMessage("图片上传失败!");
                    }
                });
    }


    //上传头像
    private void setHead(String id) {
        MLLogin user =BaseApplication.getInstance().get_user();
        ZMRequestParams params = new ZMRequestParams();
        if(!MLToolUtil.isNull(id)){
            params.addParameter(MLConstants.PARAM_MY_ICONID,id);
        }
        if(user.isDepot){
            params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,user.Id);
        }else{
            params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,user.Id);
        }
        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.MY_INFO_HEAD, null, params, _handler,HTTP_RESPONSE_HEAD , MLMyServices.getInstance());
        loadDataWithMessage(MLMePersonAty.this, null, message2);
    }

    private void initData() {
        ZMRequestParams catalogParam = new ZMRequestParams();
        if(user.isDepot){
            catalogParam.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,user.Id);
        }else{
            catalogParam.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,user.Id);
        }
        ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(ZMHttpType.RequestType.MY_INFO_D, null, catalogParam, _handler, HTTP_RESPONSE_INFO,  MLMyServices.getInstance());
        loadDataWithMessage( null, message1);

    }


    /**
     * 修改姓名
     */
    protected void updateRealName(String name) {
        MLLogin user = BaseApplication.getInstance().get_user();
        ZMRequestParams params = new ZMRequestParams();
        if(user.isDepot){
            params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,user.Id);
        }else{
            params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,user.Id);
        }
        params.addParameter(MLConstants.PARAM_MY_REALNAME,name);
        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.MY_INFO_REALNAME, null, params, _handler,HTTP_RESPONSE_REALNAME, MLMyServices.getInstance());
        loadDataWithMessage("正在提交，请稍等...", message2);
    }

    /**
     * 修改详细地址
     */
    protected void updateAddress() {

        MLLogin user = BaseApplication.getInstance().get_user();
        ZMRequestParams params = new ZMRequestParams();
        if(user.isDepot){
            params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,user.Id);
        }else{
            params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,user.Id);
        }
        params.addParameter(MLConstants.PARAM_REGISTER_LOCATION,mTvProvince.getText().toString());
        params.addParameter(MLConstants.PARAM_REGISTER_ADDRESSS,mTvAddress.getText().toString());
        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.MY_INFO_ADDRESS, null, params, _handler,HTTP_RESPONSE_ADDRESS, MLMyServices.getInstance());
        loadDataWithMessage(null, message2);
    }


    /**
     * 修改详电话
     */

    protected void updatePhone() {



        MLLogin user = BaseApplication.getInstance().get_user();
        ZMRequestParams params = new ZMRequestParams();
        if(user.isDepot){
            params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,user.Id);
        }else{
            params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,user.Id);
        }

        params.addParameter(MLConstants.PARAM_MY_PHONE,mTvPhone.getText().toString());
        params.addParameter("userPhone2",mTvMobile.getText().toString());

        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.MY_INFO_PHONE, null, params, _handler,HTTP_RESPONSE_PHONE, MLMyServices.getInstance());
        loadDataWithMessage(null, message2);
    }

    private static final int HTTP_RESPONSE_HEAD = 0;
    private static final int HTTP_RESPONSE_DEPOTNAME = 1;
    private static final int HTTP_RESPONSE_REALNAME = 2;
    private static final int HTTP_RESPONSE_PHONE = 3;
    private static final int HTTP_RESPONSE_ALIPAY = 4;
    private static final int HTTP_RESPONSE_LOCATION = 5;
    private static final int HTTP_RESPONSE_ADDRESS = 6;
    private static final int HTTP_RESPONSE_INFO = 7;

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
                //保存头像
                case  HTTP_RESPONSE_HEAD:{
                    MLRegister ret = (MLRegister) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){
                        showMessage("上传头像成功!");
                    }else{
                        showMessage("上传头像失败!");
                    }
                    break;
                }
                //修改名称
                case HTTP_RESPONSE_DEPOTNAME:{
                    MLRegister ret = (MLRegister) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){
                    }else{
                        showMessage("修改名称失败!");
                    }
                    break;
                }
                //修改真实姓名
                case HTTP_RESPONSE_REALNAME:{
                    MLRegister ret = (MLRegister) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){
                        //_nameTv.setText(_newStr);
                        updatePhone();
                    }else{
                        showMessage("修改真实姓名失败!");
                    }
                    break;
                }
                //修改手机号
                case HTTP_RESPONSE_PHONE:{
                    MLRegister ret = (MLRegister) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){
                     //   _phoneTv.setText(phone);
                        updateAddress();
                    }else{
                        showMessage("修改手机号失败!");
                    }
                    break;
                }
                //修改支付宝
                case HTTP_RESPONSE_ALIPAY:{
                    MLRegister ret = (MLRegister) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){
                      //  _alipay.setText(_newStr);
                    }else{
                        showMessage("修改支付宝失败!");
                    }
                    break;
                }

                //修改地区
                case HTTP_RESPONSE_LOCATION:{
                    MLRegister ret = (MLRegister) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){
                      //  _proviceTv.setText(_newStr);
                    }else{
                        showMessage("修改地区失败!");
                    }
                    break;
                }
                //修改详细地址
                case HTTP_RESPONSE_ADDRESS:{
                    MLRegister ret = (MLRegister) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){
                     //   _addressTv.setText(_newStr);
                    }else{
                        //showMessage("修改详细地址失败!");
                        showMessageError(ret.message);
                    }
                    break;
                }

                case HTTP_RESPONSE_INFO:{
                    MLMyInfoResponse ret = (MLMyInfoResponse) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){
                        _info =  ret.datas;
                        initView();
                    }else{
                        showMessage("获取基本信息失败!");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

}
