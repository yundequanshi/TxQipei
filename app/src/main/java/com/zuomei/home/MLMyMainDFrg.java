package com.zuomei.home;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.ml.base.utils.MLDialogUtils;
import com.baichang.android.utils.BCAppUpdateManager;
import com.baichang.android.utils.BCDialogUtil;
import com.baichang.android.utils.BCToolsUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.comment.TXCmWebAty;
import com.txsh.comment.TXHomeActivity;
import com.txsh.me.TXSupplyMyAty;
import com.txsh.model.TXShopSumRes;
import com.txsh.model.TxCmWebData;
import com.txsh.shop.TXShopOrderAllAty;
import com.txsh.shop.TXShopOrderStateAty;
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.login.MLLoginActivity;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLMyInfoData;
import com.zuomei.model.MLMyInfoResponse;
import com.zuomei.model.MLMyShareData;
import com.zuomei.model.MLVersionApp;
import com.zuomei.services.MLMessageServices;
import com.zuomei.services.MLMyServices;
import com.zuomei.utils.MLShareUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.ml.base.utils.IEvent;
import cn.ml.base.utils.MLDataCleanManager;
import cn.ml.base.utils.MLStrUtil;
import cn.ml.base.widget.roundedimageview.RoundedImageView;

/**
 * 我的 （汽修厂）
 *
 * @author Marcello
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyMainDFrg extends BaseFragment {

  public static MLMyMainDFrg INSTANCE = null;

  public static MLMyMainDFrg instance() {
    INSTANCE = new MLMyMainDFrg();
    return INSTANCE;
  }

  /**
   * 商家
   */
  @ViewInject(R.id.my_ly_business)
  private RelativeLayout _businessInfo;

  @ViewInject(R.id.me_tv_name)
  private TextView _nameTv;

  @ViewInject(R.id.my_rl_stock)
  private RelativeLayout _stockRl;

  @ViewInject(R.id.my_iv_business)
  private ImageView _headIv;

  @ViewInject(R.id.me_iv_icon)
  private RoundedImageView mIvIcon;

  @ViewInject(R.id.me_tv_phone)
  private TextView mTvPhone;
  @ViewInject(R.id.me_tv_jf)
  private TextView mTvJf;
  @ViewInject(R.id.me_tv_yue)
  private TextView mTvYue;
  private Context _context;
  private MLMyInfoData _info;

  @ViewInject(R.id.shop_sum1)
  private TextView mTvSum1;

  @ViewInject(R.id.shop_sum2)
  private TextView mTvSum2;

  @ViewInject(R.id.shop_sum3)
  private TextView mTvSum3;
  @ViewInject(R.id.tv_update_app)
  private TextView tvUpdateApp;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.my_main, null);
    ViewUtils.inject(this, view);

    _context = inflater.getContext();

    initView();
    return view;
  }

  private void initView() {
    // 获取商家的基本信息
    MLLogin data = ((BaseApplication) getActivity().getApplication())
        .get_user();
    ZMRequestParams catalogParam = new ZMRequestParams();
    catalogParam.addParameter(MLConstants.PARAM_LOGIN_DEPORTID, data.Id);
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(
        RequestType.MY_INFO_D, null, catalogParam, _handler,
        HTTP_RESPONSE_INFO, MLMyServices.getInstance());
    loadDataWithMessage(_context, null, message1);
  }


  private void initSum() {
    MLLogin data = ((BaseApplication) getActivity().getApplication())
        .get_user();
    ZMRequestParams catalogParam = new ZMRequestParams();
    catalogParam.addParameter("depotId", data.Id);
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(
        RequestType.TX_SHOP_ORDER_SUM, null, catalogParam, _handler,
        HTTP_RESPONSE_SUM, MLMyServices.getInstance());
    loadDataWithMessage(_context, null, message1);
  }

  /**
   * @description 商家基本信息
   * @author marcello
   */
  @OnClick(R.id.me_rl_info)
  public void businessOnClick(View view) {
    // starActivity(MLConstants.MY_BUSINESS_INFO,_info);
    Intent intent = new Intent();
    intent.setClass(_context, MLAuxiliaryActivity.class);
    intent.putExtra("data", MLConstants.MY_BUSINESS_INFO);
    intent.putExtra("obj", (Serializable) _info);
    startActivityForResult(intent, 1);
  }

  @Override
  public void onResume() {
    super.onResume();
    initView();
    initSum();
  }

  /**
   * @description 用户量
   * @author marcello
   */
  @OnClick(R.id.my_rl_user)
  public void userOnClick(View view) {
    starActivity(MLConstants.MY_USER);
  }

  /**
   * @description 清除缓存
   * @author marcello
   */
  @OnClick(R.id.my_rl_huancun)
  public void my_rl_huancunOnClick(View view) {
    showProgressDialog("正在清除缓存...", getActivity());
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        MLDataCleanManager.cleanExternalCache(getActivity());
        MLDataCleanManager.cleanCustomCache(BaseApplication.DEFAULT_CACHE_FOLDER);
        dismissProgressDialog();
        showMessage("清除缓存成功");
      }
    }, 2000);
  }

  /**
   * @description 交易记录
   * @author marcello
   */
  @OnClick(R.id.my_rl_pay)
  public void payOnClick(View view) {
    starActivity(MLConstants.MY_BILL_D);
  }

  /**
   * @description 联系我们
   * @author marcello
   */
  @OnClick(R.id.my_rl_contact)
  public void contactOnClick(View view) {
    //starActivity(MLConstants.MY_CONTACT);
    TxCmWebData data = new TxCmWebData();
    data.title = "联系我们";
    data.url = APIConstants.API_DEFAULT_HOST + "/mobile3/contant";
    startAct(MLMyMainDFrg.this, TXCmWebAty.class, data);
  }

  @OnClick(R.id.my_btn_exit)
  public void exitOnClick(View view) {

    BCDialogUtil.showDialog(getActivity(), R.color.convert_tv_state1, "提示", "确定退出当前账号吗？",
        new OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            Set<String> tags = new HashSet<String>();
            JPushInterface.setAliasAndTags(_context, "", tags, null);

            Intent intent = new Intent();
            intent.putExtra("data", "exit");
            intent.setClass(_context, MLLoginActivity.class);
            startActivity(intent);
            ((TXHomeActivity) _context).finish();
            BaseApplication.aCache.put(MLConstants.PARAM_REGISTER_PWD, "");
          }
        }, null);
  }

  /**
   * @description 通话记录
   * @author marcello
   */
  @OnClick(R.id.my_rl_phone)
  public void phoneOnClick(View view) {
    starActivity(MLConstants.MY_PHONE_D);
  }

  /**
   * @description 供求信息
   * @author marcello
   */
  @OnClick(R.id.my_rl_supply)
  public void supplyOnClick(View view) {
    startAct(MLMyMainDFrg.this, TXSupplyMyAty.class);
  }


  /**
   * @description 我的收藏
   * @author marcello
   */
  @OnClick(R.id.my_rl_collect)
  public void collectOnClick(View view) {
    starActivity(MLConstants.MY_COLLECT);
  }

  /**
   * @description 优惠信息
   * @author marcello
   */
  @OnClick(R.id.my_rl_favorable)
  public void favorOnClick(View view) {
    starActivity(MLConstants.MY_SPECIAL_LIST);
    // starActivity(MLConstants.MY_SPECIAL_DETAIL);
  }


  /**
   * @description 全部订单
   * @author marcello
   */
  @OnClick(R.id.my_rl_order)
  public void orderOnClick(View view) {
    startAct(MLMyMainDFrg.this, TXShopOrderAllAty.class);

  }


  @OnClick({R.id.shop_rl_1, R.id.shop_rl_2, R.id.shop_rl_3})
  public void allOnClick(View view) {
    if (view.getId() == R.id.shop_rl_1) {
      startAct(MLMyMainDFrg.this, TXShopOrderStateAty.class, "1");
    } else if (view.getId() == R.id.shop_rl_2) {
      startAct(MLMyMainDFrg.this, TXShopOrderStateAty.class, "2");
    } else if (view.getId() == R.id.shop_rl_3) {
      startAct(MLMyMainDFrg.this, TXShopOrderStateAty.class, "3");
    }
  }


  /**
   * @description 进货列表
   * @author marcello
   */
  @OnClick(R.id.my_rl_stock)
  public void stockOnClick(View view) {
    starActivity(MLConstants.MY_STOCK);
  }

  /**
   * @description 汽修列表
   * @author marcello
   */
  @OnClick(R.id.my_rl_repair)
  public void repairOnClick(View view) {
    starActivity(MLConstants.MY_REPAIR);
  }

  /**
   * @description 实用工具
   * @author marcello
   */
  @OnClick(R.id.my_rl_tool)
  public void toolsOnClick(View view) {
    starActivity(MLConstants.MY_TOOLS);
  }

  /**
   * @description 事故车配件
   * @author marcello
   */
  @OnClick(R.id.my_rl_accidentpart)
  public void partOnClick(View view) {
    starActivity(MLConstants.MY_ACCIDENT_PART);
  }

  /**
   * @description 返利纪录
   * @author marcello
   */
  @OnClick(R.id.my_rl_money)
  public void moneyOnClick(View view) {
    starActivity(MLConstants.MY_MONEY);
  }

  //便民资讯
  @OnClick(R.id.rl_bianmin)
  public void bianmianOnClick(View view) {
    startAct(getFragment(), ConvenienceActivity.class);
  }

  /**
   * @description 我的事故车
   * @author marcello
   */
  @OnClick(R.id.my_rl_accident)
  public void accidentOnClick(View view) {
    starActivity(MLConstants.MY_ACCIDENT);
  }

  //分享
  @OnClick(R.id.my_rl_fenxiang)
  public void fenxiangOnClick(View view) {
    ZMRequestParams catalogParam = new ZMRequestParams();

    //  catalogParam.addParameter(MLConstants.PARAM_LOGIN_DEPORTID, data.Id);
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(
        RequestType.MY_SHARE, null, null, _handler,
        MY_SHARERETURN, MLMyServices.getInstance());

    loadDataWithMessage(_context, null, message1);
  }

  @OnClick(R.id.rl_update_app)
  private void checkAppVersion(View view) {
    checkAppVersionUpdate(BCToolsUtil.getVersionName(getActivity()));
  }

  private void checkAppVersionUpdate(String versionName) {
    ZMRequestParams catalogParam = new ZMRequestParams();
    catalogParam.addParameter("version", versionName);
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(
        RequestType.CHECK_APPVERSION_UPDATE, null, catalogParam, _handler,
        HTTP_RESPONSE_CHECK_APP, MLMyServices.getInstance());
    loadDataWithMessage(_context, null, message1);
  }


  /**
   * @description 我的二手件
   * @author marcello
   */
  @OnClick(R.id.my_rl_part)
  public void leaveOnClick(View view) {
    starActivity(MLConstants.MY_LEAVE);
  }

  private void starActivity(int position) {
    Intent intent = new Intent();
    intent.setClass(_context, MLAuxiliaryActivity.class);
    intent.putExtra("data", position);
    startActivity(intent);
  }

  private void starActivity(int position, Object obj) {
    Intent intent = new Intent();
    intent.setClass(_context, MLAuxiliaryActivity.class);
    intent.putExtra("data", position);
    intent.putExtra("obj", (Serializable) obj);
    startActivity(intent);
  }

  private static final int HTTP_RESPONSE_INFO = 0;
  private static final int MY_SHARERETURN = 1;
  private static final int HTTP_RESPONSE_SUM = 2;
  private static final int HTTP_RESPONSE_CHECK_APP = 3;

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
        case HTTP_RESPONSE_INFO: {
          MLMyInfoResponse ret = (MLMyInfoResponse) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            _info = ret.datas;

            setHead();
          } else {
            showMessage("获取头像失败!");
          }
          break;
        }
        case MY_SHARERETURN: {
          MLMyShareData ret = (MLMyShareData) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            MLShareUtils.showShare(_context, null, ret.datas.title, ret.datas.remark,
                APIConstants.API_DEFAULT_HOST + "/" + ret.datas.url);


          } else {
            showMessage("获取分享信息失败!");
          }
          break;
        }

        case HTTP_RESPONSE_CHECK_APP: {
          MLVersionApp ret = (MLVersionApp) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            if (BCToolsUtil.getVersionName(getActivity()).equals(ret.datas.version)) {
              showMessage("暂无最新版本!");
            } else {
              new BCAppUpdateManager(getActivity(), ret.datas.path,
                  "最新版本" + ret.datas.version, false).checkUpdateInfo(R.color.float_transparent);
            }
          } else {
            showMessage("暂无最新版本!");
          }
          break;
        }

        case HTTP_RESPONSE_SUM: {
          TXShopSumRes ret = (TXShopSumRes) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            if (!MLStrUtil.compare(ret.datas.daiFu, "0")) {
              mTvSum1.setVisibility(View.VISIBLE);
              mTvSum1.setText(ret.datas.daiFu);
            }

            if (!MLStrUtil.compare(ret.datas.daiShou, "0")) {
              mTvSum2.setVisibility(View.VISIBLE);
              mTvSum2.setText(ret.datas.daiShou);
            }

            if (!MLStrUtil.compare(ret.datas.yiShou, "0")) {
              mTvSum3.setVisibility(View.VISIBLE);
              mTvSum3.setText(ret.datas.yiShou);
            }

          } else {

          }

          break;
        }

        default:
          break;
      }
    }
  };

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    // TODO Auto-generated method stub
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == MLConstants.RESULT_BUSINESS) {
      initView();
    }
  }

  private IEvent<Object> _event;

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    _event = (IEvent<Object>) activity;
  }

  protected void setHead() {
    // // 头像
    // if (_info.userPhoto <= 0) {
    // mIvIcon.setImageResource(R.drawable.default_my_info_head);
    // } else {
    // String iconUrl = APIConstants.API_IMAGE + "?id=" + _info.userPhoto;
    // bitmapUtils.display(mIvIcon, iconUrl, bigPicDisplayConfig);
    // }

    // 头像
    if (_info.userPhoto <= 0) {
      mIvIcon.setImageResource(R.drawable.wodetouxiang);
    } else {
      String iconUrl = APIConstants.API_IMAGE + "?id=" + _info.userPhoto;
      mIvIcon.setTag(iconUrl);
      if (!BaseApplication.IMAGE_CACHE.get(iconUrl, mIvIcon)) {
        mIvIcon.setImageResource(R.drawable.wodetouxiang);
      }
    }
    // 名字
    _nameTv.setText(_info.depotName);
    mTvPhone.setText(_info.userPhone);
    mTvJf.setText(Html.fromHtml(String
        .format("<font color=\"#000000\" >%s</font><font color=\"#ff0000\">%s</font>", "积分：",
            _info.integral)));
    mTvYue.setText(Html.fromHtml(String
        .format("<font color=\"#000000\" >%s</font><font color=\"#ff0000\">%s</font>", "余额：",
            _info.integral)));
    tvUpdateApp.setText("检查版本 v" + BCToolsUtil.getVersionName(getActivity()));
  }
}
