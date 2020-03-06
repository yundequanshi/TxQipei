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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLLotteryData;
import com.zuomei.model.MLLotteryInfo;
import com.zuomei.model.MLLotteryResponse;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLMyServices;
import com.zuomei.widget.EraseView;

import cn.ml.base.utils.IEvent;

/**
 * 抽奖
 * @author Marcello
 *  
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLLotteryFrg extends BaseFragment{

	public static MLLotteryFrg INSTANCE =null;
	
	public static MLLotteryFrg instance(){
	//	if(INSTANCE==null){
			INSTANCE = new MLLotteryFrg();
	//	}
		return INSTANCE;
	}
	
	
	
	@ViewInject(R.id.lottery_scrollView)
	private ScrollView _scollview;
	/*@ViewInject(R.id.login_et_address)
	private EditText _addressEt;*/
	
/*	@ViewInject(R.id.home_lv_business)
	private ListView _businessLv;*/
	@ViewInject(R.id.eraseView1)
	private EraseView _eraseView;
	
	@ViewInject(R.id.tv_content)
	private TextView _contentTv;
	
	@ViewInject(R.id.tv_lottery)
	private TextView _lotteryTv;


	@ViewInject(R.id.lottery_tv_jf)
	private TextView mTvJf;


/*	@ViewInject(R.id.rl_view)
	private RelativeLayout _animRl;*/
	
	
	private MLLotteryData _data;
	private boolean isLottery ;
	private Context _context;
	private MLLotteryInfo _info;
	//private FlakeView mFlView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.lottery_main, null);
		ViewUtils.inject(this,view);
		_context = inflater.getContext();
	//	mFlView = (FlakeView) view.findViewById(R.id.fl_view);
//		mFlView.addFlakes(17);
/*		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mFlView.addFlakes(17);
			}
		}, 500);*/
			
		
		init();
		initData();
		
		_eraseView.setEvent(new IEvent() {

			@Override
			public void onEvent(Object source, Object eventArg) {
				// TODO Auto-generated method stub
				if(isLottery){
					lottery();
				}else{
					 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT);
       			  builder.setTitle("提示");
       			  builder.setMessage("积分不足,本次抽奖不记录");
       			  builder.setNegativeButton("确定", null);
       			  builder.show();
				}
			}
		});
		
		_eraseView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				_scollview.requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		return view;
	}
	
	private void init() {
	/*	_animRl.setVisibility(View.VISIBLE);
		_hvHandler.sendEmptyMessageDelayed(0, 500);
		_hvHandler.sendEmptyMessageDelayed(1, 4000);*/
	}

	private void initData(){
		MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
		  ZMRequestParams params = new ZMRequestParams();
		     params.addParameter("depotId",user.Id);
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.LOTTERY_MAIN, null, params, _handler, HTTP_RESPONSE_LOTTERY, MLMyServices.getInstance());
	    loadDataWithMessage(_context, "数据加载中...", message1);
	}
	
	private void reviewData(){
		MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
		  ZMRequestParams params = new ZMRequestParams();
		     params.addParameter("depotId",user.Id);
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.LOTTERY_MAIN, null, params, _handler, HTTP_RESPONSE_LOTTERY_RE, MLMyServices.getInstance());
	    loadData(_context, message1);
	}
	
	private void lottery(){
		if(_info==null){
			return;
		}
		MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
		  ZMRequestParams params = new ZMRequestParams();
		     params.addParameter("depotId",user.Id);
		     params.addParameter("activityId",_info.id);
		     params.addParameter("cityId",BaseApplication._currentCity);
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.LOTTERY_RECORD, null, params, _handler, HTTP_RESPONSE_LOTTERY_l, MLMyServices.getInstance());
		loadData(_context, message1);
	}
	
	
	protected void review() {

        int i = (int) Math.round(Math.random()*9999+1);
		StringBuffer buffer = new StringBuffer();
		
		int end = 0;
		int start = 1;
		for(MLLotteryInfo info : _data.activitie){
			if (end == 0)
				end = info.getPercentage();
			else
				end += info.getPercentage();
			if (i >=start && i <= end) {
				_lotteryTv.setText(info.name);
				_info = info;
			}
			start += info.getPercentage();
			
			
			/*if(i>info.percentage){
				_lotteryTv.setText(info.name);
				_info = info;
			}*/
			buffer.append(info.name+"："+info.value+"元<br>");
		}
		
		int count = _data.sorce/_data.restrict;
	/*	String t="";
		if(_data.sorce<_data.restrict){
			t= String.format("（当前积分%d，不能抽奖！）",_data.sorce);
		}else{
			t = String.format("（当前积分%d，可抽奖%d次！）",_data.sorce, count);
		}*/

		String t = String.format("刮一次消耗%s积分",_data.restrict+"");

		mTvJf.setText(Html.fromHtml(t));

		_contentTv.setText(Html.fromHtml(buffer.toString()));
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
	
/*	@OnClick(R.id.btn_detail)
	public void detailOnClick(View view){
		_event.onEvent(null,MLConstants.MY_LOTTERY_LIST);
	}*/
	
	
	 private static final int HTTP_RESPONSE_LOTTERY = 0;
	 private static final int HTTP_RESPONSE_LOTTERY_l = 1;
	 private static final int HTTP_RESPONSE_LOTTERY_RE = 2;
	    private Handler _handler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	            super.handleMessage(msg);
	      	 // dismissProgressDialog();
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
	            case HTTP_RESPONSE_LOTTERY:{
	             	MLLotteryResponse ret = (MLLotteryResponse) msg.obj;
	             	_data = ret.datas;
	            	if(ret.state.equalsIgnoreCase("1")){
	            		if(ret.datas.sorce>=ret.datas.restrict){
	            			isLottery=true;
	            		}else{
	            			isLottery=false;
							//showMessage("积分不足，不能抽奖");
	            			 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT);
	            			  builder.setTitle("提示");
	            			  builder.setMessage("积分不足,不能参与抽奖,试一下手气吧,本次抽奖不记录,不扣积分");
	            			  builder.setNegativeButton("确定", null);
	            			  builder.show();
						/*	_eraseView.setOnTouchListener(new OnTouchListener() {
								@Override
								public boolean onTouch(View v, MotionEvent event) {
									return true;
								}
							});*/
	            		}
	            		review();
	            		
	            		
	              	}else{
	              	}
	                    break;
	            }
	            
	            case HTTP_RESPONSE_LOTTERY_RE:{
	             	MLLotteryResponse ret = (MLLotteryResponse) msg.obj;
	             	_data = ret.datas;
	            	if(ret.state.equalsIgnoreCase("1")){
	            		if(ret.datas.sorce>=ret.datas.restrict){
	            			isLottery=true;
	            		}
	            		review();
	            		
	            		
	              	}
	            	break;
	            }
	            
	            case HTTP_RESPONSE_LOTTERY_l:{
	             	MLRegister ret = (MLRegister) msg.obj;
	            	if(ret.state.equalsIgnoreCase("1")){
	            	//	reviewData();
	            		
	            		String ts = String.format("恭喜您中得<font color=\"#c42b20\">%s</font>!", _lotteryTv.getText().toString());
	            		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT);
           			  builder.setTitle("提示");
           			  builder.setMessage(Html.fromHtml(ts));
           			  builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
           			  builder.show();
	              	}else{
	              	}
	                    break;
	            }
	            
	            
	            
	                default:
	                    break;
	            }
	        }
	    };
	    
	    private static final int VIEW_START= 0;
	    private static final int VIEW_GONE= 1;
/*	    private Handler _hvHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case VIEW_START:
					mFlView.addFlakes(10);
					break;
				case VIEW_GONE:{
					mFlView.pause();
				//	_animRl.setVisibility(View.GONE);
					break;
				}
				default:
					break;
				}
			}
	    };*/
	    private IEvent<Object> _event;
		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			_event = (IEvent<Object>) activity;
		}
		
	/*	@Override
		public void onPause() {
			super.onPause();
			mFlView.pause();
		}

		@Override
		public void onResume() {
			super.onResume();
			mFlView.resume();
		}*/
		
}
