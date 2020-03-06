package com.txsh.home;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseActivity;

/**
 * 首页-实用工具
 * 
 * @author Marcello
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TXXieYi extends BaseActivity {

	@ViewInject(R.id.violationweb)
	private WebView _webview;
	@ViewInject(R.id.iv_back)
	private ImageView iv_back;
	@ViewInject(R.id.iv_go)
	private ImageView iv_go,home_top_back;
	private TextView top_title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_tools);
		Intent intent = getIntent();
		iv_back = (ImageView)findViewById(R.id.iv_back);
		iv_go = (ImageView)findViewById(R.id.iv_go);
		_webview=(WebView) findViewById(R.id.violationweb);
		home_top_back=(ImageView) findViewById(R.id.home_top_back);
		top_title=(TextView) findViewById(R.id.top_title);
		top_title.setText("用户协议");
		iv_back.setVisibility(View.GONE);
		iv_go.setVisibility(View.GONE);
		home_top_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				TXXieYi.this.finish();
			}
		});
		String id = intent.getStringExtra("id");
		_webview.getSettings().setJavaScriptEnabled(true);
		_webview.loadUrl("http://app.tianxiaqp.com:8080/tx/mobile3/protocol/showContent");
		init();
	}

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
				showProgressDialog(TXXieYi.this);
			}

		});

	}

	/**
	 * @description 返回
	 * 
	 * @author marcello
	 */
	@OnClick(R.id.home_top_back)
	public void backOnClick(View view) {
		TXXieYi.this.finish();
	}

	@OnClick(R.id.iv_back)
	public void webBackOnClick(View view) {
		_webview.goBack();
	}

	@OnClick(R.id.iv_go)
	public void webGoOnClick(View view) {
		_webview.goForward();
	}

}
