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
import com.zuomei.model.MLMyPartBusinessMagData;
import com.zuomei.model.MLMyPartBusinessMagResponse;
import com.zuomei.services.MLPartServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.ml.base.utils.IEvent;

/**
 * 商家-报价管理
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyPartOffer extends BaseFragment{

	public static MLMyPartOffer INSTANCE =null;
	
	public static MLMyPartOffer instance(){
			INSTANCE = new MLMyPartOffer();
		return INSTANCE;
	}

	@ViewInject(R.id.message_lv)
	private AbPullToRefreshView _pullToRefreshLv;
	
	@ViewInject(R.id.mListView)
	private ListView mList;
	private 	MLLogin user ;
	private MLMyPartOfferAdapter mAdapter;
	private Context _context;
	private List<MLMyPartBusinessMagData> _data;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_accident_part, null);
		ViewUtils.inject(this,view);
		_context = inflater.getContext();
		 user = ((BaseApplication)getActivity().getApplication()).get_user();
		
		mAdapter = new MLMyPartOfferAdapter(_context);
		mList.setAdapter(mAdapter);
		mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				toActivity(_context,  MLConstants.MY_PART_OFFER_PRICE, _data.get(arg2));
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
			jo.put("companyId", user.Id);
		} catch (JSONException e) {
			return;
		}
			params.addParameter("data",jo.toString());
		
	    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.BUS_PART_LIST, null, params, _handler,HTTP_RESPONSE_LIST, MLPartServices.getInstance());
	    loadData(_context, message2);
	}
	
	
	private void pageData() {
		  //获取互动列表
			ZMRequestParams params = new ZMRequestParams();
			  JSONObject jo = new JSONObject();
			  try {
				jo.put("companyId", user.Id);
				jo.put("lastId", _data.get(_data.size()-1).id+"");
			} catch (JSONException e) {
				return;
			}
				params.addParameter("data",jo.toString());
			
		    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.BUS_PART_LIST, null, params, _handler,HTTP_RESPONSE_LIST_PAGE, MLPartServices.getInstance());
		    loadData(_context, message2);
	}
	
	private static final int HTTP_RESPONSE_LIST= 0;
	private static final int HTTP_RESPONSE_LIST_PAGE= 1;
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
            	MLMyPartBusinessMagResponse ret = (MLMyPartBusinessMagResponse) msg.obj;
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
            	MLMyPartBusinessMagResponse ret = (MLMyPartBusinessMagResponse) msg.obj;
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
