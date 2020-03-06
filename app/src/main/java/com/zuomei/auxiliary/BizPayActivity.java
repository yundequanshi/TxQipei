package com.zuomei.auxiliary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import cn.ml.base.utils.IEvent;
import cn.ml.base.utils.MLStrUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.txsh.R;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLAddDeal;
import com.zuomei.model.MLDepotPayInfoData;
import com.zuomei.model.MLDepotPayInfoResponse;
import com.zuomei.model.MLHomeBusiness1Data;
import com.zuomei.model.MLHomeBusinessDetail1;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLPayAlipayData;
import com.zuomei.model.MLRegister;
import com.zuomei.model.MLSpecialResonse;
import com.zuomei.services.MLHomeServices;
import com.zuomei.services.MLPayServices;
import com.zuomei.utils.MLPayUtils;
import com.zuomei.utils.MLToolUtil;
import com.zuomei.utils.MathUtils;
import com.zuomei.utils.alipay.PayResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BizPayActivity extends BaseActivity {

  @ViewInject(R.id.business_et_total)
  private EditText _totalEt;
  @ViewInject(R.id.detail_ck_wx)
  private CheckBox cbWx;
  @ViewInject(R.id.detail_ck_alipy)
  private CheckBox cbAli;
  @ViewInject(R.id.detail_ck_yue)
  private CheckBox cbYue;

  private IWXAPI api;
  private List<Integer> mRlPays;
  private MLPayAlipayData mlPayAlipayData;
  private MLDepotPayInfoData _payInfo;
  private double totalDouble = 0;

  public MLHomeBusiness1Data _business = new MLHomeBusiness1Data();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_biz_pay);
    ViewUtils.inject(this);
    if (getIntentData() != null) {
      _business = (MLHomeBusiness1Data) getIntentData();
    }
    initWeixin();
    initPayInfo();
  }

  private void initWeixin() {
    //将微信支付标记为 普通支付
    BaseApplication.aCache.put(MLConstants.ACACHE_PARAM_WX_SHOP, "");
    api = WXAPIFactory.createWXAPI(getAty(), APIConstants.APP_ID);
    mRlPays = new ArrayList();
    Collections.addAll(mRlPays, R.id.detail_tv_wx, R.id.detail_tv_alipay,
        R.id.detail_tv_yue);
  }

  // 获取汽修厂支付信息
  private void initPayInfo() {

    MLLogin user = ((BaseApplication) getApplication())
        .get_user();
    ZMRequestParams catalogParam = new ZMRequestParams();
    catalogParam.addParameter("depotId", user.Id);

    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(
        RequestType.DEPOT_PAY_INFO, null, catalogParam, _handler,
        HTTP_RESPONSE_DEPOT_INFO, MLPayServices.getInstance());
    loadData(getAty(), message1);
  }

  /**
   * 支付方式选择
   */
  @OnClick({R.id.detail_tv_wx, R.id.detail_tv_alipay, R.id.detail_tv_yue})
  public void payChooseOnClick(View view) {
    for (int id : mRlPays) {
      if (id == view.getId()) {
        ((CheckBox) ((LinearLayout) findViewById(id))
            .getChildAt(0)).setChecked(true);
      } else {
        ((CheckBox) ((LinearLayout) findViewById(id))
            .getChildAt(0)).setChecked(false);
      }
    }
  }

  /**
   * 支付
   *
   * @description
   * @author marcello
   */
  @OnClick(R.id.business_btn_pay)
  public void payOnClick(View view) {
    String _totalString = _totalEt.getText().toString();
    if (MLToolUtil.isNull(_totalString)) {
      showMessageWarning("请填写金额!");
      return;
    }
    totalDouble = Double.valueOf(_totalString);
    if (totalDouble == 0) {
      showMessageWarning("请填写正确的金额!");
      return;
    }
    if (cbWx.isChecked()) {// 微信支付
      payGetToken();
    } else if (cbAli.isChecked()) {// 支付宝
      getPayParamsAlipay();
    } else if (cbYue.isChecked()) {// 余额
      AlertDialog builder = new AlertDialog.Builder(getAty(),
          AlertDialog.THEME_HOLO_LIGHT)
          .setTitle("提示")
          .setMessage("确认使用余额支付？")
          .setNegativeButton("取消", null)
          .setPositiveButton("确定",
              new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog,
                    int which) {
                  payWithBalance();
                }
              }).show();

    }
  }


  /**
   * 支付金额判断
   *
   * @description
   * @author marcello
   */
  private double _finalTotal;
  // item选项
  CharSequence nameCs[];

  private void payWithBalance() {
    // 初始化
    is_use_hb = false;
    is_use_yue = false;

    use_hb = 0;
    use_yue = 0;
    use_wx = 0;

    if (_payInfo == null) {
      showMessage("支付初始化失败!");
    }

    if (totalDouble > _payInfo.money) {
      showMessage("余额不足");
      return;
    }
    requestPay();
  }

  boolean is_use_hb;
  boolean is_use_yue;

  private void compute(int which, boolean isChecked) {
    String item = nameCs[which].toString();
    if (item.contains("红包")) {
      is_use_hb = isChecked;
    }

    if (item.contains("余额支付")) {
      is_use_yue = isChecked;
    }
  }

  // 使用的金额
  double use_hb;
  double use_yue;
  double use_wx;

  private void payWithBalance2() {
    StringBuffer b = new StringBuffer();
    // 使用红包
    if (is_use_hb) {
      use_hb = _business.redEnvelopeMoney;
      b.append(String.format(
          "使用红包<font color=\"#D3252E\">%1$.2f</font>元", use_hb));
      _finalTotal = MathUtils.sub(_finalTotal, use_hb);
    } else {
      b.append("使用红包<font color=\"#D3252E\">0</font>元");
    }

    // 使用余额
    if (is_use_yue && _finalTotal > 0) {
      if (_finalTotal < _payInfo.money) {
        // 需要支付的金额小于 余额
        use_yue = _finalTotal;
      } else {
        use_yue = _payInfo.money;
      }

      b.append(String.format(
          "<br/>使用余额<font color=\"#D3252E\">%1$.2f</font>元", use_yue));
      _finalTotal = MathUtils.sub(_finalTotal, use_yue);
    } else {
      b.append("<br/>使用余额<font color=\"#D3252E\">0</font>元");
    }

    // 使用微信支付
    if (_finalTotal > 0) {
      use_wx = _finalTotal;
      b.append(String.format(
          "<br/><br/>需微信支付<font color=\"#D3252E\">%1$.2f</font>元",
          use_wx));
    }

    AlertDialog.Builder builder = new AlertDialog.Builder(getAty(),
        AlertDialog.THEME_HOLO_LIGHT);
    builder.setTitle("提示");
    builder.setMessage(Html.fromHtml(b.toString()));
    builder.setNegativeButton("确定", new OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {

        if (is_use_yue) {
          // 使用余额，输入支付密码
          inputPwd();
        } else {
          // 没有使用余额，不输入支付密码
          if (_finalTotal > 0) {
            payGetToken();
          } else {
            requestPay();
          }
        }
      }
    });
    builder.setPositiveButton("取消", null);
    builder.show();
  }

  private void payGetToken() {
    showProgressDialog("正在支付...", getAty());
    MLAddDeal deal = new MLAddDeal();
    deal.companyId = _business.userID;
    deal.money = _totalEt.getText().toString();
    deal.depotId = ((BaseApplication) getApplication())
        .get_user().Id;

    deal.use_hb = use_hb;
    deal.use_wx = use_wx;
    deal.use_yue = use_yue;

    BaseApplication._addDeal = deal;

    ZMRequestParams catalogParam = new ZMRequestParams();
    catalogParam.addParameter("appid", APIConstants.APP_ID);
    // 微信显示使用
    catalogParam.addParameter("userName", _business.userName);

    catalogParam.addParameter("depotId", ((BaseApplication) getApplication()).get_user().Id);
    catalogParam.addParameter("companyId", _business.userID);
    catalogParam.addParameter("allMoney", _totalEt.getText().toString());

    double _totalDouble = Double.valueOf(deal.money);
    int total_int = (int) (_totalDouble * 100);

    Map<String, Object> otherParmas = new HashMap<String, Object>();
    otherParmas.put("total_fee", String.valueOf(total_int));

    ZMHttpRequestMessage message = new ZMHttpRequestMessage(
        RequestType.WEIXIN_GET_TOKEN, null, catalogParam, _handler,
        HTTP_RESPONSE_PAY, MLPayServices.getInstance());
    message.setOtherParmas(otherParmas);
    loadDataWithMessage(getAty(), null, message);
  }

  /**
   * 弹出框 输入支付密码
   *
   * @description
   * @author marcello
   */
  private void inputPwd() {
    /*
     * MLMyPwdPop menuWindow = new MLMyPwdPop(getActivity(), new
		 * IEvent<String>() {
		 *
		 * @Override public void onEvent(Object source, String eventArg) {
		 * verifyPayPwd(eventArg); } }); menuWindow.showAtLocation(_root,
		 * Gravity.CENTER, 0, 0);
		 */
  }

  /**
   * 获取支付宝  支付参数
   */
  private void getPayParamsAlipay() {
    MLLogin user = ((BaseApplication) getApplication())
        .get_user();
    ZMRequestParams catalogParam = new ZMRequestParams();
    catalogParam.addParameter("depotId", user.Id);
    catalogParam.addParameter("companyId", _business.userID);
    catalogParam.addParameter("allMoney", _totalEt.getText().toString());
    catalogParam.addParameter("product_name", _business.userName);
    catalogParam.addParameter("product_body", _business.userName);
    catalogParam.addParameter("order_price", _totalEt.getText().toString());
    ZMHttpRequestMessage message = new ZMHttpRequestMessage(
        RequestType.ORDER_PAY_PARAM_ALIPAY, null, catalogParam, _handler,
        ORDER_PAY_PARAM_ALIPAY_RETURN, MLHomeServices.getInstance());
    loadDataWithMessage(getAty(), "正在加载，请稍等....", message);
  }

  /**
   * 支付宝支付
   */
  private void payForAlipay(String data) {
    MLPayUtils.payForAlipay(getAty(), data, mHandler);
  }

  // 余额支付
  private void requestPay() {
    // 生成交易单号
    MLPayServices.genOutTradNo();
    ZMRequestParams catalogParam = new ZMRequestParams();
    catalogParam.addParameter("depotId", ((BaseApplication) getApplication()).get_user().Id);
    catalogParam.addParameter("companyId", _business.userID);
    catalogParam.addParameter("billNO", BaseApplication.OutTradNo);
    catalogParam.addParameter("balance", _totalEt.getText().toString());
    catalogParam.addParameter("allMoney", _totalEt.getText().toString());

    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(
        RequestType.WEIXIN_PAY_ENCRYPT, null, catalogParam, _handler,
        HTTP_RESPONSE_PAY_1, MLPayServices.getInstance());
    loadDataWithMessage(getAty(), null, message1);
  }

  ///支付宝确认
  private void alipay() {
    MLLogin mUser = BaseApplication.getInstance().get_user();
    ZMRequestParams catalogParam = new ZMRequestParams();
    catalogParam.addParameter("out_trade_no", mlPayAlipayData.out_trade_no);
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(
        RequestType.ORDER_PAY_ALIPAY_RIGHT, null, catalogParam, _handler,
        ORDER_PAY_ALIPAY_RIGHT_RETURN, MLHomeServices.getInstance());
    loadDataWithMessage(getAty(), null, message1);

  }

  private static final int HTTP_RESPONSE_PAY = 4;
  private static final int HTTP_RESPONSE_DEPOT_INFO = 5;
  private static final int HTTP_RESPONSE_PAY_1 = 6;
  private static final int HTTP_RESPONSE_JUDGEPWD = 7;
  private static final int HTTP_RESPONSE_VERIFYPWDPWD = 8;
  private static final int ORDER_PAY_PARAM_ALIPAY_RETURN = 10;
  private static final int ORDER_PAY_ALIPAY_RIGHT_RETURN = 11;
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
        case HTTP_RESPONSE_PAY: {

          PayReq ret = (PayReq) msg.obj;
          // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
          // boolean b = api.registerApp("com.zuomei");
          boolean b = api.registerApp(APIConstants.APP_ID);
          api.sendReq(ret);
          break;
        }

        // 获取汽修厂支付信息
        case HTTP_RESPONSE_DEPOT_INFO: {
          MLDepotPayInfoResponse ret = (MLDepotPayInfoResponse) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            _payInfo = ret.datas;
          } else {

          }
          break;
        }
        case HTTP_RESPONSE_PAY_1: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            _totalEt.setText("");
            showMessageSuccess("支付成功！");
          } else {
            showMessageError("支付失败!");
          }
          break;
        }
        // 判断是否设置支付密码
        case HTTP_RESPONSE_JUDGEPWD: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1") && ret.datas) {
            payWithBalance2();
          } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                getAty(), AlertDialog.THEME_HOLO_LIGHT);
            builder.setTitle("提示");
            builder.setMessage("请先设置钱包密码");
            builder.setNegativeButton("确定",
                new OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog,
                      int which) {
                    toActivity(BizPayActivity.this, MLConstants.MY_PAY_PWD, null);
                  }
                });
            builder.setPositiveButton("取消", null);
            builder.show();
          }
          break;
        }

        case HTTP_RESPONSE_VERIFYPWDPWD: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1") && ret.datas) {
            if (_finalTotal > 0) {
              payGetToken();
            } else {
              requestPay();
            }

          } else {
            showMessageError("钱包密码不正确!");
            inputPwd();
          }
          break;
        }
        case ORDER_PAY_ALIPAY_RIGHT_RETURN: {
          MLSpecialResonse mlSpecialResonse = (MLSpecialResonse) msg.obj;
          if (MLStrUtil.compare(mlSpecialResonse.datas, "ok")) {
            showMessage("支付成功!");
          } else {
            showMessage("支付失败!！！");
          }
          break;
        }
        case ORDER_PAY_PARAM_ALIPAY_RETURN:
          mlPayAlipayData = (MLPayAlipayData) msg.obj;
          payForAlipay(mlPayAlipayData.payInfo);
          break;

        default:
          break;
      }
    }
  };

  private static final int SDK_PAY_FLAG = 1;

  private static final int SDK_CHECK_FLAG = 2;
  private Handler mHandler = new Handler() {
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case SDK_PAY_FLAG: {
          PayResult payResult = new PayResult((String) msg.obj);
          // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
          String resultStatus = payResult.getResultStatus();
          // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
          if (TextUtils.equals(resultStatus, "9000")) {
            alipay();
          } else {
            // 判断resultStatus 为非“9000”则代表可能支付失败
            // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
            if (TextUtils.equals(resultStatus, "8000")) {
              showMessage("支付结果确认中!");
            } else {
              // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
              showMessage("支付失败!");
            }
          }
          break;
        }
        case SDK_CHECK_FLAG: {
          showMessage("检查结果为：" + msg.obj);
          break;
        }
        default:
          break;
      }
    }

  };


  public void back(View view) {
    finish();
  }
}
