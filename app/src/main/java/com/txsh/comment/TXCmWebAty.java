package com.txsh.comment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.model.TxCmWebData;
import com.zuomei.base.BaseActivity;

/**
 * web 页面
 * Created by Marcello on 2015/6/25.
 */
public class TXCmWebAty extends BaseActivity {


    @ViewInject(R.id.violationweb)
    private WebView mWebView;

    @ViewInject(R.id.top_tv_title)
    private TextView mTvTitle;

    private TxCmWebData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tx_integral_rule);
        ViewUtils.inject(this);

        data = (TxCmWebData) getIntentData();

        Integer.parseInt("1");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(data.url);
        mTvTitle.setText(data.title);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
		    	/*  int star = url.indexOf("http");
		    	  url = url.substring(star);*/

              //  url = url.replaceAll("baidumap","http");

                String uu = url;
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setJavaScriptEnabled(true);

        WebSettings webSettings = mWebView.getSettings();
        //启用数据库

        //启用数据库
        webSettings.setDatabaseEnabled(true);
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();

//启用地理定位
        webSettings.setGeolocationEnabled(true);
//设置定位的数据库路径
        webSettings.setGeolocationDatabasePath(dir);

//最重要的方法，一定要设置，这就是出不来的主要原因

        webSettings.setDomStorageEnabled(true);

//配置权限（同样在WebChromeClient中实现）


        mWebView.setWebChromeClient(new GeoClient());

/*
        webSettings.setDatabaseEnabled(true);
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        //设置定位的数据库路径
        webSettings.setGeolocationDatabasePath(dir);
        //启用地理定位
        webSettings.setGeolocationEnabled(true);
        //最重要的方法，一定要设置，这就是出不来的主要原因
        webSettings.setDomStorageEnabled(true);

        webSettings.setBlockNetworkImage(false);
        webSettings.setBlockNetworkLoads(false);

        webSettings.setAllowFileAccess(true);*/
    }


    @OnClick(R.id.top_btn_left)
    public void backnClick(View view){
        finish();
    }

    private class GeoClient extends WebChromeClient {
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,
                                                       android.webkit.GeolocationPermissions.Callback callback) {
            super.onGeolocationPermissionsShowPrompt(origin, callback);
            callback.invoke(origin, true, false);
        }
    }
}
