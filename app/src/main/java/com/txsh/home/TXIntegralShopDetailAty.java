package com.txsh.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.adapter.TxIntegralShopAdapter;
import com.txsh.model.TXIntegralShopDetailRes;
import com.txsh.model.TXIntergalProduct;
import com.txsh.services.MLShopServices;
import com.txsh.utils.MLScrollWebView;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLRegister;

import cn.ml.base.utils.MLDialogUtils;


/**
 * *积分商城详情
 * Created by Marcello on 2015/6/25.
 */
public class TXIntegralShopDetailAty extends BaseActivity {

    private TxIntegralShopAdapter mAdapter;

    @ViewInject(R.id.shop_tv_name)
    private TextView mTvName;
    @ViewInject(R.id.shop_tv_jf)
    private TextView mTvJf;
    @ViewInject(R.id.shop_tv_phone)
    private TextView mTvPhone;
/*    @ViewInject(R.id.shop_tv_title)
    private TextView mTvTitle;
    @ViewInject(R.id.shop_tv_content)
    private TextView mTvContent;*/

    @ViewInject(R.id.shop_web)
    private MLScrollWebView mWeb;


    @ViewInject(R.id.shop_iv)
    private ImageView mIv;



    private TXIntergalProduct data;
    private TXIntegralShopDetailRes ret;
    private MLLogin mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tx_integral_shopdetail);
        ViewUtils.inject(this);
        data = (TXIntergalProduct) getIntentData();
        mUser = BaseApplication.getInstance().get_user();
        initWeb();
        initData();

    }

    private void initWeb() {

        String url = APIConstants.API_DEFAULT_HOST+"/mobile3/product/showContent?id="+data.id;
        mWeb.loadUrl(url);

        try{
            mWeb.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }catch (Exception e){

        }


    }

    @OnClick(R.id.detail_btn_buy)
    public void buyOnClick(View view){

        if( ret.datas.price>data.money){
                showMessage("积分不足");
            return;
        }

        AlertDialog builder =MLDialogUtils.getAlertDialog(TXIntegralShopDetailAty.this)
                .setTitle("提示")
                .setMessage("确定兑换此商品？")
                .setNegativeButton("取消",null)
                .setPositiveButton("确定",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buy();
                    }
                })
                .show();


    }

    private void initData() {
        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("id",data.id);

        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_INTERGRAL_SHOP_DETAIL, null, params, _handler,HTTP_RESPONSE , MLShopServices.getInstance());
        loadDataWithMessage(TXIntegralShopDetailAty.this, "正在加载，请稍等...", message2);
    }

    private void buy() {
        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("id",data.id);
        params.addParameter("depotId",mUser.Id);

        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_INTERGRAL_SHOP_BUY, null, params, _handler,HTTP_RESPONSE_BUY , MLShopServices.getInstance());
        loadDataWithMessage(TXIntegralShopDetailAty.this, "正在加载，请稍等...", message2);
    }

    private void review(){
        TXIntegralShopDetailRes.TXIntegralShopDataRes data = ret.datas;
        String imgUrl = APIConstants.API_IMAGE_SHOW+data.images;

        mIv.setTag(imgUrl);
        if (!BaseApplication.IMAGE_CACHE.get(imgUrl, mIv)) {
            mIv.setImageResource(R.drawable.jishenbanner);
        }
        mTvJf.setText(Html.fromHtml(String.format("兑换需<font color=\"#ff0000\"> %s </font> 积分", data.price + "")));
        mTvName.setText(data.title);
        mTvPhone.setText(data.phone);

       /* try {
            JSONObject jb = new JSONObject(data.content);


            mTvTitle.setText(jb.getString("title"));
            mTvContent.setText(jb.getString("content"));
        }catch (Exception e){

            mTvTitle.setText("");
            mTvContent.setText("");
        }*/

    }

    @OnClick(R.id.top_btn_left)
    public void backOnClick(View view){
        finish();
    }

    @OnClick(R.id.detail_iv_phone)
    public void call(View view){
        if(ret==null||ret.datas==null){
            return;
        }
        cn.ml.base.utils.MLToolUtil.call(TXIntegralShopDetailAty.this,ret.datas.phone);
    }

    private static final int HTTP_RESPONSE= 1;
    private static final int HTTP_RESPONSE_BUY= 2;
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

                case HTTP_RESPONSE:{
                    ret = (TXIntegralShopDetailRes) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){
                        review();
                    }else{
                        showMessageError("初始化失败!");
                    }
                    break;
                }

                case HTTP_RESPONSE_BUY:{
                    MLRegister ret = (MLRegister) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")&&ret.datas){
                        showMessage("兑换中，请联系客服！");
                        setResult(1);
                        finish();
                    }else{
                        showMessage("兑换失败");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };



}
