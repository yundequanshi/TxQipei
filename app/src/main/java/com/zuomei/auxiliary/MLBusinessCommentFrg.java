package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.zuomei.model.MLCommentData;
import com.zuomei.model.MLCommentResponse;
import com.zuomei.model.MLDealListData;
import com.zuomei.services.MLMyServices;

import java.util.List;

import cn.ml.base.utils.IEvent;

/**
 * 评论
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLBusinessCommentFrg extends BaseFragment{

	public static MLBusinessCommentFrg INSTANCE =null;
	static String id ="";
	public static MLBusinessCommentFrg instance(Object obj){
		id = (String) obj;
	//	if(INSTANCE==null){
			INSTANCE = new MLBusinessCommentFrg();
	//	}
		return INSTANCE;
	}
	
	@ViewInject(R.id.comment_lv)
	private AbPullToRefreshView _pullToRefreshLv;
	@ViewInject(R.id.mListView)
	private ListView _messageLv;

/*	@ViewInject(R.id.tab)
	private  MLTabGroup _tab;*/
	
	private  String state="1";
	
	/**互动消息列表*/
	private List<MLCommentData> _messageData;
	  
	private int _replyPositiion;
	private MLBusinessCommentAdapter _commentAdapter;
	private Context _context;
	List<String[]> message ;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_comment, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		
		initData();
		initView();
		return view;
	}

	private void initData() {
		ZMRequestParams param = new ZMRequestParams();
		param.addParameter("companyId",id);
		param.addParameter("level",state);
		 ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.HOME_COMMENT, null, param, _handler, HTTP_RESPONSE_MESSSAGE_LIST, MLMyServices.getInstance());
		    loadDataWithMessage(_context, null, message1);
	}
	private void pageData() {
		ZMRequestParams param = new ZMRequestParams();
		param.addParameter("companyId",id);
		param.addParameter("level",state);
		String lastId = _messageData.get(_messageData.size()-1).id+"";
		param.addParameter(MLConstants.PARAM_MESSAGE_LASTID,lastId);
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.HOME_COMMENT, null, param, _handler, HTTP_RESPONSE_MESSSAGE_PAGE, MLMyServices.getInstance());
	    loadDataWithMessage(_context, null, message1);
	}
    
	
	private void initView() {
	//	_messageLv = _pullToRefreshLv.getRefreshableView();
		_commentAdapter = new MLBusinessCommentAdapter(_context);
		_messageLv.setAdapter(_commentAdapter);
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
		
		/*_tab.setRadioCheckedCallBack(new IRadioCheckedListener() {
			
			@Override
			public void radioChecked(RadioButton rb, int index) {
					if(index==0){
						state="1";
					}else{
						state="-1";
					}
					initData();
			}
		});*/
	}
	
	protected void reviewMessageList(List<MLCommentData> datas) {
		_commentAdapter.setData(datas);
	}
	/**
	  * @description   返回
	  *
	  * @author marcello
	 */
	@OnClick(R.id.top_back)
	public void backOnClick(View view){
		((MLAuxiliaryActivity)_context).onBackPressed();
	}
	
	@OnClick(R.id.btn_comment)
	public void commentOnClick(View view){
		MLDealListData _data = new MLDealListData();
		_data.userId = id;
		
		_event.onEvent(_data, MLConstants.MY_DEAL_COMMENT);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==1){
			initData();
		}
		
	}
	 private static final int HTTP_RESPONSE_MESSSAGE_LIST = 1;
	 private static final int HTTP_RESPONSE_MESSSAGE_REPLY = 2;
	 private static final int HTTP_RESPONSE_MESSSAGE_TEST = 3;
	 private static final int HTTP_RESPONSE_MESSSAGE_REPORT = 4;
	 private static final int HTTP_RESPONSE_MESSSAGE_PAGE = 5;
	 
	 
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
	            //获取消息列表
	            case HTTP_RESPONSE_MESSSAGE_LIST:{
	            	MLCommentResponse ret = (MLCommentResponse) msg.obj;
	            	_messageData =ret.datas;
      		if(ret.state.equalsIgnoreCase("1")){
      			if(ret.datas.size()>0){
      				BaseApplication._messageLastId = ret.datas.get(0).id;
      			}
      			reviewMessageList(ret.datas);
          	}else{
          		showMessageError("获取评论列表失败!");
          	}
      		_pullToRefreshLv.onHeaderRefreshFinish();
	            	break;
	            }
	            //获取分页消息
	            case HTTP_RESPONSE_MESSSAGE_PAGE:{
	            	MLCommentResponse ret = (MLCommentResponse) msg.obj;
	            	if(ret.state.equalsIgnoreCase("1")){
	            		if(ret.datas.size()==0){
	            			_pullToRefreshLv.onFooterLoadFinish();
	            			break;  
	            		}
	            		_messageData.addAll(ret.datas);
	          			reviewMessageList(_messageData);
	              	}else{
	              		showMessageError("获取评论列表失败!");
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
