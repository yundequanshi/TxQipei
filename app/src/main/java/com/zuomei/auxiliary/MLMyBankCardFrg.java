package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
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
import com.zuomei.model.MLMyBankResponse;
import com.zuomei.model.MLMyBnakData;
import com.zuomei.model.MLRegister;
import com.zuomei.model.MLSpecialResonse;
import com.zuomei.services.MLMyServices;
import com.zuomei.services.MLPayServices;
import com.zuomei.utils.MLToolUtil;

import org.json.JSONException;
import org.json.JSONObject;

import cn.ml.base.utils.IEvent;

/**
 * 我的银行卡
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyBankCardFrg extends BaseFragment{

	public static MLMyBankCardFrg INSTANCE =null;
	
	public static MLMyBankCardFrg instance(){
	//	if(INSTANCE==null){
			INSTANCE = new MLMyBankCardFrg();
	//	}
		return INSTANCE;
	}
	
	
	@ViewInject(R.id.tv_bank_name)
	private TextView _bankNameTv;
	
	@ViewInject(R.id.et_fh_name)
	private EditText _fhNameEt;
	
	@ViewInject(R.id.et_card_number)
	private EditText _cardEt;
	
	@ViewInject(R.id.et_user_name)
	private EditText _userNameEt;
	
	
	@ViewInject(R.id.et_code)
	private EditText _codeEt;
	@ViewInject(R.id.btn_time)
	private Button _timeBtn;
	
	private Context _context;
	private TimeCount time;
	private String mPhone;
	private String mCode;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_bank, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		
		
		initView();
		initData();
		return view;
	}
	
	
	private void initData() {
		MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
		  ZMRequestParams params = new ZMRequestParams();
		  
		  RequestType httpType = null;
		  
			if(user.isDepot){
				params.addParameter("depotId",user.Id);
				httpType = RequestType.MY_BANK_D;
			}else{
				params.addParameter("companyId",user.Id);
				httpType = RequestType.MY_BANK;
			}
			
		
		
	    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(httpType, null, params, _handler,HTTP_RESPONSE_BANK, MLMyServices.getInstance());
	       loadData(_context, message2);
	}


	private void initView() {
		mPhone = BaseApplication.aCache.getAsString(MLConstants.PARAM_REGISTER_USERNAME);
		
		time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
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
	
	@OnClick(R.id.btn_submit)
	public void okOnClick(View view){

		if(MLToolUtil.isNull(_bankNameTv.getText().toString())){
			showMessageWarning("银行名称不能为空!");
			return;
		}
		
		if(MLToolUtil.isNull(_cardEt.getText().toString())){
			showMessageWarning("卡号不能为空!");
			return;
		}
		
		if(MLToolUtil.isNull(_userNameEt.getText().toString())){
			showMessageWarning("姓名不能为空!");
			return;
		}
		
		String code = _codeEt.getText().toString();
		
		if(MLToolUtil.isNull(code)){
			showMessageError("验证码不能为空!");
			return;
		}
		
		if(!code.equalsIgnoreCase(mCode)){
			showMessageError("验证码不正确!");
			return;
		}
		
		
		MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
		ZMRequestParams params = new ZMRequestParams();
		  RequestType httpType = null;
		if(user.isDepot){
			params.addParameter("depotId",user.Id);
			httpType = RequestType.MY_BANK_UPDATE_D;
		}else{
			params.addParameter("companyId",user.Id);
			httpType = RequestType.MY_BANK_UPDATE;
		}
		
		params.addParameter("openBank",_bankNameTv.getText().toString()+"");
		params.addParameter("subsidiaryBank",_fhNameEt.getText().toString()+"");
		params.addParameter("card",_cardEt.getText().toString()+"");
		params.addParameter("userName",_userNameEt.getText().toString()+"");
		
	    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(httpType, null, params, _handler,HTTP_RESPONSE_BANK_UPDATE, MLMyServices.getInstance());
	    loadDataWithMessage(_context, "操作中,请稍后...",message2);
	}
	
	
	
	
	@OnClick(R.id.tv_bank_name)
	public void NameOnClick(View view){
		Builder builder = new Builder(_context, AlertDialog.THEME_HOLO_LIGHT);
	//	final String s[] = {"工商银行","农业银行","招商银行","建设银行","中国银行","浦发银行","民生银行","平安银行","光大银行","兴业银行","中信银行","交通银行","上海银行","上海农商","南粤银行","宁波银行","华润银行","华夏银行","北京银行","江苏银行","南京银行"};
		final String s[] ={"工商银行","农业银行","建设银行","中国银行","招商银行","交通银行","中信银行","浦发银行","深发展银行","民生银行"};
		builder.setItems(s, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				_bankNameTv.setText(s[which]);
			}
		});
		builder.setTitle("请选择银行");
		 builder.show();

	}
	
	@OnClick(R.id.top_back)
	public void backOnClick(View view){
		((MLAuxiliaryActivity)_context).onBackPressed();
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

		 private static final int HTTP_RESPONSE_BANK = 0;
		 private static final int HTTP_RESPONSE_BANK_UPDATE = 1;
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
		            case  HTTP_RESPONSE_BANK:{
		            		MLMyBankResponse ret = (MLMyBankResponse) msg.obj;
		            		if(ret.state.equalsIgnoreCase("1")){
		            			if(ret.datas.size()>0){
		            				review(ret.datas.get(0));
		            			}
		            			
		                	}else{
		                		showMessage("获取银行卡信息失败");
		                	}
		            	break;
		            }
		            case  HTTP_RESPONSE_BANK_UPDATE:{
	            		MLRegister ret = (MLRegister) msg.obj;
	            		if(ret.datas){
	            			showMessageSuccess("操作成功!");
	            			((MLAuxiliaryActivity)_context).onBackPressed();
	                	}else{
	                		showMessageError("操作失败!");
	                	}
	            	break;
	            }	          
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
		
		
		
	@Override
			public void onPause() {
				// TODO Auto-generated method stub
				super.onPause();
			}


	protected void review(MLMyBnakData mlMyBnakData) {
		_bankNameTv.setText(mlMyBnakData.openBank);
		_fhNameEt.setText(mlMyBnakData.subsidiaryBank);
		_cardEt.setText(mlMyBnakData.card);
		_userNameEt.setText(mlMyBnakData.userName);
	}


	private IEvent<Object> _event;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_event = (IEvent<Object>) activity;
	}
}
