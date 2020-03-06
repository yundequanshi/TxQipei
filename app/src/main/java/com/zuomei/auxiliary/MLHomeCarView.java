package com.zuomei.auxiliary;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ab.view.pullview.AbPullToRefreshView;
import com.qipei.adapter.MLPartBusinessAdapter;
import com.qipei.home.MLBusinessDetailAty;
import com.txsh.R;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseLayout;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLHomeBusinessData;
import com.zuomei.model.MLHomeBusinessList;
import com.zuomei.model.MLHomeBusinessResponse;
import com.zuomei.model.MLLogin;
import com.zuomei.services.MLHomeServices;
import com.zuomei.utils.MLToolUtil;

import java.util.List;

import cn.ml.base.utils.IEvent;
import cn.ml.base.utils.MLStrUtil;

public class MLHomeCarView extends BaseLayout{

	private Context _context;
	public MLHomeCarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		_context = context;
		init();
	}

	public MLHomeCarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		_context = context;
		init();
	}

	public MLHomeCarView(Context context) {
		super(context);
		_context = context;
		init();
	}

	private ListView _list;
	private MLPartBusinessAdapter _adapter;
	private AbPullToRefreshView _refreshView;

	private MLLogin _user;
	private List<MLHomeBusinessData> _businessData;
	private void init() {
		View view = LayoutInflater.from(_context).inflate(R.layout.home_car_view, null);
		_refreshView = (AbPullToRefreshView) view.findViewById(R.id._refressview);
		addView(view);
		
		_adapter = new MLPartBusinessAdapter(_context,R.layout.item_part_car);
		_list = (ListView) view.findViewById(R.id.car_lv);
		_list.setAdapter(_adapter);
		_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {

				MLHomeBusinessData data = (MLHomeBusinessData) arg0.getAdapter().getItem(arg2);
			//	startAct(MLPartListFrag.this, MLBusinessDetailAty.class, _business.get(position));

				Intent intent = new  Intent(_context, MLBusinessDetailAty.class);
				intent.putExtra("Data",data);
				_context.startActivity(intent);

				/*MLHomeCatalogData data = (MLHomeCatalogData) arg0.getAdapter().getItem(arg2);
				//showMessage("sdddddddd"+arg2);
				_event.onEvent(data, MLConstants.HOME_BUSINESS_LIST);*/
			}
		});

		_refreshView.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
			@Override
			public void onHeaderRefresh(AbPullToRefreshView view) {
				refreshData();
			}
		});
		_refreshView.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
			@Override
			public void onFooterLoad(AbPullToRefreshView view) {
				pageData();
			}
		});
		_refreshView.setPullRefreshEnable(false);
	}
	
	private IEvent<Object> _event;
	private String mId;
	public void setData(String id, IEvent<Object> event){
		_event = event;
		mId = id;
		initData();

	}

	private void initData() {
		ZMRequestParams catalogParam = new ZMRequestParams();
		catalogParam.addParameter(MLConstants.PARAM_HOME_CARTYPE, mId);
		catalogParam.addParameter(MLConstants.PARAM_HOME_CITYID, BaseApplication._currentCity);
		ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(ZMHttpType.RequestType.HOME_BUSINESS_LIST, null, catalogParam, _handler, HTTP_RESPONSE_BUSINESS_LIST, MLHomeServices.getInstance());

			loadDataWithMessage(MLToolUtil.getResourceString(R.string.loading_message), message1);
	}

	private void refreshData() {
		ZMRequestParams catalogParam = new ZMRequestParams();
		catalogParam.addParameter(MLConstants.PARAM_HOME_CARTYPE, mId);
		catalogParam.addParameter(MLConstants.PARAM_HOME_CITYID, BaseApplication._currentCity);
		ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(ZMHttpType.RequestType.HOME_BUSINESS_LIST, null, catalogParam, _handler, HTTP_RESPONSE_BUSINESS_LIST, MLHomeServices.getInstance());

		loadDataWithMessage(null, message1);
	}

	private void pageData() {
		if(MLStrUtil.isEmpty(mId)){
			return;
		}
		String lastId = _businessData.get(_businessData.size()-1).id+"";
		ZMRequestParams params = new ZMRequestParams();
		params.addParameter(MLConstants.PARAM_HOME_CITYID,	BaseApplication._currentCity );
		params.addParameter(MLConstants.PARAM_HOME_CARTYPE,mId);
		params.addParameter(MLConstants.PARAM_MESSAGE_LASTID,lastId);
		ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.HOME_BUSINESS_LIST, null, params, _handler, HTTP_RESPONSE_SEARCH_PAGE, MLHomeServices.getInstance());
		loadDataWithMessage(null, message2);
	}

	private static final int HTTP_RESPONSE_BUSINESS_LIST = 1;
	private static final int HTTP_RESPONSE_SEARCH_PAGE = 2;
	private static final int HTTP_RESPONSE_SEARCH = 3;
	private static final int HTTP_RESPONSE_CALL = 4;
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
				case HTTP_RESPONSE_BUSINESS_LIST:{
					MLHomeBusinessList ret = (MLHomeBusinessList) msg.obj;
					if(ret.state.equalsIgnoreCase("1")){
						_businessData = ret.datas;
						_adapter.setData(ret.datas);
					}else{
						showMessage("获取车辆失败!");
					}
					if(ret.datas!=null&&ret.datas.size()<=20){
						_refreshView.setLoadMoreEnable(false);
					}else{
						_refreshView.setLoadMoreEnable(true);
					}
					_refreshView.onHeaderRefreshFinish();
					break;
				}
				case HTTP_RESPONSE_SEARCH_PAGE:{
					MLHomeBusinessList ret = (MLHomeBusinessList) msg.obj;
					if(ret.state.equalsIgnoreCase("1")){
						if(ret.datas.size()!=0){
							_businessData.addAll(ret.datas);
							_adapter.setData(_businessData);
						}
					}else{
						showMessage("搜索失败!");
					}
					_refreshView.onFooterLoadFinish();
					break;
				}
				case HTTP_RESPONSE_SEARCH:{
					MLHomeBusinessResponse ret = (MLHomeBusinessResponse) msg.obj;
					if(ret.state.equalsIgnoreCase("1")){
						_businessData = ret.datas;
						_adapter.setData(ret.datas);
					}else{
						showMessage("没有相关的信息！");
					}
					break;
				}
				case HTTP_RESPONSE_CALL:{
					break;
				}
				default:
					break;
			}
		}
	};

}
