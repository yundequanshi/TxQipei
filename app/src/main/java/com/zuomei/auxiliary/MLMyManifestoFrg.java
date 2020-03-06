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
 * 企业宣言
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyManifestoFrg extends BaseFragment{

	public static MLMyManifestoFrg INSTANCE =null;
	
	private static String content;
	public static MLMyManifestoFrg instance(Object obj){
		content = (String) obj;
	//	if(INSTANCE==null){
			INSTANCE = new MLMyManifestoFrg();
	//	}
		return INSTANCE;
	}
	
	
	
	@ViewInject(R.id.et_content)
	private EditText _contentEt;
	
	private Context _context;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_manifesto, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		
		initView();
		return view;
	}
	
	


	private void initView() {
		_contentEt.setText(content);
	}
	
	@OnClick(R.id.btn_submit)
	public void okOnClick(View view){
		

		if(MLToolUtil.isNull(_contentEt.getText().toString())){
			showMessageWarning("内容不能为空!");
			return;
		}
		
		MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
		ZMRequestParams params = new ZMRequestParams();
			params.addParameter("companyId",user.Id);
			params.addParameter("content",_contentEt.getText().toString()+"");
		
	    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.MY_MANIFESTO, null, params, _handler,HTTP_RESPONSE_INFO, MLMyServices.getInstance());
	    loadData(_context, message2);
	}
	
	
	@OnClick(R.id.top_back)
	public void backOnClick(View view){
		((MLAuxiliaryActivity)_context).onBackPressed();
	}
	

		 private static final int HTTP_RESPONSE_INFO = 0;
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
		            case  HTTP_RESPONSE_INFO:{
		            		MLRegister ret = (MLRegister) msg.obj;
		            		if(ret.datas){
		            			showMessageSuccess("修改成功!");
		            			((MLAuxiliaryActivity)_context).onBackPressed();
		                	}else{
		                		showMessageError("修改失败!");
		                	}
		            	break;
		            }
	                default:
	                    break;
	            }
	        }
	    };
		
		
		
	@Override
			public void onPause() {
				// TODO Auto-generated method stub
				super.onPause();
			}




	private IEvent<Object> _event;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_event = (IEvent<Object>) activity;
	}
}
