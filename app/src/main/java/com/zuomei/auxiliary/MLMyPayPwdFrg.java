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
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLRegister;
import com.zuomei.model.MLSpecialResonse;
import com.zuomei.services.MLPayServices;
import com.zuomei.utils.MLToolUtil;

import org.json.JSONException;
import org.json.JSONObject;

import cn.ml.base.utils.IEvent;

/**
 * 登录-忘记密码 step2
 * 
 * @author Marcello
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyPayPwdFrg extends BaseFragment {

	public static MLMyPayPwdFrg INSTANCE = null;

	private String mPhone;
	public static MLMyPayPwdFrg instance(Object obj) {
			INSTANCE = new MLMyPayPwdFrg();
		return INSTANCE;
	}

	private TimeCount time;
	@ViewInject(R.id.et_code)
	private EditText _nameEt;
	@ViewInject(R.id.btn_time)
	private Button _timeBtn;

	private Context _context;
	@ViewInject(R.id.tv_label)
	private TextView _labelTv;
	
	private String mCode;
	
	@ViewInject(R.id.et_pwd1)
	private EditText _pwd1Et;
	@ViewInject(R.id.et_pwd2)
	private EditText _pwd2Et;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_pay_pwd, null);
		ViewUtils.inject(this, view);
		_context = inflater.getContext();

		mPhone = BaseApplication.aCache.getAsString(MLConstants.PARAM_REGISTER_USERNAME);
		
		String text = String.format("请输入<font color=\"#ff0000\">%s</font>收到的校验码", mPhone);
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
		//((MLAuxiliaryActivity) _context).finish();
		((MLAuxiliaryActivity) _context).onBackPressed();
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
		  JSONObject jo = new JSONObject();
		  try {
			jo.put("mobile", mPhone);
		} catch (JSONException e) {
			return;
		}
		  param.addParameter("data",jo.toString());
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_PAY_GETCODE, null, param, _handler, HTTP_RESPONSE_CODE, MLPayServices.getInstance());
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
		if(MLToolUtil.isNull(code)){
			showMessageError("验证码不能为空!");
			return;
		}
		
		if(!code.equalsIgnoreCase(mCode)){
			showMessageError("验证码不正确!");
			return;
		}
		
		String pwd1 = _pwd1Et.getText().toString();
		String pwd2 = _pwd2Et.getText().toString();
		if(MLToolUtil.isNull(pwd1)||pwd1.length()!=6){
			showMessageWarning("请输入6位的钱包密码");
			return;
		}
		
		if(MLToolUtil.isNull(pwd2)||pwd2.length()!=6){
			showMessageWarning("请输入6位的钱包密码");
			return;
		}
		
		if(!pwd1.equalsIgnoreCase(pwd2)){
			showMessageWarning("两次密码输入不一致");
			return;
		}
		pwd1 = MLToolUtil.MD5(pwd1);
		MLLogin user = BaseApplication.getInstance().get_user();
		ZMRequestParams param = new ZMRequestParams();
		
		  JSONObject jo = new JSONObject();
		  try {
			if(user.isDepot){
				jo.put("depotUserId", user.Id);
			}else{
				jo.put("companyId", user.Id);
			}
			jo.put("pwd", pwd1);
			  param.addParameter("data",jo.toString());
		} catch (JSONException e) {
			return;
		}
		  param.addParameter("data",jo.toString());
		
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_PAY_SETPWD, null, param, _handler, HTTP_RESPONSE_SETPWD, MLPayServices.getInstance());
	    loadDataWithMessage(_context, null, message1);
		
		
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
	 private static final int HTTP_RESPONSE_SETPWD= 3;
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
	            case  HTTP_RESPONSE_SETPWD:{
	            	MLRegister ret = (MLRegister) msg.obj;
            		if(ret.state.equalsIgnoreCase("1")&&ret.datas){
            			showMessageSuccess("钱包密码设置成功");
            			((MLAuxiliaryActivity) _context).onBackPressed();
                	}else{
                		showMessage("钱包密码设置失败!");
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
