package com.zuomei.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.widget.Toast;

import com.ab.view.sample.AbUnSlideViewPager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.qipei.found.MLFoundFrg;
import com.qipei.home.MLHomeFrg;
import com.qipei.interact.MLInteractFrag;
import com.qipei.me.MLMeRepairFrg;
import com.qipei.part.MLPartMainFrg;
import com.txsh.R;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLAddDeal;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLMapResponse;
import com.zuomei.model.MLUpdateData;
import com.zuomei.model.MLUpdateResponse;
import com.zuomei.services.MLHomeServices;
import com.zuomei.services.MLLoginServices;
import com.zuomei.utils.AppManager;
import com.zuomei.widget.MLBottomItemView;
import com.zuomei.widget.MLBottomTab;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.ml.base.utils.IEvent;

public class MLHomeActivity extends BaseActivity implements IEvent<Object> {

	@ViewInject(R.id.home_bottom_tab)
	private MLBottomTab _bottomTab;

	@ViewInject(R.id.home_viewpage)
	private AbUnSlideViewPager viewpager;

	/** 内容的View. */
	private ArrayList<Fragment> pagerItemList = null;
	// private AbFragmentPagerAdapter mFragmentPagerAdapter = null;
	private FragmentPagerAdapter mFragmentPagerAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_main);

		if (savedInstanceState != null) {
			MLLogin user = (MLLogin) savedInstanceState.getSerializable("user");
			((BaseApplication) getApplication()).set_user(user);
			BaseApplication._currentCity = savedInstanceState
					.getString("currentCity");
			BaseApplication._messageLastId = savedInstanceState
					.getString("messageId");
			BaseApplication._addDeal = (MLAddDeal) savedInstanceState
					.getSerializable("deal");
		}

		ViewUtils.inject(this);

		// 检查更新
		checkUpdate();
		// 获取坐标
		initOverlayData();

		// 开启定时
	//	_countHandler.postDelayed(runnable, 60000);

		final MLHomeFrg page1 = new MLHomeFrg();
		// Fragment page2= MLViolationFrg.instance();
		Fragment page2 = new MLInteractFrag();
		Fragment page3 = new MLPartMainFrg();
		Fragment page4 = new MLFoundFrg();
		Fragment page5 = null;

		MLLogin user = ((BaseApplication) getApplication()).get_user();
		// MLLogin user = BaseApplication._user;
		if (user.isDepot) {
			page5 = new MLMeRepairFrg();
		} else {
			page5 = MLMyMainBFrg.instance();
		}

		pagerItemList = new ArrayList<Fragment>();
		pagerItemList.add(page1);
		pagerItemList.add(page2);
		pagerItemList.add(page3);
		pagerItemList.add(page4);
		pagerItemList.add(page5);

		List<String> tabTexts = new ArrayList<String>();
		tabTexts.add("首页");
		tabTexts.add("互动");
		tabTexts.add("配件");
		tabTexts.add("发现");
		tabTexts.add("我的");

		// 注意图片的顺序
		List<Drawable> tabDrawables = new ArrayList<Drawable>();
		tabDrawables.add(this.getResources().getDrawable(
				R.drawable.bottom_home_n));
		tabDrawables.add(this.getResources().getDrawable(
				R.drawable.bottom_home_f));
		tabDrawables.add(this.getResources().getDrawable(
				R.drawable.bottom_message_n));
		tabDrawables.add(this.getResources().getDrawable(
				R.drawable.bottom_message_f));
		tabDrawables.add(this.getResources().getDrawable(
				R.drawable.bottom_part_n));
		tabDrawables.add(this.getResources().getDrawable(
				R.drawable.bottom_part_f));
		tabDrawables.add(this.getResources().getDrawable(
				R.drawable.bottom_found_n));
		tabDrawables.add(this.getResources().getDrawable(
				R.drawable.bottom_found_f));
		tabDrawables.add(this.getResources()
				.getDrawable(R.drawable.bottom_me_n));
		tabDrawables.add(this.getResources()
				.getDrawable(R.drawable.bottom_me_f));

		FragmentManager mFragmentManager = getSupportFragmentManager();
		/*
		 * mFragmentPagerAdapter = new AbFragmentPagerAdapter(mFragmentManager,
		 * pagerItemList);
		 */

		mFragmentPagerAdapter = new FragmentPagerAdapter(mFragmentManager) {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return pagerItemList.size();
			}

			@Override
			public Fragment getItem(int position) {
				// TODO Auto-generated method stub
				return pagerItemList.get(position);
			}
		};

		viewpager.setAdapter(mFragmentPagerAdapter);
		viewpager.setOffscreenPageLimit(5);
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				if (position == 0) {
					// 解决首页切换 Scrollview位置变动问题
				//	page1.reviewScrollview();
				}
				//点击互动 隐藏数字
				if (position == 2) {
					MLBottomItemView view = (MLBottomItemView) _bottomTab
							.getChildAt(2);
					view.setCount("0");
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub

			}
		});
		_bottomTab.addItemViews(tabTexts, tabDrawables, viewpager);

	//	initCount();
	}

	/*
	 * @OnClick(R.id.btn1) public void click1(View view){
	 * viewpager.setCurrentItem(0); }
	 * 
	 * @OnClick(R.id.btn2) public void click2(View view){
	 * viewpager.setCurrentItem(1); }
	 * 
	 * @OnClick(R.id.btn3) public void click3(View view){
	 * viewpager.setCurrentItem(4); }
	 */

	private void initCount() {
	/*	String count = BaseApplication._messageLastId;
		if (MLToolUtil.isNull(count)) {
			return;
		}

		ZMRequestParams params = new ZMRequestParams();
		params.addParameter(MLConstants.PARAM_MESSAGE_LASTID, count);

		ZMHttpRequestMessage message = new ZMHttpRequestMessage(
				RequestType.HOME_MESSAGE_COUNT, null, params, _handler,
				HTTP_RESPONSE_COUNT, MLHomeServices.getInstance());
		loadDataWithMessage(null, message);*/

	}

	@Override
	public void onEvent(Object source, Object eventArg) {
		// fillContent(source,(Integer)eventArg);
	}

	private void initOverlayData() {
		ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(
				RequestType.HOME_MAP, null, null, _handler, HTTP_RESPONSE_MAP,
				MLHomeServices.getInstance());
		loadData(this, message1);

	}

	// 检查更新
	private void checkUpdate() {
		
		ZMRequestParams params = new ZMRequestParams();
		MLLogin user =BaseApplication.getInstance().get_user();
		if(user.isDepot){
			params.addParameter("depotId",user.Id);
		}else{
			params.addParameter("companyId",user.Id);
		}
		params.addParameter("equipmentType", "0");
		params.addParameter("version", getVersionName());

		ZMHttpRequestMessage message = new ZMHttpRequestMessage(
				RequestType.LOGIN_UPDATE, null, params, _handler,
				HTTP_RESPONSE_UPDATE, MLLoginServices.getInstance());
		loadData(MLHomeActivity.this, message);
	}

	private void update(MLUpdateData data) {
//		String downUrl = APIConstants.API_IMAGE + "?id=" + data.downloadPath;
		String downUrl = data.downloadPath;
		String path = getDiskCacheDir("update") + "/update.apk";
		HttpUtils http = new HttpUtils();
		HttpHandler handler = http.download(downUrl, path, false, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
				true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
				new RequestCallBack<File>() {
					@Override
					public void onStart() {
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						// testTextView.setText(current + "/" + total);
						showProgressDialog("正在下载中，请稍等.... ",
								MLHomeActivity.this);
						// showMessage(current + "/" + total);
					}

					@Override
					public void onSuccess(ResponseInfo<File> responseInfo) {
						dismissProgressDialog();
						// showMessage("下载成功-------"+responseInfo.result.getPath());
						openFile(responseInfo.result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						dismissProgressDialog();
						showMessage("下载失败");
					}
				});

	}

	private String getVersionName() {
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String version = packInfo.versionName;
		return version;
	}

	private void openFile(File file) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	File _updatePath = null;

/*	public String getAttachFolder() {
	//	File file = BaseApplication.getInstance().getExternalCacheDir();
    	File file =getCacheDir();
		String subPath = String.format("%s/update", file.getAbsolutePath());
		File subFile = new File(subPath);
		return subFile.getAbsolutePath();
	}*/

	public  String getDiskCacheDir(String folderName) {
	    String cachePath = null;  
	    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())  
	            || !Environment.isExternalStorageRemovable()) {  
	        cachePath = getExternalCacheDir().getPath();  
	    } else {  
	        cachePath =getCacheDir().getPath();  
	    }  
	    
	    return String.format("%s/%s",cachePath,folderName);
	}  
	
	/*
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
	 * 
	 * //捕获返回键 if (keyCode == KeyEvent.KEYCODE_BACK) { //回到桌面 Intent i = new
	 * Intent(Intent.ACTION_MAIN); i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	 * i.addCategory(Intent.CATEGORY_HOME); startActivity(i); }
	 * 
	 * return super.onKeyDown(keyCode, event); }
	 */

	private long firstTime = 0;

	/*@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
	
	}*/
	
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long secondTime = System.currentTimeMillis();
			if (secondTime - firstTime > 800) {// 如果两次按键时间间隔大于800毫秒，则不退出
				Toast.makeText(MLHomeActivity.this, "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				firstTime = secondTime;// 更新firstTime
				return true;
			} else {
				BaseApplication.IMAGE_CACHE.saveDataToDb(getApplicationContext(), "image_cache");
				
				AppManager.getAppManager().AppExit(MLHomeActivity.this);
				
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		BaseApplication.IMAGE_CACHE.saveDataToDb(this,
				BaseApplication.TAG_CACHE);
		super.onDestroy();
		_countHandler.removeCallbacks(runnable);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		_countHandler.postDelayed(runnable, 4000);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		_countHandler.removeCallbacks(runnable);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		MLLogin user = ((BaseApplication) getApplication()).get_user();
		String currentCity = BaseApplication._currentCity;
		String messageId = BaseApplication._messageLastId;
		MLAddDeal deal = BaseApplication._addDeal;
		outState.putSerializable("user", user);
		outState.putString("currentCity", currentCity);
		outState.putString("messageId", messageId);
		outState.putSerializable("deal", deal);

	}

	// =======================定时刷新===================================
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 要做的事情，这里再次调用此Runnable对象，以实现每两秒实现一次的定时器操作
		/*	initCount();
			_countHandler.postDelayed(this, 60000);*/
		}
	};

	private Handler _countHandler = new Handler();

	private static final int HTTP_RESPONSE_COUNT = 1;
	private static final int HTTP_RESPONSE_UPDATE = 2;
	private static final int HTTP_RESPONSE_MAP = 3;
	@SuppressLint("NewApi")
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
			case HTTP_RESPONSE_COUNT: {
			/*	MLSpecialResonse ret = (MLSpecialResonse) msg.obj;
				if (ret.state.equalsIgnoreCase("1")) {
					MLBottomItemView view = (MLBottomItemView) _bottomTab
							.getChildAt(2);
					view.setCount(ret.datas);
				}*/
				break;
			}
			case HTTP_RESPONSE_MAP: {
				MLMapResponse ret = (MLMapResponse) msg.obj;
				BaseApplication.aCache.put(MLConstants.PARAM_MAP_OVERLAY, ret);
				break;
			}
			case HTTP_RESPONSE_UPDATE: {

				final MLUpdateResponse ret = (MLUpdateResponse) msg.obj;
				if (ret.state.equalsIgnoreCase("1") && ret.datas != null) {
					Builder builder = new Builder(
							MLHomeActivity.this,AlertDialog.THEME_HOLO_LIGHT);
					builder.setMessage("发现新版本" + ret.datas.version + "是否更新?");
					builder.setTitle("提示");
					builder.setPositiveButton("取消", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if(ret.datas.isUpdate.equalsIgnoreCase("1")){
								//强制更新
								AppManager.getAppManager().AppExit(MLHomeActivity.this);
							}else{
								dialog.dismiss();
							}
							
						}
					});
					builder.setNegativeButton("确认", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 更新
							update(ret.datas);  
						}
					});
					
					 builder.setOnKeyListener(new OnKeyListener() {   
						  
				            @Override  
				            public boolean onKey(DialogInterface dialog, int keyCode,   
				                    KeyEvent event) {   
				                if (keyCode == KeyEvent.KEYCODE_BACK   
				                        && event.getRepeatCount() == 0) {   
				                		return true;
				                }   
				                return false;
				            }   
				        });   

					AlertDialog  ad =builder.create();
					ad.setCanceledOnTouchOutside(false);
					ad.show();
				}
				break;
			}
			default:
				break;
			}
		}
	};

}
