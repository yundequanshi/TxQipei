package com.txsh.shop;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import cn.ml.base.utils.MLStrUtil;
import cn.ml.base.widget.sample.MLNoScrollListView;
import com.ab.view.pullview.AbPullToRefreshView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
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

public class TXShopSearchActivity extends BaseActivity {

  @ViewInject(R.id.home_et_search)
  private EditText mEtSearch;
  @ViewInject(R.id.shop_grid)
  private ListView mGridView;
  @ViewInject(R.id.refresh_layout)
  private AbPullToRefreshView _pullToRefreshLv;

  private TxShopMainAdapter mAdapter;
  private String mDatakey;
  private int mDataSortType = 0;
  private int mDataIsDesc = 1;
  private int mDatanowPage = 1;
  private List<TXShopListData> datas;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_txshop_search);
    ViewUtils.inject(this);
    mAdapter = new TxShopMainAdapter(getAty(), R.layout.tx_item_shop_main_2);
    mGridView.setAdapter(mAdapter);
    mGridView.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TXShopListData data = (TXShopListData) parent.getItemAtPosition(position);
        startAct(getAty(), TXShopDetailAty.class, data);
      }
    });
    initData();
    _pullToRefreshLv.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {

      @Override
      public void onHeaderRefresh(AbPullToRefreshView view) {
        mDatanowPage = 1;
        initData();
      }
    });

    _pullToRefreshLv.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {

      @Override
      public void onFooterLoad(AbPullToRefreshView view) {
        // TODO Auto-generated method stub
        pageData();
      }
    });
  }

  /**
   * 搜索
   */
  @OnClick(R.id.btn_search)
  public void searchOnClick(View view) {
    mDatakey = mEtSearch.getText().toString();
    initData();
  }

  /**
   * 默认排序
   */
  @OnClick(R.id.part_rb_tab1)
  public void tab1OnClick(View view) {
    mDataSortType = 0;
    sort();
    initData();
  }

  /**
   * 价格排序
   */
  @OnClick(R.id.part_rb_tab2)
  public void tab2OnClick(View view) {
    mDataSortType = 1;
    sort();
    initData();
  }

  /**
   * 销量排序
   */
  @OnClick(R.id.part_rb_tab3)
  public void tab3OnClick(View view) {
    mDataSortType = 2;
    if (mDataSortType != 2) {
      mDataIsDesc = 1;
    } else {
      sort();
    }
    initData();
  }

  public void sort() {
    if (mDataIsDesc == 0) {
      mDataIsDesc = 1;
    } else {
      mDataIsDesc = 0;
    }
  }

  private void initData() {
    ZMRequestParams params = new ZMRequestParams();
    if (!MLStrUtil.isEmpty(mDatakey)) {
      params.addParameter("key", mDatakey);
    }
    params.addParameter("sortType", mDataSortType + "");
    params.addParameter("isDesc", mDataIsDesc + "");
    params.addParameter("cityId", BaseApplication._currentCity);
    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
        ZMHttpType.RequestType.FIND_PRODUCT_BY_KEY, null, params, _handler,
        HTTP_RESPONSE_LIST, MLShopServices.getInstance());
    loadDataWithMessage(getAty(), "正在加载，请稍等...", message2);
  }

  private void pageData() {
    mDatanowPage++;
    ZMRequestParams params = new ZMRequestParams();
    if (!MLStrUtil.isEmpty(mDatakey)) {
      params.addParameter("key", mDatakey);
    }
    params.addParameter("sortType", mDataSortType + "");
    params.addParameter("isDesc", mDataIsDesc + "");
    params.addParameter("nowPage", mDatanowPage + "");
    params.addParameter("cityId", BaseApplication._currentCity);

    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
        ZMHttpType.RequestType.FIND_PRODUCT_BY_KEY, null, params, _handler,
        HTTP_RESPONSE_PAGE, MLShopServices.getInstance());
    loadData(getAty(), message2);
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
          _pullToRefreshLv.onHeaderRefreshFinish();
          break;
        }

        case HTTP_RESPONSE_PAGE: {
          TXShopListRes ret = (TXShopListRes) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            datas.addAll(ret.datas);
            mAdapter.setData(datas);
            if (ret.datas.size() < 20) {
              _pullToRefreshLv.setLoadMoreEnable(false);
            }
          }
          _pullToRefreshLv.onFooterLoadFinish();
          break;
        }
        default:
          break;
      }
    }
  };

  public void back(View view) {
    finish();
  }
}
