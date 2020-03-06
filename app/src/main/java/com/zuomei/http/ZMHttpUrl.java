package com.zuomei.http;

import com.zuomei.constants.APIConstants;

import java.util.List;

/**
 * Created with IntelliJ IDEA. User: michael Date: 12-12-8 Time: 下午4:21 Description:
 */
public class ZMHttpUrl {

  public static String getUrl(ZMHttpType.RequestType requestType,
      List<ZMHttpParam> urlParams) {
    String url = "";
    switch (requestType) {
      case REGISTER:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_REGISTER);
        break;
      case LOGIN:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_LOGIN);
        break;
      case HOME_CITY:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_HOME_CITY);
        break;
      case HOME_CATALOG:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_HOME_CATALOG);
        break;
      case HOME_BUSINESS:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_HOME_BUSINESS);
        break;
      case HOME_BUSINESS_DETAIL:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_HOME_BUSINESS_DETAIL);
        break;
      case HOME_COLLECT:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_HOME_COLLECT);
        break;
      case HOME_BUSINESS_LIST:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_HOME_BUSINESS_LIST);
        break;

      case MY_WITHDRAW_LIST:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_WITHDRAW_LIST);
        break;
      case MY_BIND:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_BIND);
        break;
      case HOME_COMMENT:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_HOME_COMMENT);
        break;

      case MESSAGE_LIST:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MESSAGE_LIST);
        break;
      case MESSAGE_REPLY:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MESSAGE_REPLY);
        break;
      case HOME_AD:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_HOME_AD);
        break;
      case HD_COLLECTION:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile2/interaction/collectionInteraction");
        break;
      case HD_DIANZAN:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile2/interaction/praiseInteraction");
        break;
      case HD_JUBAO:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile2/interaction/reportInteraction");
        break;

      case HOME_IMAGE: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/resource/findAll");
        break;

      }
      case MESSAGE_PUBLISH:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MESSAGE_PUBLISH);
        break;
      case MESSAGE_REPORT:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MESSAGE_REPORT);
        break;
      case HOME_PRODUCT:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_HOME_PRODUCT);
        break;
      case MY_STOCK_ADD:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_STOCK_ADD);
        break;
      case MY_STOCK_LIST:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_STOCK_LIST);
        break;
      case MY_STOCK_DEL:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_STOCK_DEL);
        break;
      case MY_REPAIR_LIST:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_REPAIR_LIST);
        break;
      case MY_REPAIR_ADD:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_REPAIR_ADD);
        break;
      case MY_DEAL_LIST: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_DEAL_LIST);
        break;
      }
      case MY_REPAIR_DEL:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_REPAIR_DEL);
        break;
      case MY_INFO_D:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_INFO);
        break;
      case MY_SHARE:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.MYSHARE);
        break;

      case MY_INFO_HEAD:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_HEAD);
        break;
      case MY_INFO_ADDRESS:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_ADDRESS);
        break;
      case WEIXIN_PAY:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_WEIXIN_PAY);
        break;

      case MY_INFO_DEPOTNAME:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_DEPOTNAME);
        break;
      case MY_INFO_LOCATION:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_LOCATION);
        break;
      case MY_INFO_PHONE:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_PHONE);
        break;
      case MY_INFO_REALNAME:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_REALNAME);
        break;
      case MY_INFO_ALIPAY:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_ALIPAY);
        break;
      case HOME_MAP:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_HOME_MAP);
        break;
      case ACCIDENT_ADD:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_ACCIDENT_ADD);
        break;
      case AACIDENT_LIST:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_ACCIDENT_LIST);
        break;
      case AACIDENT_DETAIL:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_ACCIDENT_DETAIL);
        break;
      case MY_SPECIAL_DETAIL:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_SPECIAL_DETAIL);
        break;
      case MY_REBATE_LIST: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_REBATE_LIST);
        break;
      }
      case MY_SPECIAL_LIST:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_SPECIAL_LIST);
        break;
      case MY_CONTACT:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_CONTACT);
        break;
      case MY_COLLECT:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_COLLECT);
        break;
      case HOME_SEARCH:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_HOME_SEARCH);
        break;
      case MY_PRODUCT_LIST:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_HOME_PRODUCT);
        break;
      case MY_PRODUCT_ADD:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_PRODUCT_ADD);
        break;
      case MY_PRODUCT_DEL:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_PRODUCT_DEL);
        break;

      case MY_USER_COUNT:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_USER_COUNT);
        break;

      case MY_USER_LIST:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_USER_LIST);
        break;

      case MY_UPDATE_PWD:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_UPDATEPWD);
        break;
    /*
     * case WEIXIN_GET_TOKEN: url = String.format("%s%s",
		 * APIConstants.API_DEFAULT_HOST, APIConstants.API_MY_USER_LIST); break;
		 */
    /*
     * case WEIXIN_GET_TOKEN: url =APIConstants.API_WEIXIN_TOKEN; break;
		 */

      case MY_DEAL_REFUND:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_DEAL_REFUND);
        break;

      case HOME_CALL:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_HOME_BUSINESS_CALL);
        break;

      case MY_DIAL_DETAIL:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_DIAL_DETAIL);
        break;

      case MY_DIAL_TODAY:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_DIAL_TODAY);
        break;

      case MY_DIAL_ALL:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_DIAL_ALL);
        break;
      case MY_DIAL_LIST:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_DIAL);
        break;
      case LOGIN_GETCODE:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_LOGIN_CODE);
        break;
      case LOGIN_RESET_PWD:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_RESET_PWD);
        break;

      case HOME_MESSAGE_COUNT:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_HOME_MESSAGE_COUNT);
        break;
      case MY_DIAL_DEL:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_DIAL_DEL);
        break;
      case MY_DIAL_DETAIL_DEL:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_DIAL_DETAIL_DEL);
        break;
      case MY_DEAL_RECHARGE:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_DEAL_RECHARGE);
        break;
      case MY_DEAL_WITHDRAW:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_DEAL_WITHDRAW);
        break;
      case MY_DEAL_COMMENT:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_DEAL_COMMENT);
        break;
      // ============删除==============================================
      case VERIFY_CODE:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_VERIFI_CODE);
        break;
      case BIND_WEIBO:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_BIND_WEIBO);
        break;
      case MYRESOURCES:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MYRESOURCES);
        break;
      case FRIEND_INVITE_CONTACT:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_FRIEND_INVITE_CONTACT);
        break;
      case FRIEND_GET_NEW:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_FRIEND_GET_NEW);
        break;
      case FRIEND_SEARCH:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_FRIEND_SEARCH);
        break;
      case FRIEND_NEAR:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_FRIEND_NEAR);
        break;
      case FRIEND_VERIFY:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_FRIEND_VERIFY);
        break;
      case LOGIN_UPDATE:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_LOGIN_UPDATE);
        break;
      case FRIEND_ADD:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_FRIEND_ADD);
        break;
      case USERDETAIL:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_GETUSERDETAIL);
        break;
      case PHOTOALBUM:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_GETPHONEALBUM);
        break;
      case ZMACHIEVEMENTS:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_GETZMACHIEVEMENTS);
        break;
      case ADDRESOURCE:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_ADDRESOURCES);
        break;
      case UPDATEUSERSIGNATURE:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_UPDATEUSERSIGNATURE);
        break;
      case UPDATEUSERPHOTO:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_UPDATEUSERPHONE);
        break;
      case UPDATEPHOTOALBUM:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_UPDATEPHONEALBUM);
        break;
      case UPDATEBASICINFO:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_UPDATEBASICINFO);

        break;
      case UPDATEDETAILINFO:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_UPDATEDETAILINFO);

        break;
      case UPDATECONSORTINFO:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_UPDATECONSORTINFO);
        break;
      case UPDATELABELS:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_UPDATELABELS);
        break;

      case GENERATE_MATCH_REPORT:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_GENERATE_MATCHREPORT);

        break;

      case FRIEND_GET_ALL:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_FRIEND_ALL);
        break;
      case DOWNLOADFILE:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_DOWNLOAD);
        break;

      case FRIEND_VERIFY_PHONE:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_FRIEND_VERIFY_PHONE);
        break;
      case USERMATCHMAKER:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_GETUSERMATCHMAKER);
        break;

      case GETWEIBOURL:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_GETWEIBOURL);
        break;
      case REPORTUSER:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_REPORTUSER);
        break;
      case RELIEVEFRIEND:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_RELIEVEFRIEND);
        break;
      case ADDBLACKLIST:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_ADDBLACK);
        break;
      case SENDMATCHREPORT:

        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_SENDMATCHREPORT);
        break;

      case MATCHRECORDS:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_GETMATCHRECORDS);
        break;
      case TX_SHOP_PLAY_DATA:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile/home/getHomeGoodsList");
        break;
      case TX_SHOP_FIND_HOMEGOODS_TYPE:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile/home/findHomeGoodsType");
        break;
      case TX_SHOP_FIND_HOMEGOODS_ALL_TYPE:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile/home/findAllGoodsType");
        break;

      // ============================================
      // 商家有限期
      case MY_INFO_B:
        url = String.format("%s%s", APIConstants.API_DEFAULT_TEST2,
            APIConstants.API_MY_INFO_B);
        break;
      // 我的事故车列表
      case MY_ACCIDENT_LIST:
        url = String.format("%s%s", APIConstants.API_DEFAULT_TEST2,
            APIConstants.API_MY_ACCIDENT_LIST);
        break;
      // 我的事故车列表
      case MY_ACCIDENT_DEL:
        url = String.format("%s%s", APIConstants.API_DEFAULT_TEST2,
            APIConstants.API_MY_ACCIDENT_DEL);
        break;
      // 通话记录列表（时间段）
      case MY_DIAL_LIST2:
        url = String.format("%s%s", APIConstants.API_DEFAULT_TEST2,
            APIConstants.API_MY_DIAL2);
        break;
      // 通话记录详情（时间段）
      case MY_DIAL_DETAIL2:
        url = String.format("%s%s", APIConstants.API_DEFAULT_TEST2,
            APIConstants.API_MY_DIAL_DETAIL2);
        break;
      // 通话记录中加已用分钟数
      case MY_DIAL_COUNT:
        url = String.format("%s%s", APIConstants.API_DEFAULT_TEST2,
            APIConstants.API_MY_DIAL_COUNT);
        break;
      // 我的-互动列表
      case MY_MESSAGE:
        url = String.format("%s%s", APIConstants.API_DEFAULT_TEST2,
            APIConstants.API_MY_MESSAGE_LIST);
        break;
      // 我的-互动列表-回复
      case MY_MESSAGE_REPLY:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_MESSAGE_REPLY);
        break;
      // 我的-互动列表-@我
      case MY_MESSAGE_ME:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_MESSAGE_ME);
        break;
      // 我的-互动列表-删除互动消息
      case MY_MESSAGE_DEL:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_MESSAGE_DEL);
        break;
      // 二手件-添加
      case LEAVE_ADD:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_LEAVE_ADD);
        break;

      // 二手件-列表
      case LEAVE_LIST:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_LEAVE_LIST);
        break;

      // 二手件-我的列表
      case LEAVE_MY_LIST:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_LEAVE_LIST);
        break;

      // 二手件-详情
      case LEAVE_DEATAIL:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_LEAVE_DETAIL);
        break;

      // 二手件-删除
      case LEAVE_DEL:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_LEAVE_DEL);
        break;

      // 优势件-添加
      case ADVAN_ADD:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_ADVAN_ADD);
        break;

      // 优势件-列表
      case ADVAN_LIST:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_ADVAN_LIST);
        break;

      // 优势件-我的列表
      case ADVAN_MY_LIST:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_ADVAN_LIST);
        break;

      // 优势件-详情
      case ADVAN_DEATAIL:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_ADVAN_DETAIL);
        break;

      // 优势件-删除
      case ADVAN_DEL:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_ADVAN_DEL);
        break;

      // 签到
      case SIGN:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_SIGN);
        break;

      // 签到积分信息
      case SIGN_INFO:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_SIGN_INFO);
        break;

      // 抽奖初始化
      case LOTTERY_MAIN:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_LOTTERY_MAIN);
        break;
      // 抽奖
      case LOTTERY_RECORD:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_LOTTERY_RECORD);
        break;
      // 抽奖记录列表
      case LOTTERY_RECORD_LIST:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_LOTTERY_RECORD_LIST);
        break;
      // 抽奖详情
      case LOTTERY_DETAIL:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_LOTTERY_DETAIL);
        break;
      // 银行信息
      case MY_BANK:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MYBANK);
        break;
      // 银行信息(汽修厂)
      case MY_BANK_D:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MYBANK_D);
        break;

      //修改银行卡信息
      case MY_BANK_UPDATE:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MYBANK_UPDATE);
        break;

      //修改银行卡信息(汽修厂)
      case MY_BANK_UPDATE_D:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MYBANK_UPDATE_D);
        break;
      //企业宣言
      case MY_MANIFESTO:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MYMANIFESTO);
        break;

      //获取红包
      case MY_PACKET_INFO:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MYPACKET);
        break;

      //修改红包
      case MY_PACKET_UPDATE:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MYPACKET_UPDATE);
        break;

      //上传账单
      case MY_PHONE_BUISNESS:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_BILL_UPDATE);
        break;

      //账单列表
      case BILL2LIST:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_BILL_LIST);
        break;

      //账单详情
      case BILL2_DETAIL:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_BILL_DETAIL);
        break;

      //汽修厂支付信息
      case DEPOT_PAY_INFO:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_DEPOT_PAT_INFO);
        break;

      //添加支付加密
      case WEIXIN_PAY_ENCRYPT:
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_WEIXIN_PAY_ENCRYPT);
        break;
      //支付确认
      case WEIXIN_PAY_CONFIRM: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_WEIXIN_PAY_CONFIRM);
        break;
      }

      //汽修厂-配件添加
      case DEPOT_PART_ADD: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_DEPOT_PART_ADD);
        break;
      }

      //汽修厂-我的事故车配件 列表
      case DEPOT_PART_MG_LIST: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_DEPOT_PART_MAN_LIST);
        break;
      }

      //汽修厂-我的事故车配件 列表-删除
      case DEPOT_PART_MG_DEL: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_DEPOT_PART_MAN_DEL);
        break;
      }
      //汽修厂-我的事故车配件 报价商家列表
      case DEPOT_PART_BUSINESS_LIST: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_DEPOT_PART_BUS_LIST);
        break;
      }

      //汽修厂-我的事故车配件 报价商家详情
      case DEPOT_PART_BUSINESS_DETAIL: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_DEPOT_PART_BUS_DETAIL);
        break;
      }

      //汽修厂-我的事故车配件 使用商家报价
      case DEPOT_PART_BUSINESS_USE: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_DEPOT_PART_BUS_USE);
        break;
      }

      //商家 报价管理列表
      case BUS_PART_LIST: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_BUS_PART_LIST);
        break;
      }

      //商家 报价详情
      case BUS_PART_DETAIL: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_BUS_PART_DETAIL);
        break;
      }

      //商家 报价
      case BUS_PART_OFFER: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_BUS_PART_OFFER);
        break;
      }

      //获取短信验证码
      case MY_PAY_GETCODE: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_PAY_GETCODE);
        break;
      }
      //设置支付密码
      case MY_PAY_SETPWD: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_PAY_SETPWD);
        break;
      }
      //验证支付密码
      case MY_PAY_VERIFYPWD: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_PAY_VERIFY);
        break;
      }
      //判断是否设置支付密码
      case MY_PAY_JUDGEPWD: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_MY_PAY_JUDGE);
        break;
      }

      //=========================

      //积分列表
      case MY_INTEGRAL_LIST: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_QP_MY_INTEGRAL);
        break;
      }

      //积分列表
      case OFFER_ADD: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_QP_OFFER_ADD);
        break;
      }

      //积分列表
      case TX_INTERGRAL_SHOP: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/product/query");
        break;
      }

      //积分描述
      case TX_INTERGRAL_SHOP_DETAIL: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/product/detail");
        break;
      }

      //积分商城-购买
      case TX_INTERGRAL_SHOP_BUY: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/product/buyProduct");
        break;
      }

      //积分商城-记录
      case TX_INTERGRAL_SHOP_CONVERT: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/record/query");
        break;
      }
      //15、行业资讯
      case TX_INFO_LIST: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/news/query");
        break;
      }

      //15、行业资讯
      case HOME_CALL_WEB: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/calls/callBack");
        break;
      }

      //12、类别数据和热门类别
      case TX_HOME_CATALOG: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/home/getCompanyType");
        break;
      }
      case GET_SECOND_COMPANYTYPE: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/home/getDaZhongCompanyType");
        break;
      }
      case GET_SECOND_COMPANYTYPE2: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/home/getSecondCompanyType");
        break;
      }
      case UPDATE_COMPANY_DECLARATION: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile2/depot/updateCompanyDeclaration");
        break;
      }
      case UPDATE_COMPANY_PHONE: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile2/depot/updateCompanyPhone");
        break;
      }
      case FIND_COLLECT_INTERACTION: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile/detail/findCollectInteraction");
        break;
      }
      //13、支付宝
      case ORDER_PAY_PARAM_ALIPAY: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile2/alipay/sign");
        break;
      }
      //14、支付宝确认
      case ORDER_PAY_ALIPAY_RIGHT: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile2/alipay/payOk");
        break;
      }

      //80、增加供应/求购
      case SUPPLY_ADD: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/sup/addSupDem");
        break;
      }

      //78、供应/求购列表
      case SUPPLY_LIST: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/sup/getAllSupDem");
        break;
      }

      //79、我的供应/求购列表
      case SUPPLY_MY: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/sup/getSupDemById");
        break;
      }

      //81、删除供应/求购
      case SUPPLY_MY_DEL: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/sup/deleteSupDem");
        break;
      }

      //82、商户搜索
      case HOME_SEARCH_ALL: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/communicate/findCompay");
        break;
      }

      //87 商城模块-商品列表
      case TX_SHOP_PRODUCT_LIST: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallProduct/listSelling");
        break;
      }

      case FIND_PRODUCT_BY_KEY: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallProduct/findProductByKey");
        break;
      }

      case UPDATE_CONCURREN_OPERATE: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile2/depot/updateCompanyConcurrenOperate");
        break;
      }
      case FIND_COMPANY_PRODUCTS: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile/detail/findCompanyProducts");
        break;
      }
      case DELETE_COMPANY_PRODUCTS: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile/detail/deleteCompanyProducts");
        break;
      }
      case ADD_COMPANY_PRODUCTS: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile/detail/addCompanyProducts");
        break;
      }

      case FIND_PRAISEINFO_BY_INTERACTIONID: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile2/interaction/findPraiseInfoByInteractionId");
        break;
      }

      case MY_UPDATE_DEPOT: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile/detail/addCompanyProducts");
        break;
      }

      //87 商城模块-商品列表
      case TX_SHOP_PRODUCT_BY_TYPE: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallProduct/listByType");
        break;
      }

      //88 商城模块-商品详细
      case TX_SHOP_PRODUCT_DETAIL: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallProduct/detail");
        break;
      }

      //91、商城模块-添加购物车
      case TX_SHOP_CAR_ADD: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallCar/addCar");
        break;
      }

      //83商城模块-获取收货地址列表
      case TX_SHOP_ADDRESS_MY: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallAddress/list");
        break;
      }

      //商城模块-添加收货地址
      case TX_SHOP_ADDRESS_ADD: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallAddress/addAddress");
        break;
      }

      //商城模块-删除收货地址
      case TX_SHOP_ADDRESS_DEL: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallAddress/removeAddress");
        break;
      }
      //商城模块-修改收货地址
      case TX_SHOP_ADDRESS_UPDATE: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallAddress/updateAddress");
        break;
      }
      //商城模块-设置默认收货地址
      case TX_SHOP_ADDRESS_SET: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallAddress/setDefaultAddress");
        break;
      }
      //商城模块-立即购买
      case TX_SHOP_PRODUCT_BUY: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallOrderForm/readyPay");
        break;
      }
      //商城模块-购物车
      case SHOPCAR: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallCar/list");
        break;
      }
      //商城模块-提交商品订单
      case TX_SHOP_PRODUCT_SUBMIT: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallOrderForm/orderSubmit");
        break;


      }
      //商城模块-购物车数量
      case EDITSHOPCAR: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallCar/carEditer");
        break;
      }

      //商城模块-删除购物车
      case DELSHOPCAR: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallCar/removeCar");
        break;
      }

      //108、商城模块-微信支付
      case TX_SHOP_PRODUCT_PAY_WX: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallPay/payWithWeixin");
        break;
      }
      //109、商城模块-支付宝支付
      case TX_SHOP_PRODUCT_PAY_ALIPAY: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallPay/payWithAlipay");
        break;
      }
      //110、商城模块-余额支付
      case TX_SHOP_PRODUCT_PAY_YUE: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallPay/payOnlyBalance");
        break;
      }
      //111.商城模块-微信支付确认接口
      case WEIXIN_PAY_CONFIRM_SHOP: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallPay/weixinPayOk");
        break;
      }
      //112、商城模块-支付宝支付确认接口
      case TX_PAY_ALIPAY_AFFIRM: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallPay/aliPayOk");
        break;
      }
      //97 97、商城模块-购物车立即购买
      case SHOPCARBUY: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallOrderForm/carReadyPay");
        break;
      }

      //100、商城模块-订单记录（待付款）
      case TX_SHOP_ORDER_DFK: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallOrderForm/prepareList");
        break;
      }

      //商城模块-订单记录（待收货）
      case TX_SHOP_ORDER_DSH: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallOrderForm/receiptList");
        break;
      }
      //商城模块-订单记录（已收货）
      case TX_SHOP_ORDER_YSH: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallOrderForm/receivedList");
        break;
      }
      //订单详情）
      case ORDERDETAIL: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallOrderForm/orderDetail");
        break;
      }
      //商品评价
      case SHOPPRODUCTPJ: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/evaluation/evaluationList");
        break;
      }
      //商品评价
      case SHOPPRODUCTLIST: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallProduct/listByCompanyId");
        break;
      }

      //商城模块-取消订单
      case TX_PAY_CANCEL: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallOrderForm/cancelOrder");
        break;
      }
      //商城模块-余额支付订单检查
      case TX_PAY_YUE_CHECEK: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallOrderForm/balanceOrderCheck");
        break;
      }

      //商城模块-确认收货
      case TX_PAY_AFFIRM: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallOrderForm/receiptProduct");
        break;
      }
      //107、商城模块-申请退货
      case TX_PAY_QUIT: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallOrderForm/returnProduct");
        break;
      }
      case SHOPPINGJIACHAT: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallOrderForm/addProductComment");
        break;

      }

      //商城模块-物流信息
      case TX_SHOP_ORDER_LOGISTICAL: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallOrderForm/logisticsDetail");
        break;
      }
      //商品发布
      case SHOPPRODUCTFABU: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallProduct/addProduct");
        break;
      }
      case GET_USER_TRENDS: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile2/interaction/getUserTrends");
        break;
      }
      case GET_USER_COMPANY_TRENDS: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile2/interaction/getCompanyTrends");
        break;
      }
      case CHECK_APPVERSION_UPDATE: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile/other/checkAndroidUpdate");
        break;
      }
      //头条
      case API_QUERY: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            APIConstants.API_QUERY);
        break;
      }

      //商城模块-提交购物车订单
      case TX_SHOP_PRODUCT_SUBMIT_CAR: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallOrderForm/orderCarSubmit");
        break;
      }

      //商城模块-特价商品
      case TX_SHOP_PRODUCT_SALE: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallProduct/saleList");
        break;
      }

      //商城模块-热卖商品
      case TX_SHOP_PRODUCT_HOT: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallProduct/sellingList");
        break;
      }

      //120 （待付款,待收货,已收货,三者的长度）
      case TX_SHOP_ORDER_SUM: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallOrderForm/size");
        break;
      } //120 （待付款,待收货,已收货,三者的长度）
      case TX_CHECK_APP_VERSION: {
        url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
            "/mobile3/mallOrderForm/size");
        break;
      }

      default:
        break;
    }
    if (urlParams == null) {
      return url;
    } else {
      if (urlParams.size() == 0) {
        return null;
      }
      for (ZMHttpParam httpParam : urlParams) {
        String key = String.format("${%s}", httpParam.getParamName());
        String value = httpParam.getParamValue();
        url = url.replace(key, value);
      }
      return url.trim();
    }
  }
}
