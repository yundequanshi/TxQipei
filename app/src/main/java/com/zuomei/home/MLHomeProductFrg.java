package com.zuomei.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.auxiliary.MLBusinessInfoAdapter;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLHomeBusiness1Data;
import com.zuomei.model.MLHomeProductData;
import com.zuomei.model.MLHomeProductResponse;
import com.zuomei.model.MLLogin;
import com.zuomei.services.MLHomeServices;
import com.zuomei.utils.MLToolUtil;

import java.util.ArrayList;
import java.util.List;

import cn.ml.base.utils.IEvent;

/**
 * 更多产品
 * @author Marcello
 *
 */
public class MLHomeProductFrg extends BaseFragment{

	public static MLHomeProductFrg INSTANCE =null;
	public static MLHomeBusiness1Data _business;
	public static MLHomeProductFrg instance(Object obj){
		_business = (MLHomeBusiness1Data) obj;
	//	if(INSTANCE==null){
			INSTANCE = new MLHomeProductFrg();
	//	}
		return INSTANCE;
	}
	@ViewInject(R.id.product_gv)
	private GridView _gridView;
	private Context _context;
	@ViewInject(R.id.root)
	private RelativeLayout _root;
	@ViewInject(R.id._refressview)
	private AbPullToRefreshView _refreshView;
	private 	MLBusinessInfoAdapter _productAdapter;
	private 	MLHomeProductPop _pop;
	
	private List<MLHomeProductData> _productDatas;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_product, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		initData();
		initView();
		return view;
	}
	
	private void initView() {
		_productAdapter = new MLBusinessInfoAdapter(_context);
		_gridView.setAdapter(_productAdapter);
		
	
	    
	    _gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				/*MLHomeProductData data = (MLHomeProductData) arg0.getAdapter().getItem(arg2);
				String path = APIConstants.API_IMAGE+"?id="+data.imageId;
				MLMessagePhotoPop _pop = new MLMessagePhotoPop(getActivity(), path);
				_pop.showAtLocation(_root, Gravity.CENTER, 0, 0);   */
				
				_pop = new MLHomeProductPop(getActivity(), getImageUrl(),arg2);
				_pop.showAtLocation(_root, Gravity.CENTER, 0, 0); 
			}
		});
	    
		_refreshView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			@Override
			public void onHeaderRefresh(AbPullToRefreshView view) {
				initData();
			}
		});
		_refreshView.setOnFooterLoadListener(new OnFooterLoadListener() {
			@Override
			public void onFooterLoad(AbPullToRefreshView view) {
				pageData();	
			}
		});
	}
	private void initData(){
		
		if(_business==null&&MLToolUtil.isNull(_business.userID)){
			return;
		}
		
		ZMRequestParams catalogParam = new ZMRequestParams();
		catalogParam.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,_business.userID);
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.HOME_PRODUCT, null, catalogParam, _handler, HTTP_RESPONSE_PRODUCT, MLHomeServices.getInstance());
	    loadDataWithMessage(_context, null, message1);
	}
	private void pageData() {
		MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
	 	ZMRequestParams catalogParam = new ZMRequestParams();
		catalogParam.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,user.Id);
		String lastId = _productDatas.get(_productDatas.size()-1).id+"";
		//param.addParameter("pageNum","2");
		catalogParam.addParameter(MLConstants.PARAM_MESSAGE_LASTID,lastId);
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.HOME_PRODUCT, null, catalogParam, _handler, HTTP_RESPONSE_PRODUCT_PAGE, MLHomeServices.getInstance());
	    loadDataWithMessage(_context, null, message1);
	}
	
	private List<String> getImageUrl(){
		List<String> images = new ArrayList<String>();
		for(MLHomeProductData data:_productDatas){
			String path = APIConstants.API_IMAGE_SHOW+data.image;
			images.add(path);
		}
		return images;
	}
	/**   
	  * @description   返回
	  *
	  * @author marcello  
	 */
	@OnClick(R.id.home_top_back)   
	public void backOnClick(View view){
		((MLAuxiliaryActivity)_context).onBackPressed();
	}
	
	private static final int HTTP_RESPONSE_PRODUCT= 0;
	private static final int HTTP_RESPONSE_PRODUCT_PAGE= 1;
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
            //获取产品
            case HTTP_RESPONSE_PRODUCT:{
            	MLHomeProductResponse ret = (MLHomeProductResponse) msg.obj;
        		if(ret.state.equalsIgnoreCase("1")){
        			_productAdapter.setData(ret.datas);
        			_productDatas = ret.datas;
        			if(ret.datas.size()<=20){
        				_refreshView.setLoadMoreEnable(false);
        			}else{
        				_refreshView.setLoadMoreEnable(true);
        			}
            	}else{
            		showMessage("获取产品失败!");
            	}
        		_refreshView.onHeaderRefreshFinish();
            	break;
            }
            case HTTP_RESPONSE_PRODUCT_PAGE:{
            	MLHomeProductResponse ret = (MLHomeProductResponse) msg.obj;
        		if(ret.state.equalsIgnoreCase("1")){
        			_productDatas.addAll(ret.datas);
        			_productAdapter.setData(_productDatas);
            	}else{
            		showMessage("获取产品失败!");
            	}
        		_refreshView.onFooterLoadFinish();
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
