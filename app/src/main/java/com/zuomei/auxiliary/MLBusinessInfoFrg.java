package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baichang.android.utils.BCStringUtil;
import com.easemob.easeui.model.HxUser;
import com.easemob.easeui.utils.Contants;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.txsh.R;
import com.txsh.friend.ChatAty;
import com.txsh.home.TXWeiWangZhan;
import com.txsh.model.TXProductModel;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.home.MLHomeCityPop;
import com.zuomei.home.MLHomeProductPop;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLAddDeal;
import com.zuomei.model.MLDepotPayInfoData;
import com.zuomei.model.MLDepotPayInfoResponse;
import com.zuomei.model.MLHomeBusiness1Data;
import com.zuomei.model.MLHomeBusinessData;
import com.zuomei.model.MLHomeBusinessDetail1;
import com.zuomei.model.MLHomeCityData;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLPayAlipayData;
import com.zuomei.model.MLRegister;
import com.zuomei.model.MLSpecialResonse;
import com.zuomei.services.MLHomeServices;
import com.zuomei.services.MLPayServices;
import com.zuomei.utils.MLPayUtils;
import com.zuomei.utils.MLShareUtils;
import com.zuomei.utils.MLToolUtil;
import com.zuomei.utils.MathUtils;
import com.zuomei.utils.alipay.PayResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ml.base.utils.IEvent;
import cn.ml.base.utils.MLStrUtil;
import cn.ml.base.widget.slider1.AbSlidingPlayView;


/**
 * 商家信息
 *
 * @author Marcello
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLBusinessInfoFrg extends BaseFragment {

  public static MLBusinessInfoFrg INSTANCE = null;
  public static MLHomeBusinessData _oldBusiness;
  public static MLHomeBusiness1Data _business;

  public static MLBusinessInfoFrg instance(Object obj) {
    _oldBusiness = (MLHomeBusinessData) obj;
    // if(INSTANCE==null){
    INSTANCE = new MLBusinessInfoFrg();
    // }
    return INSTANCE;
  }

  @ViewInject(R.id.business_scrollview)
  private ScrollView _scrollview;

  @ViewInject(R.id.rl_root)
  private RelativeLayout _root;

  @ViewInject(R.id.business_ll_declaration)
  private RelativeLayout _declarationRl;

  /**
   * 顶部图片
   */
  @ViewInject(R.id.detail_iv_slide)
  private AbSlidingPlayView mIvSlide;

  /** 收藏按钮 */
  /*
   * @ViewInject(R.id.business_ib_collect) private Button _collectBtn;
	 */

  /**
   * 名称
   */
  @ViewInject(R.id.business)
  private TextView _nameTv;
  /**
   * 电话
   */
  @ViewInject(R.id.business_tv_phone)
  private TextView _phoneTv;

  /**
   * 评论
   */
  @ViewInject(R.id.business_comment)
  private TextView _commentTv;

  /**
   * 企业宣言
   */
  @ViewInject(R.id.business_tv_declaration)
  private TextView _declarationTv;
  /**
   * 地址
   */
  @ViewInject(R.id.business_tv_address)
  private TextView _addressTv;
  /**
   * 交易次数
   */
  @ViewInject(R.id.business_tv_count)
  private TextView _countTv;
  /**
   * 主营
   */
  @ViewInject(R.id.business_tv_majorOperate)
  private TextView _majorOperateTv;
  /**
   * 兼营
   */
  @ViewInject(R.id.business_tv_concurrenOperate)
  private TextView _concurrenOperateTv;
  /**
   * 更多产品
   */
  @ViewInject(R.id.business_btn_more)
  private Button _moreBtn;

  /**
   * 支付信息
   */
  @ViewInject(R.id.pay_rl_pay)
  private RelativeLayout _payRl;

  @ViewInject(R.id.business_rl_product)
  private RelativeLayout _productRl;

  @ViewInject(R.id.pay_rl_money)
  private RelativeLayout _moneyRl;

  @ViewInject(R.id.business_btn_pay)
  private Button _payBtn;
  @ViewInject(R.id.business_ib_collect)
  private Button business_ib_collect;

  @ViewInject(R.id.business_degree)
  private TextView _degreeTv;

  @ViewInject(R.id.image_1)
  private ImageView image_1;

  @ViewInject(R.id.image_2)
  private ImageView image_2;

  @ViewInject(R.id.image_3)
  private ImageView image_3;

  @ViewInject(R.id.image_text_1)
  private TextView image_text_1;

  @ViewInject(R.id.image_text_2)
  private TextView image_text_2;

  @ViewInject(R.id.image_text_3)
  private TextView image_text_3;
  @ViewInject(R.id.dianzan_text)
  private TextView dianzan_text;
  @ViewInject(R.id.gengduochanpin)
  private TextView gengduochanpin;
  @ViewInject(R.id.chanpinzhanshi)
  private LinearLayout chanpinzhanshi;
  @ViewInject(R.id.detail_phone_bottom)
  private TextView zhifu;
  @ViewInject(R.id.iv_dengji)
  private ImageView ivDengji;


  private View mView;
  private MLBusinessInfoAdapter _productAdapter;
  private Context _context;
  private MLLogin _user;
  private String isPointLike;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    _context = inflater.getContext();
    _user = ((BaseApplication) getActivity().getApplication()).get_user();
    mView = inflater.inflate(R.layout.qp_busines_detail_main, null);
    ViewUtils.inject(this, mView);
    if (_user.isDepot) {
      business_ib_collect.setVisibility(View.VISIBLE);
      zhifu.setVisibility(View.VISIBLE);
    } else {
      business_ib_collect.setVisibility(View.GONE);
      zhifu.setVisibility(View.GONE);
    }
    initPlayView();
    initData();
    return mView;
  }


  private void initData() {
    MLLogin user = ((BaseApplication) getActivity().getApplication())
        .get_user();
    ZMRequestParams catalogParam = new ZMRequestParams();
    // 判断是否来自收藏列表页面
    if (_oldBusiness.isCollectParam) {
      catalogParam.addParameter(MLConstants.PARAM_HOME_BUSINESSID,
          _oldBusiness.compayId);
    } else {
      catalogParam.addParameter(MLConstants.PARAM_HOME_BUSINESSID,
          _oldBusiness.id);
    }
    catalogParam.addParameter(MLConstants.PARAM_MY_DEPTID2, user.Id);
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(
        RequestType.HOME_BUSINESS_DETAIL, null, catalogParam, _handler,
        HTTP_RESPONSE_BUSINESS, MLHomeServices.getInstance());
    loadDataWithMessage(_context, null, message1);
  }

  /*
   * private Drawable collect_n ; private Drawable collect_f ;
   */
  private Drawable drawable;


  @Override
  public void onStart() {
    // TODO Auto-generated method stub
    super.onStart();
    drawable = getResources().getDrawable(R.drawable.business_stars);
  }

  private void initView() {
    if (_business == null) {
      return;
    }

    if (!BCStringUtil.isEmpty(_business.gradeNum)) {
      ivDengji.setVisibility(View.VISIBLE);
      if (Integer.parseInt(_business.gradeNum) == 1) {
        ivDengji.setImageResource(R.mipmap.msg_members_1);
      } else if (Integer.parseInt(_business.gradeNum) == 2) {
        ivDengji.setImageResource(R.mipmap.msg_members_2);
      } else if (Integer.parseInt(_business.gradeNum) == 3) {
        ivDengji.setImageResource(R.mipmap.msg_members_3);
      } else if (Integer.parseInt(_business.gradeNum) == 4) {
        ivDengji.setImageResource(R.mipmap.msg_members_4);
      }
    } else {
      ivDengji.setVisibility(View.GONE);
    }
    List<Integer> imageIds = _business.imageIds;
    for (int i = 0; i < imageIds.size(); i++) {
      ImageView image = getImageView();
      mIvSlide.addView(image);
      String imgUrl = APIConstants.API_IMAGE + "?id=" + imageIds.get(i);
      BaseApplication.IMAGE_CACHE.get(imgUrl, image);
    }
    mIvSlide.startPlay();

    if (_business.isLikeRecord.equals("0")) {
      isPointLike = "0";

      if (MLStrUtil.compare(_business.likeRecodCount + "", "0")) {
        dianzan_text.setText("点赞");
      } else {
        dianzan_text.setText("点赞" + "(" + _business.likeRecodCount + ")");
      }


    } else {
      isPointLike = "1";
      dianzan_text.setText("取消" + "(" + _business.likeRecodCount + ")");
    }

    _nameTv.setText(_business.userName);
    _phoneTv.setText(_business.phone1);
    _addressTv.setText(_business.address);

    String comment = String.format("评价(%s条)", _business.commentCount);

    _majorOperateTv.setText(_business.majorOperate);
    _declarationTv.setText(_business.declaration);
    if (_business.products != null && _business.products.size() != 0) {
      if (_business.products.size() >= 3) {
        BaseApplication.IMAGE_CACHE.get(APIConstants.API_IMAGE_SHOW
            + _business.products.get(0).image, image_1);
        image_text_1.setText(_business.products.get(0).title.toString());
        BaseApplication.IMAGE_CACHE.get(APIConstants.API_IMAGE_SHOW
            + _business.products.get(1).image, image_2);
        image_text_2.setText(_business.products.get(1).title.toString());
        BaseApplication.IMAGE_CACHE.get(APIConstants.API_IMAGE_SHOW
            + _business.products.get(2).image, image_3);
        image_text_3.setText(_business.products.get(2).title.toString());
      }
      if (_business.products.size() == 2) {
        BaseApplication.IMAGE_CACHE.get(APIConstants.API_IMAGE_SHOW
            + _business.products.get(0).image, image_1);
        image_text_1.setText(_business.products.get(0).title.toString());
        BaseApplication.IMAGE_CACHE.get(APIConstants.API_IMAGE_SHOW
            + _business.products.get(1).image, image_2);
        image_text_2.setText(_business.products.get(1).title.toString());
      }
      if (_business.products.size() == 1) {
        BaseApplication.IMAGE_CACHE.get(APIConstants.API_IMAGE_SHOW
            + _business.products.get(0).image, image_1);
        image_text_1.setText(_business.products.get(0).title.toString());
      }
    } else {
      chanpinzhanshi.setVisibility(View.GONE);
    }

    if (MLToolUtil.isNull(_business.concurrenOperate)) {
      _business.concurrenOperate = "";
    }
    _concurrenOperateTv.setText(_business.concurrenOperate);
    String count = "0";
    if (_business.barGainNum != null) {
      count = _business.barGainNum;
    }
    // 累计拨打次数
    String phoneCount = String.format("拨打数:%s", _business.allCount);
    _countTv.setText(phoneCount);
    isCollect = _business.isCollect;

    String degree = _business.satisfaction;
  }


  private void initPlayView() {

    mIvSlide.setOnTouchListener(new AbSlidingPlayView.AbOnTouchListener() {

      @Override
      public void onTouch(MotionEvent event) {
        _scrollview.requestDisallowInterceptTouchEvent(true);
      }
    });
    mIvSlide.setOnItemClickListener(new AbSlidingPlayView.AbOnItemClickListener() {

      @Override
      public void onClick(int position) {
        if (getHeadImageUrl().size() == 0) {
          return;
        }
        MLHomeProductPop _pop = new
            MLHomeProductPop(getActivity(), getHeadImageUrl(), position);
        _pop.showAtLocation(_root, Gravity.CENTER, 0, 0);
      }
    });

  }

  /**
   * @description 收藏按钮
   * @author marcello
   */
  boolean isCollect = false;

  @OnClick(R.id.business_ib_collect)
  public void collectOnClick(View view) {
    requestCollect();
  }

  /**
   * @author marcello
   */

  @OnClick(R.id.gengduochanpin)
  public void gengduochanpinOnClick(View view) {
    _event.onEvent(_business, MLConstants.HOME_PRODUCT);
  }

  @OnClick(R.id.ll_siliao)
  public void siliaogengduochanpinOnClick(View view) {
    if (!BCStringUtil.isEmpty(_business.hxUser)) {
      HxUser mHxUser = new HxUser();
      mHxUser.emId = _business.hxUser;
      mHxUser.name = _business.userName;
      mHxUser.userId = _business.id;
      Intent intent = new Intent(getActivity(), ChatAty.class);
      intent.putExtra(Contants.EXTRA_USER, mHxUser);
      startActivity(intent);
    }
  }


  /**
   * 分享
   */
  @OnClick(R.id.ll_fenxiang)
  public void shareOnClick(View view) {
    if (_business == null) {
      return;
    }
    MLShareUtils
        .showShare(_context, null, _business.userName, _business.userName, _business.weixinUrl);
  }

  /**
   * @author marcello
   */

  @OnClick(R.id.image_1)
  public void image_1OnClick(View view) {
    List<String> images = new ArrayList<String>();
    for (int i = 0; i < _business.products.size(); i++) {
      images.add(APIConstants.API_IMAGE_SHOW
          + _business.products.get(i).image);
    }

    MLHomeProductPop _pop = new MLHomeProductPop(getActivity(), images, 0);
    _pop.showAtLocation(_root, Gravity.CENTER, 0, 0);
  }

  @OnClick(R.id.image_2)
  public void image_2OnClick(View view) {
    List<String> images = new ArrayList<String>();
    for (int i = 0; i < _business.products.size(); i++) {
      images.add(APIConstants.API_IMAGE_SHOW
          + _business.products.get(i).image);
    }

    MLHomeProductPop _pop = new MLHomeProductPop(getActivity(), images, 0);
    _pop.showAtLocation(_root, Gravity.CENTER, 0, 0);
  }

  @OnClick(R.id.image_3)
  public void image_3OnClick(View view) {
    List<String> images = new ArrayList<String>();
    for (int i = 0; i < _business.products.size(); i++) {
      images.add(APIConstants.API_IMAGE_SHOW
          + _business.products.get(i).image);
    }

    MLHomeProductPop _pop = new MLHomeProductPop(getActivity(), images, 0);
    _pop.showAtLocation(_root, Gravity.CENTER, 0, 0);
  }

  /**
   * 评论
   *
   * @description
   *
   * @author marcello
   */
  /*
   * @OnClick({R.id.business_stars,R.id.business_comment}) public void
	 * commentOnClick(View view){ _event.onEvent(_business.userID,
	 * MLConstants.HOME_COMMENT); }
	 */

  /**
   * 地址
   *
   * @description
   * @author marcello
   */
  @OnClick(R.id.business_tv_address)
  public void addressOnClick(View view) {
    /*
     * Intent intent = new Intent(_context, MLBusinessDetailMapAct.class);
		 * intent.putExtra("obj", (Serializable) _business);
		 * startActivity(intent);
		 */
  }

  @OnClick(R.id.home_top_back)
  public void home_top_back(View view) {
    getActivity().finish();
  }

  /**
   * @description
   * @author marcello
   */
  @OnClick(R.id.business_btn_more)
  public void moreOnClick(View view) {
    _event.onEvent(_business, MLConstants.HOME_PRODUCT);
  }

  /**
   * @description
   * @author marcello
   */
  @OnClick(R.id.dianzan)
  public void dianzanOnClick(View view) {
    RequestParams catalogParam = new RequestParams();
    catalogParam.addBodyParameter("userId", String.valueOf(_user.Id));
    catalogParam.addBodyParameter("companyId", _business.userID);
    if (isPointLike.equals("0")) {
      catalogParam.addBodyParameter("isPointLike", "1");
    } else {
      catalogParam.addBodyParameter("isPointLike", "0");
    }

    HttpUtils httpUtils = new HttpUtils();
    httpUtils.send(HttpMethod.POST, APIConstants.API_DEFAULT_HOST
            + APIConstants.pointLikeReocrd, catalogParam,
        new RequestCallBack<String>() {

          @Override
          public void onFailure(HttpException arg0, String arg1) {
            // TODO Auto-generated method stub

          }

          @Override
          public void onSuccess(ResponseInfo<String> arg0) {
            // TODO Auto-generated method stub
            if (isPointLike.equals("0")) {
              isPointLike = "1";
              _business.likeRecodCount++;
              dianzan_text.setText("取消"
                  + "("
                  + (_business.likeRecodCount)
                  + ")");
              showMessage("已赞");
            } else {
              isPointLike = "0";
              _business.likeRecodCount--;
              int u = _business.likeRecodCount;
              u = (u < 0) ? 0 : u;

              if (u == 0) {
                dianzan_text.setText("点赞");
              } else {
                dianzan_text.setText("点赞"
                    + "("
                    + u
                    + ")");
              }
              showMessage("已取消");
            }

          }

        });
  }

  @OnClick(R.id.weiwangzhan)
  public void weiwangzhanOnClick(View view) {
    Intent intent = new Intent(getActivity(), TXWeiWangZhan.class);
    intent.putExtra("url", _business.weixinUrl);
    startActivity(intent);
  }

  private MLHomeCityPop _menuWindow;

  /**
   * @description
   * @author marcello
   */
  @OnClick({R.id.business_ll_phone, R.id.business_tv_phone})
  public void callOnClick(View view) {
    final List<MLHomeCityData> datas = new ArrayList<MLHomeCityData>();

    MLHomeCityData data2 = new MLHomeCityData();
    data2.cityName = _business.phone1;

//    MLHomeCityData data1 = new MLHomeCityData();
//    data1.cityName = _business.phone;

    MLHomeCityData data3 = new MLHomeCityData();
    data3.cityName = _business.phone2;

    MLHomeCityData data4 = new MLHomeCityData();
    data4.cityName = _business.phone3;

    MLHomeCityData data5 = new MLHomeCityData();
    data5.cityName = _business.phone4;

    if (!BCStringUtil.isEmpty(_business.phone)) {
      datas.add(data2);
    }

    if (!BCStringUtil.isEmpty(_business.phone2)) {
      datas.add(data3);
    }

    if (!BCStringUtil.isEmpty(_business.phone3)) {
      datas.add(data4);
    }

    if (!BCStringUtil.isEmpty(_business.phone4)) {
      datas.add(data5);
    }

    _menuWindow = new MLHomeCityPop(getActivity(), datas,
        new OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> arg0, View arg1,
              int arg2, long arg3) {
            MLHomeCityData data = datas.get(arg2);
            String phoneNum = data.cityName;
//            phoneNum = phoneNum.replaceAll("\\(免费拨打\\)", "");
//            phoneNum.replaceAll(" ", "");
            _menuWindow.dismiss();
            if (_user.isDepot) {
              call(phoneNum);
            } else {
              Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
              startActivity(intent);
//              Intent intent = new Intent(Intent.ACTION_CALL,
//                  Uri.parse("tel:" + phoneNum));
//              startActivity(intent);
//              dial("0");
            }
          }
        });
    _menuWindow.showAtLocation(_root, Gravity.CENTER, 0, 0);
  }

  private void call(final String phone) {
    if (MLToolUtil.isNull(phone)) {
      return;
    }
    if (phone.startsWith("400")) {
      Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
      startActivity(intent);
      dial("0");
      return;
    }
    final List<MLHomeCityData> datas = new ArrayList<MLHomeCityData>();
    MLHomeCityData data1 = new MLHomeCityData();
    data1.cityName = "直接拨打";

    MLHomeCityData data2 = new MLHomeCityData();
    data2.cityName = "免费通话";
    Collections.addAll(datas, data1, data2);

    _menuWindow = new MLHomeCityPop(getActivity(), datas, new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
          long arg3) {
        if (arg2 == 0) {
          //直接拨打
          Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
          startActivity(intent);
          dial("0");
        } else {
          //网络电话
            /*	MLHomeBusiness1Data data = new MLHomeBusiness1Data();
              data.address = _data.address;
    					data.userName = _data.userName;
    					data.phone = phone;
    					data.logo = _data.logo;

    				Intent phoneIt = new Intent();
    				phoneIt.setClass(_context, MLAuxiliaryActivity.class);
    				phoneIt.putExtra("data", MLConstants.HOME_CALL);
    				phoneIt.putExtra("obj", (Serializable) data);
    				startActivity(phoneIt);*/
          callWeb(phone);
        }
        _menuWindow.dismiss();

      }
    });
    _menuWindow.showAtLocation(_root, Gravity.CENTER, 0, 0);
  }

  private void callWeb(String phone) {
    MLLogin mUser = BaseApplication.getInstance().get_user();
    ZMRequestParams catalogParam = new ZMRequestParams();
    catalogParam.addParameter("fromClient", mUser.clientNumber);
    catalogParam.addParameter("to", phone);

    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.HOME_CALL_WEB, null,
        catalogParam, _handler, HTTP_RESPONSE_CALL_WEB, MLHomeServices.getInstance());
    loadDataWithMessage(_context, null, message1);

  }

  //商家支付
  @OnClick({R.id.detail_phone_bottom})
  public void phoneOnClick(View view) {
    startAct(getFragment(), BizPayActivity.class, _business);
  }


  private List<String> getImageUrl() {
    List<String> images = new ArrayList<String>();
    for (TXProductModel data : _business.products) {
      String path = APIConstants.API_IMAGE + "?id=" + data.image;
      images.add(path);
    }
    return images;
  }

  private List<String> getHeadImageUrl() {
    List<String> images = new ArrayList<String>();
    for (int id : _business.imageIds) {
      String path = APIConstants.API_IMAGE + "?id=" + id;
      images.add(path);
    }
    return images;
  }

//  private void call(final String phone) {
//    if (MLToolUtil.isNull(phone)) {
//      return;
//    }
//    if (phone.startsWith("400")) {
//      Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
//          + phone));
//      startActivity(intent);
//      dial("0");
//      return;
//    }
//
//    final List<MLHomeCityData> datas = new ArrayList<MLHomeCityData>();
//    MLHomeCityData data1 = new MLHomeCityData();
//    data1.cityName = "直接拨打";
//
//    MLHomeCityData data2 = new MLHomeCityData();
//    data2.cityName = "免费通话";
//    Collections.addAll(datas, data1, data2);
//
//    _menuWindow = new MLHomeCityPop(getActivity(), datas,
//        new OnItemClickListener() {
//          @Override
//          public void onItemClick(AdapterView<?> arg0, View arg1,
//              int arg2, long arg3) {
//            if (arg2 == 0) {
//              // 直接拨打
//              Intent intent = new Intent(Intent.ACTION_CALL,
//                  Uri.parse("tel:" + phone));
//              startActivity(intent);
//              dial("0");
//            } else {
//              callWeb(phone);
//            }
//            _menuWindow.dismiss();
//
//          }
//        });
//    _menuWindow.showAtLocation(_root, Gravity.CENTER, 0, 0);
//  }

  /**
   * @description 收藏 (参数有问题)
   * @author marcello
   */
  private void requestCollect() {
    MLLogin user = ((BaseApplication) getActivity().getApplication())
        .get_user();

    String collect = "";
    if (isCollect) {
      collect = "0";
    } else {
      collect = "1";
    }
    if (_business == null) {
      return;
    }
    ZMRequestParams catalogParam = new ZMRequestParams();
    catalogParam.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,
        _business.userID);
    catalogParam.addParameter(MLConstants.PARAM_HOME_DEPORT, user.Id);
    catalogParam.addParameter(MLConstants.PARAM_HOME_ISFLAG, collect);
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(
        RequestType.HOME_COLLECT, null, catalogParam, _handler,
        HTTP_RESPONSE_COLLECT, MLHomeServices.getInstance());
    loadDataWithMessage(_context, null, message1);

  }

  private void dial(String isNetWorkPhone) {
    MLLogin user = ((BaseApplication) getActivity().getApplication())
        .get_user();

    if (!user.isDepot) {
      return;
    }
    ZMRequestParams catalogParam = new ZMRequestParams();
    catalogParam.addParameter(MLConstants.PARAM_HOME_ISNETWORKPHONE,
        isNetWorkPhone);
    catalogParam.addParameter(MLConstants.PARAM_LOGIN_DEPORTID, user.Id);
    // 判断是否来自收藏列表页面
    if (_oldBusiness.isCollectParam) {
      catalogParam.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,
          _oldBusiness.compayId);
    } else {
      catalogParam.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,
          _oldBusiness.id);
    }
    catalogParam.addParameter(MLConstants.PARAM_HOME_DEPOTPHONE,
        BaseApplication.aCache.getAsString(MLConstants.PARAM_REGISTER_USERNAME));
    catalogParam.addParameter(MLConstants.PARAM_HOME_COMPANYPHONE,
        _business.phone);
    catalogParam.addParameter("phoneTime", "1");

    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(
        RequestType.HOME_CALL, null, catalogParam, _handler,
        HTTP_RESPONSE_CALL, MLHomeServices.getInstance());
    loadDataWithMessage(_context, null, message1);
  }

//  private void callWeb(String phone) {
//    MLLogin mUser = BaseApplication.getInstance().get_user();
//    ZMRequestParams catalogParam = new ZMRequestParams();
//    catalogParam.addParameter("fromClient", mUser.clientNumber);
//    catalogParam.addParameter("to", phone);
//
//    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(
//        RequestType.HOME_CALL_WEB, null, catalogParam, _handler,
//        HTTP_RESPONSE_CALL_WEB, MLHomeServices.getInstance());
//    loadDataWithMessage(_context, null, message1);
//
//  }


  /**
   * @description 返回
   * @author marcello
   */
  @OnClick(R.id.top_back)
  public void backOnClick(View view) {
    ((MLAuxiliaryActivity) _context).finish();
  }


  private static final int HTTP_RESPONSE_BUSINESS = 1;
  private static final int HTTP_RESPONSE_COLLECT = 2;
  private static final int HTTP_RESPONSE_CALL = 3;
  private static final int HTTP_RESPONSE_CALL_WEB = 9;

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
        // 获取商家详情
        case HTTP_RESPONSE_BUSINESS: {
          MLHomeBusinessDetail1 ret = (MLHomeBusinessDetail1) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            _business = ret.datas;
            initView();
          } else {
            showMessage("获取详情失败!");
          }
          break;
        }
        case HTTP_RESPONSE_COLLECT: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            if (isCollect) {
              showMessageSuccess("取消收藏!");
            } else {
              showMessageSuccess("收藏成功!");
            }
            isCollect = !isCollect;
          } else {
            showMessage("收藏失败!");
          }
          break;
        }
        case HTTP_RESPONSE_CALL_WEB: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1") && ret.datas) {
            showMessage("呼叫成功,请等待回拨");
          } else {
            showMessage("呼叫失败");
          }
          break;
        }
        default:
          break;
      }
    }
  };

  @Override
  public void onDestroy() {
    // TODO Auto-generated method stub
    mView = null;
    super.onDestroy();
  }

  public ImageView getImageView() {
    ImageView image = new ImageView(_context);
    image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
        LayoutParams.MATCH_PARENT));
    image.setScaleType(ScaleType.CENTER_CROP);
    return image;
  }

  @Override
  public void onPause() {
    // TODO Auto-generated method stub
    super.onPause();
    // 关闭支付加载框
    dismissProgressDialog();
  }

  private IEvent<Object> _event;

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    _event = (IEvent<Object>) activity;
  }
}
