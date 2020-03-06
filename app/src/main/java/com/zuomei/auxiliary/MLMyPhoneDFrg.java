package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
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
import com.zuomei.model.MLDialData;
import com.zuomei.model.MLDialResponse;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLRegister;
import com.zuomei.model.MLSpecialResonse2;
import com.zuomei.services.MLMyServices;

import java.util.List;

import cn.ml.base.utils.IEvent;

/**
 * 通话记录
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyPhoneDFrg extends BaseFragment{

	public static MLMyPhoneDFrg INSTANCE =null;
	
	public static MLMyPhoneDFrg instance(){
	//	if(INSTANCE==null){
			INSTANCE = new MLMyPhoneDFrg();
	//	}
		return INSTANCE;
	}
	@ViewInject(R.id.phone_lv)
	private AbPullToRefreshView _pullToRefreshLv;
	@ViewInject(R.id.mListView)
	private ListView _phoneLv;

	@ViewInject(R.id.phone_tv_count)
	private TextView _countTv;
	
	private Context _context;
	private MLMyPhoneDAdapter _adapter;
	public List<MLDialData> _datas;

	public int page = 1;
	public boolean isRefresh = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_phone_d, null);
		ViewUtils.inject(this, view);
		
		_context = inflater.getContext();
		initView();
		initData();
		return view;
	}
	
	
	private void initView() {
	// 汽修厂显示剩余分钟， 汽配城不显示
		MLLogin user  =((BaseApplication)getActivity().getApplication()).get_user();
		/*if(user.isDepot){
			_countTv.setVisibility(View.VISIBLE);
			getCount();
		}else{
			_countTv.setVisibility(View.GONE);
		}*/
		
		_adapter = new MLMyPhoneDAdapter(_context);
		_phoneLv.setAdapter(_adapter);
		
		
	_pullToRefreshLv.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			
			@Override
			public void onHeaderRefresh(AbPullToRefreshView view) {
				page = 1;
				isRefresh = true;
				initData();
			}
		});;
		_pullToRefreshLv.setOnFooterLoadListener(new OnFooterLoadListener() {
			
			@Override
			public void onFooterLoad(AbPullToRefreshView view) {
				// TODO Auto-generated method stub
				isRefresh = false;
				page++;
				initData();
			}
		});  
		
		_phoneLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				MLDialData data = _datas.get(arg2);
				_event.onEvent(data, MLConstants.MY_PHONE_DETAIL);
			}
		});
		
		_phoneLv.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
										   int position, long id) {
				final MLDialData data = (MLDialData) parent.getAdapter().getItem(position);
				Builder builder = new Builder(_context);
				builder.setItems(new String[]{"删除"}, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
						delPhone(data.id);
					}


				}).setTitle("请选择");
				builder.show();
				return true;
			}
		});
	}

	
	private void 	initData(){
		ZMRequestParams catalogParam = new ZMRequestParams();
		MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
		if(user.isDepot){
			catalogParam.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,user.Id);
		}else{
			catalogParam.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,user.Id);
		}

		catalogParam.addParameter("nowPage",page+"");
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_DIAL_LIST, null, catalogParam, _handler, HTTP_RESPONSE_DIAL_LIST,  MLMyServices.getInstance());
	    loadDataWithMessage(_context, null, message1);
	}
	
/*	 private void pageDate(){
			ZMRequestParams catalogParam = new ZMRequestParams();
			MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
			if(user.isDepot){
				catalogParam.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,user.Id);
			}else{
				catalogParam.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,user.Id);
			}
			String lastId = _datas.get(_datas.size()-1).id+"";
			catalogParam.addParameter(MLConstants.PARAM_MESSAGE_LASTID,lastId);
		    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_DIAL_LIST, null, catalogParam, _handler, HTTP_RESPONSE_DIAL_PAGE,  MLMyServices.getInstance());
		    loadDataWithMessage(_context, null, message1);
	 }*/

	
	private void getCount(){
		MLLogin data = ((BaseApplication)getActivity().getApplication()).get_user();
		ZMRequestParams catalogParam = new ZMRequestParams();
		catalogParam.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,data.Id);
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_DIAL_COUNT, null, catalogParam, _handler, HTTP_RESPONSE_DIAL_COUNT,  MLMyServices.getInstance());
	    loadDataWithMessage(_context, null, message1);
	}
	
	private void delPhone(String id) {
		ZMRequestParams catalogParam = new ZMRequestParams();
		catalogParam.addParameter(MLConstants.PARAM_MY_PHONE_DELID,id);
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_DIAL_DEL, null, catalogParam, _handler, HTTP_RESPONSE_DIAL_DEL,  MLMyServices.getInstance());
	    loadDataWithMessage(_context, null, message1);
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
	
	/**
	  * @description  搜索
	  *
	  * @author marcello
	 */
	@OnClick(R.id.ib_search)
	public void searchOnClick(View view){
		_event.onEvent(null, MLConstants.MY_PHONE_D_SEARCH);
	}
	

	
	 private static final int HTTP_RESPONSE_DIAL_LIST= 0;
	 private static final int HTTP_RESPONSE_DIAL_COUNT= 1;
	 private static final int HTTP_RESPONSE_DIAL_PAGE= 2;
	 private static final int HTTP_RESPONSE_DIAL_DEL= 3;
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
	            case HTTP_RESPONSE_DIAL_LIST:{
	            	MLDialResponse ret = (MLDialResponse) msg.obj;
         		if(ret.state.equalsIgnoreCase("1")){


					if(isRefresh){
						_datas = ret.datas;
					}else{
						_datas.addAll(ret.datas);
					}

         			_adapter.setData(_datas);
             	}else{
             		showMessageError("获取通话列表失败!");
             	}
         		_pullToRefreshLv.onHeaderRefreshFinish();
					_pullToRefreshLv.onFooterLoadFinish();
	            	break;
	            }
	            case HTTP_RESPONSE_DIAL_COUNT:{
	            	MLSpecialResonse2 ret = (MLSpecialResonse2) msg.obj;
	            	String text = String.format("剩余免费:%s     已用免费:%s", ret.datas.KEY_SURPLUS,ret.datas.KEY_USED);
	         		if(ret.state.equalsIgnoreCase("1")){
	         			_countTv.setText(text);
	             	}else{
	             		showMessageError("获取剩余分钟失败");
	             	}
	            	break;
	            }
	            case HTTP_RESPONSE_DIAL_PAGE:{
	            	MLDialResponse ret = (MLDialResponse) msg.obj;
	         		if(ret.state.equalsIgnoreCase("1")){
	         			if(ret.datas.size()>0){
	         				_datas.addAll(ret.datas);
	         				_adapter.setData(_datas);
	         			}else{
	         				showMessageWarning("数据已记载完成!");
	         			}
	         			
	             	}else{
	             		showMessageError("获取通话列表失败!");
	             	}
	         		_pullToRefreshLv.onFooterLoadFinish();
	            	break;
	            }
	            case HTTP_RESPONSE_DIAL_DEL:{
	            	MLRegister ret =  (MLRegister) msg.obj;
	            	if(ret.state.equalsIgnoreCase("1")&&ret.datas==true){
	            		showMessageSuccess("删除成功!");
	            		initData();
	            	}else{
	            		showMessageError("删除失败!");
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
