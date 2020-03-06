package com.zuomei.home;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qipei.me.MLIntegralAty;
import com.txsh.R;
import com.txsh.home.TXIntegralShopAty;
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLIntegralData;
import com.zuomei.model.MLIntegralResponse;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLMyServices;
import com.zuomei.utils.MLToolUtil;
import com.zuomei.widget.MLCircleImageView;

import java.util.Calendar;

import cn.ml.base.utils.IEvent;

/**
 * 我的积分
 *
 * @author Marcello
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyIntegralFrg extends BaseFragment {

  public static MLMyIntegralFrg INSTANCE = null;

  public static MLMyIntegralFrg instance() {
    // if(INSTANCE==null){
    INSTANCE = new MLMyIntegralFrg();
    // }
    return INSTANCE;
  }

  @ViewInject(R.id.tv_name)
  private TextView _nameTv;

  @ViewInject(R.id.tv_balance)
  private TextView _balanceTv;

  @ViewInject(R.id.my_iv_head)
  private MLCircleImageView _headIv;

  @ViewInject(R.id.money_root)
  private RelativeLayout _root;

  @ViewInject(R.id.btn_sign)
  private Button _signBtn;

  @ViewInject(R.id.money_iv_head)
  private ImageView _headBg;

  @ViewInject(R.id.tv_sm)
  private TextView _smTv;

  @ViewInject(R.id.intrgral_ll_shop)
  private LinearLayout _llshop;

  @ViewInject(R.id.web)
  private WebView _webview;
  private MLMyMoneyAdapter _moneyAdapter;
  private Context _context;
  private MLLogin _user;
  private MLIntegralData _info;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.my_intrgral, null);
    ViewUtils.inject(this, view);

    _context = inflater.getContext();

    initView();
    initData();
    return view;
  }

  private void initView() {
    _user = ((BaseApplication) getActivity().getApplication()).get_user();
    _moneyAdapter = new MLMyMoneyAdapter(_context);
//		Bitmap bitmap = MLToolUtil.readBitMap(_context, R.drawable.my_money_bg);
    // 设置背景
//		_headBg.setImageBitmap(bitmap);

    _webview.getSettings().setJavaScriptEnabled(true);
    // _webview.setInitialScale(25);//为25%，最小缩放等级

    _webview.getSettings().setBuiltInZoomControls(true);
    _webview.getSettings().setUseWideViewPort(true);
    _webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
    _webview.setWebViewClient(new WebViewClient() {
      @Override
      public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        dismissProgressDialog();
      }

      @Override
      public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        showProgressDialog("加载中...", _context);

      }
    });

    String url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
        APIConstants.API_LOTTERY_DETAIL);
    _webview.loadUrl(url);

  }

  private void initData() {
    ZMRequestParams params = new ZMRequestParams();
    params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID, _user.Id);
    ZMHttpRequestMessage message = new ZMHttpRequestMessage(
        RequestType.SIGN_INFO, null, params, _handler,
        HTTP_RESPONSE_SIGN_INFO, MLMyServices.getInstance());
    loadDataWithMessage(_context, null, message);

  }

  @OnClick(R.id.btn_sign)
  public void signOnClick(View view) {

    ZMRequestParams params = new ZMRequestParams();
    params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID, _user.Id);
    params.addParameter("cityId", BaseApplication._currentCity);
    ZMHttpRequestMessage message = new ZMHttpRequestMessage(
        RequestType.SIGN, null, params, _handler, HTTP_RESPONSE_SIGN,
        MLMyServices.getInstance());
    loadDataWithMessage(_context, null, message);
  }

  @OnClick(R.id.btn_award)
  public void awardOnClick(View view) {
    _event.onEvent(null, MLConstants.MY_LOTTERY);
//		 toActivity(_context, MLConstants.MY_LOTTERY, null);	
  }


  /**
   * 商城
   */
  @OnClick(R.id.intrgral_ll_shop)
  public void shopOnClick(View view) {
    startAct(MLMyIntegralFrg.this, TXIntegralShopAty.class);
  }


  /**
   * 规则
   *
   * @description
   * @author marcello
   */
  @OnClick(R.id.money_btn_guize)
  public void gzOnClick(View view) {
//		_event.onEvent(null, MLConstants.MY_LOTTERY_DETAIL);
    startAct(MLMyIntegralFrg.this, MLIntegralAty.class);
  }

  private void review() {

    String imgUrl = APIConstants.API_IMAGE + "?id=" + _info.userPhoto;
    _headIv.setTag(imgUrl);
    if (!BaseApplication.IMAGE_CACHE.get(imgUrl, _headIv)) {
      //_headIv.setBackgroundResource(R.drawable.default_my_info_head);
    }
    _nameTv.setText(_info.depotName + "	" + getPhoneNumber());

    _balanceTv.setText(
        Html.fromHtml(String.format("积分:  <font color=\"#ff0000\">%s</font>", _info.sorce)));
    //String t = String.format("今日签到可领取" + _info.signVal + "积分");
    String t = "赚积分";
    _smTv.setText(t);
    String sign = BaseApplication.aCache.getAsString("SIGN");
    if (!MLToolUtil.isNull(sign) && sign.equalsIgnoreCase(getTimeDay())) {
      _smTv.setText("今日您已签到");
    }
  }

  private String getPhoneNumber() {
    String username = BaseApplication.aCache.getAsString(MLConstants.PARAM_REGISTER_USERNAME);
    return username;
  }
  /*
	 * private void requestCash(String money){ ZMRequestParams params = new
	 * ZMRequestParams();
	 * params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,_user.Id); // double
	 * m = Double.parseDouble(money); params.addParameter("money",money);
	 * ZMHttpRequestMessage message = new
	 * ZMHttpRequestMessage(RequestType.MY_DEAL_WITHDRAW, null, params,
	 * _handler, HTTP_RESPONSE_WITHDRAW, MLMyServices.getInstance());
	 * loadDataWithMessage(_context, null, message); }
	 */
	/*
	 * private void requestRecharge(String money){ ZMRequestParams params = new
	 * ZMRequestParams(); if(_user.isDepot){
	 * params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,_user.Id); }else{
	 * params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,_user.Id); } //
	 * double m = Double.parseDouble(money); params.addParameter("money",money);
	 * ZMHttpRequestMessage message = new
	 * ZMHttpRequestMessage(RequestType.MY_DEAL_RECHARGE, null, params,
	 * _handler, HTTP_RESPONSE_RECHARGE, MLMyServices.getInstance());
	 * loadDataWithMessage(_context, null, message); }
	 */

  private String getTimeDay() {
    Calendar ca = Calendar.getInstance();
    int year = ca.get(Calendar.YEAR);// 获取年份
    int month = ca.get(Calendar.MONTH);// 获取月份
    int day = ca.get(Calendar.DATE);// 获取日
    return String.format("%d%d%d", year, (month + 1), day);
  }

  private static final int HTTP_RESPONSE_SIGN = 0;
  private static final int HTTP_RESPONSE_SIGN_INFO = 1;
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

        case HTTP_RESPONSE_SIGN: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.datas) {
            initData();
            showMessageSuccess("签到成功!");
            BaseApplication.aCache.put("SIGN", getTimeDay());
            //	_signBtn.setText("已签到");
            _smTv.setText("今日您已签到");
          } else {
            showMessageError("今日已签到!");
          }
          break;
        }

        case HTTP_RESPONSE_SIGN_INFO: {
          MLIntegralResponse ret = (MLIntegralResponse) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            _info = ret.datas;
            review();
          } else {
          }
          break;
        }
        default:
          break;
      }
    }

  };

  /**
   * @description 返回
   * @author marcello
   */
  @OnClick(R.id.money_top_back)
  public void backOnClick(View view) {
    ((MLAuxiliaryActivity) _context).finish();
  }

  private IEvent<Object> _event;

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    _event = (IEvent<Object>) activity;
  }
}
