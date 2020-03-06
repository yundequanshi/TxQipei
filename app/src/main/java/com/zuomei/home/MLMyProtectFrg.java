package com.zuomei.home;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.base.BaseFragment;

/**
 *  密码保护
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyProtectFrg extends BaseFragment{

	public static MLMyProtectFrg INSTANCE =null;
	
	public static MLMyProtectFrg instance(){
	//	if(INSTANCE==null){
			INSTANCE = new MLMyProtectFrg();
//		}
		return INSTANCE;
	}
	
/*	@ViewInject(R.id.login_et_business)
	private EditText _businessEt;
	
	//登录
	@ViewInject(R.id.login_btn)
	private Button _loginBtn;
	
	*/
	
	private Context _context;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_protect, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		return view;
	}
	
	

	/**
	  * @description  返回
	  *
	  * @author marcello
	 */
	@OnClick(R.id.my_top_back)
	public void backOnClick(View view){
		((MLAuxiliaryActivity)_context).onBackPressed();
	}
}
