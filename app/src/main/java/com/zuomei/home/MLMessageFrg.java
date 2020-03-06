package com.zuomei.home;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.login.MLBusinessDetailMapAct;
import com.zuomei.model.MLHomeBusiness1Data;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLMessageCommentData;
import com.zuomei.model.MLMessageData;
import com.zuomei.model.MLMessageListResponse;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLMessageServices;
import com.zuomei.utils.MLToolUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.ml.base.utils.IEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 互动
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMessageFrg extends BaseFragment{

	public static MLMessageFrg INSTANCE =null;
	
	public static MLMessageFrg instance(){
			INSTANCE = new MLMessageFrg();
		return INSTANCE;
	}
	
	/*@ViewInject(R.id.message_lv)
	private PullToRefreshListView _pullToRefreshLv;*/
	@ViewInject(R.id.message_lv)
	private AbPullToRefreshView _pullToRefreshLv;
	@ViewInject(R.id.mListView)
	private ListView _messageLv;

	
	@ViewInject(R.id.rl_root)
	private RelativeLayout _root;
	
	@ViewInject(R.id.message_rl_reply)
	private RelativeLayout _replyLy;
	
	@ViewInject(R.id.message_et_reply)
	private EditText _replyEt;
	
	@ViewInject(R.id.message_btn_reply)
	private Button _replyBtn;

	@ViewInject(R.id.message_ib_add)
	private ImageButton _addBtn;
	
	/**互动消息列表*/
	private List<MLMessageData> _messageData;
	  
	private int _replyPositiion;
	private MLMessageAdapter _messageAdapter;
	private Context _context;
	List<String[]> message ;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_message, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		
		initData();
		initView();
		return view;
	}

	private void initData() {
		ZMRequestParams param = new ZMRequestParams();
		param.addParameter("cityId",BaseApplication._currentCity);
		 ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MESSAGE_LIST, null, param, _handler, HTTP_RESPONSE_MESSSAGE_LIST, MLMessageServices.getInstance());
		    loadDataWithMessage(_context, null, message1);
	}
	private void pageData() {
		if(_messageData==null)return;
		  //获取互动列表
		ZMRequestParams param = new ZMRequestParams();
		param.addParameter("cityId",BaseApplication._currentCity);
		String lastId = _messageData.get(_messageData.size()-1).info.id+"";
		//param.addParameter("pageNum","2");
		param.addParameter(MLConstants.PARAM_MESSAGE_LASTID,lastId);
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MESSAGE_LIST, null, param, _handler, HTTP_RESPONSE_MESSSAGE_PAGE, MLMessageServices.getInstance());
	    loadDataWithMessage(_context, null, message1);
	}
    
	//举报
	private void report(){
		ZMRequestParams params = new ZMRequestParams();
		MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
		if(user.isDepot){
			params.addParameter(MLConstants.PARAM_HOME_DEPORT,user.Id);
		}else{
			params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,user.Id);
		}
		params.addParameter(MLConstants.PARAM_MESSAGE_ITID,_messageData.get(_replyPositiion).info.id);
		
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MESSAGE_REPORT, null, params, _handler, HTTP_RESPONSE_MESSSAGE_REPORT, MLMessageServices.getInstance());
	    loadDataWithMessage(_context, null, message1);
	}
	
	private void initView() {
	//	_messageLv = _pullToRefreshLv.getRefreshableView();
		_messageAdapter = new MLMessageAdapter(_context,_updateHandler);
		_messageLv.setAdapter(_messageAdapter);
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
		_messageLv.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				_replyLy.setVisibility(View.GONE);
			}
			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				
			}
		});
//		_messageLv.setSelector(getResources().getDrawable(R.drawable.message_list_selector));
		_messageLv.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				_pullToRefreshLv.onHeaderRefreshFinish();
				_pullToRefreshLv.onFooterLoadFinish();
				  Builder builder = new Builder(_context);
	                builder.setItems( new String[] { "举报" }, new DialogInterface.OnClickListener()
	                {
	                    public void onClick(DialogInterface arg0, int arg1)
	                    {
	                    	report();
	                        arg0.dismiss();
	                    }
	                }).setTitle("请选择");
	                builder.show();
				return false;
			}
		});
	}
	
	protected void reviewMessageList(List<MLMessageData> datas) {
		_messageAdapter.setData(datas);
	}


	@OnClick(R.id.message_btn_reply)
	public void replyOnClick(View view){
		
		String content = _replyEt.getText().toString();
		   content = content.replaceAll("\r|\n", "");
		if(MLToolUtil.isNull(content)){
			showMessage("评论内容不能为空!");
			return;
		}
		
		String name =  ((BaseApplication)getActivity().getApplication()).get_user().name;
		MLMessageCommentData comment = new MLMessageCommentData();
		/*comment.userName=name;
		comment.content= content;*/
		comment.userName=name;
		comment.content= content;
		
		ZMRequestParams param = new ZMRequestParams();
		
		MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
		if(user.isDepot){
			param.addParameter("depotId",user.Id);
		}else{
			param.addParameter("companyId",user.Id);
		}
		param.addParameter(MLConstants.PARAM_MESSAGE_CONTENT,content);
		param.addParameter(MLConstants.PARAM_MESSAGE_USERNAME,name);
		param.addParameter(MLConstants.PARAM_MESSAGE_ITID,_messageData.get(_replyPositiion).info.id);
		
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MESSAGE_REPLY, null, param, _handler, HTTP_RESPONSE_MESSSAGE_REPLY, MLMessageServices.getInstance());
		loadData(_context, message1);
		
		_messageData.get(_replyPositiion).info.interactionComment.add(comment);
		_messageAdapter.setData(_messageData);
		
		_replyLy.setVisibility(View.GONE);
	}
	/**
	  * @description  发表互动
	  *
	  * @author marcello
	 */
	@OnClick(R.id.message_ib_add)
	public void addOnClick(View view){
		Intent intent = new Intent();
		intent.setClass(_context, MLAuxiliaryActivity.class);
		intent.putExtra("data", MLConstants.MESSAGE_REPLY_ADD);
		//startActivity(intent);
		
		startActivityForResult(intent, 111);
	}
	
	@OnClick(R.id.top_message)
	public void myOnClick(View view){
		toActivity(_context, MLConstants.MY_MESSAGE, null);
	}
	
	@Subscribe
	public void refresh(String s){
		initData();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==1){
			initData();
		}
		
	}

	public Handler _updateHandler  = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			switch (msg.what) {
			case 1:
				//评论
				_replyLy.setVisibility(View.VISIBLE);
				_replyPositiion =  msg.arg1;	
				break;
			case 2:
				//图片
				String path = APIConstants.API_IMAGE+"?id="+_messageData.get(msg.arg1).info.images;
			/*	MLMessagePhotoPop _pop = new MLMessagePhotoPop(getActivity(), path);
				_pop.showAtLocation(_root, Gravity.CENTER, 0, 0); */
				List<String> images = new ArrayList<String>();
				images.add(path);
				MLHomeProductPop _pop = new MLHomeProductPop(getActivity(), images,0);
				_pop.showAtLocation(_root, Gravity.CENTER, 0, 0); 
				break;
			case 3:{
				//头像
				final MLMessageData d = (MLMessageData) msg.obj;
				if(d.userType.equalsIgnoreCase("1")){
					//汽修厂
					Builder builder = new Builder(_context, AlertDialog.THEME_HOLO_LIGHT);
					String s[] = {"查看地图","拨打电话"};
					builder.setItems(s, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//选择了第index项
							if(which==0){
								MLHomeBusiness1Data _business = new MLHomeBusiness1Data();
								_business.lan = d.user.latitude;
								_business.lon = d.user.longitude;
								_business.userName = d.user.depotName;
								_business.phone = d.user.userPhone;
								Intent intent = new Intent(_context,MLBusinessDetailMapAct.class);
								intent.putExtra("obj", (Serializable) _business);
								_context.startActivity(intent);
							}else{
								Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+d.user.userPhone));  
		    	                _context.startActivity(intent); 
							}
							
						}
					});
					 builder.show();

				}

				break;
			}
				
			default:
				break;
			}
			
			
		}
		
	};
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
	            	MLMessageListResponse ret = (MLMessageListResponse) msg.obj;
	            	_messageData =ret.datas;
      		if(ret.state.equalsIgnoreCase("1")){
      			if(ret.datas.size()>0){
      				BaseApplication._messageLastId = ret.datas.get(0).info.id;
      			}
      			reviewMessageList(ret.datas);
          	}else{
          		showMessageError("获取消息列表失败!");
          	}
      		_pullToRefreshLv.onHeaderRefreshFinish();
	            	break;
	            }
	            //获取分页消息
	            case HTTP_RESPONSE_MESSSAGE_PAGE:{
	            	MLMessageListResponse ret = (MLMessageListResponse) msg.obj;
	            	if(ret.state.equalsIgnoreCase("1")){
	            		if(ret.datas.size()==0){
	            			_pullToRefreshLv.onFooterLoadFinish();
	            			break;  
	            		}
	            		_messageData.addAll(ret.datas);
	          			reviewMessageList(_messageData);
	              	}else{
	              		showMessageError("获取消息列表失败!");
	              	}
	          		_pullToRefreshLv.onFooterLoadFinish();
	          		break;
	            }
	            //评论
	            case HTTP_RESPONSE_MESSSAGE_REPLY:{
	            	MLRegister ret = (MLRegister) msg.obj;
	            	if(ret.state.equalsIgnoreCase("1")){
	            		showMessageSuccess("评论成功!");
	              	}else{
	              		showMessageError("评论失败!");
	              	}
	            	_replyEt.setText("");
	            	break;
	            }
	            case HTTP_RESPONSE_MESSSAGE_REPORT:{
	            	MLRegister ret = (MLRegister) msg.obj;
	            	if(ret.state.equalsIgnoreCase("1")){
	            		showMessageSuccess("举报成功!");
	              	}else{
	              		showMessageError("举报失败!");
	              	}
	            	break;
	            }
	            
	                default:
	                    break;
	            }
	        }
	    };
	    
	    
	    
	@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			EventBus.getDefault().unregister(this);
		}

	private IEvent<Object> _event;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_event = (IEvent<Object>) activity;
	}
}
