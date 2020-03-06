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
import android.widget.ListView;

import com.ab.view.pullview.AbPullToRefreshView;
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
import com.zuomei.model.MLDealListResponse;
import com.zuomei.model.MLLogin;
import com.zuomei.services.MLMyServices;

import cn.ml.base.utils.IEvent;

/**
 * 账单-汽配城
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyBillBFrg extends BaseFragment{

	public static MLMyBillBFrg INSTANCE =null;
	
	public static MLMyBillBFrg instance(){
		if(INSTANCE==null){
			INSTANCE = new MLMyBillBFrg();
		}
		return INSTANCE;
	}
	@ViewInject(R.id.message_lv)
	private AbPullToRefreshView _pullToRefreshLv;
	@ViewInject(R.id.bill_lv)
	private ListView _billLv;

	private Context _context;
	private MLMyBillDAdapter _adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_stock, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		
		initView();
		return view;
	}
	
	
	private void initView() {
		_adapter = new MLMyBillDAdapter(_context);
		_billLv.setAdapter(_adapter);
		
		ZMRequestParams params = new ZMRequestParams();
//		MLLogin user = BaseApplication._user;
		MLLogin user = 	((BaseApplication)getActivity().getApplication()).get_user();
		if(user.isDepot){
			params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,user.Id);
		}else{
			params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,user.Id);
		}
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_DEAL_LIST, null, params, _handler, HTTP_RESPONSE_STOCK_LIST,  MLMyServices.getInstance());
	    loadDataWithMessage(_context, null, message1);
/*	    
	    _billLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MLDealListData data = (MLDealListData) parent.getAdapter().getItem(position);
				if(data.dealType.equalsIgnoreCase("1")){
					showMessage(data.dealType);
				}
				
			}
		});*/
	}

	
	/**
	  * @description  增加进货信息
	  *
	  * @author marcello
	 */
	@OnClick(R.id.stock_ib_add)
	public void addOnClick(View view){
		_event.onEvent(null, MLConstants.MY_STOCK_ADD);
	}
	
	/**
	  * @description  返回
	  *
	  * @author marcello
	 */
	@OnClick(R.id.home_top_back)
	public void backOnClick(View view){
		((MLAuxiliaryActivity)_context).finish();
	}
	
	 private static final int HTTP_RESPONSE_STOCK_LIST= 0;
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
	            case HTTP_RESPONSE_STOCK_LIST:{
	            	MLDealListResponse ret = (MLDealListResponse) msg.obj;
         		if(ret.state.equalsIgnoreCase("1")){
         			_adapter.setData(ret.datas);
             	}else{
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
