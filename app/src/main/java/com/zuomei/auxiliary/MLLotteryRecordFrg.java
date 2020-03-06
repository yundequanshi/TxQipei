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
import com.zuomei.model.MLLotteryRecordData;
import com.zuomei.model.MLLotteryRecordResponse;
import com.zuomei.services.MLMyServices;

import java.util.List;

import cn.ml.base.utils.IEvent;

/**
 * 抽奖记录
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLLotteryRecordFrg extends BaseFragment{

	public static MLLotteryRecordFrg INSTANCE =null;
	
	public static MLLotteryRecordFrg instance(){
	//	if(INSTANCE==null){
			INSTANCE = new MLLotteryRecordFrg();
	//	}
		return INSTANCE;
	}
	
	@ViewInject(R.id.record_lv)
	private AbPullToRefreshView _pullToRefreshLv;
	@ViewInject(R.id.mListView)
	private ListView _recordLv;

	
	private MLLotteryAdapter _adapter;
	private Context _context;
	private 	MLLogin _user;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.lottery_record_main, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		
		initView();
		initData();
		return view;
	}
	
	
	private void initView() {
		_user = ((BaseApplication)getActivity().getApplication()).get_user();
		_adapter = new MLLotteryAdapter(_context);
		_recordLv.setAdapter(_adapter);
	
		
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

	private void initData() {
		
		ZMRequestParams params = new ZMRequestParams();
	
			params.addParameter("depotId",_user.Id);
	      ZMHttpRequestMessage message = new ZMHttpRequestMessage(RequestType.LOTTERY_RECORD_LIST, null, params, _handler, HTTP_RESPONSE_LIST, MLMyServices.getInstance());
	      loadDataWithMessage(_context, null, message);
	}
	
	private void pageData() {
		ZMRequestParams params = new ZMRequestParams();
		String lastId = _datas.get( _datas.size()-1).id+"";
		params.addParameter("depotId",_user.Id);
		params.addParameter(MLConstants.PARAM_MESSAGE_LASTID,lastId);
	      ZMHttpRequestMessage message = new ZMHttpRequestMessage(RequestType.LOTTERY_RECORD_LIST, null, params, _handler, HTTP_RESPONSE_LIST_PAGE, MLMyServices.getInstance());
	      loadDataWithMessage(_context, null, message);
	}
	
	

	
	
	 private static final int HTTP_RESPONSE_LIST = 0;
	 private static final int HTTP_RESPONSE_RECHARGE = 1;
	 private static final int HTTP_RESPONSE_WITHDRAW = 2;
	 private static final int HTTP_RESPONSE_LIST_PAGE = 3;
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
	            case HTTP_RESPONSE_LIST:{
	            	MLLotteryRecordResponse ret = (MLLotteryRecordResponse) msg.obj;
	            	_datas = ret.datas;
			   		if(ret.state.equalsIgnoreCase("1")){
			   		if(ret.datas.size()<20){
			   				_pullToRefreshLv.setLoadMoreEnable(false);
			   			}
				            review(ret.datas);
			   		   }
					_pullToRefreshLv.onHeaderRefreshFinish();
			   		break;
	            }
	
	         
	            case HTTP_RESPONSE_LIST_PAGE:{
	            	MLLotteryRecordResponse ret = (MLLotteryRecordResponse) msg.obj;
			   		if(ret.state.equalsIgnoreCase("1")){
				           _datas.addAll(ret.datas);
				           review(_datas);
			   		   }
					_pullToRefreshLv.onFooterLoadFinish();
	            	break;
	            }
	                default:
	                    break;
	            }
	        }
	    };
	
	/**
	  * @description   返回
	  *
	  * @author marcello
	 */
	@OnClick(R.id.top_back)
	public void backOnClick(View view){
		((MLAuxiliaryActivity)_context).onBackPressed();
	}
	
	private List<MLLotteryRecordData> _datas;
	protected void review(List<MLLotteryRecordData> _datas2) {
		_adapter.setData(_datas2);
	}

	private IEvent<Object> _event;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_event = (IEvent<Object>) activity;
	}
}
