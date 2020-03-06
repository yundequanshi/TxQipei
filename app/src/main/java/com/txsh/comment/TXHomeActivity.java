package com.txsh.comment;

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
import android.os.AsyncTask;
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
import com.baichang.android.utils.BCStringUtil;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.model.HxUser;
import com.easemob.easeui.ui.EaseConversationListFragment;
import com.easemob.easeui.utils.Contants;
import com.hyphenate.chat.EMClient;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.qipei.interact.MLInteractFrag;
import com.txsh.R;
import com.txsh.friend.ChatAty;
import com.txsh.friend.ContactListAty;
import com.txsh.home.TXHomeFrg;
import com.txsh.home.TXHomeFrgShangHu;
import com.txsh.shop.TXShopMainFrag;
import com.txsh.shop.TXShopMainFrag2;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.MLConstants;
import com.zuomei.home.MLMyMainBFrg;
import com.zuomei.home.MLMyMainDFrg;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLAddDeal;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLMapResponse;
import com.zuomei.model.MLSpecialResonse;
import com.zuomei.model.MLUpdateData;
import com.zuomei.model.MLUpdateResponse;
import com.zuomei.services.MLHomeServices;
import com.zuomei.services.MLLoginServices;
import com.zuomei.utils.AppManager;
import com.zuomei.utils.MLToolUtil;
import com.zuomei.widget.MLBottomItemView;
import com.zuomei.widget.MLBottomTab;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.ml.base.utils.IEvent;

public class TXHomeActivity extends BaseActivity implements IEvent<Object> {

  @ViewInject(R.id.home_bottom_tab)
  private MLBottomTab _bottomTab;

  @ViewInject(R.id.home_viewpage)
  private AbUnSlideViewPager viewpager;

  /**
   * 内容的View.
   */
  private ArrayList<Fragment> pagerItemList = null;
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
    //initOverlayData();

    // 开启定时
    _countHandler.postDelayed(runnable, 60000);
    MLLogin user = BaseApplication.getInstance().get_user();
    Fragment page1 = null;
    if (user.isDepot) {
      page1 = new TXHomeFrg();//修理厂
    } else {
      page1 = new TXHomeFrgShangHu();//商家
    }
    final Fragment page2 = new TXShopMainFrag2();
    Fragment page3 = new EaseConversationListFragment();
    Fragment page4 = new MLInteractFrag();
    Fragment page5 = null;

    if (user.isDepot) {
      page5 = new MLMyMainDFrg();//修理厂
    } else {
      page5 = MLMyMainBFrg.instance();//商家
    }

    pagerItemList = new ArrayList<Fragment>();
    pagerItemList.add(page1);
    pagerItemList.add(page2);
    pagerItemList.add(page3);
    pagerItemList.add(page4);
    pagerItemList.add(page5);

    List<String> tabTexts = new ArrayList<String>();
    tabTexts.add("首页");
    tabTexts.add("商城");
    tabTexts.add("朋友");
    tabTexts.add("交流");
    tabTexts.add("我的天夏");

    // 注意图片的顺序
    List<Drawable> tabDrawables = new ArrayList<Drawable>();
    tabDrawables.add(this.getResources().getDrawable(
        R.drawable.tab_shouye));
    tabDrawables.add(this.getResources().getDrawable(
        R.drawable.tab_shouye_pre));
    tabDrawables.add(this.getResources().getDrawable(
        R.drawable.tab_shangcheng));
    tabDrawables.add(this.getResources().getDrawable(
        R.drawable.tab_shangcheng_pre));
    tabDrawables.add(this.getResources().getDrawable(
        R.mipmap.tab_icon_friends_default));
    tabDrawables.add(this.getResources().getDrawable(
        R.mipmap.tab_icon_friends_click));
    tabDrawables.add(this.getResources().getDrawable(
        R.drawable.tab_faxian));
    tabDrawables.add(this.getResources().getDrawable(
        R.drawable.tab_faxian_pre));
    tabDrawables.add(this.getResources()
        .getDrawable(R.drawable.tab_wode));
    tabDrawables.add(this.getResources()
        .getDrawable(R.drawable.tab_wode_pre));

    FragmentManager mFragmentManager = getSupportFragmentManager();
    mFragmentPagerAdapter = new FragmentPagerAdapter(mFragmentManager) {

      @Override
      public int getCount() {
        return pagerItemList.size();
      }

      @Override
      public Fragment getItem(int position) {
        return pagerItemList.get(position);
      }
    };

    viewpager.setAdapter(mFragmentPagerAdapter);
    viewpager.setOffscreenPageLimit(5);
    viewpager.setOnPageChangeListener(new OnPageChangeListener() {

      @Override
      public void onPageSelected(int position) {
        if (position == 0) {
        }
        //点击互动 隐藏数字
        if (position == 3) {
          MLBottomItemView view = (MLBottomItemView) _bottomTab
              .getChildAt(3);
          view.setCount("0");
        } else if (position == 2) {
          MLBottomItemView view = (MLBottomItemView) _bottomTab
              .getChildAt(2);
          view.setCount("0");
          EMClient.getInstance().chatManager().markAllConversationsAsRead();
        } else if (position == 1) {
          if (page2 != null) {
            ((TXShopMainFrag2) page2).initPlayData();
            ((TXShopMainFrag2) page2).initChangeData();
          }
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
  }

  private void initCount() {
    String count = BaseApplication._messageLastId;
    if (MLToolUtil.isNull(count)) {
      return;
    }

    ZMRequestParams params = new ZMRequestParams();
    params.addParameter(MLConstants.PARAM_MESSAGE_LASTID, count);
    if (BaseApplication._currentCity != null) {
      params.addParameter("cityId", BaseApplication._currentCity);
    }

    ZMHttpRequestMessage message = new ZMHttpRequestMessage(
        RequestType.HOME_MESSAGE_COUNT, null, params, _handler,
        HTTP_RESPONSE_COUNT, MLHomeServices.getInstance());
    loadDataWithMessage(null, message);

  }

  @Override
  public void onEvent(Object source, Object eventArg) {
    int i = (int) source;
    switch (i) {
      case 2: {
        //通讯录
        if (BCStringUtil.compare((String) eventArg, "left")) {
          startAct(this, ContactListAty.class);
        }
        break;
      }
      case 3: {
        //单聊
        HxUser user = (HxUser) eventArg;
        startActivity(new Intent(this, ChatAty.class).putExtra(Contants.EXTRA_USER, user));
        break;
      }
      case 4: {
        //群聊
        String user = (String) eventArg;
        startActivity(new Intent(this, ChatAty.class).putExtra(Contants.EXTRA_USER_ID, user)
            .putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP));
        break;
      }
      case 5: {
        //环信未读数量
        HxGetUnreadMessage();
      }
    }
  }

  private void HxGetUnreadMessage() {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        //获取消息数目
        int count = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        MLBottomItemView view = (MLBottomItemView) _bottomTab
            .getChildAt(2);
        view.setCount(String.valueOf(count));
      }
    });
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
    MLLogin user = BaseApplication.getInstance().get_user();
    if (user.isDepot) {
      params.addParameter("depotId", user.Id);
    } else {
      params.addParameter("companyId", user.Id);
    }
    params.addParameter("equipmentType", "0");
    params.addParameter("version", getVersionName());

    ZMHttpRequestMessage message = new ZMHttpRequestMessage(
        RequestType.LOGIN_UPDATE, null, params, _handler,
        HTTP_RESPONSE_UPDATE, MLLoginServices.getInstance());
    loadData(TXHomeActivity.this, message);
  }

  private void update(MLUpdateData data) {
    String downUrl = data.downloadPath;
    String path = getDiskCacheDir("update") + "/update.apk";
    HttpUtils http = new HttpUtils();
    HttpHandler handler = http
        .download(downUrl, path, false, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
            true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
            new RequestCallBack<File>() {
              @Override
              public void onStart() {
              }

              @Override
              public void onLoading(long total, long current,
                  boolean isUploading) {
                showProgressDialog("正在下载中，请稍等.... ",
                    TXHomeActivity.this);
              }

              @Override
              public void onSuccess(ResponseInfo<File> responseInfo) {
                dismissProgressDialog();
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
    PackageInfo packInfo = null;
    try {
      packInfo = packageManager.getPackageInfo(getPackageName(), 0);
    } catch (NameNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    String version = packInfo.versionCode + "";
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

  public String getDiskCacheDir(String folderName) {
    String cachePath = null;
    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
        || !Environment.isExternalStorageRemovable()) {
      cachePath = getExternalCacheDir().getPath();
    } else {
      cachePath = getCacheDir().getPath();
    }

    return String.format("%s/%s", cachePath, folderName);
  }

  private long firstTime = 0;

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    // TODO Auto-generated method stub

    if (keyCode == KeyEvent.KEYCODE_BACK) {
      long secondTime = System.currentTimeMillis();
      if (secondTime - firstTime > 800) {// 如果两次按键时间间隔大于800毫秒，则不退出
        Toast.makeText(TXHomeActivity.this, "再按一次退出程序",
            Toast.LENGTH_SHORT).show();
        firstTime = secondTime;// 更新firstTime
        return true;
      } else {
        BaseApplication.IMAGE_CACHE.saveDataToDb(getApplicationContext(), "image_cache");

        AppManager.getAppManager().AppExit(TXHomeActivity.this);

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

    //环信未读数量
    HxGetUnreadMessage();

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
      initCount();
      _countHandler.postDelayed(this, 60000);
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
          MLSpecialResonse ret = (MLSpecialResonse) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            MLBottomItemView view = (MLBottomItemView) _bottomTab
                .getChildAt(3);
            view.setCount(ret.datas);
          }
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
                TXHomeActivity.this, AlertDialog.THEME_HOLO_LIGHT);
//            MLLogin user = (MLLogin) BaseApplication.aCache
//                .getAsObject(MLConstants.PARAM_LOGIN_USER);
//            user.nowVersion = ret.datas.version;
//            BaseApplication.aCache.put(MLConstants.PARAM_LOGIN_USER, user);
            builder.setMessage("发现新版本" + ret.datas.version + "是否更新?");
            builder.setTitle("提示");
            builder.setPositiveButton("取消", new OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                if (ret.datas.isUpdate.equalsIgnoreCase("1")) {
                  //强制更新
                  AppManager.getAppManager().AppExit(TXHomeActivity.this);
                } else {
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

            AlertDialog ad = builder.create();
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
