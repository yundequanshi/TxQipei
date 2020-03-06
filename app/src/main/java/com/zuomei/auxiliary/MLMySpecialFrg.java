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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
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
import com.zuomei.model.MLMySpecialData;
import com.zuomei.model.MLMySpecialResponse;
import com.zuomei.services.MLMyServices;

import java.util.List;

import cn.ml.base.utils.IEvent;

/**
 * 首页-优惠信息列表页
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMySpecialFrg extends BaseFragment{

	public static MLMySpecialFrg INSTANCE =null;
	
	public static MLMySpecialFrg instance(){
	//	if(INSTANCE==null){
			INSTANCE = new MLMySpecialFrg();
	//	}
		return INSTANCE;
	}
	
	/*@ViewInject(R.id.login_et_address)
	private EditText _addressEt;*/
	
/*	@ViewInject(R.id.home_lv_business)
	private ListView _businessLv;*/
	
	@ViewInject(R.id.listview)
	private AbPullToRefreshView _pullToRefreshLv;
	@ViewInject(R.id.mListView)
	private ListView _businessLv;
	//private ListView _messageLv;
	public List<MLMySpecialData> _specialDatas;
	private MLMySpecialAdapter _specialAdapter;
	private Context _context;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_special, null);
		ViewUtils.inject(this,view);
		_context = inflater.getContext();
		
		init();
		initData();
		return view;
	}
	
	private void init() {
		_specialAdapter = new MLMySpecialAdapter(_context);
		_businessLv.setAdapter(_specialAdapter);
	/*	ZMRequestParams catalogParam = new ZMRequestParams();
		catalogParam.addParameter(MLConstants.PARAM_HOME_CATALOG,"0");*/

	    _businessLv.setSelector(getResources().getDrawable(R.drawable.message_list_selector));
	    _businessLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				MLMySpecialData data = (MLMySpecialData) arg0.getAdapter().getItem(arg2);
				_event.onEvent(data,MLConstants.MY_SPECIAL_DETAIL);
			}
		});
	    
	    
	_pullToRefreshLv.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			
			@Override
			public void onHeaderRefresh(AbPullToRefreshView view) {
				initData();
			}
		});;
		_pullToRefreshLv.setOnFooterLoadListener(new OnFooterLoadListener() {
			
			@Override
			public void onFooterLoad(AbPullToRefreshView view) {
				// TODO Auto-generated method stub
				pageData();
			}
		});  
	}

	private void initData(){
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_SPECIAL_LIST, null, null, _handler, HTTP_RESPONSE_SPECIAL_LIST, MLMyServices.getInstance());
	    loadData(_context, message1);
	}
	private void pageData(){
		ZMRequestParams param = new ZMRequestParams();
		String lastId = _specialDatas.get(_specialDatas.size()-1).id+"";
		//param.addParameter("pageNum","2");
		param.addParameter(MLConstants.PARAM_MESSAGE_LASTID,lastId);
		 ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_SPECIAL_LIST, null, param, _handler, HTTP_RESPONSE_SPECIAL_PAGE, MLMyServices.getInstance());
		    loadData(_context, message1);
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
	
	 private static final int HTTP_RESPONSE_SPECIAL_LIST = 0;
	 private static final int HTTP_RESPONSE_SPECIAL_PAGE = 1;
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
	           // 优惠列表页
	            case HTTP_RESPONSE_SPECIAL_LIST:{
	                	MLMySpecialResponse ret = (MLMySpecialResponse) msg.obj;
	                	if(ret.state.equalsIgnoreCase("1")){
	                		_specialDatas = ret.datas;
	                		_specialAdapter.setData(ret.datas);
	                	}else{
	                		showMessageError("获取优惠信息失败!");
	                	}
	                	_pullToRefreshLv.onHeaderRefreshFinish();
	                    break;
	            }
	            case HTTP_RESPONSE_SPECIAL_PAGE:{
	            	MLMySpecialResponse ret = (MLMySpecialResponse) msg.obj;
                	if(ret.state.equalsIgnoreCase("1")){
                		_specialDatas.addAll(ret.datas);
                		_specialAdapter.setData(_specialDatas);
                	}else{
                		showMessageError("获取优惠信息失败!");
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
