package com.zuomei.login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.base.BaseFragment;

import cn.ml.base.utils.IEvent;

/**
 * 首页界面
 * @author Marcello
 *
 */
public class MLHomeFrg extends BaseFragment{

	public static MLHomeFrg INSTANCE =null;
	
	public static MLHomeFrg instance(){
		if(INSTANCE==null){
			INSTANCE = new MLHomeFrg();
		}
		return INSTANCE;
	}
	
	@ViewInject(R.id.login_et_business)
	private EditText _businessEt;
	
	//注册
	@ViewInject(R.id.login_register)
	private Button _registerBtn;
	
	private View mView;
	private ListView _chooseLt;
	private Context _context;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_tab1, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();



		return view;
	}



	private IEvent<Object> _event;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_event = (IEvent<Object>) activity;
	}

	public void reviewScrollview() {

	}
}
