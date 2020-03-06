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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

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
import com.zuomei.model.MLHomeBusinessData;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLMyPartBusinessData;
import com.zuomei.model.MLMyPartBusinessResponse;
import com.zuomei.model.MLMyPartOfferMgData;
import com.zuomei.services.MLPartServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.ml.base.utils.IEvent;

/**
 * 我的-事故车配件-商家列表
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyAccidentBnList extends BaseFragment{

	public static MLMyAccidentBnList INSTANCE =null;
	
	public static MLMyPartOfferMgData _data;
	public static MLMyAccidentBnList instance(Object obj){
		_data= (MLMyPartOfferMgData) obj;
			INSTANCE = new MLMyAccidentBnList();
		return INSTANCE;
	}
	
	@ViewInject(R.id.home_lv_business)
	private ListView _businessLv;
	
	@ViewInject(R.id._refressview)
	private AbPullToRefreshView _refreshView;
	
	@ViewInject(R.id.home_et_search)
	private EditText _searchEt;
	
	@ViewInject(R.id.login_register_root)
	private RelativeLayout _root;
	private MLMyAccidentBnAdapter _searchAdapter;
	private Context _context;
	private MLLogin _user;
	private List<MLMyPartBusinessData> _businessData;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_business_list, null);
		ViewUtils.inject(this,view);
		_context = inflater.getContext();
		init();
		initData();
		return view;
	}   
	
	private void init() {
		_searchAdapter = new MLMyAccidentBnAdapter(_context,new IEvent<MLMyPartBusinessData>() {
			@Override
			public void onEvent(Object source, MLMyPartBusinessData data) {
				MLHomeBusinessData mData = new MLHomeBusinessData();
				mData.id = data.companyId;
				toActivity(_context, MLConstants.HOME_BUSINESS_INFO, mData);
			}
		});
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
			/*	MLHomeBusinessData data = (MLHomeBusinessData) parent.getAdapter().getItem(position);
				Intent intent = new Intent();
				intent.setClass(_context, MLAuxiliaryActivity.class);
				intent.putExtra("data", MLConstants.HOME_BUSINESS_INFO);
				intent.putExtra("obj", (Serializable) data);
				startActivity(intent);*/
				MLMyPartBusinessData d = _businessData.get(position);
				d.state = _data.state;
				toActivity(_context, MLConstants.PART_DEPOT_DETAIL, d);
			}
		});
		
		_user = 	((BaseApplication)getActivity().getApplication()).get_user();
	}

	private void initData(){
		ZMRequestParams params = new ZMRequestParams();
		  JSONObject jo = new JSONObject();
		  try {
			jo.put("id", _data.id);
		} catch (JSONException e) {
			return;
		}
			params.addParameter("data",jo.toString());
		
	    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.DEPOT_PART_BUSINESS_LIST, null, params, _handler,HTTP_RESPONSE_LIST, MLPartServices.getInstance());
	    loadData(_context, message2);
	}
	
	private void pageData() {
		ZMRequestParams params = new ZMRequestParams();
		  JSONObject jo = new JSONObject();
		  try {
			jo.put("Id", _data.id);
			jo.put("lastId", _businessData.get(_businessData.size()-1).id+"");
		} catch (JSONException e) {
			return;
		}
			params.addParameter("data",jo.toString());
		
	    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.DEPOT_PART_BUSINESS_LIST, null, params, _handler,HTTP_RESPONSE_PAGE, MLPartServices.getInstance());
	    loadData(_context, message2);
	}
	
	 private static final int HTTP_RESPONSE_LIST = 1;
	 private static final int HTTP_RESPONSE_PAGE = 2;
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
	            //获取商家列表
	            case HTTP_RESPONSE_LIST:{
	            	MLMyPartBusinessResponse ret = (MLMyPartBusinessResponse) msg.obj;
         		if(ret.state.equalsIgnoreCase("1")){
         			_businessData = ret.datas;
         			setBusinessList(ret.datas);
             	}else{
             		showMessage("获取商家列表失败!");
             	}
         		if(ret.datas!=null&&ret.datas.size()<=20){
    				_refreshView.setLoadMoreEnable(false);
    			}else{
    				_refreshView.setLoadMoreEnable(true);
    			}
         		_refreshView.onHeaderRefreshFinish();
	            	break;
	            }
	            case HTTP_RESPONSE_PAGE:{
	            	MLMyPartBusinessResponse ret = (MLMyPartBusinessResponse) msg.obj;
                	if(ret.state.equalsIgnoreCase("1")){
                		if(ret.datas.size()!=0){
                			_businessData.addAll(ret.datas);
                			_searchAdapter.setData(_businessData);
                		}
                	}else{
                		showMessage("获取商家列表失败!");
                	}
                	_refreshView.onFooterLoadFinish();
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
	@OnClick(R.id.home_top_back)
	public void backOnClick(View view){
	//	_event.onEvent(null, MLConstants.HOME_CAR);
		((MLAuxiliaryActivity)_context).finish();
		/*startActivity(new Intent(_context,MLHomeActivity.class));
		((MLAuxiliaryActivity)_context).finish();*/
	}

	protected void setBusinessList(List<MLMyPartBusinessData> datas) {
		_searchAdapter.setData(datas);
	}

	private IEvent<Object> _event;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_event = (IEvent<Object>) activity;
	}
	
	
}
