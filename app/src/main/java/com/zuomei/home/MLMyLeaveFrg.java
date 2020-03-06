package com.zuomei.home;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.adapter.TXMyLeaveAdapter;
import com.txsh.model.TXEventModel;
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLeaveData;
import com.zuomei.model.MLLeaveResponse;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLLeaveServices;
import com.zuomei.utils.MLToolUtil;

import java.util.List;

import cn.ml.base.utils.IEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 我的二手件
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyLeaveFrg extends BaseFragment{

	public static MLMyLeaveFrg INSTANCE =null;
	
	public static MLMyLeaveFrg instance(){
			INSTANCE = new MLMyLeaveFrg();
		return INSTANCE;
	}
	@ViewInject(R.id.accident_lv_car)
	private AbPullToRefreshView _pullToRefreshLv;
	@ViewInject(R.id.mListView)
	private ListView _carLv;
	
	@ViewInject(R.id.accident_et_search)
	private EditText _searchEt;
	
	@ViewInject(R.id.accident_iv_search)
	private ImageView _searchBtn;
	
	public List<MLLeaveData> _accidentDatas;
	
	private TXMyLeaveAdapter _carAdapter;
	private MLLeaveData _data ;
	private String keyWord;
	private Context _context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	private MLLogin mUser;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_leave, null);
		ViewUtils.inject(this,view);
		mUser = BaseApplication.getInstance().get_user();
		_context = inflater.getContext();
		
		initView();
		initData();
		return view;   
	}   
	
	
	private void initData() {
		/* ZMRequestParams params = new ZMRequestParams();
	      params.addParameter(MLConstants.PARAM_REGISTER_PWD,pwd);*/
		MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
		  ZMRequestParams params = new ZMRequestParams();
			if(mUser.isDepot){
				params.addParameter("depotId",user.Id);
			}else{
				params.addParameter("companyId",user.Id);
			}

			
	      ZMHttpRequestMessage message = new ZMHttpRequestMessage(RequestType.LEAVE_MY_LIST, null, params, _handler, HTTP_RESPONSE_LEAVE_LIST, MLLeaveServices.getInstance());
	      loadDataWithMessage(_context, null, message);
		
	}


	private void initView() {
		// TODO Auto-generated method stub
		_carLv.setSelector(getResources().getDrawable(R.drawable.message_list_selector));
		_carAdapter = new TXMyLeaveAdapter(_context,R.layout.accident_car_item);
		_carLv.setAdapter(_carAdapter);
		_pullToRefreshLv.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			@Override
			public void onHeaderRefresh(AbPullToRefreshView view) {
				if(MLToolUtil.isNull(keyWord)){
					initData();
				}else{
		//			search();
				}
			}
		});
		_pullToRefreshLv.setOnFooterLoadListener(new OnFooterLoadListener() {
			@Override
			public void onFooterLoad(AbPullToRefreshView view) {
				pageData();	
			}
		});
		_carLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				//	_pullToRefreshLv.onRefreshComplete()
//				MLLeaveData data = (MLLeaveData) arg0.getAdapter().getItem(arg2);
			//	starActivity(MLConstants.ACCIDENT_DETAIL);

//				startAct(MLMyLeaveFrg.this, MLIncidentDetailAty.class,data);
			/*	Intent intent = new Intent();
				intent.setClass(_context, MLAuxiliaryActivity.class);
				intent.putExtra("data", MLConstants.MY_LEAVE_DETAIL);
				intent.putExtra("obj", data);
				startActivity(intent);*/
				
				MLLeaveData data = (MLLeaveData) arg0.getAdapter().getItem(arg2);
				Intent intent = new Intent();
				intent.setClass(_context, MLAuxiliaryActivity.class);
				intent.putExtra("data", MLConstants.MY_LEAVE_DETAIL);
				intent.putExtra("obj", data);
				startActivity(intent);
			}
		});
		
		_carLv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				_data= (MLLeaveData) parent.getAdapter().getItem(position);
				Builder builder = new Builder(_context, AlertDialog.THEME_HOLO_LIGHT);
				String s[] = {"已售","删除"};
				builder.setItems(s, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						verify(which);
					}
				});
				builder.setTitle("操作");
				 builder.show();
				return true;
			}
		});
	}

	
	@OnClick(R.id.accident_ib_add)
	public void addOnClick(View view){
		Intent intent = new Intent();
		intent.setClass(_context, MLAuxiliaryActivity.class);
		intent.putExtra("data", MLConstants.MY_LEAVE_ADD1);
		startActivityForResult(intent, 1);
	}
	
	@OnClick(R.id.accident_iv_search)
	public void searchOnClick(View view){
		keyWord = _searchEt.getText().toString();
		if(MLToolUtil.isNull(keyWord)){
			initData();
		}else{
		//	search();
		}
	}
	@OnClick(R.id.top_btn_left)
	public void top_btn_leftOnClick(View view){
		getActivity().finish();
	}
	
/*	public void search(){
		ZMRequestParams params = new ZMRequestParams();
	      params.addParameter(MLConstants.PARAM_MY_ACCIDENT,keyWord);
	      ZMHttpRequestMessage message = new ZMHttpRequestMessage(RequestType.AACIDENT_LIST, null, params, _handler, HTTP_RESPONSE_ACCIDENT_LIST, MLAccidentServices.getInstance());
	      loadDataWithMessage(_context, null, message);
	}*/
	public void pageData(){
		MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
		  ZMRequestParams params = new ZMRequestParams();
		if(mUser.isDepot){
			params.addParameter("depotId",user.Id);
		}else{
			params.addParameter("companyId",user.Id);
		}
		
/*		if(!MLToolUtil.isNull(keyWord)){
			params.addParameter(MLConstants.PARAM_MY_ACCIDENT,keyWord);
		}*/
	  	String lastId = _accidentDatas.get(_accidentDatas.size()-1).info.id+"";
	  	params.addParameter(MLConstants.PARAM_MESSAGE_LASTID,lastId);
	      ZMHttpRequestMessage message = new ZMHttpRequestMessage(RequestType.LEAVE_MY_LIST, null, params, _handler, HTTP_RESPONSE_LEAVE_PAGE, MLLeaveServices.getInstance());
	      loadDataWithMessage(_context, null, message);
	}
	
	private String param="";
	private void verify(int postion){
		String state = "";
		if(postion==0){
			state="确认将拆车件改为已售状态";
			param="2";
		}else{
			state="确认删除?";
			param="1";
		}
		Builder builder = new Builder(_context, AlertDialog.THEME_HOLO_LIGHT);
		builder.setMessage(state);
		builder.setTitle("操作");
		  builder.setNegativeButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				request();
			}
		});
		  builder.setPositiveButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		 builder.show();
	}
	
	private void request(){
		  ZMRequestParams params = new ZMRequestParams();
		  	params.addParameter("id",_data.info.id);
		  	params.addParameter("state", param);
		      ZMHttpRequestMessage message = new ZMHttpRequestMessage(RequestType.LEAVE_DEL, null, params, _handler, HTTP_RESPONSE_ACCIDENT_STATE, MLLeaveServices.getInstance());
		      loadDataWithMessage(_context, null, message);
	}

	/**
	 * 删除和已售操作
	 * @param data
	 */
	@Subscribe
	public void action(TXEventModel data){
		_data = (MLLeaveData) data.obj;
		if(data.type==MLConstants.EVENT_PARAM_LEAVE_SELL){
			verify(0);
		}else if(data.type==MLConstants.EVENT_PARAM_LEAVE_DEL){
			verify(1);
		}
	}

	private void starActivity(int position){
		Intent intent = new Intent();
		intent.setClass(_context, MLAuxiliaryActivity.class);
		intent.putExtra("data", position);
		startActivity(intent);
		/*((MLHomeActivity)_context).overridePendingTransition(android.R.anim.slide_in_left,    
				android.R.anim.slide_out_right);  */
	}
	 private static final int HTTP_RESPONSE_LEAVE_LIST = 0;
	 private static final int HTTP_RESPONSE_LEAVE_PAGE = 1;
	 private static final int HTTP_RESPONSE_ACCIDENT_STATE = 2;
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
	                case HTTP_RESPONSE_LEAVE_LIST:{
	                	MLLeaveResponse ret = (MLLeaveResponse) msg.obj;
	                	if(ret.state.equalsIgnoreCase("1")&&ret.datas!=null){
	                		_accidentDatas = ret.datas;
	                		_carAdapter.setData(ret.datas);
	                	}else{
	                		showMessageError("获取事故车列表失败!");
	                	}
	                	_pullToRefreshLv.onHeaderRefreshFinish();
	                    break;
	                }
	                case HTTP_RESPONSE_LEAVE_PAGE:{
	                	MLLeaveResponse ret = (MLLeaveResponse) msg.obj;
	                	if(ret.state.equalsIgnoreCase("1")&&ret.datas!=null){
	                		_accidentDatas.addAll(ret.datas);
	                		_carAdapter.setData(_accidentDatas);
	                	}else{
	                		showMessageError("获取二手件列表失败!");
	                	}
	                	_pullToRefreshLv.onFooterLoadFinish();
	                	break;
	                }
	                case HTTP_RESPONSE_ACCIDENT_STATE:{
	                	
	                	MLRegister ret = (MLRegister) msg.obj;
	                	if(ret.state.equalsIgnoreCase("1")&&ret.datas){
	                			initData();
	                		showMessageSuccess("操作成功!");
	                	}else{
	                		showMessageError("操作失败!");
	                	}
	                	break;
	                }
	                default:
	                    break;
	            }
	        }
	    };
	    
	    
	@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			if(resultCode==MLConstants.RESULT_ACCEIDENT_ADD){
				initData();
			}
		}
	private IEvent<Object> _event;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_event = (IEvent<Object>) activity;
	}
}
