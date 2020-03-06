package com.txsh.quote.deport;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout.OnRefreshListener;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.txsh.R;
import com.txsh.quote.CommonActivity;
import com.txsh.quote.Flag;
import com.txsh.quote.deport.entity.QuotedListData;
import com.txsh.quote.deport.present.Impl.QuotedListImpl;
import com.txsh.quote.deport.present.QuotedListPresent;
import com.txsh.quote.deport.view.QuotedListView;
import com.zuomei.base.MLEventBusModel;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class QuotedListActivity extends CommonActivity implements QuotedListView,
    OnRefreshListener {

  private ListView lvPrice;
  private SwipyRefreshLayout mRefreshLayout;

  private QuotedListPresent mPresent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bj_quoted_price);
    init();
  }

  private void init() {
    EventBus.getDefault().register(this);
    mPresent = new QuotedListImpl(this);
    lvPrice = (ListView) findViewById(R.id.lv_price);
    mRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyRefreshLayout);
    mPresent.initListView(lvPrice, this);
    mRefreshLayout.setColorSchemeColors(Color.parseColor("#00B4FF"));
    mRefreshLayout.setOnRefreshListener(this);
  }

  public void publishOnClick(View view) {
    startAct(getAty(), PublishActivity.class);
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void event(MLEventBusModel model) {
    if (model.type == Flag.EVENT_QUOTED_LIST_REFRESH) {
      mPresent.refreshFindDepotOfferSheet(this);
    }
  }

  @Override
  public void showProgressBar() {
  }

  @Override
  public void hideProgressBar() {
    mRefreshLayout.setRefreshing(false);
  }

  @Override
  public void showMsg(String msg) {
    showMessage(msg);
  }

  @Override
  public void startToOtherActivity(Class cls, QuotedListData quotedListData) {
    startAct(getAty(), cls, quotedListData);
  }

  @Override
  public void setRefreshDirection(SwipyRefreshLayoutDirection direction) {
    mRefreshLayout.setDirection(direction);
  }

  @Override
  public void onRefresh(SwipyRefreshLayoutDirection direction) {
    mPresent.refresh(direction, this);
  }

  @Override
  protected void onStart() {
    super.onStart();
    mPresent.onStart();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mPresent.onDestroy();
    EventBus.getDefault().unregister(this);
  }
}
