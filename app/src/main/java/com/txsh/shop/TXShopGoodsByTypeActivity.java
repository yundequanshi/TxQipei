package com.txsh.shop;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.ml.base.utils.MLStrUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.txsh.adapter.TxShopMainAdapter;
import com.txsh.model.TXShopListRes;
import com.txsh.model.TXShopListRes.TXShopListData;
import com.txsh.model.TXShopPlayListRes;
import com.txsh.model.TXShopTypeListRes;
import com.txsh.model.TXShopTypeListRes.TXHomeGoodsTypeImageData;
import com.txsh.services.MLShopServices;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import java.util.List;

public class TXShopGoodsByTypeActivity extends BaseActivity {

  @ViewInject(R.id.refresh_layout)
  private AbPullToRefreshView abPullToRefreshView;
  @ViewInject(R.id.lv_shop)
  private ListView lvShop;

  private TXHomeGoodsTypeImageData fromData;
  private TxShopMainAdapter mAdapter;
  private int mDatanowPage = 1;
  public List<TXShopListData> datas;
  private String typeId = "";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_txshop_goods_by_type);
    ViewUtils.inject(this);
    if (getIntentData() != null) {
      fromData = (TXHomeGoodsTypeImageData) getIntentData();
      typeId = fromData.id;
    }
    mAdapter = new TxShopMainAdapter(getAty(), R.layout.tx_item_shop_main_2);
    lvShop.setAdapter(mAdapter);
    lvShop.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TXShopListData mData = (TXShopListData) parent.getItemAtPosition(position);
        startAct(getAty(), TXShopDetailAty.class, mData);
      }
    });
    abPullToRefreshView.setOnHeaderRefreshListener(
        new com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener() {

          @Override
          public void onHeaderRefresh(com.ab.view.pullview.AbPullToRefreshView view) {
            mDatanowPage = 1;
            refreshData();
          }
        });

    abPullToRefreshView.setOnFooterLoadListener(
        new com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener() {

          @Override
          public void onFooterLoad(com.ab.view.pullview.AbPullToRefreshView view) {
            pageData();
          }
        });
    refreshData();
  }

  private void refreshData() {
    ZMRequestParams params = new ZMRequestParams();

    params.addParameter("typeId", typeId + "");
    params.addParameter("cityId", BaseApplication._currentCity);
    params.addParameter("nowPage", mDatanowPage + "");

    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
        ZMHttpType.RequestType.TX_SHOP_PRODUCT_BY_TYPE, null, params, _handler,
        HTTP_RESPONSE_LIST, MLShopServices.getInstance());
    loadData(getAty(), message2);
  }

  private void pageData() {
    mDatanowPage++;
    ZMRequestParams params = new ZMRequestParams();

    params.addParameter("typeId", typeId + "");
    params.addParameter("cityId", BaseApplication._currentCity);
    params.addParameter("nowPage", mDatanowPage + "");

    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
        ZMHttpType.RequestType.TX_SHOP_PRODUCT_BY_TYPE, null, params, _handler,
        HTTP_RESPONSE_PAGE, MLShopServices.getInstance());
    loadData(getAty(), message2);
  }

  public void back(View view) {
    finish();
  }

  private static final int HTTP_RESPONSE_LIST = 1;
  private static final int HTTP_RESPONSE_PAGE = 2;

  private Handler _handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      dismissProgressDialog();
      if (msg == null || msg.obj == null) {
        showMessage(R.string.loading_data_failed);
        return;
      }
      if (msg.obj instanceof ZMHttpError) {
        ZMHttpError error = (ZMHttpError) msg.obj;
        showMessage(error.errorMessage);
        return;
      }
      switch (msg.what) {
        case HTTP_RESPONSE_LIST: {
          TXShopListRes ret = (TXShopListRes) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            datas = ret.datas;
            mAdapter.setData(ret.datas);
          } else {
            showMessageError("初始化失败!");
          }
          abPullToRefreshView.onHeaderRefreshFinish();
          break;
        }

        case HTTP_RESPONSE_PAGE: {
          TXShopListRes ret = (TXShopListRes) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            datas.addAll(ret.datas);
            mAdapter.setData(datas);
            if (ret.datas.size() < 20) {
              abPullToRefreshView.setLoadMoreEnable(false);
            }
          }
          abPullToRefreshView.onFooterLoadFinish();
          break;
        }
        default:
          break;
      }
    }
  };
}
