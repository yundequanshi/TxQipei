package com.txsh.quote.business;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout.OnRefreshListener;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.txsh.R;
import com.txsh.quote.CommonActivity;
import com.txsh.quote.Flag;
import com.txsh.quote.business.entity.BizQuotedTransferData;
import com.txsh.quote.business.present.BizQuotedListPresent;
import com.txsh.quote.business.present.Impl.BizQuotedListPresentImpl;
import com.txsh.quote.business.view.BizQuotedListView;
import com.zuomei.base.MLEventBusModel;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BizQuotedListActivity extends CommonActivity implements BizQuotedListView,
    OnRefreshListener {

  private RadioButton rbState1;
  private RadioButton rbState2;
  private ListView lvQuoted;
  private SwipyRefreshLayout swipyRefreshLayout;

  private BizQuotedListPresent mPresent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_biz_quoted_list);
    EventBus.getDefault().register(this);
    init();
  }

  private void init() {
    rbState1 = (RadioButton) findViewById(R.id.rb_state1);
    rbState2 = (RadioButton) findViewById(R.id.rb_state2);
    lvQuoted = (ListView) findViewById(R.id.lv_biz_quoted);
    swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyRefreshLayout);
    mPresent = new BizQuotedListPresentImpl(this);
    rbState1.setChecked(true);
    rbState2.setChecked(false);
    mPresent.showWaitPrice(lvQuoted, this);
    swipyRefreshLayout.setOnRefreshListener(this);
  }

  public void allOnClick(View view) {
    switch (view.getId()) {
      //待报价
      case R.id.ll_state1:
        rbState1.setChecked(true);
        rbState2.setChecked(false);
        mPresent.showWaitPrice(lvQuoted, this);
        break;
      //已报价
      case R.id.ll_state2:
        rbState1.setChecked(false);
        rbState2.setChecked(true);
        mPresent.showAlreadyPrice(lvQuoted, this);
        break;
    }
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void event(MLEventBusModel model) {
    if (Flag.EVENT_BIZ_QUOTED_LIST_REFRESH == model.type) {
      mPresent.refreshData(this);
    }
  }

  @Override
  public void showProgressBar() {

  }

  @Override
  public void hideProgressBar() {
    swipyRefreshLayout.setRefreshing(false);
  }

  @Override
  public void showMsg(String msg) {
    showMessage(msg);
  }

  @Override
  public void startToDetail(BizQuotedTransferData bizQuotedTransferData) {
    startAct(getAty(), BizQuotedDetailActivity.class, bizQuotedTransferData);
  }

  @Override
  public void setRefreshDirection(SwipyRefreshLayoutDirection direction) {
    swipyRefreshLayout.setDirection(direction);
  }

  @Override
  public void onRefresh(SwipyRefreshLayoutDirection direction) {
    mPresent.refreshDirection(direction, this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
    mPresent.onDestroy();
  }
}
