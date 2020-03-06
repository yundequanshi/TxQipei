package com.zuomei.auxiliary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ab.view.pullview.AbPullToRefreshView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseLayout;
import com.zuomei.constants.MLConstants;
import com.zuomei.home.MLHomeActivity;
import com.zuomei.home.MLHomeCityPop;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLHomeBusiness1Data;
import com.zuomei.model.MLHomeBusinessData;
import com.zuomei.model.MLHomeBusinessList;
import com.zuomei.model.MLHomeCityData;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLHomeServices;
import com.zuomei.services.MLMyServices;
import com.zuomei.utils.MLToolUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.ml.base.utils.IEvent;

public class MLHomeCarCollectView extends BaseLayout{

	@ViewInject(R.id.home_lv_business)
	private AbPullToRefreshView _pullToRefreshLv;
	@ViewInject(R.id.mListView)
	private ListView _businessLv;
	public List<MLHomeBusinessData> _businessDatas;
	@ViewInject(R.id.login_register_root)
	private RelativeLayout _root;
	private MLMyCollectAdapter _collectAdapter;
	private Context _context;
	public MLHomeCarCollectView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		_context = context;
		init();
	}

	public MLHomeCarCollectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		_context = context;
		init();
	}

	public MLHomeCarCollectView(Context context) {
		super(context);
		_context = context;
		init();
	}


	private void init() {
		View view = LayoutInflater.from(_context).inflate(R.layout.my_collect, null);
		addView(view);
		ViewUtils.inject(this,view);

		_user =	BaseApplication.getInstance().get_user();
		_collectAdapter = new MLMyCollectAdapter(_context,_callHandler);
		_businessLv.setAdapter(_collectAdapter);


		_businessLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				// TODO Auto-generated method stub
				MLHomeBusinessData data = (MLHomeBusinessData) arg0.getAdapter().getItem(arg2);
				data.isCollectParam = true;
				Intent intent = new Intent();
				intent.setClass(_context, MLAuxiliaryActivity.class);
				intent.putExtra("data", MLConstants.HOME_BUSINESS_INFO);
				intent.putExtra("obj", (Serializable) data);
				_context.startActivity(intent);
			}
		});

		_businessLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
										   int arg2, long arg3) {
				final MLHomeBusinessData data = (MLHomeBusinessData) arg0.getAdapter().getItem(arg2);
				AlertDialog.Builder builder = new AlertDialog.Builder(_context);
				builder.setItems( new String[] { "取消收藏" }, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{
						arg0.dismiss();
						requestCollect(data.compayId);
						//  delStock(data.id);
					}
				}).setTitle("请选择");
				builder.show();

				return false;
			}
		});

		_pullToRefreshLv.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {

			@Override
			public void onHeaderRefresh(AbPullToRefreshView view) {
				initData();
			}
		});
		_pullToRefreshLv.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {

			@Override
			public void onFooterLoad(AbPullToRefreshView view) {
				// TODO Auto-generated method stub
				pageData();
			}
		});
		_pullToRefreshLv.setPullRefreshEnable(false);
	}
	
	private IEvent<Object> _event;
	private String mId;
	public void setData(){
		initData();

	}



	private void initData(){
//		MLLogin user = BaseApplication._user;
		ZMRequestParams catalogParam = new ZMRequestParams();
		catalogParam.addParameter(MLConstants.PARAM_MY_DEPTID1,_user.Id);
		ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(ZMHttpType.RequestType.MY_COLLECT, null, catalogParam, _handler, HTTP_RESPONSE_COLLECT, MLMyServices.getInstance());
		loadDataWithMessage(null, message1);
	}
	private void pageData(){
//		MLLogin user = BaseApplication._user;
		ZMRequestParams catalogParam = new ZMRequestParams();
		catalogParam.addParameter(MLConstants.PARAM_MY_DEPTID1,_user.Id);
		String lastId = _businessDatas.get(_businessDatas.size()-1).id+"";
		catalogParam.addParameter(MLConstants.PARAM_MESSAGE_LASTID,lastId);
		ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(ZMHttpType.RequestType.MY_COLLECT, null, catalogParam, _handler, HTTP_RESPONSE_COLLECT_PAGE, MLMyServices.getInstance());
		loadDataWithMessage( null, message1);
	}

	/**
	 * @description  收藏   (参数有问题)
	 *
	 * @author marcello
	 */
	private void requestCollect(String id){
//		MLLogin user = BaseApplication._user;
		ZMRequestParams catalogParam = new ZMRequestParams();
		catalogParam.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,id);
		catalogParam.addParameter(MLConstants.PARAM_HOME_DEPORT,_user.Id);
		catalogParam.addParameter(MLConstants.PARAM_HOME_ISFLAG,"0");
		ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(ZMHttpType.RequestType.HOME_COLLECT, null, catalogParam, _handler, HTTP_RESPONSE_COLLECT_CANCEL, MLHomeServices.getInstance());
		loadDataWithMessage(null, message1);
	}
	private MLHomeCityPop _menuWindow ;
	private       MLHomeBusinessData _data;
	private MLLogin _user;
	private Handler _callHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			_data = (MLHomeBusinessData) msg.obj;

			final List<MLHomeCityData> datas = new ArrayList<MLHomeCityData>();
			MLHomeCityData data1 =new MLHomeCityData();
			data1.cityName = _data.phone;

			MLHomeCityData data2 =new MLHomeCityData();
			data2.cityName = _data.phone1;

			MLHomeCityData data3 =new MLHomeCityData();
			data3.cityName = _data.phone2;
			Collections.addAll(datas, data1, data2, data3);

			_menuWindow= new MLHomeCityPop((MLHomeActivity)_context, datas,new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
										long arg3) {
					MLHomeCityData data =  datas.get(arg2);
					String phoneNum = data.cityName;
					_menuWindow.dismiss();
					if(_user.isDepot){
						call(phoneNum);
					}else{
						Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
						_context.startActivity(intent);
					}
				}
			});
			_menuWindow.showAtLocation(_root, Gravity.CENTER, 0, 0);

		}};

	private void call(final String phone){
		if(MLToolUtil.isNull(phone)){
			return;
		}
		if(phone.startsWith("400")){
			Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));
			_context.startActivity(intent);
			dial("0");
			return;
		}
		final List<MLHomeCityData> datas = new ArrayList<MLHomeCityData>();
		MLHomeCityData data1 =new MLHomeCityData();
		data1.cityName = "直接拨打";

		MLHomeCityData data2 =new MLHomeCityData();
		data2.cityName =  "免费通话";
		Collections.addAll(datas, data1,data2);

		_menuWindow= new MLHomeCityPop((MLHomeActivity)_context, datas,new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				if(arg2==0){
					//直接拨打
					Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));
					_context.startActivity(intent);
					dial("0");
				}else{
					//网络电话
					MLHomeBusiness1Data data = new MLHomeBusiness1Data();
					data.address = _data.address;
					data.userName = _data.userName;
					data.phone = phone;
					data.logo = _data.logo;

					Intent phoneIt = new Intent();
					phoneIt.setClass(_context, MLAuxiliaryActivity.class);
					phoneIt.putExtra("data", MLConstants.HOME_CALL);
					phoneIt.putExtra("obj", (Serializable) data);
					_context.startActivity(phoneIt);
					//	dial("1");
				}
				_menuWindow.dismiss();

			}
		});
		_menuWindow.showAtLocation(_root, Gravity.CENTER, 0, 0);
	}
	private void dial(String isNetWorkPhone){
		//	MLLogin user = BaseApplication._user;
		ZMRequestParams catalogParam = new ZMRequestParams();
		if(!_user.isDepot){
			return;
		}
		catalogParam.addParameter(MLConstants.PARAM_HOME_ISNETWORKPHONE,isNetWorkPhone);
		catalogParam.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,_user.Id);
		//判断是否来自收藏列表页面
		catalogParam.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,_data.id);
		catalogParam.addParameter(MLConstants.PARAM_HOME_DEPOTPHONE,BaseApplication.aCache.getAsString(MLConstants.PARAM_REGISTER_USERNAME));
		catalogParam.addParameter(MLConstants.PARAM_HOME_COMPANYPHONE,_data.phone);
		catalogParam.addParameter("phoneTime","1");

		ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(ZMHttpType.RequestType.HOME_CALL, null, catalogParam, _handler, HTTP_RESPONSE_CALL, MLHomeServices.getInstance());
		loadDataWithMessage( null, message1);
	}
	private static final int HTTP_RESPONSE_COLLECT= 0;
	private static final int HTTP_RESPONSE_COLLECT_CANCEL= 1;
	private static final int HTTP_RESPONSE_COLLECT_PAGE= 2;
	private static final int HTTP_RESPONSE_CALL= 3;
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
				//获取收藏列表
				case HTTP_RESPONSE_COLLECT:{
					MLHomeBusinessList ret = (MLHomeBusinessList) msg.obj;
					if(ret.state.equalsIgnoreCase("1")){
						_businessDatas = ret.datas;
						setBusinessList(ret.datas);
					}else{
						showMessage("获取车辆失败!");
					}
					_pullToRefreshLv.onHeaderRefreshFinish();
					break;
				}
				case HTTP_RESPONSE_COLLECT_CANCEL:{
					MLRegister ret = (MLRegister) msg.obj;
					if(ret.state.equalsIgnoreCase("1")){
						initData();
					}else{
						showMessage("操作失败!");
					}
					break;
				}
				case HTTP_RESPONSE_COLLECT_PAGE:{
					MLHomeBusinessList ret = (MLHomeBusinessList) msg.obj;
					if(ret.state.equalsIgnoreCase("1")){
						_businessDatas.addAll(ret.datas);
						setBusinessList(_businessDatas);
					}else{
						showMessage("获取车辆失败!");
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
		//getActivity().onBackPressed();
		((MLAuxiliaryActivity)_context).finish();
	}

	protected void setBusinessList(List<MLHomeBusinessData> datas) {
		_collectAdapter.setData(datas);
	}



}
