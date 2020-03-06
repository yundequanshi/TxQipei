package com.txsh.quote.deport.present.Impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.baichang.android.widget.BCNoScrollListView;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.txsh.R;
import com.txsh.quote.Flag;
import com.txsh.quote.IBaseInteraction.BaseListener;
import com.txsh.quote.deport.QuotedDetailActivity;
import com.txsh.quote.deport.adapter.QuotedPriceAdapter;
import com.txsh.quote.deport.entity.QuotedListData;
import com.txsh.quote.deport.model.Impl.QuotedListInteractionImpl;
import com.txsh.quote.deport.model.QuotedListInteraction;
import com.txsh.quote.deport.present.QuotedDetailPresent;
import com.txsh.quote.deport.present.QuotedListPresent;
import com.txsh.quote.deport.view.QuotedListView;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.MLAppDiskCache;
import com.zuomei.constants.APIConstants;
import com.zuomei.model.MLLogin;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 汉玉 on 2017/3/15.
 */
public class QuotedListImpl implements QuotedListPresent {

  private QuotedListView mView;
  private QuotedListInteraction mInt;

  private QuotedPriceAdapter quotedPriceAdapter;
  private String depotId = "";
  private int nowPage = 1;
  private MLLogin mlLogin = new MLLogin();

  public QuotedListImpl(QuotedListView quotedListView) {
    this.mView = quotedListView;
    this.mInt = new QuotedListInteractionImpl();
    mlLogin = MLAppDiskCache.getLoginUser();
    depotId = mlLogin.Id;
  }

  @Override
  public void initListView(ListView listView, Activity activity) {
    quotedPriceAdapter = new QuotedPriceAdapter(activity, R.layout.bj_item_quoted_price);
    listView.setAdapter(quotedPriceAdapter);
    listView.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        QuotedListData quotedListData = (QuotedListData) parent.getItemAtPosition(position);
        mView.startToOtherActivity(QuotedDetailActivity.class,quotedListData);
      }
    });
    findDepotOfferSheet(activity);
  }

  @Override
  public void refresh(SwipyRefreshLayoutDirection direction, Activity activity) {
    if (direction == SwipyRefreshLayoutDirection.TOP) {
      nowPage = 1;
    } else {
      nowPage++;
    }
    findDepotOfferSheet(activity);
  }

  @Override
  public void refreshFindDepotOfferSheet(Activity activity) {
    nowPage = 1;
    findDepotOfferSheet(activity);
  }

  private void findDepotOfferSheet(Activity activity) {
    Map<String, String> map = new HashMap<>();
    map.put("depotId", depotId);
    map.put("nowPage", nowPage + "");
    mInt.findDepotOfferSheet(map, activity, new BaseListener<List<QuotedListData>>() {
      @Override
      public void success(List<QuotedListData> quotedListDatas) {
        if (nowPage == 1) {
          quotedPriceAdapter.setData(quotedListDatas);
        } else {
          quotedPriceAdapter.addData(quotedListDatas);
        }

        if (quotedListDatas.size() < Flag.DEFAULT_PAGE_SIZE) {
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
