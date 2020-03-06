package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.home.MLHomeCityPop;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLDialData;
import com.zuomei.model.MLDialDetailData;
import com.zuomei.model.MLDialDetailResponse;
import com.zuomei.model.MLHomeCityData;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLHomeServices;
import com.zuomei.services.MLMyServices;
import com.zuomei.utils.MLToolUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.ml.base.utils.IEvent;

/**
 * 通话记录详情
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyPhoneDetailFrg extends BaseFragment{

	public static MLMyPhoneDetailFrg INSTANCE =null;
	public static MLDialData _data;
	public static MLMyPhoneDetailFrg instance(Object obj ){
		_data = (MLDialData) obj;
			INSTANCE = new MLMyPhoneDetailFrg();
		return INSTANCE;
	}
	@ViewInject(R.id.phone_lv)
	private AbPullToRefreshView _pullToRefreshLv;
	@ViewInject(R.id.mListView)
	private ListView _phoneLv;
	@ViewInject(R.id.root)
	private RelativeLayout _root;
	@ViewInject(R.id.head_phone)
	private ImageView head_phone;
	@ViewInject(R.id.head_name)
	private TextView head_name;
	
	
	private Context _context;
	private MLMyPhoneDetailAdapter _adapter;
	public List<MLDialDetailData> _datas;
	
	private  MLDialDetailData _detailData =null;
	private MLLogin user ;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_phone_detail, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		
		initView();
		initData();
		return view;
	}
	
	
	private void initView() {
		
		_adapter = new MLMyPhoneDetailAdapter(_context);
		_phoneLv.setAdapter(_adapter);
		
		String iconUrl = APIConstants.API_IMAGE + "?id=" + _data.logo;
		 bitmapUtils.display(head_phone, iconUrl, bigPicDisplayConfig);
		 
		 head_name.setText(_data.userName);
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
				pageDate();
			}
		});  
		
		_phoneLv.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final MLDialDetailData data = (MLDialDetailData) parent.getAdapter().getItem(position);
				  Builder builder = new Builder(_context);
	                builder.setItems( new String[] { "删除" }, new DialogInterface.OnClickListener()
	                {
	                    public void onClick(DialogInterface arg0, int arg1)
	                    {
	                        arg0.dismiss();
	                        delPhone(data.id);
	                    }

						
	                }).setTitle("请选择");
	                builder.show();				
				return true;
			}
		});
		
	
		_phoneLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			
				_detailData = _datas.get(position);
				
				if(user.isDepot){
					call(_detailData.companyPhone);
				}else{
					Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+_detailData.depotPhone));
			    	_context.startActivity(intent);
				}
			/*	Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+data.companyPhone));  
		    	_context.startActivity(intent);  */
			}
		});
		
	}
	
	
   
	
	private void 	initData(){
		ZMRequestParams catalogParam = new ZMRequestParams();
//		MLLogin user = BaseApplication._user;
		user =((BaseApplication)getActivity().getApplication()).get_user();
		if(user.isDepot){
			catalogParam.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,user.Id);
		}else{
			catalogParam.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,user.Id);
		}
		catalogParam.addParameter(MLConstants.PARAM_MY_COMID,_data.id);
		
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_DIAL_DETAIL, null, catalogParam, _handler, HTTP_RESPONSE_DIAL_LIST,  MLMyServices.getInstance());
	    loadDataWithMessage(_context, null, message1);
	}
	
	 private void pageDate(){
			ZMRequestParams catalogParam = new ZMRequestParams();
//		MLLogin user = BaseApplication._user;
			if(user.isDepot){
				catalogParam.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,user.Id);
			}else{
				catalogParam.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,user.Id);
			}
			catalogParam.addParameter(MLConstants.PARAM_MY_COMID,_data.id);
			String lastId = _datas.get(_datas.size()-1).id+"";
			catalogParam.addParameter(MLConstants.PARAM_MESSAGE_LASTID,lastId);
		    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_DIAL_DETAIL, null, catalogParam, _handler, HTTP_RESPONSE_DIAL_PAGE,  MLMyServices.getInstance());
		    loadDataWithMessage(_context, null, message1);
	 }

	 private void delPhone(String id) {
			ZMRequestParams catalogParam = new ZMRequestParams();
			catalogParam.addParameter(MLConstants.PARAM_MY_PHONE_DELID,id);
		    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_DIAL_DETAIL_DEL, null, catalogParam, _handler, HTTP_RESPONSE_DIAL_DEL,  MLMyServices.getInstance());
		    loadDataWithMessage(_context, null, message1);
		}
	 
	 private MLHomeCityPop _menuWindow;
     private void call(final String phone){
    	 if(MLToolUtil.isNull(phone)){
     		return;
     	}
     	if(phone.startsWith("400")){
     		Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));  
             startActivity(intent); 
             dial("0");
             return;
     	}
 		final List<MLHomeCityData> datas = new ArrayList<MLHomeCityData>();
 		MLHomeCityData data1 =new MLHomeCityData(); 
 		data1.cityName = "直接拨打";
 		
 		MLHomeCityData data2 =new MLHomeCityData(); 
 		data2.cityName =  "免费通话";
 		Collections.addAll(datas, data1,data2);
 		
 		_menuWindow= new MLHomeCityPop(getActivity(), datas,new OnItemClickListener() {
 			@Override
 			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
 					long arg3) {
 				if(arg2==0){
 					//直接拨打
 					Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));  
 	                startActivity(intent); 
 	                dial("0");
 				}else{
 					//网络电话
					callWeb(phone);
 				/*	MLHomeBusiness1Data data = new MLHomeBusiness1Data();
 			//		data.address = _businessData.address;
 					data.userName = _data.userName;
 					data.phone =phone;
 					data.logo = _data.logo;
 					
 				Intent phoneIt = new Intent();
 				phoneIt.setClass(_context, MLAuxiliaryActivity.class);
 				phoneIt.putExtra("data", MLConstants.HOME_CALL);
 				phoneIt.putExtra("obj", (Serializable) data);
 				startActivity(phoneIt);*/
 				//	dial("1");
 				}
 				_menuWindow.dismiss();
 		      
 			}
 		});  
 	_menuWindow.showAtLocation(_root, Gravity.CENTER, 0, 0); 
 	}
     
     private void dial(String isNetWorkPhone){
// 		MLLogin user = BaseApplication._user;
 		MLLogin user = 	((BaseApplication)getActivity().getApplication()).get_user();
		 if(!user.isDepot){
			 return;
		 }

		 ZMRequestParams catalogParam = new ZMRequestParams();
 		catalogParam.addParameter(MLConstants.PARAM_HOME_ISNETWORKPHONE,isNetWorkPhone);
 		catalogParam.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,user.Id);
 		//判断是否来自收藏列表页面   
 			catalogParam.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,_data.userId);
		 catalogParam.addParameter(MLConstants.PARAM_HOME_DEPOTPHONE,BaseApplication.aCache.getAsString(MLConstants.PARAM_REGISTER_USERNAME));
 		catalogParam.addParameter(MLConstants.PARAM_HOME_COMPANYPHONE,_detailData.companyPhone);
 		catalogParam.addParameter("phoneTime","1");
 	    
 		ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.HOME_CALL, null, catalogParam, _handler, HTTP_RESPONSE_CALL, MLHomeServices.getInstance());
 	    loadDataWithMessage(_context, null, message1);
 	}

	private void callWeb(String phone) {
		MLLogin mUser = BaseApplication.getInstance().get_user();
		ZMRequestParams catalogParam = new ZMRequestParams();
		catalogParam.addParameter("fromClient",mUser.clientNumber);
		catalogParam.addParameter("to",phone);



		ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.HOME_CALL_WEB, null, catalogParam, _handler, HTTP_RESPONSE_CALL_WEB, MLHomeServices.getInstance());
		loadDataWithMessage(_context, null, message1);

	}
	 
	/**
	  * @description  返回
	  *
	  * @author marcello
	 */
	@OnClick(R.id.top_back)
	public void backOnClick(View view){
		((MLAuxiliaryActivity)_context).finish();
	}
	
	 private static final int HTTP_RESPONSE_DIAL_LIST= 0;
	 private static final int HTTP_RESPONSE_DIAL_PAGE= 2;
	 private static final int HTTP_RESPONSE_DIAL_DEL= 3;
	 private static final int HTTP_RESPONSE_CALL= 4;
	private static final int HTTP_RESPONSE_CALL_WEB = 9;
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
	            case HTTP_RESPONSE_DIAL_LIST:{
	            	MLDialDetailResponse ret = (MLDialDetailResponse) msg.obj;
         		if(ret.state.equalsIgnoreCase("1")){
         			_datas = ret.datas;
         			_adapter.setData(ret.datas);
             	}else{
             		showMessageError("获取通话列表失败!");
             	}
         		_pullToRefreshLv.onHeaderRefreshFinish();
	            	break;
	            }
	            case HTTP_RESPONSE_DIAL_PAGE:{
	            	MLDialDetailResponse ret = (MLDialDetailResponse) msg.obj;
	         		if(ret.state.equalsIgnoreCase("1")){
	         			if(ret.datas.size()>0){
	         				_datas.addAll(ret.datas);
	         				_adapter.setData(_datas);
	         			}else{
	         				showMessageWarning("数据已记载完成!");
	         			}
	         			
	             	}else{
	             		showMessageError("获取通话列表失败!");
	             	}
	         		_pullToRefreshLv.onFooterLoadFinish();
	            	break;
	            }
	            case HTTP_RESPONSE_DIAL_DEL:{
	            	MLRegister ret =  (MLRegister) msg.obj;
	            	if(ret.state.equalsIgnoreCase("1")&&ret.datas==true){
	            		showMessageSuccess("删除成功!");
	            		initData();
	            	}else{
	            		showMessageError("删除失败!");
	            	}
	            	break;
	            }

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
	
	private IEvent<Object> _event;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_event = (IEvent<Object>) activity;
	}
}
