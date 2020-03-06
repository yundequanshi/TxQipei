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
import org.greenrobot.eventbus.EventBus;

/**
 *最近通话（商家）
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyPhoneBusinessFrg extends BaseFragment{

	public static MLMyPhoneBusinessFrg INSTANCE =null;
	
	public static MLMyPhoneBusinessFrg instance(){
			INSTANCE = new MLMyPhoneBusinessFrg();
		return INSTANCE;
	}
	@ViewInject(R.id.phone_lv)
	private AbPullToRefreshView _pullToRefreshLv;
	@ViewInject(R.id.mListView)
	private ListView _phoneLv;

	@ViewInject(R.id.phone_tv_count)
	private TextView _countTv;
	
	private Context _context;
	private MLMyPhoneBusinessAdapter _adapter;
	public List<MLDialData> _datas;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_phone_business_main, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		
		initView();
		initData();
		return view;
	}
	
	
	private void initView() {
		_countTv.setVisibility(View.GONE);
		
		_adapter = new MLMyPhoneBusinessAdapter(_context);
		_phoneLv.setAdapter(_adapter);
		
		
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
				pageDate();
			}
		});  
		
		_phoneLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				MLDialData data = (MLDialData) arg0.getAdapter().getItem(arg2);
				EventBus.getDefault().postSticky(data);
				((MLAuxiliaryActivity)_context).finish();
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
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_DIAL_LIST, null, catalogParam, _handler, HTTP_RESPONSE_DIAL_LIST,  MLMyServices.getInstance());
	    loadDataWithMessage(_context, null, message1);
	}
	
	 private void pageDate(){
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
         			_datas = ret.datas;
         			_adapter.setData(ret.datas);
             	}else{
             		showMessageError("获取通话列表失败!");
             	}
         		_pullToRefreshLv.onHeaderRefreshFinish();
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
