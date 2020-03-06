package com.zuomei.home;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.base.BaseFragment;

import cn.ml.base.utils.IEvent;

/**
 * 违章
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLViolationWebFrg extends BaseFragment{

	public static MLViolationWebFrg INSTANCE =null;
	
	public static MLViolationWebFrg instance(){
		if(INSTANCE==null){
			INSTANCE = new MLViolationWebFrg();
		}
		return INSTANCE;
	}
	
	@ViewInject(R.id.violationweb)
	private WebView _webview;
	
	private Context _context;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_violationweb, null);
		ViewUtils.inject(this,view);
		_context = inflater.getContext();
		_webview.setWebViewClient(new WebViewClient() {      
		      @Override      
		      public boolean shouldOverrideUrlLoading(WebView view, String url)      
		      {      
		    	/*  int star = url.indexOf("http");
		    	  url = url.substring(star);*/
		        view.loadUrl(url);      
		        return true;      
		      }      
		    });       
		
		int version = Build.VERSION.SDK_INT;
		
		if(version<14){
			_webview.loadUrl("http://cha.wcar.net.cn");
		}else{
			_webview.loadUrl("http://m.cheshouye.com/api/weizhang/?dp=3&dc=23&t=2394E4");
		}
		
		
		_webview.getSettings().setJavaScriptEnabled(true);
		return view;
	}
	
	
	
	private void initView() {
		// TODO Auto-generated method stub
		
	}

	private IEvent<Object> _event;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_event = (IEvent<Object>) activity;
	}
}
