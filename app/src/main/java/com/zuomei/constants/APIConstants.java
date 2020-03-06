package com.zuomei.constants;

public class APIConstants {

  //      public static final String API_DEFAULT_HOST = "http://192.168.1.134:8080/tx";
  public static final String API_DEFAULT_HOST = "http://app.tianxiaqp.com:8080/tx";
    public static final String API_NEW_DEFAULT_HOST = "http://121.42.149.98:8080/offersheet/";
//  public static final String API_NEW_DEFAULT_HOST = "http://192.168.1.134:8087/offersheet/";
  public static final String API_DEFAULT_TEST2 = "http://app.tianxiaqp.com:8080/tx";
//  public static final String API_DEFAULT_TEST2 = "http://192.168.1.139:8080/tx";

  public static final String API_IMAGE = "http://app.tianxiaqp.com:8080/tx/image/load";
  public static final String API_IMAGE_UPLOAD = "http://app.tianxiaqp.com:8080";
  public static final String API_IMAGE_UPLOAD_OLD = "http://app.tianxiaqp.com:8080/tx/image/upload";
  public static final String API_IMAGE_SHOW = "http://app.tianxiaqp.com:8080/uploads/";
  /**
   * 微信支付
   */
  public static final String APP_ID = "wxf3c855eceae9f001";
  public static final String PARTNER_ID = "1249787001";

  /**
   * 注册
   */
  public static final String API_REGISTER = "/mobile/user/addUser";
  /**
   * 登录
   */
  public static final String API_LOGIN = "/mobile/user/login";
  /**
   * 头条
   */
  public static final String API_QUERY = "/mobile3/head/query";
  /**
   * 登录-获取验证码
   */
  public static final String API_LOGIN_CODE = "/mobile2/other/msgVerify";
  /**
   * 登录更新
   */
  public static final String API_LOGIN_UPDATE = "/mobile2/other/checkUpdate";
  /**
   * 登录-重置密码
   */
  public static final String API_RESET_PWD = "/mobile/other/changePssword";
  /**
   * 首页-城市
   */
  public static final String API_HOME_CITY = "/mobile/home/getAllCityAndHotCity";
  /**
   * 首页-分类
   */
  public static final String API_HOME_CATALOG = "/mobile/home/getCompanyType";
  /**
   * 首页-广告位
   */
  public static final String API_HOME_AD = "/mobile/home/getHomeList";
  /**
   * 首页-评论
   */
  public static final String API_HOME_COMMENT = "/mobile2/comment/getCommentList";
  /**
   * 首页-地图
   */
  public static final String API_HOME_MAP = "/mobile/other/getMap";
  /**
   * 首页-商家
   */
  public static final String API_HOME_BUSINESS = "/mobile/home/getHomeDetileList";
  /**
   * 首页-搜索
   */
  public static final String API_HOME_SEARCH = "/mobile3/communicate/searchCompany";
  /**
   * 首页-消息数量
   */
  public static final String API_HOME_MESSAGE_COUNT = "/mobile/interaction/getNewTrendCount";
  /**
   * 首页-商家详情
   */
  public static final String API_HOME_BUSINESS_DETAIL = "/mobile2/detail/getDetailByID";

  /**
   * 首页-商家详情-拨打电话
   */
  public static final String API_HOME_BUSINESS_CALL = "/mobile/communicate/addPhoneList";
  /**
   * 首页-商家详情-产品
   */
  public static final String API_HOME_PRODUCT = "/mobile/detail/getMoreProduct";
  /**
   * 首页-商家详情-收藏
   */
  public static final String API_HOME_COLLECT = "/mobile/detail/addCollect";
  /**
   * 首页-商家详情-列表
   */
  public static final String API_HOME_BUSINESS_LIST = "/mobile/home/getComPanyByType";

  /**
   * 互动-列表
   */
  public static final String API_MESSAGE_LIST = "/mobile2/interaction/getTrends";
  /**
   * 互动-回复
   */
  public static final String API_MESSAGE_REPLY = "/mobile2/interaction/addTrendsReply";
  /**
   * 互动-发表互动消息
   */
  public static final String API_MESSAGE_PUBLISH = "/mobile2/interaction/addTrends";
  /**
   * 互动-举报
   */
  public static final String API_MESSAGE_REPORT = "/mobile/comment/addReport";

  /**
   * 事故车-添加
   */
  public static final String API_ACCIDENT_ADD = "/mobile2/accident/addAccident";
  /**
   * 事故车-列表
   */
  public static final String API_ACCIDENT_LIST = "/mobile2/accident/getAccidentList";
  /**
   * 事故车-详细
   */
  public static final String API_ACCIDENT_DETAIL = "/mobile/accident/getAccidentDetail";

  /**
   * 我的-上传商品
   */
  public static final String API_MY_PRODUCT_ADD = "/mobile/detail/addProductDetail";
  /**
   * 我的-删除商品
   */
  public static final String API_MY_PRODUCT_DEL = "/mobile/detail/removeGood";
  /**
   * 我的-进货列表
   */
  public static final String API_MY_STOCK_LIST = "/mobile/stock/getGoods";
  /**
   * 我的-添加进货
   */
  public static final String API_MY_STOCK_ADD = "/mobile/stock/addGoods";
  /**
   * 我的-删除进货
   */
  public static final String API_MY_STOCK_DEL = "/mobile/stock/removeGoods";
  /**
   * 我的-汽修列表
   */
  public static final String API_MY_REPAIR_LIST = "/mobile/breakdown/getBreakdown";
  /**
   * 我的-添加汽修
   */
  public static final String API_MY_REPAIR_ADD = "/mobile/breakdown/addBreakdown";
  /**
   * 我的-删除汽修
   */
  public static final String API_MY_REPAIR_DEL = "/mobile/breakdown/removeBreakdown";
  /**
   * 我的_基本信息
   */
  public static final String API_MY_INFO = "/mobile2/depot/getDepotDetail";
  /**
   * 我的_基本信息2
   */
  public static final String API_MY_INFO_B = "/mobile2/depot/getDepotDetail";
  /**
   * 我的_修改头像
   */
  public static final String API_MY_HEAD = "/mobile/depot/alertUserIcon";
  /**
   * 我的_修改名称
   */
  public static final String API_MY_DEPOTNAME = "/mobile/depot/alertDepotName";
  /**
   * 我的_修改真实名称
   */
  public static final String API_MY_REALNAME = "/mobile/depot/alertRealName";
  /**
   * 我的_修改支付宝
   */
  public static final String API_MY_ALIPAY = "/mobile/depot/alertAlipay";
  /**
   * 我的_修改密码
   */
  public static final String API_MY_UPDATEPWD = "/mobile/depot/alertPassword";
  /**
   * 我的_修改地区
   */
  public static final String API_MY_LOCATION = "/mobile/depot/alertLocation";
  /**
   * 我的_修改详细地址
   */
  public static final String API_MY_ADDRESS = "/mobile/depot/alertAddress";
  /**
   * 我的_修改手机号
   */
  public static final String API_MY_PHONE = "/mobile/depot/alertPhone";
  /**
   * 我的_优惠信息列表
   */
  public static final String API_MY_SPECIAL_LIST = "/mobile/other/getArticleList";
  /**
   * 我的_优惠信息详情
   */
  public static final String API_MY_SPECIAL_DETAIL = "/mobile/other/getArtile";
  /**
   * 我的_联系我们
   */
  public static final String API_MY_CONTACT = "/mobile2/other/getContact";
  /**
   * 我的_收藏
   */
  public static final String API_MY_COLLECT = "/mobile/detail/getCollectList";
  /**
   * 我的_用户量
   */
  public static final String API_MY_USER_COUNT = "/mobile/other/getUserCount";
  /**
   * 我的_用户搜素
   */
  public static final String API_MY_USER_LIST = "/mobile2/other/getUserCount";
  /**
   * 我的_通话记录
   */
  public static final String API_MY_DIAL = "/mobile2/communicate/getPhoneList2";
  /**
   * 我的_删除通话记录
   */
  public static final String API_MY_DIAL_DEL = "/mobile/communicate/delCommunicate";
  /**
   * 我的_删除通话记录详情
   */
  public static final String API_MY_DIAL_DETAIL_DEL = "/mobile/communicate/delCommunicateDetail";
  /**
   * 我的_通话记录详情
   */
  public static final String API_MY_DIAL_DETAIL = "/mobile/communicate/getPhoneDetail";
  /**
   * 我的_剩余分钟数
   */
  public static final String API_MY_DIAL_COUNT = "/mobile2/communicate/getSurplusMinute";
  /**
   * 我的_今日拨打次数
   */
  public static final String API_MY_DIAL_TODAY = "/mobile/communicate/getTodayCount";
  /**
   * 我的_总共拨打次数
   */
  public static final String API_MY_DIAL_ALL = "/mobile/communicate/getAllCount";
  /**
   * 我的_交易记录
   */
  public static final String API_MY_DEAL_LIST = "/mobile2/deal/getDealList";
  /**
   * 我的_提现
   */
  //public static final String API_MY_DEAL_RECHARGE= "/mobile/deal/askForCash";
  public static final String API_MY_DEAL_RECHARGE = "/mobile2/deal/askForCash";
  /**
   * 我的_充值
   */
//  public static final String API_MY_DEAL_WITHDRAW= "/mobile/deal/chargeCalls";
  public static final String API_MY_DEAL_WITHDRAW = "/mobile2/deal/chargeCalls";
  /**
   * 我的_提现列表
   */
  public static final String API_MY_WITHDRAW_LIST = "/mobile/deal/forCashList";
  /**
   * 我的_提现列表
   */
  public static final String API_MY_REBATE_LIST = "/mobile/deal/getRebateList";
  /**
   * 我的_交易_商品评论
   */
  public static final String API_MY_DEAL_COMMENT = "/mobile2/comment/addComment";
  /**
   * 我的_交易_发起商品退货
   */
  public static final String API_MY_DEAL_REFUND = "/mobile2/deal/salesReturn";
  /**
   * 我的_绑定账号
   */
  public static final String API_MY_BIND = "/mobile/user/alertBindInfo";
  public static final String MYSHARE = "/mobile3/share/info";


  /**
   * 我的_积分列表
   */
  public static final String API_QP_MY_INTEGRAL = "/mobile3/depot/integral";
  /**
   * 我的_积分列表
   */
  public static final String API_QP_OFFER_ADD = "/mobile3/partsQuotation/addPartsQuotation";

  //=====================

  /**
   * 我的_事故车列表
   */
  public static final String API_MY_ACCIDENT_LIST = "/mobile2/accident/getMyAccidentList";
  /**
   * 我的_事故车状态更改（出售 删除）
   */
  public static final String API_MY_ACCIDENT_DEL = "/mobile2/accident/changeAccidentState";
  /**
   * 我的_通话记录(时间段)
   */
  public static final String API_MY_DIAL2 = "/mobile2/communicate/getPhoneList";
  /**
   * 我的_通话记录详情(时间段)
   */
  public static final String API_MY_DIAL_DETAIL2 = "/mobile2/communicate/getPhoneDetail";
  /**
   * 我的_互动列表
   */
  public static final String API_MY_MESSAGE_LIST = "/mobile2/interaction/getMyTrends";
  /**
   * 我的_互动-回复
   */
  public static final String API_MY_MESSAGE_REPLY = "/mobile2/interaction/addTrendsReply";
  /**
   * 我的_互动-@我
   */
  public static final String API_MY_MESSAGE_ME = "/mobile2/interaction/getReplyMeTrends";
  /**
   * 我的_互动-删除
   */
  public static final String API_MY_MESSAGE_DEL = "/mobile2/interaction/delTrends";

  /**
   * 我的_二手件-添加
   */
  public static final String API_MY_LEAVE_ADD = "/mobile2/secondHand/addSecondHand";
  /**
   * 我的_二手件-列表
   */
  public static final String API_LEAVE_LIST = "/mobile2/secondHand/secondHandList";
  /**
   * 我的_二手件-我的列表
   */
  public static final String API_MY_LEAVE_LIST = "/mobile2/secondHand/mySecondHandList";
  /**
   * 我的_二手件-详情
   */
  public static final String API_MY_LEAVE_DETAIL = "/mobile2/secondHand/detail";
  /**
   * 我的_二手件-删除
   */
  public static final String API_MY_LEAVE_DEL = "/mobile2/secondHand/changeState";

  /**
   * 我的_优势件-添加
   */
  public static final String API_MY_ADVAN_ADD = "/mobile2/advantagehand/addAdvantageHand";
  /**
   * 我的_优势件-列表
   */
  public static final String API_ADVAN_LIST = "/mobile2/advantagehand/advantageHandList";
  /**
   * 我的_优势件-我的列表
   */
  public static final String API_MY_ADVAN_LIST = "/mobile2/advantagehand/myAdvantageHandList";
  /**
   * 我的_优势件-详情
   */
  public static final String API_MY_ADVAN_DETAIL = "/mobile2/advantagehand/detail";
  /**
   * 我的_优势件-删除
   */
  public static final String API_MY_ADVAN_DEL = "/mobile2/advantagehand/changeState";

  /**
   * 签到
   */
  public static final String API_SIGN = "/mobile2/integral/sign";
  /**
   * 签到积分-信息
   */
  public static final String API_SIGN_INFO = "/mobile2/integral/integralInfo";

  /**
   * 抽奖-初始化
   */
  public static final String API_LOTTERY_MAIN = "/mobile2/money/initDraw";
  /**
   * 抽奖
   */
  public static final String API_LOTTERY_RECORD = "/mobile2/money/drawRecord";
  /**
   * 抽奖记录列表
   */
  public static final String API_LOTTERY_RECORD_LIST = "/mobile2/money/recordList";
  /**
   * 抽奖规则
   */
  public static final String API_LOTTERY_DETAIL = "/mobile2/other/getDrawRuleInfo";
  /**
   * 获取银行卡信息
   */
  public static final String API_MYBANK = "/mobile2/detail/bankCardInfo";
  /**
   * 获取银行卡信息(汽修厂)
   */
  public static final String API_MYBANK_D = "/mobile2/depot/bankCardInfo";
  /**
   * 修改银行卡信息
   */
  public static final String API_MYBANK_UPDATE = "/mobile2/detail/bindBankCard";
  /**
   * 修改银行卡信息(汽修厂)
   */
  public static final String API_MYBANK_UPDATE_D = "/mobile2/depot/bindBankCard";
  /**
   * 企业宣言
   */
  public static final String API_MYMANIFESTO = "/mobile2/detail/updateDeclaration";
  /**
   * 获取红包
   */
  public static final String API_MYPACKET = "/mobile2/detail/getRedInfo";
  /**
   * 修改红包
   */
  public static final String API_MYPACKET_UPDATE = "/mobile2/detail/updateRedInfo";

  /**
   * 上传账单
   */
  public static final String API_BILL_UPDATE = "/mobile2/bill/addBill";
  /**
   * 账单列表
   */
  public static final String API_BILL_LIST = "/mobile2/bill/billList";
  /**
   * 账单详情
   */
  public static final String API_BILL_DETAIL = "/mobile2/bill/getBill";
  /**
   * 汽修厂支付信息
   */
  public static final String API_DEPOT_PAT_INFO = "/mobile2/money/getDepotMoneyInfo";

  /**
   * 汽修厂 配件添加
   */
  public static final String API_DEPOT_PART_ADD = "/mobile2/accidentCar/sendAccidentCar";
  /**
   * 汽修厂 我的事故车配件列表
   */
  public static final String API_DEPOT_PART_MAN_LIST = "/mobile2/accidentCar/myAccidentCar";
  /**
   * 汽修厂 我的事故车配件列表-删除
   */
  public static final String API_DEPOT_PART_MAN_DEL = "/mobile2/accidentCar/delAccidentCar";
  /**
   * 汽修厂 我的事故车配件列表-商家列表
   */
  public static final String API_DEPOT_PART_BUS_LIST = "/mobile2/accidentCar/getCompany";
  /**
   * 汽修厂 我的事故车配件列表-商家详细
   */
  public static final String API_DEPOT_PART_BUS_DETAIL = "/mobile2/accidentCar/getAccidentMatch";
  /**
   * 汽修厂 我的事故车配件列表-使用
   */
  public static final String API_DEPOT_PART_BUS_USE = "/mobile2/accidentCar/useAccidentMatch";

  /**
   * 商家 报价管理列表
   */
  public static final String API_BUS_PART_LIST = "/mobile2/accidentCar/getAccidentCar";
  /**
   * 商家 报价管理列表
   */
  public static final String API_BUS_PART_DETAIL = "/mobile2/accidentCar/getAccidentMatchOfCompany";
  /**
   * 商家 报价
   */
  public static final String API_BUS_PART_OFFER = "/mobile2/accidentCar/quote";

  /**
   * 支付 获取短信验证码
   */
  public static final String API_MY_PAY_GETCODE = "/mobile2/other/msgVerify";
  /**
   * 支付 设置支付密码
   */
  public static final String API_MY_PAY_SETPWD = "/mobile2/other/setPayPwd";
  /**
   * 支付 验证支付密码
   */
  public static final String API_MY_PAY_VERIFY = "/mobile2/other/verifyPayPwd";
  /**
   * 支付 判断支付密码
   */
  public static final String API_MY_PAY_JUDGE = "/mobile2/other/verifyIsPayPwd";

  /**
   * 微信支付_获取token
   */
  public static final String API_WEIXIN_TOKEN = "https://api.weixin.qq.com/cgi-bin/token";
  /**
   * 微信支付_获取token(服务端)
   */
  public static final String API_WEIXIN_TOKEN_SERVERS = "/mobile2/weixin/sign";
  /**
   * 微信支付_添加交易
   */
  public static final String API_WEIXIN_PAY = "/mobile2/deal/addDeal";
  /**
   * 微信支付_添加交易_加密
   */
  public static final String API_WEIXIN_PAY_ENCRYPT = "/mobile2/deal/addDealEncrypt";

  /**
   * 微信支付_支付确认
   */
  public static final String API_WEIXIN_PAY_CONFIRM = "/mobile2/weixin/payOk";

  public static final String API_UPLOAD_IMAGES = "/fileUpload/file/upload";
  public static final String API_UPLOAD_VOICE = "/fileUpload/file/uploadVoice";

  public static final String pointLikeReocrd = "/mobile3/pointLikeReocrd/changePointLikeReocrd";

  //=======================删除=======================================================
  public static final String API_LOGIN_PHONE = "/admin/phone/login";
  public static final String API_LOGIN_RESET_PASSWORD = "/admin/resetPassword";
  public static final String API_REGISTER_VERIFYCODE = "/admin/getVerifyCode";
  public static final String API_VERIFI_CODE = "/admin/register/verifyPhone";
  public static final String API_BIND_WEIBO = "/admin/bindWeibo";
  public static final String API_MYRESOURCES = "/zm/getMyResources";
  public static final String API_FRIEND_INVITE_CONTACT = "/zm/inviteContactFriend";
  public static final String API_FRIEND_GET_NEW = "/zm/getNewFriends";
  public static final String API_FRIEND_SEARCH = "/zm/searchResources";
  public static final String API_FRIEND_ALL = "/zm/getMyFriends";
  public static final String API_FRIEND_NEAR = "/zm/getNearFriends";
  public static final String API_FRIEND_VERIFY = "/zm/updateFriendValid";
  public static final String API_FRIEND_VERIFY_PHONE = "/zm/verifyContactPhone";
  public static final String API_FRIEND_ADD = "/zm/addFriendRelation";
  public static final String API_ADDRESOURCES = "/zm/addResource";
  public static final String API_UPDATEUSERSIGNATURE = "/zm/updateUserSignature";
  public static final String API_UPDATEUSERPHONE = "/zm/updateUserPhone";
  public static final String API_UPDATEPHONEALBUM = "/zm/updatePhoneAlbum";
  public static final String API_UPDATEBASICINFO = "/zm/updateBasicInfo";
  public static final String API_UPDATEDETAILINFO = "/zm/updateDetailInfo";
  public static final String API_UPDATECONSORTINFO = "/zm/updateConsortInfo";
  public static final String API_UPDATELABELS = "/zm/updateLabels";
  public static final String API_GENERATE_MATCHREPORT = "/zm/generateMatchReport";
  public static final String API_GETUSERDETAIL = "/zm/getUserDetail";
  public static final String API_GETPHONEALBUM = "/zm/getPhoneAlbum";
  public static final String API_GETZMACHIEVEMENTS = "/zm/getZmAchievements";
  public static final String API_GETUSERMATCHMAKER = "/zm/getUserMatchMaker";
  public static final String API_DOWNLOAD = "/zm/download/file";

  public static final String API_GETWEIBOURL = "/zm/getWeiboUrl";
  public static final String API_REPORTUSER = "/zm/reportUser";
  public static final String API_ADDBLACK = "/zm/addBlackList";
  public static final String API_RELIEVEFRIEND = "/zm/relieveFriendRelation";
  public static final String API_SENDMATCHREPORT = "/zm/sendMatchReport";
  public static final String API_GETMATCHRECORDS = "/zm/getMatchRecords";
}
