package com.zuomei.home;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.zuomei.model.MLHomeCatalogData;
import com.zuomei.model.MLLeaveDetail;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLLeaveServices;
import com.zuomei.utils.MLToolUtil;
import com.zuomei.widget.wheel.MLWheelPop;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ml.base.utils.IEvent;
import cn.ml.base.utils.MLToastUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 二手件-step1
 * 
 * @author Marcello
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLLeaveadd1Frg extends BaseFragment implements GetRefusePhoto {

	public static MLLeaveadd1Frg INSTANCE = null;

	public static MLLeaveadd1Frg instance() {
		// if(INSTANCE==null){
		INSTANCE = new MLLeaveadd1Frg();
		// }
		return INSTANCE;
	}

	@ViewInject(R.id.accident_gv_photo)
	private GridViewInScrollView _photoGv;

	@ViewInject(R.id.accident_add_roota)
	private LinearLayout _root;

	private int i = 0;
	/*
	 * @ViewInject(R.id.violation_wb) private WebView _webview;
	 */
	private ArrayList<String> mPhotoList = new ArrayList<String>();
	private MLAccidentPhotoAdapter mImagePathAdapter;
	private Context _context;
	private MLMessageAddPop _pop;

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
	// -----------------------------------------------------------------------------

	public static List<String> _paths;

	/** 产品名称 */
	@ViewInject(R.id.leave_tv_nice)
	private EditText _niceTv;

	/** 车辆类型 */
	@ViewInject(R.id.leave_tv_carType)
	private TextView _carTypeTv;

	/** 车辆子类型 */
	@ViewInject(R.id.leave_tv_child)
	private TextView _childTv;

	/** 品质 */
	@ViewInject(R.id.leave_tv_quality)
	private TextView _qualityTv;

	/** 所在城市 */
	@ViewInject(R.id.accident_tv_city)
	private TextView _cityTv;

	/** 排量 */
	@ViewInject(R.id.leave_tv_displacement)
	private TextView _displacementTv;

	/** 原价 */
	@ViewInject(R.id.leave_tv_oldprice)
	private EditText _oldpriceTv;

	/** 现价 */
	@ViewInject(R.id.leave_tv_price)
	private EditText _priceTv;

	/** 名字 */
	@ViewInject(R.id.leave_tv_name)
	private EditText _nameTv;

	/** 电话 */
	@ViewInject(R.id.leave_tv_phone)
	private EditText _phoneTv;
	private MLWheelPop _timePop;
	// -----------------------------------------------------------------------------
	@ViewInject(R.id.paizhao)
	private Button paizhao;

	@ViewInject(R.id.xiangcexuanze)
	private Button xiangcexuanze;
	// ============================================================================

	private static MLLeaveDetail detail;

	@ViewInject(R.id.accident_et_content)
	private EditText _contentEt;

	private String _content;

	// ============================================================================

	List<String> lists;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.leave_add1, null);
		ViewUtils.inject(this, view);

		_context = inflater.getContext();

		initView();
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
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

	@OnClick(R.id.top_back)
	public void backOnClick(View view) {
		getActivity().finish();
		/*
		 * ((MLAuxiliaryActivity)_context).overridePendingTransition(android.R.anim
		 * .slide_in_left, android.R.anim.slide_out_right);
		 */
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
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
			} else {
				showMessage("最多添加6张照片");
			}
		} catch (ActivityNotFoundException e) {
			showMessage("没有找到照片");
		}
	}

	/**
	 * @description 下一步
	 * 
	 * @author marcello
	 */
	public void nextOnClick1() {

		lists = mImagePathAdapter.getImagePaths();
		if (lists.size() <= 1) {
			Toast.makeText(getActivity(), "请添加照片!", 1500).show();
			return;
		}
		lists.remove(lists.size() - 1);
		nextOnClick2();

		// _event.onEvent(lists, MLConstants.MY_LEAVE_ADD2);
	}

	@OnItemClick(R.id.accident_gv_photo)
	public void photoOnItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// if (position == 6) {
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

	@OnClick(R.id.add_btn_cancel)
	public void cancelOnClick(View view) {
		startActivity(new Intent(_context, MLHomeActivity.class));
		// ((MLAuxiliaryActivity) _context).finish();
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
			// if (!AbStrUtil.isEmpty(currentFilePath)) {
			// Intent intent1 = new Intent(getActivity(),
			// MLCropImageActivity.class);
			// intent1.putExtra("PATH", currentFilePath);
			// startActivityForResult(intent1, CAMERA_CROP_DATA);
			// } else {
			// showMessage("未在存储卡中找到这个文件");
			// }
			startPhotoZoom(uri);

			break;
		case CAMERA_WITH_DATA:
			if (D)
				Log.d(TAG, "将要进行裁剪的图片的路径是 = " + mCurrentPhotoFile.getPath());
			String currentFilePath2 = mCurrentPhotoFile.getPath();
			// Intent intent2 = new Intent(getActivity(),
			// MLCropImageActivity.class);
			// intent2.putExtra("PATH", currentFilePath2);
			// startActivityForResult(intent2, CAMERA_CROP_DATA);
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

			 mFileName = System.currentTimeMillis() + ".jpg";
			
			  mCurrentPhotoFile= new File(Environment.getExternalStorageDirectory(),getPhotoFileName());
			 
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
	@SuppressWarnings("deprecation")
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

	@SuppressWarnings("unused")
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	@SuppressWarnings("unused")
	private IEvent<Object> _event;

	@SuppressWarnings("unchecked")
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_event = (IEvent<Object>) activity;
	}

	// --------------------------------------------------------------------------------------------
	/**
	 * @description 下一步
	 * 
	 * @author marcello
	 */
	public void nextOnClick2() {

		String nice = _niceTv.getText().toString();
		/*String type = _carTypeTv.getText().toString();
		String child = _childTv.getText().toString();
		String quality = _qualityTv.getText().toString();*/

		String type = "车型";
		String child = "子车型";
		String quality ="正品";
		String city = _cityTv.getText().toString();
		//String displacement = _displacementTv.getText().toString();
		String displacement ="1";

		String oldPrice = _oldpriceTv.getText().toString();
		String price = _priceTv.getText().toString();

		String masterName = _nameTv.getText().toString();
		String masterPhone = _phoneTv.getText().toString();

		if (MLToolUtil.isNull(nice)) {
			showMessage("产品名称不能为空!");
			return;
		}
		nice = nice.replaceAll("\\s*", "");

	/*	if (MLToolUtil.isNull(type)) {
			showMessage("车辆类型不能为空!");
			return;
		}

		if (MLToolUtil.isNull(child)) {
			showMessage("车辆子类型不能为空!");
			return;
		}
		if (MLToolUtil.isNull(quality)) {
			showMessage("品质不能为空!");
			return;
		}
*/
		if (MLToolUtil.isNull(city)) {
			showMessage("所在城市不能为空!");
			return;
		}
	/*	displacement = displacement.replace("L", "");
		if (MLToolUtil.isNull(displacement)) {
			showMessage("排量不能为空!");
			return;
		}*/

		oldPrice = oldPrice.replace("元", "");
		if (MLToolUtil.isNull(oldPrice)) {
			showMessage("原价不能为空!");
			return;
		}
		price = price.replace("元", "");
		if (MLToolUtil.isNull(price)) {
			showMessage("现价不能为空!");
			return;
		}
		if (MLToolUtil.isNull(masterName)) {
			showMessage("姓名不能为空!");
			return;
		}

		if (MLToolUtil.isNull(masterPhone)) {
			showMessage("电话不能为空!");
			return;
		}

		detail = new MLLeaveDetail();
		detail.nice = nice;
		detail.type = type;
		detail.child = child;
		detail.quality = quality;
		detail.city = city;
		detail.displacement = displacement;
		detail.price = price;
		detail.oldPrice = oldPrice;
		detail.masterName = masterName;
		detail.masterPhone = masterPhone;
		detail.paths = lists;
		requestAccident();
		// _event.onEvent(detail, MLConstants.MY_LEAVE_ADD3);
	}

	@OnClick(R.id.leave_rl_quality)
	public void qualityOnClick(View view) {
		Builder builder = new Builder(_context,
				AlertDialog.THEME_HOLO_LIGHT);
		String s[] = { "正品", "副品", "高仿" };
		builder.setItems(s, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					_qualityTv.setText("正品");
				} else if (which == 1) {
					_qualityTv.setText("副品");
				} else {
					_qualityTv.setText("高仿");
				}

			}
		});
		builder.setTitle("选择品质");
		builder.show();

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

	/*@OnClick({ R.id.leave_rl_nick, R.id.leave_rl_child,
			R.id.leave_rl_displacement, R.id.leave_rl_oldprice,
			R.id.leave_rl_pricea, R.id.leave_rl_name, R.id.leave_rl_phone })
	public void inputOnClick(View view) {
		final int id = view.getId();
		int type = 0;
		if (id == R.id.leave_rl_oldprice || id == R.id.leave_rl_pricea
				|| id == R.id.leave_rl_displacement) {
			type = InputType.TYPE_NUMBER_FLAG_DECIMAL;
		} else if (id == R.id.leave_rl_phone) {
			type = InputType.TYPE_CLASS_PHONE;
		}

		MLMyInputPop menuWindow = new MLMyInputPop(type, getActivity(),
				new IEvent<String>() {
					@Override
					public void onEvent(Object source, String eventArg) {
						switch (id) {

						case R.id.leave_rl_carType:
							_carTypeTv.setText(eventArg);
							break;

						case R.id.leave_rl_child:
							_childTv.setText(eventArg);
							break;

						case R.id.leave_rl_nick:
							_niceTv.setText(eventArg);
							break;
						case R.id.leave_rl_displacement:
							_displacementTv.setText(eventArg + "L");
							break;
						case R.id.leave_rl_oldprice:
							if (eventArg.length() > 6) {
								showMessage("请填写正确的价格");
								return;
							}
							_oldpriceTv.setText(eventArg + "元");
							break;
							case R.id.leave_rl_pricea:
								if (eventArg.length() > 6) {
									showMessage("请填写正确的价格");
									return;
								}
								_priceTv.setText(eventArg + "元");
							break;

						case R.id.accident_rl_price:
							if (eventArg.length() > 6) {
								showMessage("请填写正确的价格");
								return;
							}
							_priceTv.setText(eventArg + "元");
							break;

						case R.id.leave_rl_name:
							_nameTv.setText(eventArg);
							break;
						case R.id.leave_rl_phone:
							_phoneTv.setText(eventArg);
							break;
						default:
							break;
						}
					}
				});
		menuWindow.showAtLocation(_root, Gravity.CENTER, 0, 0);
	}*/

	@OnClick(R.id.leave_rl_carType)
	public void carOnClick(View view) {
		toActivity(_context, MLConstants.MY_PART_CAR1, null);
	}

	private MLHomeCatalogData mCatalogData;

	@Subscribe
	public void setCarInfo(MLHomeCatalogData data) {
		if (data == null)
			return;
		mCatalogData = data;
		_carTypeTv.setText(data.name);
	}

	// --------------------------------------------------------------------------------------------
	// ============================================================================
	/**
	 * @description 下一步
	 * 
	 * @author marcello
	 */
	private String imageId = "";

	@OnClick(R.id.accident_btn_next)
	public void nextOnClick(View view) {
		_content = _contentEt.getText().toString();
		if (MLToolUtil.isNull(_content)) {
			MLToastUtils.showMessage(_context,"描述不能为空!");
			return;
		}
		nextOnClick1();

	}

	private void requestAccident() {
		MLLogin user = ((BaseApplication) getActivity().getApplication())
				.get_user();
		ZMRequestParams params = new ZMRequestParams();

		if(user.isDepot){
			params.addParameter("depotUser.id", user.Id);
		}else{
			params.addParameter("company.id", user.Id);
		}

		params.addParameter("carType", detail.type);
		params.addParameter("childType", detail.child);
		params.addParameter("exhaust", detail.displacement);
		params.addParameter("city.id", BaseApplication._currentCity);
		params.addParameter("cityName", detail.city);
		params.addParameter("name", detail.nice);
		params.addParameter("introduction", _content);
		params.addParameter("quality", detail.quality);
		params.addParameter("mobile", detail.masterPhone);
		params.addParameter("user", detail.masterName);
		params.addParameter("originalCost", detail.oldPrice);
		params.addParameter("currentCost", detail.price);

		ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
				RequestType.LEAVE_ADD, null, params, _handler,
				HTTP_RESPONSE_ACCIDENT_ADD, MLLeaveServices.getInstance());

		Map<String, Object> otherParam = new HashMap<String, Object>();
		otherParam.put("image", detail.paths);
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
					showMessageSuccess("拆车件发布成功!");
					((MLAuxiliaryActivity) _context)
							.setResult(MLConstants.RESULT_ACCEIDENT_ADD);
					((MLAuxiliaryActivity) _context).finish();
				} else {
					showMessageError("拆车件发布失败!");
				}
				break;
			}
			default:
				break;
			}
		}
	};

	@Override
	public void onGetRefusePhoto(String number) {
		// TODO Auto-generated method stub
		int msg = Integer.parseInt(number);

		if (msg == 5) {
			mPhotoList.add(String.valueOf(R.drawable.zuoyiqu));
			mImagePathAdapter.notifyDataSetChanged();
			i = i - 1;
		}
		if (msg == 4) {
			mPhotoList.add(String.valueOf(R.drawable.zhongkongqu));
			mImagePathAdapter.notifyDataSetChanged();
			i = i - 1;
		}
		if (msg == 3) {
			mPhotoList.add(String.valueOf(R.drawable.zhengqianfangtu));
			mImagePathAdapter.notifyDataSetChanged();
			i = i - 1;
		}
		if (msg == 2) {
			mPhotoList.add(String.valueOf(R.drawable.qianfang45du));
			mImagePathAdapter.notifyDataSetChanged();
			i = i - 1;
		}
		if (msg == 1) {
			mPhotoList.add(String.valueOf(R.drawable.fadongjicang));
			mImagePathAdapter.notifyDataSetChanged();
			i = i - 1;
		}
		if (msg == 0) {
			mPhotoList.add(String.valueOf(R.drawable.cemianzhengti));
			mImagePathAdapter.notifyDataSetChanged();
			i = i - 1;
		}
	}

	// ============================================================================
}
