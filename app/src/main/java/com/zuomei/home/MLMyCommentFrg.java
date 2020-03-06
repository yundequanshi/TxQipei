package com.zuomei.home;

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
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLDealListData;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLMyServices;
import com.zuomei.utils.MLToolUtil;

import cn.ml.base.utils.IEvent;

/**
 * 评论
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyCommentFrg extends BaseFragment{

	public static MLMyCommentFrg INSTANCE =null;
	
	private static MLDealListData _data;
	public static MLMyCommentFrg instance(Object obj){
		_data = (MLDealListData) obj;
	//	if(INSTANCE==null){
			INSTANCE = new MLMyCommentFrg();
	//	}
		return INSTANCE;
	}
	
	
	@ViewInject(R.id.comment_et)
	private EditText _commentEt;
	
	@ViewInject(R.id.money_root)
	private RelativeLayout _root;
	
    String level = "";	
	private Context _context;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_deal_comment, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		
		initView();
		return view;
	}
	
	
	private void initView() {
		
		
	}
	

	 private static final int HTTP_RESPONSE_LIST = 0;
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
	            	MLRegister ret = (MLRegister) msg.obj;
			   		if(ret.state.equalsIgnoreCase("1")){
			   			showMessageSuccess("评论成功");
			   			((MLAuxiliaryActivity)_context).onBackPressed();
			   		   }else{
			   			   showMessageError("不能重复评论");
			   		   }
			   		break;
	            }
	                default:
	                    break;
	            }
	        }
	    };
	    
	    
		@OnClick(R.id.money_btn_ok)
		public void okOnClick(View view){
             String comment = _commentEt.getText().toString();
             if(MLToolUtil.isNull(comment)){
            	 showMessageWarning("评论内容不能为空!");
            	 return;
             }
             
             if(level.equalsIgnoreCase("")){
            	 showMessageWarning("请选择好评/差评!");
            	 return;
             }
             
             MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
//评论
             ZMRequestParams params = new ZMRequestParams();
   	      params.addParameter("companyId",_data.userId);
   	      params.addParameter("userId",user.Id);
   	      params.addParameter("content",comment);
   	      params.addParameter("level",level);
   	      
   	      ZMHttpRequestMessage message = new ZMHttpRequestMessage(RequestType.MY_DEAL_COMMENT, null, params, _handler, HTTP_RESPONSE_LIST, MLMyServices.getInstance());
   	      loadDataWithMessage(_context, null, message);
             
		}
		
		@OnClick({R.id.money_btn_good,R.id.money_btn_bad})
		public void btnOnClick(View view){
				if(view.getId()==R.id.money_btn_good){
					//好评
					level = "1";
					showMessageSmile("好评");
				}else{
					//差评
					showMessageError("差评");
					level = "-1";
				}
		}
	
	/**
	  * @description   返回
	  *
	  * @author marcello
	 */
	@OnClick(R.id.top_back)
	public void backOnClick(View view){
//		((MLAuxiliaryActivity)_context).finish();
		((MLAuxiliaryActivity)_context).onBackPressed();
	}
	
	
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		_commentEt.setText("");
	}


	private IEvent<Object> _event;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_event = (IEvent<Object>) activity;
	}
}
