package com.txsh.quote.deport;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.baichang.android.widget.BCNoScrollListView;
import com.txsh.R;
import com.txsh.model.TXShopTypeListRes.TXHomeGoodsTypeImageData;
import com.txsh.quote.CarTypeActivity;
import com.txsh.quote.CommonActivity;
import com.txsh.quote.Flag;
import com.txsh.quote.deport.present.Impl.PublishPresentImpl;
import com.txsh.quote.deport.present.PublishPresent;
import com.txsh.quote.deport.view.PublishView;
import com.txsh.shop.TXShopTypeActivity;
import com.zuomei.base.MLEventBusModel;
import com.zuomei.constants.MLConstants;
import com.zuomei.model.MLHomeCatalogData;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class PublishActivity extends CommonActivity implements PublishView {

  private BCNoScrollListView lvPeijian;
  private TextView tvType;
  private TextView etChildType;
  private TextView etYears;
  private EditText etDisplay;
  private EditText etLogistics;
  private EditText etCarJiaNum;
  private ImageView ivQuoted;
  private ImageView ivQuotedDelete;

  private PublishPresent mPresent;
  private String typeId = "";
  private String typeChildId = "";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bj_publish);
    EventBus.getDefault().register(this);
    init();
  }

  private void init() {
    lvPeijian = (BCNoScrollListView) findViewById(R.id.lv_peijian);
    tvType = (TextView) findViewById(R.id.tv_car_type);
    etChildType = (TextView) findViewById(R.id.et_child_type);
    etYears = (TextView) findViewById(R.id.et_years);
    etDisplay = (EditText) findViewById(R.id.et_display);
    etCarJiaNum = (EditText) findViewById(R.id.et_car_jia);
    etLogistics = (EditText) findViewById(R.id.et_logistics);
    ivQuoted = (ImageView) findViewById(R.id.iv_up_quoted);
    ivQuotedDelete = (ImageView) findViewById(R.id.iv_delete);
    mPresent = new PublishPresentImpl(this);
    mPresent.attachListView(lvPeijian, this);
  }


  public void allOnClick(View view) {
    switch (view.getId()) {
      //添加配件
      case R.id.tv_add:
        mPresent.showPopupWindow(findViewById(R.id.ll_main), this);
        break;
      //发布报价
      case R.id.tv_publish:
        mPresent.addOfferSheet(
            typeId,
            typeChildId,
            tvType.getText().toString(),
            etChildType.getText().toString(),
            etYears.getText().toString(),
            etDisplay.getText().toString(),
            etCarJiaNum.getText().toString(),
            etLogistics.getText().toString(),
            this
        );
        break;
      //选择车型
      case R.id.rl_select_type:
        startAct(getAty(), CarTypeActivity.class);
        break;
      //选择图片
      case R.id.tv_photos:
        mPresent.selectPhoto(this, ivQuoted, ivQuotedDelete);
        break;
      //选择子车型
      case R.id.et_child_type:
      case R.id.ll_select_child_type:
        mPresent.selectChildType(typeId, this);
        break;
      //选择年款
      case R.id.et_years:
        mPresent.selectYear(this, etYears);
        break;
      //删除
      case R.id.iv_delete:
        mPresent.delete(this, ivQuoted, ivQuotedDelete);
        break;
    }
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void event(MLEventBusModel model) {
    //获取车型
    if (model.type == Flag.EVENT_CAR_TYPE) {
      MLHomeCatalogData mlHomeCatalogData = (MLHomeCatalogData) model.obj[0];
      typeId = mlHomeCatalogData.id;
      tvType.setText(mlHomeCatalogData.name);
      etChildType.setText("");
    }
  }

  @Override
  public void showProgressBar() {

  }

  @Override
  public void hideProgressBar() {

  }

  @Override
  public void showMsg(String msg) {
    showMessage(msg);
  }

  @Override
  protected void onStart() {
    super.onStart();
    mPresent.onStart();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
    mPresent.onDestroy();
  }

  @Override
  public Activity getNowAty() {
    return this;
  }

  @Override
  public void setChildType(String typeChildName, String id) {
    typeChildId = id;
    etChildType.setText(typeChildName);
  }
}
