package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

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
import com.zuomei.model.MLBill2List;
import com.zuomei.model.MLBill2ListResponse;
import com.zuomei.model.MLLogin;
import com.zuomei.services.MLMyServices;

import java.util.ArrayList;
import java.util.List;
/**
 * 首页-账单列表
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyBill2ListFrg extends BaseFragment{

	public static MLMyBill2ListFrg INSTANCE =null;
	
	public static MLMyBill2ListFrg instance(){
//		if(INSTANCE==null){
			INSTANCE = new MLMyBill2ListFrg();
	//	}
		return INSTANCE;
	}
	private MLLogin _user;
	@ViewInject(R.id.home_lv_business)
	private ListView _businessLv;
	
	@ViewInject(R.id.tv_explain)
	private TextView _explain;
	
	@ViewInject(R.id.refressview)
	private AbPullToRefreshView _refreshView;
	private MLMyBill2ListAdapter _searchAdapter;
	private Context _context;
	public List<MLBill2List> _businessList;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_bill2_list, null);
		ViewUtils.inject(this,view);
		_context = inflater.getContext();
		


		initData();
		return view;
	}
	
	private void init() {
//		_user = BaseApplication._user;
		_user = 	((BaseApplication)getActivity().getApplication()).get_user();
		_searchAdapter = new MLMyBill2ListAdapter(_context);
		_businessLv.setAdapter(_searchAdapter);
		
		_refreshView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			@Override
			public void onHeaderRefresh(AbPullToRefreshView view) {
				initData();
			}
		});
		_refreshView.setOnFooterLoadListener(new OnFooterLoadListener() {
			@Override
			public void onFooterLoad(AbPullToRefreshView view) {
			pageData();	
			}
		});
		
		_businessLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MLBill2List data = _businessList.get(position);
				toActivity(_context,MLConstants.MY_BILL2_BUSINESS_DETAIL,data.id);

			//	_event.onEvent(data.id, MLConstants.MY_BILL2_BUSINESS_DETAIL);
			}
		});
	}
		public void initData(){
			init();
			MLLogin user = BaseApplication.getInstance().get_user();

		     ZMRequestParams params = new ZMRequestParams();
		     params.addParameter("depotId",user.Id);
		     ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.BILL2LIST, null, params, _handler,HTTP_RESPONSE_SEARCH , MLMyServices.getInstance());
		     loadData(_context, message2);
		}
		private void pageData() {
			String lastId = _businessList.get(_businessList.size()-1).id+"";
			MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
		     ZMRequestParams params = new ZMRequestParams();
		     params.addParameter("depotId",user.Id);
		     params.addParameter(MLConstants.PARAM_MESSAGE_LASTID,lastId);
		     ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.BILL2LIST, null, params, _handler,HTTP_RESPONSE_SEARCH_PAGE , MLMyServices.getInstance());
		     loadData(_context, message2);
		}
	@OnClick(R.id.btn_search)
	public void searchOnClick(View view){
		
	}
	/**
	  * @description   返回
	  *
	  * @author marcello
	 */
	@OnClick(R.id.home_top_back)
	public void backOnClick(View view){
		((MLAuxiliaryActivity)_context).finish();
	}
	
	 private static final int HTTP_RESPONSE_SEARCH = 0;
	 private static final int HTTP_RESPONSE_SEARCH_PAGE = 1;
	 private static final int HTTP_RESPONSE_CALL = 2;
	    private Handler _handler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	            super.handleMessage(msg);
	      	 // dismissProgressDialog();
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
	            case HTTP_RESPONSE_SEARCH:{
	            	MLBill2ListResponse ret = (MLBill2ListResponse) msg.obj;
	                	if(ret.state.equalsIgnoreCase("1")){
	                		if(ret.datas.list.size()==0){
	                			_businessList = new ArrayList<MLBill2List>();
	                			_searchAdapter.setData(_businessList);
	                		}else{
	                			_businessList = ret.datas.list;
	                			_searchAdapter.setData(ret.datas.list);
	                		}
	                		if(ret.datas.list.size()<=20){
	            				_refreshView.setLoadMoreEnable(false);
	            			}else{
	            				_refreshView.setLoadMoreEnable(true);
	            			}
	                		
	                		_explain.setText(String.format("通过数量%s条，共获得%s积分", ret.datas.count,ret.datas.score));
	                	}else{
	                		  
	                	}
	                	
	                	
	                	_refreshView.onHeaderRefreshFinish();
	                    break;
	            }
	            case HTTP_RESPONSE_SEARCH_PAGE:{
	            	MLBill2ListResponse ret = (MLBill2ListResponse) msg.obj;
                	if(ret.state.equalsIgnoreCase("1")){
                		if(ret.datas.list.size()!=0){
                			_businessList.addAll(ret.datas.list);
                			_searchAdapter.setData(_businessList);
                		}
                	}else{
                		
                	}
                	_refreshView.onFooterLoadFinish();
	            	break;
	            }
	                default:
	                    break;
	            }
	        }
	    };
	    
	/*	private IEvent<Object> _event;
		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			_event = (IEvent<Object>) activity;
		}*/
}
