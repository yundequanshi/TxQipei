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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
import com.zuomei.model.MLMyUserData;
import com.zuomei.model.MLMyUserListResponse;
import com.zuomei.model.MLSpecialResonse;
import com.zuomei.services.MLMyServices;
import com.zuomei.utils.MLToolUtil;

import java.util.List;
/**
 * 我的-用户量
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyUserFrg extends BaseFragment{

	public static MLMyUserFrg INSTANCE =null;
	
	public static MLMyUserFrg instance(){
	//	if(INSTANCE==null){
			INSTANCE = new MLMyUserFrg();
	//	}
		return INSTANCE;
	}
	
	@ViewInject(R.id.home_et_search)
	private EditText _searchEt;
	
/*	@ViewInject(R.id.home_lv_business)
	private ListView _businessLv;*/
	
	@ViewInject(R.id.top_title)
	private TextView _countTv;
	
	/*@ViewInject(R.id.btn_search)
	private ImageView searchBtn;;*/
	
	@ViewInject(R.id.refresh_lv)
	private AbPullToRefreshView _pullToRefreshLv;
	@ViewInject(R.id.home_lv_business)
	private ListView _businessLv;
	
	private MLMyUserAdapter _userAdapter;
	private Context _context;

	@ViewInject(R.id.user_tv_count)
	private TextView mTvCount;

	private List<MLMyUserData> _datas;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_user, null);
		ViewUtils.inject(this,view);
		_context = inflater.getContext();
		
		init();
	//	initData();
		getUserList();
		return view;
	}
	
	private void init() {
		_userAdapter = new MLMyUserAdapter(_context);
		_businessLv.setAdapter(_userAdapter);
		
	_pullToRefreshLv.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			
			@Override
			public void onHeaderRefresh(AbPullToRefreshView view) {
				getUserList();
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
		//获取用户数量
	     ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.MY_USER_COUNT, null, null, _handler,HTTP_RESPONSE_COUNT , MLMyServices.getInstance());
	     loadData(_context, message2);
	}
	
	private void pageData() {
		ZMRequestParams param = new ZMRequestParams();
		String lastId = _datas.get(_datas.size()-1).id+"";
		//param.addParameter("pageNum","2");
		 
		 if(!MLToolUtil.isNull(text)){
			 param.addParameter("key",text);
			 param.addParameter("isCompany",_datas.get(_datas.size()-1).isCompany+"");
		 }
		 
		
		param.addParameter(MLConstants.PARAM_MESSAGE_LASTID,lastId);
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_USER_LIST, null, param, _handler, HTTP_RESPONSE_LIST_PAGE, MLMyServices.getInstance());
	    loadDataWithMessage(_context, null, message1);
	}

	String text;
	 private void searchUserList(){

		 text = _searchEt.getText().toString();
		 if(MLToolUtil.isNull(text)){
		     showMessageWarning("请输入要查询的关键字!");
			 return ;
		 }
		 
		 if(_datas==null||_datas.size()==0){
			 return;
		 }
		 
		   ZMRequestParams params = new ZMRequestParams();
		     params.addParameter("key",text);
		     params.addParameter("isCompany",_datas.get(_datas.size()-1).isCompany+"");
		  ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.MY_USER_LIST, null, params, _handler,HTTP_RESPONSE_LIST , MLMyServices.getInstance());
		  loadData(_context, message2);
	  }
	
  private void getUserList(){
	  _searchEt.setText("");
	  //获取用户列表
	  ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.MY_USER_LIST, null, null, _handler,HTTP_RESPONSE_LIST , MLMyServices.getInstance());
	  loadData(_context, message2);
  }
	protected void reviewMessageList(List<MLMyUserData> datas) {
		_userAdapter.setData(datas);
	}
	@OnClick(R.id.btn_search)
	public void searchOnClick(View view){
		searchUserList();
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
	 private static final int HTTP_RESPONSE_COUNT = 1;
	 private static final int HTTP_RESPONSE_LIST = 2;
	 private static final int HTTP_RESPONSE_LIST_PAGE = 3;
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
	            /*	MLHomeBusinessResponse ret = (MLHomeBusinessResponse) msg.obj;
	                	if(ret.state.equalsIgnoreCase("1")){
	                		if(ret.datas.size()==0){
	                			showMessage("未查询到相关信息!");
	                		}else{
	                			_userAdapter.setData(ret.datas);
	                		}
	                	}else{
	                		showMessage("搜索失败!");
	                	}*/
	                    break;
	            }
	            case HTTP_RESPONSE_COUNT:{
	            	MLSpecialResonse ret = (MLSpecialResonse) msg.obj;
                	if(ret.state.equalsIgnoreCase("1")){
						mTvCount.setText("用户数量 : "+ret.datas);
                	}else{
                		showMessageError("未请求到用户数量!");
                	}
	            	break;
	            }
	            case HTTP_RESPONSE_LIST:{
	            	MLMyUserListResponse ret = (MLMyUserListResponse) msg.obj;
                	if(ret.state.equalsIgnoreCase("1")){
                		_datas = ret.datas.data;
                		_userAdapter.setData(ret.datas.data);
						mTvCount.setText("用户数量 : "+ret.datas.count);
                	}else{
                	//	showMessageError("");
                	}
                	_pullToRefreshLv.onHeaderRefreshFinish();
	            	break;
	            }
	            case HTTP_RESPONSE_LIST_PAGE:{
	            	MLMyUserListResponse ret = (MLMyUserListResponse) msg.obj;
	            	if(ret.state.equalsIgnoreCase("1")){
	            		if(ret.datas.data.size()==0){
	            			_pullToRefreshLv.onFooterLoadFinish();
	            			break;  
	            		}
	            		_datas.addAll(ret.datas.data);
	          			reviewMessageList(_datas);
						mTvCount.setText("用户数量 : "+ret.datas.count);
	              	}else{
	              		showMessageError("获取消息列表失败!");
	              	}
	          		_pullToRefreshLv.onFooterLoadFinish();
	            	break;
	            }
	            
	                default:
	                    break;
	            }
	        }
	    };
}
