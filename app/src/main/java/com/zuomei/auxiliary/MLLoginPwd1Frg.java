package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLRegister;
import com.zuomei.model.MLSpecialResonse;
import com.zuomei.services.MLLoginServices;
import com.zuomei.utils.MLToolUtil;

import org.json.JSONException;
import org.json.JSONObject;

import cn.ml.base.utils.IEvent;

/**
 * 登录-忘记密码 step1
 *
 * @author Marcello
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLLoginPwd1Frg extends BaseFragment {

  public static MLLoginPwd1Frg INSTANCE = null;

  public static MLLoginPwd1Frg instance() {
    INSTANCE = new MLLoginPwd1Frg();
    return INSTANCE;
  }

  @ViewInject(R.id.et_name)
  private EditText _nameEt;
  private Context _context;
  String phone;
  private TimeCount time;
  @ViewInject(R.id.et_code)
  private EditText et_code;
  @ViewInject(R.id.btn_time)
  private Button _timeBtn;
  private String mCode;
  @ViewInject(R.id.et_oldPwd)
  private EditText _oldEt;

  @ViewInject(R.id.et_newPwd)
  private EditText _newEt;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.login_pwd1, null);
    ViewUtils.inject(this, view);
    _context = inflater.getContext();
    time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
    return view;
  }


  /**
   * @description 确定
   * @author marcello
   */
  @OnClick(R.id.btn_next)
  public void nextOnClick(View view) {
    phone = _nameEt.getText().toString();
    String oldPwd = _oldEt.getText().toString();
    String newPwd = _newEt.getText().toString();
    String code = et_code.getText().toString();

    if (MLToolUtil.isNull(phone)) {
      showMessageWarning("请输入手机号");
      return;
    }

    if (MLToolUtil.isNull(code)) {
      showMessageError("验证码不能为空");
      return;
    }

    if (!oldPwd.equalsIgnoreCase(newPwd)) {
      showMessageWarning("两次密码输入不一致！");
      return;
    }
    submitPwd(phone,newPwd);
  }

  /**
   * @description 获取验证码
   * @author marcello
   */
  @OnClick(R.id.btn_time)
  public void timeOnClick(View view) {
    phone = _nameEt.getText().toString();
    if (MLToolUtil.isNull(phone)) {
      showMessageWarning("请输入手机号");
      return;
    }
    time.start();
    ZMRequestParams param = new ZMRequestParams();
    JSONObject jo = new JSONObject();
    try {
      jo.put(MLConstants.PARAM_LOGIN_NUMBER, phone);
    } catch (JSONException e) {
      return;
    }
    param.addParameter("data", jo.toString());
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.LOGIN_GETCODE, null, param,
        _handler, HTTP_RESPONSE_CODE, MLLoginServices.getInstance());
    loadDataWithMessage(_context, null, message1);
  }

  public void submitPwd(String phone,String pwd) {

    ZMRequestParams param = new ZMRequestParams();
    param.addParameter("userPhoneNumber", phone);
    param.addParameter("password", pwd);
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.LOGIN_RESET_PWD, null,
        param, __handler, HTTP_RESPONSE_PWD, MLLoginServices.getInstance());
    loadDataWithMessage(_context, null, message1);

  }

  private static final int HTTP_RESPONSE_CODE = 1;
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
        case HTTP_RESPONSE_CODE: {
          MLSpecialResonse ret = (MLSpecialResonse) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            mCode = ret.datas;
          }
          break;
        }
        default:
          break;
      }
    }
  };

  private static final int HTTP_RESPONSE_PWD = 2;
  private Handler __handler = new Handler() {
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
        case HTTP_RESPONSE_PWD: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            showMessageSuccess("密码修改成功!");
            ((MLAuxiliaryActivity) _context).finish();
          } else {
            showMessageError("密码修改失败!");
          }
          break;
        }
        default:
          break;
      }
    }
  };

  /* 定义一个倒计时的内部类 */
  class TimeCount extends CountDownTimer {

    public TimeCount(long millisInFuture, long countDownInterval) {
      super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
    }

    @Override
    public void onFinish() {// 计时完毕时触发
      _timeBtn.setText("重新验证");
      _timeBtn.setClickable(true);
    }

    @Override
    public void onTick(long millisUntilFinished) {// 计时过程显示
      _timeBtn.setClickable(false);
      _timeBtn.setText(millisUntilFinished / 1000 + "秒");
    }
  }

  /**
   * @description 返回
   * @author marcello
   */
  @OnClick(R.id.home_top_back)
  public void backOnClick(View view) {
    ((MLAuxiliaryActivity) _context).finish();
  }
}
