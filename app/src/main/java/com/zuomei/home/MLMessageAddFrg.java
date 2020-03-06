package com.zuomei.home;

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
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.exception.ZMParserException;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLMessagePublishResponse;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLMessageServices;
import com.zuomei.utils.MLToolUtil;
import com.zuomei.utils.ZMJsonParser;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.ml.base.utils.IEvent;

/**
 * 发表消息
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMessageAddFrg extends BaseFragment{

	public static MLMessageAddFrg INSTANCE =null;
	
	public static MLMessageAddFrg instance(){
	//	if(INSTANCE==null){
			INSTANCE = new MLMessageAddFrg();
	//	}
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
/*	@ViewInject(R.id.add_gv_photo)
	private GridView _photoGv;*/
	
	@ViewInject(R.id.add_phone)
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
		View view = inflater.inflate(R.layout.message_add, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		
		initView();
		return view;
	}
	
	
	private void initView() {
	/*	mPhotoList.clear();
		mPhotoList.add(String.valueOf(R.drawable.message_cam_photo));*/
	//	mImagePathAdapter = new MLMessagePhotoAdapter(_context, mPhotoList,116,116);
	//	_photoGv.setAdapter(mImagePathAdapter);
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
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
						intent.setType("image/*");
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
	private boolean isAdd = false;
	@OnClick(R.id.add_btn_ok)
	public void okOnClick(View view){
		if(isAdd){
			showMessageWarning("发布中");
			return;
		}
		isAdd = true;
		
		String message = _contentEt.getText().toString();
		if(MLToolUtil.isNull(message)){
			showMessageWarning("请填写消息内容!");
			isAdd = false;
			return ;
		}
		
		if(_photoPath.equalsIgnoreCase("")){
			publish(null);
			return;
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
    	        	isAdd=false;
    	        }
    	});
	}
	
	/**
	  * @description  发表互动
	  *
	  * @author marcello
	 */  
	private void publish(String id){
		MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
		  ZMRequestParams params = new ZMRequestParams();
		if(!MLToolUtil.isNull(id)){
			  params.addParameter(MLConstants.PARAM_MESSAGE_IMAGE,id);
		}
		if(user.isDepot){
			params.addParameter(MLConstants.PARAM_HOME_DEPORT,user.Id);
		}else{
			params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,user.Id);
		}
		String content = _contentEt.getText().toString();
				content = content.replaceAll("\r|\n", "");
	params.addParameter(MLConstants.PARAM_MESSAGE_CONTENT,content);
	params.addParameter("cityId",BaseApplication._currentCity);
		
	    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.MESSAGE_PUBLISH, null, params, _handler,HTTP_RESPONSE_PUBLICH , MLMessageServices.getInstance());
	       loadDataWithMessage(_context, "正在发布...", message2);
	}
	
/*	@OnItemClick(R.id.add_gv_photo)
	public void photoOnItemClick(AdapterView<?> arg0, View arg1,	int position, long arg3){
		selectIndex = position;
		if(selectIndex == camIndex){
			_pop.showAtLocation(_root, Gravity.CENTER, 0, 0); 
		}else{
			String path = mPhotoList.get(position);
			_photoPop = new MLMessagePhotoPop(getActivity(), path);
		    _photoPop.showAtLocation(_root, Gravity.CENTER, 0, 0); 
		}
		String path = mPhotoList.get(position);
		if(path.equalsIgnoreCase(String.valueOf(R.drawable.message_cam_photo))){
			_pop.showAtLocation(_root, Gravity.CENTER, 0, 0); 
		}else{
		
			_photoPop = new MLMessagePhotoPop(getActivity(), path);
		    _photoPop.showAtLocation(_root, Gravity.CENTER, 0, 0); 
		}
	}*/
	

	@OnClick(R.id.add_phone)
	public void addOnClick(View view){
		if(_photoPath.equalsIgnoreCase("")){
			_pop.showAtLocation(_root, Gravity.CENTER, 0, 0); 
		}else{
			_photoPop = new MLMessagePhotoPop(getActivity(), _photoPath);
		    _photoPop.showAtLocation(_root, Gravity.CENTER, 0, 0); 
		}
		
	}
	
	
	@OnClick(R.id.add_btn_cancel)
	public void cancelOnClick(View view){
		//startActivity(new Intent(_context,MLHomeActivity.class));
		((MLAuxiliaryActivity)_context).finish();
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
		    	/*mImagePathAdapter.addItem(mImagePathAdapter.getCount()-1,_photoPath);
		     	camIndex++;*/
		    //	AbViewUtil.setAbsListViewHeight(_photoGv,3,25);
		    	if(!MLToolUtil.isNull(_photoPath)){
		    	Drawable d = Drawable.createFromPath(_photoPath);
		    	_photoIv.setImageDrawable(d);
		    	}
				break;
		}
	}

		 private static final int HTTP_RESPONSE_PUBLICH = 0;
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
		             //发表互动消息
		            case  HTTP_RESPONSE_PUBLICH:{
		            	isAdd = false;
		            		MLRegister ret = (MLRegister) msg.obj;
		            		if(ret.state.equalsIgnoreCase("1")){
		            			showMessageSuccess("发表成功!");
		                    	((MLAuxiliaryActivity)_context).setResult(1);
				            	((MLAuxiliaryActivity)_context).finish();
		                	}else{
		                		showMessage("发表互动消息失败!");
		                	}
		            	break;
		            }
	                default:
	                    break;
	            }
	        }
	    };
		
		
		
	@Override
			public void onPause() {
				// TODO Auto-generated method stub
				super.onPause();
				_photoPath = "";
				isAdd = false;
			}


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
