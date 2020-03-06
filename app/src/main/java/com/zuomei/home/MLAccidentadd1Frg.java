package com.zuomei.home;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.util.AbStrUtil;
import com.ab.util.AbViewUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.txsh.R;
import com.txsh.utils.GridViewInScrollView;
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.MLConstants;
import com.zuomei.home.MLAccidentPhotoAdapter.GetRefusePhoto;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.login.MLLoginCityPop;
import com.zuomei.model.MLAccidentInfo;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLAccidentServices;
import com.zuomei.utils.MLToolUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ml.base.utils.IEvent;
import cn.ml.base.utils.MLDateUtil;
import cn.ml.base.utils.MLStrUtil;

/**
 * 发布信息-step1
 * 
 * @author Marcello
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLAccidentadd1Frg extends BaseFragment implements GetRefusePhoto {

	public static MLAccidentadd1Frg INSTANCE = null;

	public static MLAccidentadd1Frg instance() {
		// if(INSTANCE==null){
		INSTANCE = new MLAccidentadd1Frg();
		// }
		return INSTANCE;
	}

	@ViewInject(R.id.accident_gv_photo)
	private GridViewInScrollView _photoGv;

	@ViewInject(R.id.accident_add_root)
	private RelativeLayout _root;
	/*
	 * @ViewInject(R.id.violation_wb) private WebView _webview;
	 */
	private ArrayList<String> mPhotoList = new ArrayList<String>();
	private MLAccidentPhotoAdapter mImagePathAdapter;
	private Context _context;
	private MLMessageAddPop _pop;
	private List<Map<String, String>> imageuploadList = new ArrayList<Map<String, String>>();
	private static final String TAG = "AddPhotoActivity";
	private static final boolean D = true;
	private int selectIndex = 0;
	private int camIndex = 0;
	/* 用来标识请求照相功能的activity */
	private static final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求gallery的activity */
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	/* 用来标识请求裁剪图片后的activity */
	private static final int CAMERA_CROP_DATA = 3022;
	/* 拍照的照片存储位置 */
	private File PHOTO_DIR = null;
	// 照相机拍照得到的图片
	private File mCurrentPhotoFile;
	private String mFileName;
	private MLMessagePhotoPop _photoPop;

	// -----------------------------------------------------

	public static List<String> _paths;
	private int i = 0;

	/** 车型名称 */
	@ViewInject(R.id.accident_tv_nice)
	private EditText _niceTv;

	/** 上牌时间 */
	@ViewInject(R.id.accident_tv_platedata)
	private TextView _platedataTv;

	/** 所在城市 */
	@ViewInject(R.id.accident_tv_city)
	private TextView _cityTv;

	/** 行驶里程 */
	@ViewInject(R.id.accident_tv_mileage)
	private EditText _mileageTv;

	/** 受损部位 */
	@ViewInject(R.id.accident_tv_damaged)
	private TextView _damagedTv;

	/** 预售价格 */
	@ViewInject(R.id.accident_tv_price)
	private EditText _priceTv;

	/** 买时裸车价 */
/*	@ViewInject(R.id.accident_tv_oldprice)
	private EditText _oldpriceTv;*/

	/** 名字 */
	@ViewInject(R.id.accident_tv_title)
	private EditText _nameTv;

	/** 排量 */
	@ViewInject(R.id.accident_tv_displacement)
	private EditText _displacementTv;

	/** 电话 */
	@ViewInject(R.id.accident_tv_phone)
	private EditText _phoneTv;

	@ViewInject(R.id.paizhao)
	private Button paizhao;

	@ViewInject(R.id.xiangcexuanze)
	private Button xiangcexuanze;

	// private MLWheelPop _timePop;
	// ----------------------------------------------------

	// ========================================================================

	private static MLAccidentInfo detail;

	@ViewInject(R.id.accident_et_content)
	private EditText _contentEt;

	@ViewInject(R.id.accident_add_root)
	/*
	 * @ViewInject(R.id.violation_wb) private WebView _webview;
	 */
	private String _content;
	// ========================================================================

	// *******************************
	List<String> lists;

	// **************************
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.accident_add1, null);
		ViewUtils.inject(this, view);

		_context = inflater.getContext();

		initView();
		return view;
	}

	private void initView() {
		mPhotoList.clear();
		mPhotoList.add(String.valueOf(R.drawable.cemianzhengti));
		mPhotoList.add(String.valueOf(R.drawable.fadongjicang));
		mPhotoList.add(String.valueOf(R.drawable.qianfang45du));
		mPhotoList.add(String.valueOf(R.drawable.zhengqianfangtu));
		mPhotoList.add(String.valueOf(R.drawable.zhongkongqu));
		mPhotoList.add(String.valueOf(R.drawable.zuoyiqu));

		mImagePathAdapter = new MLAccidentPhotoAdapter(_context, mPhotoList,
				400, 240);
		mImagePathAdapter.setHandler(this);
		_photoGv.setAdapter(mImagePathAdapter);
		View mAvatarView = LayoutInflater.from(_context).inflate(
				R.layout.message_choose_avatar, null);
		_pop = new MLMessageAddPop(getActivity(), mAvatarView);
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
					Intent intent = new Intent(Intent.ACTION_PICK, null);
					intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
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

	/**
	 * @description 下一步
	 * 
	 * @author marcello
	 */
	public void nextOnClick2() {

		lists = mImagePathAdapter.getImagePaths();
		if (lists.size() < 1) {
			showMessage("请添加照片!");
			return;
		}
		// lists.remove(lists.size()-1
		// );
		// _event.onEvent(lists, MLConstants.ACCIDENT_ADD2);
	}

	@OnItemClick(R.id.accident_gv_photo)
	public void photoOnItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// if(position==6){
		// showMessage("最多添加6张图片!");
		// return;
		// }
		selectIndex = position;
		String path = mPhotoList.get(position);
		if (path.equals("2130837615") || path.equals("2130837567")
				|| path.equals("2130837621") || path.equals("2130837868")
				|| path.equals("2130837982") || path.equals("2130837985")
				|| path.equals("2130837988")) {
		} else {

			_photoPop = new MLMessagePhotoPop(getActivity(), path);
			_photoPop.showAtLocation(_root, Gravity.CENTER, 0, 0);
		}
	}

	@OnClick(R.id.paizhao)
	public void paizhaoClick(View view) {
		if (i != 6) {
			doPickPhotoAction();
		} else {
			showMessage("最多添加6张照片");
		}
	}

	@OnClick(R.id.xiangcexuanze)
	public void xiangcexuanzeClick(View view) {
		// 从相册中去获取
		try {
			if (i != 6) {
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
			} else {
				showMessage("最多添加6张照片");
			}
		} catch (ActivityNotFoundException e) {
			showMessage("没有找到照片");
		}
	}

	@OnClick(R.id.add_btn_cancel)
	public void cancelOnClick(View view) {
		startActivity(new Intent(_context, MLHomeActivity.class));
		// ((MLAuxiliaryActivity)_context).finish();
		getActivity().finish();
	}

	/**
	 * 描述：因为调用了Camera和Gally所以要判断他们各自的返回情况, 他们启动时是这样的startActivityForResult
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent mIntent) {
		_pop.dismiss();
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case PHOTO_PICKED_WITH_DATA:
			Uri uri = mIntent.getData();
			String currentFilePath = getPath(uri);
//			if (!AbStrUtil.isEmpty(currentFilePath)) {
//				Intent intent1 = new Intent(getActivity(),
//						MLCropImageActivity.class);
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
//			Intent intent2 = new Intent(getActivity(),
//					MLCropImageActivity.class);
//			intent2.putExtra("PATH", currentFilePath2);
//			startActivityForResult(intent2, CAMERA_CROP_DATA);
			
			Uri uri2 = Uri.fromFile(mCurrentPhotoFile);
			startPhotoZoom(uri2);
			break;
		case CAMERA_CROP_DATA:
//			String path = mIntent.getStringExtra("PATH");
			String path = mCurrentPhotoFile.getPath();
			if (D)
				Log.d(TAG, "裁剪后得到的图片的路径是 = " + path);

			if (mPhotoList.size() == 6) {
				mImagePathAdapter.addItem(0, path);
			}
			if (mPhotoList.size() == 5) {
				mImagePathAdapter.addItem(1, path);
			}
			if (mPhotoList.size() == 4) {
				mImagePathAdapter.addItem(2, path);
			}
			if (mPhotoList.size() == 3) {
				mImagePathAdapter.addItem(3, path);
			}
			if (mPhotoList.size() == 2) {
				mImagePathAdapter.addItem(4, path);
			}
			if (mPhotoList.size() == 1) {
				mImagePathAdapter.addItem(5, path);
			}
			if (mPhotoList.size() == 0) {
				mImagePathAdapter.addItem(6, path);
			}
			i = i + 1;
			mPhotoList.remove(i);

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
     //   intent.putExtra("return-data", true);
        
        
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

			// mFileName = System.currentTimeMillis() + ".jpg";
			
			  mCurrentPhotoFile= new
			  File(Environment.getExternalStorageDirectory(),
			  getPhotoFileName());
			
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

	/**
	 * 从相册得到的url转换为SD卡中图片路径
	 */
	public String getPath(Uri uri) {
		if (AbStrUtil.isEmpty(uri.getAuthority())) {
			return null;
		}
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = getActivity().managedQuery(uri, projection, null, null,
				null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
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

	// -------------------------------------------------------------
	@OnClick(R.id.top_back)
	public void backOnClick(View view) {
		// startActivity(new Intent(_context,MLHomeActivity.class));
		// ((MLAuxiliaryActivity)_context).onBackPressed();
		getActivity().finish();
	}

	/**
	 * @description 下一步
	 * 
	 * @author marcello
	 */
	@OnClick(R.id.accident_btn_next)
	public void nextOnClick3() {

		String accidentName = _niceTv.getText().toString();
		String mtime = _platedataTv.getText().toString();
		String city = _cityTv.getText().toString();
		String mileage = _mileageTv.getText().toString();
		//String damaged = _damagedTv.getText().toString();
		String damaged = "正品";
	//	String oldPrice = _oldpriceTv.getText().toString();
		String displacement = _displacementTv.getText().toString();
		String price = _priceTv.getText().toString();
		String masterName = _nameTv.getText().toString();
		String masterPhone = _phoneTv.getText().toString();

		if (MLToolUtil.isNull(accidentName)) {
			showMessage("车型名称不能为空!");
			return;
		}
		if (MLToolUtil.isNull(mtime)) {
			showMessage("上牌时间不能为空!");
			return;
		}
		if (MLToolUtil.isNull(city)) {
			showMessage("所在城市不能为空!");
			return;
		}
		if (MLToolUtil.isNull(mileage)) {
			showMessage("行驶里程不能为空!");
			return;
		}
		mileage = mileage.replace("万公里", "");
	/*	if (MLToolUtil.isNull(damaged)) {
			showMessage("受损部位不能为空!");
			return;
		}*/
	/*	if (MLToolUtil.isNull(oldPrice)) {
			showMessage("买时裸车价不能为空!");
			return;
		}
		oldPrice = oldPrice.replace("万", "");*/
		if (MLToolUtil.isNull(displacement)) {
			showMessage("排量不能为空!");
			return;
		}
		displacement = displacement.replace("L", "");
		if (MLToolUtil.isNull(price)) {
			showMessage("预售价格不能为空!");
			return;
		}
		price = price.replace("万元", "");
		if (MLToolUtil.isNull(masterName)) {
			showMessage("姓名不能为空!");
			return;
		}

		if (MLToolUtil.isNull(masterPhone)) {
			showMessage("电话不能为空!");
			return;
		}

		detail = new MLAccidentInfo();
		detail.accidentName = accidentName.replaceAll("\\s*", "");;
		detail.city = city;
		detail.mileage = mileage;
		detail.damaged = damaged;
	//	detail.oldPrice = oldPrice;
		detail.oldPrice = "1";
		detail.price = price;
		detail.masterName = masterName;
		detail.masterPhone = masterPhone;
		detail.displacement = displacement;
		detail.paths = lists;
		detail.platedata = mtime;


		requestAccident();
		/**
		 * for(final String path : detail.paths){ //上传图片 RequestParams params =
		 * new RequestParams(); params.addBodyParameter("file", new File(path));
		 * HttpUtils http = new HttpUtils();
		 * http.send(HttpRequest.HttpMethod.POST,
		 * APIConstants.API_IMAGE_UPLOAD_OLD, params, new
		 * RequestCallBack<String>() {
		 * 
		 * @Override public void onStart() { }
		 * @Override public void onLoading(long total, long current, boolean
		 *           isUploading) { }
		 * @Override public void onSuccess(ResponseInfo<String> responseInfo) {
		 *           try { JSONObject jsonObject = new
		 *           JSONObject(responseInfo.result);
		 *           jsonObject.get("datas").toString(); Map<String, String>
		 *           imageMap = new HashMap<String, String>();
		 *           imageMap.put("id", jsonObject.get("datas").toString());
		 *           imageuploadList.add(imageMap); } catch (JSONException e) {
		 *           // TODO Auto-generated catch block e.printStackTrace(); }
		 * 
		 *           if(detail.paths.size()==imageuploadList.size()){
		 *           requestAccident(); }
		 * 
		 * 
		 *           }
		 * @Override public void onFailure(HttpException error, String msg) {
		 *           showMessage("图片上传失败!"); } }); }
		 **/
		/*
		 * detail.companyId = ""; detail.companyLogo=""; detail.depotLogo="";
		 * detail.id=""; detail.image="";
		 */

		// _event.onEvent(detail, MLConstants.ACCIDENT_ADD3);
	}

	@OnClick(R.id.accident_rl__platedata)
	public void plateOnClick(View view) {
		/*
		 * _timePop = new MLWheelPop(getActivity(), new IEvent<String>() {
		 * 
		 * @Override public void onEvent(Object source, String eventArg) { //
		 * TODO Auto-generated method stub _platedataTv.setText(eventArg);
		 * _timePop.dismiss(); }
		 * 
		 * }); _timePop.showAtLocation(_root, Gravity.CENTER, 0, 0);
		 */

		String mBirth = _platedataTv.getText().toString();

		Calendar c = Calendar.getInstance();
		if (!MLStrUtil.isEmpty(mBirth)) {
			c.setTime(MLDateUtil.getDateByFormat(mBirth, "yyyy-MM-dd"));
		}

		new DatePickerDialog(_context, DatePickerDialog.THEME_HOLO_LIGHT,
				new DatePickerDialog.OnDateSetListener() {
					public void onDateSet(DatePicker dp, int year, int month,
							int dayOfMonth) {
						_platedataTv.setText(year + "-" + (month + 1) + "-"
								+ dayOfMonth);
					}
				}, c.get(Calendar.YEAR), // 传入年份
				c.get(Calendar.MONTH), // 传入月份
				c.get(Calendar.DAY_OF_MONTH) // 传入天数
		).show();

	}

	@OnClick(R.id.accident_rl_city)
	public void cityOnClick(View view) {
		MLLoginCityPop menuWindow = new MLLoginCityPop(getActivity(),
				new IEvent<String>() {
					@Override
					public void onEvent(Object source, String eventArg) {
						_cityTv.setText(eventArg);
					}
				});
		menuWindow.showAtLocation(_root, Gravity.CENTER, 0, 0);
	}

	/*@OnClick({ R.id.accident_rl_nick, R.id.accident_rl_displacement,
			R.id.accident_rl_mileage, R.id.accident_rl_oldprice,
			R.id.accident_rl_damaged, R.id.accident_rl_price,
			R.id.accident_rl_name, R.id.accident_rl_phone })
	public void inputOnClick(View view) {
		final int id = view.getId();
		int type = 0;
		if (id == R.id.accident_rl_mileage || id == R.id.accident_rl_oldprice
				|| id == R.id.accident_rl_price) {
			type = InputType.TYPE_NUMBER_FLAG_DECIMAL;
		} else if (id == R.id.accident_rl_phone) {
			type = InputType.TYPE_CLASS_PHONE;
		} else if (id == R.id.accident_rl_displacement) {
			type = EditorInfo.TYPE_NUMBER_FLAG_DECIMAL;
			// type =InputType.TYPE_CLASS_TEXT;
		}

		MLMyInputPop menuWindow = new MLMyInputPop(type, getActivity(),
				new IEvent<String>() {
					@Override
					public void onEvent(Object source, String eventArg) {
						switch (id) {
						case R.id.accident_rl_nick:
							_niceTv.setText(eventArg);
							break;
						case R.id.accident_rl_displacement:
							_displacementTv.setText(eventArg + "L");
							break;
						case R.id.accident_rl_mileage:
							_mileageTv.setText(eventArg + "万公里");
							break;
						case R.id.accident_rl_oldprice:
							if(eventArg.length()>5){
								showMessage("请填写正确的价格");
								return;
							}
							_oldpriceTv.setText(eventArg + "万");
							break;
						case R.id.accident_rl_damaged:
							_damagedTv.setText(eventArg);
							break;

						case R.id.accident_rl_price:
							if(eventArg.length()>5){
								showMessage("请填写正确的价格");
								return;
							}
							_priceTv.setText(eventArg + "万元");
							break;
						case R.id.accident_rl_name:
							_nameTv.setText(eventArg);
							break;
						case R.id.accident_rl_phone:
							_phoneTv.setText(eventArg);
							break;
						default:
							break;
						}
					}
				});
		menuWindow.showAtLocation(_root, Gravity.CENTER, 0, 0);
	}
*/
	// -------------------------------------------------------------

	// ======================================================================

	/**
	 * @description 下一步
	 * 
	 * @author marcello
	 */
	private String imageId = "";

	@OnClick(R.id.accident_btn_next_fabu)
	public void nextOnClick(View view) {
		_content = _contentEt.getText().toString();
		if (MLToolUtil.isNull(_content)) {
			showMessage("描述不能为空!");
			return;
		}
		nextOnClick2();
		nextOnClick3();


	}

	private void requestAccident() {
		MLLogin user = ((BaseApplication) getActivity().getApplication())
				.get_user();
		ZMRequestParams params = new ZMRequestParams();
		if (user.isDepot) {
			params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID, user.Id);
		} else {
			params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1, user.Id);
		}
		params.addParameter("city.id", BaseApplication._currentCity);
		params.addParameter(MLConstants.PARAM_MY_CITY, detail.city);
		params.addParameter(MLConstants.PARAM_MY_ACCIDENT, detail.accidentName);
		params.addParameter(MLConstants.PARAM_MY_DAMAGED, detail.damaged);

		params.addParameter(MLConstants.PARAM_MY_DISPLACE, detail.displacement);
		params.addParameter(MLConstants.PARAM_MY_MASTERNAME, detail.masterName);
		params.addParameter(MLConstants.PARAM_MY_MASTERPHONE,
				detail.masterPhone);

		params.addParameter(MLConstants.PARAM_MY_MASTERCONTENT, _content);
		params.addParameter(MLConstants.PARAM_MY_PRICE, detail.price);
		params.addParameter(MLConstants.PARAM_MY_MILEAGE, detail.mileage);
		params.addParameter(MLConstants.PARAM_MY_OLDPRICE, detail.oldPrice);
		params.addParameter(MLConstants.PARAM_MY_PLATEDATA, detail.platedata);

		// params.addParameter("image", imageid);
		// Gson gson = new Gson();
		// if (imageuploadList.size() > 0) {
		// params.addParameter("image", gson.toJsonTree(imageuploadList)
		// .toString());
		// }
		ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
				RequestType.ACCIDENT_ADD, null, params, _handler,
				HTTP_RESPONSE_ACCIDENT_ADD, MLAccidentServices.getInstance());

		Map<String, Object> otherParam = new HashMap<String, Object>();

		// for(int i=0;i<imageuploadList.size();i++){
		// imageid=imageuploadList.get(i).get("id")+",";
		// }
		otherParam.put("image", detail.paths);
		// otherParam.put("image", imageid);
		message2.setOtherParmas(otherParam);
		loadDataWithMessage(_context, "正在发布...", message2);
	}

	private static final int HTTP_RESPONSE_ACCIDENT_ADD = 0;
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
			// 事故车添加
			case HTTP_RESPONSE_ACCIDENT_ADD: {
				MLRegister ret = (MLRegister) msg.obj;
				if (ret.state.equalsIgnoreCase("1")) {
					showMessageSuccess("二手车添加成功!");
					((MLAuxiliaryActivity) _context)
							.setResult(MLConstants.RESULT_ACCEIDENT_ADD);
					((MLAuxiliaryActivity) _context).finish();

				} else {
					showMessageError("二手车添加失败!");
				}
				break;
			}
			default:
				break;
			}
		}
	};

	// =======================================================================

	@Override
	public void onGetRefusePhoto(String number) {
		// TODO Auto-generated method stub
		int msg = Integer.parseInt(number);

		if (msg == 5) {
			mPhotoList.add(String.valueOf(R.drawable.zuoyiqu));
			mImagePathAdapter.notifyDataSetChanged();
			i=i-1;
		}
		if (msg == 4) {
			mPhotoList.add(String.valueOf(R.drawable.zhongkongqu));
			mImagePathAdapter.notifyDataSetChanged();
			i=i-1;
		}
		if (msg == 3) {
			mPhotoList.add(String.valueOf(R.drawable.zhengqianfangtu));
			mImagePathAdapter.notifyDataSetChanged();
			i=i-1;
		}
		if (msg == 2) {
			mPhotoList.add(String.valueOf(R.drawable.qianfang45du));
			mImagePathAdapter.notifyDataSetChanged();
			i=i-1;
		}
		if (msg == 1) {
			mPhotoList.add(String.valueOf(R.drawable.fadongjicang));
			mImagePathAdapter.notifyDataSetChanged();
			i=i-1;
		}
		if (msg == 0) {
			mPhotoList.add(String.valueOf(R.drawable.cemianzhengti));
			mImagePathAdapter.notifyDataSetChanged();
			i=i-1;
		}
	}
}
