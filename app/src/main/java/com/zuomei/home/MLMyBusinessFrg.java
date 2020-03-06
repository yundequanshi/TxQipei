package com.zuomei.home;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.util.AbStrUtil;
import com.baichang.android.utils.BCStringUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.quote.BCPopUpWindowsUtils;
import com.txsh.shop.TXShopMyAddressAty;
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.exception.ZMParserException;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.login.MLLoginCityPop;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLMessagePublishResponse;
import com.zuomei.model.MLMyBankResponse;
import com.zuomei.model.MLMyInfoData;
import com.zuomei.model.MLMyInfoResponse;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLMyServices;
import com.zuomei.utils.MLToolUtil;
import com.zuomei.utils.ZMJsonParser;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.ml.base.utils.IEvent;
import cn.ml.base.utils.MLStrUtil;
import cn.ml.base.widget.roundedimageview.RoundedImageView;

/**
 * 我的-商家信息
 *
 * @author Marcello
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyBusinessFrg extends BaseFragment {

  public static MLMyBusinessFrg INSTANCE = null;

  public static MLMyInfoData _info;

  public static MLMyBusinessFrg instance(Object obj) {
    _info = (MLMyInfoData) obj;
    // if(INSTANCE==null){
    INSTANCE = new MLMyBusinessFrg();
    // }
    return INSTANCE;
  }

  /**
   * 头像
   */
  @ViewInject(R.id.my_rl_head)
  private RelativeLayout _headRl;
  /**
   * 名称
   */
  @ViewInject(R.id.my_rl_nice)
  private RelativeLayout _niceRl;
  @ViewInject(R.id.my_tv_nice)
  private TextView _niceTv;
  /**
   * 真实姓名
   */
  @ViewInject(R.id.my_tv_name)
  private TextView _nameTv;
  /**
   * 手机号
   */
  @ViewInject(R.id.my_tv_phone)
  private TextView _phoneTv;
  /**
   * 省市区
   */
  @ViewInject(R.id.my_tv_province)
  private TextView _proviceTv;
  /**
   * 详细地址
   */
  @ViewInject(R.id.my_tv_address)
  private TextView _addressTv;

  /**
   * 支付宝
   */
  @ViewInject(R.id.my_tv_alipay)
  private TextView _alipay;

  @ViewInject(R.id.my_rl_root)
  private RelativeLayout _root;

  @ViewInject(R.id.my_iv_business)
  private RoundedImageView _headIv;

  //收货地址
  @ViewInject(R.id.my_rl_goods)
  private RelativeLayout _goods;

  @ViewInject(R.id.my_ll_manifesto)
  private LinearLayout _manifestoLL;
  @ViewInject(R.id.my_tv_bank)
  private TextView _tvbank;
  @ViewInject(R.id.xiugaimima)
  private TextView xiugaimima;

  @ViewInject(R.id.my_tv_bank)
  private TextView mTvBank;
  @ViewInject(R.id.tv_declaration)
  private TextView tvDeclaration;
  @ViewInject(R.id.my_rl_manifesto)
  private LinearLayout llDeclaration;
  @ViewInject(R.id.my_ll_juti)
  private LinearLayout llJuti;
  @ViewInject(R.id.tv_juti)
  private TextView tvJuti;

  private MLLogin user;
  private String _photoPath;
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
  private MLMessageAddPop _pop;
  private Context _context;
  private String phone = "";
  private String _newStr;
  private String _provice;
  private List<String> phones = new ArrayList<>();
  private View cuView;
  private String showPhone = "";

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    cuView = inflater.inflate(R.layout.my_business, null);
    ViewUtils.inject(this, cuView);
    _context = inflater.getContext();
    user = ((BaseApplication) getActivity().getApplication()).get_user();
    initData();
    initBank();
    initAddress();
    initManifesto();
    return cuView;
  }

  private void initAddress() {
    if (!user.isDepot) {
      _goods.setVisibility(View.GONE);
    }
  }

  private void initManifesto() {
    if (user.isDepot) {
      _manifestoLL.setVisibility(View.GONE);
      llDeclaration.setVisibility(View.GONE);
      llJuti.setVisibility(View.GONE);
    } else {
      _manifestoLL.setVisibility(View.VISIBLE);
      llDeclaration.setVisibility(View.VISIBLE);
      llJuti.setVisibility(View.VISIBLE);
    }
  }

  private void initData() {
    ZMRequestParams catalogParam = new ZMRequestParams();
    if (user.isDepot) {
      catalogParam
          .addParameter(MLConstants.PARAM_LOGIN_DEPORTID, user.Id);
    } else {
      catalogParam.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,
          user.Id);
    }
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(
        RequestType.MY_INFO_D, null, catalogParam, _handler,
        HTTP_RESPONSE_INFO, MLMyServices.getInstance());
    loadDataWithMessage(_context, null, message1);

  }

  private void initBank() {
    MLLogin user = ((BaseApplication) getActivity().getApplication()).get_user();
    ZMRequestParams params = new ZMRequestParams();

    RequestType httpType = null;

    if (user.isDepot) {
      params.addParameter("depotId", user.Id);
      httpType = RequestType.MY_BANK_D;
    } else {
      params.addParameter("companyId", user.Id);
      httpType = RequestType.MY_BANK;
    }

    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(httpType, null, params, _handler,
        HTTP_RESPONSE_BANK, MLMyServices.getInstance());
    loadData(_context, message2);
  }

  private void initView() {
    // //头像
    // if(_info.userPhoto<=0){
    // _headIv.setImageResource(R.drawable.default_my_info_head);
    // }else{
    // String iconUrl = APIConstants.API_IMAGE+"?id="+_info.userPhoto;
    // bitmapUtils.display(_headIv, iconUrl, bigPicDisplayConfig);
    // }
    //
    // 头像
    if (_info.userPhoto <= 0) {
      _headIv.setImageResource(R.drawable.default_my_info_head);
    } else {
      String iconUrl = APIConstants.API_IMAGE + "?id=" + _info.userPhoto;
      _headIv.setTag(iconUrl);
      if (!BaseApplication.IMAGE_CACHE.get(iconUrl, _headIv)) {
        _headIv.setImageDrawable(null);
      }
    }
    if (user.isDepot) {
      phones.add(_info.userPhone);
      showPhone = _info.userPhone;
    } else {
      if (!BCStringUtil.isEmpty(_info.userPhone)) {
        phones.add(_info.userPhone);
        showPhone = showPhone + _info.userPhone + ",";
      }
      if (!BCStringUtil.isEmpty(_info.userPhone2)) {
        phones.add(_info.userPhone2);
        showPhone = showPhone + _info.userPhone2 + ",";
      }
      if (!BCStringUtil.isEmpty(_info.userPhone3)) {
        phones.add(_info.userPhone3);
        showPhone = showPhone + _info.userPhone3 + ",";
      }
      if (!BCStringUtil.isEmpty(_info.userPhone4)) {
        phones.add(_info.userPhone4);
        showPhone = showPhone + _info.userPhone4 + ",";
      }
      if (!BCStringUtil.isEmpty(_info.userPhone5)) {
        phones.add(_info.userPhone5);
        showPhone = showPhone + _info.userPhone5 + ",";
      }
      if (!BCStringUtil.isEmpty(showPhone)) {
        showPhone = showPhone.substring(0, showPhone.length() - 1);
      }
    }
    _nameTv.setText(_info.depotName);
    _niceTv.setText(_info.depotName);
    _phoneTv.setText(showPhone);
    _proviceTv.setText(_info.location);
    _addressTv.setText(_info.address);
    tvJuti.setText(_info.concurrenOperate);
    _alipay.setText(_info.alipay);
    tvDeclaration.setText(_info.declaration);
    xiugaimima.setText("******");
    // _tvbank.setText(_info.);

    View mAvatarView = LayoutInflater.from(_context).inflate(
        R.layout.message_choose_avatar, null);
    if (getActivity() == null) {
      return;
    }
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

  @OnClick(R.id.home_top_back)
  public void cancelOnClick(View view) {
    ((MLAuxiliaryActivity) _context).setResult(MLConstants.RESULT_BUSINESS);
    ((MLAuxiliaryActivity) _context).finish();
  }

  /**
   * @description 头像事件
   * @author marcello
   */
  @OnClick(R.id.my_rl_head)
  public void headOnClick(View view) {
    if (_pop == null || _root == null) {
      return;
    }
    _pop.showAtLocation(_root, Gravity.CENTER, 0, 0);
  }

  /**
   * @description 密码修改
   * @author marcello
   */
  @OnClick(R.id.my_rl_password)
  public void passwordOnClick(View view) {
    _event.onEvent(null, MLConstants.MY_PASSWORD);
  }

  /**
   * @description 密保问题
   * @author marcello
   */
  @OnClick(R.id.my_rl_protect)
  public void protectOnClick(View view) {
    _event.onEvent(null, MLConstants.MY_PROTECT);
  }


  /**
   * @description 密保问题
   * @author marcello
   */
  @OnClick(R.id.my_rl_goods)
  public void goodOnClick(View view) {
    startAct(MLMyBusinessFrg.this, TXShopMyAddressAty.class);
  }


  /**
   * @description 城市选择
   * @author marcello
   */
  @OnClick(R.id.my_rl_province)
  public void proviceOnClick(View view) {
    MLLoginCityPop menuWindow = new MLLoginCityPop(getActivity(),
        new IEvent<String>() {
          @Override
          public void onEvent(Object source, String eventArg) {
            _provice = eventArg;
            _proviceTv.setText(eventArg);
            _addressTv.setText("");
            // updateLocation(eventArg);
          }
        });
    menuWindow.showAtLocation(_root, Gravity.CENTER, 0, 0);
  }

  @OnClick(R.id.my_rl_bank)
  public void bankOnClick(View view) {
    _event.onEvent(null, MLConstants.MY_BANK_CARD);
  }

  @OnClick({R.id.my_rl_nice, R.id.my_rl_name, R.id.my_rl_address,
      R.id.my_rl_alipay, R.id.my_rl_manifesto, R.id.my_ll_juti})
  public void inputOnClick(View view) {
    final int id = view.getId();
    String text = "";
    if (id == R.id.my_rl_address
        && MLToolUtil.isNull(_proviceTv.getText().toString())) {
      showMessageWarning("请选择地区");
      return;
    }

    switch (id) {
      case R.id.my_rl_nice:
        text = _info.depotName;
        break;
      case R.id.my_rl_name:
        text = _info.depotName;
        break;
      case R.id.my_rl_address:
        text = _info.address;
        break;
      case R.id.my_rl_alipay:
        text = _info.alipay;
        break;
      case R.id.my_rl_manifesto:
        text = _info.declaration;
        break;
      case R.id.my_ll_juti:
        text = _info.concurrenOperate;
        break;
      default:
        break;
    }

    MLMyInputPop menuWindow = new MLMyInputPop(getActivity(), text,
        new IEvent<String>() {
          @Override
          public void onEvent(Object source, String eventArg) {
            _newStr = eventArg;
            switch (id) {
              case R.id.my_rl_nice:
                _info.depotName = _newStr;
                //updateDrpotName(eventArg);
                break;
              case R.id.my_rl_name:
                _info.realName = _newStr;
                //updateRealName(eventArg);
                updateDrpotName(eventArg);
                break;
              case R.id.my_rl_address:
                _info.address = _newStr;
                updateAddress(eventArg);
                break;
              case R.id.my_rl_alipay:
                _info.alipay = _newStr;
                updateAlipay(eventArg);
                break;
              case R.id.my_rl_manifesto:
                _info.declaration = _newStr;
                updateDeclaration(eventArg);
                break;
              case R.id.my_ll_juti:
                _info.concurrenOperate = _newStr;
                updateConcurrenOperate(eventArg);
                break;
              default:
                break;
            }
          }

        });
    menuWindow.showAtLocation(_root, Gravity.CENTER, 0, 0);
  }

  @OnClick(R.id.my_rl_phone)
  private void phoneOnClick(View view) {
    View centerOfView = LayoutInflater.from(getActivity()).inflate(R.layout.me_info_pop, null);
    final PopupWindow popupWindow = BCPopUpWindowsUtils.getIstnace()
        .getPopUpWindows(centerOfView, 0, 0, getActivity(), 0.3f, true)
        .showCenterOfView(_root);
    ImageView btnClose = (ImageView) centerOfView.findViewById(R.id.btn_close);
    TextView btnOk = (TextView) centerOfView.findViewById(R.id.login_city_ok);
    ListView lvMePhones = (ListView) centerOfView.findViewById(R.id.lv_me_phone);
    final PopMePhonesAdapter popMePhonesAdapter = new PopMePhonesAdapter(getActivity(), phones);
    lvMePhones.setAdapter(popMePhonesAdapter);
    btnClose.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        popupWindow.dismiss();
      }
    });
    btnOk.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        String phone = "";
        String phone2 = "";
        String phone3 = "";
        String phone4 = "";
        String phone5 = "";
        phones = popMePhonesAdapter.getList();
        if (!phones.isEmpty()) {
          if (phones.size() == 1) {
            showPhone = phones.get(0);
            phone = phones.get(0);
          }
          if (phones.size() == 2) {
            phone = phones.get(0);
            phone2 = phones.get(1);
            showPhone = phone + "," + phone2;
          }
          if (phones.size() == 3) {
            phone = phones.get(0);
            phone2 = phones.get(1);
            phone3 = phones.get(2);
            showPhone = phone + "," + phone2 + "," + phone3;
          }
          if (phones.size() == 4) {
            phone = phones.get(0);
            phone2 = phones.get(1);
            phone3 = phones.get(2);
            phone4 = phones.get(3);
            showPhone = phone + "," + phone2 + "," + phone3 + "," + phone4;
          }
          if (phones.size() == 5) {
            phone = phones.get(0);
            phone2 = phones.get(1);
            phone3 = phones.get(2);
            phone4 = phones.get(3);
            phone5 = phones.get(4);
            showPhone = phone + "," + phone2 + "," + phone3 + "," + phone4 + "," + phone5;
          }
          if (user.isDepot) {
            List<String> phones = new ArrayList<String>();
            phones.add(phone);
            updatePhone(phones);
          } else {
            updateCompanyPhone(phone, phone2, phone3, phone4, phone5);
          }
          popupWindow.dismiss();
        }
      }
    });
  }

  /**
   * 支付密码
   *
   * @description
   * @author marcello
   */
  @OnClick(R.id.my_rl_pwd)
  private void pwdOnClick(View view) {
    _event.onEvent(_info.userPhone, MLConstants.MY_PAY_PWD);
  }

  /**
   * 修改名称
   */
  protected void updateDrpotName(String name) {
    MLLogin user = ((BaseApplication) getActivity().getApplication())
        .get_user();
    ZMRequestParams params = new ZMRequestParams();
    if (user.isDepot) {
      params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID, user.Id);
    } else {
      params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1, user.Id);
    }
    params.addParameter(MLConstants.PARAM_REGISTER_DEPORTNAME, name);
    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
        RequestType.MY_INFO_DEPOTNAME, null, params, _handler,
        HTTP_RESPONSE_DEPOTNAME, MLMyServices.getInstance());
    loadDataWithMessage(_context, null, message2);
  }

  /**
   * 修改商家的电话
   */
  protected void updateCompanyPhone(String phone, String phone2, String phone3, String phone4,
      String phone5) {
    MLLogin user = ((BaseApplication) getActivity().getApplication())
        .get_user();
    ZMRequestParams params = new ZMRequestParams();
    params.addParameter("companyId", user.Id);
    params.addParameter("phone", phone);
    params.addParameter("phone2", phone2);
    params.addParameter("phone3", phone3);
    params.addParameter("phone4", phone4);
    params.addParameter("phone5", phone5);
    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
        RequestType.UPDATE_COMPANY_PHONE, null, params, _handler,
        HTTP_RESPONSE_UPDATE_PHONE, MLMyServices.getInstance());
    loadDataWithMessage(_context, null, message2);
  }

  /**
   * 修改具体
   */
  protected void updateConcurrenOperate(String declaration) {
    MLLogin user = ((BaseApplication) getActivity().getApplication())
        .get_user();
    ZMRequestParams params = new ZMRequestParams();
    params.addParameter("companyId", user.Id);
    params.addParameter("concurrenOperate", declaration);
    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
        RequestType.UPDATE_CONCURREN_OPERATE, null, params, _handler,
        HTTP_RESPONSE_CONCURREN_OPERATE, MLMyServices.getInstance());
    loadDataWithMessage(_context, null, message2);
  }

  /**
   * 修改企业宣言
   */
  protected void updateDeclaration(String declaration) {
    MLLogin user = ((BaseApplication) getActivity().getApplication())
        .get_user();
    ZMRequestParams params = new ZMRequestParams();
    params.addParameter("companyId", user.Id);
    params.addParameter("declaration", declaration);
    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
        RequestType.UPDATE_COMPANY_DECLARATION, null, params, _handler,
        HTTP_RESPONSE_DECLARATION, MLMyServices.getInstance());
    loadDataWithMessage(_context, null, message2);
  }

  /**
   * 修改支付宝
   */
  protected void updateAlipay(String name) {
    MLLogin user = ((BaseApplication) getActivity().getApplication())
        .get_user();
    ZMRequestParams params = new ZMRequestParams();
    if (user.isDepot) {
      params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID, user.Id);
    } else {
      params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1, user.Id);
    }

    params.addParameter(MLConstants.PARAM_MY_ALIPAY, name);
    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
        RequestType.MY_INFO_ALIPAY, null, params, _handler,
        HTTP_RESPONSE_ALIPAY, MLMyServices.getInstance());
    loadDataWithMessage(_context, null, message2);
  }


  /**
   * 修改手机号
   */
  protected void updateDepot(String name) {
    MLLogin user = ((BaseApplication) getActivity().getApplication())
        .get_user();
    ZMRequestParams params = new ZMRequestParams();
    params.addParameter("depotId", user.Id);
    params.addParameter("userPhoto", _info.userPhoto + "");
    params.addParameter("depotName", _info.depotName);
    params.addParameter("userPhone", name);
    params.addParameter("location", _info.location);
    params.addParameter("address", _info.address);
    params.addParameter("cardNo", _info.cardNo);
    params.addParameter("openBank", _info.openBank);
    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
        RequestType.MY_UPDATE_DEPOT, null, params, _handler,
        HTTP_RESPONSE_REALNAME2, MLMyServices.getInstance());
    loadDataWithMessage(_context, null, message2);
  }

  /**
   * 修改姓名
   */
  protected void updateRealName(String name) {
    MLLogin user = ((BaseApplication) getActivity().getApplication())
        .get_user();
    ZMRequestParams params = new ZMRequestParams();
    if (user.isDepot) {
      params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID, user.Id);
    } else {
      params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1, user.Id);
    }
    params.addParameter(MLConstants.PARAM_MY_REALNAME, name);
    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
        RequestType.MY_INFO_REALNAME, null, params, _handler,
        HTTP_RESPONSE_REALNAME, MLMyServices.getInstance());
    loadDataWithMessage(_context, null, message2);
  }

  /**
   * 修改地区
   */
  /*
   * protected void updateLocation(String name) { MLLogin user =
	 * ((BaseApplication)getActivity().getApplication()).get_user();
	 * ZMRequestParams params = new ZMRequestParams();
	 * params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,user.Id);
	 * params.addParameter(MLConstants.PARAM_REGISTER_LOCATION,name);
	 * ZMHttpRequestMessage message2 = new
	 * ZMHttpRequestMessage(RequestType.MY_INFO_LOCATION, null, params,
	 * _handler,HTTP_RESPONSE_LOCATION, MLMyServices.getInstance());
	 * loadDataWithMessage(_context, null, message2); }
	 */

  /**
   * 修改详细地址
   */
  protected void updateAddress(String name) {

    if (MLToolUtil.isNull(_provice)) {
      _provice = _proviceTv.getText().toString();
    }

    MLLogin user = ((BaseApplication) getActivity().getApplication())
        .get_user();
    ZMRequestParams params = new ZMRequestParams();
    if (user.isDepot) {
      params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID, user.Id);
    } else {
      params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1, user.Id);
    }
    params.addParameter(MLConstants.PARAM_REGISTER_LOCATION, _provice);
    params.addParameter(MLConstants.PARAM_REGISTER_ADDRESSS, name);
    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
        RequestType.MY_INFO_ADDRESS, null, params, _handler,
        HTTP_RESPONSE_ADDRESS, MLMyServices.getInstance());
    loadDataWithMessage(_context, null, message2);
  }

  /**
   * 修改详电话
   */

  protected void updatePhone(List<String> datas) {
    MLLogin user = ((BaseApplication) getActivity().getApplication())
        .get_user();
    ZMRequestParams params = new ZMRequestParams();
    if (user.isDepot) {
      params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID, user.Id);
    } else {
      params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1, user.Id);
    }

    for (int i = 0; i < datas.size(); i++) {
      String num = datas.get(i);
      if (MLToolUtil.isNull(num)) {
        break;
      }
      if (i == 0) {
        phone = num;
        _info.userPhone = num;
        params.addParameter(MLConstants.PARAM_MY_PHONE, num);
      } else if (i == 1) {
        _info.userPhone2 = num;
        params.addParameter(MLConstants.PARAM_MY_PHONE1, num);
      } else if (i == 2) {
        _info.userPhone3 = num;
        params.addParameter(MLConstants.PARAM_MY_PHONE2, num);
      }
    }

    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
        RequestType.MY_INFO_PHONE, null, params, _handler,
        HTTP_RESPONSE_PHONE, MLMyServices.getInstance());
    loadDataWithMessage(_context, null, message2);
  }

  // =====================================拍照====================================================================

  /**
   * 描述：因为调用了Camera和Gally所以要判断他们各自的返回情况, 他们启动时是这样的startActivityForResult
   */
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent mIntent) {
    if (_pop != null) {
      _pop.dismiss();
    }
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
        if (D) {
          Log.d(TAG, "将要进行裁剪的图片的路径是 = " + mCurrentPhotoFile.getPath());
        }
        String currentFilePath2 = mCurrentPhotoFile.getPath();
        Uri uri2 = Uri.fromFile(mCurrentPhotoFile);
        startPhotoZoom(uri2);
        // Intent intent2 = new Intent(getActivity(),
        // MLCropImageActivity.class);
        // intent2.putExtra("PATH", currentFilePath2);
        // startActivityForResult(intent2, CAMERA_CROP_DATA);

        break;
      case CAMERA_CROP_DATA:
        _photoPath = mCurrentPhotoFile.getPath();
        String currentFilePath3 = mCurrentPhotoFile.getPath();
        uploadImage(currentFilePath3);
        break;
    }
  }

  /**
   * 裁剪图片方法实现
   */
  public void startPhotoZoom(Uri uri) {
    /*
     * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的，小马不懂C C++
		 * 这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么 制做的了...吼吼
		 */
    Intent intent = new Intent("com.android.camera.action.CROP");
    intent.setDataAndType(uri, "image/*");
    // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
    intent.putExtra("crop", "true");
    // aspectX aspectY 是宽高的比例
    intent.putExtra("aspectX", 1);
    intent.putExtra("aspectY", 1);
    // outputX outputY 是裁剪图片宽高
    intent.putExtra("outputX", 150);
    intent.putExtra("outputY", 150);
    intent.putExtra("return-data", true);

    String mFileName = System.currentTimeMillis() + ".jpg";

    mCurrentPhotoFile = new File(Environment.getExternalStorageDirectory(),
        mFileName);

    // mCurrentPhotoFile = getAttachFolder();
    // mCurrentPhotoFile = new File(PHOTO_DIR, mFileName);
    intent.putExtra(MediaStore.EXTRA_OUTPUT,
        Uri.fromFile(mCurrentPhotoFile));

    startActivityForResult(intent, CAMERA_CROP_DATA);
  }

  /**
   * 描述：从相机获取
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
      mCurrentPhotoFile = getAttachFolder();
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
    String[] projection = {MediaStore.Images.Media.DATA};
    Cursor cursor = getActivity().managedQuery(uri, projection, null, null,
        null);
    int column_index = cursor
        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    cursor.moveToFirst();
    String path = cursor.getString(column_index);
    return path;
  }

  // =====================================网络操作====================================================================

  // 上传头像
  private void uploadImage(String photoPath) {
    RequestParams params = new RequestParams();
    params.addBodyParameter("file", new File(photoPath));

    HttpUtils http = new HttpUtils();
    http.send(HttpRequest.HttpMethod.POST,
        APIConstants.API_IMAGE_UPLOAD_OLD, params,
        new RequestCallBack<String>() {

          @Override
          public void onStart() {
          }

          @Override
          public void onLoading(long total, long current,
              boolean isUploading) {
          }

          @Override
          public void onSuccess(ResponseInfo<String> responseInfo) {
            try {
              MLMessagePublishResponse ret = ZMJsonParser
                  .fromJsonString(
                      MLMessagePublishResponse.class,
                      responseInfo.result);
              if (ret.state.equalsIgnoreCase("1")) {
                setHead(ret.datas);
              } else {
                showMessage("图片上传失败!");
              }
            } catch (ZMParserException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
          }

          @Override
          public void onFailure(HttpException error, String msg) {
            showMessage("图片上传失败!");
          }
        });
  }

  // 上传头像
  private void setHead(String id) {
    MLLogin user = ((BaseApplication) getActivity().getApplication())
        .get_user();
    ZMRequestParams params = new ZMRequestParams();
    if (!MLToolUtil.isNull(id)) {
      params.addParameter(MLConstants.PARAM_MY_ICONID, id);
    }
    if (user.isDepot) {
      params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID, user.Id);
    } else {
      params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1, user.Id);
    }
    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
        RequestType.MY_INFO_HEAD, null, params, _handler,
        HTTP_RESPONSE_HEAD, MLMyServices.getInstance());
    loadDataWithMessage(_context, null, message2);
  }

  private static final int HTTP_RESPONSE_HEAD = 0;
  private static final int HTTP_RESPONSE_DEPOTNAME = 1;
  private static final int HTTP_RESPONSE_REALNAME = 2;
  private static final int HTTP_RESPONSE_REALNAME2 = 12;
  private static final int HTTP_RESPONSE_PHONE = 3;
  private static final int HTTP_RESPONSE_ALIPAY = 4;
  private static final int HTTP_RESPONSE_LOCATION = 5;
  private static final int HTTP_RESPONSE_ADDRESS = 6;
  private static final int HTTP_RESPONSE_INFO = 7;
  private static final int HTTP_RESPONSE_BANK = 8;
  private static final int HTTP_RESPONSE_DECLARATION = 9;
  private static final int HTTP_RESPONSE_UPDATE_PHONE = 10;
  private static final int HTTP_RESPONSE_CONCURREN_OPERATE = 11;

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
        // 保存头像
        case HTTP_RESPONSE_HEAD: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            Drawable drawable = Drawable.createFromPath(_photoPath);
            _headIv.setImageDrawable(drawable);
          } else {
            showMessage("上传头像失败!");
          }
          break;
        }
        // 修改名称
        case HTTP_RESPONSE_DEPOTNAME: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            _nameTv.setText(_newStr);
          } else {
            showMessage("修改名称失败!");
          }
          break;
        }
        // 修改真实姓名
        case HTTP_RESPONSE_REALNAME: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            _phoneTv.setText(showPhone);
          } else {
            showMessage("修改手机号失败!");
          }
          break;
        }
        // 修改真实姓名
        case HTTP_RESPONSE_REALNAME2: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            _phoneTv.setText(showPhone);
          } else {
            showMessage("修改手机号失败!");
          }
          break;
        }
        case HTTP_RESPONSE_DECLARATION: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            tvDeclaration.setText(_newStr);
            showMessage("修改企业宣言成功!");
          } else {
            showMessage("修改企业宣言失败!");
          }
          break;
        }
        case HTTP_RESPONSE_CONCURREN_OPERATE: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            tvJuti.setText(_newStr);
            showMessage("修改具体成功!");
          } else {
            showMessage("修改具体失败!");
          }
          break;
        }
        case HTTP_RESPONSE_UPDATE_PHONE: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            _phoneTv.setText(showPhone);
            showMessage("修改手机号成功!");
          } else {
            showMessage("修改手机号失败!");
          }
          break;
        }
        // 修改手机号
        case HTTP_RESPONSE_PHONE: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            _phoneTv.setText(showPhone);
          } else {
            showMessage("修改手机号失败!");
          }
          break;
        }
        // 修改支付宝
        case HTTP_RESPONSE_ALIPAY: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            _alipay.setText(_newStr);
          } else {
            showMessage("修改支付宝失败!");
          }
          break;
        }

        // 修改地区
        case HTTP_RESPONSE_LOCATION: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            _proviceTv.setText(_newStr);
          } else {
            showMessage("修改地区失败!");
          }
          break;
        }
        // 修改详细地址
        case HTTP_RESPONSE_ADDRESS: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            _addressTv.setText(_newStr);
          } else {
            // showMessage("修改详细地址失败!");
            showMessageError(ret.message);
          }
          break;
        }

        case HTTP_RESPONSE_INFO: {
          MLMyInfoResponse ret = (MLMyInfoResponse) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            _info = ret.datas;
            initView();
          } else {
            showMessage("获取基本信息失败!");
          }
          break;
        }

        case HTTP_RESPONSE_BANK: {
          MLMyBankResponse ret = (MLMyBankResponse) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            if (ret.datas.size() > 0) {

              if (ret.datas.size() > 0) {

                String card = ret.datas.get(0).card;
                if (!MLStrUtil.isEmpty(card) && card.length() > 8) {
                  String maskNumber =
                      "***********" + card.substring(card.length() - 4, card.length());
                  mTvBank.setText(maskNumber + "\n" + ret.datas.get(0).openBank);
                } else {
                  mTvBank.setText("");
                }

              }

            }

          } else {
            showMessage("获取银行卡信息失败");
          }
          break;
        }
        default:
          break;
      }
    }
  };

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
}
