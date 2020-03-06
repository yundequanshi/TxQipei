package com.txsh.services;

import com.lidroid.xutils.exception.HttpException;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.txsh.model.TXIntegralShopConvertRes;
import com.txsh.model.TXIntegralShopDetailRes;
import com.txsh.model.TXIntegralShopRes;
import com.txsh.model.TXOrderDetailRes;
import com.txsh.model.TXShopAddressRes;
import com.txsh.model.TXShopBuyRes;
import com.txsh.model.TXShopCarData;
import com.txsh.model.TXShopDetailRes;
import com.txsh.model.TXShopListRes;
import com.txsh.model.TXShopLogisticalRes;
import com.txsh.model.TXShopOrderRes;
import com.txsh.model.TXShopPjData;
import com.txsh.model.TXShopPlayListRes;
import com.txsh.model.TXShopTypeListRes;
import com.txsh.model.TXShopYueCheckRes;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseHttpService;
import com.zuomei.constants.MLConstants;
import com.zuomei.exception.ZMHttpException;
import com.zuomei.exception.ZMParserException;
import com.zuomei.http.IWebService;
import com.zuomei.http.ZMHttpParam;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMHttpUrl;
import com.zuomei.http.ZMParserResponse;
import com.zuomei.model.MLBaseResponse;
import com.zuomei.model.MLLoginFail;
import com.zuomei.model.MLPayAlipayData;
import com.zuomei.model.MLRegister;
import com.zuomei.model.TXShopSubmitCar;
import com.zuomei.utils.ZMJsonParser;
import com.zuomei.weixin.pay.EncryptUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MLShopServices extends BaseHttpService implements IWebService {

  public String home_cache = null;

  public static MLShopServices INSTANCE = new MLShopServices();

  public static MLShopServices getInstance() {
    return INSTANCE;
  }

  @Override
  public Object httpPost(ZMHttpRequestMessage httpMessage)
      throws ZMParserException, ZMHttpException {
    switch (httpMessage.getHttpType()) {

      case TX_INTERGRAL_SHOP: {
        //积分详情
        return getResonseData(TXIntegralShopRes.class, httpMessage);
      }
      case TX_INTERGRAL_SHOP_DETAIL: {
        //积分商城 详情
        return getResonseData(TXIntegralShopDetailRes.class, httpMessage);
      }

      case TX_INTERGRAL_SHOP_BUY: {
        //积分商城 购买
        return getResonseEncry(MLRegister.class, httpMessage);
      }

      case TX_INTERGRAL_SHOP_CONVERT: {
        //积分商城 兑换记录
        return getResonseData(TXIntegralShopConvertRes.class, httpMessage);
      }

      case TX_SHOP_PRODUCT_LIST: {
        //商城列表
        return getResonseData(TXShopListRes.class, httpMessage);
      }

      case FIND_PRODUCT_BY_KEY: {
        //商城列表
        return getResonseData(TXShopListRes.class, httpMessage);
      }

      case TX_SHOP_PRODUCT_BY_TYPE: {
        //商城列表
        return getResonseData(TXShopListRes.class, httpMessage);
      }

      case TX_SHOP_PRODUCT_DETAIL: {
        //商品详情
        return getResonseData(TXShopDetailRes.class, httpMessage);
      }
      case TX_SHOP_CAR_ADD: {
        //加入购物车
        return getResonseData(MLRegister.class, httpMessage);
      }
      case TX_SHOP_ADDRESS_MY: {
        //我的收获地址
        return getResonseData(TXShopAddressRes.class, httpMessage);
      }

      case TX_SHOP_ADDRESS_ADD: {
        //增加我的收货地址
        return getResonseData(MLRegister.class, httpMessage);
      }

      case TX_SHOP_ADDRESS_UPDATE: {
        //修改我的收货地址
        return getResonseData(MLRegister.class, httpMessage);
      }

      case TX_SHOP_ADDRESS_DEL: {
        //删除我的收货地址
        return getResonseData(MLRegister.class, httpMessage);
      }

      case TX_SHOP_ADDRESS_SET: {
        //商城模块-设置默认收货地址
        return getResonseData(MLRegister.class, httpMessage);
      }

      case TX_SHOP_PRODUCT_BUY: {
        //95商城模块-立即购买
        return getResonseData(TXShopBuyRes.class, httpMessage);
      }

      case SHOPCAR: {
        //商城模块-设置默认收货地址
        return getResonseData(TXShopCarData.class, httpMessage);
      }

      case EDITSHOPCAR: {
        //商城模块-设置默认收货地址
        return getResonseData(MLRegister.class, httpMessage);
      }
      case DELSHOPCAR: {
        //商城模块-设置默认收货地址
        return getResonseData(MLRegister.class, httpMessage);
      }

      case TX_SHOP_PRODUCT_SUBMIT: {
        //98商城模块-提交商品订单
        return getResonseData(MLLoginFail.class, httpMessage);
      }

      case TX_SHOP_PRODUCT_PAY_WX: {
        //商城模块-微信支付
        return getWXParams(httpMessage);
      }
      case TX_SHOP_PRODUCT_PAY_ALIPAY: {
        //商城模块-支付宝支付
        return getAlipayParams(httpMessage);
      }

      case TX_SHOP_PRODUCT_PAY_YUE: {
        //商城模块-余额支付
        return getResonseData(MLRegister.class, httpMessage);
      }

      case TX_PAY_ALIPAY_AFFIRM: {
        //112、商城模块-支付宝支付确认接口
        return getResonseData(MLLoginFail.class, httpMessage);
      }

      case SHOPCARBUY: {
        //97 购物车 购买
        return getResonseData(TXShopBuyRes.class, httpMessage);
      }

      case TX_SHOP_ORDER_DFK: {
        //待付款
        return getResonseData(TXShopOrderRes.class, httpMessage);

      }

      case TX_SHOP_ORDER_DSH: {
        //待收货
        return getResonseData(TXShopOrderRes.class, httpMessage);
      }

      case TX_SHOP_ORDER_YSH: {
        //已收货
        return getResonseData(TXShopOrderRes.class, httpMessage);
      }

      case TX_PAY_CANCEL: {
        //取消订单
        return getResonseData(MLRegister.class, httpMessage);
      }

      case TX_PAY_AFFIRM: {
        //确认收货
        return getResonseData(MLRegister.class, httpMessage);
      }

      case TX_PAY_QUIT: {
        //申请退货
        return getResonseData(MLRegister.class, httpMessage);
      }

      case TX_PAY_YUE_CHECEK: {
        //商城模块-余额支付订单检查
        return getResonseData(TXShopYueCheckRes.class, httpMessage);
      }
      case ORDERDETAIL: {
        //订单详情
        return getResonseData(TXOrderDetailRes.class, httpMessage);
      }
      case SHOPPRODUCTPJ: {
        //商品评价
        return getResonseData(TXShopPjData.class, httpMessage);
      }
      case SHOPPRODUCTLIST: {
        //商家商品列表
        return getResonseData(TXShopListRes.class, httpMessage);
      }
      case SHOPPINGJIACHAT: {
        //商品评价
        return getResonseData(MLRegister.class, httpMessage);
      }

      case TX_SHOP_ORDER_LOGISTICAL: {
        //117、商城模块-物流信息
        return getResonseData(TXShopLogisticalRes.class, httpMessage);
      }
      case SHOPPRODUCTFABU: {
        return getResonseData(MLRegister.class, httpMessage);
      }

      //商城模块-提交购物车订单
      case TX_SHOP_PRODUCT_SUBMIT_CAR: {
        return getResonseData(TXShopSubmitCar.class, httpMessage);
      }

      case TX_SHOP_PRODUCT_SALE: {
        //商城模块-特价商品
        return getResonseData(TXShopListRes.class, httpMessage);
      }

      case TX_SHOP_PRODUCT_HOT: {
        //商城模块-热卖商品
        return getResonseData(TXShopListRes.class, httpMessage);
      }

      case TX_SHOP_PLAY_DATA: {
        return getResonseData(TXShopPlayListRes.class, httpMessage);
      }
      case TX_SHOP_FIND_HOMEGOODS_TYPE: {
        return getResonseData(TXShopTypeListRes.class, httpMessage);
      }
      case TX_SHOP_FIND_HOMEGOODS_ALL_TYPE: {
        return getResonseData(TXShopTypeListRes.class, httpMessage);
      }
      default:
        break;
    }
    return null;
  }


  /**
   * 微信支付
   */
  private Object getWXParams(ZMHttpRequestMessage httpMessage)
      throws ZMParserException, ZMHttpException {
    String url = ZMHttpUrl.getUrl(httpMessage.getHttpType(),
        httpMessage.getUrlParamList());
    String ret = post(url, httpMessage);

    try {
      JSONObject json = new JSONObject(ret);
      json = json.getJSONObject("datas");
      //  	System.out.println(json.toString());
      if (null != json) {
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
    } catch (Exception e) {
      return null;
    }

    return null;
  }

  /**
   * 支付宝支付
   */
  private Object getAlipayParams(ZMHttpRequestMessage httpMessage)
      throws ZMParserException, ZMHttpException {
    String url = ZMHttpUrl.getUrl(httpMessage.getHttpType(),
        httpMessage.getUrlParamList());

    String ret = post(url, httpMessage);
    JSONObject json = null;
    try {
      json = new JSONObject(ret);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    MLPayAlipayData base1 = null;
    try {
      base1 = ZMParserResponse
          .parserResponse(ZMHttpType.ResponseType.JSON, json.getJSONObject("datas").toString(),
              MLPayAlipayData.class);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return base1;
  }


  private <T extends MLBaseResponse> Object getResonseData(Class<T> cls,
      ZMHttpRequestMessage httpMessage) throws ZMParserException, ZMHttpException {
    T ret = post(httpMessage, cls);
    return ret;
  }

  /**
   * 解密加密操作
   *
   * @description
   * @author marcello
   */
  private <T extends MLBaseResponse> Object getResonseEncry(Class<T> cls,
      ZMHttpRequestMessage httpMessage) throws ZMParserException, ZMHttpException {
    JSONObject obj = new JSONObject();
    for (ZMHttpParam param : httpMessage.getPostParamList()) {
      try {
        obj.put(param.getParamName() + "", param.getParamValue() + "");
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    String content1 = "";
    //加密
    try {
      content1 = EncryptUtils.encode(obj.toString(), MLConstants.CARSHOP_HTTP_TAG);
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
}
