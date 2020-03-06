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

import com.baichang.android.utils.BCAppUpdateManager;
import com.baichang.android.utils.BCDialogUtil;
import com.baichang.android.utils.BCStringUtil;
import com.baichang.android.utils.BCToolsUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.comment.TXCmWebAty;
import com.txsh.comment.TXHomeActivity;
import com.txsh.me.TXSupplyMyAty;
import com.txsh.model.TxCmWebData;
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.login.MLLoginActivity;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLMyInfoData;
import com.zuomei.model.MLMyInfoResponse;
import com.zuomei.model.MLMyShareData;
import com.zuomei.model.MLVersionApp;
import com.zuomei.services.MLMyServices;
import com.zuomei.utils.MLShareUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.ml.base.utils.IEvent;
import cn.ml.base.utils.MLDataCleanManager;
import cn.ml.base.widget.roundedimageview.RoundedImageView;

/**
 * 我的 （汽配城）
 *
 * @author Marcello
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyMainBFrg extends BaseFragment {

  public static MLMyMainBFrg INSTANCE = null;
  private MLMyInfoData _info;

  public static MLMyMainBFrg instance() {
    INSTANCE = new MLMyMainBFrg();
    return INSTANCE;
  }

  /**
   * 商家
   */
  @ViewInject(R.id.my_ly_business)
  private RelativeLayout _businessInfo;

  @ViewInject(R.id.my_iv_business)
  private ImageView _headIv;
  @ViewInject(R.id.my_rl_stock)
  private RelativeLayout _stockRl;

  @ViewInject(R.id.me_tv_name)
  private TextView _nameTv;

  @ViewInject(R.id.tv_time)
  private TextView _timeTv;

  private Context _context;

  @ViewInject(R.id.me_iv_icon)
  private RoundedImageView mIvIcon;

  @ViewInject(R.id.me_tv_phone)
  private TextView mTvPhone;
  @ViewInject(R.id.me_tv_jf)
  private TextView mTvJf;
  @ViewInject(R.id.me_tv_yue)
  private TextView mTvYue;
  @ViewInject(R.id.iv_dengji)
  private ImageView ivDengji;
  @ViewInject(R.id.tv_update_app)
  private TextView tvUpdateApp;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.my_main_b, null);
    ViewUtils.inject(this, view);
    _context = inflater.getContext();
    return view;
  }

  // 获取商家的基本信息
  private void initView() {
    MLLogin data = ((BaseApplication) getActivity().getApplication())
        .get_user();
    ZMRequestParams catalogParam = new ZMRequestParams();
    catalogParam.addParameter(MLConstants.PARAM_HOME_BUSINESSID1, data.Id);
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(
        RequestType.MY_INFO_B, null, catalogParam, _handler,
        HTTP_RESPONSE_INFO, MLMyServices.getInstance());
    loadDataWithMessage(_context, null, message1);
  }

  @Override
  public void onResume() {
    super.onResume();
    initView();
  }

  @OnClick(R.id.my_rl_product_manger)
  public void productManger(View view) {
    startAct(getFragment(), ProductMangerActivity.class);
  }

  /**
   * @description 商家基本信息
   * @author marcello
   */
  @OnClick(R.id.my_ly_business)
  public void businessOnClick(View view) {
    Intent intent = new Intent();
    intent.setClass(_context, MLAuxiliaryActivity.class);
    intent.putExtra("data", MLConstants.MY_BUSINESS_INFO);
    intent.putExtra("obj", (Serializable) _info);
    startActivityForResult(intent, 1);
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
   * @description 商品管理
   * @author marcello
   */
  @OnClick(R.id.my_rl_product)
  public void productOnClick(View view) {
    starActivity(MLConstants.MY_PRODUCT);
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
   * @description 优惠信息详情
   * @author marcello
   */
  @OnClick(R.id.my_rl_favorable)
  public void favorOnClick(View view) {
    starActivity(MLConstants.MY_SPECIAL_LIST);
  }

  /**
   * @description 供求信息
   * @author marcello
   */
  @OnClick(R.id.my_rl_supply)
  public void supplyOnClick(View view) {
    startAct(MLMyMainBFrg.this, TXSupplyMyAty.class);
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
   * @description 联系我们
   * @author marcello
   */
  @OnClick(R.id.my_rl_contact)
  public void contactOnClick(View view) {
    TxCmWebData data = new TxCmWebData();
    data.title = "联系我们";
    data.url = APIConstants.API_DEFAULT_HOST + "/mobile3/contant";
    startAct(MLMyMainBFrg.this, TXCmWebAty.class, data);
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
   * @description 进货列表
   * @author marcello
   */
  @OnClick(R.id.my_rl_stock)
  public void stockOnClick(View view) {
    starActivity(MLConstants.MY_STOCK);
  }

  /**
   * @description 用户量
   * @author marcello
   */
  @OnClick(R.id.my_rl_user)
  public void userOnClick(View view) {
    starActivity(MLConstants.MY_USER);
  }

  //便民资讯
  @OnClick(R.id.rl_bianmin)
  public void bianmianOnClick(View view) {
    startAct(getFragment(), ConvenienceActivity.class);
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
   * @description 返利纪录
   * @author marcello
   */
  @OnClick(R.id.my_rl_money)
  public void moneyOnClick(View view) {
    starActivity(MLConstants.MY_MONEY);
  }

  /**
   * @description 红包管理
   * @author marcello
   */
  @OnClick(R.id.my_rl_packet)
  public void packetOnClick(View view) {
    // starActivity(MLConstants.MY_MONEY);
    starActivity(MLConstants.MY_PACKET);
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

  /**
   * @description 我的事故车
   * @author marcello
   */
  @OnClick(R.id.my_rl_accident)
  public void accidentOnClick(View view) {
    starActivity(MLConstants.MY_ACCIDENT);
  }

  /**
   * @description 我的事故车
   * @author marcello
   */
  @OnClick(R.id.my_rl_part)
  public void partOnClick(View view) {
    starActivity(MLConstants.MY_LEAVE);
  }

  /**
   * @description 报价管理
   * @author marcello
   */
  @OnClick(R.id.my_rl_part_offer)
  public void offerOnClick(View view) {
    starActivity(MLConstants.MY_PART_OFFER);
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

  private void starActivity(int position) {
    Intent intent = new Intent();
    intent.setClass(_context, MLAuxiliaryActivity.class);
    intent.putExtra("data", position);
    startActivity(intent);
  }

  private static final int HTTP_RESPONSE_INFO = 0;
  private static final int MY_SHARERETURN = 1;
  private static final int HTTP_RESPONSE_CHECK_APP = 2;
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
        default:
          break;
      }
    }
  };

  protected void setHead() {

    if (!BCStringUtil.isEmpty(_info.level)) {
      ivDengji.setVisibility(View.VISIBLE);
      if (Integer.parseInt(_info.level) == 1) {
        ivDengji.setImageResource(R.mipmap.msg_members_1);
      } else if (Integer.parseInt(_info.level) == 2) {
        ivDengji.setImageResource(R.mipmap.msg_members_2);
      } else if (Integer.parseInt(_info.level) == 3) {
        ivDengji.setImageResource(R.mipmap.msg_members_3);
      } else if (Integer.parseInt(_info.level) == 4) {
        ivDengji.setImageResource(R.mipmap.msg_members_4);
      }
    } else {
      ivDengji.setVisibility(View.GONE);
    }

    // 头像
    if (_info.userPhoto <= 0) {
      mIvIcon.setImageResource(R.drawable.default_my_info_head);
    } else {
      String iconUrl = APIConstants.API_IMAGE + "?id=" + _info.userPhoto;
      mIvIcon.setTag(iconUrl);
      if (!BaseApplication.IMAGE_CACHE.get(iconUrl, mIvIcon)) {
        mIvIcon.setImageDrawable(null);
      }

    }
    // //名字
    _nameTv.setText(_info.depotName);
    _timeTv.setText(_info.startDate + "至" + _info.endDate);
    mTvPhone.setText(_info.userPhone);
    String money = _info.money == null ? "0" : _info.money;
    mTvJf.setText(Html.fromHtml(String
        .format(
            "<font color=\"#000000\" >%s</font><font color=\"#ff0000\">%s</font>  <font color=\"#000000\" >%s</font><font color=\"#ff0000\">%s</font>",
            "余额：",
            money, "信誉值：", _info.gradeIntegral)));
    tvUpdateApp.setText("检查版本 v" + BCToolsUtil.getVersionName(getActivity()));
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    // TODO Auto-generated method stub
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == MLConstants.RESULT_BUSINESS) {
      initView();
    }
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


  private IEvent<Object> _event;

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    _event = (IEvent<Object>) activity;
  }
}
