package com.zuomei.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.comment.TXCmWebAty;
import com.txsh.home.TXInfoAty;
import com.txsh.home.TXIntegralShopAty;
import com.txsh.home.TXInvoiceAty;
import com.txsh.model.TxCmWebData;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.MLConstants;
import com.zuomei.model.MLLogin;

public class ConvenienceActivity extends BaseActivity {

  @ViewInject(R.id.iv_invoice)
  private ImageView fahuoIv;
  @ViewInject(R.id.tv_invoice)
  private TextView fahuoTv;
  @ViewInject(R.id.iv_qiandao)
  private ImageView qiandaoIv;
  @ViewInject(R.id.tv_qiandao)
  private TextView qiandaoTv;
  @ViewInject(R.id.iv_jifen)
  private ImageView ivJIfen;
  @ViewInject(R.id.tv_jifen)
  private TextView tvJifen;

  private MLLogin user = new MLLogin();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_convenience);
    ViewUtils.inject(this);
    user = ((BaseApplication) getApplication()).get_user();
    if (!user.isDepot) {
      fahuoIv.setVisibility(View.GONE);
      fahuoTv.setVisibility(View.GONE);
      qiandaoIv.setVisibility(View.GONE);
      qiandaoTv.setVisibility(View.GONE);
      ivJIfen.setVisibility(View.GONE);
      tvJifen.setVisibility(View.GONE);
    }
  }

  // 违章查询
  @OnClick(R.id.home_acciedent)
  public void accidentgOnClick(View view) {
    TxCmWebData data = new TxCmWebData();
    data.title = "违章查询";
    data.url = "http://wz.m.autohome.com.cn/";
    startAct(getAty(), TXCmWebAty.class, data);
  }

  // 快递查询
  @OnClick(R.id.home_kd)
  public void kdOnClick(View view) {
    toActivity(getAty(), MLConstants.MY_PART_CAR, "384");
  }

  // 地图
  @OnClick(R.id.home_btn_map)
  public void mapOnClick(View view) {
    TxCmWebData data = new TxCmWebData();
    data.title = "地图导航";
    data.url = "http://map.qq.com/m/index/map";
    startAct(getAty(), TXCmWebAty.class, data);
  }

  //签到抽奖
  @OnClick(R.id.qiandaochoujiang)
  public void qiandaoOnClick(View view) {
    if (qiandaoIv.getVisibility() != View.GONE && qiandaoTv.getVisibility() != View.GONE) {
      toActivity(getAty(), MLConstants.MY_INTRGRAL, 1);
    }
  }

  //行业资讯
  @OnClick(R.id.home_info)
  public void infoOnClick(View view) {
    startAct(getAty(), TXInfoAty.class);
  }


  //上传发货单
  @OnClick(R.id.home_btn_invoice)
  public void invoiceOnClick(View view) {
    if (fahuoIv.getVisibility() != View.GONE && fahuoTv.getVisibility() != View.GONE) {
      startAct(getAty(), TXInvoiceAty.class);
    }
  }

  //二手车市
  @OnClick(R.id.ll_ershou)
  public void erOnClick(View view) {
    toActivity(getAty(), MLConstants.HOME_SECOND_HAND_CAR, null);
  }

  //积分商城
  @OnClick(R.id.ll_jifen)
  public void jiOnClick(View view) {
    startAct(getAty(), TXIntegralShopAty.class);
  }

  public void back(View view) {
    finish();
  }
}
