package com.zuomei.home;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLMyTxCashData;
import com.zuomei.model.MLMyTxListData;
import com.zuomei.model.MLMyTxListResponse;
import com.zuomei.model.MLRegister;
import com.zuomei.model.MLSpecialResonse;
import com.zuomei.services.MLMyServices;
import com.zuomei.services.MLPayServices;
import com.zuomei.utils.MLToolUtil;
import com.zuomei.widget.MLCircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import cn.ml.base.utils.IEvent;

/**
 * 提现
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyCashFrg extends BaseFragment{

	public static MLMyCashFrg INSTANCE =null;
	
	public static MLMyCashFrg instance(){
	//	if(INSTANCE==null){
			INSTANCE = new MLMyCashFrg();
	//	}
		return INSTANCE;
	}
	
	@ViewInject(R.id.message_lv)
	private AbPullToRefreshView _pullToRefreshLv;
	@ViewInject(R.id.money_lv_record)
	private ListView _recordLv;
	
	@ViewInject(R.id.money_tv_name)
	private TextView _nameTv;

	@ViewInject(R.id.money_tv_balance)
	private TextView _balanceTv;
	
	@ViewInject(R.id.my_iv_head)
	private MLCircleImageView _headIv;
	
	@ViewInject(R.id.money_iv_head)
	private ImageView _headBg;
	
	@ViewInject(R.id.money_root)
	private RelativeLayout _root;
	private MLMyCashAdapter _moneyAdapter;
	private Context _context;
	private 	MLLogin _user;
	private String moneyCash;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_cash, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		
		initView();
		initData();
		return view;
	}
	
	
	private void initView() {
		_user = ((BaseApplication)getActivity().getApplication()).get_user();
		_moneyAdapter = new MLMyCashAdapter(_context);
		_recordLv.setAdapter(_moneyAdapter);
		_recordLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MLMyTxCashData data = (MLMyTxCashData) parent.getAdapter().getItem(position);
				//交易成功
				if(!data.cashStatus.equalsIgnoreCase("3"))return;
				 MLMyCashFailPop menuWindow = new MLMyCashFailPop(getActivity(), data, _datas.name,new IEvent<String>() {
						@Override
						public void onEvent(Object source, String eventArg) {
							
						}
					});  
				        menuWindow.showAtLocation(_root, Gravity.CENTER, 0, 0); 
				
			}
		});
		
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
		  
		Bitmap bitmap = MLToolUtil.readBitMap(_context,R.drawable.my_money_bg);
		//设置背景
		_headBg.setImageBitmap(bitmap);
	}
	
	private void initData() {
		/* ZMRequestParams params = new ZMRequestParams();
	      params.addParameter(MLConstants.PARAM_REGISTER_PWD,pwd);*/
		
		ZMRequestParams params = new ZMRequestParams();
	
		if(_user.isDepot){
			params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,_user.Id);
		}else{
			params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,_user.Id);
		}
	      ZMHttpRequestMessage message = new ZMHttpRequestMessage(RequestType.MY_WITHDRAW_LIST, null, params, _handler, HTTP_RESPONSE_LIST, MLMyServices.getInstance());
	      loadDataWithMessage(_context, null, message);
		
	}
	
	private void pageData() {
		ZMRequestParams params = new ZMRequestParams();
		String lastId = _datas.withDrawCash.get( _datas.withDrawCash.size()-1).id+"";
		if(_user.isDepot){
			params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,_user.Id);
		}else{
			params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,_user.Id);
		}
		params.addParameter(MLConstants.PARAM_MESSAGE_LASTID,lastId);
	      ZMHttpRequestMessage message = new ZMHttpRequestMessage(RequestType.MY_WITHDRAW_LIST, null, params, _handler, HTTP_RESPONSE_LIST_PAGE, MLMyServices.getInstance());
	      loadDataWithMessage(_context, null, message);
	}
	
	private void requestRecharge(String money){
		ZMRequestParams params = new ZMRequestParams();
		if(_user.isDepot){
			params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,_user.Id);
		}else{
			params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,_user.Id);
		}
		params.addParameter("money",money);
      ZMHttpRequestMessage message = new ZMHttpRequestMessage(RequestType.MY_DEAL_RECHARGE, null, params, _handler, HTTP_RESPONSE_RECHARGE, MLMyServices.getInstance());
      loadDataWithMessage(_context, null, message);
	}
	
	
	private MLMyTxListData _datas;
	protected void review(MLMyTxListData datas) {
        double m = Double.parseDouble(datas.balance);
		DecimalFormat df=new DecimalFormat(".##");
        String money =df.format(m);
		_datas = datas;
		_balanceTv.setText("余额："+money+"元");
		_nameTv.setText(datas.name);
		String iconUrl = APIConstants.API_IMAGE+"?id="+datas.logo;
		BaseApplication.IMAGE_CACHE.get(iconUrl, _headIv);
		
		_moneyAdapter.setData(datas.withDrawCash);
		
		//	BaseApplication.IMAGE_CACHE.
		
	}
	
	@OnClick(R.id.money_btn_add)
	public void inputOnClick(View view){
		judgePwd();
		}
	

	private void showCashPop(){
		
		MLMyCashPop menuWindow = new MLMyCashPop(null,getActivity(), new IEvent<String>() {
			@Override
			public void onEvent(Object source, String eventArg) {
				double balance =Double.parseDouble( _datas.balance);
				double money =Double.parseDouble(eventArg);
				
				double minMoneyUser = Double.parseDouble(_datas.minMoneyUser);
				if(money<minMoneyUser){
					showMessageWarning("最低提现额度为 : "+_datas.minMoneyUser+"元");
					return;
				}
				if(money>balance){
					showMessageWarning("余额不足!");
					return;
				}
				
				moneyCash = eventArg;
				//弹出框 输入支付密码
				//inputPwd();

				requestRecharge(eventArg);
			}
		});  
	        menuWindow.showAtLocation(_root, Gravity.CENTER, 0, 0); 
	}
	
	/**
	 * 弹出框 输入支付密码
	  * @description  
	  *
	  * @author marcello
	 */
	private void inputPwd(){
		
		MLMyPwdPop menuWindow = new MLMyPwdPop(getActivity(), new IEvent<String>() {
			@Override
			public void onEvent(Object source, String eventArg) {
				verifyPayPwd(eventArg);
			}
		});  
	        menuWindow.showAtLocation(_root, Gravity.CENTER, 0, 0); 
	}
	
	
	//====================================================
		/**
		 * 判断是否设置支付密码
		  * @description  
		  *
		  * @author marcello
		 */
		private void judgePwd(){
			ZMRequestParams catalogParam = new ZMRequestParams();
			  JSONObject jo = new JSONObject();
			  try {
				jo.put("companyId", _user.Id);
			} catch (JSONException e) {
				return;
			}
			  catalogParam.addParameter("data",jo.toString());
			
		    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_PAY_JUDGEPWD, null, catalogParam, _handler, HTTP_RESPONSE_JUDGEPWD, MLPayServices.getInstance());
		    loadDataWithMessage(_context, null, message1);
		}
		private void verifyPayPwd(String pwd){
			pwd = MLToolUtil.MD5(pwd);
			ZMRequestParams catalogParam = new ZMRequestParams();
			
			  JSONObject jo = new JSONObject();
			  try {
				jo.put("companyId", _user.Id);
				jo.put("pwd", pwd);
			} catch (JSONException e) {
				return;
			}
			  catalogParam.addParameter("data",jo.toString());
			
		    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_PAY_VERIFYPWD, null, catalogParam, _handler, HTTP_RESPONSE_VERIFYPWDPWD, MLPayServices.getInstance());
		    loadDataWithMessage(_context, null, message1);
		}

	 private static final int HTTP_RESPONSE_LIST = 0;
	 private static final int HTTP_RESPONSE_RECHARGE = 1;
	 private static final int HTTP_RESPONSE_WITHDRAW = 2;
	 private static final int HTTP_RESPONSE_LIST_PAGE = 3;
	 private static final int HTTP_RESPONSE_JUDGEPWD= 7;
	 private static final int HTTP_RESPONSE_VERIFYPWDPWD= 8;
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
	            //获取消息列表
	            case HTTP_RESPONSE_LIST:{
	            	MLMyTxListResponse ret = (MLMyTxListResponse) msg.obj;
			   		if(ret.state.equalsIgnoreCase("1")){
				            review(ret.datas);
			   		   }
					_pullToRefreshLv.onHeaderRefreshFinish();
			   		break;
	            }
	            case HTTP_RESPONSE_RECHARGE:{
	            	MLSpecialResonse ret  = (MLSpecialResonse) msg.obj;
	            	if(ret.state.equalsIgnoreCase("1")){
	            		showMessageSuccess("提现成功!");
	            		initData();
	       		   }else if(ret.state.equalsIgnoreCase("-1")&&ret.datas.contains("未绑定")){
	       			   
	       			  Builder builder = new Builder(_context);
	       			builder.setTitle("提示");
	       			  builder.setMessage("绑定银行卡才能体现，是否现在绑定？").setPositiveButton("确定", new OnClickListener() {
	       				@Override
	       				public void onClick(DialogInterface dialog, int which) {
	       					_event.onEvent("1", MLConstants.MY_BANK_CARD);
	       				}
	       			}).setNegativeButton("取消", new OnClickListener() {
	       				
	       				@Override
	       				public void onClick(DialogInterface dialog, int which) {
	       					
	       				}
	       			});
	       	          builder.show();
	       			   
	       			   
	       		   }
	            	break;
	            }
	       
	            case HTTP_RESPONSE_LIST_PAGE:{
	             	MLMyTxListResponse ret = (MLMyTxListResponse) msg.obj;
			   		if(ret.state.equalsIgnoreCase("1")){
				           _datas.withDrawCash.addAll(ret.datas.withDrawCash);
				           review(_datas);
			   		   }
					_pullToRefreshLv.onFooterLoadFinish();
	            	break;
	            }
	            
	            //判断是否设置支付密码
	            case HTTP_RESPONSE_JUDGEPWD:{
	            	MLRegister ret = (MLRegister) msg.obj;
         		if(ret.state.equalsIgnoreCase("1")&&ret.datas){
         			showCashPop();
             	}else{
             		 Builder builder = new Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT);
        			  builder.setTitle("提示");
        			  builder.setMessage("请先设置钱包密码");
        			  builder.setNegativeButton("确定", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
								_event.onEvent(null, MLConstants.MY_PAY_PWD);
						}
					});
        			  builder.setPositiveButton("取消", null);
        			  builder.show();
             	}
	            	break;
	            }
	            
	            case HTTP_RESPONSE_VERIFYPWDPWD:{
	             	MLRegister ret = (MLRegister) msg.obj;
	         		if(ret.state.equalsIgnoreCase("1")&&ret.datas){
	         			//提现
						requestRecharge(moneyCash);
	         		}else{
	         			showMessageError("钱包密码不正确!");
	         			inputPwd();
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
	@OnClick(R.id.money_top_back)
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
