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
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLMyRepairData;
import com.zuomei.model.MLMyRepairResponse;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLMyServices;

import java.util.List;

import cn.ml.base.utils.IEvent;

/**
 * 汽修列表
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyRepairFrg extends BaseFragment{

	public static MLMyRepairFrg INSTANCE =null;
	
	public static MLMyRepairFrg instance(){
		//if(INSTANCE==null){
			INSTANCE = new MLMyRepairFrg();
		//}
		return INSTANCE;
	}
	
	
	@ViewInject(R.id.repair_lv)
	private AbPullToRefreshView _pullToRefreshLv;
	@ViewInject(R.id.mListView)
	private ListView _repairLv;
	
	private Context _context;
	private MLMyRepairAdapter _adapter;
	public List<MLMyRepairData> _repairDatas;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_repair, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		
		initView();
		return view;
	}
	
	
	private void initView() {
		_adapter = new MLMyRepairAdapter(_context);
		_repairLv.setAdapter(_adapter);
		_repairLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				MLMyRepairData data = (MLMyRepairData) arg0.getAdapter().getItem(arg2);
				_event.onEvent(data, MLConstants.MY_PRODUCT_DETAIL);
			}
		});
		_repairLv.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				final MLMyRepairData data = (MLMyRepairData) arg0.getAdapter().getItem(arg2);
				  Builder builder = new Builder(_context);
	                builder.setItems( new String[] { "删除" }, new DialogInterface.OnClickListener()
	                { 
	                    public void onClick(DialogInterface arg0, int arg1)
	                    {
	                        arg0.dismiss();
	                        delRepair(data.id);
	                    }
	                }).setTitle("请选择");
	                builder.show();
				return true;
			}
		});
	_pullToRefreshLv.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			
			@Override
			public void onHeaderRefresh(AbPullToRefreshView view) {
				initData();
			}
		});
		_pullToRefreshLv.setOnFooterLoadListener(new OnFooterLoadListener() {
			
			@Override
			public void onFooterLoad(AbPullToRefreshView view) {
				// TODO Auto-generated method stub
				pageData();
			}
		});  
		initData();
	}
	  private void 	initData(){
			
			MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
			ZMRequestParams catalogParam = new ZMRequestParams();
			catalogParam.addParameter(MLConstants.PARAM_MY_DEPTID1,user.Id);

		    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_REPAIR_LIST, null, catalogParam, _handler, HTTP_RESPONSE_REPAIR_LIST, MLMyServices.getInstance());
		    loadDataWithMessage(_context, null, message1);
		}
	  private void 	pageData(){
			MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
			ZMRequestParams catalogParam = new ZMRequestParams();
			catalogParam.addParameter(MLConstants.PARAM_MY_DEPTID1,user.Id);
			String lastId = _repairDatas.get(_repairDatas.size()-1).id+"";
			catalogParam.addParameter(MLConstants.PARAM_MESSAGE_LASTID,lastId);
		    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_REPAIR_LIST, null, catalogParam, _handler, HTTP_RESPONSE_REPAIR_PAGE, MLMyServices.getInstance());
		    loadDataWithMessage(_context, null, message1);
		}
	
	private void delRepair(String id){
		ZMRequestParams catalogParam = new ZMRequestParams();
		catalogParam.addParameter(MLConstants.PARAM_MY_GOODID,id);
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_REPAIR_DEL, null, catalogParam, _handler, HTTP_RESPONSE_REPAIR_ADD,  MLMyServices.getInstance());
	    loadDataWithMessage(_context, null, message1);
	}
	

	/**
	  * @description  增加进货信息
	  *
	  * @author marcello
	 */
	@OnClick(R.id.repair_ib_add)
	public void addOnClick(View view){
		_event.onEvent(null, MLConstants.MY_REPAIR_ADD);
	}
	
	/**
	  * @description  返回
	  *
	  * @author marcello
	 */
	@OnClick(R.id.home_top_back)
	public void backOnClick(View view){
		((MLAuxiliaryActivity)_context).finish();
	}
	

	 private static final int HTTP_RESPONSE_REPAIR_LIST= 0;
	 private static final int HTTP_RESPONSE_REPAIR_ADD= 1;
	 private static final int HTTP_RESPONSE_REPAIR_PAGE= 2;
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
	            case HTTP_RESPONSE_REPAIR_LIST:{
	            	MLMyRepairResponse ret = (MLMyRepairResponse) msg.obj;
     		if(ret.state.equalsIgnoreCase("1")){
     			_repairDatas = ret.datas;
     			_adapter.setData(ret.datas);
         	}else{
         		showMessageError("获取汽修记录失败!");
         	}
     		_pullToRefreshLv.onHeaderRefreshFinish();
	            	break;
	            }
	            case HTTP_RESPONSE_REPAIR_ADD:{
	            	MLRegister ret = (MLRegister) msg.obj;
         		if(ret.state.equalsIgnoreCase("1")){
         			//_adapter.setData(ret.datas);
         			initData();
             	}else{
             		showMessageError("删除失败!");
             	}
	            	break;
	            }
	            case HTTP_RESPONSE_REPAIR_PAGE:{
	            	MLMyRepairResponse ret = (MLMyRepairResponse) msg.obj;
	         		if(ret.state.equalsIgnoreCase("1")){
	         			_repairDatas.addAll(ret.datas);
	         			_adapter.setData(_repairDatas);
	             	}else{
	             		showMessageError("获取汽修记录失败!");
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
