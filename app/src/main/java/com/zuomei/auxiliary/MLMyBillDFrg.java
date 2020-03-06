package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import com.zuomei.model.MLDealListData;
import com.zuomei.model.MLDealListResponse;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLMyServices;

import java.util.List;

import cn.ml.base.utils.IEvent;

/**
 * 账单-汽修厂
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyBillDFrg extends BaseFragment{

	public static MLMyBillDFrg INSTANCE =null;
	
	public static MLMyBillDFrg instance(){
	//	if(INSTANCE==null){
			INSTANCE = new MLMyBillDFrg();
	//	}
		return INSTANCE;
	}
	@ViewInject(R.id.message_lv)
	private AbPullToRefreshView _pullToRefreshLv;
	@ViewInject(R.id.bill_lv)
	private ListView _billLv;

	private Context _context;
	private MLMyBillDAdapter _adapter;
	public List<MLDealListData> _datas;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_bill_d, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		
		initView();
		initData();
		return view;
	}
	
	
	private void initView() {
		_adapter = new MLMyBillDAdapter(_context);
		_billLv.setAdapter(_adapter);
		
		_pullToRefreshLv.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			
			@Override
			public void onHeaderRefresh(AbPullToRefreshView view) {
				initData();
			}
		});;
		_pullToRefreshLv.setOnFooterLoadListener(new OnFooterLoadListener() {
			
			@Override
			public void onFooterLoad(AbPullToRefreshView view) {
				pageData();
			}
		});  
		
	    
	    _billLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MLLogin user = BaseApplication.getInstance().get_user();
				MLDealListData data = (MLDealListData) parent.getAdapter().getItem(position);
				//交易状态1交易中2成功3退货处理中、4卖家同意退款、5卖家拒绝退款、6退款成功
				if(data.dealType.equalsIgnoreCase("1")&&user.isDepot){
					refund(data);
				}else if(data.dealType.equalsIgnoreCase("5")&&user.isDepot){
					refund(data);
				}else if(data.dealType.equalsIgnoreCase("2")&&user.isDepot){
					//交易成功
				//	_event.onEvent(data, MLConstants.MY_DEAL_COMMENT);
				}else if(data.dealType.equalsIgnoreCase("3")&&!user.isDepot){
					//退款处理中
					companyRefund(data);			
				}
				
			}
		});
	}

	private void initData(){
		ZMRequestParams params = new ZMRequestParams();
//		MLLogin user = BaseApplication._user;
		MLLogin user = 	((BaseApplication)getActivity().getApplication()).get_user();
		if(user.isDepot){
			params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,user.Id);
		}else{
			params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,user.Id);
		}
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_DEAL_LIST, null, params, _handler, HTTP_RESPONSE_STOCK_LIST,  MLMyServices.getInstance());
	    loadDataWithMessage(_context, null, message1);
	}
	
	private void pageData() {
		String lastId = _datas.get(_datas.size()-1).userId+"";
		ZMRequestParams params = new ZMRequestParams();
//		MLLogin user = BaseApplication._user;
		MLLogin user = 	((BaseApplication)getActivity().getApplication()).get_user();
		if(user.isDepot){
			params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,user.Id);
		}else{
			params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,user.Id);
		}
		params.addParameter(MLConstants.PARAM_MESSAGE_LASTID,lastId);
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_DEAL_LIST, null, params, _handler, HTTP_RESPONSE_STOCK_PAGE,  MLMyServices.getInstance());
	    loadDataWithMessage(_context, null, message1);
	}
	
	private void reviewDealList(List<MLDealListData>  data){
		_adapter.setData(data);
	}
	
	private void refund(final MLDealListData data){
		  Builder builder = new Builder(_context);
		  builder.setMessage("是否选择退货").setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				requestRefund(data);
			}
		}).setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
          builder.show();
	}
	
	
	private void companyRefund(final MLDealListData data){
		  Builder builder = new Builder(_context);
		  builder.setMessage("是否同意退货").setPositiveButton("同意", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				requestCompanyRefund(data,"1");
			}
		}).setNegativeButton("拒绝", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				requestCompanyRefund(data,"0");
			}
		});
        builder.show();
	}
	
	private void requestRefund(MLDealListData data){
		ZMRequestParams params = new ZMRequestParams();
		params.addParameter("dealId",data.id);
		params.addParameter("isCompany","0");
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_DEAL_REFUND, null, params, _handler, HTTP_RESPONSE_REFUND,  MLMyServices.getInstance());
	    loadDataWithMessage(_context, null, message1);
	}
	
	//商家退款
	private void requestCompanyRefund(MLDealListData data,String agree){
		ZMRequestParams params = new ZMRequestParams();
		params.addParameter("dealId",data.id);
		params.addParameter("isCompany","1");
		params.addParameter("isAgree",agree);
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_DEAL_REFUND, null, params, _handler, HTTP_RESPONSE_COMPANY_REFUND,  MLMyServices.getInstance());
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
	
	 private static final int HTTP_RESPONSE_STOCK_LIST= 0;
	 private static final int HTTP_RESPONSE_STOCK_PAGE= 1;
	 private static final int HTTP_RESPONSE_REFUND= 2;
	 private static final int HTTP_RESPONSE_COMPANY_REFUND= 3;
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
	            case HTTP_RESPONSE_STOCK_LIST:{
	            	MLDealListResponse ret = (MLDealListResponse) msg.obj;
        		if(ret.state.equalsIgnoreCase("1")){
        			_datas = ret.datas;
        			_adapter.setData(ret.datas);
            	}else{
            	}
        		_pullToRefreshLv.onHeaderRefreshFinish();
	            	break;
	            }
	            
	            case HTTP_RESPONSE_STOCK_PAGE:{
	            	MLDealListResponse ret = (MLDealListResponse) msg.obj;
	            	if(ret.state.equalsIgnoreCase("1")){
	            		if(ret.datas.size()==0){
	            			_pullToRefreshLv.onFooterLoadFinish();
	            			break;  
	            		}
	            		_datas.addAll(ret.datas);
	          			reviewDealList(_datas);
	              	}else{
	              		showMessageError("获取交易列表失败!");
	              	}
	          		_pullToRefreshLv.onFooterLoadFinish();
	            	break;
	            }
	            case HTTP_RESPONSE_REFUND:{
	            	MLRegister ret = (MLRegister) msg.obj;
	            	if(ret.state.equalsIgnoreCase("1")){
	            		showMessageSuccess("提交退款申请成功");
	            		initData();
	            	}else{
	            		showMessageError("提交退款申请失败");
	            	}
	            	break;
	            }
	            case HTTP_RESPONSE_COMPANY_REFUND:{
	            	MLRegister ret = (MLRegister) msg.obj;
	            	if(ret.state.equalsIgnoreCase("1")){
	            		showMessageSuccess("操作成功");
	            		initData();
	            	}else{
	            		showMessageError("操作失败");
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
