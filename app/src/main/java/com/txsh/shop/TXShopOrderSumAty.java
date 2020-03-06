package com.txsh.shop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.txsh.R;
import com.txsh.adapter.TXShopOrderSumAdapter;
import com.txsh.model.TXShopAddressRes;
import com.txsh.model.TXShopBuyRes;
import com.txsh.model.TXShopListRes;
import com.txsh.services.MLShopServices;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLoginFail;
import com.zuomei.model.MLPayAlipayData;
import com.zuomei.model.MLRegister;
import com.zuomei.model.TXShopSubmitCar;
import com.zuomei.utils.MLPayUtils;
import com.zuomei.utils.MathUtils;
import com.zuomei.utils.alipay.PayResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.ml.base.MLBaseConstants;
import cn.ml.base.utils.MLDialogUtils;
import cn.ml.base.utils.MLStrUtil;
import cn.ml.base.utils.MLToolUtil;
import cn.ml.base.widget.sample.MLNoScrollListView;

/**
 * Created by Marcello on 2015/7/16.
 */
public class TXShopOrderSumAty extends BaseActivity {

    @ViewInject(R.id.order_lv)
    private MLNoScrollListView mListView;

    private TXShopOrderSumAdapter mAdapter;
    //收货地址
    private TXShopAddressRes.TXShopAddressData mDataAdress;
    @ViewInject(R.id.order_tv_name)
    private TextView mTvName;
    @ViewInject(R.id.order_tv_address)
    private TextView mTvAddress;
    @ViewInject(R.id.sum_tv_count)
    private TextView mTvOrderCount;
    @ViewInject(R.id.sum_tv_orderfreight)
    private TextView mTvOrderFreight;
    @ViewInject(R.id.sum_tv_orderall)
    private TextView mTvOrderAll;
    @ViewInject(R.id.order_tv_content)
    private EditText mEtContent;

    //页面数据
    private TXShopBuyRes.TXShopBuyData mDataOrder;

    // 选择的支付方式
    private int mPayType = R.id.detail_tv_wx;
    private int mDataPayType;
    private List<Integer> mRlPays;
    // ************微信支付****************************************************
    private IWXAPI api;
    private String mDataTradeNos;

    // 支付宝
    MLPayAlipayData mlPayAlipayData;

    private String mCarId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tx_order_sum);
        ViewUtils.inject(this);
        mDataOrder = (TXShopBuyRes.TXShopBuyData) getIntentData();

        //购物车id
        mCarId = getIntent().getStringExtra("CARID");
        initView();
        initWeixin();
    }

    private void initView() {
        mAdapter = new TXShopOrderSumAdapter(TXShopOrderSumAty.this,R.layout.tx_item_shop_sum);
        mListView.setAdapter(mAdapter);
        //收货地址

        if(mDataOrder.address==null){
            mTvName.setText("收货人 :");
            mTvAddress.setText("收货地址 : ");
        }else{
            mTvName.setText(Html.fromHtml(String.format("收货人 : <font color=\"#3c3c3c\">%s   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;     %s</font>", mDataOrder.address.name, mDataOrder.address.mobile)));
            mTvAddress.setText(Html.fromHtml(String.format("收货地址 : <font color=\"#3c3c3c\">%s</font>", mDataOrder.address.address)));
        }


        mAdapter.setData(mDataOrder.products);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TXShopListRes.TXShopListData data = new TXShopListRes().new TXShopListData();
                data.id = mDataOrder.products.get(position).productId;
                Intent intent = new Intent();
                intent.setClass(TXShopOrderSumAty.this, TXShopDetailAty.class);
                intent.putExtra(MLBaseConstants.TAG_INTENT_DATA, data);
                startActivity(intent);
            }
        });
        getOrderInfo();
    }

    private void initWeixin() {
        api = WXAPIFactory.createWXAPI(TXShopOrderSumAty.this, APIConstants.APP_ID);
        mRlPays = new ArrayList();
        Collections.addAll(mRlPays, R.id.detail_tv_wx, R.id.detail_tv_alipay,
                R.id.detail_tv_yue);
    }



    @OnClick(R.id.top_btn_left)
    private void backOnClick(View view){
        finish();
    }

    /**
     *  支付方式
     * @param view
     */
    @OnClick(R.id.business_btn_pay)
    public void pay(View view){

        //判断支付方式
        if(mPayType==R.id.detail_tv_wx){
            mDataPayType = 2;
        }else if(mPayType==R.id.detail_tv_alipay){
            mDataPayType = 1;
        }else{
            mDataPayType = 0;
        }
        //showMessage(payType+"");

        if(MLStrUtil.isEmpty(mCarId)){
        //商品支付
            submitProduct();
        }else{
        //购物车
            submitProductCar();
        }


    }

    /**
     * 支付---商品
     */
    private void submitProduct() {
        String content = mEtContent.getText().toString();

        if(mDataOrder.address==null){
            showMessage("请填写收货地址");
            return;
        }

        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("depotId", BaseApplication.getInstance().get_user().Id);
        params.addParameter("productId",mDataOrder.products.get(0).productId);
        params.addParameter("productNumber",mDataOrder.products.get(0).productNumber);
        params.addParameter("payType",mDataPayType+"");
        if(!MLStrUtil.isEmpty(content)){
            params.addParameter("detail", content);
        }
        params.addParameter("addressId",mDataOrder.address.addressId);

        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_SHOP_PRODUCT_SUBMIT, null, params, _handler
                ,HTTP_RESPONSE_PAY_PRODUCT , MLShopServices.getInstance());
        loadDataWithMessage(TXShopOrderSumAty.this, "正在提交，请稍等...", message2);
    }
    private void submitProductCar() {
        String content = mEtContent.getText().toString();

        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("depotId", BaseApplication.getInstance().get_user().Id);
        params.addParameter("carIds",mCarId);
        params.addParameter("payType",mDataPayType+"");
        if(!MLStrUtil.isEmpty(content)){
            params.addParameter("detail", content);
        }
        params.addParameter("addressId",mDataOrder.address.addressId);

        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_SHOP_PRODUCT_SUBMIT_CAR, null, params, _handler
                ,HTTP_RESPONSE_PAY_SUBMITCAR , MLShopServices.getInstance());
        loadDataWithMessage(TXShopOrderSumAty.this, "正在提交，请稍等...", message2);
    }

    /**
     * 收货地址
     * @param view
     */
    @OnClick(R.id.order_rl_address)
    public void address(View view){
        startAct(TXShopOrderSumAty.this, TXShopAddressAty.class, "", 1);
    }

    /**
     * 支付方式选择
     *
     * @param view
     */
    @OnClick({ R.id.detail_tv_wx, R.id.detail_tv_alipay, R.id.detail_tv_yue })
    public void payChooseOnClick(View view) {
        for (int id : mRlPays) {
            if (id == view.getId()) {
                mPayType = id;

                ((CheckBox) ((LinearLayout) findViewById(id))
                        .getChildAt(0)).setChecked(true);
            } else {
                ((CheckBox) ((LinearLayout) findViewById(id))
                        .getChildAt(0)).setChecked(false);
            }
        }
    }



    private void pay() {
        if(MLStrUtil.isEmpty(mDataOrder.allPrice)||MLStrUtil.compare(mDataOrder.allPrice,"0")){
            showMessage("订单已创建，请联系商家改价");
            return;
        }


        if(mPayType==R.id.detail_tv_wx){
            //微信支付
            payWx(0);
        }else if(mPayType==R.id.detail_tv_alipay){
            //支付宝
            payAlipay(0);
        }else{
            //余额支付
            payYue();
        }
    }

    /**
     * 微信支付
     */
    private void payWx(int type){
        //0：普通微信支付      1：余额+微信

        //商品总价
        double price = Double.parseDouble(mDataOrder.allPrice)*100;
        //用户余额
        double balance = Double.parseDouble(mDataOrder.money)*100;
        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("tradeNos", mDataTradeNos);
        params.addParameter("ip", MLToolUtil.getLocalIpAddress());
        //余额不足时

        if(type==0){
            params.addParameter("money", (int)price+"");
        }else{
            params.addParameter("balance",mDataOrder.money);
            params.addParameter("money", (int)(MathUtils.sub(price,balance))+"");
        }
        params.addParameter("allMoney",mDataOrder.allPrice);
        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_SHOP_PRODUCT_PAY_WX, null, params, _handler
                ,HTTP_RESPONSE_PAY_WX , MLShopServices.getInstance());
        loadDataWithMessage(TXShopOrderSumAty.this, "正在支付，请稍等...", message2);
    }

    /**
     * 支付宝支付
     */
    private void payAlipay(int type){
        //0：普通支付宝支付      1：余额+支付宝

        //商品总价
        double price = Double.parseDouble(mDataOrder.allPrice);
        //用户余额
        double balance = Double.parseDouble(mDataOrder.money);

        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("tradeNos", mDataTradeNos);
        if(type==0){
            params.addParameter("money", price+"");
        }else{

            params.addParameter("balance",mDataOrder.money);
            params.addParameter("money", (MathUtils.sub(price,balance))+"");
        }
        params.addParameter("allMoney",mDataOrder.allPrice);

        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_SHOP_PRODUCT_PAY_ALIPAY, null, params, _handler
                ,HTTP_RESPONSE_PAY_ALIPAY , MLShopServices.getInstance());
        loadDataWithMessage(TXShopOrderSumAty.this, "正在支付，请稍等...", message2);
    }

    /**
     * 支付宝支付确认
     */
    public void alipayAffirm(){
        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("out_trade_no", mDataTradeNos);
        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_PAY_ALIPAY_AFFIRM, null, params, _handler
                ,HTTP_RESPONSE_PAY_ALIPAY_AFFIRM , MLShopServices.getInstance());
        loadDataWithMessage(TXShopOrderSumAty.this, "正在确认，请稍等...", message2);
    }

    /**
     * 余额支付
     */
    public void payYue(){
        //商品总价
        double price = Double.parseDouble(mDataOrder.allPrice);
        //用户余额
        double balance = Double.parseDouble(mDataOrder.money);

        //余额不足
        if(price>balance){
            //String content = String.format("余额不足，剩余%s元请选择其他支付方式",String.valueOf((price-balance)));

            AlertDialog builder =   MLDialogUtils.getAlertDialog(TXShopOrderSumAty.this).setTitle("提示")
                    .setMessage(Html.fromHtml(String.format("余额不足，剩余<font color=\"#E90C0C\">%s元</font>请选择其他支付方式", String.valueOf(MathUtils.sub(price,balance)))))
                            .setPositiveButton("支付宝", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    payAlipay(1);
                                }
                            })
                            .setNegativeButton("微信", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    payWx(1);
                                }
                            })
                            .show();


            return;
        }

        AlertDialog builder =   MLDialogUtils.getAlertDialog(TXShopOrderSumAty.this).setTitle("提示")
                .setMessage("确认使用余额进行支付？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ZMRequestParams params = new ZMRequestParams();
                        params.addParameter("tradeNos", mDataTradeNos);
                        params.addParameter("balance", mDataOrder.allPrice);
                        params.addParameter("allMoney", mDataOrder.allPrice);
                        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_SHOP_PRODUCT_PAY_YUE, null, params, _handler
                                , HTTP_RESPONSE_PAY_YUE, MLShopServices.getInstance());
                        loadDataWithMessage(TXShopOrderSumAty.this, "正在支付，请稍等...", message2);
                    }
                })
                .setNegativeButton("取消", null)
                .show();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            mDataAdress = (TXShopAddressRes.TXShopAddressData) data.getSerializableExtra("data");
            if(mDataOrder.address==null){
                mDataOrder.address = new TXShopBuyRes().new TXShopAddressData();
            }

            if(mDataAdress!=null){
                mDataOrder.address.addressId = mDataAdress.id;
                reviewAddress();
            }

        }
    }



    private static final int HTTP_RESPONSE_PAY_PRODUCT = 1;
    private static final int HTTP_RESPONSE_PAY_WX = 2;
    private static final int HTTP_RESPONSE_PAY_ALIPAY = 3;
    private static final int HTTP_RESPONSE_PAY_ALIPAY_AFFIRM = 4;
    private static final int HTTP_RESPONSE_PAY_YUE = 5;
    private static final int HTTP_RESPONSE_PAY_SUBMITCAR = 6;
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
                case HTTP_RESPONSE_PAY_PRODUCT:{
                    MLLoginFail ret = (MLLoginFail) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){
                        mDataTradeNos = ret.datas;
                        pay();
                    }else{
                        showMessage("获取订单失败!");
                    }
                    break;
                }

                case HTTP_RESPONSE_PAY_WX:{

                    //本地记录下，此次支付是微信支付
                    BaseApplication.aCache.put(MLConstants.ACACHE_PARAM_WX_SHOP,MLConstants.ACACHE_PARAM_WX_SHOP);
                    PayReq ret = (PayReq) msg.obj;
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    // boolean b = api.registerApp("com.zuomei");
                    boolean b = api.registerApp(APIConstants.APP_ID);
                    api.sendReq(ret);
                    break;
                }

                case HTTP_RESPONSE_PAY_ALIPAY:{
                    mlPayAlipayData = (MLPayAlipayData) msg.obj;
                    //  String mlPayAlipayData=(String) msg.obj;;
                    payForAlipay(mlPayAlipayData.payInfo);
                    break;
                }

                case HTTP_RESPONSE_PAY_ALIPAY_AFFIRM:{
                    //支付宝支付确认
                    MLLoginFail ret = (MLLoginFail) msg.obj;
                    if(MLStrUtil.compare(ret.datas,"ok")){
                        showMessage("支付成功");
                    }else{
                        showMessage("支付失败");
                    }
                    break;
                }

                case HTTP_RESPONSE_PAY_YUE:{
                    //余额支付
                    MLRegister ret = (MLRegister) msg.obj;
                    if(ret.datas){
                        showMessage("支付成功");
                    }else{
                        showMessage("支付失败");
                    }

                    break;
                }
                case HTTP_RESPONSE_PAY_SUBMITCAR:{
                    //商城模块-提交购物车订单
                    TXShopSubmitCar ret = (TXShopSubmitCar) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){
                        StringBuffer sb = new StringBuffer();
                        for(int i=0;i<ret.datas.size();i++){
                            if(i==ret.datas.size()-1){

                                sb.append(ret.datas.get(i));
                            }else{
                                sb.append(ret.datas.get(i)+",");
                            }
                        }
                        mDataTradeNos = sb.toString();
                        pay();
                    }else{
                        showMessage("获取订单失败!");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };



    /**
     * 计算订单信息
     */
    private void getOrderInfo(){
        //数量
        SpannableString msp = new SpannableString("合计：¥"+mDataOrder.allPrice);
        msp.setSpan(new AbsoluteSizeSpan(14,true), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置字体前景色
        msp.setSpan(new ForegroundColorSpan(Color.parseColor("#3c3c3c")), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvOrderAll.setText(msp);


        double freight = 0;
        int count = 0;
        for(TXShopBuyRes.TXShopProductData data : mDataOrder.products){
            freight = freight+Double.parseDouble(data.productFreight);
            count = count+Integer.parseInt(data.productNumber);
        }
        mTvOrderCount.setText(Html.fromHtml(String.format("<font color=\"#979797\">数量：</font>%s",count+"")));
        mTvOrderFreight.setText(Html.fromHtml(String.format("<font color=\"#979797\">运费：</font>%s 元", freight + "")));
    }

    private void reviewAddress() {
        mTvName.setText(Html.fromHtml(String.format("收货人 : <font color=\"#3c3c3c\">%s   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;     %s</font>", mDataAdress.name,mDataAdress.mobile)));
        mTvAddress.setText(Html.fromHtml(String.format("收货地址 : <font color=\"#3c3c3c\">%s</font>",mDataAdress.address)));
    }


    /**
     * 支付宝支付
     *
     * @param
     */
    private void payForAlipay(String  data) {
        MLPayUtils.payForAlipay(TXShopOrderSumAty.this, data, mHandler);
    }

    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                       // alipay();
                        alipayAffirm();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {

                            showMessage(  "支付结果确认中!");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误

                            showMessage(  "支付失败!");
                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    showMessage(  "检查结果为：" + msg.obj);
                    break;
                }
                default:
                    break;
            }
        }

    };


}
