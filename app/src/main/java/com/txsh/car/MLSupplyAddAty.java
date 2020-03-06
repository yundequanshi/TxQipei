package com.txsh.car;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.util.AbStrUtil;
import com.ab.util.AbViewUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.txsh.R;
import com.txsh.model.FileUpload;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.home.MLAccidentPhotoAdapter;
import com.zuomei.home.MLAccidentPhotoAdapter.GetRefusePhoto;
import com.zuomei.home.MLMessageAddPop;
import com.zuomei.home.MLMessagePhotoPop;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLAccidentServices;
import com.zuomei.utils.MLToolUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ml.base.utils.MLStrUtil;
import cn.ml.base.utils.photo.MLPhotoUtil;

/**
 * Created by Marcello on 2015/6/11.
 */
public class MLSupplyAddAty extends BaseActivity implements GetRefusePhoto{
	private static final String TAG = "MLInteractAddAty";
	private static final boolean D = true;
	private int selectIndex = 0;
	private ArrayList<String> mPhotoList = new ArrayList<String>();
	private MLAccidentPhotoAdapter mImagePathAdapter;
	private MLMessageAddPop _pop;
	private MLMessagePhotoPop _photoPop;
	/* 用来标识请求gallery的activity */
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	// 照相机拍照得到的图片
	private File mCurrentPhotoFile;
	/* 用来标识请求照相功能的activity */
	private static final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求裁剪图片后的activity */
	private static final int CAMERA_CROP_DATA = 3022;
	private int camIndex = 0;
	@ViewInject(R.id.accident_add_root)
	private LinearLayout _root;

	@ViewInject(R.id.accident_gv_photo)
	private GridView _photoGv;
	@ViewInject(R.id.last_hint)
	private TextView last_hint;

	//供求 选择
	@ViewInject(R.id.accident_tv_city)
	private TextView mTvSupply;
	//联系方式
	@ViewInject(R.id.add_tv_phone)
	private EditText mTvPhone;

	//姓名
	@ViewInject(R.id.add_tv_name)
	private EditText mEtName;
	//姓名
	@ViewInject(R.id.add_tv_content)
	private EditText mEtContent;


/*
	@ViewInject(R.id.interact_et_content)
	private EditText mEtContent;
*/


	private int supply=2;
	private List<String> imageurl = new ArrayList<String>();

	private Gson gson;

	private List<Map<String, String>> imageuploadList = new ArrayList<Map<String, String>>();

	private List<FileUpload> voiceuploadList = new ArrayList<FileUpload>();
	private int i = 0;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tx_supply_add);
		ViewUtils.inject(this);


		init();
		gson = new Gson();

		last_hint.setText(Html.fromHtml(String
				.format("点击添加6张照片，请从<font color=\"#279efa\">%s</font>展示车辆。",
						"正面、侧面、细节")));
		MLPhotoUtil.clear();
		mPhotoList.clear();
		mPhotoList.add(String.valueOf(R.drawable.ershouchetjann));
		mImagePathAdapter = new MLAccidentPhotoAdapter(this, mPhotoList, 400,
				300);
		mImagePathAdapter.setHandler(this);
		_photoGv.setAdapter(mImagePathAdapter);
		View mAvatarView = LayoutInflater.from(this).inflate(
				R.layout.message_choose_avatar, null);
		_pop = new MLMessageAddPop(this, mAvatarView);
		Button albumButton = (Button) mAvatarView
				.findViewById(R.id.choose_album);
		Button camButton = (Button) mAvatarView.findViewById(R.id.choose_cam);
		Button cancelButton = (Button) mAvatarView
				.findViewById(R.id.choose_cancel);

		albumButton.setOnClickListener(new OnClickListener() {
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
		camButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				doPickPhotoAction();
			}
		});

		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_pop.dismiss();
			}
		});





	}

	private void init() {
		String name = BaseApplication.aCache.getAsString(MLConstants.PARAM_REGISTER_USERNAME);
		mTvPhone.setText(name);
	}


	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		isAdd = false;
	}

	@OnClick({R.id.add_rl_supply})
	public void onClick(View view){
		switch (view.getId()){
			case R.id.add_rl_supply:{
			//供求选择
				final String s[] = {"求购","供应"};
				AlertDialog builder = new AlertDialog.Builder(MLSupplyAddAty.this, AlertDialog.THEME_HOLO_LIGHT)
						.setItems(s, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								mTvSupply.setText(s[which]);
								supply = which;
							}
						}).create();
				builder.show();
				break;
			}


		}
	}

	@OnItemClick(R.id.accident_gv_photo)
	public void photoOnItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		if (position == 6) {
			showMessage("最多添加6张图片!");
			return;
		}
		selectIndex = position;
		String path = mPhotoList.get(position);
		if (path.equalsIgnoreCase(String.valueOf(R.drawable.ershouchetjann))) {
			_pop.showAtLocation(_root, Gravity.CENTER, 0, 0);
		} else {

			_photoPop = new MLMessagePhotoPop(this, path);
			_photoPop.showAtLocation(_root, Gravity.CENTER, 0, 0);
		}
	}

	/**
	 * 描述：从照相机获取
	 */
	private void doPickPhotoAction() {
		String status = Environment.getExternalStorageState();
		// 判断是否有SD卡,如果有sd卡存入sd卡在说，没有sd卡直接转换为图片
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

			 String mFileName = System.currentTimeMillis() + ".jpg";
			
			 mCurrentPhotoFile= new
			 File(Environment.getExternalStorageDirectory(),
					 mFileName);
			 
//			mCurrentPhotoFile = getAttachFolder();
			// mCurrentPhotoFile = new File(PHOTO_DIR, mFileName);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(mCurrentPhotoFile));
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (Exception e) {
			showMessage("未找到系统相机程序");
		}
	}

	File _attachDirFile = null;

	public File getAttachFolder() {
		if (_attachDirFile == null) {
			File file = BaseApplication.getInstance().getExternalCacheDir();
			String subPath = String.format("%s/image", file.getAbsolutePath());
			/*
			 * File file = getActivity().getFilesDir(); String subPath =
			 * String.format("%s/uploadAttach",file.getAbsolutePath()); File
			 * subFile = new File(subPath); return subFile.getAbsoluteFile();
			 */
			File subFile = new File(subPath);
			return subFile.getAbsoluteFile();
		}
		return _attachDirFile;
	}

	@OnClick(R.id.top_btn_left)
	public void backOnClick(View view) {
		finish();
	}

	private boolean isAdd = false;

	@OnClick(R.id.accident_btn_next)
	public void addOnClick(View view) {


		if(supply==2){
			showMessage("请先选择供求选项");
			return;
		}

		String phone = mTvPhone.getText().toString();
		String name = mEtName.getText().toString();
		String content = mEtContent.getText().toString();

		if(MLStrUtil.isEmpty(phone)){
			showMessage("手机号不能为空");
			return;
		}

		if(MLStrUtil.isEmpty(name)){
			showMessage("产品名称不能为空");
			return;
		}

		if(MLStrUtil.isEmpty(content)){
			showMessage("产品说明不能为空");
			return;
		}

		// if (isAdd) {
		// showMessage("发布中");
		// return;
		// }
		// isAdd = true;

		String message = mEtContent.getText().toString();
		if (MLToolUtil.isNull(message) && imageurl.size() == 0) {
			showMessage("请填写消息内容或上传图片");
			// isAdd = false;
			return;
		}

		if(imageuploadList.size()==0){
			publish();
		}else{
			showProgressDialog(MLSupplyAddAty.this);
		}

		for (i = 0; i < imageurl.size(); i++) {
			RequestParams params = new RequestParams();
			HttpUtils http = new HttpUtils();
			params.addBodyParameter("file", new File(imageurl.get(i)));
			http.send(HttpRequest.HttpMethod.POST,
					APIConstants.API_IMAGE_UPLOAD
							+ APIConstants.API_UPLOAD_IMAGES, params,
					new RequestCallBack<String>() {

						@Override
						public void onStart() {
						}

						@Override
						public void onLoading(long total, long current,
								boolean isUploading) {
						}

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							FileUpload imageUpload = gson.fromJson(
									responseInfo.result, FileUpload.class);
							Map<String, String> imageMap = new HashMap<String, String>();
							imageMap.put("path", imageUpload.getRes().getData()
									.get(0).getPath());
							imageuploadList.add(imageMap);
							if (imageuploadList.size() == i) {
								publish();
							}
							return;
						}

						@Override
						public void onFailure(HttpException error, String msg) {
							//uploadVoice();
						}
					});
		}


	}



	/**
	 * @description 发表互动
	 * 
	 * @author marcello
	 */
	private void publish() {
		MLLogin user = BaseApplication.getInstance().get_user();
		ZMRequestParams params = new ZMRequestParams();
		gson.toJsonTree(imageuploadList);
		if (imageuploadList.size() > 0) {
			params.addParameter("images", gson.toJsonTree(imageuploadList)
					.toString());
		}
		if (user.isDepot) {
			params.addParameter("depotId", user.Id);
		} else {
			params.addParameter("companyId", user.Id);
		}
		params.addParameter("type", supply+"");
		params.addParameter("name", mEtName.getText().toString());

		String content = mEtContent.getText().toString();
		content = content.replaceAll("\r|\n", "");
		params.addParameter("content", content);
		params.addParameter("mobile", mTvPhone.getText().toString());

		ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
				ZMHttpType.RequestType.SUPPLY_ADD, null, params, _handler,
				HTTP_RESPONSE_PUBLICH, MLAccidentServices.getInstance());
		loadDataWithMessage("正在发布...", message2);

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
			// 发表互动消息
			case HTTP_RESPONSE_PUBLICH: {
				isAdd = false;
				MLRegister ret = (MLRegister) msg.obj;
				if (ret.state.equalsIgnoreCase("1")) {
					showMessageSuccess("发布成功!");
					setResult(2);
					//EventBus.getDefault().post(new EventBusModel("refuse"));
					finish();
				} else {
					showMessage("发布失败!");
					imageuploadList.clear();
					voiceuploadList.clear();
				}
		break;
			}
			default:
				break;
			}
		}
	};

	/**
	 * 描述：因为调用了Camera和Gally所以要判断他们各自的返回情况, 他们启动时是这样的startActivityForResult
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent mIntent) {
		_pop.dismiss();
		if (resultCode != android.app.Activity.RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case PHOTO_PICKED_WITH_DATA:
			Uri uri = mIntent.getData();
			String currentFilePath = getPath(uri);
			
//			imageurl.add(currentFilePath);
//			mImagePathAdapter.addItem(mImagePathAdapter.getCount() - 1, currentFilePath);
//			camIndex++;
//			AbViewUtil.setAbsListViewHeight(_photoGv, 3, 25);
//			if (!AbStrUtil.isEmpty(currentFilePath)) {
//				Intent intent1 = new Intent(this, MLCropImageActivity.class);
//				intent1.putExtra("PATH", currentFilePath);
//				startActivityForResult(intent1, CAMERA_CROP_DATA);
//			} else {
//				showMessage("未在存储卡中找到这个文件");
//			}
			startPhotoZoom(uri);
			break;
		case CAMERA_WITH_DATA:
			if (D)
				Log.d(TAG, "将要进行裁剪的图片的路径是 = " + mCurrentPhotoFile.getPath());
			String currentFilePath2 = mCurrentPhotoFile.getPath();
			Uri uri2 = Uri.fromFile(mCurrentPhotoFile);
			startPhotoZoom(uri2);
//			imageurl.add(currentFilePath2);
//			mImagePathAdapter.addItem(mImagePathAdapter.getCount() - 1, currentFilePath2);
//			camIndex++;
//			AbViewUtil.setAbsListViewHeight(_photoGv, 3, 25);
//			Intent intent2 = new Intent(this, MLCropImageActivity.class);
//			intent2.putExtra("PATH", currentFilePath2);
//			startActivityForResult(intent2, CAMERA_CROP_DATA);
			break;
		case CAMERA_CROP_DATA:
			
			  Bundle extras = mIntent.getExtras();    
			  String currentFilePath3 = mCurrentPhotoFile.getPath();
			
//			String path = mIntent.getStringExtra("PATH");
//			if (D)
//				Log.d(TAG, "裁剪后得到的图片的路径是 = " + path);
			imageurl.add(currentFilePath3);
			mImagePathAdapter.addItem(mImagePathAdapter.getCount() - 1, currentFilePath3);
			camIndex++;
			AbViewUtil.setAbsListViewHeight(_photoGv, 3, 25);
			break;
		}
	}

	
	/**   
     * 裁剪图片方法实现   
     * @param uri   
     */   
    public void startPhotoZoom(Uri uri) {    
        /*   
         * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页   
         * yourself_sdk_path/docs/reference/android/content/Intent.html   
         * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能,   
         * 是直接调本地库的，小马不懂C C++  这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么   
         * 制做的了...吼吼   
         */   
        Intent intent = new Intent("com.android.camera.action.CROP");    
        intent.setDataAndType(uri, "image/*");    
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪    
        intent.putExtra("crop", "true");    
        // aspectX aspectY 是宽高的比例    
        intent.putExtra("aspectX", 1);    
        intent.putExtra("aspectY", 1);    
        // outputX outputY 是裁剪图片宽高    
        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 600);
      //  intent.putExtra("return-data", true);
        
        
        String mFileName = System.currentTimeMillis() + ".jpg";
		
		 mCurrentPhotoFile= new
		 File(Environment.getExternalStorageDirectory(),
				 mFileName);
		 
//		mCurrentPhotoFile = getAttachFolder();
		// mCurrentPhotoFile = new File(PHOTO_DIR, mFileName);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(mCurrentPhotoFile));
        
        startActivityForResult(intent, CAMERA_CROP_DATA);    
    }    
	/**
	 * 从相册得到的url转换为SD卡中图片路径
	 */
	@SuppressWarnings("deprecation")
	public String getPath(Uri uri) {
		if (AbStrUtil.isEmpty(uri.getAuthority())) {
			return null;
		}
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = MLSupplyAddAty.this.managedQuery(uri, projection,
				null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		return path;
	}



	@Override
	public void onGetRefusePhoto(String number) {
		// TODO Auto-generated method stub
        String msg = number; 
        imageurl.remove(Integer.parseInt(msg));
	}
}
