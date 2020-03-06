package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.zuomei.model.MLMyRepairDetail;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLMyServices;
import com.zuomei.utils.MLToolUtil;

import java.util.ArrayList;
import java.util.List;

import cn.ml.base.utils.IEvent;

/**
 * 添加汽修信息
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyRepairAddFrg extends BaseFragment{

	public static MLMyRepairAddFrg INSTANCE =null;
	
	public static MLMyRepairAddFrg instance(){
	//	if(INSTANCE==null){
			INSTANCE = new MLMyRepairAddFrg();
	//	}
		return INSTANCE;
	}
	
/*	//价格
	@ViewInject(R.id.add_et_price)
	private EditText _priceEt;
	
	//质量选择
	@ViewInject(R.id.add_btn_quality)
	private Button _qualityBtn;
	*/

	@ViewInject(R.id.add_et_number)
	private EditText _numberEt;
	
	@ViewInject(R.id.add_et_phone)
	private EditText _phoneEt;
	
	@ViewInject(R.id.add_et_price)
	private EditText _priceEt;
	
	@ViewInject(R.id.add_lv_record)
	private ListView _recordLv;
	
	@ViewInject(R.id.stock_rl_root)
	private RelativeLayout _root;
	private MLMyRepairAddPop _addPop;
	private MLMyRecordAdapter _adapter;
	private Context _context;
	private List<MLMyRepairDetail> _lists;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_repair_add, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		
		initView();
		return view;
	}
	
	
	private void initView() {
		_lists = new ArrayList<MLMyRepairDetail>();
		_adapter = new MLMyRecordAdapter(_context);
		_recordLv.setAdapter(_adapter);
	}
	
	
	
	@Override
	public void onPause() {
		super.onPause();
		_numberEt.setText("");
		_phoneEt.setText("");
		_priceEt.setText("");
	}


	/**
	  * @description  添加详细信息
	  *
	  * @author marcello
	 */
	@OnClick(R.id.add_btn)
	public void addOnClick(View view){
		_addPop = new MLMyRepairAddPop(_context, new IEvent<String>() {
			@Override
			public void onEvent(Object source, String eventArg) {
				if(source==null) return;
				_lists.add((MLMyRepairDetail)source);
				_adapter.setData(_lists);
			}
		});  
		_addPop.showAtLocation(_root, Gravity.CENTER, 0, 0); 
	}
	/**
	  * @description  提交
	  *
	  * @author marcello
	 */
	@OnClick(R.id.stock_btn_add)
	public void okOnClick(View view){
		
		
		String number = _numberEt.getText().toString();
		String phone = _phoneEt.getText().toString();
		String price = _priceEt.getText().toString();
		
		if(MLToolUtil.isNull(number)) {
			showMessage("车牌号不能为空!");
			return ;
		}
		if(MLToolUtil.isNull(phone)) {
			showMessage("电话不能为空!");
			return ;
		}
		if(MLToolUtil.isNull(price)) {
			showMessage("工时费不能为空!");
			return ;
		}
		
		MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
		ZMRequestParams catalogParam = new ZMRequestParams();
		catalogParam.addParameter(MLConstants.PARAM_MY_DEPTID1,user.Id);
		catalogParam.addParameter(MLConstants.PARAM_MY_COST,price);
		catalogParam.addParameter(MLConstants.PARAM_MY_CARNUM,number);
		catalogParam.addParameter(MLConstants.PARAM_MY_PHONE,phone);
		
		for(MLMyRepairDetail data : _lists){
			catalogParam.addParameter(MLConstants.PARAM_MY_PART,data.part);
			catalogParam.addParameter(MLConstants.PARAM_MY_KEEPTIME,data.keepTime);
			catalogParam.addParameter(MLConstants.PARAM_MY_PRICE,data.price);
		}
		

	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_REPAIR_ADD, null, catalogParam, _handler, HTTP_RESPONSE_REPAIR_ADD, MLMyServices.getInstance());
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
	
/*	private MLMyStockQualityPop _menuWindow;
	@OnClick(R.id.add_btn_quality)
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

	
	
	 private static final int HTTP_RESPONSE_REPAIR_ADD= 0;
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
	            case HTTP_RESPONSE_REPAIR_ADD:{
	            	MLRegister ret = (MLRegister) msg.obj;
         		if(ret.state.equalsIgnoreCase("1")){
         			showMessageSuccess("添加进货成功!");
         			_event.onEvent(null, MLConstants.MY_REPAIR);
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
