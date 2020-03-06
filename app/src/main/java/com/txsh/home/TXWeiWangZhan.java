package com.txsh.home;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseActivity;

import cn.ml.base.utils.MLStrUtil;

/**
 * 首页-实用工具
 * 
 * @author Marcello
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TXWeiWangZhan extends BaseActivity {

	@ViewInject(R.id.violationweb)
	private WebView _webview;
	@ViewInject(R.id.iv_back)
	private ImageView iv_back;
	@ViewInject(R.id.iv_go)
	private ImageView iv_go, home_top_back;
	private TextView top_title;

	private void init() {
		_webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				dismissProgressDialog();
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				showProgressDialog(TXWeiWangZhan.this);
			}

		});

	}

	private Handler mHandler = new Handler();

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.my_tools);
		Intent intent = getIntent();
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_go = (ImageView) findViewById(R.id.iv_go);
		_webview = (WebView) findViewById(R.id.violationweb);
		home_top_back = (ImageView) findViewById(R.id.home_top_back);
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("微网站");
		iv_back.setVisibility(View.GONE);
		iv_go.setVisibility(View.GONE);
		home_top_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				TXWeiWangZhan.this.finish();
			}
		});
		String url = intent.getStringExtra("url");

	/*	WebSettings webSettings = _webview.getSettings();
		webSettings.setSavePassword(false);
		webSettings.setSaveFormData(false);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(false);
		_webview.getSettings().setDomStorageEnabled(true);
		_webview.getSettings().setDatabaseEnabled(true);
		_webview.setWebChromeClient(new WebChromeClient()
        {

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                    JsResult result)
            {
                // TODO Auto-generated method stub
                return super.onJsAlert(view, url, message, result);
            }

        });

		_webview.addJavascriptInterface(new DemoJavaScriptInterface(), "demo");
		_webview.loadUrl(url);*/

		initWebView(url);
	//	init();

	}

	private void initWebView(String url)
	{
		this._webview.clearCache(true);
		getApplicationContext().deleteDatabase("webview.db");
		getApplicationContext().deleteDatabase("webviewCache.db");
		getApplicationContext().deleteDatabase("webview.db");
		getApplicationContext().deleteDatabase("webviewCache.db");
		this._webview.loadUrl(url);
		this._webview.getSettings().setJavaScriptEnabled(true);
		this._webview.getSettings().setCacheMode(1);
		this._webview.setDownloadListener(new DownloadListener()
		{
			public void onDownloadStart(String paramAnonymousString1, String paramAnonymousString2, String paramAnonymousString3, String paramAnonymousString4, long paramAnonymousLong)
			{
				new Intent("android.intent.action.VIEW").setDataAndType(Uri.parse(paramAnonymousString1), paramAnonymousString4);
			}
		});
		this._webview.setWebViewClient(new HelloWebViewClient());
	}


	final class DemoJavaScriptInterface {

		DemoJavaScriptInterface() {
		}

		/**
		 * This is not called on the UI thread. Post a runnable to invoke
		 * loadUrl on the UI thread.
		 */
		public void clickOnAndroid() {
			mHandler.post(new Runnable() {
				public void run() {
					_webview.loadUrl("javascript:wave()");
				}
			});

		}
	}


	/**
	 * @description 返回
	 * 
	 * @author marcello
	 */
	@OnClick(R.id.home_top_back)
	public void backOnClick(View view) {
		TXWeiWangZhan.this.finish();
	}

	@OnClick(R.id.iv_back)
	public void webBackOnClick(View view) {
		_webview.goBack();
	}

	@OnClick(R.id.iv_go)
	public void webGoOnClick(View view) {
		_webview.goForward();
	}


	class HelloWebViewClient
			extends WebViewClient
	{
		HelloWebViewClient() {}

		public void onPageFinished(WebView paramWebView, String paramString)
		{
			super.onPageFinished(paramWebView, paramString);
		}

		public void onPageStarted(WebView paramWebView, String paramString, Bitmap paramBitmap)
		{
			super.onPageStarted(paramWebView, paramString, paramBitmap);
		}

		public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
		{

			if(!MLStrUtil.isEmpty(paramString)&&paramString.startsWith("tel")){
				Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse(paramString));
				startActivity(intent);
				return true;
			}

			if (paramWebView != null)
			{
				TXWeiWangZhan.this.getApplicationContext().deleteDatabase("webview.db");
				TXWeiWangZhan.this.getApplicationContext().deleteDatabase("webviewCache.db");
				TXWeiWangZhan.this.getApplicationContext().deleteDatabase("webview.db");
				TXWeiWangZhan.this.getApplicationContext().deleteDatabase("webviewCache.db");
				paramWebView.loadUrl(paramString);
			}
			return true;
		}
	}
}
