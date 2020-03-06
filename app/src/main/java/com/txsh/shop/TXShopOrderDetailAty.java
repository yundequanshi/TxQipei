package com.txsh.shop;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.adapter.TXShopOrderSumAdapter;
import com.txsh.model.TXOrderDetailRes;
import com.txsh.model.TXShopOrderRes;
import com.txsh.services.MLShopServices;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLogin;

import org.json.JSONException;
import org.json.JSONObject;

import cn.ml.base.widget.sample.MLNoScrollListView;

/**
 * Created by Marcello on 2015/7/16.
 */
public class TXShopOrderDetailAty extends BaseActivity {

//    @ViewInject(R.id.order_lv)
//    private MLNoScrollListView mListView;

    @ViewInject(R.id.orderdetail_tv_num)
    private TextView ordernum;
    @ViewInject(R.id.orderdetail_tv_time)
    private TextView ordertime;

    @ViewInject(R.id.orderdetail_tv_proname)
    private TextView orderproname;
    @ViewInject(R.id.order_tv_price)
    private TextView price;

    @ViewInject(R.id.orderdetail_num)
    private TextView orderdetailnum;
    @ViewInject(R.id.orderdetai_fei)
    private TextView orderfei;
    @ViewInject(R.id.orderdetail_price)
    private TextView ordertotalprice;

    @ViewInject(R.id.order_tv_name)
    private TextView orderperson;
    ///
    @ViewInject(R.id.order_tv_address)
    private TextView orderaddress;

    @ViewInject(R.id.order_tv_content)
    private TextView ordercontent;

    @ViewInject(R.id.order_tv_busname)
    private TextView orderbusname;

    @ViewInject(R.id.order_tv_phone)
    private TextView orderphone;

    @ViewInject(R.id.orderdetail_iv)
    private ImageView orderiv;

    public String phonenum = "";

    private TXShopOrderSumAdapter mAdapter;

    TXShopOrderRes.TXShopOrderData txShopOrderData;
    private String  dacompanyId="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tx_order_detail);
        ViewUtils.inject(this);
        if (getIntentData() != null)
            txShopOrderData = (TXShopOrderRes.TXShopOrderData) getIntentData();
        //  initView();

        initOrderDetail();
    }

    //    private void initView() {
//        mAdapter = new TXShopOrderSumAdapter(TXShopOrderDetailAty.this, R.layout.tx_item_shop_sum);
//        mListView.setAdapter(mAdapter);
//    }
//底部拨打电话
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @OnClick({R.id.order_iv_phone})
    public void phoneOnClick(View view) {

        AlertDialog builder = new AlertDialog.Builder(TXShopOrderDetailAty.this, AlertDialog.THEME_HOLO_LIGHT).setTitle("提示")
                .setMessage("拨打 " + phonenum)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL,
                                Uri.parse("tel:" + phonenum));
                        startActivity(intent);
                        //  dial("0");
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @OnClick(R.id.top_btn_left)
    public void backOnClick(View view) {
        finish();
    }
//去商品列表
    @OnClick(R.id.order_tv_label1)
    public void goShopListOnClick(View view) {
        startAct(TXShopOrderDetailAty.this, TxShopProductListAty.class, dacompanyId);
    }



    private void initOrderDetail() {
        //   if(_messageData==null)return;
        //
        MLLogin user = ((BaseApplication) this.getApplication()).get_user();
        ZMRequestParams param = new ZMRequestParams();
        param.addParameter("id", txShopOrderData.id);

        ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(ZMHttpType.RequestType.ORDERDETAIL, null, param,
                _handler, ORDERDETAILRETURN, MLShopServices.getInstance());
        loadDataWithMessage(TXShopOrderDetailAty.this, null, message1);
    }

    private static final int ORDERDETAILRETURN = 1;

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
                case ORDERDETAILRETURN: {
                    TXOrderDetailRes res = (TXOrderDetailRes) msg.obj;
                    ordernum.setText(Html.fromHtml(String.format("<font color=\"#979797\">订单号：</font>%s", res.datas.orderId + "")));
                    ordertime.setText(Html.fromHtml(String.format("<font color=\"#979797\">时&nbsp;&nbsp;&nbsp;&nbsp;间：</font>%s", res.datas.orderTime + "")));
                    orderproname.setText(res.datas.productName);
                    price.setText(res.datas.productPrice);
                    dacompanyId=res.datas.companyId;
                    SpannableString msp = new SpannableString("合计：¥" + res.datas.sumPrice);
                    msp.setSpan(new AbsoluteSizeSpan(14, true), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    //设置字体前景色
                    msp.setSpan(new ForegroundColorSpan(Color.parseColor("#3c3c3c")), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ordertotalprice.setText(msp);
                    orderdetailnum.setText(Html.fromHtml(String.format("<font color=\"#979797\">数量：</font>%s", res.datas.productNumber + "")));
                    orderfei.setText(Html.fromHtml(String.format("<font color=\"#979797\">运费：</font>%s 元", res.datas.productFreight + "")));
                    String address = "";
                    String person = "";
                    String mobile = "";
                    try {
                        JSONObject jsonObject = new JSONObject(res.datas.address);
                        address = jsonObject.getString("address");
                        person = jsonObject.getString("name");
                        mobile = jsonObject.getString("mobile");
                    } catch (JSONException e) {

                    }
                    orderperson.setText(Html.fromHtml(String.format("收货人 : <font color=\"#3c3c3c\">%s   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;     %s</font>", person, mobile)));
                    orderaddress.setText(Html.fromHtml(String.format("收货地址 : <font color=\"#3c3c3c\">%s</font>", address)));
                    ordercontent.setText(res.datas.detail);
                    orderbusname.setText(res.datas.companyName);
                    orderphone.setText(res.datas.companyMobile);
                    phonenum = res.datas.companyMobile;
                    String imgUrl = APIConstants.API_IMAGE_SHOW + res.datas.productPicture;
                    orderiv.setTag(imgUrl);
                    if (!BaseApplication.IMAGE_CACHE.get(imgUrl, orderiv)) {
                        orderiv.setImageResource(R.drawable.shop_default);
                    }
                }

            }
        }
    };

}
