package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.zuomei.model.MLSpecialResonse;
import com.zuomei.services.MLLoginServices;

import cn.ml.base.utils.IEvent;

/**
 * 登录-忘记密码 step2
 * 
 * @author Marcello
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLLoginPwd2Frg extends BaseFragment {

	public static MLLoginPwd2Frg INSTANCE = null;

	private static String phone;
	public static MLLoginPwd2Frg instance(Object obj) {
		phone = (String) obj;
			INSTANCE = new MLLoginPwd2Frg();
		return INSTANCE;
	}

	private TimeCount time;
	@ViewInject(R.id.et_code)
	private EditText _nameEt;
	@ViewInject(R.id.btn_time)
	private Button _timeBtn;
	private String mCode;
	private Context _context;
	@ViewInject(R.id.tv_label)
	private TextView _labelTv;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.login_pwd2, null);
		ViewUtils.inject(this, view);
		_context = inflater.getContext();
		
		String text = String.format("请输入<font color=\"#ff0000\">%s</font>收到的校验码", phone);
		_labelTv.setText(Html.fromHtml(text));   
		time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
		return view;
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		_nameEt.setText("");
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
	 * @description 获取验证码
	 * 
	 * @author marcello
	 */
	@OnClick(R.id.btn_time)
	public void timeOnClick(View view) {
		time.start();
		
		ZMRequestParams param = new ZMRequestParams();
		param.addParameter(MLConstants.PARAM_LOGIN_NUMBER,phone);
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.LOGIN_GETCODE, null, param, _handler, HTTP_RESPONSE_CODE, MLLoginServices.getInstance());
	    loadDataWithMessage(_context, null, message1);
	}

	/**
	 * @description 获取验证码
	 * 
	 * @author marcello
	 */
	@OnClick(R.id.btn_next)
	public void nextOnClick(View view) {
		String code = _nameEt.getText().toString();
		if(code.equalsIgnoreCase(mCode)){
			_event.onEvent(phone, MLConstants.LOGIN_PWD3);
		}else{
			showMessageError("验证码不正确!");
		}
			//	mCode
	}
	/* 定义一个倒计时的内部类 */
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			_timeBtn.setText("重新验证");
			_timeBtn.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			_timeBtn.setClickable(false);
			_timeBtn.setText(millisUntilFinished / 1000 + "秒");
		}
	}
	
	 private static final int HTTP_RESPONSE_CODE = 2;
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
	            case  HTTP_RESPONSE_CODE:{
	            		MLSpecialResonse ret = (MLSpecialResonse) msg.obj;
	            		if(ret.state.equalsIgnoreCase("1")){
	            			mCode = ret.datas;
	                	}else{
	                		//showMessage("获取分类失败!");
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
