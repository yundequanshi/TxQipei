package com.txsh.quote.deport;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.baichang.android.widget.BCNoScrollListView;
import com.txsh.R;
import com.txsh.quote.CommonActivity;
import com.txsh.quote.Flag;
import com.txsh.quote.deport.entity.PartsData;
import com.txsh.quote.deport.entity.QuotedDetailData.CompanyData;
import com.txsh.quote.deport.entity.QuotedListData;
import com.txsh.quote.deport.entity.QuotedTransferData;
import com.txsh.quote.deport.present.Impl.QuotedDetailPresentImpl;
import com.txsh.quote.deport.present.QuotedDetailPresent;
import com.txsh.quote.deport.view.QuotedDetailView;
import com.zuomei.base.MLEventBusModel;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class QuotedDetailActivity extends CommonActivity implements QuotedDetailView {

  private BCNoScrollListView lvParts;
  private BCNoScrollListView lvBizQuoted;
  private TextView tvName;
  private TextView tvState;
  private TextView tvTime;
  private LinearLayout llCompany;

  private QuotedDetailPresent quotedDetailPresent;
  private QuotedListData quotedListData = new QuotedListData();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quoted_detail);
    EventBus.getDefault().register(this);
    if (getIntentData() != null) {
      quotedListData = (QuotedListData) getIntentData();
    }
    init();
  }

  private void init() {
    quotedDetailPresent = new QuotedDetailPresentImpl(this);
    lvParts = (BCNoScrollListView) findViewById(R.id.lv_peijian);
    lvBizQuoted = (BCNoScrollListView) findViewById(R.id.lv_biz_quoted);
    llCompany = (LinearLayout) findViewById(R.id.ll_company);
    tvState = (TextView) findViewById(R.id.tv_state);
    tvTime = (TextView) findViewById(R.id.tv_time);
    tvName = (TextView) findViewById(R.id.tv_name);
    quotedDetailPresent.getOfferSheetDetail(quotedListData, this);
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void event(MLEventBusModel model) {
    if (Flag.EVENT_QUOTED_LIST_REFRESH == model.type) {
      quotedDetailPresent.getOfferSheetDetail(quotedListData, this);
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
  public void startToBizQuoted(QuotedTransferData quotedTransferData) {
    startAct(getAty(), BizQuotedDetailInDepotActivity.class, quotedTransferData);
  }

  @Override
  public void setDetailData(String name, String state, String time, List<PartsData> parts,
      List<CompanyData> company, String sureCompanyId) {
    tvName.setText(name);
    tvTime.setText(time);
    quotedDetailPresent.setState(tvState, state, this);
    quotedDetailPresent
        .initListView(lvParts, lvBizQuoted, llCompany, parts, company, quotedListData.id,
            sureCompanyId, this);
  }

  @Override
  protected void onStart() {
    super.onStart();
    quotedDetailPresent.onStart();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    quotedDetailPresent.onDestroy();
    EventBus.getDefault().unregister(this);
  }
}
