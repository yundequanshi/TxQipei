package com.txsh.utils;

import android.content.Context;
import android.util.Log;

import com.baichang.android.request.HttpErrorListener;
import com.baichang.android.request.HttpLoggerInterceptor;
import com.baichang.android.request.HttpSubscriber;
import com.baichang.android.request.HttpSuccessListener;
import com.baichang.android.request.ResponseConverterFactory;
import com.easemob.easeui.model.HxConfig;
import com.easemob.easeui.model.HxUser;
import com.easemob.easeui.utils.MLDBUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.lidroid.xutils.db.sqlite.Selector;
import com.txsh.model.HxUserLoginData;
import com.easemob.easeui.utils.HxApi;
import com.easemob.easeui.utils.HxHttpApi;
import com.zuomei.base.MLAppDiskCache;

import com.zuomei.constants.APIConstants;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xuhanyu on 2016/12/14 Description
 */
public class HxUtils {

  public static HxUtils INSTANCE;
  private static Context mContext;
  public boolean isVideoCalling;
  public boolean isVoiceCalling;

  public static HxUtils getInstance(Context context) {
    if (INSTANCE == null) {
      INSTANCE = new HxUtils();
    }
    mContext = context;
    return INSTANCE;
  }

  public void login(final HxUserLoginData mdata) {
    EMClient.getInstance().login(mdata.hxUser, mdata.hxPwd, new EMCallBack() {
      @Override
      public void onSuccess() {
        getUser(mdata);
        Log.d("request", "环信登陆成功！！！");
      }

      @Override
      public void onProgress(int progress, String status) {
      }

      @Override
      public void onError(int code, String error) {
        Log.d("request", "环信登陆失败！！错误信息:" + code + "-----" + error);
        if (EMError.USER_ALREADY_LOGIN == code) {
          loginOut(mdata);
        } else {
          login(mdata);
        }
      }
    });
  }

  public void loginOut(final HxUserLoginData mdata) {
    EMClient.getInstance().logout(true, new EMCallBack() {

      @Override
      public void onSuccess() {
        login(mdata);
      }

      @Override
      public void onProgress(int progress, String status) {
        // TODO Auto-generated method stub

      }

      @Override
      public void onError(int code, String message) {
        // TODO Auto-generated method stub

      }
    });
  }


  public void getUserInfo(String hxid) {
    Map<String, String> map = new HashMap<String, String>();
    map.put("emId", hxid);
    HxApi requset = new HxHttpApi();
    requset.getUserByEmId(map)
        .compose(HttpSubscriber.<HxUser>applySchedulers())
        .subscribe(new HttpSubscriber<HxUser>(new HttpSuccessListener<HxUser>() {
          @Override
          public void success(HxUser hxUser) {
            MLDBUtils.saveOrUpdate(hxUser);
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable throwable) {

          }
        }));
  }

  public void getUser(HxUserLoginData mdata) {
    Map<String, String> map = new HashMap<String, String>();
    map.put("userId", mdata.userId);
    map.put("userType", mdata.userType);
    HxApi requset = new HxHttpApi();
    requset.getHxUser(map)
        .compose(HttpSubscriber.<HxUser>applySchedulers())
        .subscribe(new HttpSubscriber<>(new HttpSuccessListener<HxUser>() {
          @Override
          public void success(HxUser hxUser) {
            HxUserLoginData data = MLAppDiskCache.getUser();
            if (data == null) {
              data = new HxUserLoginData();
            }
            data.hxKid = hxUser.kid;
            MLAppDiskCache.setUser(data);
            MLDBUtils.saveOrUpdate(hxUser);
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable throwable) {

          }
        }));

  }

  public void insert(HxUser hx) {
    HxUser user = com.easemob.easeui.utils.MLDBUtils
        .getFirst(Selector.from(HxUser.class).where("emId", "=", hx.emId));
    if (user != null) {
      return;
    }
    MLDBUtils.saveOrUpdate(hx);
  }


  //有新的系统提醒
  public void setNewNotify(boolean b) {
    HxConfig config = new HxConfig();
    config.id = "1";
    config.isNewNotify = b;
    com.easemob.easeui.utils.MLDBUtils.saveOrUpdate(config);
  }

  public boolean isNewNotify() {
    HxConfig config = MLDBUtils.getFirst(HxConfig.class);
    if (config != null && config.isNewNotify) {
      return true;
    } else {
      return false;
    }
  }
}
