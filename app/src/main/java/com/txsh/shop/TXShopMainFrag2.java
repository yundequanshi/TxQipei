package com.txsh.shop;

import android.content.Context;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import cn.ml.base.utils.MLStrUtil;
import cn.ml.base.widget.sample.MLNoScrollListView;
import cn.ml.base.widget.sample.MLScrollGridView;
import cn.ml.base.widget.slider1.AbSlidingPlayView;
import com.ab.view.pullview.AbPullToRefreshView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.adapter.TXShopTypeAdapter;
import com.txsh.adapter.TxShopMainAdapter;
import com.txsh.model.TXHomeImageRes;
import com.txsh.model.TXShopListRes;
import com.txsh.model.TXShopListRes.TXShopListData;
import com.txsh.model.TXShopPlayListRes;
import com.txsh.model.TXShopPlayListRes.TXHomeImageData;
import com.txsh.model.TXShopTypeListRes;
import com.txsh.model.TXShopTypeListRes.TXHomeGoodsTypeImageData;
import com.txsh.services.MLShopServices;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.APIConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLHomeAdData;
import com.zuomei.model.MLLogin;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcello on 2015/7/16.
 */
public class TXShopMainFrag2 extends BaseFragment {

  private Context _context;

  @ViewInject(R.id.shop_grid)
  private MLNoScrollListView mGridView;

  @ViewInject(R.id.head_right_bt)
  private ImageView mIvCar;
  @ViewInject(R.id.refresh_layout)
  private AbPullToRefreshView _pullToRefreshLv;

  @ViewInject(R.id.house_info_banner_img)
  private AbSlidingPlayView _bannerimg;
  @ViewInject(R.id.gv_type)
  private MLScrollGridView gvType;

  @ViewInject(R.id.home_scrollview)
  private ScrollView mScrollview;

  private TxShopMainAdapter mAdapter;
  private TXShopTypeAdapter txShopTypeAdapter;
  public List<TXShopListRes.TXShopListData> datas;
  public List<TXHomeImageData> txHomeImageDatas = new ArrayList<>();

  private int mDataSortType = 0;
  private int mDataIsDesc = 1;
  private int mDatanowPage = 1;
  //是否第一次点击销量排序
  private boolean mDescSales = true;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.tx_shop_main2, null);
    ViewUtils.inject(this, view);
    _context = inflater.getContext();

    initView();
    refreshData();
    initPlayView();
    initPlayData();
    initHomeGoodsType();
    return view;
  }

  private void initHomeGoodsType() {
    txShopTypeAdapter = new TXShopTypeAdapter(getActivity(), R.layout.item_shop_type);
    gvType.setAdapter(txShopTypeAdapter);
    gvType.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TXHomeGoodsTypeImageData data = (TXHomeGoodsTypeImageData) parent
            .getItemAtPosition(position);
        if (data.id.equals("-1")) {
          startAct(getFragment(), TXShopTypeActivity.class);
        } else {
          startAct(getFragment(), TXShopGoodsByTypeActivity.class, data);
        }
      }
    });
    ZMRequestParams params = new ZMRequestParams();
    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
        ZMHttpType.RequestType.TX_SHOP_FIND_HOMEGOODS_TYPE, null, null, _handler,
        HTTP_RESPONSE_FIND_HOMEGOODS_TYPE, MLShopServices.getInstance());
    loadData(getActivity(), message2);
  }

  public void refreshData() {
    ZMRequestParams params = new ZMRequestParams();
    params.addParameter("sortType", mDataSortType + "");
    params.addParameter("isDesc", mDataIsDesc + "");
    params.addParameter("cityId", BaseApplication._currentCity);
    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
        ZMHttpType.RequestType.TX_SHOP_PRODUCT_LIST, null, params, _handler,
        HTTP_RESPONSE_LIST, MLShopServices.getInstance());
    loadData(getActivity(), message2);
  }

  private void initData() {
    ZMRequestParams params = new ZMRequestParams();
    params.addParameter("sortType", mDataSortType + "");
    params.addParameter("isDesc", mDataIsDesc + "");
    params.addParameter("cityId", BaseApplication._currentCity);
    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
        ZMHttpType.RequestType.TX_SHOP_PRODUCT_LIST, null, params, _handler,
        HTTP_RESPONSE_LIST, MLShopServices.getInstance());
    loadDataWithMessage(getActivity(), "正在加载，请稍等...", message2);
  }

  private void pageData() {
    mDatanowPage++;
    ZMRequestParams params = new ZMRequestParams();

    params.addParameter("sortType", mDataSortType + "");
    params.addParameter("isDesc", mDataIsDesc + "");
    params.addParameter("nowPage", mDatanowPage + "");
    params.addParameter("cityId", BaseApplication._currentCity);

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
        TXShopListData tXShopListData = (new TXShopListRes()).new TXShopListData();
        tXShopListData.id = txHomeImageDatas.get(position).id;
        startAct(TXShopMainFrag2.this, TXShopDetailAty.class, tXShopListData);
      }
    });

    _bannerimg.setNavHorizontalGravity(Gravity.RIGHT);
    _bannerimg.setPageLineImage(show, hide);
  }

  public void initPlayData() {
    ZMRequestParams params = new ZMRequestParams();
    params.addParameter("cityId", BaseApplication._currentCity);
    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
        ZMHttpType.RequestType.TX_SHOP_PLAY_DATA, null, params, _handler,
        HTTP_RESPONSE_SHOP_PLAY_DATA, MLShopServices.getInstance());
    loadData(getActivity(), message2);
  }

  protected void reviewAd(List<TXHomeImageData> datas) {
    _bannerimg.removeAllViews();
    if (!txHomeImageDatas.isEmpty()) {
      txHomeImageDatas.clear();
    }
    if (datas.isEmpty()) {
      ImageView image = getImageView();
      _bannerimg.addView(image);
      image.setImageResource(R.drawable.banner);
    } else {
      txHomeImageDatas.addAll(datas);
      for (int i = 0; i < datas.size(); i++) {
        ImageView image = getImageView();
        _bannerimg.addView(image);
        String imgUrl = APIConstants.API_IMAGE_SHOW + datas.get(i).img;
        image.setTag(imgUrl);
        if (!BaseApplication.IMAGE_CACHE.get(imgUrl, image)) {
          image.setImageDrawable(null);
        }
      }
    }
    _bannerimg.stopPlay();
    _bannerimg.startPlay();
  }

  public ImageView getImageView() {
    ImageView image = new ImageView(_context);
    image.setLayoutParams(new RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.MATCH_PARENT,
        RelativeLayout.LayoutParams.MATCH_PARENT));
    image.setScaleType(ImageView.ScaleType.CENTER_CROP);
    return image;
  }

  private void initView() {
    MLLogin mUser = BaseApplication.getInstance().get_user();
    if (!mUser.isDepot) {
      mIvCar.setImageResource(R.drawable.nav_zengjia);
    }
    mAdapter = new TxShopMainAdapter(_context, R.layout.tx_item_shop_main_2);
    mGridView.setAdapter(mAdapter);

    mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startAct(TXShopMainFrag2.this, TXShopDetailAty.class, datas.get(position));
      }
    });

    _pullToRefreshLv.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {

      @Override
      public void onHeaderRefresh(AbPullToRefreshView view) {
        mDatanowPage = 1;
        refreshData();
        initHomeGoodsType();
        initPlayData();
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
      startAct(TXShopMainFrag2.this, TXShopCarAty.class);
    } else {
      //商家身份-发布商品
      startAct(TXShopMainFrag2.this, TXShopFaProductAty.class);
    }


  }

  @OnClick(R.id.shop_top)
  public void topOnClick(View view) {
    if (mScrollview != null) {
      mScrollview.scrollTo(0, 0);
    }
  }

  public void initChangeData() {
    try {
      mDataSortType = 0;
      mDataIsDesc = 1;
      mDatanowPage = 1;
      refreshData();
    } catch (Exception e) {
    }
  }

  @OnClick(R.id.top_btn_left)
  public void back(View view) {
    mDataIsDesc = 1;
    mDataSortType = 0;
    initData();
  }

  /**
   * 搜索
   */
  @OnClick(R.id.home_et_search)
  public void searchOnClick(View view) {
    startAct(getFragment(), TXShopSearchActivity.class);
  }

  private static final int HTTP_RESPONSE_LIST = 1;
  private static final int HTTP_RESPONSE_PAGE = 2;
  private static final int HTTP_RESPONSE_SHOP_PLAY_DATA = 3;
  private static final int HTTP_RESPONSE_FIND_HOMEGOODS_TYPE = 4;

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
        case HTTP_RESPONSE_SHOP_PLAY_DATA: {
          TXShopPlayListRes txShopPlayListRes = (TXShopPlayListRes) msg.obj;
          if (txShopPlayListRes.state.equalsIgnoreCase("1")) {
            reviewAd(txShopPlayListRes.datas);
          }
          break;
        }
        case HTTP_RESPONSE_FIND_HOMEGOODS_TYPE: {
          TXShopTypeListRes txShopPlayListRes = (TXShopTypeListRes) msg.obj;
          if (txShopPlayListRes.state.equalsIgnoreCase("1")) {
            TXHomeGoodsTypeImageData data = (new TXShopTypeListRes()).new TXHomeGoodsTypeImageData();
            data.id = "-1";
            data.name = "更多";
            txShopPlayListRes.datas.add(data);
            txShopTypeAdapter.setData(txShopPlayListRes.datas);
          }
          break;
        }
        default:
          break;
      }
    }
  };
}
