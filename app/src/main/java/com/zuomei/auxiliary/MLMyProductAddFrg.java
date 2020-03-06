package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ab.util.AbStrUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.exception.ZMParserException;
import com.zuomei.home.MLCropImageActivity;
import com.zuomei.home.MLMessageAddPop;
import com.zuomei.home.MLMessagePhotoAdapter;
import com.zuomei.home.MLMessagePhotoPop;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLMessagePublishResponse;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLMyServices;
import com.zuomei.utils.MLToolUtil;
import com.zuomei.utils.ZMJsonParser;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.ml.base.utils.IEvent;

/**
 * 商品管理-发布
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyProductAddFrg extends BaseFragment{

	public static MLMyProductAddFrg INSTANCE =null;
	
	public static MLMyProductAddFrg instance(){
//		if(INSTANCE==null){
			INSTANCE = new MLMyProductAddFrg();
//		}
		return INSTANCE;
	}
	private static final String TAG = "AddPhotoActivity";
	private static final boolean D =true;
	private int selectIndex = 0;
	private int camIndex = 0;
	/* 用来标识请求照相功能的activity */
	private static final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求gallery的activity */
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	/* 用来标识请求裁剪图片后的activity */
	private static final int CAMERA_CROP_DATA = 3022;
	/* 拍照的照片存储位置 */
	private  File PHOTO_DIR = null;
	// 照相机拍照得到的图片
	private File mCurrentPhotoFile;
	private String mFileName;
	
	private ArrayList<String> mPhotoList = new ArrayList<String>();
	@ViewInject(R.id.product_iv_add)
	private ImageView _photoIv;
	private MLMessagePhotoAdapter mImagePathAdapter;
	
	@ViewInject(R.id.message_add_root)
	private RelativeLayout _root;
	private Context _context;
	
	
	@ViewInject(R.id.add_btn_cancel)
	private Button _cancelBtn;
	
	@ViewInject(R.id.add_et_message)
	private EditText _contentEt;
	private MLMessageAddPop _pop;
	private MLMessagePhotoPop _photoPop;
	
	private String _photoPath = "";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_product_add, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		
		initView();
		return view;
	}
	
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		_contentEt.setText("");
	}


	private void initView() {
		mPhotoList.clear();
		 View mAvatarView = LayoutInflater.from(_context).inflate(R.layout.message_choose_avatar, null);
		 _pop = new MLMessageAddPop(getActivity(), mAvatarView);
			Button albumButton = (Button)mAvatarView.findViewById(R.id.choose_album);
			Button camButton = (Button)mAvatarView.findViewById(R.id.choose_cam);
			Button cancelButton = (Button)mAvatarView.findViewById(R.id.choose_cancel);
			
			albumButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// 从相册中去获取
					try {
						Intent intent = new Intent(Intent.ACTION_PICK, null);
						intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
						startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
					} catch (ActivityNotFoundException e) {
						showMessage("没有找到照片");
					}
				}
			});
			camButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					doPickPhotoAction();
				}
			});
			
			cancelButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					_pop.dismiss();
				}
			});
	}

	@OnClick(R.id.add_btn_ok)
	public void okOnClick(View view){
		
		if(_photoPath.equalsIgnoreCase("")){
		showMessage("请选择产品图片!");
		return ;
		}	
		
		String name = _contentEt.getText().toString();
		if(MLToolUtil.isNull(name)){
			showMessage("请填写产品名称!");
			return ;
		}
		
	   RequestParams params = new RequestParams();
    	params.addBodyParameter("file", new File(_photoPath));
    	
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
						publish(ret.datas);
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
	}
	
	/**
	  * @description  产品发布
	  *
	  * @author marcello
	 */  
	private void publish(String id){
		MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
		  ZMRequestParams params = new ZMRequestParams();
			  params.addParameter(MLConstants.PARAM_MESSAGE_IMAGE,id);
			params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,user.Id);
			
	params.addParameter(MLConstants.PARAM_MY_SHOPNAME,_contentEt.getText().toString());   
	    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.MY_PRODUCT_ADD, null, params, _handler, HTTP_RESPONSE_PRODUCT_ADD, MLMyServices.getInstance());
	       loadDataWithMessage(_context, "正在发布,请稍等...", message2);
	}
	
	
	@OnClick(R.id.product_iv_add)
	public void photoOnClick(View view){
		_pop.showAtLocation(_root, Gravity.CENTER, 0, 0); 
	}
	
	@OnClick(R.id.top_back)
	public void backOnClick(View view){
		getActivity().onBackPressed();
	}
	/**
	 * 描述：因为调用了Camera和Gally所以要判断他们各自的返回情况,
	 * 他们启动时是这样的startActivityForResult  
	 */
		@Override
	public void onActivityResult(int requestCode, int resultCode, Intent mIntent) {
		_pop.dismiss();
		if (resultCode != Activity.RESULT_OK){
			return;
		}
		switch (requestCode) {
			case PHOTO_PICKED_WITH_DATA:
				Uri uri = mIntent.getData();
				String currentFilePath = getPath(uri);
				if(!AbStrUtil.isEmpty(currentFilePath)){
					Intent intent1 = new Intent(getActivity(), MLCropImageActivity.class);
					intent1.putExtra("PATH", currentFilePath);
					startActivityForResult(intent1, CAMERA_CROP_DATA);
		        }else{
		        	showMessage("未在存储卡中找到这个文件");
		        }
				break;  
			case CAMERA_WITH_DATA:
				if(D)Log.d(TAG, "将要进行裁剪的图片的路径是 = " + mCurrentPhotoFile.getPath());
				String currentFilePath2 = mCurrentPhotoFile.getPath();
				Intent intent2 = new Intent(getActivity(), MLCropImageActivity.class);
				intent2.putExtra("PATH",currentFilePath2);
				startActivityForResult(intent2,CAMERA_CROP_DATA);
				break;
			case CAMERA_CROP_DATA:
				_photoPath= mIntent.getStringExtra("PATH");
		    	if(D)Log.d(TAG, "裁剪后得到的图片的路径是 = " + _photoPath);
		     	
		     Drawable drawable = Drawable.createFromPath(_photoPath);
		     _photoIv.setImageDrawable(drawable);	
				break;
		}
	}

		 private static final int HTTP_RESPONSE_PUBLICH = 0;
		 private static final int HTTP_RESPONSE_PRODUCT_ADD = 1;
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
		            case  HTTP_RESPONSE_PUBLICH:{
		            		MLRegister ret = (MLRegister) msg.obj;
		            		if(ret.state.equalsIgnoreCase("1")){
		            			showMessage("发表成功!");
		                    	((MLAuxiliaryActivity)_context).setResult(1);
				            	((MLAuxiliaryActivity)_context).finish();
		                	}else{
		                		showMessage("发表互动消息失败!");
		                	}
		            	break;
		            }
		            
		            case HTTP_RESPONSE_PRODUCT_ADD:{
		            	MLRegister ret = (MLRegister) msg.obj;
	            		if(ret.state.equalsIgnoreCase("1")){
	            			showMessageSuccess("发布成功!");
	            			_event.onEvent(null, MLConstants.MY_PRODUCT);
	                    /*	((MLAuxiliaryActivity)_context).setResult(1);
			            	((MLAuxiliaryActivity)_context).finish();*/
	                	}else{
	                		showMessage("发布失败!");
	                	}
		            	break;
		            }
	                default:
	                    break;
	            }
	        }
	    };
		
		
		
	/**
	 * 描述：从照相机获取
	 */
	private void doPickPhotoAction() {
		String status = Environment.getExternalStorageState();
		//判断是否有SD卡,如果有sd卡存入sd卡在说，没有sd卡直接转换为图片
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			doTakePhoto();
		} else {
			showMessage("没有可用的存储卡");
		}
	}
	
	/**
	 * 拍照获取图片
	 */
	protected void doTakePhoto() { 
		try {
			
		//	mFileName = System.currentTimeMillis() + ".jpg";
			/*mCurrentPhotoFile= new File(Environment.getExternalStorageDirectory(),
		             getPhotoFileName());*/
			mCurrentPhotoFile = getAttachFolder();
			//mCurrentPhotoFile = new File(PHOTO_DIR, mFileName);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (Exception e) {
			showMessage("未找到系统相机程序");
		}
	}
	/**
	 * 从相册得到的url转换为SD卡中图片路径
	 */
	public String getPath(Uri uri) {
		if(AbStrUtil.isEmpty(uri.getAuthority())){
			return null;
		}
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		return path;
	}
	
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }
	private IEvent<Object> _event;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_event = (IEvent<Object>) activity;
	}
}
