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
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
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
import com.zuomei.model.MLMyRebateData;
import com.zuomei.model.MLMyRebateResponse;
import com.zuomei.services.MLMyServices;

import java.util.List;

import cn.ml.base.utils.IEvent;

/**
 * 汽修厂-返利列表
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyFanliFrg extends BaseFragment{

	public static MLMyFanliFrg INSTANCE =null;
	
	public static MLMyFanliFrg instance(){
	//	if(INSTANCE==null){
			INSTANCE = new MLMyFanliFrg();
	//	}
		return INSTANCE;
	}
	@ViewInject(R.id.message_lv)
	private AbPullToRefreshView _pullToRefreshLv;
	@ViewInject(R.id.bill_lv)
	private ListView _billLv;

	private Context _context;
	private MLMyFanliAdapter _adapter;
	public List<MLMyRebateData> _datas;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_fanli, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		
		initView();
		initData();
		return view;
	}
	
	
	private void initView() {
		_adapter = new MLMyFanliAdapter(_context);
		_billLv.setAdapter(_adapter);
		
		_pullToRefreshLv.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			
			@Override
			public void onHeaderRefresh(AbPullToRefreshView view) {
				initData();
			}
		});;
		_pullToRefreshLv.setOnFooterLoadListener(new OnFooterLoadListener() {
			
			@Override
			public void onFooterLoad(AbPullToRefreshView view) {
				pageData();
			}
		});  
	}

	private void initData(){
		ZMRequestParams params = new ZMRequestParams();
//		MLLogin user = BaseApplication._user;
		MLLogin user = 	((BaseApplication)getActivity().getApplication()).get_user();
		if(user.isDepot){
			params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,user.Id);
		}else{
			params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,user.Id);
		}
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_REBATE_LIST, null, params, _handler, HTTP_RESPONSE_STOCK_LIST,  MLMyServices.getInstance());
	    loadDataWithMessage(_context, null, message1);
	}
	
	private void pageData() {
		String lastId = _datas.get(_datas.size()-1).userId+"";
		ZMRequestParams params = new ZMRequestParams();
//		MLLogin user = BaseApplication._user;
		MLLogin user = 	((BaseApplication)getActivity().getApplication()).get_user();
		if(user.isDepot){
			params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,user.Id);
		}else{
			params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,user.Id);
		}
		params.addParameter(MLConstants.PARAM_MESSAGE_LASTID,lastId);
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_REBATE_LIST, null, params, _handler, HTTP_RESPONSE_STOCK_PAGE,  MLMyServices.getInstance());
	    loadDataWithMessage(_context, null, message1);
	}
	
	private void reviewDealList(List<MLMyRebateData>  data){
		_adapter.setData(data);
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
	
	 private static final int HTTP_RESPONSE_STOCK_LIST= 0;
	 private static final int HTTP_RESPONSE_STOCK_PAGE= 1;
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
	            	MLMyRebateResponse ret = (MLMyRebateResponse) msg.obj;
        		if(ret.state.equalsIgnoreCase("1")){
        			_datas = ret.datas;
        			_adapter.setData(ret.datas);
            	}else{
            	}
        		_pullToRefreshLv.onHeaderRefreshFinish();
	            	break;
	            }
	            
	            case HTTP_RESPONSE_STOCK_PAGE:{
	            	MLMyRebateResponse ret = (MLMyRebateResponse) msg.obj;
	            	if(ret.state.equalsIgnoreCase("1")){
	            		if(ret.datas.size()==0){
	            			_pullToRefreshLv.onFooterLoadFinish();
	            			break;  
	            		}
	            		_datas.addAll(ret.datas);
	          			reviewDealList(_datas);
	              	}else{
	              		showMessageError("获取返利列表失败!");
	              	}
	          		_pullToRefreshLv.onFooterLoadFinish();
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
