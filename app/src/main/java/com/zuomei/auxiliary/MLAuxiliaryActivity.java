package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.txsh.R;
import com.txsh.home.SecondSerach;
import com.txsh.home.TXInfoDetailFrg;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.MLConstants;
import com.zuomei.home.MLAccidentDetailFrg;
import com.zuomei.home.MLAccidentFrg;
import com.zuomei.home.MLAccidentadd1Frg;
import com.zuomei.home.MLAccidentadd2Frg;
import com.zuomei.home.MLAccidentadd3Frg;
import com.zuomei.home.MLAdvanDetailFrg;
import com.zuomei.home.MLAdvanadd1Frg;
import com.zuomei.home.MLAdvanadd2Frg;
import com.zuomei.home.MLAdvanadd3Frg;
import com.zuomei.home.MLHomeProductFrg;
import com.zuomei.home.MLLeaveDetailFrg;
import com.zuomei.home.MLLeaveadd1Frg;
import com.zuomei.home.MLLeaveadd2Frg;
import com.zuomei.home.MLLeaveadd3Frg;
import com.zuomei.home.MLMessageAddFrg;
import com.zuomei.home.MLMyAccidentFrg;
import com.zuomei.home.MLMyAdvanFrg;
import com.zuomei.home.MLMyBusinessFrg;
import com.zuomei.home.MLMyCashFrg;
import com.zuomei.home.MLMyCommentFrg;
import com.zuomei.home.MLMyIntegralFrg;
import com.zuomei.home.MLMyLeaveFrg;
import com.zuomei.home.MLMyMessageFrg;
import com.zuomei.home.MLMyMoneyFrg;
import com.zuomei.home.MLMyPasswordFrg;
import com.zuomei.home.MLMyProtectFrg;
import com.zuomei.model.MLAddDeal;
import com.zuomei.model.MLLogin;

import cn.ml.base.utils.IEvent;

/**
 * 附属Acitivity
 *
 * @author Marcello
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLAuxiliaryActivity extends BaseActivity implements IEvent<Object> {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.auxiliary_main);

    // 系统回收后 恢复数据
    if (savedInstanceState != null) {
      MLLogin user = (MLLogin) savedInstanceState.getSerializable("user");
      BaseApplication.getInstance().set_user(user);
      BaseApplication._currentCity = savedInstanceState
          .getString("currentCity");
      BaseApplication._messageLastId = savedInstanceState
          .getString("messageId");
      BaseApplication._addDeal = (MLAddDeal) savedInstanceState
          .getSerializable("deal");
    }

    Intent intent = getIntent();
    if (intent != null) {
      int position = intent.getIntExtra("data", 0);
      Object obj = intent.getSerializableExtra("obj");
      try {
        fillContent(obj, position);
      } catch (Exception e) {
        // 则返回到登录界面
        Intent i = getBaseContext().getPackageManager()
            .getLaunchIntentForPackage(
                getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
      }
    }
  }

  @Override
  protected void onNewIntent(Intent intent) {
    // TODO Auto-generated method stub
    super.onNewIntent(intent);
    if (intent != null) {
      int position = intent.getIntExtra("data", 0);
      fillContent(null, position);
    }
  }

  private Fragment oldFrg;

  private void fillContent(Object obj, int position) {
    Fragment fragment = null;
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    switch (position) {
      // 忘记密码-step1
      case MLConstants.LOGIN_PWD1:
        fragment = MLLoginPwd1Frg.instance();
        transaction.addToBackStack(null);
        break;
      // 忘记密码-step2
      case MLConstants.LOGIN_PWD2:
        fragment = MLLoginPwd2Frg.instance(obj);
        transaction.addToBackStack(null);
        break;
      // 忘记密码-step2
      case MLConstants.LOGIN_PWD3:
        fragment = MLLoginPwd3Frg.instance(obj);
        transaction.addToBackStack(null);
        break;
      // 首页搜索
      case MLConstants.HOME_SEARCH:
        fragment = MLHomeSearchFrg.instance(obj);
        transaction.addToBackStack(null);
        break;
      // 首页选车
      case MLConstants.HOME_CAR:
        fragment = MLHomeCarFrg.instance(obj);
        transaction.addToBackStack(null);
        break;
      // 首页选车-搜索
      case MLConstants.HOME_CAR_SEARCH:
        fragment = MLHomeCarSearchListFrg.instance(obj);
        transaction.addToBackStack(null);
        break;
      // 商家列表
      case MLConstants.HOME_BUSINESS_LIST:
        fragment = MLHomeBusinessListFrg.instance(obj);
        transaction.addToBackStack(null);
        break;
      // 商家列表
      case MLConstants.HOME_BUSINESS_YOU_LIST:
        fragment = MLHomeBusinessYouListFrg.instance();
        transaction.addToBackStack(null);
        break;
      // 商家评论
      case MLConstants.HOME_COMMENT:
        fragment = MLBusinessCommentFrg.instance(obj);
        transaction.addToBackStack(null);
        break;
      // 商家信息
      case MLConstants.HOME_BUSINESS_INFO:
        fragment = MLBusinessInfoFrg.instance(obj);
        transaction.addToBackStack(null);
        break;
      // 商家信息
      case MLConstants.HOME_PRODUCT:
        fragment = MLHomeProductFrg.instance(obj);
        transaction.addToBackStack(null);
        break;

      // 互动-发表回复
      case MLConstants.MESSAGE_REPLY_ADD:
        fragment = MLMessageAddFrg.instance();
        transaction.addToBackStack(null);
        break;
      // 事故车详细
      case MLConstants.ACCIDENT_DETAIL:
        fragment = MLAccidentDetailFrg.instance(obj);
        transaction.addToBackStack(null);
        break;
      // 事故车发布-step1
      case MLConstants.ACCIDENT_ADD:
        fragment = MLAccidentadd1Frg.instance();
        transaction.addToBackStack(null);
        break;
      // 事故车发布-step2
      case MLConstants.ACCIDENT_ADD2:
        fragment = MLAccidentadd2Frg.instance(obj);
        transaction.addToBackStack(null);
        break;
      // 事故车发布-step3
      case MLConstants.ACCIDENT_ADD3:
        fragment = MLAccidentadd3Frg.instance(obj);
        transaction.addToBackStack(null);
        break;
      // 我的-商家基本信息
      case MLConstants.MY_BUSINESS_INFO:
        fragment = MLMyBusinessFrg.instance(obj);
        transaction.addToBackStack(null);
        break;

      // 我的-密码修改
      case MLConstants.MY_PASSWORD:
        fragment = MLMyPasswordFrg.instance();
        transaction.addToBackStack(null);
        break;
      // 我的-密保问题
      case MLConstants.MY_PROTECT:
        fragment = MLMyProtectFrg.instance();
        transaction.addToBackStack(null);
        break;
      // 我的-通话记录
      case MLConstants.MY_PHONE_D:
        fragment = MLMyPhoneDFrg.instance();
        transaction.addToBackStack(null);
        break;
      // 我的-通话记录--搜索
      case MLConstants.MY_PHONE_D_SEARCH:
        fragment = MLMyPhoneDSearchFrg.instance();
        transaction.addToBackStack(null);
        break;
      // 我的-商品管理
      case MLConstants.MY_PRODUCT:
        fragment = MLMyProductFrg.instance(obj);
        transaction.addToBackStack(null);
        break;
      // 我的-商品发布
      case MLConstants.MY_PRODUCT_ADD:
        fragment = MLMyProductAddFrg.instance();
        transaction.addToBackStack(null);
        break;
      // 我的- 钱包
      case MLConstants.MY_MONEY:
        fragment = MLMyMoneyFrg.instance();
        transaction.addToBackStack(null);
        break;
      // 我的-提现
      case MLConstants.MY_CASH: {
        fragment = MLMyCashFrg.instance();
        transaction.addToBackStack(null);
        break;
      }
      // 我的-用户量
      case MLConstants.MY_USER: {
        fragment = MLMyUserFrg.instance();
        transaction.addToBackStack(null);
        break;
      }
      // 我的-账单
      case MLConstants.MY_BILL_D:
        fragment = MLMyBillDFrg.instance();
        transaction.addToBackStack(null);
        break;
      // 我的-优惠信息列表
      case MLConstants.MY_SPECIAL_LIST:
        fragment = MLMySpecialFrg.instance();
        transaction.addToBackStack(null);
        break;
      // 我的-优惠信息详情
      case MLConstants.MY_SPECIAL_DETAIL:
        fragment = MLMySpecialDetailFrg.instance(obj);
        transaction.addToBackStack(null);
        break;
      // 我的-通话记录详情
      case MLConstants.MY_PHONE_DETAIL:
        fragment = MLMyPhoneDetailFrg.instance(obj);
        transaction.addToBackStack(null);
        break;
      // 我的-进货记录
      case MLConstants.MY_STOCK:
        fragment = MLMyStockFrg.instance();
        transaction.addToBackStack(null);
        break;
      // 我的-进货记录详情
      case MLConstants.MY_STOCK_DETAIL:
        fragment = MLMyStockDetailFrg.instance(obj);
        transaction.addToBackStack(null);
        break;
      // 我的-进货记录添加
      case MLConstants.MY_STOCK_ADD:
        fragment = MLMyStockAddFrg.instance();
        transaction.addToBackStack(null);
        break;
      // 我的-汽修记录
      case MLConstants.MY_REPAIR:
        fragment = MLMyRepairFrg.instance();
        transaction.addToBackStack(null);
        break;
      // 我的-汽修详情
      case MLConstants.MY_PRODUCT_DETAIL:
        fragment = MLMyRepairDetailFrg.instance(obj);
        transaction.addToBackStack(null);
        break;
      // 我的-收藏
      case MLConstants.MY_COLLECT:
        fragment = MLMyCollectFrg.instance();
        transaction.addToBackStack(null);
        break;
      // 我的-汽修记录添加
      case MLConstants.MY_REPAIR_ADD:
        fragment = MLMyRepairAddFrg.instance();
        transaction.addToBackStack(null);
        break;
      // 我的-联系我们
      case MLConstants.MY_CONTACT:
        fragment = MLMyContactFrg.instance();
        transaction.addToBackStack(null);
        break;
      // 我的-绑定财付通
      case MLConstants.MY_BIND:
        fragment = MLBindFrg.instance(obj);
        transaction.addToBackStack(null);
        break;
      // 汽修厂-返利
      case MLConstants.MY_DEPOT_FANLI:
        fragment = MLMyFanliFrg.instance();
        transaction.addToBackStack(null);
        break;
      // 拨打电话
      case MLConstants.HOME_CALL:
        fragment = MLHomeCallFrg.instance(obj);
        transaction.addToBackStack(null);
        break;
      // 交易评论
      case MLConstants.MY_DEAL_COMMENT:
        fragment = MLMyCommentFrg.instance(obj);
        transaction.addToBackStack(null);
        break;
      case MLConstants.MY_TOOLS:
        fragment = MLMyToolFrg.instance();
        transaction.addToBackStack(null);
        break;
      // =======二期=================
      case MLConstants.MY_ACCIDENT:
        fragment = MLMyAccidentFrg.instance();
        transaction.addToBackStack(null);
        break;
      // 我的-通话记录详情（时间段）
      case MLConstants.MY_PHONE_DETAIL2:
        fragment = MLMyPhoneDetail2Frg.instance(obj);
        transaction.addToBackStack(null);
        break;
      // 我的互动
      case MLConstants.MY_MESSAGE:
        fragment = MLMyMessageFrg.instance();
        transaction.addToBackStack(null);
        break;
      // 我的二手件
      case MLConstants.MY_LEAVE:
        fragment = MLMyLeaveFrg.instance();
        transaction.addToBackStack(null);
        break;
      // 二手件详情
      case MLConstants.MY_LEAVE_DETAIL:
        fragment = MLLeaveDetailFrg.instance(obj);
        transaction.addToBackStack(null);
        break;

      // 我的二手件-STEP1
      case MLConstants.MY_LEAVE_ADD1:
        fragment = MLLeaveadd1Frg.instance();
        transaction.addToBackStack(null);
        break;
      // 我的二手件-STEP2
      case MLConstants.MY_LEAVE_ADD2:
        fragment = MLLeaveadd2Frg.instance(obj);
        transaction.addToBackStack(null);
        break;
      // 我的二手件-STEP3
      case MLConstants.MY_LEAVE_ADD3:
        fragment = MLLeaveadd3Frg.instance(obj);
        transaction.addToBackStack(null);
        break;

      // 我的优势件
      case MLConstants.MY_ADVAN:
        fragment = MLMyAdvanFrg.instance();
        transaction.addToBackStack(null);
        break;
      // 我的优势件-STEP1
      case MLConstants.MY_ADVAN_ADD1:
        fragment = MLAdvanadd1Frg.instance();
        transaction.addToBackStack(null);
        break;
      // 我的优势件-STEP2
      case MLConstants.MY_ADVAN_ADD2:
        fragment = MLAdvanadd2Frg.instance(obj);
        transaction.addToBackStack(null);
        break;
      // 我的优势件-STEP3
      case MLConstants.MY_ADVAN_ADD3:
        fragment = MLAdvanadd3Frg.instance(obj);
        transaction.addToBackStack(null);
        break;
      // 我的优势件-详情
      case MLConstants.MY_ADCAN_DETAIL:
        fragment = MLAdvanDetailFrg.instance(obj);
        transaction.addToBackStack(null);
        break;
      // 我的积分（签到）
      case MLConstants.MY_INTRGRAL:
        fragment = MLMyIntegralFrg.instance();
        transaction.addToBackStack(null);
        break;
      // 抽奖
      case MLConstants.MY_LOTTERY:
        fragment = MLLotteryFrg.instance();
        transaction.addToBackStack(null);
        break;
      // 抽奖记录
      case MLConstants.MY_LOTTERY_LIST:
        fragment = MLLotteryRecordFrg.instance();
        transaction.addToBackStack(null);
        break;
      // 抽奖规则
      case MLConstants.MY_LOTTERY_DETAIL:
        fragment = MLLotteryDetailFrg.instance();
        transaction.addToBackStack(null);
        break;

      // 绑定银行卡
      case MLConstants.MY_BANK_CARD:
        fragment = MLMyBankCardFrg.instance();
        transaction.addToBackStack(null);
        break;
      // 企业宣言
      case MLConstants.MY_MANIFESTO:
        fragment = MLMyManifestoFrg.instance(obj);
        transaction.addToBackStack(null);
        break;

      // 红包管理
      case MLConstants.MY_PACKET:
        fragment = MLMyPacketFrg.instance();
        transaction.addToBackStack(null);
        break;

      // 汽修厂-上传发货单
      case MLConstants.MY_BILL2:
        fragment = MLMyBill2Frg.instance();
        transaction.addToBackStack(null);
        break;

      // 我的账单-最近商家
      case MLConstants.MY_BILL2_BUSINESS:
        fragment = MLMyPhoneBusinessFrg.instance();
        transaction.addToBackStack(null);
        break;
      // 我的账单-历史账单
      case MLConstants.MY_BILL2_BUSINESS_LIST:
        fragment = MLMyBill2ListFrg.instance();
        transaction.addToBackStack(null);
        break;

      // 我的账单-账单详情
      case MLConstants.MY_BILL2_BUSINESS_DETAIL:
        fragment = MLMyBill2DetailFrg.instance(obj);
        transaction.addToBackStack(null);
        break;
      // 我的--支付密码
      case MLConstants.MY_PAY_PWD:
        fragment = MLMyPayPwdFrg.instance(obj);
        transaction.addToBackStack(null);
        break;

      // =======事故车报价模块---汽修厂==========================
      // 汽修厂-配件报价
      case MLConstants.PART_DEPOT:
        fragment = MLDepotPartAdd.instance();
        transaction.addToBackStack(null);
        break;

      // 汽修厂-商家报价详情
      case MLConstants.PART_DEPOT_DETAIL:
        fragment = MLDepotOfferDetail.instance(obj);
        transaction.addToBackStack(null);
        break;

      // 汽修厂-我的 配件列表
      case MLConstants.MY_ACCIDENT_PART:
        fragment = MLMyAccidentPart.instance();
        transaction.addToBackStack(null);
        break;

      // 汽修厂-我的事故车报价--商家列表
      case MLConstants.MY_ACCIDENT_BUSSINESS:
        fragment = MLMyAccidentBnList.instance(obj);
        transaction.addToBackStack(null);
        break;

      // 汽修厂-报价时 选择车型
      case MLConstants.MY_PART_CAR:
        fragment = MLMyPartCarFrg.instance(obj);
        transaction.addToBackStack(null);
        break;
      // 上传时选择车型
      case MLConstants.MY_PART_CAR1:
        fragment = MLMyPartCarFrg1.instance();
        transaction.addToBackStack(null);
        break;
      // 行业资讯详情
      case MLConstants.TX_INFO_DETAIL:
        fragment = TXInfoDetailFrg.instance(obj);
        transaction.addToBackStack(null);
        break;

      // =======事故车报价模块---商家==========================

      // 商家-我的报价管理
      case MLConstants.MY_PART_OFFER:
        fragment = MLMyPartOffer.instance();
        transaction.addToBackStack(null);
        break;
      // 报价管理- 商家报价
      case MLConstants.MY_PART_OFFER_PRICE:
        fragment = MLMyPartOfferPrice.instance(obj);
        transaction.addToBackStack(null);
        break;
      // 报价二手车搜索
      case MLConstants.Second_Serach:
        fragment = SecondSerach.instance(obj);
        transaction.addToBackStack(null);
        break;
      case MLConstants.HOME_SECOND_HAND_CAR:
        fragment = MLAccidentFrg.instance();
        transaction.addToBackStack(null);
        break;
      default:
        break;
    }
    if (fragment == null) {
      return;
    }
    /*
		 * transaction.replace(R.id.auxiliary_fl_content, fragment);
		 * transaction.setCustomAnimations(android.R.animator.fade_in,
		 * android.R.animator.fade_out); //transaction.show(fragment);
		 * //transaction.commit(); transaction.commitAllowingStateLoss();
		 */
    FragmentManager fragmentManager1 = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager1
        .beginTransaction();
    Fragment to_fragment = fragmentManager1.findFragmentByTag(fragment
        .getClass().getName());
    if (to_fragment != null) {
      for (int i = 0; i < fragmentManager1.getBackStackEntryCount(); i++) {
        BackStackEntry entry = fragmentManager1.getBackStackEntryAt(i);
        if (fragment.getClass().getName().equals(entry.getName())) {
          fragmentManager1.popBackStack(entry.getName(), 1);
        }
      }
    }
    fragmentTransaction.addToBackStack(fragment.getClass().getName());
    fragmentTransaction.replace(R.id.auxiliary_fl_content, fragment,
        fragment.getClass().getName()).commitAllowingStateLoss();

		/*
		 * if(oldFrg==null){ transaction.add(R.id.auxiliary_fl_content,
		 * fragment); oldFrg = fragment; transaction.commit(); return; } if
		 * (oldFrg != fragment) { if (!fragment.isAdded()) { // 先判断是否被add过
		 * transaction.hide(oldFrg).add(R.id.auxiliary_fl_content,
		 * fragment).commit(); // 隐藏当前的fragment，add下一个到Activity中 } else {
		 * transaction.hide(oldFrg).show(fragment).commit(); //
		 * 隐藏当前的fragment，显示下一个 } oldFrg = fragment;
		 * 
		 * }
		 */

  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    MLLogin user = BaseApplication.getInstance().get_user();
    String currentCity = BaseApplication._currentCity;
    String messageId = BaseApplication._messageLastId;
    MLAddDeal deal = BaseApplication._addDeal;
    outState.putSerializable("user", user);
    outState.putString("currentCity", currentCity);
    outState.putString("messageId", messageId);
    outState.putSerializable("deal", deal);
  }

  @Override
  public void onEvent(Object source, Object eventArg) {
    fillContent(source, (Integer) eventArg);
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) { // 按下的如果是BACK，同时没有重复
      FragmentManager fragmentManager1 = getSupportFragmentManager();
      int size = fragmentManager1.getBackStackEntryCount();
      if (size == 1) {
        finish();
      } else {
        return super.onKeyDown(keyCode, event);
      }

      return true;
    }
    return super.onKeyDown(keyCode, event);
  }

}
