package com.txsh.quote.deport.present.Impl;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.baichang.android.utils.BCDialogUtil;
import com.baichang.android.utils.BCStringUtil;
import com.baichang.android.utils.BCToolsUtil;
import com.baichang.android.widget.BCNoScrollListView;
import com.bumptech.glide.Glide;
import com.easemob.easeui.model.HxUser;
import com.easemob.easeui.utils.Contants;
import com.txsh.R;
import com.txsh.friend.ChatAty;
import com.txsh.quote.Flag;
import com.txsh.quote.IBaseInteraction.BaseListener;
import com.txsh.quote.business.entity.BizQuotedDetailData;
import com.txsh.quote.business.entity.BizQuotedPriceData;
import com.txsh.quote.deport.adapter.PartsHasPriceAdapter;
import com.txsh.quote.deport.adapter.PartsPriceAdapter;
import com.txsh.quote.deport.adapter.PeijianAdapter;
import com.txsh.quote.deport.entity.CompanyDetailData;
import com.txsh.quote.deport.entity.QuotedTransferData;
import com.txsh.quote.deport.model.BizQuotedInteraction;
import com.txsh.quote.deport.model.Impl.BizQuotedInteractionImpl;
import com.txsh.quote.deport.present.BizQuotedPresent;
import com.txsh.quote.deport.view.BizQuotedView;
import com.zuomei.base.MLAppDiskCache;
import com.zuomei.base.MLEventBusModel;
import com.zuomei.constants.APIConstants;
import com.zuomei.model.MLLogin;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by 汉玉 on 2017/3/15.
 */
public class BizQuotedPresentImpl implements BizQuotedPresent {

  private BizQuotedInteraction mInt;
  private BizQuotedView mView;

  private PartsHasPriceAdapter partsHasPriceAdapter;
  private String allMoney = "";
  private CompanyDetailData mCompanyDetailData = new CompanyDetailData();
  private String state = "";
  private String companyId = "";
  private MLLogin mlLogin = new MLLogin();

  public BizQuotedPresentImpl(BizQuotedView bizQuotedView) {
    this.mView = bizQuotedView;
    this.mInt = new BizQuotedInteractionImpl();
    mlLogin = MLAppDiskCache.getLoginUser();
  }

  @Override
  public void initListView(BCNoScrollListView lvParts, Activity activity) {
    partsHasPriceAdapter = new PartsHasPriceAdapter(activity,
        R.layout.bj_item_com_biz_quote_has_price);
    lvParts.setAdapter(partsHasPriceAdapter);
  }

  /**
   * 确认采购
   */
  @Override
  public void sureAccept(final QuotedTransferData quotedTransferData, final Activity activity) {
    BCDialogUtil.showDialog(activity, "提示", "确认采购此商家吗？", new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        purchase(quotedTransferData, activity);
      }
    }, null);
  }

  /**
   * 获取报价的详情
   */
  @Override
  public void getBizQuotedDetailData(final QuotedTransferData quotedTransferData,
      Activity activity) {
    Map<String, String> map = new HashMap<>();
    map.put("id", quotedTransferData.quoteId);
    map.put("companyId", quotedTransferData.companyId);
    mInt.getCompanyOfferSheetDetail(map, activity, new BaseListener<CompanyDetailData>() {
      @Override
      public void success(CompanyDetailData companyDetailData) {
        mCompanyDetailData = companyDetailData;
        state = companyDetailData.state;
        companyId = quotedTransferData.companyId;
        mView.setDetailData(companyDetailData);
      }

      @Override
      public void error(String error) {

      }
    });
  }

  /**
   * 设置界面信息
   */
  @Override
  public void detailSetDatas(CompanyDetailData mData, TextView tvName,
      ImageView ivQuoteDetail, TextView tvQuotePrice, TextView tvState, LinearLayout llLogistics,
      TextView tvLogisticsName, TextView tvLogisticsNumber, TextView btnSure,
      Activity activity) {

    Glide.with(activity)
        .load(APIConstants.API_IMAGE + "?id=" + mData.company.headPic)
        .asBitmap()
        .error(R.mipmap.bj_default)
        .into(ivQuoteDetail);

    tvName.setText(mData.title);
    String state = mData.state;
    allMoney = getAllPrice(mData.parts);
    tvQuotePrice.setText("报价：" + allMoney + "元");
    if (!BCStringUtil.isEmpty(state)) {
      if (state.equals("1")) {//待报价
        tvState.setText(R.string.com_state_2);
        tvState.setTextColor(activity.getResources().getColor(R.color.bj_state1));
        btnSure.setClickable(false);
        btnSure.setBackgroundColor(activity.getResources().getColor(R.color.cm_tv_black3));
      } else if (state.equals("2")) {//已报价
        tvState.setText(R.string.com_state_3);
        tvState.setTextColor(activity.getResources().getColor(R.color.bj_state2));
      } else if (state.equals("3")) {//已采购 未选中
        if (companyId.equals(mData.sureCompanyId)) {
          tvState.setText(R.string.com_state_6);
          tvState.setTextColor(activity.getResources().getColor(R.color.bj_state2));
        } else {
          tvState.setText(R.string.biz_state_3);
          tvState.setTextColor(activity.getResources().getColor(R.color.bj_biz_state1));
        }
        btnSure.setClickable(false);
        btnSure.setBackgroundColor(activity.getResources().getColor(R.color.cm_tv_black3));
      } else if (state.equals("4")) {//已确认
        tvState.setText(R.string.com_state_1);
        tvState.setTextColor(activity.getResources().getColor(R.color.bj_state2));
        btnSure.setClickable(false);
        btnSure.setBackgroundColor(activity.getResources().getColor(R.color.cm_tv_black3));
      } else if (state.equals("5")) {//待发货
        tvState.setText(R.string.com_state_4);
        tvState.setTextColor(activity.getResources().getColor(R.color.bj_state4));
        btnSure.setClickable(false);
        btnSure.setBackgroundColor(activity.getResources().getColor(R.color.cm_tv_black3));
      } else if (state.equals("6")) {//已发货
        tvState.setText(R.string.com_state_5);
        tvState.setTextColor(activity.getResources().getColor(R.color.bj_state4));
        btnSure.setClickable(false);
        btnSure.setBackgroundColor(activity.getResources().getColor(R.color.cm_tv_black3));
        llLogistics.setVisibility(View.VISIBLE);
        tvLogisticsName.setText(mData.logosticsName);
        tvLogisticsNumber.setText(mData.logisticsNo);
      }
    }

  }

  /**
   * 打电话
   */
  @Override
  public void call(final Activity activity) {
    BCDialogUtil.showDialog(activity, "提示", "确认拨打电话" + mCompanyDetailData.company.phone + "吗？",
        new OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            BCToolsUtil.call(activity, mCompanyDetailData.company.phone);
          }
        }, null);
  }

  /**
   * 私信
   */
  @Override
  public void talk(Activity activity) {
    if (!BCStringUtil.isEmpty(mCompanyDetailData.company.hxUser)) {
      HxUser mHxUser = new HxUser();
      mHxUser.emId = mCompanyDetailData.company.hxUser;
      mHxUser.name = mCompanyDetailData.company.name;
      mHxUser.userId = mCompanyDetailData.company.id;
      Intent intent = new Intent(activity, ChatAty.class);
      intent.putExtra(Contants.EXTRA_USER, mHxUser);
      activity.startActivity(intent);
    }
  }

  @Override
  public void adapterSetDatas(List<BizQuotedPriceData> parts) {
    partsHasPriceAdapter.setData(parts);
  }

  /**
   * 确认采购
   */
  private void purchase(QuotedTransferData quotedTransferData, final Activity activity) {
    Map<String, String> map = new HashMap<>();
    map.put("id", quotedTransferData.quoteId);
    map.put("companyId", quotedTransferData.companyId);
    map.put("companyName", quotedTransferData.companyName);
    map.put("companyPhone", quotedTransferData.companyPhone);
    map.put("allMoney", allMoney);
    mInt.purchase(map, activity, new BaseListener<Boolean>() {
      @Override
      public void success(Boolean aBoolean) {
        if (aBoolean) {
          mView.showMsg("采购成功");
          EventBus.getDefault().post(new MLEventBusModel(Flag.EVENT_QUOTED_LIST_REFRESH));
          activity.finish();
        }
      }

      @Override
      public void error(String error) {

      }
    });
  }

  /**
   * 计算配件价格
   */
  private String getAllPrice(List<BizQuotedPriceData> datas) {
    double price = 0;
    for (BizQuotedPriceData b : datas) {
      if (!BCStringUtil.isEmpty(b.money) && !BCStringUtil.isEmpty(b.number)) {
        price += (Double.parseDouble(b.money) * Integer.parseInt(b.number));
      }
    }
    return BCToolsUtil.numberFormat(price, "0.00") + "";
  }


  @Override
  public void onDestroy() {
    mView = null;
  }

  @Override
  public void onStart() {

  }
}
