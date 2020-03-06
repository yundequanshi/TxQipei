package com.zuomei.home;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ab.view.sliding.AbSlidingPlayView;
import com.ab.view.sliding.AbSlidingPlayView.AbOnItemClickListener;
import com.ab.view.sliding.AbSlidingPlayView.AbOnTouchListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.APIConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLeaveData;
import com.zuomei.model.MLLeaveDetailData;
import com.zuomei.model.MLLeaveDetailResponse;
import com.zuomei.services.MLAdvanServices;

import java.util.ArrayList;
import java.util.List;

import cn.ml.base.utils.IEvent;

/**
 * 优势件详情
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLAdvanDetailFrg extends BaseFragment{

	public static MLAdvanDetailFrg INSTANCE =null;
	public static MLLeaveData data;
	public static MLAdvanDetailFrg instance(Object obj){
		data = (MLLeaveData) obj;
	//	if(INSTANCE==null){
			INSTANCE = new MLAdvanDetailFrg();
	//	}
		return INSTANCE;
	}
	
	@ViewInject(R.id.iv_playView)
	private AbSlidingPlayView _playView;
	
	@ViewInject(R.id.accident_scrollview)
	private ScrollView _scrollview;
	
	@ViewInject(R.id.root)
	private RelativeLayout _root;
	
	/** 标题*/
	@ViewInject(R.id.tv_name)
	private TextView _titleTv;
	
	/** 车辆类型*/
	@ViewInject(R.id.tv_type)
	private TextView _typeTv;
	
	/** 车辆子类型*/
	@ViewInject(R.id.tv_child)
	private TextView _childTv;
	
	/** 原价*/
	@ViewInject(R.id.tv_oldprice)
	private TextView _oldTv;
	
	/** 价格*/
	@ViewInject(R.id.tv_price)
	private TextView _priceTv;
	
	/** 排量*/
	@ViewInject(R.id.tv_displacement)
	private TextView _displaceTv;
	
	/** 所在地*/
	@ViewInject(R.id.tv_address)
	private TextView _addressTv;
	
	/** 品质*/
	@ViewInject(R.id.tv_damaged)
	private TextView _damagedTv;
	
	/** 产品介绍*/
	@ViewInject(R.id.accident_tv_detail)
	private TextView _detailTv;
	
	/** 联系方式*/
	@ViewInject(R.id.accident_tv_phone)
	private TextView _phoneTv;
	
	@ViewInject(R.id.top_title)
	private TextView _topTv;
	
	private Context _context;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.leave_detail, null);
		ViewUtils.inject(this,view);
		
		_topTv.setText("优势件详情");
		_context = inflater.getContext();
		
		initView();
		return view;
	}
	
	private void initView() {
		
		 ZMRequestParams params = new ZMRequestParams();
	      params.addParameter("id",data.info.id);

	      ZMHttpRequestMessage message = new ZMHttpRequestMessage(RequestType.ADVAN_DEATAIL, null, params, _handler, HTTP_RESPONSE_ACCIDENT_DETIAL, MLAdvanServices.getInstance());
	      loadDataWithMessage(_context, null, message);
		
	}
	private void initTextView(TextView tv,int index) {
		String text  = tv.getText().toString();
		SpannableStringBuilder builder = new SpannableStringBuilder(text);  
		ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.BLACK);  
		builder.setSpan(redSpan, index, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
		tv.setText(builder);
	}
	
	private void initPlayView(String image) {
		
		final String imageId [] = image.split(",");
		for(String id : imageId){
			ImageView iv = getImageView();
			_playView.addView(iv);
			String imgUrl = APIConstants.API_IMAGE+"?id="+id;
		//	 bitmapUtils.display(iv, imgUrl, bigPicDisplayConfig);		
			 BaseApplication.IMAGE_CACHE.get(imgUrl, iv);
		}
		_playView.setOnTouchListener(new AbOnTouchListener() {
				@Override
				public void onTouch(MotionEvent event) {
					_scrollview.requestDisallowInterceptTouchEvent(true);
				}
			});
		_playView.setOnItemClickListener(new AbOnItemClickListener() {
			
			@Override
			public void onClick(int position) {
			/*	String path = APIConstants.API_IMAGE+"?id="+imageId[position];
				MLMessagePhotoPop _pop = new MLMessagePhotoPop(getActivity(), path);
				_pop.showAtLocation(_root, Gravity.CENTER, 0, 0);   */
				MLHomeProductPop _pop = new MLHomeProductPop(getActivity(), getHeadImageUrl(),position);
				_pop.showAtLocation(_root, Gravity.CENTER, 0, 0); 
			}
		});
	}
	public ImageView getImageView(){
		ImageView image = new ImageView(_context);
		image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
	//	image.setImageResource(id);
	//	image.setBackgroundColor(getResources().getColor(R.color.common_red));
		image.setScaleType(ScaleType.CENTER_CROP);
		return image;
	}
	
	private MLLeaveDetailData _data;
	private void review(MLLeaveDetailData data){
		if(data==null){
			return;
		}
		_data = data;
		initPlayView(_data.images);
		_titleTv.setText(_data.name);
		_typeTv.setText("车辆类型："+_data.carType);
		_childTv.setText("子类型："+_data.childType);
		_oldTv.setText("原价："+_data.originalCost);
		_priceTv.setText(_data.currentCost+"元");
		
		_addressTv.setText("所在城市："+_data.cityName);
		_damagedTv.setText("品质："+_data.quality);
		_displaceTv.setText("排量："+_data.exhaust+"L");
		_detailTv.setText(_data.introduction);
		_phoneTv.setText("  "+_data.user+"       "+_data.mobile);
		
		initTextView(_typeTv,5);		
		initTextView(_childTv,4);		
		initTextView(_oldTv,2);		
		initTextView(_addressTv,5);		
		initTextView(_damagedTv,3);		
		initTextView(_displaceTv,3);		
		
	}

	/**
	  * @description   返回
	  *
	  * @author marcello
	 */
	@OnClick(R.id.top_back)
	public void backOnClick(View view){
		((MLAuxiliaryActivity)_context).finish();
	}
	
	@OnClick(R.id.accident_tv_phone)
	public void callOnClick(View view){
		if(_data==null)return;
		Builder builder = new Builder(_context);
		builder.setMessage("拨打"+_data.mobile);
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+_data.mobile));  
                startActivity(intent); 
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
		
	}
	private List<String> getHeadImageUrl(){
		List<String> images = new ArrayList<String>();

		final String imageId [] = _data.images.split(",");
		for(String id : imageId){
			String imgUrl = APIConstants.API_IMAGE+"?id="+id;
			images.add(imgUrl);
		}
		
		return images;
	}
	 private static final int HTTP_RESPONSE_ACCIDENT_DETIAL = 0;
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
	                case HTTP_RESPONSE_ACCIDENT_DETIAL:
	                	MLLeaveDetailResponse ret = (MLLeaveDetailResponse) msg.obj;
	                	if(ret.state.equalsIgnoreCase("1")&&ret.datas!=null){
	                		review(ret.datas);
	                	//	_carAdapter.setData(ret.datas);
	                	}else{
	                		showMessage("获取事故车详情失败!");
	                	}
	                    break;
	                default:
	                    break;
	            }
	        }
	    };	
	
		@Override
		public void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
		//	BaseApplication.IMAGE_CACHE.setOnImageCallbackListener(BaseApplication.imageCallBack);
		}
	private IEvent<Object> _event;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_event = (IEvent<Object>) activity;
	}
}
