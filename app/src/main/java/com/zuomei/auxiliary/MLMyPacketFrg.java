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
import com.zuomei.model.MLMyPacketData;
import com.zuomei.model.MLMyPacketResponse;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLMyServices;
import com.zuomei.utils.MLToolUtil;

import cn.ml.base.utils.IEvent;

/**
 *红包管理
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyPacketFrg extends BaseFragment{

	public static MLMyPacketFrg INSTANCE =null;
	
	public static MLMyPacketFrg instance(){
	//	if(INSTANCE==null){
			INSTANCE = new MLMyPacketFrg();
	//	}
		return INSTANCE;
	}
	
	
	@ViewInject(R.id.et_count)
	private EditText _countEt;
	
	@ViewInject(R.id.et_price)
	private EditText _priceEt;
	
	@ViewInject(R.id.et_quota)
	private EditText _quotaEt;
	
	
	private Context _context;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_packet, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		
		initView();
		initData();
		return view;
	}
	
	
	private void initData() {
		MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
		  ZMRequestParams params = new ZMRequestParams();
		  
		  
			if(user.isDepot){
				params.addParameter("depotId",user.Id);
			}else{
				params.addParameter("companyId",user.Id);
			}
			
		
		
	    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.MY_PACKET_INFO, null, params, _handler,HTTP_RESPONSE_MYPACK, MLMyServices.getInstance());
	       loadData(_context, message2);
	}


	private void initView() {
		
	}
	
	@OnClick(R.id.btn_submit)
	public void okOnClick(View view){

		if(MLToolUtil.isNull(_countEt.getText().toString())){
			showMessageWarning("红包个数不能为空!");
			return;
		}
		
		if(MLToolUtil.isNull(_priceEt.getText().toString())){
			showMessageWarning("红包金额不能为空!");
			return;
		}
		
		if(MLToolUtil.isNull(_quotaEt.getText().toString())){
			showMessageWarning("红包限额不能为空!");
			return;
		}
		
		int xiane = Integer.parseInt(_quotaEt.getText().toString());
		int jine = Integer.parseInt(_priceEt.getText().toString());
		
		//限额必须大于金额
		if(xiane<jine){
			showMessage("红包限额必须大于红包金额!");
			return;
		}
		
		MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
		ZMRequestParams params = new ZMRequestParams();
		  RequestType httpType = null;
			params.addParameter("companyId",user.Id);
		params.addParameter("redEnvelopeNum",_countEt.getText().toString()+"");
		params.addParameter("redEnvelopeMoney",_priceEt.getText().toString()+"");
		params.addParameter("tradingLimit",_quotaEt.getText().toString()+"");
		
	    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.MY_PACKET_UPDATE, null, params, _handler,HTTP_RESPONSE_MYPACK_UPDATE, MLMyServices.getInstance());
	    loadData(_context, message2);
	}
	
	
	@OnClick(R.id.top_back)
	public void backOnClick(View view){
		((MLAuxiliaryActivity)_context).finish();
	}
	

		 private static final int HTTP_RESPONSE_MYPACK = 0;
		 private static final int HTTP_RESPONSE_MYPACK_UPDATE = 1;
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
		            case  HTTP_RESPONSE_MYPACK:{
		            	MLMyPacketResponse ret = (MLMyPacketResponse) msg.obj;
		            		if(ret.state.equalsIgnoreCase("1")){
		            			review(ret.datas);
		                	}else{
		                		showMessage("获取红包信息失败");
		                	}
		            	break;
		            }
		            case  HTTP_RESPONSE_MYPACK_UPDATE:{
	            		MLRegister ret = (MLRegister) msg.obj;
	            		if(ret.datas){
	            			showMessageSuccess("修改成功!");
	            			((MLAuxiliaryActivity)_context).finish();
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


	protected void review(MLMyPacketData data) {
		_countEt.setText(data.redEnvelopeNum);
		_priceEt.setText(data.redEnvelopeMoney);
		_quotaEt.setText(data.tradingLimit);
	}


	private IEvent<Object> _event;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_event = (IEvent<Object>) activity;
	}
}
