package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

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
import com.zuomei.model.MLMyStockDetail;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLMyServices;
import com.zuomei.utils.MLToolUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.ml.base.utils.IEvent;

/**
 * 添加进货信息
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyStockAddFrg extends BaseFragment{

	public static MLMyStockAddFrg INSTANCE =null;
	
	public static MLMyStockAddFrg instance(){
	//	if(INSTANCE==null){
			INSTANCE = new MLMyStockAddFrg();
	//	}
		return INSTANCE;
	}
	
	@ViewInject(R.id.add_et_name)
	private EditText _nameEt;
	@ViewInject(R.id.add_et_phone)
	private EditText _phoneEt;
	
	@ViewInject(R.id.add_lv_record)
	private ListView _recordLv;
	private MLMyStockAddAdapter _adapter;
	private List<MLMyStockDetail> _lists;
	
	@ViewInject(R.id.stock_rl_root)
	private RelativeLayout _root;
	@ViewInject(R.id.add_et_time)
	private Button add_et_time;
	private Context _context;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_stock_add, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		
		initView();
		return view;
	}
	
	
	private void initView() {
		_lists = new ArrayList<MLMyStockDetail>();
		_adapter = new MLMyStockAddAdapter(_context);
		_recordLv.setAdapter(_adapter);
	}
	
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		_nameEt.setText("");
		_phoneEt.setText("");
	}
	@OnClick(R.id.add_et_time)
	public void timeOnClick(View view) {
		Calendar c = Calendar.getInstance();
		new DatePickerDialog(_context, DatePickerDialog.THEME_HOLO_LIGHT,
				new DatePickerDialog.OnDateSetListener() {
					public void onDateSet(DatePicker dp, int year, int month,
							int dayOfMonth) {

						String _month = "";
						String _dayOfMonth = "";

						if ((month + 1) < 10) {
							_month = "0" + (month + 1);
						} else {
							_month = String.valueOf(month + 1);
						}

						if (dayOfMonth < 10) {
							_dayOfMonth = "0" + dayOfMonth;
						} else {
							_dayOfMonth = dayOfMonth + "";
						}

						String text = String.format("%s-%s-%s", year, _month,
								_dayOfMonth);

						add_et_time.setText(text);
					}
				}, c.get(Calendar.YEAR), // 传入年份
				c.get(Calendar.MONTH), // 传入月份
				c.get(Calendar.DAY_OF_MONTH) // 传入天数
		).show();
	}

	/**
	  * @description  提交
	  *
	  * @author marcello
	 */
	@OnClick(R.id.stock_btn_add)
	public void okOnClick(View view){
		String name = _nameEt.getText().toString();
		String phone = _phoneEt.getText().toString();
		String time = add_et_time.getText().toString();
		
		if(MLToolUtil.isNull(name)) {
			showMessage("商户名不能为空!");
			return;
		}
		if(MLToolUtil.isNull(phone)){
			 showMessage("商户电话不能为空!");
			 return;
		}
		if(MLToolUtil.isNull(time)){
			showMessage("进货时间不能为空!");
			return;
		}
		
		
		MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
		ZMRequestParams catalogParam = new ZMRequestParams();
		catalogParam.addParameter(MLConstants.PARAM_MY_DEPTID,user.Id);
		catalogParam.addParameter(MLConstants.PARAM_MY_COMPANYNAME,name);
		catalogParam.addParameter(MLConstants.PARAM_HOME_COMPANYPHONE,phone);
		catalogParam.addParameter("sendTime",time);
		
		for(MLMyStockDetail data : _lists){
			catalogParam.addParameter(MLConstants.PARAM_MY_QYALITY,"正品");
			catalogParam.addParameter(MLConstants.PARAM_MY_PRICE,data.price);
			catalogParam.addParameter(MLConstants.PARAM_MY_GOODNAME,data.goodName);
			catalogParam.addParameter(MLConstants.PARAM_MY_GOODNUM,data.goodNum);
		}
		
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_STOCK_ADD, null, catalogParam, _handler, HTTP_RESPONSE_STOCK_ADD, MLMyServices.getInstance());
	    loadDataWithMessage(_context, "正在填加...", message1);
		
	}
	
	/**
	  * @description  返回
	  *
	  * @author marcello
	 */
	@OnClick(R.id.home_top_back)
	public void backOnClick(View view){
		getActivity().onBackPressed();
	}
	
	private MLMyStockAddPop _addPop;
	@OnClick(R.id.add_btn)
	public void addOnClick(View view){
		_addPop = new MLMyStockAddPop(_context, new IEvent<String>() {
			@Override
			public void onEvent(Object source, String eventArg) {
				if(source==null) return;
				_lists.add((MLMyStockDetail)source);
				_adapter.setData(_lists);
			}
		});  
		_addPop.showAtLocation(_root, Gravity.CENTER, 0, 0); 
	}
	
	private MLMyStockQualityPop _menuWindow;
	/*@OnClick(R.id.add_btn_quality)
	public void qualityOnClick(View view ){
	
		_menuWindow = new MLMyStockQualityPop(_context, new IEvent<String>() {
			@Override
			public void onEvent(Object source, String eventArg) {
				_qualityBtn.setText(eventArg);
				_menuWindow.dismiss();
			}
		});  
		_menuWindow.showAtLocation(_root, Gravity.CENTER, 0, 0); 
	}*/

	
	
	 private static final int HTTP_RESPONSE_STOCK_ADD= 0;
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
	            case HTTP_RESPONSE_STOCK_ADD:{
	            	MLRegister ret = (MLRegister) msg.obj;
         		if(ret.state.equalsIgnoreCase("1")){
         			showMessageSuccess("添加进货成功!");
         			_event.onEvent(null, MLConstants.MY_STOCK);
             	}else{
             		showMessage("添加进货失败!");
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
