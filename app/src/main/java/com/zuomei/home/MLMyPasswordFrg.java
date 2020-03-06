package com.zuomei.home;

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

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLMyServices;
import com.zuomei.utils.MLToolUtil;

/**
 *  密码修改
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyPasswordFrg extends BaseFragment{

	@ViewInject(R.id.my_et_old)
	private EditText _oldeEt;
	
	@ViewInject(R.id.my_et_pw)
	private EditText _newEt1;
	
	@ViewInject(R.id.my_et_pw1)
	private EditText _newEt2;
	public static MLMyPasswordFrg INSTANCE =null;
	
	public static MLMyPasswordFrg instance(){
//		if(INSTANCE==null){
			INSTANCE = new MLMyPasswordFrg();
	//	}
		return INSTANCE;
	}
	
	private Context _context;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_password, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		return view;
	}
	
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		_oldeEt.setText("");
		_newEt1.setText("");
		_newEt2.setText("");
	}


	@OnClick(R.id.my_btn_ok)
	public void nextClick(View view){ 
		String oldPwd = _oldeEt.getText().toString();
		String newPwd1 = _newEt1.getText().toString();
		String newPwd2 = _newEt2.getText().toString();
		
		   if(MLToolUtil.isNull(oldPwd)){
			   showMessage("请输入原密码！");
			   return;
		   }
		   if(MLToolUtil.isNull(newPwd1)){
			   showMessage("新密码不能为空！");
			   return;
		   }
		if(!newPwd1.equalsIgnoreCase(newPwd2)){
			showMessage("两次密码输入不一致!");
			return;
		}
		String pwd = BaseApplication.aCache.getAsString(MLConstants.PARAM_REGISTER_PWD);
		
		   if(MLToolUtil.isNull(pwd)){
			   showMessage("请重新登录！");
			   return;
		   }
		
		if(!pwd.equalsIgnoreCase(oldPwd)){
			showMessage("原密码不正确!");
			return;
		}
		
		MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
		 ZMRequestParams params = new ZMRequestParams();
			if(user.isDepot){
				params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,user.Id);
			}else{
				params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,user.Id);
			}
	      params.addParameter("oldPwd",oldPwd);
	      params.addParameter("newPwd",newPwd2);

	      ZMHttpRequestMessage message = new ZMHttpRequestMessage(RequestType.MY_UPDATE_PWD, null, params, _handler, HTTP_RESPONSE_PWD, 	MLMyServices.getInstance());
	      loadDataWithMessage(_context, null, message);
		
		
	}
	
	 private static final int HTTP_RESPONSE_PWD = 1;
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
	                case HTTP_RESPONSE_PWD:{
	                	MLRegister ret = (MLRegister) msg.obj;
	                	if(ret.state.equalsIgnoreCase("1")&&ret.datas){
	                		showMessageSuccess("修改密码成功!");
	                		((MLAuxiliaryActivity)_context).onBackPressed();
	                	}else{
	                		showMessageError("修改密码失败!");
	                	}
	                	break;
	                }
	                default:
	                    break;
	            }
	        }
	    };
	    
	
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
