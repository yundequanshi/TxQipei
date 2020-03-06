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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.home.MLHomeProductPop;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLHomeProductData;
import com.zuomei.model.MLHomeProductResponse;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLHomeServices;
import com.zuomei.services.MLMyServices;

import java.util.ArrayList;
import java.util.List;

import cn.ml.base.utils.IEvent;

/**
 * 商品管理
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyProductFrg extends BaseFragment{

	public static MLMyProductFrg INSTANCE =null;
//	public static MLHomeBusinessData _business;
	public static MLMyProductFrg instance(Object obj){
	//	_business = (MLHomeBusinessData) obj;
	//	if(INSTANCE==null){
			INSTANCE = new MLMyProductFrg();
	//	}
		return INSTANCE;
	}
	@ViewInject(R.id.product_gv)
	private GridView _gridView;
	
	@ViewInject(R.id.root)
	private RelativeLayout _root;
	private Context _context;
	private 	MLMyProductAdapter _productAdapter;
	private List<MLHomeProductData> _productDatas;
	private List<String> _photoPaths;
	@ViewInject(R.id._refressview)
	private AbPullToRefreshView _refreshView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_product, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		
		initView();
		initData();
		return view;
	}
	
	private void initView() {
		_productAdapter = new MLMyProductAdapter(_context);
		_gridView.setAdapter(_productAdapter);
		
		_gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			/*	MLHomeProductData data = (MLHomeProductData) arg0.getAdapter().getItem(arg2);
				String path = APIConstants.API_IMAGE+"?id="+data.imageId;
				MLMessagePhotoPop _pop = new MLMessagePhotoPop(getActivity(), path);
				_pop.showAtLocation(_root, Gravity.CENTER, 0, 0);   */
				MLHomeProductPop _pop = new MLHomeProductPop(getActivity(), getHeadImageUrl(),arg2);
				_pop.showAtLocation(_root, Gravity.CENTER, 0, 0); 
			}
		});
		
		_gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				_refreshView.onHeaderRefreshFinish();
				_refreshView.onFooterLoadFinish();
				
				final MLHomeProductData data  = (MLHomeProductData) arg0.getAdapter().getItem(arg2);
				
				  Builder builder = new Builder(_context);
	                builder.setItems( new String[] { "删除" }, new DialogInterface.OnClickListener()
	                {
	                    public void onClick(DialogInterface arg0, int arg1)
	                    {
	                        arg0.dismiss();  
	                        delProduct(data.id);
	                    }
	                }).setTitle("请选择");
	                builder.show();
				
				return false;
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
		MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
	 	ZMRequestParams catalogParam = new ZMRequestParams();
		catalogParam.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,user.Id);
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
	
	
	private void delProduct(String id){
		ZMRequestParams catalogParam = new ZMRequestParams();
		catalogParam.addParameter(MLConstants.PARAM_MY_GID,id);
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_PRODUCT_DEL, null, catalogParam, _handler, HTTP_RESPONSE_PRODUCT_DEL, MLMyServices.getInstance());
	    loadDataWithMessage(_context, null, message1);
	}
	
	private List<String> getHeadImageUrl(){
		List<String> images = new ArrayList<String>();
/*		final String imageId [] = _productDatas.image.split(",");
		for(String id : imageId){
			String imgUrl = APIConstants.API_IMAGE+"?id="+id;
			images.add(imgUrl);
		}*/
		for(MLHomeProductData data : _productDatas){
			String imgUrl = APIConstants.API_IMAGE+"?id="+data.image;
			images.add(imgUrl);
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
		((MLAuxiliaryActivity)_context).finish();
	}
	
	@OnClick(R.id.product_ib_add)
	public void addOnClick(View view){
		_event.onEvent(null,MLConstants.MY_PRODUCT_ADD);
	}
	
	
	private static final int HTTP_RESPONSE_PRODUCT= 0;
	private static final int HTTP_RESPONSE_PRODUCT_DEL= 1;
	private static final int HTTP_RESPONSE_PRODUCT_PAGE= 2;
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
        			_productDatas = ret.datas;
        			_productAdapter.setData(ret.datas);
        			if(ret.datas.size()<=20){
        				_refreshView.setLoadMoreEnable(false);
        			}else{
        				_refreshView.setLoadMoreEnable(true);
        			}
            	}else{
            		showMessageError("获取产品失败!");
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
        			showMessageError("获取产品失败!");
            	}
        		_refreshView.onFooterLoadFinish();
            	break;
            }
            //删除产品
            case HTTP_RESPONSE_PRODUCT_DEL:{
            	MLRegister ret = (MLRegister) msg.obj;
        		if(ret.state.equalsIgnoreCase("1")){
        			initView();
            	}else{
            		showMessage("删除产品失败!");
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
