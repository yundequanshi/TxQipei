package com.zuomei;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

import com.txsh.R;
import com.zuomei.base.BaseActivity;
import com.zuomei.http.ZMHttpError;

public class HomeActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);           //设置标题栏样式
	/*	setContentView(R.layout.home_main);
		
		  ZMRequestParams params = new ZMRequestParams();
		  params.addParameter("ecode", "zhengfuban");
		  params.addParameter("un", "王军");
		  params.addParameter("pw", "000000");
		
	        ZMHttpRequestMessage message = new ZMHttpRequestMessage(RequestType.LOGIN_PHONE, 	null, params, _handler, HTTP_RESPONSE_MENU, ZMLoginServices.getInstance());
	        loadDataWithMessage(null, message);*/
	        
	        
	}
	
	
	private static final int HTTP_RESPONSE_MENU =0;
	private Handler _handler  = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			dismissProgressDialog();
			if (msg == null || msg.obj == null) {
				showMessage(R
						.string.loading_data_failed);
				return;
			}
			if (msg.obj instanceof ZMHttpError) {
				ZMHttpError error = (ZMHttpError) msg.obj;
				showMessage(error.errorMessage);
				return;
			}
			switch (msg.what) {
			case HTTP_RESPONSE_MENU:
				break;
			default:
				break;
			}
		}
	};
}
