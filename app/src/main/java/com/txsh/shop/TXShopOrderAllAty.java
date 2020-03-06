package com.txsh.shop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.ab.view.pullview.AbPullToRefreshView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.txsh.R;
import com.txsh.adapter.TXShopOrderDfkAdapter;
import com.txsh.adapter.TXShopOrderDshAdapter;
import com.txsh.adapter.TXShopOrderYshAdapter;
import com.txsh.model.TXEventModel;
import com.txsh.model.TXShopOrderRes;
import com.txsh.model.TXShopYueCheckRes;
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
import com.zuomei.utils.MLPayUtils;
import com.zuomei.utils.MathUtils;
import com.zuomei.utils.alipay.PayResult;

import java.util.List;

import cn.ml.base.utils.MLDialogUtils;
import cn.ml.base.utils.MLStrUtil;
import cn.ml.base.utils.MLToolUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 全部订单
 * Created by Marcello on 2015/7/16.
 */
public class TXShopOrderAllAty extends BaseActivity {

    @ViewInject(R.id.order_lv)
    private ListView mListView;

    @ViewInject(R.id.interact_rg)
    private RadioGroup mGroup;

    @ViewInject(R.id.order_refresh)
    private AbPullToRefreshView pullToRefreshLv;

    // ************微信支付****************************************************
    private IWXAPI api;

    //待付款
    private TXShopOrderDfkAdapter mAdapterDfk;
    //待收货
    private TXShopOrderDshAdapter mAdapterDsh;
    //已收货
    private TXShopOrderYshAdapter mAdapterYsh;

    int type=1;
    int page=1;
    boolean isRefresh = true;

    public List<TXShopOrderRes.TXShopOrderData> mDataDfk;
    public List<TXShopOrderRes.TXShopOrderData> mDataDsh;
    public List<TXShopOrderRes.TXShopOrderData> mDataYsh;

    private MLPayAlipayData mlPayAlipayData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tx_order_all);
        EventBus.getDefault().register(this);
        ViewUtils.inject(this);

        initView();
        initDataDfk();
        initWeixin();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        mAdapterDfk = new TXShopOrderDfkAdapter(TXShopOrderAllAty.this,R.layout.tx_item_shop_dfk);
        mAdapterDsh = new TXShopOrderDshAdapter(TXShopOrderAllAty.this,R.layout.tx_item_shop_ysh);
        mAdapterYsh = new TXShopOrderYshAdapter(TXShopOrderAllAty.this,R.layout.tx_item_shop_ysh);
        mListView.setAdapter(mAdapterDfk);

        mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.order_rb_tab1: {
                        //待付款
                        type = 1;
                        mListView.setAdapter(mAdapterDfk);
                        initDataDfk();
                        break;
                    }

                    case R.id.order_rb_tab2: {
                        //待收货
                        type = 2;
                        mListView.setAdapter(mAdapterDsh);
                        initDataDsh();
                        break;
                    }

                    case R.id.order_rb_tab3: {
                        //已收货
                        type = 3;
                        mListView.setAdapter(mAdapterYsh);
                        initDataYsh();
                        break;
                    }

                }
            }
        });

        pullToRefreshLv
                .setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
                    @Override
                    public void onHeaderRefresh(AbPullToRefreshView view) {
                        page = 1;
                        isRefresh = true;
                        if (type == 1) {
                            initDataDfk();
                        } else if (type == 2) {
                            initDataDsh();
                        } else {
                            initDataYsh();
                        }

						/*
						 * if(MLToolUtil.isNull(keyWord)){ initData(); }else{
						 * search(); }
						 */
                    }
                });
        pullToRefreshLv.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
            @Override
            public void onFooterLoad(AbPullToRefreshView view) {
                page++;
                isRefresh = false;
                if (type == 1) {
                    initDataDfk();
                } else if (type == 2) {
                    initDataDsh();
                } else {
                    initDataYsh();
                }
            }
        });


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (type == 1) {
                    startAct(TXShopOrderAllAty.this, TXShopOrderDetailAty.class, mAdapterDfk.getItem(position));
                } else if (type == 2) {
                    startAct(TXShopOrderAllAty.this, TXShopOrderDetailAty.class, mAdapterDsh.getItem(position));
                } else {
                    startAct(TXShopOrderAllAty.this, TXShopOrderDetailAty.class, mAdapterYsh.getItem(position));
                }
            }
        });

    }


    @Subscribe
    public void eventOnClick(TXEventModel eventModel){

        switch (eventModel.type){
            //取消订单
            case MLConstants.EVENT_PARAM_ORDER_CANCEL:{
                TXShopOrderRes.TXShopOrderData data = (TXShopOrderRes.TXShopOrderData) eventModel.obj;
                cancelOrder(data.id);
                break;
            }
            //确认收货
            case MLConstants.EVENT_PARAM_ORDER_AFFIRM:{
                TXShopOrderRes.TXShopOrderData data = (TXShopOrderRes.TXShopOrderData) eventModel.obj;
                affirmPay(data.id);

                break;
            }
            //退货
            case MLConstants.EVENT_PARAM_ORDER_QUIT:{
                TXShopOrderRes.TXShopOrderData data = (TXShopOrderRes.TXShopOrderData) eventModel.obj;
                quitPay(data.id);
                break;
            }
            //评价
            case MLConstants.EVENT_PARAM_ORDER_EVALUATE:{
                TXShopOrderRes.TXShopOrderData data = (TXShopOrderRes.TXShopOrderData) eventModel.obj;
                      startAct(TXShopOrderAllAty.this, TXShopPJChatAty.class, data.id);
                break;
            }
            //付款
            case MLConstants.EVENT_PARAM_ORDER_PAY:{
                TXShopOrderRes.TXShopOrderData data = (TXShopOrderRes.TXShopOrderData) eventModel.obj;
                if(data.sumPrice==0){
                    showMessage("请联系商家改价");
                    return;
                }else{
                    pay(data);
                }

                break;
            }

            //物流信息
            case MLConstants.EVENT_PARAM_ORDER_LOGISTICAL:{
                TXShopOrderRes.TXShopOrderData data = (TXShopOrderRes.TXShopOrderData) eventModel.obj;
                startAct(TXShopOrderAllAty.this,TXShopLogisticalAty.class,data);
                break;
            }

            //微信支付成功确认
            case MLConstants.EVENT_PARAM_ORDER_PAY_WXOK:{
                initDataDfk();
                break;
            }

        }
    }


    /**
     * 待付款
     */
    private void initDataDfk(){
        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("depotId", BaseApplication.getInstance().get_user().Id);
        params.addParameter("nowPage", page + "");
        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_SHOP_ORDER_DFK, null, params, _handler
                ,HTTP_RESPONSE_DFK,MLShopServices.getInstance());
        loadDataWithMessage(TXShopOrderAllAty.this, "正在加载，请稍等...", message2);
    }


    /**
     * 待收货
     */
    private void initDataDsh(){
        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("nowPage",page+"");
        params.addParameter("depotId", BaseApplication.getInstance().get_user().Id);
        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_SHOP_ORDER_DSH, null, params, _handler
                ,HTTP_RESPONSE_DSH,MLShopServices.getInstance());
        loadDataWithMessage(TXShopOrderAllAty.this, "正在加载，请稍等...", message2);
    }


    /**
     * 已收货
     */
    private void initDataYsh(){
        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("nowPage", page + "");
        params.addParameter("depotId", BaseApplication.getInstance().get_user().Id);
        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_SHOP_ORDER_YSH, null, params, _handler
                ,HTTP_RESPONSE_YSH,MLShopServices.getInstance());
        loadDataWithMessage(TXShopOrderAllAty.this, "正在加载，请稍等...", message2);
    }

    /**
     * 取消订单
     */
    private void cancelOrder(final String id){
        AlertDialog.Builder builder = MLDialogUtils.getAlertDialog(TXShopOrderAllAty.this);
        builder.setTitle("提示");
        builder.setMessage("确认取消订单？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ZMRequestParams params = new ZMRequestParams();
                params.addParameter("id", id);
                ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_PAY_CANCEL, null, params, _handler
                        , HTTP_RESPONSE_CANCEL, MLShopServices.getInstance());
                loadDataWithMessage(TXShopOrderAllAty.this, "正在加载，请稍等...", message2);
            }
        });
        builder.show();
    }

    /**
     * 确认收货
     */
    private void affirmPay(final String id){
        AlertDialog.Builder builder = MLDialogUtils.getAlertDialog(TXShopOrderAllAty.this);
        builder.setTitle("提示");
        builder.setMessage("确认操作收货？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ZMRequestParams params = new ZMRequestParams();
                params.addParameter("id", id);
                ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_PAY_AFFIRM, null, params, _handler
                        , HTTP_RESPONSE_AFFRIM, MLShopServices.getInstance());
                loadDataWithMessage(TXShopOrderAllAty.this, "正在加载，请稍等...", message2);
            }
        });
        builder.show();
    }

    /**
     * 退货
     */
    private void quitPay(final String id){
        AlertDialog.Builder builder = MLDialogUtils.getAlertDialog(TXShopOrderAllAty.this);
        builder.setTitle("提示");
        builder.setMessage("确认操作退货？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ZMRequestParams params = new ZMRequestParams();
                params.addParameter("id", id);
                ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_PAY_QUIT, null, params, _handler
                        , HTTP_RESPONSE_QUIT, MLShopServices.getInstance());
                loadDataWithMessage(TXShopOrderAllAty.this, "正在加载，请稍等...", message2);
            }
        });
        builder.show();
    }

    /**
     * 付款
     * @param data
     */
    double mDataYue=0;
    private TXShopOrderRes.TXShopOrderData mDataPay;
    private void pay(TXShopOrderRes.TXShopOrderData data){
        mDataPay = data;
        if(MLStrUtil.compare(data.payType,"2")){
            //微信支付
            payWx(0);
        }else if(MLStrUtil.compare(data.payType,"1")){
            //支付宝支付
            payAlipay(0);
        }else{
            //余额支付
            payCheck();
        }
    }

    private void payWx(int type){
      /*  double price = Double.parseDouble(mDataPay.productPrice);
        double fright = Double.parseDouble(mDataPay.productFreight);

        double sum = MathUtils.add(price,fright);*/
        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("tradeNos", mDataPay.orderId);
        params.addParameter("ip", MLToolUtil.getLocalIpAddress());
        //余额不足时

        if(type==0){
            params.addParameter("money", (int)(mDataPay.sumPrice*100)+"");
        }else{

            params.addParameter("balance",mDataYue+"");
            params.addParameter("money", (int)(MathUtils.sub(mDataPay.sumPrice, mDataYue))+"");
        }
        params.addParameter("allMoney",mDataPay.sumPrice+"");
        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_SHOP_PRODUCT_PAY_WX, null, params, _handler
                ,HTTP_RESPONSE_PAY_WX , MLShopServices.getInstance());
        loadDataWithMessage(TXShopOrderAllAty.this, "正在支付，请稍等...", message2);
    }

    private void payWxYue(TXShopYueCheckRes.TXShopYueCheckData datas){

        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("tradeNos", mDataPay.orderId);
        params.addParameter("ip", MLToolUtil.getLocalIpAddress());
        params.addParameter("balance", datas.money+"");
        params.addParameter("money", (int)((MathUtils.sub(datas.payCount,datas.money))*100)+"");
        params.addParameter("allMoney",datas.payCount+"");
        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_SHOP_PRODUCT_PAY_WX, null, params, _handler
                ,HTTP_RESPONSE_PAY_WX , MLShopServices.getInstance());
        loadDataWithMessage(TXShopOrderAllAty.this, "正在支付，请稍等...", message2);
    }

    private void payAlipay(int type){
        double price = Double.parseDouble(mDataPay.productPrice);
        double fright = Double.parseDouble(mDataPay.productFreight);
      //  double sum = MathUtils.add(price, fright);

        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("tradeNos",  mDataPay.orderId);
        if(type==0){
            params.addParameter("money", mDataPay.sumPrice+"");
        }else{

            params.addParameter("balance",mDataPay.sumPrice+"");
            params.addParameter("money", (MathUtils.sub(mDataPay.sumPrice,mDataYue))+"");
        }
        params.addParameter("allMoney",mDataPay.sumPrice+"");

        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_SHOP_PRODUCT_PAY_ALIPAY, null, params, _handler
                ,HTTP_RESPONSE_PAY_ALIPAY , MLShopServices.getInstance());
        loadDataWithMessage(TXShopOrderAllAty.this, "正在支付，请稍等...", message2);
    }

    private void payAlipayYue(TXShopYueCheckRes.TXShopYueCheckData datas){

        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("tradeNos",  mDataPay.orderId);
        params.addParameter("balance", datas.money+"");
        params.addParameter("money", (MathUtils.sub(datas.payCount,datas.money))+"");
        params.addParameter("allMoney",datas.payCount+"");

        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_SHOP_PRODUCT_PAY_ALIPAY, null, params, _handler
                ,HTTP_RESPONSE_PAY_ALIPAY , MLShopServices.getInstance());
        loadDataWithMessage(TXShopOrderAllAty.this, "正在支付，请稍等...", message2);
    }


    private void payYue(TXShopYueCheckRes.TXShopYueCheckData datas) {
        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("tradeNos", mDataPay.orderId);
        params.addParameter("balance", datas.payCount+"");
        params.addParameter("allMoney", datas.payCount+"");
        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_SHOP_PRODUCT_PAY_YUE, null, params, _handler
                , HTTP_RESPONSE_PAY_YUE, MLShopServices.getInstance());
        loadDataWithMessage(TXShopOrderAllAty.this, "正在支付，请稍等...", message2);
    }

    //余额支付订单检查
    private void  payCheck(){
        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("id", mDataPay.id);
        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_PAY_YUE_CHECEK, null, params, _handler
                ,HTTP_RESPONSE_PAY_YUE_CHECK , MLShopServices.getInstance());
        loadDataWithMessage(TXShopOrderAllAty.this, "正在支付，请稍等...", message2);
    }


    public void alipayAffirm(){
        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("out_trade_no", mDataPay.orderId);
        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_PAY_ALIPAY_AFFIRM, null, params, _handler
                ,HTTP_RESPONSE_PAY_ALIPAY_AFFIRM , MLShopServices.getInstance());
        loadDataWithMessage(TXShopOrderAllAty.this, "正在确认，请稍等...", message2);
    }

    private void payForAlipay(String  data) {
        MLPayUtils.payForAlipay(TXShopOrderAllAty.this, data, mHandler);
    }


    @OnClick(R.id.top_btn_left)
    public void backOnClick(View view){
        finish();
    }




    private static final int HTTP_RESPONSE_DFK = 0;
    private static final int HTTP_RESPONSE_DSH = 1;
    private static final int HTTP_RESPONSE_YSH = 2;
    private static final int HTTP_RESPONSE_CANCEL = 3;
    private static final int HTTP_RESPONSE_AFFRIM = 4;
    private static final int HTTP_RESPONSE_QUIT = 5;
    private static final int HTTP_RESPONSE_PAY_WX = 6;
    private static final int HTTP_RESPONSE_PAY_ALIPAY = 7;
    private static final int HTTP_RESPONSE_PAY_ALIPAY_AFFIRM = 8;
    private static final int HTTP_RESPONSE_PAY_YUE_CHECK = 9;
    private static final int HTTP_RESPONSE_PAY_YUE = 10;


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
                case HTTP_RESPONSE_DFK:{
                    //待付款
                    TXShopOrderRes ret = (TXShopOrderRes) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){

                        if(isRefresh){
                            mDataDfk = ret.datas;

                        }else{
                            mDataDfk.addAll(ret.datas);
                        }
                        mAdapterDfk.setData(mDataDfk);

                    }else{
                        showMessage("获取订单失败");
                    }
                    pullToRefreshLv.onHeaderRefreshFinish();
                    pullToRefreshLv.onFooterLoadFinish();
                    break;
                }

                case HTTP_RESPONSE_DSH:{
                    //待收货
                    TXShopOrderRes ret = (TXShopOrderRes) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){
                        if(isRefresh){
                            mDataDsh = ret.datas;

                        }else{
                            mDataDsh.addAll(ret.datas);
                        }
                        mAdapterDsh.setData(mDataDsh);
                    }else{
                        showMessage("获取订单失败");
                    }
                    pullToRefreshLv.onHeaderRefreshFinish();
                    pullToRefreshLv.onFooterLoadFinish();
                    break;
                }

                case HTTP_RESPONSE_YSH:{
                    //已收货
                    TXShopOrderRes ret = (TXShopOrderRes) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){
                        if(isRefresh){
                            mDataYsh = ret.datas;

                        }else{
                            mDataYsh.addAll(ret.datas);
                        }
                        mAdapterYsh.setData(mDataYsh);
                    }else{
                        showMessage("获取订单失败");
                    }
                    pullToRefreshLv.onHeaderRefreshFinish();
                    pullToRefreshLv.onFooterLoadFinish();
                    break;
                }

                case HTTP_RESPONSE_CANCEL:{
                    //订单详情
                    MLRegister ret = (MLRegister) msg.obj;
                    if(ret.datas){
                       showMessage("订单取消成功");
                        initDataDfk();
                    }else{
                        showMessage("订单取消失败");
                    }

                    break;
                }


                case HTTP_RESPONSE_AFFRIM:{
                    //确认收货
                    MLRegister ret = (MLRegister) msg.obj;
                    if(ret.datas){
                        showMessage("操作成功");
                        initDataDsh();
                    }else{
                        showMessage("操作失败");
                    }

                    break;
                }

                case HTTP_RESPONSE_QUIT:{
                    //退货
                    MLRegister ret = (MLRegister) msg.obj;
                    if(ret.datas){
                        showMessage("操作成功");
                        initDataYsh();
                    }else{
                        showMessage("操作失败");
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
                        initDataDfk();
                    }else{
                        showMessage("支付失败");
                    }
                    break;
                }
                case HTTP_RESPONSE_PAY_YUE_CHECK:{
                    //106、余额支付订单检查
                    final TXShopYueCheckRes ret = (TXShopYueCheckRes) msg.obj;
                   /* money:修理厂的余额
                    payCount：需要支付的余额*/
                    if(ret.datas.money>ret.datas.payCount){
                         payYue(ret.datas);
                    }else{
                        AlertDialog builder =   MLDialogUtils.getAlertDialog(TXShopOrderAllAty.this).setTitle("提示")
                                .setMessage(Html.fromHtml(String.format("余额不足，剩余<font color=\"#E90C0C\">%s元</font>请选择其他支付方式", String.valueOf(MathUtils.sub(ret.datas.payCount, ret.datas.money)))))
                                .setPositiveButton("支付宝", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        payAlipayYue(ret.datas);
                                    }
                                })
                                .setNegativeButton("微信", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        payWxYue(ret.datas);
                                    }
                                })
                                .show();
                    }
                    break;
                }

                case HTTP_RESPONSE_PAY_YUE:{
                    //余额支付
                    MLRegister ret = (MLRegister) msg.obj;
                    if(ret.datas){
                        showMessage("支付成功");
                        initDataDfk();
                    }else{
                        showMessage("支付失败");
                    }
                    break;
                }

                default:
                    break;
            }


        }
    };



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


    private void initWeixin() {
        api = WXAPIFactory.createWXAPI(TXShopOrderAllAty.this, APIConstants.APP_ID);
    }

}
