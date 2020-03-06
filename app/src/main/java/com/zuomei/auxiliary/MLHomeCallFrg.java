package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.yzx.api.CallType;
import com.yzx.api.UCSCall;
import com.yzx.api.UCSService;
import com.yzx.listenerInterface.CallStateListener;
import com.yzx.listenerInterface.UcsReason;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.APIConstants;
import com.zuomei.model.MLHomeBusiness1Data;
import com.zuomei.model.MLHomeCatalogData;

import cn.ml.base.utils.IEvent;

/**
 * 拨打电话页面
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLHomeCallFrg extends BaseFragment{

	public static MLHomeCallFrg INSTANCE =null;
	
	public static MLHomeCatalogData _catalog;
	
	public static MLHomeBusiness1Data _business;
	public static MLHomeCallFrg instance(Object obj){
		_business= (MLHomeBusiness1Data) obj;
	//	if(INSTANCE==null){
			INSTANCE = new MLHomeCallFrg();
	//	}
		return INSTANCE;
	}


	@ViewInject(R.id.tv_state)
	private TextView _stateTv;
	
	@ViewInject(R.id.tv_name)
	private TextView _nameTv;
	
	@ViewInject(R.id.tv_phone)
	private TextView _phoneTv;
	
	@ViewInject(R.id.tv_address)
	private TextView _addressTv;
	
	@ViewInject(R.id.btn_speaker)
	private ImageButton _speakerBtn;
	
	@ViewInject(R.id.iv_head)
	private ImageView _headIv;
	
	@ViewInject(R.id.tv_time)
	private Chronometer _timeTv;
	
	private Context _context;
	
	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.home_call, null);
		ViewUtils.inject(this,view);
	_context = inflater.getContext();
	initView();
	initPhone();
//	showMessage("状态:"+MLToolUtil.getNetworkType());
		return view;
	}


	private void initView(){
		_nameTv.setText(_business.userName);
		_phoneTv.setText(_business.phone);
		_addressTv.setText(_business.address);
		
		String iconUrl = APIConstants.API_IMAGE+"?id="+_business.logo;
		
		 bitmapUtils.display(_headIv, iconUrl, bigPicDisplayConfig);		
	}   
	
	private void initPhone() {
		if(UCSService.isConnected()){
	//	showMessage("连接中...");
//		UCSCall.dial(_context, CallType.DIRECT, _business.phone);
		String phone = _business.phone.replace("-", "");
		UCSCall.dial(_context, CallType.CALLBACK, phone);
		//UCSCall.callBack(_context,phone,null,null);
	}else{
		showMessage("连接失败");
	}
	
	
	UCSCall.addCallStateListener(new CallStateListener() {
		
		@Override
		public void onIncomingCall(String arg0, String arg1, String arg2) {
					showMessage("onIncomingCall");
					Log.d("yunzhixun","---------------------------onIncomingCall");
		}
				
		@Override
		public void onHangUp(String arg0, UcsReason arg1) {
			//挂断电话
			Log.d("yunzhixun","---------------------------onHangUp");
			((MLAuxiliaryActivity)_context).finish();
		}
				
		@Override
		public void onDialFailed(String arg0, UcsReason arg1) {
			handler.sendEmptyMessage(DIAL_FAIL);
			Log.d("yunzhixun","---------------------------onDialFailed"+arg0);
		}
				
		@Override
		public void onCallBackSuccess() {
			Log.d("yunzhixun","---------------------------onCallBackSuccess");
		}
				
		@Override
		public void onAnswer(String arg0) {
			//已接听
			handler.sendEmptyMessage(DIAL_ANSWER);
			Log.d("yunzhixun","---------------------------onAnswer");
		}
				
		@Override
		public void onAlerting(String arg0) {
			//正在等待
			Log.d("yunzhixun","---------------------------onAlerting");
			}
	});
	}
	
	
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		((MLAuxiliaryActivity)_context).finish();
	}


	private  final int DIAL_FAIL = 0;
	private  final int DIAL_ANSWER = 1;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch (msg.what) {
			case DIAL_FAIL:{
				_stateTv.setText("通话失败!");
				showMessageError("通话失败!");
				break;
			}
			case DIAL_ANSWER:{
				_timeTv.setVisibility(View.VISIBLE);
				_timeTv.setBase(SystemClock.elapsedRealtime());  
				_timeTv.start();
				break;
			}
				

			default:
				break;
			}
			
		
		}
		
	};
	
	@OnClick(R.id.btn_end)
	private void endOnClick(View view){
		_timeTv.stop();
		_stateTv.setText("通话已结束");
		UCSCall.stopRinging();
		
		UCSCall.setSpeakerphone(false);
		UCSCall.hangUp("");
		((MLAuxiliaryActivity)_context).finish();
	}
	private boolean isSpeaker = false;
	@OnClick(R.id.btn_speaker)
	private void speakerOnClick(View view){
		isSpeaker=!isSpeaker;
		if(isSpeaker){
			_speakerBtn.setImageResource(R.drawable.call_btn_speaker_f);
		}else{
			_speakerBtn.setImageResource(R.drawable.call_btn_speaker_n);
		}
		//开启免提
		UCSCall.setSpeakerphone(!UCSCall.isSpeakerphoneOn());
	}
	
	private IEvent<Object> _event;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_event = (IEvent<Object>) activity;
	}
}
