package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.app.Activity;
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
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLMyServices;
import com.zuomei.utils.MLToolUtil;

import cn.ml.base.utils.IEvent;

/**
 * 绑定财付通
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLBindFrg extends BaseFragment{

	public static MLBindFrg INSTANCE =null;
	
	
	static String isCompany="";
	public static MLBindFrg instance(Object obj){
		isCompany = (String) obj;
			INSTANCE = new MLBindFrg();
		return INSTANCE;
	}
	
	@ViewInject(R.id.add_et_name)
	private EditText _nameEt;
	
	@ViewInject(R.id.add_et_cft)
	private EditText _cftEt;
	
	private Context _context;
	
	
	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.my_bind, null);
		ViewUtils.inject(this,view);
		_context = inflater.getContext();
		return view;
	}
	

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		_nameEt.setText("");
		_cftEt.setText("");
	}


	/**
	  * @description   返回
	  *
	  * @author marcello
	 */
	@OnClick(R.id.top_back)
	public void backOnClick(View view){
//		((MLAuxiliaryActivity)_context).finish();
		((MLAuxiliaryActivity)_context).onBackPressed();
	}
	@OnClick(R.id.bind_btn)
	public void okOnClick(View view){
		String name = _nameEt.getText().toString();
		String cft = _cftEt.getText().toString();
		
		if(MLToolUtil.isNull(name)){
			showMessage("请填写姓名!");
			return;
		}
		
		if(MLToolUtil.isNull(cft)){
			showMessage("请填写财付通账号!");
			return;
		}
		
		MLLogin user = BaseApplication.getInstance().get_user();
		
		ZMRequestParams params = new ZMRequestParams();
	    params.addParameter("isCompany",isCompany);
	    params.addParameter("userId",user.Id);
	    params.addParameter("alipay",cft);
	    params.addParameter("realName",name);
	      ZMHttpRequestMessage message = new ZMHttpRequestMessage(RequestType.MY_BIND, null, params, _handler, HTTP_RESPONSE_BIND, MLMyServices.getInstance());
	      loadDataWithMessage(_context, null, message);
		
	}
	
	 private static final int HTTP_RESPONSE_BIND= 2;
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
	          //      showMessage(error.errorMessage);
	                return;
	            }
	            switch (msg.what) {
	            case HTTP_RESPONSE_BIND:{
	            	MLRegister ret = (MLRegister) msg.obj;
			   		if(ret.state.equalsIgnoreCase("1")){
			   				showMessageSuccess("绑定成功!");
			   				((MLAuxiliaryActivity)_context).onBackPressed();
			   		   }else{
			   			   showMessageError("绑定失败!");
			   		   }
	            	break;
	            }
	                default:
	                    break;
	            }
	        }
	    };
	
	private IEvent<Object> _event;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_event = (IEvent<Object>) activity;
	}
}
