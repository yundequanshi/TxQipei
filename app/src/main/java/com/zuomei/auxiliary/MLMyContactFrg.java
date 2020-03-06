package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLMyContactData;
import com.zuomei.model.MLMyContactResponse;
import com.zuomei.model.MLSpecialResonse;
import com.zuomei.services.MLMyServices;

import cn.ml.base.utils.IEvent;

/**
 * 联系我们
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyContactFrg extends BaseFragment{

	public static MLMyContactFrg INSTANCE =null;
	
	public static MLMyContactFrg instance(){
	//	if(INSTANCE==null){
			INSTANCE = new MLMyContactFrg();
	//	}
		return INSTANCE;
	}
	
	
	@ViewInject(R.id.tv_contact)
	private TextView _labelTv;
	
	@ViewInject(R.id.tv_address)
	private TextView _addressTv;
	
	
	@ViewInject(R.id.tv_tody)
	private TextView _todayTv;
	
	@ViewInject(R.id.tv_all)
	private TextView _allTv;
	@ViewInject(R.id.tv_sign)
	private TextView _signTv;
	
	@ViewInject(R.id.btn_call)
	private Button _callBtn;
	
/*	@ViewInject(R.id.iv_ios)
	private ImageView _iosIv;*/
	
	@ViewInject(R.id.iv_android)
	private ImageView _androidIv;
	
	private Context _context;
	private MLMyPhoneDAdapter _adapter;
	
	private MLMyContactData _data;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_contact, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		view.setFocusable(true);//这个和下面的这个命令必须要设置了，才能监听back事件。
		 view.setFocusableInTouchMode(true);
		 view.setOnKeyListener(backlistener);
		initView();
		return view;
	}
	
	
	private void initView() {
		ZMRequestParams catalogParam = new ZMRequestParams();
		catalogParam.addParameter(MLConstants.PARAM_MY_CITYID,BaseApplication._currentCity);
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_CONTACT, null, catalogParam, _handler, HTTP_RESPONSE_CONTACT,  MLMyServices.getInstance());
	    loadDataWithMessage(_context, null, message1);
	    
	    
	    _labelTv.setText("客户端下载 v2.2.1");
	    
	 /*   ZMHttpRequestMessage message3 = new ZMHttpRequestMessage(RequestType.MY_DIAL_TODAY, null, null, _handler, HTTP_RESPONSE_TODY,  MLMyServices.getInstance());
	    loadDataWithMessage(_context, null, message3);
	    
	    ZMHttpRequestMessage message4 = new ZMHttpRequestMessage(RequestType.MY_DIAL_ALL, null, null, _handler, HTTP_RESPONSE_ALL,  MLMyServices.getInstance());
	    loadDataWithMessage(_context, null, message4);*/
	}

	
	
	/**
	  * @description  返回
	  *
	  * @author marcello
	 */
	@OnClick(R.id.top_back)
	public void backOnClick(View view){
		((MLAuxiliaryActivity)_context).finish();
	}
	
	@OnClick(R.id.btn_call)
	public void callOnClick(View view){
		

		Builder builder = new Builder(_context);
		builder.setMessage("拨打"+_data.phone);
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+_data.phone));  
                startActivity(intent); 
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
		
		
	}
	
	
	private void review(MLMyContactData datas) {
		_addressTv.setText(datas.location);
		_callBtn.setText("  客服电话      "+datas.phone);
		
		_todayTv.setText("今日拨打:"+datas.todayTime);
		_allTv.setText("总共拨打:"+datas.allTime);
		
		_signTv.setText("今日签到:"+datas.signCount);
		String androidUrl = APIConstants.API_IMAGE+"?id="+datas.androidImg;
		String iosUrl = APIConstants.API_IMAGE+"?id="+datas.iosImg;
		 bitmapUtils.display(_androidIv, androidUrl, bigPicDisplayConfig);		
	//	 bitmapUtils.display(_iosIv, iosUrl, bigPicDisplayConfig);	
	}
	
	 private static final int HTTP_RESPONSE_CONTACT= 0;
	 private static final int HTTP_RESPONSE_TODY= 1;
	 private static final int HTTP_RESPONSE_ALL= 2;
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
	            case HTTP_RESPONSE_CONTACT:{
	            	MLMyContactResponse ret = (MLMyContactResponse) msg.obj;
         		if(ret.state.equalsIgnoreCase("1")){
         			_data = ret.datas;
         			review(ret.datas);
             	}else{
             		showMessageError("获取信息失败!");
             	}
	            	break;
	            }
	            case HTTP_RESPONSE_TODY:{
	            	MLSpecialResonse ret = (MLSpecialResonse) msg.obj;
	         		if(ret.state.equalsIgnoreCase("1")){
	         			_todayTv.setText("今日拨打:"+ret.datas);
	             	}else{
	             		showMessageError("获取今日拨打次数失败！");
	             	}
	            	break;
	            }
	            case HTTP_RESPONSE_ALL:{
	            	MLSpecialResonse ret = (MLSpecialResonse) msg.obj;
	         		if(ret.state.equalsIgnoreCase("1")){
	         			_allTv.setText("总共拨打:"+ret.datas);
	             	}else{
	             		showMessageError("获取总共拨打次数失败！");
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
	 private View.OnKeyListener backlistener = new View.OnKeyListener() {

		  @Override
		  public boolean onKey(View v, int keyCode, KeyEvent event) {
		   // TODO Auto-generated method stub
		         if(keyCode==KeyEvent.KEYCODE_BACK){
		        		((MLAuxiliaryActivity)_context).finish();
		          return true;
		         }
		   return false;
		  }
		     };
}
