package com.txsh.quote.business.present.Impl;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.txsh.R;
import com.txsh.quote.Flag;
import com.txsh.quote.IBaseInteraction.BaseListener;
import com.txsh.quote.business.adapter.BizAlreadyQuotedListAdapter;
import com.txsh.quote.business.adapter.BizWaitQuotedListAdapter;
import com.txsh.quote.business.entity.BizQuotedListData;
import com.txsh.quote.business.entity.BizQuotedTransferData;
import com.txsh.quote.business.model.BizQuotedInteraction;
import com.txsh.quote.business.model.Impl.BizQuotedInterationImpl;
import com.txsh.quote.business.present.BizQuotedListPresent;
import com.txsh.quote.business.view.BizQuotedListView;
import com.zuomei.base.MLAppDiskCache;
import com.zuomei.model.MLLogin;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 汉玉 on 2017/3/15.
 */
public class BizQuotedListPresentImpl implements BizQuotedListPresent {

  private BizQuotedInteraction mInt;
  private BizQuotedListView mView;

  private BizWaitQuotedListAdapter bizWaitQuotedListAdapter;
  private BizAlreadyQuotedListAdapter bizAlreadyQuotedListAdapter;
  private MLLogin mlLogin = new MLLogin();
  private String companyId = "";
  private String state = "1";
  private int nowPage = 1;

  public BizQuotedListPresentImpl(BizQuotedListView bizQuotedListView) {
    this.mView = bizQuotedListView;
    this.mInt = new BizQuotedInterationImpl();
    mlLogin = MLAppDiskCache.getLoginUser();
    companyId = mlLogin.Id;
  }

  @Override
  public void showWaitPrice(ListView listview, Activity activity) {
    bizWaitQuotedListAdapter = new BizWaitQuotedListAdapter(activity, R.layout.bj_item_wait_price);
    listview.setAdapter(bizWaitQuotedListAdapter);
    listview.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BizQuotedListData bizQuotedListData = (BizQuotedListData) parent
            .getItemAtPosition(position);
        BizQuotedTransferData bizQuotedTransferData = new BizQuotedTransferData();
        bizQuotedTransferData.companyId = companyId;
        bizQuotedTransferData.quoteId = bizQuotedListData.id;
        mView.startToDetail(bizQuotedTransferData);
      }
    });
    nowPage = 1;
    state = "1";
    findCompanyOfferSheet(state, nowPage + "", activity);
  }

  @Override
  public void showAlreadyPrice(ListView listview, Activity activity) {
    bizAlreadyQuotedListAdapter = new BizAlreadyQuotedListAdapter(activity,
        R.layout.bj_item_already_price);
    listview.setAdapter(bizAlreadyQuotedListAdapter);
    listview.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BizQuotedListData bizQuotedListData = (BizQuotedListData) parent
            .getItemAtPosition(position);
        BizQuotedTransferData bizQuotedTransferData = new BizQuotedTransferData();
        bizQuotedTransferData.companyId = companyId;
        bizQuotedTransferData.quoteId = bizQuotedListData.id;
        mView.startToDetail(bizQuotedTransferData);
      }
    });
    nowPage = 1;
    state = "2";
    findCompanyOfferSheet(state, nowPage + "", activity);
  }

  @Override
  public void refreshDirection(SwipyRefreshLayoutDirection direction, Activity activity) {
    if (direction == SwipyRefreshLayoutDirection.TOP) {
      nowPage = 1;
    } else {
      nowPage++;
    }
    findCompanyOfferSheet(state, nowPage + "", activity);
  }

  @Override
  public void refreshData(Activity activity) {
    findCompanyOfferSheet(state, nowPage + "", activity);
  }

  private void findCompanyOfferSheet(final String state, final String nowPage, Activity activity) {
    Map<String, String> map = new HashMap<>();
    map.put("companyId", companyId);
    map.put("state", state);
    map.put("nowPage", nowPage);
    mInt.findCompanyOfferSheet(map, activity, new BaseListener<List<BizQuotedListData>>() {
      @Override
      public void success(List<BizQuotedListData> bizQuotedListDatas) {
        if (state.equals("1")) {
          if (nowPage.equals("1")) {
            bizWaitQuotedListAdapter.setData(bizQuotedListDatas);
          } else {
            bizWaitQuotedListAdapter.addData(bizQuotedListDatas);
          }
        } else {
          if (nowPage.equals("1")) {
            bizAlreadyQuotedListAdapter.setData(bizQuotedListDatas);
          } else {
            bizAlreadyQuotedListAdapter.addData(bizQuotedListDatas);
          }
        }
        if (bizQuotedListDatas.size() < Flag.DEFAULT_PAGE_SIZE) {
          mView.setRefreshDirection(SwipyRefreshLayoutDirection.TOP);
        } else {
          mView.setRefreshDirection(SwipyRefreshLayoutDirection.BOTH);
        }
        mView.hideProgressBar();
      }

      @Override
      public void error(String error) {

      }
    });
  }

  @Override
  public void onDestroy() {
    mView = null;
  }

  @Override
  public void onStart() {

  }
}
