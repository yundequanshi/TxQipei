package com.txsh.shop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;

import android.widget.ScrollView;
import cn.ml.base.widget.sample.MLScrollGridView;
import cn.ml.base.widget.slider1.AbSlidingPlayView;
import com.ab.view.pullview.AbPullToRefreshView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.adapter.TxShopMainAdapter;
import com.txsh.model.TXShopListRes;
import com.txsh.services.MLShopServices;
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLHomeAdData;
import com.zuomei.model.MLHomeBusinessData;
import com.zuomei.model.MLLogin;

import java.io.Serializable;
import java.util.List;

import cn.ml.base.utils.MLStrUtil;

/**
 * Created by Marcello on 2015/7/16.
 */
public class TXShopMainFrag extends BaseFragment {

  private Context _context;

  @ViewInject(R.id.shop_grid)
  private MLScrollGridView mGridView;

  @ViewInject(R.id.home_et_search)
  private EditText mEtSearch;

  @ViewInject(R.id.head_right_bt)
  private ImageView mIvCar;
  @ViewInject(R.id.refresh_layout)
  private AbPullToRefreshView _pullToRefreshLv;

  @ViewInject(R.id.part_rb_tab1)
  private RadioButton mBtnTab1;

  @ViewInject(R.id.house_info_banner_img)
  private AbSlidingPlayView _bannerimg;

  @ViewInject(R.id.home_scrollview)
  private ScrollView mScrollview;

  private TxShopMainAdapter mAdapter;

  public List<TXShopListRes.TXShopListData> datas;
  private List<MLHomeAdData> mAdDatas;

  private String mDatakey;
  private int mDataSortType = 0;
  private int mDataIsDesc = 1;
  private int mDatanowPage = 1;
  //是否第一次点击销量排序
  private boolean mDescSales = true;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.tx_shop_main, null);
    ViewUtils.inject(this, view);
    _context = inflater.getContext();

    initView();
    refreshData();
    initPlayView();
    return view;
  }


  private void initData() {
    ZMRequestParams params = new ZMRequestParams();
    if (!MLStrUtil.isEmpty(mDatakey)) {
      params.addParameter("key", mDatakey);
    }

    params.addParameter("sortType", mDataSortType + "");
    params.addParameter("isDesc", mDataIsDesc + "");
    //params.addParameter("nowPage",mDatakey);

    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
        ZMHttpType.RequestType.TX_SHOP_PRODUCT_LIST, null, params, _handler,
        HTTP_RESPONSE_LIST, MLShopServices.getInstance());
    loadDataWithMessage(getActivity(), "正在加载，请稍等...", message2);
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

    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
        ZMHttpType.RequestType.TX_SHOP_PRODUCT_LIST, null, params, _handler,
        HTTP_RESPONSE_PAGE, MLShopServices.getInstance());
    loadData(getActivity(), message2);
  }

  private void initPlayView() {

    Bitmap show = BitmapFactory.decodeResource(_context.getResources(), R.drawable.bannerfanye1);
    Bitmap hide = BitmapFactory.decodeResource(_context.getResources(), R.drawable.bannerfanye2);

    _bannerimg.setOnTouchListener(new AbSlidingPlayView.AbOnTouchListener() {
      @Override
      public void onTouch(MotionEvent event) {
        _bannerimg.requestDisallowInterceptTouchEvent(true);
      }
    });
    _bannerimg.setOnItemClickListener(new AbSlidingPlayView.AbOnItemClickListener() {

      @Override
      public void onClick(int position) {

      }
    });

    _bannerimg.setNavHorizontalGravity(Gravity.RIGHT);
    _bannerimg.setPageLineImage(show, hide);
  }

  private void refreshData() {
    ZMRequestParams params = new ZMRequestParams();
    if (!MLStrUtil.isEmpty(mDatakey)) {
      params.addParameter("key", mDatakey);
    }

    params.addParameter("sortType", mDataSortType + "");
    params.addParameter("isDesc", mDataIsDesc + "");
    //params.addParameter("nowPage",mDatakey);

    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
        ZMHttpType.RequestType.TX_SHOP_PRODUCT_LIST, null, params, _handler,
        HTTP_RESPONSE_LIST, MLShopServices.getInstance());
    loadData(getActivity(), message2);
  }


  private void initView() {
    MLLogin mUser = BaseApplication.getInstance().get_user();
    if (!mUser.isDepot) {
      mIvCar.setImageResource(R.drawable.nav_zengjia);
    }
    mAdapter = new TxShopMainAdapter(_context, R.layout.tx_item_shop_main_1);
    mGridView.setAdapter(mAdapter);

    mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startAct(TXShopMainFrag.this, TXShopDetailAty.class, datas.get(position));
      }
    });

    _pullToRefreshLv.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {

      @Override
      public void onHeaderRefresh(AbPullToRefreshView view) {
        mDatanowPage = 1;
        refreshData();
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

  @OnClick(R.id.head_right_bt)
  public void carOnClick(View view) {
    MLLogin mUser = BaseApplication.getInstance().get_user();
    if (mUser.isDepot) {
      //用户身份-购物车
      startAct(TXShopMainFrag.this, TXShopCarAty.class);
    } else {
      //商家身份-发布商品
      startAct(TXShopMainFrag.this, TXShopFaProductAty.class);
    }


  }


  /**
   * 搜索
   */
  @OnClick(R.id.btn_search)
  public void searchOnClick(View view) {
    mDatakey = mEtSearch.getText().toString();
    sort();
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

  @OnClick(R.id.shop_top)
  public void topOnClick(View view) {
    if (mScrollview != null) {
      mScrollview.scrollTo(0, 0);
    }
  }

  public void sort() {
    if (mDataIsDesc == 0) {
      mDataIsDesc = 1;
    } else {
      mDataIsDesc = 0;
    }
  }

  @OnClick(R.id.top_btn_left)
  public void back(View view) {
    mDataIsDesc = 1;
    mBtnTab1.setChecked(true);
    mDatakey = "";
    mEtSearch.setText("");
    mDataSortType = 0;
    initData();
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
            //  review();
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
          } else {

          }
          _pullToRefreshLv.onFooterLoadFinish();
          break;
        }
        default:
          break;
      }
    }
  };
}
