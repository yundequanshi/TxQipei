package com.txsh.home;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.adapter.TxIntegralShopAdapter;
import com.zuomei.base.BaseActivity;
import com.zuomei.constants.APIConstants;

/**
 *积分商城 规则
 * Created by Marcello on 2015/6/25.
 */
public class TXIntegralShopRuleAty extends BaseActivity {

    private TxIntegralShopAdapter mAdapter;

    @ViewInject(R.id.violationweb)
    private WebView mWebView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tx_integral_rule);
        ViewUtils.inject(this);


        mWebView.getSettings().setJavaScriptEnabled(true);
        String url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
                APIConstants.API_LOTTERY_DETAIL);
        mWebView.loadUrl(url);
    }


    @OnClick(R.id.top_btn_left)
    public void backOnClick(View view){
        finish();
    }




}
