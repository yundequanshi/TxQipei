package com.zuomei.login;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;

import cn.jpush.android.api.TagAliasCallback;
import com.baichang.android.utils.BCStringUtil;
import com.baichang.android.utils.BCToolsUtil;
import com.txsh.R;
import com.txsh.comment.TXHomeActivity;
import com.txsh.home.CityActivity;
import com.txsh.model.HxUserLoginData;
import com.txsh.utils.HxUtils;
import com.txsh.utils.MLUserCityUtils;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.MLAppDiskCache;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLLoginResponse;
import com.zuomei.model.UserCityData.UserCity;
import com.zuomei.services.MLLoginServices;
import com.zuomei.utils.MLToolUtil;

import cn.jpush.android.api.JPushInterface;
import cn.ml.base.utils.IEvent;
import java.util.HashSet;
import java.util.Set;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLLoginActivity extends BaseActivity implements IEvent<Object> {

  private WebView _web;
  private MLLogin user = new MLLogin();
  private String isDepot = "1";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login_main);

    Intent intent = getIntent();
    String data = intent.getStringExtra("data");
    if (MLToolUtil.isNull(data)) {
      fillContent(null, MLConstants.LOGIN_SPLASH);
      _handler1.sendEmptyMessageDelayed(0, 2000);
    } else {
      fillContent(null, MLConstants.LOGIN_MAIN);
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    JPushInterface.onPause(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    JPushInterface.onResume(this);
  }

  private void fillContent(Object obj, int position) {
    Fragment fragment = null;
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    switch (position) {
      //闪屏界面
      case MLConstants.LOGIN_SPLASH:
        fragment = MLSplashFragment.instance();
        transaction.addToBackStack(null);
        break;
      //登录界面
      case MLConstants.LOGIN_MAIN:
        fragment = MLLoginFrg.instance();
        //		 transaction.addToBackStack(null);
        break;
      //注册界面
      case MLConstants.LOGIN_REGISTER:
        fragment = MLRegisterFrg.instance();
        transaction.addToBackStack(null);
        break;
      default:
        break;
    }

    if (fragment == null) {
      return;
    }
    transaction.replace(R.id.login_fl_content, fragment);
    transaction.show(fragment);
    transaction.commitAllowingStateLoss();
  }

  @Override
  public void onEvent(Object source, Object eventArg) {
    fillContent(source, (Integer) eventArg);
  }

  private Handler _handler1 = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);

      user = (MLLogin) BaseApplication.aCache.getAsObject(MLConstants.PARAM_LOGIN_USER);
      if (user != null) {
        ((BaseApplication) getApplication()).set_user(user);

        String username = BaseApplication.aCache.getAsString(MLConstants.PARAM_REGISTER_USERNAME);
        String pwd = BaseApplication.aCache.getAsString(MLConstants.PARAM_REGISTER_PWD);
        isDepot = BaseApplication.aCache.getAsString(MLConstants.PARAM_LOGIN_DEPORTID);

        ZMRequestParams params = new ZMRequestParams();
        params.addParameter(MLConstants.PARAM_REGISTER_USERNAME, username);
        params.addParameter(MLConstants.PARAM_REGISTER_PWD, pwd);
        params.addParameter(MLConstants.PARAM_LOGIN_DEPORT, isDepot);

        ZMHttpRequestMessage message = new ZMHttpRequestMessage(ZMHttpType.RequestType.LOGIN, null,
            params, _handler, HTTP_RESPONSE_LOGIN, MLLoginServices.getInstance());
        loadData(MLLoginActivity.this, message);
      } else {
        fillContent(null, MLConstants.LOGIN_MAIN);
      }
    }
  };


  private static final int HTTP_RESPONSE_LOGIN = 0;
  private Handler _handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);

      if (msg == null || msg.obj == null) {
        dismissProgressDialog();
        showMessage(R.string.loading_data_failed);
        return;
      }
      if (msg.obj instanceof ZMHttpError) {
        dismissProgressDialog();
        ZMHttpError error = (ZMHttpError) msg.obj;
        showMessage(error.errorMessage);
        return;
      }
      switch (msg.what) {
        case HTTP_RESPONSE_LOGIN:
          MLLoginResponse ret = (MLLoginResponse) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {

            if (isDepot.equalsIgnoreCase("1")) {
              ret.datas.isDepot = true;
            } else {
              ret.datas.isDepot = false;
            }

            if (MLUserCityUtils.getCityData(ret.datas.Id, ret.datas.name) != null) {
              UserCity userCity = MLUserCityUtils.getCityData(ret.datas.Id, ret.datas.name);
              BaseApplication._currentCity = userCity.cityId;
              BaseApplication.aCache.put("cityid", userCity.cityId);
              BaseApplication.aCache.put("cityname", userCity.cityName);
              Intent intent = new Intent(getAty(), TXHomeActivity.class);
              intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
              startActivity(intent);
            } else {
              startAct(getAty(), CityActivity.class, "1");
            }
            //环信登录
            HxUserLoginData userLoginData = new HxUserLoginData();
            userLoginData.hxUser = ret.datas.hxUser;
            userLoginData.userId = ret.datas.Id;
            userLoginData.hxPwd = ret.datas.hxPwd;
            if (user.isDepot) {
              userLoginData.userType = "0";
            } else {
              userLoginData.userType = "1";
            }
            HxUtils.getInstance(getAty()).login(userLoginData);
            MLAppDiskCache.setUser(userLoginData);
            finish();
          } else {
            dismissProgressDialog();
            fillContent(null, MLConstants.LOGIN_MAIN);
          }

          break;
        default:
          break;
      }
    }
  };


  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }
}
