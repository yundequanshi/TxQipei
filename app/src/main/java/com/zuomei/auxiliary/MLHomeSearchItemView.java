package com.zuomei.auxiliary;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseLayout;
import com.zuomei.constants.APIConstants;
import com.zuomei.home.MLHomeCityPop;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLHomeBusinessData;
import com.zuomei.model.MLHomeCityData;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLHomeServices;
import com.zuomei.utils.MLToolUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MLHomeSearchItemView extends BaseLayout{
	private MLHomeCityPop _menuWindow ;
	private Context _context;
	private MLLogin _user;
	public MLHomeSearchItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		_context = context;
		init();
	}

	public MLHomeSearchItemView(Context context) {
		super(context);
		_context = context;
		init();
	}
	 private Handler _callHandler;
	public MLHomeSearchItemView(Context context, Handler callHandler) {
		super(context);
		_callHandler = callHandler;
		_context = context;
		init();
	}


	@ViewInject(R.id.part_tv_name)
	private TextView mTvName;

	@ViewInject(R.id.part_tv_major)
	private TextView mTvMajor;

	@ViewInject(R.id.part_tv_address)
	private TextView mTvAddress;
	@ViewInject(R.id.item_part_car_call)
	private TextView item_part_car_call;

	@ViewInject(R.id.part_iv_icon)
	private ImageView nIvIcon;
	
	View view;
	private void init(){
		view = LayoutInflater.from(_context).inflate(R.layout.item_part_car, null);
		addView(view);
		_user =	((BaseApplication)_context.getApplicationContext()).get_user();
		ViewUtils.inject(this, view);
	
	}

	@OnClick(R.id.home_business_call)
	public void callOnClick(View view){
		Message m = new Message();
		m.obj = _data;
		_callHandler.sendMessage(m);
	}
	
	
	MLHomeBusinessData _data ;
	public void setData(final MLHomeBusinessData data) {
		if(data==null){
			return;
		}
		String imgUrl = APIConstants.API_IMAGE+"?id="+data.logo;

		nIvIcon.setTag(imgUrl);
		if (!BaseApplication.IMAGE_CACHE.get(imgUrl, nIvIcon)) {
			nIvIcon.setImageDrawable(null);
		}

		//	BaseApplication.IMAGE_CACHE.get(imgUrl, _headIv);
		//	bitmapUtils.display(_headIv, imgUrl);

		String majorOperate = "";



/*		if(data.majorOperate.size()>1){

		}*/
		for(int i=0;i<data.majorOperate.size();i++){
			if(i==data.majorOperate.size()-1){
				majorOperate=majorOperate+	data.majorOperate.get(i);
			}else{
				majorOperate=majorOperate+	data.majorOperate.get(i)+"、";
			}
		}


		mTvName.setText(data.compayName);
		mTvMajor.setText("主营:"+majorOperate);
		mTvAddress.setText("地址:" + data.address.replace(" ",""));
		
		item_part_car_call.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Message message = new Message();
				message.obj = data;

				_callHandler.sendMessage(message);
				/*final List<MLHomeCityData> datas = new ArrayList<MLHomeCityData>();
				MLHomeCityData data1 =new MLHomeCityData(); 
				data1.cityName = data.phone;
				
				MLHomeCityData data2 =new MLHomeCityData(); 
				data2.cityName = data.phone1;
				
				MLHomeCityData data3 =new MLHomeCityData(); 
				data3.cityName = data.phone2;
				Collections.addAll(datas, data1,data2,data3);
				
				_menuWindow= new MLHomeCityPop(_context, datas,new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						MLHomeCityData data =  datas.get(arg2);
						String phoneNum = data.cityName;
						_menuWindow.dismiss();
						if(_user.isDepot){
							call(phoneNum);
						}else{
							Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNum));  
							_context.startActivity(intent); 
						}
					}
				});  
			_menuWindow.showAtLocation(view, Gravity.CENTER, 0, 0); */
			}
		});
			
		
	}
	 private void call(final String phone){
     	if(MLToolUtil.isNull(phone)){
     		return;
     	}
     	if(phone.startsWith("400")){
     		Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));  
             _context.startActivity(intent); 
//             dial("0");
             return;
     	}
 		final List<MLHomeCityData> datas = new ArrayList<MLHomeCityData>();
 		MLHomeCityData data1 =new MLHomeCityData(); 
 		data1.cityName = "直接拨打";
 		
 		MLHomeCityData data2 =new MLHomeCityData(); 
 		data2.cityName =  "免费通话";
 		Collections.addAll(datas, data1,data2);
 		
 		_menuWindow= new MLHomeCityPop(_context, datas,new OnItemClickListener() {
 			@Override
 			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
 					long arg3) {
 				if(arg2==0){
 					//直接拨打
 					Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));  
 					_context.startActivity(intent); 
// 	                dial("0");
 				}else{
					callWeb(phone);
 					//网络电话
 					/*MLHomeBusiness1Data data = new MLHomeBusiness1Data();
 					data.address = _data.address;
 					data.userName = _data.userName;
 					data.phone = phone;
 					data.logo = _data.logo;
 					
 				Intent phoneIt = new Intent();
 				phoneIt.setClass(_context, MLAuxiliaryActivity.class);
 				phoneIt.putExtra("data", MLConstants.HOME_CALL);
 				phoneIt.putExtra("obj", (Serializable) data);
 				_context.startActivity(phoneIt);*/
 				//	dial("1");
 				}
 				_menuWindow.dismiss();
 		      
 			}
 		});  
 	_menuWindow.showAtLocation(view, Gravity.CENTER, 0, 0); 
 	}
	 
//	   private void dial(String isNetWorkPhone){
////   		MLLogin user = BaseApplication._user;
//   		MLLogin user =	((BaseApplication)_context.getApplicationContext()).get_user();
//   		ZMRequestParams catalogParam = new ZMRequestParams();
//   		catalogParam.addParameter(MLConstants.PARAM_HOME_ISNETWORKPHONE,isNetWorkPhone);
//   		catalogParam.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,user.Id);
//   		//判断是否来自收藏列表页面   
//   			catalogParam.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,_data.id);
//   		catalogParam.addParameter(MLConstants.PARAM_HOME_DEPOTPHONE,user.name);
//   		catalogParam.addParameter(MLConstants.PARAM_HOME_COMPANYPHONE,_data.phone);
//   		catalogParam.addParameter("phoneTime","1");
//   	    
//   		ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.HOME_CALL, null, catalogParam, _handler, HTTP_RESPONSE_CALL, MLHomeServices.getInstance());
//   	    loadDataWithMessage( null, message1);
//   	}


	private void callWeb(String phone) {
		MLLogin mUser = BaseApplication.getInstance().get_user();
		ZMRequestParams catalogParam = new ZMRequestParams();
		catalogParam.addParameter("fromClient",mUser.clientNumber);
		catalogParam.addParameter("to",phone.replace("-",""));



		ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(ZMHttpType.RequestType.HOME_CALL_WEB, null, catalogParam, _handler, HTTP_RESPONSE_CALL_WEB, MLHomeServices.getInstance());
		loadDataWithMessage(null, message1);

	}

	private static final int HTTP_RESPONSE_CALL_WEB = 5;
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
				case HTTP_RESPONSE_CALL_WEB:{
					MLRegister ret = (MLRegister) msg.obj;
					if(ret.state.equalsIgnoreCase("1")&&ret.datas){
						showMessage("呼叫成功,请等待回拨");
					}else {
						showMessage("呼叫失败");
					}
					break;
				}
				default:
					break;
			}
		}
	};


}
