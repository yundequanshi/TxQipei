package com.txsh.shop;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baichang.android.utils.BCStringUtil;
import com.easemob.easeui.model.HxUser;
import com.easemob.easeui.utils.Contants;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.friend.ChatAty;
import com.txsh.model.TXShopBuyRes;
import com.txsh.model.TXShopDetailRes;
import com.txsh.model.TXShopListRes;
import com.txsh.services.MLShopServices;
import com.txsh.utils.MLScrollWebView;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.home.MLHomeProductPop;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLHomeServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.ml.base.utils.MLStrUtil;
import cn.ml.base.widget.slider1.AbSlidingPlayView;

/**
 * Created by Marcello on 2015/7/16.
 */
public class TXShopDetailAty extends BaseActivity {

  @ViewInject(R.id.detail_tv_name)
  private TextView mTvName;
  @ViewInject(R.id.detail_tv_price)
  private TextView mTvPrice;
  @ViewInject(R.id.detail_tv_priceold)
  private TextView mTvPriceOld;
  @ViewInject(R.id.detail_tv_area)
  private TextView mTvArea;
  @ViewInject(R.id.detail_tv_kc)
  private TextView mTvKc;
  @ViewInject(R.id.detail_tv_yf)
  private TextView mTvYf;
  @ViewInject(R.id.detail_tv_xl)
  private TextView mTvXl;
  @ViewInject(R.id.detail_tv_pj)
  private TextView mTvPj;
  @ViewInject(R.id.detail_tv_busname)
  private TextView mTvBusName;
  @ViewInject(R.id.detail_tv_address)
  private TextView mTvAddress;
  @ViewInject(R.id.detail_iv_icon)
  private ImageView mIvLogo;
  @ViewInject(R.id.shop_web)
  private MLScrollWebView mWebView;
  @ViewInject(R.id.detail_iv_slide)
  private AbSlidingPlayView mPlayView;
  @ViewInject(R.id.root)
  private RelativeLayout _root;
  //数量
  @ViewInject(R.id.shopcar_item_ed)
  private EditText mEtCount;

  @ViewInject(R.id.detail_rl_bottom)
  private LinearLayout mLlBottom;
  @ViewInject(R.id.head_right_bt)
  private ImageView mIvCar;

  private MLLogin mUser;

  private TXShopListRes.TXShopListData mData;
  private TXShopDetailRes.TXShopDetailData mDataDetail;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.tx_shop_detail);
    ViewUtils.inject(this);

    mData = (TXShopListRes.TXShopListData) getIntentData();
    mUser = BaseApplication.getInstance().get_user();
    initStatus();
    initData();
  }

  private void initStatus() {
    MLLogin mUser = BaseApplication.getInstance().get_user();
    if (!mUser.isDepot) {
      mIvCar.setVisibility(View.GONE);
      mLlBottom.setVisibility(View.GONE);
    }
  }

  private void initView() {
    mTvName.setText(mDataDetail.name);

    if (MLStrUtil.compare(mDataDetail.privce, "0.00")) {
      mTvPrice.setText("电话联系");
    } else {

      mTvPrice.setText("¥" + mDataDetail.privce);
    }

    if (MLStrUtil.isEmpty(mDataDetail.oldprivce) || MLStrUtil
        .compare(mDataDetail.oldprivce, "0.00")) {
      mTvPriceOld.setText("");
    } else {
      mTvPriceOld.setText("¥" + mDataDetail.oldprivce);
    }

    mTvPriceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    mTvArea.setText(mDataDetail.cityName);
    mTvKc.setText("库存：" + mDataDetail.count);
    mTvBusName.setText(mDataDetail.companyName);
    mTvAddress.setText(mDataDetail.companyAddress);

    String freight = "";
    if (MLStrUtil.compare(mDataDetail.freight, "0.00")) {
      freight = "包邮";
      mTvYf.setText(Html.fromHtml(String
          .format("<font color=\"#979797\">运费</font><br><font color=\"#FF5000\">%s</font>",
              freight)));
    } else {
      freight = mDataDetail.freight;
      mTvYf.setText(
          Html.fromHtml(String.format("<font color=\"#979797\">运费</font><br>%s", freight)));
    }

    mTvXl.setText(
        Html.fromHtml(String.format("<font color=\"#979797\">销量</font><br>%s", mDataDetail.sales)));
    mTvPj.setText(Html.fromHtml(
        String.format("<font color=\"#979797\">评价</font><br>%s", mDataDetail.evaluationCount)));
    String imgUrl = APIConstants.API_IMAGE + "?id=" + mDataDetail.companyLogo;
    mIvLogo.setTag(imgUrl);
    if (!BaseApplication.IMAGE_CACHE.get(imgUrl, mIvLogo)) {
      mIvLogo.setImageResource(R.drawable.default_message_header);
    }
    mWebView
        .loadUrl(APIConstants.API_DEFAULT_HOST + "/mobile3/mallProduct/view?id=" + mDataDetail.id);
    initPlayView();
  }

  /**
   * 幻灯
   */
  private List<String> mDataUrl;

  private void initPlayView() {
    mDataUrl = new ArrayList<String>();
    try {
      JSONArray array = new JSONArray(mDataDetail.images);
      for (int i = 0; i < array.length(); i++) {
        JSONObject j = new JSONObject(array.get(i).toString());
        ImageView image = getImageView();
        mPlayView.addView(image);
        String imgUrl = APIConstants.API_IMAGE_SHOW + j.getString("path");
        mDataUrl.add(imgUrl);
        image.setTag(imgUrl);
        if (!BaseApplication.IMAGE_CACHE.get(imgUrl, image)) {
          image.setImageDrawable(null);
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    Bitmap show = BitmapFactory.decodeResource(getResources(), R.drawable.bannerfanye1);
    Bitmap hide = BitmapFactory.decodeResource(getResources(), R.drawable.bannerfanye2);
    mPlayView.setOnTouchListener(new AbSlidingPlayView.AbOnTouchListener() {
      @Override
      public void onTouch(MotionEvent event) {
        mPlayView.requestDisallowInterceptTouchEvent(true);
      }
    });
    mPlayView.setOnItemClickListener(new AbSlidingPlayView.AbOnItemClickListener() {
      @Override
      public void onClick(int position) {
        MLHomeProductPop _pop = new
            MLHomeProductPop(TXShopDetailAty.this, mDataUrl, position);
        _pop.showAtLocation(_root, Gravity.CENTER, 0, 0);
      }
    });
    mPlayView.setNavHorizontalGravity(Gravity.RIGHT);
    mPlayView.setPageLineImage(show, hide);
  }

  //底部拨打电话
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  @OnClick({R.id.detail_ll_phone})
  public void phoneOnClick(View view) {

    AlertDialog builder = new AlertDialog.Builder(TXShopDetailAty.this,
        AlertDialog.THEME_HOLO_LIGHT).setTitle("提示")
        .setMessage("拨打 " + mDataDetail.companyPhone)
        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent(Intent.ACTION_CALL,
                Uri.parse("tel:" + mDataDetail.companyPhone));
            startActivity(intent);
            dial("0");
          }
        })
        .setNegativeButton("取消", null)
        .show();
  }

  @OnClick({R.id.ll_sixin})
  public void sixinOnClick(View view) {
    if (!BCStringUtil.isEmpty(mDataDetail.hxUser)) {
      HxUser mHxUser = new HxUser();
      mHxUser.emId = mDataDetail.hxUser;
      mHxUser.name = mDataDetail.companyName;
      mHxUser.userId = mDataDetail.companyId;
      Intent intent = new Intent(getAty(), ChatAty.class);
      intent.putExtra(Contants.EXTRA_USER, mHxUser);
      startActivity(intent);
    }
  }

  //减
  @OnClick(R.id.shopcar_item_subtract)
  public void subOnClick(View view) {
    String countString = mEtCount.getText().toString();
    if (MLStrUtil.isEmpty(countString) || !MLStrUtil.isNumber(countString) || MLStrUtil
        .compare(countString, "1")) {
      return;
    }
    int count = Integer.parseInt(countString);
    count--;
    mEtCount.setText(count + "");
  }

  //加
  @OnClick(R.id.shopcar_item_add)
  public void addOnClick(View view) {
    String countString = mEtCount.getText().toString();
    if (MLStrUtil.isEmpty(countString) || !MLStrUtil.isNumber(countString)) {
      return;
    }
    int count = Integer.parseInt(countString);
    count++;
    mEtCount.setText(count + "");
  }

  /**
   * 购物车
   */
  @OnClick(R.id.detail_tv_gwc)
  public void carOnClick(View view) {
    addCar();
  }

  @OnClick(R.id.detail_rel_companyId)
  public void companyOnClick(View view) {
    startAct(TXShopDetailAty.this, TxShopProductListAty.class, mDataDetail.companyId);
  }


  // 评价
  @OnClick(R.id.detail_tv_pj)
  public void pjOnClick(View view) {
    startAct(TXShopDetailAty.this, TXShopPJAty.class, mData.id);
  }

  @OnClick(R.id.head_right_bt)
  public void goCarOnClick(View view) {
    startAct(TXShopDetailAty.this, TXShopCarAty.class);
  }

  /**
   * 立即购买
   */
  @OnClick(R.id.detail_tv_busbuy)
  public void buyOnClick(View view) {
    //   startAct(TXShopDetailAty.this, TXShopOrderSumAty.class);
    buy();
  }


  public ImageView getImageView() {
    ImageView image = new ImageView(TXShopDetailAty.this);
    image.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
        RelativeLayout.LayoutParams.MATCH_PARENT));
    image.setScaleType(ImageView.ScaleType.CENTER_CROP);
    return image;
  }


  private void initData() {
    ZMRequestParams params = new ZMRequestParams();
    params.addParameter("id", mData.id + "");

    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
        ZMHttpType.RequestType.TX_SHOP_PRODUCT_DETAIL, null, params, _handler,
        HTTP_RESPONSE_DETAIL, MLShopServices.getInstance());
    loadDataWithMessage(TXShopDetailAty.this, "正在加载，请稍等...", message2);
  }

  private void buy() {
    if (mUser == null) {
      return;
    }
    String count = mEtCount.getText().toString();
    if (MLStrUtil.isEmpty(count) || MLStrUtil.compare(count, "0")) {
      showMessage("请填写商品数量");
      return;
    }
    ZMRequestParams params = new ZMRequestParams();
    params.addParameter("depotId", mUser.Id);
    params.addParameter("productId", mData.id);
    params.addParameter("productNumber", count);

    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
        ZMHttpType.RequestType.TX_SHOP_PRODUCT_BUY, null, params, _handler,
        HTTP_RESPONSE_BUY, MLShopServices.getInstance());
    loadDataWithMessage(TXShopDetailAty.this, "正在加载，请稍等...", message2);
  }


  private void addCar() {
    if (MLStrUtil.compare(mDataDetail.privce, "0.00")) {
      showMessage("价格电联商品请直接购买");
      return;
    }

    String count = mEtCount.getText().toString();
    if (MLStrUtil.isEmpty(count)) {
      showMessage("请先填写数量");
      return;
    }

    ZMRequestParams params = new ZMRequestParams();
    params.addParameter("depotId", mUser.Id);
    params.addParameter("productId", mData.id);
    params.addParameter("number", count);

    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_SHOP_CAR_ADD,
        null, params, _handler,
        HTTP_RESPONSE_CAR, MLShopServices.getInstance());
    loadDataWithMessage(TXShopDetailAty.this, "正在加载，请稍等...", message2);
  }


  @OnClick(R.id.top_btn_left)
  public void backOnClick(View view) {
    finish();
  }

  private void dial(String isNetWorkPhone) {

    MLLogin user = BaseApplication.getInstance().get_user();
    if (!user.isDepot) {
      return;
    }
    ZMRequestParams catalogParam = new ZMRequestParams();
    catalogParam.addParameter(MLConstants.PARAM_HOME_ISNETWORKPHONE,
        isNetWorkPhone);
    catalogParam.addParameter(MLConstants.PARAM_LOGIN_DEPORTID, user.Id);
    catalogParam.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,
        mDataDetail.companyId);
    catalogParam.addParameter(MLConstants.PARAM_HOME_DEPOTPHONE,
        BaseApplication.aCache.getAsString(MLConstants.PARAM_REGISTER_USERNAME));
    catalogParam.addParameter(MLConstants.PARAM_HOME_COMPANYPHONE,
        mDataDetail.companyPhone);
    catalogParam.addParameter("phoneTime", "1");

    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(
        ZMHttpType.RequestType.HOME_CALL, null, catalogParam, _handler,
        HTTP_RESPONSE_CALL, MLHomeServices.getInstance());
    loadDataWithMessage(this, null, message1);
  }


  private static final int HTTP_RESPONSE_DETAIL = 1;
  private static final int HTTP_RESPONSE_CALL = 2;
  private static final int HTTP_RESPONSE_CAR = 3;
  private static final int HTTP_RESPONSE_BUY = 4;

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
        case HTTP_RESPONSE_DETAIL: {
          TXShopDetailRes ret = (TXShopDetailRes) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            mDataDetail = ret.datas;
            initView();
          } else {
            showMessageError("初始化失败!");
          }

          break;
        }
        case HTTP_RESPONSE_CAR: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            showMessage("加入购物车成功");
          } else {
            showMessage("商品已加入购物车");
          }
          break;
        }

        case HTTP_RESPONSE_BUY: {
          TXShopBuyRes ret = (TXShopBuyRes) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            startAct(TXShopDetailAty.this, TXShopOrderSumAty.class, ret.datas);
          } else {
            showMessage("购买失败");
          }
          break;
        }

        default:
          break;
      }

    }
  };


}
