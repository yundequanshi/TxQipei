package com.zuomei.home;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.txsh.R;
import com.zuomei.base.BaseFragment;

import cn.ml.base.utils.IEvent;

/**
 * 违章
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLViolationFrg extends BaseFragment{

	public static MLViolationFrg INSTANCE =null;
	
	public static MLViolationFrg instance(){
		if(INSTANCE==null){
			INSTANCE = new MLViolationFrg();
		}
		return INSTANCE;
	}
	
	/*@ViewInject(R.id.violation_wb)
	private WebView _webview;*/
	
	private Context _context;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_violation, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		
		initView();
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
