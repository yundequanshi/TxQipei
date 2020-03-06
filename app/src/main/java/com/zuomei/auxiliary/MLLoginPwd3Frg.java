package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLLoginServices;

/**
 * 登录-忘记密码 step3
 * 
 * @author Marcello
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLLoginPwd3Frg extends BaseFragment {

	public static MLLoginPwd3Frg INSTANCE = null;

	private static String phone;
	public static MLLoginPwd3Frg instance(Object obj) {
		phone = (String) obj;
			INSTANCE = new MLLoginPwd3Frg();
		return INSTANCE;
	}

	@ViewInject(R.id.et_oldPwd)
	private EditText _oldEt;
	
	@ViewInject(R.id.et_newPwd)
	private EditText _newEt;

	private Context _context;
	@ViewInject(R.id.tv_label)
	private TextView _labelTv;
	
	private String mCode;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.login_pwd3, null);
		ViewUtils.inject(this, view);
		_context = inflater.getContext();
		
	
		return view;
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		_oldEt.setText("");
		_newEt.setText("");
	}
	/**
	 * @description 返回
	 * 
	 * @author marcello
	 */
	@OnClick(R.id.home_top_back)
	public void backOnClick(View view) {
		((MLAuxiliaryActivity) _context).finish();
	}

	/**
	 * @description 下一步
	 * 
	 * @author marcello
	 */
	@OnClick(R.id.btn_next)
	public void nextOnClick(View view) {
		
		
		String oldPwd = _oldEt.getText().toString();
		String newPwd = _newEt.getText().toString();
		if(!oldPwd.equalsIgnoreCase(newPwd)){
			showMessageWarning("两次密码输入不一致！");
			return;
		}
		
		
		ZMRequestParams param = new ZMRequestParams();
		param.addParameter(MLConstants.PARAM_LOGIN_NUMBER,phone);
		param.addParameter(MLConstants.PARAM_LOGIN_PASSWORD,newPwd);
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.LOGIN_RESET_PWD, null, param, _handler, HTTP_RESPONSE_PWD, MLLoginServices.getInstance());
	    loadDataWithMessage(_context, null, message1);
		
	}

	
	 private static final int HTTP_RESPONSE_PWD= 2;
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
	            case  HTTP_RESPONSE_PWD:{
	            		MLRegister ret = (MLRegister) msg.obj;
	            		if(ret.state.equalsIgnoreCase("1")){
	            			showMessageSuccess("密码修改成功!");
	            			((MLAuxiliaryActivity) _context).finish();
	                	}else{
	                		showMessageError("密码修改失败!");
	                	}
	            	break;
	            }
	                default:
	                    break;
	            }
	        }
	    };
}
