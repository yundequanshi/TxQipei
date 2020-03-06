package com.txsh.home;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import cn.jpush.android.api.TagAliasCallback;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.quote.deport.QuotedListActivity;
import com.txsh.model.TXHomeImageRes;
import com.txsh.model.TXHomeTTRes;
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLHomeAdData;
import com.zuomei.model.MLHomeAdResponse;
import com.zuomei.model.MLHomeBusinessData;
import com.zuomei.model.MLLogin;
import com.zuomei.services.MLHomeServices;
import com.zuomei.utils.AutoTextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.ml.base.widget.slider1.AbSlidingPlayView;

/**
 * 首页界面
 *
 * @author Marcello
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TXHomeFrg extends BaseFragment {

  //首页广告位
  private List<MLHomeAdData> mAdDatas;
  @ViewInject(R.id.home_tv_phone)
  private TextView mTvPhone;

  @ViewInject(R.id.tx_frag_home_autotext)
  private AutoTextView tx_frag_home_autotext;

  private Context _context;
  @ViewInject(R.id.home_et_search)
  private EditText mEtSearch;

  @ViewInject(R.id.home_scrollview)
  private ScrollView mScrollview;


  @ViewInject(R.id.home_tv_sign)
  private ImageView mIvSign;
  // 轿车客车 通用产品 汽车用品 电摩五金 重汽轻卡 通用产品
  @ViewInject(R.id.tx_frah_home_Passenger_car)
  private ImageView mBtPassengerCar;
  @ViewInject(R.id.tx_frah_home_general)
  private ImageView mBtGeneral;
  @ViewInject(R.id.tx_frah_home_car_user)
  private ImageView mBtcarUser;
  @ViewInject(R.id.tx_frah_home_moto_five_king)
  private ImageView mBtFiveKing;
  @ViewInject(R.id.tx_frah_home_heavy_duty_truck)
  private ImageView mBtDutyTruck;
  @ViewInject(R.id.tx_frah_home_engineering)
  private ImageView mBtEngineering;

  //首页3张图片
  @ViewInject(R.id.index_business_bt)
  private ImageView mIvImage0;
  @ViewInject(R.id.home_iv_image1)
  private ImageView mIvImage1;
  @ViewInject(R.id.home_iv_image2)
  private ImageView mIvImage2;

  @ViewInject(R.id.qiandaochoujiang)
  private Button qiandaochoujiang;
  @ViewInject(R.id.refresh_layout)
  private AbPullToRefreshView _pullToRefreshLv;

  @ViewInject(R.id.head_left_bt2)
  private Button head_left_bt2;
  @ViewInject(R.id.house_info_banner_img)
  private AbSlidingPlayView _bannerimg;

  private MLLogin _user;
  private int i = 0;
  private int TIME = 10000;
  private HttpUtils httpUtils;
  private List<TouTiao> touTiaoList = new ArrayList<TouTiao>();
  private MLBizYouListInHomeFrg mlBizYouListInHomeFrg = null;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.tx_frag_home_first, null);
    ViewUtils.inject(this, view);
    httpUtils = new HttpUtils();
    _context = inflater.getContext();
    _user = ((BaseApplication) getActivity().getApplication()).get_user();
    head_left_bt2.setText(BaseApplication.aCache.getAsString("cityname"));
    if (BaseApplication.aCache.getAsString("cityid") != ""
        && BaseApplication.aCache.getAsString("cityname") != null) {
      BaseApplication._currentCity = BaseApplication.aCache.getAsString("cityid");
    } else {
      BaseApplication._currentCity = "1";
    }
    if (BaseApplication.aCache.getAsString("cityname") != ""
        && BaseApplication.aCache.getAsString("cityname") != null) {
      head_left_bt2.setText(BaseApplication.aCache.getAsString("cityname"));
    } else {
      head_left_bt2.setText("济南");
    }
    _pullToRefreshLv.setLoadMoreEnable(false);
    _pullToRefreshLv.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {

      @Override
      public void onHeaderRefresh(AbPullToRefreshView view) {
        if (mlBizYouListInHomeFrg != null) {
          mlBizYouListInHomeFrg.initData();
          new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              _pullToRefreshLv.onHeaderRefreshFinish();
            }
          }, 5000);
        }
      }
    });
    _pullToRefreshLv.setOnFooterLoadListener(new OnFooterLoadListener() {
      @Override
      public void onFooterLoad(AbPullToRefreshView abPullToRefreshView) {
        _pullToRefreshLv.onFooterLoadFinish();
      }
    });
    loadTouTiao();
    initImage();
    initData();
    initPlayView();
    initPush();
    initBizYou();
    return view;
  }

  /**
   * 优质商家
   */
  private void initBizYou() {
    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    mlBizYouListInHomeFrg = MLBizYouListInHomeFrg.instance();
    transaction.add(R.id.auxiliary_fl_content, mlBizYouListInHomeFrg,
        mlBizYouListInHomeFrg.getClass().getName()).commitAllowingStateLoss();
  }

  @OnClick(R.id.tx_frag_home_autotext)
  public void toutiaoOnClick(View view) {
    try {
      Intent intent = new Intent(getActivity(), TXTouTiao.class);
      intent.putExtra("id", String.valueOf(touTiaoList.get(i - 1).getId()));
      startActivity(intent);
    } catch (Exception e) {
    }
  }

  // 报价平台
  @OnClick(R.id.baojia)
  public void baojia(View view) {
    startAct(getFragment(), QuotedListActivity.class);
  }

  // 轿车客车
  @OnClick(R.id.tx_frah_home_Passenger_car)
  public void passengerCarOnClick(View view) {
    toActivity(getActivity(), MLConstants.MY_PART_CAR, "1");

  }

  // 通用产品
  @OnClick(R.id.tx_frah_home_general)
  public void homeGeneralOnClick(View view) {
    toActivity(getActivity(), MLConstants.MY_PART_CAR, "2");
  }

  // 汽车用品
  @OnClick(R.id.tx_frah_home_car_user)
  public void carUserOnClick(View view) {
    toActivity(getActivity(), MLConstants.MY_PART_CAR, "3");
  }

  // 电摩五金
  @OnClick(R.id.tx_frah_home_moto_five_king)
  public void motoFiveKingOnClick(View view) {
    toActivity(getActivity(), MLConstants.MY_PART_CAR, "4");
  }

  // 重汽轻卡
  @OnClick(R.id.tx_frah_home_heavy_duty_truck)
  public void heavrDutyTruckOnClick(View view) {
    toActivity(getActivity(), MLConstants.MY_PART_CAR, "5");
  }

  // 通用产品
  @OnClick(R.id.tx_frah_home_engineering)
  public void engineeringOnClick(View view) {
    toActivity(getActivity(), MLConstants.MY_PART_CAR, "6");
  }

  // 优质商家
  @OnClick(R.id.index_business_bt)
  public void index_business_btOnClick(View view) {
    toActivity(getActivity(), MLConstants.HOME_BUSINESS_YOU_LIST, null);
  }

  // 搜索
  @OnClick(R.id.head_right_bt)
  public void searchOnClick(View view) {
    toActivity(_context, MLConstants.HOME_SEARCH, null);
  }

  //定位
  @OnClick(R.id.head_left_bt2)
  public void dingweiOnClick(View view) {
    Intent intent = new Intent(getActivity(), CityActivity.class);
    startActivityForResult(intent, 1);
  }


  //积分商城
  @OnClick(R.id.home_jf)
  public void jfOnClick(View view) {
    startAct(TXHomeFrg.this, TXIntegralShopAty.class);
  }

  //特价专场
  @OnClick(R.id.home_ll_sale)
  public void saleOnClick(View view) {
    toActivity(getActivity(), MLConstants.HOME_SECOND_HAND_CAR, null);
//    startAct(TXHomeFrg.this, TxShopSaleAty.class);
  }

  //更多商家
  @OnClick(R.id.home_tv_more)
  public void hoteOnClick(View view) {
    toActivity(getActivity(), MLConstants.HOME_BUSINESS_YOU_LIST, null);
//    startAct(TXHomeFrg.this, TxShopHotAty.class);
  }

  //回到顶部
  @OnClick(R.id.interact_top)
  public void topOnClick(View view) {
    if (mScrollview != null) {
      mScrollview.scrollTo(0, 0);
    }
  }


  Handler handler = new Handler();
  Runnable runnable = new Runnable() {

    @Override
    public void run() {
      // handler自带方法实现定时器
      try {
        handler.postDelayed(this, TIME);

        if (i == touTiaoList.size()) {
          i = 0;
        }
        tx_frag_home_autotext.setText(touTiaoList.get(i).getTitle());
        i = i + 1;
        System.out.println("do...");
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        System.out.println("exception...");
      }
    }
  };

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
        String id = mAdDatas.get(position).userID;
        MLHomeBusinessData data = new MLHomeBusinessData();
        data.id = id;
        Intent intent = new Intent();
        intent.setClass(_context, MLAuxiliaryActivity.class);
        intent.putExtra("data", MLConstants.HOME_BUSINESS_INFO);
        intent.putExtra("obj", (Serializable) data);
        startActivity(intent);
      }
    });

    _bannerimg.setNavHorizontalGravity(Gravity.RIGHT);
    _bannerimg.setPageLineImage(show, hide);
  }

  public ImageView getImageView() {

    ImageView image = new ImageView(_context);
    image.setLayoutParams(new RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.MATCH_PARENT,
        RelativeLayout.LayoutParams.MATCH_PARENT));
    image.setScaleType(ScaleType.CENTER_CROP);
    return image;
  }

  private void initPush() {
    MLLogin user = ((BaseApplication) getActivity().getApplication()).get_user();

    //商户 c  汽修厂d
    String alias = "";
    //初始化推送 标签
    Set<String> set = new HashSet<String>();
    if (user.isDepot) {
      alias = "d";
      set.add("depot");
    } else {
      alias = "c";
      set.add("company");
    }
    JPushInterface.setAliasAndTags(_context, alias + user.Id, set, new TagAliasCallback() {
      @Override
      public void gotResult(int i, String s, Set<String> set) {
        Log.d("别名标签", s + "");
      }
    });
  }

  private void initData() {
    // 获取广告位
    ZMRequestParams adParam = new ZMRequestParams();
    adParam.addParameter(MLConstants.PARAM_HOME_CITYID, BaseApplication._currentCity);
    ZMHttpRequestMessage message3 = new ZMHttpRequestMessage(
        RequestType.HOME_AD, null, adParam, _handler, HTTP_RESPONSE_AD,
        MLHomeServices.getInstance());
    loadData(_context, message3);
  }


  //获取3张图片
  private void initImage() {
    ZMRequestParams adParam = new ZMRequestParams();
    ZMHttpRequestMessage message3 = new ZMHttpRequestMessage(
        RequestType.HOME_IMAGE, null, null, _handler, HTTP_RESPONSE_IMAGE,
        MLHomeServices.getInstance());

    loadData(_context, message3);
  }

  private static final int HTTP_RESPONSE_CITY = 0;
  private static final int HTTP_RESPONSE_CATALOG = 1;
  private static final int HTTP_RESPONSE_BUSINESS = 2;
  private static final int HTTP_RESPONSE_AD = 3;
  private static final int HTTP_RESPONSE_TT = 7;
  private static final int HTTP_RESPONSE_CALL = 4;
  private static final int HTTP_RESPONSE_IMAGE = 5;
  private static final int HTTP_RESPONSE_HOT = 6;
  private Handler _handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      // dismissProgressDialog();
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
        //获取广告位
        case HTTP_RESPONSE_AD: {
          MLHomeAdResponse ret = (MLHomeAdResponse) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {

            reviewAd(ret.datas);
          } else {
            showMessage("获取广告位失败!");
          }
          break;
        }
        //获取首页3张图片
        case HTTP_RESPONSE_IMAGE: {
          TXHomeImageRes ret = (TXHomeImageRes) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            reviewImage(ret.datas);
          } else {
            //showMessage("获取广告位失败!");
          }
          break;
        }

        case HTTP_RESPONSE_TT: {
          try {
            TXHomeTTRes ret = (TXHomeTTRes) msg.obj;
            if (!touTiaoList.isEmpty()) {
              touTiaoList.clear();
              tx_frag_home_autotext.setText("");
            }
            for (int i = 0; i < ret.datas.size(); i++) {
              TouTiao touTiao = new TouTiao();
              touTiao.setId(Integer.parseInt(ret.datas.get(i).id));
              touTiao.setTitle(ret.datas.get(i).title);
              touTiaoList.add(touTiao);
            }
            handler.postDelayed(runnable, 1000);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }

        default:
          break;
      }
    }
  };

  private void reviewImage(TXHomeImageRes.TXHomeImageData datas) {
    String imgUrl0 = APIConstants.API_IMAGE_SHOW + datas.goodbusinessman;
    if (!BaseApplication.IMAGE_CACHE.get(imgUrl0, mIvImage0)) {
      mIvImage0.setImageResource(R.drawable.youzhishangjiatuijian);
    }

    String imgUrl1 = APIConstants.API_IMAGE_SHOW + datas.Specialsale;
//    if (!BaseApplication.IMAGE_CACHE.get(imgUrl1, mIvImage1)) {
    mIvImage1.setImageResource(R.mipmap.index_img_store);
//    }

//    String imgUrl2 = APIConstants.API_IMAGE_SHOW + datas.Integralmall;
//    if (!BaseApplication.IMAGE_CACHE.get(imgUrl2, mIvImage2)) {
    mIvImage2.setImageResource(R.mipmap.img_store);
//    }
  }


  protected void reviewAd(List<MLHomeAdData> datas) {
    mAdDatas = datas;
    _bannerimg.removeAllViews();
    for (int i = 0; i < datas.size(); i++) {
      ImageView image = getImageView();
      _bannerimg.addView(image);
      String imgUrl = APIConstants.API_IMAGE + "?id=" + datas.get(i).imageUrl;
      image.setTag(imgUrl);
      if (!BaseApplication.IMAGE_CACHE.get(imgUrl, image)) {
        image.setImageDrawable(null);
      }
    }
    _bannerimg.stopPlay();
    _bannerimg.startPlay();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    // TODO Auto-generated method stub

    if (requestCode == 1 && resultCode == 1) {
      head_left_bt2.setText(data.getStringExtra("city"));
      if (mlBizYouListInHomeFrg != null) {
        mlBizYouListInHomeFrg.initData();
      }
      loadTouTiao();
      initData();
    }
  }

  private void loadTouTiao() {
    // 获取广告位
    ZMRequestParams adParam = new ZMRequestParams();
    adParam.addParameter("cityId", BaseApplication._currentCity);
    ZMHttpRequestMessage message3 = new ZMHttpRequestMessage(
        RequestType.API_QUERY, null, adParam, _handler, HTTP_RESPONSE_TT,
        MLHomeServices.getInstance());
    loadData(_context, message3);
  }

  class TouTiao {

    int id;
    String title;

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

  }
}
