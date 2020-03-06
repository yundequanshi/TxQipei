package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ab.view.pullview.AbPullToRefreshView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseFragment;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLAccidentQuotesData;
import com.zuomei.model.MLMyPartBusinessData;
import com.zuomei.model.MLMyPartDetailResponse;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLPartServices;
import com.zuomei.utils.MLToolUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.ml.base.utils.IEvent;

/**
 * 汽修厂-商家报价详情
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLDepotOfferDetail extends BaseFragment{

	public static MLDepotOfferDetail INSTANCE =null;
	
	static MLMyPartBusinessData data;
	public static MLDepotOfferDetail instance(Object obj){
			INSTANCE = new MLDepotOfferDetail();
			data = (MLMyPartBusinessData) obj;
		return INSTANCE;
	}
	
	@ViewInject(R.id.tv_remark)
	private EditText remarkTv;
	
	@ViewInject(R.id._refressview)
	private AbPullToRefreshView _refreshView;

	@ViewInject(R.id.mListView)
	private ListView mList;
	
	@ViewInject(R.id.tv_label)
	private TextView mlabelTv;
	
	@ViewInject(R.id.btn_list)
	private Button mUseBtn;
	
	private List<MLAccidentQuotesData> _datas;
	private MLDepotOfferDeatilAdapter mAdapter;
	private Context _context;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.depot_offer_detail, null);
		ViewUtils.inject(this,view);
		_context = inflater.getContext();
		
		mAdapter = new MLDepotOfferDeatilAdapter(_context);
		mList.setAdapter(mAdapter);
		initData();
		
		if(!data.state.contains("报价")){
			mUseBtn.setVisibility(View.GONE);
		}
		
	/*	_refreshView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			
			@Override
			public void onHeaderRefresh(AbPullToRefreshView view) {
				initData();
			}
		});
		_refreshView.setOnFooterLoadListener(new OnFooterLoadListener() {
			
			@Override
			public void onFooterLoad(AbPullToRefreshView view) {
				// TODO Auto-generated method stub
				pageData();
			}
		});  */
		
		
		return view;
	}
	
	private void initData(){
		ZMRequestParams params = new ZMRequestParams();
		  JSONObject jo = new JSONObject();
		  try {
			jo.put("id", data.id);
		} catch (JSONException e) {
			return;
		}
			params.addParameter("data",jo.toString());
		
	    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.DEPOT_PART_BUSINESS_DETAIL, null, params, _handler,HTTP_RESPONSE_LIST, MLPartServices.getInstance());
	    loadData(_context, message2);
	}
	
	/*private void pageData() {
		
		ZMRequestParams params = new ZMRequestParams();
		  JSONObject jo = new JSONObject();
		  try {
			jo.put("id", id);
			jo.put("lastId", _datas.get(_datas.size()-1).+"");
			
		} catch (JSONException e) {
			return;
		}
			params.addParameter("data",jo.toString());
		
	    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.DEPOT_PART_BUSINESS_DETAIL, null, params, _handler,HTTP_RESPONSE_PAGE, MLPartServices.getInstance());
	    loadData(_context, message2);
	}*/
	
	
	@OnClick(R.id.btn_list)
	public void useOnClick(View view){
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT);
		  builder.setTitle("提示");
		  builder.setMessage("是否使用该商家报价？");
		  builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				use();
			}
		});
		  builder.setPositiveButton("取消", null);
		  builder.show();

	
	}
	
	private void use(){
		ZMRequestParams params = new ZMRequestParams();
		  JSONObject jo = new JSONObject();
		  try {
			jo.put("id", data.id);
		} catch (JSONException e) {
			return;
		}
			params.addParameter("data",jo.toString());
		
	    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.DEPOT_PART_BUSINESS_USE, null, params, _handler,HTTP_RESPONSE_USE, MLPartServices.getInstance());
	    loadData(_context, message2);
	}
	
	 private static final int HTTP_RESPONSE_LIST = 1;
	 private static final int HTTP_RESPONSE_PAGE = 2;
	 private static final int HTTP_RESPONSE_USE = 3;
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
	            case HTTP_RESPONSE_LIST:{
	            	MLMyPartDetailResponse ret = (MLMyPartDetailResponse) msg.obj;
        		if(ret.state.equalsIgnoreCase("1")){
        			_datas = ret.datas.accidentQuotes;
        			mAdapter.setData(ret.datas.accidentQuotes);
        			MLToolUtil.setListViewHeightBasedOnChildren(mList);
        			//备注
        			if(MLToolUtil.isNull(ret.datas.remark)){
        				remarkTv.setVisibility(View.GONE);
        				mlabelTv.setVisibility(View.GONE);
        			}else{
        				remarkTv.setText(ret.datas.remark);
        			}
            	}else{
            		showMessage("获取报价列表失败!");
            	}
        		if(ret.datas!=null&&ret.datas.accidentQuotes.size()<=20){
   				//_refreshView.setLoadMoreEnable(false);
   			}else{
   			//	_refreshView.setLoadMoreEnable(true);
   			}
        	//	_refreshView.onHeaderRefreshFinish();
	            	break;
	            }
	            case HTTP_RESPONSE_PAGE:{
	            	MLMyPartDetailResponse ret = (MLMyPartDetailResponse) msg.obj;
               	if(ret.state.equalsIgnoreCase("1")){
               		if(ret.datas.accidentQuotes.size()!=0){
               			_datas.addAll(ret.datas.accidentQuotes);
               			mAdapter.setData(_datas);
               			MLToolUtil.setListViewHeightBasedOnChildren(mList);
               		}
               	}else{
               		showMessage("获取报价列表失败!");
               	}
               	_refreshView.onFooterLoadFinish();
	            	break;
	            }
	            
	            case HTTP_RESPONSE_USE:{
	             	MLRegister ret = (MLRegister) msg.obj;
            		if(ret.datas){
            			showMessageSuccess("操作成功!");
            		//	_event.onEvent(null, MLConstants.MY_BILL2_BUSINESS_LIST);
            			((MLAuxiliaryActivity)_context).finish();
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
	
	/**
	  * @description   返回
	  *
	  * @author marcello
	 */
	@OnClick(R.id.top_back)
	public void backOnClick(View view){
		((MLAuxiliaryActivity)_context).finish();
	}
	
	private IEvent<Object> _event;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_event = (IEvent<Object>) activity;
	}

}
