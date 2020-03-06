package com.zuomei.services;

import com.lidroid.xutils.exception.HttpException;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseHttpService;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.exception.ZMHttpException;
import com.zuomei.exception.ZMParserException;
import com.zuomei.http.IWebService;
import com.zuomei.http.ZMHttpParam;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpUrl;
import com.zuomei.model.MLBaseResponse;
import com.zuomei.model.MLDepotPayInfoResponse;
import com.zuomei.model.MLRegister;
import com.zuomei.model.MLSpecialResonse;
import com.zuomei.utils.MD5;
import com.zuomei.utils.MLToolUtil;
import com.zuomei.utils.ZMJsonParser;
import com.zuomei.weixin.pay.EncryptUtils;
import com.zuomei.weixin.pay.Util;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MLPayServices extends BaseHttpService implements IWebService {

  public String home_cache = null;

  public static MLPayServices INSTANCE = new MLPayServices();

  public static MLPayServices getInstance() {
    return INSTANCE;
  }

  @Override
  public Object httpPost(ZMHttpRequestMessage httpMessage)
      throws ZMParserException, ZMHttpException {
    switch (httpMessage.getHttpType()) {
      case WEIXIN_GET_TOKEN: {
        return getToken(httpMessage);
      }
      //获取短信验证码
      case MY_PAY_GETCODE: {
        return EncodeData(httpMessage, MLSpecialResonse.class);
      }
      //设置支付密码
      case MY_PAY_SETPWD: {
        return EncodeData(httpMessage, MLRegister.class);
      }
      //判断是否设置支付密码
      case MY_PAY_JUDGEPWD: {
        return EncodeData(httpMessage, MLRegister.class);
      }
      //验证支付密码
      case MY_PAY_VERIFYPWD: {
        return EncodeData(httpMessage, MLRegister.class);
      }

      //支付确认
      case WEIXIN_PAY_CONFIRM: {
        return getResonseData(MLSpecialResonse.class, httpMessage);
      }
      case WEIXIN_PAY_CONFIRM_SHOP: {
        return getResonseData(MLSpecialResonse.class, httpMessage);
      }

      case DEPOT_PAY_INFO: {
        //获取商家支付信息
        return getResonseData(MLDepotPayInfoResponse.class, httpMessage);
      }

      case WEIXIN_PAY: {
        //添加交易(加密)
        return getResonseData(MLRegister.class, httpMessage);
      }

      case WEIXIN_PAY_ENCRYPT: {
        //添加交易(加密)
        JSONObject obj = new JSONObject();
        for (ZMHttpParam param : httpMessage.getPostParamList()) {
          try {
            obj.put(param.getParamName() + "", param.getParamValue() + "");
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }

        String content1 = "";
        //加密
        try {
          content1 = EncryptUtils.encode(obj.toString(), MLConstants.CARSHOP_HTTP_TAG);
        } catch (Exception e) {
          e.printStackTrace();
        }
        //获取URL
        String url = ZMHttpUrl.getUrl(httpMessage.getHttpType(),
            httpMessage.getUrlParamList());
        //==========
        MLRegister rs2 = null;

        try {
          String rs = post(url, content1);
          String rs1 = EncryptUtils.decode(rs, MLConstants.CARSHOP_HTTP_TAG);
          return ZMJsonParser.fromJsonString(MLRegister.class, rs1);
        } catch (HttpException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        return null;
      }
      default:
        break;
    }
    return null;
  }

  private long timeStamp;
  private String nonceStr, packageValue;

  private Object getToken(ZMHttpRequestMessage httpMessage) throws ZMParserException {

    String total_fee = (String) httpMessage.getOtherParmas("total_fee");
    String _name = (String) httpMessage.getPostParams("userName");

    String token_url = String.format("%s%s?order_price=%s&product_name=%s&depotId=%s&companyId=%s",
        APIConstants.API_DEFAULT_HOST,
        APIConstants.API_WEIXIN_TOKEN_SERVERS,
        total_fee,
        _name,
        httpMessage.getPostParams("depotId"),
        httpMessage.getPostParams("companyId")
    );

    StringBuffer buffer = new StringBuffer();
    String isUsedRed = httpMessage.getPostParams("isUsedRed");
    String redMoney = httpMessage.getPostParams("redMoney");
    String allMoney = httpMessage.getPostParams("allMoney");
    String balance = httpMessage.getPostParams("balance");

    if (!MLToolUtil.isNull(isUsedRed)) {
      //	buffer.append("&isUsedRed="+isUsedRed);
    }
    if (!MLToolUtil.isNull(redMoney)) {
      //	buffer.append("&redMoney="+redMoney);
    }
    if (!MLToolUtil.isNull(allMoney)) {
      buffer.append("&allMoney=" + allMoney);
    }
    if (!MLToolUtil.isNull(balance)) {
      buffer.append("&balance=" + balance);
    }

    //ip
    buffer.append("&ip=" + cn.ml.base.utils.MLToolUtil.getLocalIpAddress());

    if (!MLToolUtil.isNull(buffer.toString())) {
      token_url = token_url + buffer.toString();
    }

    try {
      byte[] buf = Util.httpGet(token_url);
      if (buf != null && buf.length > 0) {
        String content = new String(buf);
        JSONObject json = new JSONObject(content);
        //  	System.out.println(json.toString());
        if (null != json && 0 == json.getInt("retcode")) {
          PayReq req = new PayReq();
          //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
          req.appId = json.getString("appid");
          req.partnerId = json.getString("partnerid");
          req.prepayId = json.getString("prepayid");
          req.nonceStr = json.getString("noncestr");
          req.timeStamp = json.getString("timestamp");
          req.packageValue = json.getString("package");
          req.sign = json.getString("sign");
          req.extData = "app data";

          //订单号
          BaseApplication.out_trade_no = json.getString("out_trade_no");
          return req;
        }
      }
    } catch (Exception e) {
      // TODO: handle exception
      return null;
    }

    return null;

    /**
     *
     * 微信端支付
     *
     //1.获取token
     String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
     APIConstants.APP_ID,	APIConstants.AppSecret);
     MLPayTokenData data = new MLPayTokenData();
     byte[] buf = Util.httpGet(url);
     if (buf == null || buf.length == 0) {
     data.localRetCode = LocalRetCode.ERR_HTTP;
     return null;
     }
     String content = new String(buf);
     data = ZMJsonParser.fromJsonString(MLPayTokenData.class, content);
     if(MLToolUtil.isNull(data.access_token)){
     return null;
     }
     //需要判断 成功
     MLPayTokenData data1 = new MLPayTokenData();
     String url1 = String.format("https://api.weixin.qq.com/pay/genprepay?access_token=%s", data.access_token);
     String entity = genProductArgs(total_fee);
     byte[] buf1 = Util.httpPost(url1, entity);
     if (buf1 == null || buf1.length == 0) {
     data1.localRetCode = LocalRetCode.ERR_HTTP;
     return null;
     }
     //需要判断 成功
     String content1 = new String(buf1);
     data1 = ZMJsonParser.fromJsonString(MLPayTokenData.class, content1);
     if(MLToolUtil.isNull(data1.prepayid)){
     return null;
     }

     PayReq req = new PayReq();
     req.appId =APIConstants.APP_ID;
     req.partnerId = APIConstants.PARTNER_ID;
     req.prepayId = data1.prepayid;
     req.nonceStr = nonceStr;
     req.timeStamp = String.valueOf(timeStamp);
     req.packageValue = "Sign=" + packageValue;

     List<NameValuePair> signParams = new LinkedList<NameValuePair>();
     signParams.add(new BasicNameValuePair("appid", req.appId));
     signParams.add(new BasicNameValuePair("appkey", APIConstants.AppKEY));
     signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
     signParams.add(new BasicNameValuePair("package", req.packageValue));
     signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
     signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
     signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
     req.sign = genSign(signParams);

     return req;
     */
  }


  private String genProductArgs(String total) {
    JSONObject json = new JSONObject();

    try {
      json.put("appid", APIConstants.APP_ID);
      String traceId = getTraceId();  // traceId 由开发者自定义，可用于订单的查询与跟踪，建议根据支付用户信息生成此id
      json.put("traceid", traceId);
      nonceStr = genNonceStr();
      json.put("noncestr", nonceStr);

      List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
      packageParams.add(new BasicNameValuePair("bank_type", "WX"));
      packageParams.add(new BasicNameValuePair("body", MLConstants.PAY_PARAM_BODY));
      packageParams.add(new BasicNameValuePair("fee_type", "1"));
      packageParams.add(new BasicNameValuePair("input_charset", "UTF-8"));
      packageParams.add(new BasicNameValuePair("notify_url", "http://weixin.qq.com"));
      packageParams.add(new BasicNameValuePair("out_trade_no", genOutTradNo()));
      packageParams.add(new BasicNameValuePair("partner", APIConstants.PARTNER_ID));
      packageParams.add(new BasicNameValuePair("spbill_create_ip", "196.168.1.1"));
      packageParams.add(new BasicNameValuePair("total_fee", total));
      packageValue = genPackage(packageParams);

      json.put("package", packageValue);
      timeStamp = genTimeStamp();
      json.put("timestamp", timeStamp);

      List<NameValuePair> signParams = new LinkedList<NameValuePair>();
      signParams.add(new BasicNameValuePair("appid", APIConstants.APP_ID));
      //	signParams.add(new BasicNameValuePair("appkey",APIConstants.AppKEY));
      signParams.add(new BasicNameValuePair("noncestr", nonceStr));
      signParams.add(new BasicNameValuePair("package", packageValue));
      signParams.add(new BasicNameValuePair("timestamp", String.valueOf(timeStamp)));
      signParams.add(new BasicNameValuePair("traceid", traceId));
      json.put("app_signature", genSign(signParams));

      json.put("sign_method", "sha1");
    } catch (Exception e) {
      //Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
      return null;
    }

    return json.toString();
  }

  /**
   * 建议 traceid 字段包含用户信息及订单信息，方便后续对订单状态的查询和跟踪
   */
  private String getTraceId() {
    return "crestxu_" + genTimeStamp();
  }

  private long genTimeStamp() {
    return System.currentTimeMillis() / 1000;
  }

  private String genNonceStr() {
    Random random = new Random();
    return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
  }

  /**
   * 注意：商户系统内部的订单号,32个字符内、可包含字母,确保在商户系统唯一
   */
  public static String genOutTradNo() {

    Date date = new Date();
    String param1 = dateFormater2.get().format(date);
    double i = Math.random() * 890000000 + 100000000;
    int param2 = (int) i;
    String OutTradNo = String.format("JY%s%d", param1, param2);

    //保存订单号
    BaseApplication.OutTradNo = OutTradNo;
    return OutTradNo;
  }

  private String genPackage(List<NameValuePair> params) {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < params.size(); i++) {
      sb.append(params.get(i).getName());
      sb.append('=');
      sb.append(params.get(i).getValue());
      sb.append('&');
    }
    sb.append("key=");
    //	sb.append(APIConstants.PARTNER_KEY); // 注意：不能hardcode在客户端，建议genPackage这个过程都由服务器端完成

    // 进行md5摘要前，params内容为原始内容，未经过url encode处理
    String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();

    return URLEncodedUtils.format(params, "utf-8") + "&sign=" + packageSign;
  }

  private String genSign(List<NameValuePair> params) {
    StringBuilder sb = new StringBuilder();

    int i = 0;
    for (; i < params.size() - 1; i++) {
      sb.append(params.get(i).getName());
      sb.append('=');
      sb.append(params.get(i).getValue());
      sb.append('&');
    }
    sb.append(params.get(i).getName());
    sb.append('=');
    sb.append(params.get(i).getValue());

    String sha1 = Util.sha1(sb.toString());
    //	Log.d(TAG, "genSign, sha1 = " + sha1);
    return sha1;
  }


  private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
    @Override
    protected SimpleDateFormat initialValue() {
      return new SimpleDateFormat("yyyyMMdd");
    }
  };

  private <T> Object EncodeData(ZMHttpRequestMessage httpMessage, Class<T> cls) {

    String data = httpMessage.getPostParams("data");
    String content1 = "";
    //加密
    try {
      content1 = EncryptUtils.encode(data, MLConstants.CARSHOP_HTTP_TAG);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    //获取URL
    String url = ZMHttpUrl.getUrl(httpMessage.getHttpType(),
        httpMessage.getUrlParamList());
    //==========
    try {
      String rs = post(url, content1);
      String rs1 = EncryptUtils.decode(rs, MLConstants.CARSHOP_HTTP_TAG);
      return ZMJsonParser.fromJsonString(cls, rs1);
    } catch (HttpException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }


  private <T extends MLBaseResponse> Object getResonseData(Class<T> cls,
      ZMHttpRequestMessage httpMessage) throws ZMParserException, ZMHttpException {
    T ret = post(httpMessage, cls);
    return ret;
  }

  public static enum LocalRetCode {
    ERR_OK, ERR_HTTP, ERR_JSON, ERR_OTHER
  }
}
