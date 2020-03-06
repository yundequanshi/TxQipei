package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
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
import com.zuomei.model.MLMyPartOfferMgData;
import com.zuomei.model.MLMyPartOfferMgResponse;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLPartServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.ml.base.utils.IEvent;

/**
 * 汽修厂-我的事故车报价
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyAccidentPart extends BaseFragment{

	public static MLMyAccidentPart INSTANCE =null;
	
	public static MLMyAccidentPart instance(){
			INSTANCE = new MLMyAccidentPart();
		return INSTANCE;
	}

	@ViewInject(R.id.mListView)
	private ListView mList;
	
	@ViewInject(R.id.message_lv)
	private AbPullToRefreshView _pullToRefreshLv;
	
	
	private List<MLMyPartOfferMgData> _data;
	private 	MLLogin user ;
	private MLMyAccidentPartAdapter mAdapter;
	private Context _context;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_accident_part, null);
		ViewUtils.inject(this,view);
		_context = inflater.getContext();
		 user = ((BaseApplication)getActivity().getApplication()).get_user();
		mAdapter = new MLMyAccidentPartAdapter(_context);
		mList.setAdapter(mAdapter);
		mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.setClass(_context, MLAuxiliaryActivity.class);
				intent.putExtra("obj", _data.get(arg2));
				intent.putExtra("data", MLConstants.MY_ACCIDENT_BUSSINESS);
				startActivity(intent);
			}
		});
		mList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				final String id = _data.get(arg2).id;
				Builder builder = new Builder(_context, AlertDialog.THEME_HOLO_LIGHT);
				String s[] = {"删除"};
				builder.setItems(s, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
							del(id);
					}
				});
				builder.setTitle("操作");
				 builder.show();
				
				return true;
			}
		});
		
		initData();
		
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
		
		return view;
	}
	
	private void initData(){
		ZMRequestParams params = new ZMRequestParams();
		  JSONObject jo = new JSONObject();
		  try {
			jo.put("depotUserId", user.Id);
		} catch (JSONException e) {
			return;
		}
			params.addParameter("data",jo.toString());
		
	    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.DEPOT_PART_MG_LIST, null, params, _handler,HTTP_RESPONSE_LIST, MLPartServices.getInstance());
	    loadData(_context, message2);
	}
	
	
	
	private void pageData() {
		
		  //获取互动列表
		ZMRequestParams params = new ZMRequestParams();
		  JSONObject jo = new JSONObject();
		  try {
			jo.put("depotUserId", user.Id);
			jo.put("lastId", _data.get(_data.size()-1).id+"");
		} catch (JSONException e) {
			return;
		}
			params.addParameter("data",jo.toString());
		
	    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.DEPOT_PART_MG_LIST, null, params, _handler,HTTP_RESPONSE_LIST_PAGE, MLPartServices.getInstance());
	    loadData(_context, message2);
	}
	
	private void del(String id){
		ZMRequestParams params = new ZMRequestParams();
		  JSONObject jo = new JSONObject();
		  try {
			jo.put("id", id);
		} catch (JSONException e) {
			return;
		}
			params.addParameter("data",jo.toString());
		
	    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.DEPOT_PART_MG_DEL, null, params, _handler,HTTP_RESPONSE_DEL, MLPartServices.getInstance());
	    loadData(_context, message2);
	}
	
	private static final int HTTP_RESPONSE_LIST= 0;
	private static final int HTTP_RESPONSE_LIST_PAGE= 1;
	private static final int HTTP_RESPONSE_DEL= 2;
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
            case  HTTP_RESPONSE_LIST:{
            	MLMyPartOfferMgResponse ret = (MLMyPartOfferMgResponse) msg.obj;
            	if(ret.state.equalsIgnoreCase("1")){
            		_data =ret.datas;
        			mAdapter.setData(ret.datas);
            	}else{
            		showMessageError("获取列表失败!");
            	}
            	_pullToRefreshLv.onHeaderRefreshFinish();
            	break;
            }
            
            //获取分页消息
            case HTTP_RESPONSE_LIST_PAGE:{
            	MLMyPartOfferMgResponse ret = (MLMyPartOfferMgResponse) msg.obj;
            	if(ret.state.equalsIgnoreCase("1")){
            		if(ret.datas.size()==0){
            			_pullToRefreshLv.onFooterLoadFinish();
            			break;  
            		}
            		_data.addAll(ret.datas);
            		mAdapter.setData(_data);
              	}else{
              		showMessageError("获取列表失败!");
              	}
          		_pullToRefreshLv.onFooterLoadFinish();
          		break;
            }
            
            case HTTP_RESPONSE_DEL:{
            	MLRegister ret = (MLRegister) msg.obj;
        		if(ret.datas){
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
