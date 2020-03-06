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
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLeaveDetail;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLLeaveServices;
import com.zuomei.utils.MLToolUtil;

import java.util.HashMap;
import java.util.Map;

import cn.ml.base.utils.IEvent;

/**
 * 二手件-step3
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLLeaveadd3Frg extends BaseFragment{

	public static MLLeaveadd3Frg INSTANCE =null;
	
	private static MLLeaveDetail detail;
	public static MLLeaveadd3Frg instance(Object obj){
		detail = (MLLeaveDetail) obj;
	//	if(INSTANCE==null){
			INSTANCE = new MLLeaveadd3Frg();
	//	}
		return INSTANCE;
	}
	
	@ViewInject(R.id.accident_gv_photo)
	private GridView _photoGv;
	
	@ViewInject(R.id.accident_et_content)
	private EditText _contentEt;
	
	@ViewInject(R.id.accident_add_root)
	private RelativeLayout _root;
	/*@ViewInject(R.id.violation_wb)
	private WebView _webview;*/
	private Context _context;
	private String _content;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.leave_add3, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		
		initView();
		return view;
	}
	
	
	private void initView() {
	}
	
	@OnClick(R.id.top_back)
	public void backOnClick(View view){
		//startActivity(new Intent(_context,MLHomeActivity.class));
		((MLAuxiliaryActivity)_context).onBackPressed();
	}
	
	/**
	  * @description  下一步
	  *
	  * @author marcello
	 */
	private String imageId="";
	@OnClick(R.id.accident_btn_next)
	public void nextOnClick(View view){
		_content = _contentEt.getText().toString();
		if(MLToolUtil.isNull(_content)){
			showMessage("描述不能为空!");
			return ;
		}
		requestAccident();

		/*   for(final String path : detail.paths){
		//上传图片
		   RequestParams params = new RequestParams();
			params.addBodyParameter("file", new File(path));
	    	HttpUtils http = new HttpUtils();
	    	http.send(HttpRequest.HttpMethod.POST,
	    	   APIConstants.API_IMAGE_UPLOAD,
	    	    params,
	    	    new RequestCallBack<String>() {
	    	        @Override
	    	        public void onStart() {
	    	        }
	    	        @Override
	    	        public void onLoading(long total, long current, boolean isUploading) {
	    	        }
	    	        @Override
	    	        public void onSuccess(ResponseInfo<String> responseInfo) {
	    	        	try {
							MLMessagePublishResponse ret = ZMJsonParser.fromJsonString(MLMessagePublishResponse.class, responseInfo.result);
						
							if(detail.paths.get(detail.paths.size()-1).equalsIgnoreCase(path)){
								imageId= imageId+ret.datas;
								//上传图片完成 ，开始添加事故车
								requestAccident();
							}else{
								imageId= imageId+ret.datas+",";
							}
							//	System.out.println("");
						//	publish(ret.datas);
						} catch (ZMParserException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	    	        }
	    	        @Override
	    	        public void onFailure(HttpException error, String msg) {
	    	        	showMessage("图片上传失败!");
	    	        }
	    	});	
		   }*/
	    	
	    	
/*	detail.companyId = "";
	detail.companyLogo="";
	detail.depotLogo="";
	detail.id="";
	detail.image="";*/
		
		
	}
	
	private void requestAccident(){
		MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
	  ZMRequestParams params = new ZMRequestParams();
			params.addParameter("depotUser.id",user.Id);
			params.addParameter("carType",detail.type);
			params.addParameter("childType",detail.child);
			params.addParameter("exhaust",detail.displacement);
			//params.addParameter("city.id",BaseApplication._currentCity);
		params.addParameter("city.id","1");
			params.addParameter("cityName",detail.city);
			params.addParameter("name",detail.nice);
			params.addParameter("introduction",_content);
			params.addParameter("quality",detail.quality);
			params.addParameter("mobile",detail.masterPhone);
			params.addParameter("user",detail.masterName);
			params.addParameter("originalCost",detail.oldPrice);
			params.addParameter("currentCost",detail.price);
			
			
		
	    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.LEAVE_ADD, null, params, _handler,HTTP_RESPONSE_ACCIDENT_ADD , MLLeaveServices.getInstance());
	    
	    Map<String, Object> otherParam = new HashMap<String, Object>();
	    otherParam.put("image", detail.paths);
	    message2.setOtherParmas(otherParam);
	    loadDataWithMessage(_context, "正在发布...", message2);
	}
	
	 private static final int HTTP_RESPONSE_ACCIDENT_ADD= 0;
	    private Handler _handler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	            super.handleMessage(msg);
	      	 // dismissProgressDialog();
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
	           //事故车添加
	            case HTTP_RESPONSE_ACCIDENT_ADD:{
	                	MLRegister ret = (MLRegister) msg.obj;
	                	if(ret.state.equalsIgnoreCase("1")){
	                		showMessageSuccess("二手件发布成功!");
	                		((MLAuxiliaryActivity)_context).setResult(MLConstants.RESULT_ACCEIDENT_ADD);
	                		((MLAuxiliaryActivity)_context).finish();
	                	}else{
	                		showMessageError("二手件发布失败!");
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
