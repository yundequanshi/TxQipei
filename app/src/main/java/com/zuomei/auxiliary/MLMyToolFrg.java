package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseFragment;
/**
 * 首页-实用工具
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyToolFrg extends BaseFragment{

	public static MLMyToolFrg INSTANCE =null;
	
	public static MLMyToolFrg instance(){
	//	if(INSTANCE==null){
			INSTANCE = new MLMyToolFrg();
	//	}
		return INSTANCE;
	}
	private Context _context;
	@ViewInject(R.id.violationweb)
	private WebView _webview;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_tools, null);
		ViewUtils.inject(this,view);
		_context = inflater.getContext();
		
		init();
		return view;
	}
	
	private void init() {
		_webview.setWebViewClient(new WebViewClient() {      
		      @Override      
		      public boolean shouldOverrideUrlLoading(WebView view, String url)      
		      {      
		    	  int star = url.indexOf("http");
		    	  url = url.substring(star);
		        view.loadUrl(url);      
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
				showProgressDialog("正在加载...",_context);
			}
		      
		    });       
		_webview.getSettings().setJavaScriptEnabled(true);
	     //_webview.loadUrl("http://m.cheshouye.com/api/weizhang/?dp=3&dc=23&t=2394E4");
//天气	     _webview.loadUrl("http://waptianqi.2345.com/jinan-54823.htm");
//购车计算器		http://m.46644.com/tool/loan/car.php?tpltype=uc
//汽车标志大全  _webview.loadUrl("http://m.46644.com/tool/car/");
//快递100       http://m.kuaidi100.com/
//公交车   http://gj.aibang.com/
//		_webview.loadUrl("http://hao.uc.cn/bst/index?uc_param_str=prdnfrpfbivelabtbmntpvsscp");
		_webview.loadUrl("http://hao.uc.cn/bst/index");
		
		
	}
	/**
	  * @description   返回
	  *
	  * @author marcello
	 */
	@OnClick(R.id.home_top_back)
	public void backOnClick(View view){
		((MLAuxiliaryActivity)_context).finish();
	}
	
	@OnClick(R.id.iv_back)
	public void webBackOnClick(View view){
		_webview.goBack();
	}
	
	@OnClick(R.id.iv_go)
	public void webGoOnClick(View view){
		_webview.goForward();
	}
	
	
}
