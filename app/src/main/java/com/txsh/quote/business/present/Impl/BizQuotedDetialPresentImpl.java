package com.txsh.quote.business.present.Impl;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.baichang.android.utils.BCDialogUtil;
import com.baichang.android.utils.BCStringUtil;
import com.baichang.android.utils.BCToolsUtil;
import com.baichang.android.widget.BCNoScrollListView;
import com.bumptech.glide.Glide;
import com.easemob.easeui.model.HxUser;
import com.easemob.easeui.utils.Contants;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.txsh.R;
import com.txsh.friend.ChatAty;
import com.txsh.quote.BCPopUpWindowsUtils;
import com.txsh.quote.Flag;
import com.txsh.quote.IBaseInteraction.BaseListener;
import com.txsh.quote.business.adapter.BizQuotedDetailAdapter;
import com.txsh.quote.business.entity.BizQuotedDetailData;
import com.txsh.quote.business.entity.BizQuotedPriceData;
import com.txsh.quote.business.entity.BizQuotedTransferData;
import com.txsh.quote.business.entity.CompanySubmitData;
import com.txsh.quote.business.entity.DeportInfoData;
import com.txsh.quote.business.model.BizQuotedDetailInteraction;
import com.txsh.quote.business.model.Impl.BizQuotedDetailInteractionImpl;
import com.txsh.quote.business.present.BizQuotedDetailPresent;
import com.txsh.quote.business.view.BizQuotedDetailView;
import com.zuomei.base.MLAppDiskCache;
import com.zuomei.base.MLEventBusModel;
import com.zuomei.constants.APIConstants;
import com.zuomei.model.MLLogin;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 汉玉 on 2017/3/15.
 */
public class BizQuotedDetialPresentImpl implements BizQuotedDetailPresent {

  private BizQuotedDetailInteraction mInt;
  private BizQuotedDetailView mView;

  private BizQuotedDetailAdapter bizQuotedDetailAdapter;
  private BizQuotedDetailData mBizQuotedDetailData = new BizQuotedDetailData();
  private String companyId = "";
  private String state = "";
  private MLLogin mlLogin = new MLLogin();
  private DeportInfoData deportInfoData = new DeportInfoData();

  public BizQuotedDetialPresentImpl(BizQuotedDetailView bizQuotedDetailView) {
    this.mView = bizQuotedDetailView;
    this.mInt = new BizQuotedDetailInteractionImpl();
    mlLogin = MLAppDiskCache.getLoginUser();
    companyId = mlLogin.Id;

  }


  @Override
  public void initListView(BCNoScrollListView lvParts, final View mainView,
      final Activity activity) {
    bizQuotedDetailAdapter = new BizQuotedDetailAdapter(activity,
        R.layout.bj_item_biz_quoted_detail);
    lvParts.setAdapter(bizQuotedDetailAdapter);
    lvParts.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!BCStringUtil.isEmpty(state)) {
          if (state.equals("1")) {//待报价
            BizQuotedPriceData mDatas = (BizQuotedPriceData) parent.getItemAtPosition(position);
            showPopUpQuoted(mainView, activity, position, mDatas);
          }
        }
      }
    });
  }

  @Override
  public void adapterSetDatas(List<BizQuotedPriceData> parts) {
    bizQuotedDetailAdapter.setData(parts);
  }

  /**
   * 设置页面信息
   */
  @Override
  public void detailSetDatas(BizQuotedDetailData mData, ListView lvParts, TextView tvName,
      ImageView ivQuoteDetail, TextView tvQuotePrice, TextView tvState, LinearLayout llLogistics,
      TextView tvLogisticsName, TextView tvLogisticsNumber, TextView btnSure, Activity activity) {

    if (!BCStringUtil.isEmpty(mBizQuotedDetailData.depotInfo)) {
      deportInfoData = (new Gson())
          .fromJson(mBizQuotedDetailData.depotInfo, DeportInfoData.class);
    }

    Glide.with(activity)
        .load(APIConstants.API_IMAGE + "?id=" + deportInfoData.headPic)
        .asBitmap()
        .error(R.mipmap.bj_default)
        .into(ivQuoteDetail);

    tvName.setText(mData.title);
    String state = mData.state;
    String allMoney = getAllPrice(mData.parts);
    tvQuotePrice.setText("报价：" + allMoney + "元");
    mView.setAllPrice(allMoney + "元");
    if (!BCStringUtil.isEmpty(state)) {
      if (state.equals("1")) {//待报价
        tvState.setText(R.string.biz_state_1);
        tvState.setTextColor(activity.getResources().getColor(R.color.bj_state1));
        tvQuotePrice.setVisibility(View.GONE);
        btnSure.setText("提交");
      } else if (state.equals("2")) {//已报价
        tvState.setText(R.string.biz_state_2);
        tvState.setTextColor(activity.getResources().getColor(R.color.bj_state2));
        btnSure.setText("确认");
        btnSure.setClickable(false);
        btnSure.setBackgroundColor(activity.getResources().getColor(R.color.tv_black3));
      } else if (state.equals("3")) {//被采购 未选中 （根据商家ID判断）
        if (mData.sureCompanyId.equals(companyId)) {
          tvState.setText(R.string.biz_state_4);
          tvState.setTextColor(activity.getResources().getColor(R.color.bj_biz_state3));
          btnSure.setText("确认");
        } else {
          tvState.setText(R.string.biz_state_3);
          tvState.setTextColor(activity.getResources().getColor(R.color.bj_biz_state1));
          btnSure.setText("确认");
          btnSure.setClickable(false);
          btnSure.setBackgroundColor(activity.getResources().getColor(R.color.tv_black3));
        }
      } else if (state.equals("4")) {//已确认
        tvState.setText(R.string.biz_state_5);
        tvState.setTextColor(activity.getResources().getColor(R.color.bj_state2));
        btnSure.setText("发货");
      } else if (state.equals("5")) {//待发货
        tvState.setText(R.string.biz_state_6);
        tvState.setTextColor(activity.getResources().getColor(R.color.bj_state4));
        btnSure.setText("发货");
      } else if (state.equals("6")) {//已发货
        tvState.setText(R.string.biz_state_7);
        tvState.setTextColor(activity.getResources().getColor(R.color.bj_state4));
        llLogistics.setVisibility(View.VISIBLE);
        tvLogisticsName.setText(mData.logosticsName);
        tvLogisticsNumber.setText(mData.logisticsNo);
        btnSure.setText("发货");
        btnSure.setClickable(false);
        btnSure.setBackgroundColor(activity.getResources().getColor(R.color.tv_black3));
      }
    }
  }

  /**
   * 获取报价详情
   */
  @Override
  public void getBizQuotedDetailData(BizQuotedTransferData bizQuotedTransferData,
      Activity activity) {
    Map<String, String> map = new HashMap<>();
    map.put("id", bizQuotedTransferData.quoteId);
    map.put("companyId", bizQuotedTransferData.companyId);
    mInt.getCompanyOfferSheetDetail(map, activity, new BaseListener<BizQuotedDetailData>() {
      @Override
      public void success(BizQuotedDetailData bizQuotedDetailData) {
        mBizQuotedDetailData = bizQuotedDetailData;
        state = mBizQuotedDetailData.state;
        mView.setDetailData(bizQuotedDetailData);
      }

      @Override
      public void error(String error) {

      }
    });
  }

  @Override
  public void showSendOrder(View view, final Activity activity) {
    if (!BCStringUtil.isEmpty(state)) {
      //待报价 提交
      if (state.equals("1")) {
        final List<BizQuotedPriceData> datas = bizQuotedDetailAdapter.getList();
        final String offerPrice = getAllPrice(datas);
        if (BCStringUtil.isEmpty(offerPrice)) {
          mView.showMsg("报价不能为空");
          return;
        } else {
          double price = Double.parseDouble(offerPrice);
          if (price == 0) {
            mView.showMsg("报价不能为0");
            return;
          }
          BizQuotedPriceData isQuoted = getQuotedState(datas);
          if (isQuoted != null) {
            mView.showMsg(isQuoted.name + "未报价,请报价");
            return;
          }
        }
        BCDialogUtil.showDialog(activity, "提示", "确定提交报价吗？", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            companyOffer(activity, offerPrice, datas);
          }
        }, null);
      } else if (state.equals("2")) {//已报价 确认
        BCDialogUtil.showDialog(activity, "提示", "确定确认报价吗？", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            companySure(activity);
          }
        }, null);
      } else if (state.equals("3")) {//被采购 未选中 （根据商家ID判断）
        if (mBizQuotedDetailData.sureCompanyId.equals(companyId)) { //确认
          BCDialogUtil
              .showDialog(activity, "提示", "确定确认报价吗？", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  companySure(activity);
                }
              }, null);
        }
      } else if (state.equals("4")) {//已确认  发货
        showPopUpSend(view, activity);
      } else if (state.equals("5")) {//待发货  发货
        showPopUpSend(view, activity);
      }
    }
  }

  /**
   * 商家报价提交
   */
  private void companyOffer(final Activity activity, String offerPrice,
      List<BizQuotedPriceData> datas) {
    Map<String, String> map = new HashMap<>();
    map.put("id", mBizQuotedDetailData.id);
    map.put("companyId", companyId);
    CompanySubmitData companySubmitData = new CompanySubmitData();
    companySubmitData.id = companyId;
    companySubmitData.offerPrice = offerPrice;
    companySubmitData.headPic = mlLogin.headPic;
    companySubmitData.offerSheetState = "2";
    companySubmitData.state = "2";
    companySubmitData.hxUser = mlLogin.hxUser;
    companySubmitData.hxPwd = mlLogin.hxPwd;
    companySubmitData.name = mlLogin.name;
    companySubmitData.partses = datas;
    companySubmitData.phone = mlLogin.phone;
    String offer = (new Gson()).toJson(companySubmitData).toString();
    map.put("offer", offer);
    mInt.companyOffer(map, activity, new BaseListener<Boolean>() {
      @Override
      public void success(Boolean aBoolean) {
        if (aBoolean) {
          mView.showMsg("提交成功！！！");
          EventBus.getDefault().post(new MLEventBusModel(Flag.EVENT_BIZ_QUOTED_LIST_REFRESH));
          activity.finish();
        }
      }

      @Override
      public void error(String error) {

      }
    });
  }

  /**
   * 商家确认
   */
  private void companySure(final Activity activity) {
    Map<String, String> map = new HashMap<>();
    map.put("id", mBizQuotedDetailData.id);
    mInt.companySure(map, activity, new BaseListener<Boolean>() {
      @Override
      public void success(Boolean aBoolean) {
        if (aBoolean) {
          mView.showMsg("确认成功！！！");
          mView.setBtnFont("发货");
          state = "4";
          EventBus.getDefault().post(new MLEventBusModel(Flag.EVENT_BIZ_QUOTED_LIST_REFRESH));
          activity.finish();
        }
      }

      @Override
      public void error(String error) {

      }
    });
  }

  /**
   * 商家发货
   */
  private void companySend(final Activity activity, String logisticsName, String logisticsNo) {
    Map<String, String> map = new HashMap<>();
    map.put("id", mBizQuotedDetailData.id);
    map.put("logisticsName", logisticsName);
    map.put("logisticsNo", logisticsNo);
    mInt.companySend(map, activity, new BaseListener<Boolean>() {
      @Override
      public void success(Boolean aBoolean) {
        if (aBoolean) {
          mView.showMsg("发货成功！！！");
          EventBus.getDefault().post(new MLEventBusModel(Flag.EVENT_BIZ_QUOTED_LIST_REFRESH));
          activity.finish();
        }
      }

      @Override
      public void error(String error) {

      }
    });
  }

  /**
   * 发货
   */
  private void showPopUpSend(View view, final Activity activity) {
    View contentView = LayoutInflater.from(activity).inflate(R.layout.bj_pop_send_order, null);
    final PopupWindow mPop = BCPopUpWindowsUtils
        .getIstnace()
        .getPopUpWindows(contentView, 0, 0, activity, 0.3f, false)
        .showCenterOfView(view);
    ImageView ivDismiss = (ImageView) contentView.findViewById(R.id.iv_dismiss);
    TextView btnSend = (TextView) contentView.findViewById(R.id.tv_send);
    final EditText etName = (EditText) contentView.findViewById(R.id.et_logistics_name);
    final EditText etNum = (EditText) contentView.findViewById(R.id.et_logistics_num);
    ivDismiss.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mPop.dismiss();
      }
    });
    btnSend.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        final String name = etName.getText().toString();
        final String num = etNum.getText().toString();
        if (BCStringUtil.isEmpty(name)) {
          mView.showMsg("物流名称不能为空");
          return;
        }

        if (BCStringUtil.isEmpty(num)) {
          mView.showMsg("物流单号不能为空");
          return;
        }
        BCDialogUtil
            .showDialog(activity, "提示", "确定发货吗？", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                companySend(activity, name, num);
              }
            }, null);
      }
    });
  }

  /**
   * 配件报价
   */
  private void showPopUpQuoted(View view, Activity activity,
      final int position, BizQuotedPriceData mData) {
    View contentView = LayoutInflater.from(activity).inflate(R.layout.bj_pop_quoted, null);
    final PopupWindow mPop = BCPopUpWindowsUtils
        .getIstnace()
        .getPopUpWindows(contentView, 0, 0, activity, 0.3f, false)
        .showCenterOfView(view);
    ImageView ivDismiss = (ImageView) contentView.findViewById(R.id.iv_dismiss);
    TextView btnAdd = (TextView) contentView.findViewById(R.id.tv_add);
    TextView tvName = (TextView) contentView.findViewById(R.id.tv_name);
    TextView tvTypeName = (TextView) contentView.findViewById(R.id.tv_type_name);
    TextView tvNumber = (TextView) contentView.findViewById(R.id.tv_number);
    final EditText etPrice = (EditText) contentView.findViewById(R.id.et_prcie);
    tvName.setText("配件：" + mData.name);
    tvTypeName.setText("类别：" + mData.typeName);
    tvNumber.setText("数量：" + mData.number);
    ivDismiss.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mPop.dismiss();
      }
    });
    btnAdd.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        String priceStr = etPrice.getText().toString();
        double price = 0;
        if (BCStringUtil.isEmpty(priceStr)) {
          mView.showMsg("报价不能为空");
          return;
        } else {
          price = Double.parseDouble(priceStr);
          if (price == 0) {
            mView.showMsg("报价不能为0");
            return;
          }
        }
        List<BizQuotedPriceData> datas = bizQuotedDetailAdapter.getList();
        BizQuotedPriceData bizQuotedDetailData = datas.get(position);
        bizQuotedDetailData.money = price + "";
        datas.set(position, bizQuotedDetailData);
        bizQuotedDetailAdapter.setData(datas);
        mView.setAllPrice(getAllPrice(datas) + "元");
        mPop.dismiss();
      }
    });
  }

  /***
   * 打电话
   */
  @Override
  public void call(final Activity activity) {
    BCDialogUtil.showDialog(activity, "提示", "确认拨打电话" + deportInfoData.phone + "吗？",
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            BCToolsUtil.call(activity, deportInfoData.phone);
          }
        }, null);
  }

  /**
   * 私信
   */
  @Override
  public void talk(Activity activity) {
    if (!BCStringUtil.isEmpty(deportInfoData.hxUser)) {
      HxUser mHxUser = new HxUser();
      mHxUser.emId = deportInfoData.hxUser;
      mHxUser.name = deportInfoData.name;
      mHxUser.userId = deportInfoData.id;
      Intent intent = new Intent(activity, ChatAty.class);
      intent.putExtra(Contants.EXTRA_USER, mHxUser);
      activity.startActivity(intent);
    }
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

  private BizQuotedPriceData getQuotedState(List<BizQuotedPriceData> datas) {
    BizQuotedPriceData bizQuotedPriceData = null;
    for (BizQuotedPriceData b : datas) {
      if (BCStringUtil.isEmpty(b.money)) {
        bizQuotedPriceData = b;
        break;
      } else {
        if (Double.parseDouble(b.money) == 0) {
          bizQuotedPriceData = b;
          break;
        }
      }
    }
    return bizQuotedPriceData;
  }

  @Override
  public void onDestroy() {
    mView = null;
  }

  @Override
  public void onStart() {

  }
}
