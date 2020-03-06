package com.txsh.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.txsh.R;
import com.txsh.model.TXEventModel;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLSpecialResonse;
import com.zuomei.services.MLPayServices;

import cn.ml.base.utils.MLStrUtil;
import org.greenrobot.eventbus.EventBus;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    
	@ViewInject(R.id.pay_iv_ret)
	private ImageView _payIv;	

/*	@ViewInject(R.id.top_back)
	private ImageView _backIv;	*/
	
	@ViewInject(R.id.pay_tv_order)
	private TextView _orderTv;	
	
	@ViewInject(R.id.pay_tv_ret)
	private TextView _statusTv;	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
		ViewUtils.inject(this);
        
    	api = WXAPIFactory.createWXAPI(this, APIConstants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		
	}
  
	@Override
	public void onResp(BaseResp resp) {
	//	Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		if(resp.errCode==0){
			_payIv.setImageResource(R.drawable.pay_success);
			_statusTv.setText("支付成功");

			if(MLStrUtil.compare(BaseApplication.aCache.getAsString(MLConstants.ACACHE_PARAM_WX_SHOP), MLConstants.ACACHE_PARAM_WX_SHOP)){
				//商城
				requestPayShop();
			}else{
				//商家支付
				requestPay();
			}
		}else{
			_payIv.setImageResource(R.drawable.pay_error);
			_statusTv.setText("支付失败");
		}
		finish();
	//	requestPay();
	}

	@OnClick(R.id.top_back)
	public void backOnClick(View view){
		finish();
	}
	  
	@OnClick(R.id.pay_btn_ok)
	public void okOnClick(View view){
		finish();
	}
	
	    
	
	private void requestPay(){
		ZMRequestParams catalogParam = new ZMRequestParams();
		catalogParam.addParameter("out_trade_no",BaseApplication.out_trade_no);

	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.WEIXIN_PAY_CONFIRM, null, catalogParam, _handler, HTTP_RESPONSE_PAY, MLPayServices.getInstance());
	    loadDataWithMessage(WXPayEntryActivity.this, null, message1);
	}

	private void requestPayShop(){
		ZMRequestParams catalogParam = new ZMRequestParams();
		catalogParam.addParameter("out_trade_no",BaseApplication.out_trade_no);

		ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.WEIXIN_PAY_CONFIRM_SHOP, null, catalogParam, _handler, HTTP_RESPONSE_PAY_SHOP, MLPayServices.getInstance());
		loadDataWithMessage(WXPayEntryActivity.this,null,message1);
	}




	private static final int HTTP_RESPONSE_PAY= 0;
	private static final int HTTP_RESPONSE_PAY_SHOP= 1;

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
	            case HTTP_RESPONSE_PAY:{
	            	MLSpecialResonse ret = (MLSpecialResonse) msg.obj;
         		if(ret.datas.equalsIgnoreCase("ok")){
         			showMessageSuccess("支付成功！");
             	}else{
             		showMessageError("支付失败!");
             	}
	            	break;
	            }

					case HTTP_RESPONSE_PAY_SHOP:{
						MLSpecialResonse ret = (MLSpecialResonse) msg.obj;
						if(ret.datas.equalsIgnoreCase("ok")){
							//通知界面刷新
							EventBus.getDefault().post(new TXEventModel(MLConstants.EVENT_PARAM_ORDER_PAY_WXOK,""));
							showMessageSuccess("支付成功！");
						}else{
							showMessageError("支付失败!");
						}
						break;
					}
	                default:
	                    break;
	            }
	        }
	    };
}