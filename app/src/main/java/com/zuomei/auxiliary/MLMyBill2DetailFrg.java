package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.APIConstants;
import com.zuomei.home.MLHomeProductPop;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLBill2DetailData;
import com.zuomei.model.MLBill2DetailResponse;
import com.zuomei.services.MLMyServices;

import java.util.ArrayList;
import java.util.List;

import cn.ml.base.utils.IEvent;

/**
 *汽修厂-我的账单-详情
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyBill2DetailFrg extends BaseFragment{

	public static MLMyBill2DetailFrg INSTANCE =null;
	
	private static String id;
	public static MLMyBill2DetailFrg instance(Object obj){
		id = (String) obj;
	//	if(INSTANCE==null){
			INSTANCE = new MLMyBill2DetailFrg();
	//	}
		return INSTANCE;
	}
	
	@ViewInject(R.id.add_phone)
	private ImageView _addIv;
	
	@ViewInject(R.id.tv_time)
	private TextView _timeTv;
	
	@ViewInject(R.id.tv_name)
	private TextView _nameTv;

	@ViewInject(R.id.tv_money)
	private TextView _moneyTv;


/*	@ViewInject(R.id.tv_updateTime)
	private TextView _updateTv;*/
	
	
	@ViewInject(R.id.root)
	private RelativeLayout _root;
	
	private Context _context;
	

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_bill2_add, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		
		initView();
		initData();
		return view;
	}
	
	

	private void initData() {
		ZMRequestParams params = new ZMRequestParams();
			params.addParameter("id",id);
		
	    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.BILL2_DETAIL, null, params, _handler,HTTP_RESPONSE_UPDATE, MLMyServices.getInstance());
	    loadData(_context, message2);
	}

	private void initView() {

		
	}
	
	
	
	@OnClick(R.id.top_back)
	public void backOnClick(View view){
		((MLAuxiliaryActivity)_context).finish();
	}

		 private static final int HTTP_RESPONSE_UPDATE= 0;
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
		            case  HTTP_RESPONSE_UPDATE:{
		            	MLBill2DetailResponse ret = (MLBill2DetailResponse) msg.obj;
		            		if(ret.state.equalsIgnoreCase("1")){
		            			review(ret.datas);
		            			
		            			
		                	}else{
		                	}
		            	break;
		            }
	            

	                default:
	                    break;
	            }
	        }
	    };
		
		
	protected void review(MLBill2DetailData datas) {
		String imgUrl = APIConstants.API_IMAGE+"?id="+datas.images;
		
		_addIv.setTag(imgUrl);
		 if (!BaseApplication.IMAGE_CACHE.get(imgUrl, _addIv)) {
			 _addIv.setImageDrawable(null);
		    }
		_nameTv.setText("公司名称："+datas.company.companyName);
		_timeTv.setText("发货日期："+datas.sendTime);
		_moneyTv.setText("金        额："+datas.money+" 元");
		//_updateTv.setText(datas.createTime);

		final List<String> images = new ArrayList<String>();
		images.add(imgUrl);
		
		_addIv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				MLHomeProductPop _pop = new MLHomeProductPop(getActivity(), images,0);
				_pop.showAtLocation(_root, Gravity.CENTER, 0, 0);
			}
		});
	}

	private IEvent<Object> _event;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_event = (IEvent<Object>) activity;
	}
}
