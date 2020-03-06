package com.zuomei.login;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import com.baichang.android.utils.BCStringUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.comment.TXHomeActivity;
import com.txsh.home.CityActivity;
import com.txsh.home.TXXieYi;
import com.txsh.model.HxUserLoginData;
import com.txsh.utils.HxUtils;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.base.MLAppDiskCache;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLoginResponse;
import com.zuomei.services.MLLoginServices;
import com.zuomei.utils.MLToolUtil;

import cn.ml.base.utils.IEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * 注册界面
 *
 * @author Marcello
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLRegisterFrg extends BaseFragment {

  public static MLRegisterFrg INSTANCE = null;

  public static MLRegisterFrg instance() {
    INSTANCE = new MLRegisterFrg();
    return INSTANCE;
  }

  /**
   * 用户名
   */
  @ViewInject(R.id.login_et_username)
  private EditText _usernameEt;
  /**
   * 密码
   */
  @ViewInject(R.id.login_et_pwd1)
  private EditText _pwd1Et;
  /**
   * 重复密码
   */
  @ViewInject(R.id.login_et_pwd2)
  private EditText _pwd2Et;
  /**
   * 名称
   */
  @ViewInject(R.id.login_et_nick)
  private EditText _nickEt;
  /**
   * 地址
   */
  @ViewInject(R.id.login_et_address)
  private EditText _addressEt;
  /**
   * 详细地址
   */
  @ViewInject(R.id.login_et_dadress)
  private EditText _dadressEt;

	/*@ViewInject(R.id.tv_agreement)
  private TextView _agreementTv;*/

  @ViewInject(R.id.login_register_root)
  private RelativeLayout _root;
  private Context _context;


  @ViewInject(R.id.checkBox)
  private CheckBox _checkBox;

  /**
   * 注册 标示
   */
  private boolean isPass = true;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.login_register, null);
    ViewUtils.inject(this, view);

    _context = inflater.getContext();

    //_agreementTv.setText(Html.fromHtml("<u>同意条款</u>"));
    return view;
  }


  @OnClick(R.id.regist_tv_service)
  public void agreeOnClick(View view) {
    Intent intent = new Intent(getActivity(), TXXieYi.class);
    startActivity(intent);
  }

  @OnClick(R.id.boda_linear)
  public void bodaClick(View view) {
    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:4000114234"));
    startActivity(intent);
  }

  /**
   * @description 注册按钮
   * @author marcello
   */
  @OnClick(R.id.login_btn_register)
  public void resisterOnClick(View view) {

    String userName = _usernameEt.getText().toString();
    String pwd1 = _pwd1Et.getText().toString();
    String pwd2 = _pwd2Et.getText().toString();
    String nick = _nickEt.getText().toString();
    String address = _addressEt.getText().toString();
    String daddress = _dadressEt.getText().toString();

    if (BCStringUtil.isEmpty(userName)) {
      showMessage("手机号不能为空!");
      return;
    }

    if (BCStringUtil.isEmpty(pwd1)) {
      showMessage("密码不能为空!");
      return;
    }

    if (BCStringUtil.isEmpty(pwd2)) {
      showMessage("确认密码不能为空!");
      return;
    }

    if (!pwd1.equalsIgnoreCase(pwd2)) {
      showMessage("两次密码输入不一致!");
      return;
    }
    if (BCStringUtil.isEmpty(nick)) {
      showMessage("用户名不能为空!");
      return;
    }
    if (BCStringUtil.isEmpty(address)) {
      showMessage("请选择地址！");
      return;
    }
    if (BCStringUtil.isEmpty(daddress)) {
      showMessage("请输入详细地址！");
      return;
    }

    if (!_checkBox.isChecked()) {
      showMessage("请阅读并同意条款！");
      return;
    }
    ZMRequestParams params = new ZMRequestParams();
    params.addParameter(MLConstants.PARAM_REGISTER_USERNAME, userName);
    params.addParameter(MLConstants.PARAM_REGISTER_PWD, pwd2);
    params.addParameter(MLConstants.PARAM_REGISTER_LOCATION, address);
    params.addParameter(MLConstants.PARAM_REGISTER_ADDRESSS, daddress);
    params.addParameter(MLConstants.PARAM_REGISTER_DEPORTNAME, nick);

    ZMHttpRequestMessage message = new ZMHttpRequestMessage(RequestType.REGISTER, null, params,
        _handler, HTTP_RESPONSE_REGISTER, MLLoginServices.getInstance());
    loadDataWithMessage(_context, "正在注册，请稍等...", message);

  }

  private static final int HTTP_RESPONSE_REGISTER = 0;
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
        case HTTP_RESPONSE_REGISTER:

          MLLoginResponse ret = (MLLoginResponse) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            showMessageSuccess("注册成功!");
            // 保存用户名
            BaseApplication.aCache
                .put(MLConstants.PARAM_REGISTER_USERNAME, _usernameEt.getText().toString());
            BaseApplication.aCache
                .put(MLConstants.PARAM_REGISTER_PWD, _pwd1Et.getText().toString());
            BaseApplication.aCache.put(MLConstants.PARAM_LOGIN_DEPORT, "汽修厂");
            BaseApplication.aCache.put(MLConstants.PARAM_LOGIN_DEPORTID, "1");

            ret.datas.isDepot = true;

            //环信登录
            HxUserLoginData userLoginData = new HxUserLoginData();
            userLoginData.hxUser = ret.datas.hxUser;
            userLoginData.userId = ret.datas.Id;
            userLoginData.hxPwd = ret.datas.hxPwd;
            userLoginData.userType = "0";
            HxUtils.getInstance(getActivity()).login(userLoginData);
            MLAppDiskCache.setUser(userLoginData);

            //保存登录信息
            BaseApplication.getInstance().set_user(ret.datas);
            startAct(getFragment(), CityActivity.class, "1");
            ((MLLoginActivity) _context).finish();
          } else {
            if (MLToolUtil.isNull(ret.message)) {
              ret.message = "注册失败!";
            }
            showMessageError(ret.message);
          }

          break;
        default:
          break;
      }
    }
  };

  /**
   * @description 选择地址
   * @author marcello
   */
  @OnClick(R.id.login_et_address)
  public void addressOnClick(View view) {
    /*	_cityPicker = (CityPicker) mView.findViewById(R.id.login_citypicker);
       _okBtn = (TextView) mView.findViewById(R.id.login_city_ok);
	      AbDialogUtil.showAlertDialog(mView);
		
	   _okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String cityString = _cityPicker.getCity_string();
				if(cityString!=null&&!cityString.equalsIgnoreCase("")){
					_addressEt.setText(cityString);
				}
				AbDialogUtil.removeDialog(_context);
			}
		});*/

    MLLoginCityPop menuWindow = new MLLoginCityPop(getActivity(), new IEvent<String>() {
      @Override
      public void onEvent(Object source, String eventArg) {
        _addressEt.setText(eventArg);
      }
    });
    menuWindow.showAtLocation(_root, Gravity.CENTER, 0, 0);
  }

  /**
   * @description 返回
   * @author marcello
   */
  @OnClick(R.id.login_top_back)
  public void backOnClick(View view) {
    getActivity().onBackPressed();
  }
}
