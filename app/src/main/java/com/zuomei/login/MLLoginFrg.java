package com.zuomei.login;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import cn.jpush.android.api.TagAliasCallback;
import com.baichang.android.utils.BCStringUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.comment.TXHomeActivity;
import com.txsh.home.CityActivity;
import com.txsh.market.EventBusModel;
import com.txsh.model.HxUserLoginData;
import com.txsh.utils.HxUtils;
import com.txsh.utils.MLUserCityUtils;
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.base.MLAppDiskCache;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLoginResponse;
import com.zuomei.model.UserCityData.UserCity;
import com.zuomei.services.MLLoginServices;
import com.zuomei.utils.ACache;
import com.zuomei.utils.MLToolUtil;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.ml.base.utils.IEvent;
import cn.ml.base.utils.MLStrUtil;
import org.greenrobot.eventbus.EventBus;

/**
 * 登陆界面
 *
 * @author Marcello
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLLoginFrg extends BaseFragment {

  public static MLLoginFrg INSTANCE = null;

  public static MLLoginFrg instance() {
    INSTANCE = new MLLoginFrg();
    return INSTANCE;
  }

  /**
   * 用户名
   */
  @ViewInject(R.id.login_et_name)
  private EditText _nameEt;

  /**
   * 密码
   */
  @ViewInject(R.id.login_et_pwd)
  private EditText _pwdEt;

  //注册按钮
  @ViewInject(R.id.login_register)
  private Button _registerBtn;

  //登录按钮
  @ViewInject(R.id.login_btn)
  private Button _loginBtn;

  @ViewInject(R.id.login_rg)
  private RadioGroup mGroup;

  @ViewInject(R.id.login_rb_repair)
  private RadioButton mBtnRepair;
  @ViewInject(R.id.login_rb_business)
  private RadioButton mBtnBusiness;

  /**
   * 是否为汽修厂 0（否） 1（是）
   */
  private String isDepot = "1";

  private Context _context;
  private ACache acache;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.qp_login_login, null);
    ViewUtils.inject(this, view);
    _context = inflater.getContext();
    acache = BaseApplication.aCache;
    initData();
    return view;
  }

  private void initData() {
    String username = acache.getAsString(MLConstants.PARAM_REGISTER_USERNAME);
    String pwd = acache.getAsString(MLConstants.PARAM_REGISTER_PWD);
    String deportId = acache.getAsString(MLConstants.PARAM_LOGIN_DEPORTID);

    if (!MLToolUtil.isNull(username)) {
      _nameEt.setText(username);
    }
    if (!MLToolUtil.isNull(pwd)) {
      _pwdEt.setText(pwd);
    }
    if (!MLToolUtil.isNull(deportId)) {
      isDepot = deportId;
      if (MLStrUtil.compare(isDepot, "0")) {
        mBtnRepair.setSelected(true);
      } else {
        mBtnBusiness.setSelected(true);
      }
    }

  }

  /**
   * @description 注册事件
   * @author marcello
   */
  @OnClick(R.id.login_tv_regist)
  public void registerOnClick(View view) {
    _event.onEvent(null, MLConstants.LOGIN_REGISTER);
  }

  @OnClick(R.id.login_tv_phone)
  public void callOnClick(View view) {
    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:4000114234"));
    startActivity(intent);
  }

  /**
   * @description 忘记密码
   * @author marcello
   */
  @OnClick(R.id.login_tv_foget)
  public void pwdOnClick(View view) {
    Intent intent = new Intent();
    intent.setClass(_context, MLAuxiliaryActivity.class);
    intent.putExtra("data", MLConstants.LOGIN_PWD1);
    startActivity(intent);
  }


  private IEvent<Object> _event;

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    _event = (IEvent<Object>) activity;
  }

  boolean isLogin = false;

  /**
   * @description 登录事件
   * @author marcello
   */
  @OnClick(R.id.login_btn_login)
  public void loginOnClick(View view) {
    String username = _nameEt.getText().toString();
    String pwd = _pwdEt.getText().toString();
    if (mBtnRepair.isChecked()) {
      isDepot = "1";
    } else if (mBtnBusiness.isChecked()) {
      isDepot = "0";
    }
    if (MLToolUtil.isNull(username)) {
      _nameEt.setError("用户名不能为空！");
      return;
    }
    if (MLToolUtil.isNull(pwd)) {
      _pwdEt.setError("密码不能为空!");
      return;
    }
    if (BCStringUtil.isEmpty(isDepot)) {
      showMessage("请选择身份!");
      return;
    }

    ZMRequestParams params = new ZMRequestParams();
    params.addParameter(MLConstants.PARAM_REGISTER_USERNAME, username);
    params.addParameter(MLConstants.PARAM_REGISTER_PWD, pwd);
    params.addParameter(MLConstants.PARAM_LOGIN_DEPORT, isDepot);

    ZMHttpRequestMessage message = new ZMHttpRequestMessage(RequestType.LOGIN, null, params,
        _handler, HTTP_RESPONSE_LOGIN, MLLoginServices.getInstance());
    loadDataWithMessage(_context, "正在登录,请稍等....", message);


  }

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

            ((BaseApplication) getActivity().getApplication()).set_user(ret.datas);
            //保存用户名密码
            acache.put(MLConstants.PARAM_REGISTER_USERNAME, _nameEt.getText().toString());
            acache.put(MLConstants.PARAM_REGISTER_PWD, _pwdEt.getText().toString());
            acache.put(MLConstants.PARAM_LOGIN_DEPORTID, isDepot);

            //保存登录信息
            acache.put(MLConstants.PARAM_LOGIN_USER, ret.datas);

            //环信登录
            final HxUserLoginData userLoginData = new HxUserLoginData();
            userLoginData.hxUser = ret.datas.hxUser;
            userLoginData.userId = ret.datas.Id;
            userLoginData.hxPwd = ret.datas.hxPwd;
            if (MLStrUtil.compare(isDepot, "1")) {
              userLoginData.userType = "0";
            } else {
              userLoginData.userType = "1";
            }

            HxUtils.getInstance(getActivity()).login(userLoginData);

            MLAppDiskCache.setUser(userLoginData);

            if (MLUserCityUtils.getCityData(ret.datas.Id, ret.datas.name) != null) {
              UserCity userCity = MLUserCityUtils.getCityData(ret.datas.Id, ret.datas.name);
              BaseApplication._currentCity = userCity.cityId;
              BaseApplication.aCache.put("cityid", userCity.cityId);
              BaseApplication.aCache.put("cityname", userCity.cityName);
              Intent intent = new Intent(_context, TXHomeActivity.class);
              intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
              startActivity(intent);
            } else {
              startAct(getFragment(), CityActivity.class, "1");
            }
            dismissProgressDialog();
            ((MLLoginActivity) _context).finish();
          } else {
            dismissProgressDialog();
            showMessageError("账号或密码错误!");
          }
          isLogin = false;
          break;
        default:
          break;
      }
    }
  };
}
