package com.zuomei.services;

import com.lidroid.xutils.exception.HttpException;
import com.txsh.model.TXShopSumRes;
import com.zuomei.base.BaseHttpService;
import com.zuomei.constants.MLConstants;
import com.zuomei.exception.ZMHttpException;
import com.zuomei.exception.ZMParserException;
import com.zuomei.http.IWebService;
import com.zuomei.http.ZMHttpParam;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpUrl;
import com.zuomei.model.MLAccidentListResponse;
import com.zuomei.model.MLBaseResponse;
import com.zuomei.model.MLBill2DetailResponse;
import com.zuomei.model.MLBill2ListResponse;
import com.zuomei.model.MLCommentResponse;
import com.zuomei.model.MLDealListResponse;
import com.zuomei.model.MLDialDetailResponse;
import com.zuomei.model.MLDialResponse;
import com.zuomei.model.MLHomeBusinessList;
import com.zuomei.model.MLIntegralResponse;
import com.zuomei.model.MLLoginFail;
import com.zuomei.model.MLLotteryRecordResponse;
import com.zuomei.model.MLLotteryResponse;
import com.zuomei.model.MLMyBankResponse;
import com.zuomei.model.MLMyContactResponse;
import com.zuomei.model.MLMyInfoResponse;
import com.zuomei.model.MLMyPacketResponse;
import com.zuomei.model.MLMyRebateResponse;
import com.zuomei.model.MLMyRepairResponse;
import com.zuomei.model.MLMyShareData;
import com.zuomei.model.MLMySpecialResponse;
import com.zuomei.model.MLMyStockResponse;
import com.zuomei.model.MLMyTxListResponse;
import com.zuomei.model.MLMyUserListResponse;
import com.zuomei.model.MLRegister;
import com.zuomei.model.MLSpecialResonse;
import com.zuomei.model.MLSpecialResonse2;
import com.zuomei.model.MLVersionApp;
import com.zuomei.model.QPIntegralRes;
import com.zuomei.utils.ZMJsonParser;
import com.zuomei.weixin.pay.EncryptUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MLMyServices extends BaseHttpService implements IWebService {

  public String home_cache = null;

  public static MLMyServices INSTANCE = new MLMyServices();

  public static MLMyServices getInstance() {
    return INSTANCE;
  }

  @Override
  public Object httpPost(ZMHttpRequestMessage httpMessage)
      throws ZMParserException, ZMHttpException {
    switch (httpMessage.getHttpType()) {
      case MY_STOCK_ADD: {
        //添加进货
        return getResonseData(MLRegister.class, httpMessage);
      }
      case MY_STOCK_LIST: {
        //获取进货列表
        return getResonseData(MLMyStockResponse.class, httpMessage);
      }
      case MY_REPAIR_LIST: {
        //获取汽修列表
        return getResonseData(MLMyRepairResponse.class, httpMessage);
      }
      case MY_REPAIR_ADD: {
        //添加汽修记录
        return getResonseData(MLRegister.class, httpMessage);
      }
      case MY_INFO_D: {
        //获取商家基本信息
        return getResonseData(MLMyInfoResponse.class, httpMessage);
      }
      case MY_INFO_HEAD: {
        //修改头像
        return getResonseData(MLRegister.class, httpMessage);
      }
      case MY_INFO_ADDRESS: {
        //修改详细地址
        return getResonseData(MLRegister.class, httpMessage);
      }
      case MY_INFO_DEPOTNAME: {
        //修改名称
        return getResonseData(MLRegister.class, httpMessage);
      }
      case MY_INFO_REALNAME: {
        //修改姓名
        return getResonseData(MLRegister.class, httpMessage);
      }
      case MY_UPDATE_DEPOT: {
        //修改姓名
        return getResonseData(MLRegister.class, httpMessage);
      }
      case UPDATE_COMPANY_DECLARATION: {
        //修改企业宣言
        return getResonseData(MLRegister.class, httpMessage);
      }
      case UPDATE_CONCURREN_OPERATE: {
        //修改具体
        return getResonseData(MLRegister.class, httpMessage);
      }
      case UPDATE_COMPANY_PHONE: {
        //修改商家手机号
        return getResonseData(MLRegister.class, httpMessage);
      }
      case MY_INFO_LOCATION: {
        //修改地区
        return getResonseData(MLRegister.class, httpMessage);
      }
      case MY_INFO_ALIPAY: {
        //修改支付宝
        return getResonseData(MLRegister.class, httpMessage);
      }
      case MY_INFO_PHONE: {
        //修改电话
        return getResonseData(MLRegister.class, httpMessage);
      }
      case MY_SPECIAL_DETAIL: {
        //获取优惠详情
        return getResonseData(MLSpecialResonse.class, httpMessage);
      }
      case MY_SPECIAL_LIST: {
        //获取优惠列表
        return getResonseData(MLMySpecialResponse.class, httpMessage);
      }
      case MY_CONTACT: {
        //获取联系我们
        return getResonseData(MLMyContactResponse.class, httpMessage);
      }
      case MY_COLLECT: {
        //获取收藏列表
        return getResonseData(MLHomeBusinessList.class, httpMessage);
      }
      case MY_STOCK_DEL: {
        //删除进货记录
        return getResonseData(MLRegister.class, httpMessage);
      }
      case MY_REPAIR_DEL: {
        //删除汽修记录
        return getResonseData(MLRegister.class, httpMessage);
      }
      case MY_PRODUCT_ADD: {
        //产品发布
        return getResonseData(MLRegister.class, httpMessage);
      }
      case MY_PRODUCT_DEL: {
        //产品删除
        return getResonseData(MLRegister.class, httpMessage);
      }
      case MY_USER_COUNT: {
        //用户数量
        return getResonseData(MLSpecialResonse.class, httpMessage);
      }
      case MY_USER_LIST: {
        //用户量信息
        return getResonseData(MLMyUserListResponse.class, httpMessage);
      }
      case MY_DIAL_LIST: {
        //通话记录
        return getResonseData(MLDialResponse.class, httpMessage);
      }
      case MY_DIAL_LIST2: {
        //通话记录（时间段）
        return getResonseData(MLDialResponse.class, httpMessage);
      }
      case MY_DIAL_DETAIL: {
        //通话记录详情
        return getResonseData(MLDialDetailResponse.class, httpMessage);
      }
      case MY_DIAL_DETAIL2: {
        //通话记录详情(时间段)
        return getResonseData(MLDialDetailResponse.class, httpMessage);
      }

      case MY_DIAL_COUNT: {
        //剩余分钟数
        return getResonseData(MLSpecialResonse2.class, httpMessage);
      }
      case MY_DIAL_TODAY: {
        //今日拨打次数
        return getResonseData(MLSpecialResonse.class, httpMessage);
      }
      case MY_DIAL_ALL: {
        //总拨打次数
        return getResonseData(MLSpecialResonse.class, httpMessage);
      }
      case MY_DIAL_DEL: {
        //删除通话记录
        return getResonseData(MLRegister.class, httpMessage);
      }
      case MY_DIAL_DETAIL_DEL: {
        //删除通话详情记录
        return getResonseData(MLRegister.class, httpMessage);
      }
      case MY_UPDATE_PWD: {
        //修改密码
        return getResonseData(MLRegister.class, httpMessage);
      }
      case MY_DEAL_LIST: {
        //交易记录
        return getResonseData(MLDealListResponse.class, httpMessage);
      }
      case MY_WITHDRAW_LIST: {
        //提现列表
        return getResonseData(MLMyTxListResponse.class, httpMessage);
      }
      case MY_DEAL_RECHARGE: {
        //提现
        return getResonseEncry(MLSpecialResonse.class, httpMessage);
      }
      case MY_DEAL_WITHDRAW: {

        //充值
        return getResonseEncry(MLLoginFail.class, httpMessage);
      }
      case MY_REBATE_LIST: {
        //汽修厂-返利
        return getResonseData(MLMyRebateResponse.class, httpMessage);
      }
      case MY_DEAL_COMMENT: {
        //交易评价
        return getResonseData(MLRegister.class, httpMessage);
      }
      case MY_DEAL_REFUND: {
        //交易退款
        return getResonseEncry(MLRegister.class, httpMessage);
      }
      case MY_BIND: {
        //绑定财付通
        return getResonseData(MLRegister.class, httpMessage);
      }
      case HOME_COMMENT: {
        //评论列表
        return getResonseData(MLCommentResponse.class, httpMessage);
      }
      case MY_INFO_B: {
        //获取商家基本信息
        return getResonseData(MLMyInfoResponse.class, httpMessage);
      }

      case CHECK_APPVERSION_UPDATE: {
        //获取商家基本信息
        return getResonseData(MLVersionApp.class, httpMessage);
      }

      //====二期==================================
      case MY_ACCIDENT_LIST: {
        //我的事故车列表
        return getResonseData(MLAccidentListResponse.class, httpMessage);
      }
      case MY_ACCIDENT_DEL: {
        //修改事故车状态
        return getResonseData(MLRegister.class, httpMessage);
      }

      case SIGN: {
        //签到
        return getResonseData(MLRegister.class, httpMessage);
      }
      case SIGN_INFO: {
        //签到积分信息
//                return null;
        return getResonseData(MLIntegralResponse.class, httpMessage);
      }
      case LOTTERY_MAIN: {
        //抽奖初始化
        return getResonseData(MLLotteryResponse.class, httpMessage);
      }
      case LOTTERY_RECORD: {
        //抽奖
        return getResonseEncry(MLRegister.class, httpMessage);
      }
      case LOTTERY_DETAIL: {
        //抽奖规则
        return getResonseData(MLSpecialResonse.class, httpMessage);
      }

      case LOTTERY_RECORD_LIST: {
        //抽奖记录
        return getResonseData(MLLotteryRecordResponse.class, httpMessage);
      }

      case MY_BANK: {
        //获取银行卡信息
        return getResonseData(MLMyBankResponse.class, httpMessage);
      }
      case MY_BANK_UPDATE: {
        //修改银行卡信息
        return getResonseData(MLRegister.class, httpMessage);
      }

      case MY_BANK_D: {
        //获取银行卡信息
        return getResonseData(MLMyBankResponse.class, httpMessage);
      }
      case MY_BANK_UPDATE_D: {
        //修改银行卡信息
        return getResonseData(MLRegister.class, httpMessage);
      }
      case MY_MANIFESTO: {
        //企业宣言
        return getResonseData(MLRegister.class, httpMessage);
      }

      case MY_PACKET_INFO: {
        //获取红包信息
        return getResonseData(MLMyPacketResponse.class, httpMessage);
      }

      case MY_PACKET_UPDATE: {
        //修改红包
        return getResonseData(MLRegister.class, httpMessage);
      }

      case MY_PHONE_BUISNESS: {
        //上传账单
        return getResonseData(MLRegister.class, httpMessage);
      }

      case BILL2LIST: {
        //账单列表
        return getResonseData(MLBill2ListResponse.class, httpMessage);
      }

      case BILL2_DETAIL: {
        //账单详情
        return getResonseData(MLBill2DetailResponse.class, httpMessage);
      }

      case MY_INTEGRAL_LIST: {
        //QP_积分列表
        return getResonseData(QPIntegralRes.class, httpMessage);
      }

      case MY_SHARE: {
        //QP_积分列表
        return getResonseData(MLMyShareData.class, httpMessage);
      }

      case TX_SHOP_ORDER_SUM: {

        //订单数量
        return getResonseData(TXShopSumRes.class, httpMessage);
      }

      case TX_CHECK_APP_VERSION: {
        return getResonseData(MLVersionApp.class, httpMessage);
      }

      default:
        break;
    }
    return null;
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
