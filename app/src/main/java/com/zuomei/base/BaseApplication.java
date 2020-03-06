package com.zuomei.base;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.baichang.android.common.Configuration;
import com.baichang.android.common.ConfigurationImpl;
import com.baidu.mapapi.SDKInitializer;
import com.tencent.bugly.crashreport.CrashReport;
import com.txsh.R;
import com.txsh.friend.HxHelper;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.model.MLAddDeal;
import com.zuomei.model.MLLogin;
import com.zuomei.utils.ACache;
import com.zuomei.utils.MLToolUtil;
import com.zuomei.utils.ZMCrashHandler;
import com.zuomei.widget.picgallery.MyImageView;

import java.io.File;

import cn.jpush.android.api.JPushInterface;
import cn.ml.base.utils.MLFolderUtils;
import cn.trinea.android.common.entity.FailedReason;
import cn.trinea.android.common.service.impl.ImageCache;
import cn.trinea.android.common.service.impl.ImageMemoryCache.OnImageCallbackListener;
import cn.trinea.android.common.service.impl.RemoveTypeUsedCountSmall;
import cn.trinea.android.common.util.ObjectUtils;

public class BaseApplication extends Application implements Configuration {

  private static BaseApplication instance;

  // 屏幕宽度
  public static int screenWidth;
  // 屏幕高度
  public static int screenHeight;

  public static String token;
  public static Long loginUserId;
  public static ACache aCache;

  public boolean m_bKeyRight = true;

  //当前用户
  // public static MLLogin _user = null;
  public MLLogin _user1 = null;
  //当前城市
  public static String _currentCity = "1";
  //互动消息 最新ID
  public static String _messageLastId = "";
  //交易信息
  public static MLAddDeal _addDeal = null;
  //订单号
  public static String OutTradNo = null;
  //微信支付 返回订单号
  public static String out_trade_no = "";

  //图片缓存
  public static final String TAG_CACHE = "image_cache";
  //	 public static final ImageCache IMAGE_CACHE = new ImageCache(128, 512);
  public static final ImageCache IMAGE_CACHE = new ImageCache();
  public static OnImageCallbackListener imageCallBack;
  public static String DEFAULT_CACHE_FOLDER = "";

  public static BaseApplication getInstance() {
    return instance;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    instance = this;
    //ACache 缓存
    //	aCache = ACache.get(instance, "acache");
    try {
      aCache = ACache.get(MLFolderUtils.getDiskFile(this, "acache"));
    } catch (Exception e) {
    }
//		DEFAULT_CACHE_FOLDER = new StringBuilder().append(getExternalCacheDir().getAbsolutePath()).append(File.separator).append("TImageCache").toString();
    DEFAULT_CACHE_FOLDER = new StringBuilder().append(getCacheDir().getAbsolutePath())
        .append(File.separator).append("TImageCache").toString();
    ZMCrashHandler crashHandler = ZMCrashHandler.getInstance();
    crashHandler.init(getApplicationContext());
    IMAGE_CACHE.initData(this, TAG_CACHE);
    IMAGE_CACHE.setContext(this);
    IMAGE_CACHE.setCacheFolder(DEFAULT_CACHE_FOLDER);
    File p = new File(DEFAULT_CACHE_FOLDER);
    if (!p.exists() || p.listFiles().length == 0) {
      IMAGE_CACHE.clear();
    }

    init();
    //百度地图-- 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
    SDKInitializer.initialize(this);
    //激光推送
    JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
    JPushInterface.init(this);            // 初始化 JPush
    //通话
    //UCSService. init(this, true);
    //	UCSService.connect("d8981cf83d2f85ff7e8a8f8fc02e6aca","902797b33a887fde13a2f6f5c1d34080","75994000403630","1b35f863");

    // 文件路径设置
    String parentPath = null;

    // 存在SDCARD的时候，路径设置到SDCARD
    if (Environment.MEDIA_MOUNTED.equals(Environment
        .getExternalStorageState())) {
      parentPath = Environment.getExternalStorageDirectory().getPath()
          + File.separatorChar + getPackageName();
      // 不存在SDCARD的时候
    } else {
      parentPath = Environment.getDataDirectory().getPath()
          + File.separatorChar + "data" + File.separatorChar
          + getPackageName();
    }
    ConfigurationImpl.init(this);
    //环信
    HxHelper.getInstance().init(this);
    CrashReport.initCrashReport(getApplicationContext(), "97bcb127f3", false);
  }

  private void init() {
    WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
    screenWidth = wm.getDefaultDisplay()
        .getWidth();
    screenHeight = wm.getDefaultDisplay()
        .getHeight();

    token = aCache.getAsString("token");
    loginUserId = (Long) aCache.getAsObject("loginUserId");
  }

  static {
    /** init icon cache **/
    imageCallBack = new OnImageCallbackListener() {
      @Override
      public void onGetSuccess(String imageUrl, Bitmap loadedImage, View view, boolean isInCache) {
        if (view != null && loadedImage != null && view instanceof MyImageView) {
          MyImageView imageView = (MyImageView) view;
          String imageUrlTag = (String) imageView.getTag();
          imageView.setImageBitmap(loadedImage);
          //    imageView.setScaleType(ScaleType.CENTER_CROP);
          return;
        }

        if (view != null && loadedImage != null) {
          ImageView imageView = (ImageView) view;
          String imageUrlTag = (String) imageView.getTag();

          if (MLToolUtil.isNull(imageUrlTag)) {
            imageView.setImageBitmap(loadedImage);
          }
          if (ObjectUtils.isEquals(imageUrlTag, imageUrl)) {
            imageView.setImageBitmap(loadedImage);
                   /*     LayoutParams imageParams = (LayoutParams)imageView.getLayoutParams();
                        imageParams.height = imageParams.width * loadedImage.getHeight() / loadedImage.getWidth();*/
            imageView.setScaleType(ScaleType.CENTER_CROP);
          }
          //       imageView.setImageBitmap(loadedImage);
          /*          if (!isInCache) {
                        imageView.startAnimation(getInAlphaAnimation(2000));
                    }*/
                    
               /*     LayoutParams imageParams = (LayoutParams)imageView.getLayoutParams();
                    imageParams.height = imageParams.width * loadedImage.getHeight() / loadedImage.getWidth();*/
          //      imageView.setScaleType(ScaleType.CENTER_CROP);
        }
      }

      @Override
      public void onPreGet(String imageUrl, View view) {
      }

      @Override
      public void onGetFailed(String imageUrl, Bitmap loadedImage, View view,
          FailedReason failedReason) {
        Log.e(TAG_CACHE,
            new StringBuilder(128).append("get image ").append(imageUrl)
                .append(" error, failed type is: ")
                .append(failedReason.getFailedType()).append(", failed reason is: ")
                .append(failedReason.getCause().getMessage()).toString());
            /*    if (view != null) {
                    ImageView imageView = (ImageView)view;   
                    imageView.setImageResource(R.drawable.test2);
                    }*/
      }

      @Override
      public void onGetNotInCache(String imageUrl, View view) {
             /*   if (view != null && view instanceof ImageView) {
                    ((ImageView)view).setImageResource(R.drawable.trinea);
                }*/
      }
    };

//			IMAGE_CACHE.setCompressListener(new CompressListener() {
//			    @Override
//			    public int getCompressSize(String imagePath) {
//			        if (FileUtils.isFileExist(imagePath)) {
//			            long fileSize = FileUtils.getFileSize(imagePath) / 1000;
//			            /**
//			             * if image bigger than 100k, compress to 1/(n + 1) width and 1/(n + 1) height, n is fileSize / 100k
//			             **/
//			            if (fileSize > 100) {
//			                return (int)(fileSize / 100) + 1;
//			            }
//			        }
//			        return 1;
//			    }
//			});
    IMAGE_CACHE.setOnImageCallbackListener(imageCallBack);
    IMAGE_CACHE.setCacheFullRemoveType(new RemoveTypeUsedCountSmall<Bitmap>());
    IMAGE_CACHE.setHttpReadTimeOut(10000);
    IMAGE_CACHE.setOpenWaitingQueue(true);
    IMAGE_CACHE.setValidTime(-1);

    // IMAGE_CACHE.setRequestProperty("Connection", "false");
  }

  public static AlphaAnimation getInAlphaAnimation(long durationMillis) {
    AlphaAnimation inAlphaAnimation = new AlphaAnimation(0, 1);
    inAlphaAnimation.setDuration(durationMillis);
    return inAlphaAnimation;
  }

  public MLLogin get_user() {
    MLLogin user = (MLLogin) aCache.getAsObject(MLConstants.ACACHE_PARAM_USER);
    return user;
  }

  public void set_user(MLLogin _user) {
    this._user1 = _user;
    aCache.put(MLConstants.ACACHE_PARAM_USER, _user);
    MLAppDiskCache.setLoginUser(_user);
  }

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }

  @Override
  public String getApiDefaultHost() {
    return APIConstants.API_NEW_DEFAULT_HOST;
  }

  @Override
  public String getApiWebView() {
    return null;
  }

  @Override
  public Context getAppContext() {
    return this;
  }

  @Override
  public String getApiUploadImage() {
    return null;
  }

  @Override
  public String getApiLoadImage() {
    return null;
  }

  @Override
  public String getToken() {
    return null;
  }

  @Override
  public String getApiDownload() {
    return null;
  }

  @Override
  public String getApiUpload() {
    return "";
  }

  @Override
  public void refreshToken() {

  }

  @Override
  public int getAppBarColor() {
    return R.color.head_back_nomall;
  }
}

