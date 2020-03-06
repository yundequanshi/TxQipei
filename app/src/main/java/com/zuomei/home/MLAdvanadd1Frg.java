package com.zuomei.home;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.ab.util.AbStrUtil;
import com.ab.util.AbViewUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.txsh.R;
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.MLConstants;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.ml.base.utils.IEvent;

/**
 * 优势件-step1
 * @author Marcello
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLAdvanadd1Frg extends BaseFragment{

	public static MLAdvanadd1Frg INSTANCE =null;
	
	public static MLAdvanadd1Frg instance(){
	//	if(INSTANCE==null){
			INSTANCE = new MLAdvanadd1Frg();
	//	}
		return INSTANCE;
	}
	
	@ViewInject(R.id.accident_gv_photo)
	private GridView _photoGv;
	
	@ViewInject(R.id.accident_add_root)
	private RelativeLayout _root;
	/*@ViewInject(R.id.violation_wb)
	private WebView _webview;*/
	private ArrayList<String> mPhotoList = new ArrayList<String>();
	private MLAccidentPhotoAdapter mImagePathAdapter;
	private Context _context;
	private MLMessageAddPop _pop;
	
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
	private MLMessagePhotoPop _photoPop;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.advan_add1, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		
		initView();
		return view;
	}
	
	
	private void initView() {
		mPhotoList.clear();
		mPhotoList.add(String.valueOf(R.drawable.message_cam_photo));
		mImagePathAdapter = new MLAccidentPhotoAdapter(_context, mPhotoList,116,116);
		_photoGv.setAdapter(mImagePathAdapter);
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
	
	@OnClick(R.id.top_back)
	public void backOnClick(View view){
		((MLAuxiliaryActivity)_context).finish();
	/*	((MLAuxiliaryActivity)_context).overridePendingTransition(android.R.anim.slide_in_left,    
				android.R.anim.slide_out_right);  */
	}
	
	/**
	  * @description  下一步
	  *
	  * @author marcello
	 */
	@OnClick(R.id.accident_btn_next)
	public void nextOnClick(View view){
		
		List<String> lists = mImagePathAdapter.getImagePaths();
		if(lists.size()<=1){
			showMessage("请添加照片!");
			return;
		}
		lists.remove(lists.size()-1
				);
		_event.onEvent(lists, MLConstants.MY_ADVAN_ADD2);
	}
	
	@OnItemClick(R.id.accident_gv_photo)
	public void photoOnItemClick(AdapterView<?> arg0, View arg1,	int position, long arg3){
		if(position==6){
			showMessage("最多添加6张图片!");
			return;
		}
		selectIndex = position;
		String path = mPhotoList.get(position);
		if(path.equalsIgnoreCase(String.valueOf(R.drawable.message_cam_photo))){
			_pop.showAtLocation(_root, Gravity.CENTER, 0, 0); 
		}else{
		
			_photoPop = new MLMessagePhotoPop(getActivity(), path);
		    _photoPop.showAtLocation(_root, Gravity.CENTER, 0, 0); 
		}
	}
	
	
	@OnClick(R.id.add_btn_cancel)
	public void cancelOnClick(View view){
		startActivity(new Intent(_context,MLHomeActivity.class));
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
				String path = mIntent.getStringExtra("PATH");
		    	if(D)Log.d(TAG, "裁剪后得到的图片的路径是 = " + path);
		    	mImagePathAdapter.addItem(mImagePathAdapter.getCount()-1,path);
		     	camIndex++;
		    	AbViewUtil.setAbsListViewHeight(_photoGv,3,25);
				break;
		}
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
