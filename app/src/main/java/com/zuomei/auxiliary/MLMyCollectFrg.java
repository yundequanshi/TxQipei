package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.baichang.android.utils.BCStringUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qipei.adapter.MLInteractAdapter;
import com.qipei.interact.InteractPeopleActivity;
import com.txsh.R;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.base.MLEventBusModel;
import com.zuomei.constants.MLConstants;
import com.zuomei.home.MLHomeCityPop;
import com.zuomei.home.MLHomeProductPop;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLHomeBusiness1Data;
import com.zuomei.model.MLHomeBusinessData;
import com.zuomei.model.MLHomeBusinessList;
import com.zuomei.model.MLHomeCityData;
import com.zuomei.model.MLInteractionData;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLMessageCommentData;
import com.zuomei.model.MLMessageData;
import com.zuomei.model.MLMessageListResponse;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLHomeServices;
import com.zuomei.services.MLMessageServices;
import com.zuomei.services.MLMyServices;
import com.zuomei.utils.MLToolUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.ml.base.utils.IEvent;
import org.greenrobot.eventbus.EventBus;

/**
 * 我的-收藏列表
 *
 * @author Marcello
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyCollectFrg extends BaseFragment {

  public static MLMyCollectFrg INSTANCE = null;

  public static MLMyCollectFrg instance() {
    //if(INSTANCE==null){
    INSTANCE = new MLMyCollectFrg();
    //	}
    return INSTANCE;
  }

  @ViewInject(R.id.home_lv_business)
  private AbPullToRefreshView _pullToRefreshLv;
  @ViewInject(R.id.mListView)
  private ListView _businessLv;
  public List<MLHomeBusinessData> _businessDatas;
  @ViewInject(R.id.login_register_root)
  private RelativeLayout _root;
  @ViewInject(R.id.interact_rl_reply)
  private RelativeLayout _replyLy;
  @ViewInject(R.id.interact_et_reply)
  private EditText _replyEt;
  @ViewInject(R.id.interact_rb_tab1)
  private RadioButton rb1;
  @ViewInject(R.id.interact_rg)
  private RadioGroup rg;

  private MLMyCollectAdapter _collectAdapter;
  private Context _context;
  private List<MLMessageData> _messageData;
  private int _replyPositiion;
  private MLInteractAdapter mAdapter;
  private MLLogin user = new MLLogin();
  private String isPraise = "0";
  private String isCollection = "0";

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.my_collect, null);
    ViewUtils.inject(this, view);
    _context = inflater.getContext();
    user = ((BaseApplication) getActivity().getApplication()).get_user();
    if (user.isDepot) {
      rg.setVisibility(View.VISIBLE);
      init();
      initData();
    } else {
      initHu();
      initHuData();
    }
    return view;
  }

  @OnClick(R.id.interact_rb_tab1)
  private void shopRb(View view) {
    _replyLy.setVisibility(View.GONE);
    _collectAdapter = new MLMyCollectAdapter(_context, _callHandler);
    _businessLv.setAdapter(_collectAdapter);
    initData();
  }

  @OnClick(R.id.interact_rb_tab2)
  private void huRb(View view) {
    mAdapter = new MLInteractAdapter(_context, R.layout.item_interact_list, _updateHandler);
    _businessLv.setAdapter(mAdapter);
    initHuData();
  }

  private void init() {
    _user = ((BaseApplication) getActivity().getApplication()).get_user();
    _collectAdapter = new MLMyCollectAdapter(_context, _callHandler);
    _businessLv.setAdapter(_collectAdapter);

    _businessLv.setOnScrollListener(new AbsListView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(AbsListView arg0, int arg1) {
        _replyLy.setVisibility(View.GONE);
      }

      @Override
      public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

      }
    });

    _businessLv.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
          long arg3) {
        // TODO Auto-generated method stub
        if (rb1.isChecked()) {
          MLHomeBusinessData data = (MLHomeBusinessData) arg0.getAdapter().getItem(arg2);
          data.isCollectParam = true;
          Intent intent = new Intent();
          intent.setClass(_context, MLAuxiliaryActivity.class);
          intent.putExtra("data", MLConstants.HOME_BUSINESS_INFO);
          intent.putExtra("obj", (Serializable) data);
          startActivity(intent);
        }
      }
    });

    _businessLv.setOnItemLongClickListener(new OnItemLongClickListener() {

      @Override
      public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
          int arg2, long arg3) {
        if (rb1.isChecked()) {
          final MLHomeBusinessData data = (MLHomeBusinessData) arg0.getAdapter().getItem(arg2);
          Builder builder = new Builder(_context);
          builder.setItems(new String[]{"取消收藏"}, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
              arg0.dismiss();
              requestCollect(data.compayId);
              //  delStock(data.id);
            }
          }).setTitle("请选择");
          builder.show();
        }
        return false;
      }
    });

    _pullToRefreshLv.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {

      @Override
      public void onHeaderRefresh(AbPullToRefreshView view) {
        if (rb1.isChecked()) {
          initData();
        } else {
          initHuData();
        }
      }
    });
    _pullToRefreshLv.setOnFooterLoadListener(new OnFooterLoadListener() {

      @Override
      public void onFooterLoad(AbPullToRefreshView view) {
        // TODO Auto-generated method stub
        if (rb1.isChecked()) {
          pageData();
        } else {
          pageHuData();
        }
      }
    });
  }

  private void initHu() {
    mAdapter = new MLInteractAdapter(_context, R.layout.item_interact_list, _updateHandler);
    _businessLv.setAdapter(mAdapter);

    _businessLv.setOnScrollListener(new AbsListView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(AbsListView arg0, int arg1) {
        _replyLy.setVisibility(View.GONE);
      }

      @Override
      public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

      }
    });

    _pullToRefreshLv.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {

      @Override
      public void onHeaderRefresh(AbPullToRefreshView view) {
        if (rb1.isChecked()) {
          initData();
        } else {
          initHuData();
        }
      }
    });
    _pullToRefreshLv.setOnFooterLoadListener(new OnFooterLoadListener() {

      @Override
      public void onFooterLoad(AbPullToRefreshView view) {
        // TODO Auto-generated method stub
        if (rb1.isChecked()) {
          pageData();
        } else {
          pageHuData();
        }
      }
    });
  }

  public void initHuData() {
    ZMRequestParams param = new ZMRequestParams();
    if (user.isDepot) {
      param.addParameter("depotId", user.Id);
    } else {
      param.addParameter("companyId", user.Id);
    }
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(
        ZMHttpType.RequestType.FIND_COLLECT_INTERACTION,
        null, param, _handler, HTTP_RESPONSE_MESSSAGE_LIST, MLMessageServices.getInstance());
    loadDataWithMessage(_context, null, message1);
  }

  private void pageHuData() {
    if (_messageData == null) {
      return;
    }
    //获取互动列表
    ZMRequestParams param = new ZMRequestParams();
    if (user.isDepot) {
      param.addParameter("depotId", user.Id);
    } else {
      param.addParameter("companyId", user.Id);
    }
    String lastId = _messageData.get(_messageData.size() - 1).info.id + "";
    //param.addParameter("pageNum","2");
    param.addParameter(MLConstants.PARAM_MESSAGE_LASTID, lastId);
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(
        ZMHttpType.RequestType.FIND_COLLECT_INTERACTION,
        null, param, _handler, HTTP_RESPONSE_MESSSAGE_PAGE, MLMessageServices.getInstance());
    loadDataWithMessage(_context, null, message1);
  }


  private void initData() {
//		MLLogin user = BaseApplication._user;
    ZMRequestParams catalogParam = new ZMRequestParams();
    catalogParam.addParameter(MLConstants.PARAM_MY_DEPTID1, _user.Id);
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_COLLECT, null,
        catalogParam, _handler, HTTP_RESPONSE_COLLECT, MLMyServices.getInstance());
    loadDataWithMessage(_context, null, message1);
  }

  private void pageData() {
//		MLLogin user = BaseApplication._user;
    ZMRequestParams catalogParam = new ZMRequestParams();
    catalogParam.addParameter(MLConstants.PARAM_MY_DEPTID1, _user.Id);
    String lastId = _businessDatas.get(_businessDatas.size() - 1).id + "";
    catalogParam.addParameter(MLConstants.PARAM_MESSAGE_LASTID, lastId);
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.MY_COLLECT, null,
        catalogParam, _handler, HTTP_RESPONSE_COLLECT_PAGE, MLMyServices.getInstance());
    loadDataWithMessage(_context, null, message1);
  }

  /**
   * @description 收藏   (参数有问题)
   * @author marcello
   */
  private void requestCollect(String id) {
//		MLLogin user = BaseApplication._user;
    ZMRequestParams catalogParam = new ZMRequestParams();
    catalogParam.addParameter(MLConstants.PARAM_HOME_BUSINESSID1, id);
    catalogParam.addParameter(MLConstants.PARAM_HOME_DEPORT, _user.Id);
    catalogParam.addParameter(MLConstants.PARAM_HOME_ISFLAG, "0");
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.HOME_COLLECT, null,
        catalogParam, _handler, HTTP_RESPONSE_COLLECT_CANCEL, MLHomeServices.getInstance());
    loadDataWithMessage(_context, null, message1);
  }

  private MLHomeCityPop _menuWindow;
  private MLHomeBusinessData _data;
  private MLLogin _user;
  private Handler _callHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      _data = (MLHomeBusinessData) msg.obj;

      final List<MLHomeCityData> datas = new ArrayList<MLHomeCityData>();
      MLHomeCityData data1 = new MLHomeCityData();
      data1.cityName = _data.phone;

      MLHomeCityData data2 = new MLHomeCityData();
      data2.cityName = _data.phone1;

      MLHomeCityData data3 = new MLHomeCityData();
      data3.cityName = _data.phone2;
      Collections.addAll(datas, data1, data2, data3);

      _menuWindow = new MLHomeCityPop(getActivity(), datas, new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
            long arg3) {
          MLHomeCityData data = datas.get(arg2);
          String phoneNum = data.cityName;
          _menuWindow.dismiss();
          if (_user.isDepot) {
            call(phoneNum);
          } else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
            startActivity(intent);
          }
        }
      });
      _menuWindow.showAtLocation(_root, Gravity.CENTER, 0, 0);

    }
  };

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
          MLHomeBusiness1Data data = new MLHomeBusiness1Data();
          data.address = _data.address;
          data.userName = _data.userName;
          data.phone = phone;
          data.logo = _data.logo;

          Intent phoneIt = new Intent();
          phoneIt.setClass(_context, MLAuxiliaryActivity.class);
          phoneIt.putExtra("data", MLConstants.HOME_CALL);
          phoneIt.putExtra("obj", (Serializable) data);
          startActivity(phoneIt);
          //	dial("1");
        }
        _menuWindow.dismiss();

      }
    });
    _menuWindow.showAtLocation(_root, Gravity.CENTER, 0, 0);
  }

  private void dial(String isNetWorkPhone) {
    //	MLLogin user = BaseApplication._user;
    if (!_user.isDepot) {
      return;
    }

    ZMRequestParams catalogParam = new ZMRequestParams();
    catalogParam.addParameter(MLConstants.PARAM_HOME_ISNETWORKPHONE, isNetWorkPhone);
    catalogParam.addParameter(MLConstants.PARAM_LOGIN_DEPORTID, _user.Id);
    //判断是否来自收藏列表页面
    catalogParam.addParameter(MLConstants.PARAM_HOME_BUSINESSID1, _data.id);
    catalogParam.addParameter(MLConstants.PARAM_HOME_DEPOTPHONE,
        BaseApplication.aCache.getAsString(MLConstants.PARAM_REGISTER_USERNAME));
    catalogParam.addParameter(MLConstants.PARAM_HOME_COMPANYPHONE, _data.phone);
    catalogParam.addParameter("phoneTime", "1");

    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.HOME_CALL, null,
        catalogParam, _handler, HTTP_RESPONSE_CALL, MLHomeServices.getInstance());
    loadDataWithMessage(_context, null, message1);
  }

  @OnClick(R.id.interact_btn_reply)
  public void replyOnClick(View view) {

    String content = _replyEt.getText().toString();
    content = content.replaceAll("\r|\n", "");
    if (MLToolUtil.isNull(content)) {
      showMessage("评论内容不能为空!");
      return;
    }

    String name = ((BaseApplication) getActivity().getApplication()).get_user().name;
    MLMessageCommentData comment = new MLMessageCommentData();
    /*comment.userName=name;
    comment.content= content;*/
    comment.userName = name;
    comment.content = content;

    ZMRequestParams param = new ZMRequestParams();

    MLLogin user = ((BaseApplication) getActivity().getApplication()).get_user();
    if (user.isDepot) {
      param.addParameter("depotId", user.Id);
    } else {
      param.addParameter("companyId", user.Id);
    }
    param.addParameter(MLConstants.PARAM_MESSAGE_CONTENT, content);
    param.addParameter(MLConstants.PARAM_MESSAGE_USERNAME, name);
    param.addParameter(MLConstants.PARAM_MESSAGE_ITID, _messageData.get(_replyPositiion).info.id);

    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(ZMHttpType.RequestType.MESSAGE_REPLY,
        null, param, _handler, HTTP_RESPONSE_MESSSAGE_REPLY, MLMessageServices.getInstance());
    loadData(_context, message1);

    _messageData.get(_replyPositiion).info.interactionComment.add(comment);
    mAdapter.setData(_messageData);

    _replyLy.setVisibility(View.GONE);
  }

  //收藏
  private void collection(String interactionId) {
    ZMRequestParams param = new ZMRequestParams();
    if (user.isDepot) {
      param.addParameter("depotId", user.Id);
    } else {
      param.addParameter("companyId", user.Id);
    }
    param.addParameter("interactionId", interactionId);
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(ZMHttpType.RequestType.HD_COLLECTION,
        null, param, _handler, HTTP_RESPONSE_HD_COLLECTION, MLMessageServices.getInstance());
    loadDataWithMessage(_context, null, message1);
  }

  //点赞
  private void dianzan(String interactionId) {
    ZMRequestParams param = new ZMRequestParams();
    if (user.isDepot) {
      param.addParameter("depotId", user.Id);
    } else {
      param.addParameter("companyId", user.Id);
    }
    param.addParameter("interactionId", interactionId);
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(ZMHttpType.RequestType.HD_DIANZAN,
        null, param, _handler, HTTP_RESPONSE_HD_DIANZAN, MLMessageServices.getInstance());
    loadDataWithMessage(_context, null, message1);
  }

  //举报
  private void jubao(String interactionId) {
    ZMRequestParams param = new ZMRequestParams();
    if (user.isDepot) {
      param.addParameter("depotId", user.Id);
    } else {
      param.addParameter("companyId", user.Id);
    }
    param.addParameter("interactionId", interactionId);
    param.addParameter("reprotReason", "");
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(ZMHttpType.RequestType.HD_JUBAO,
        null, param, _handler, HTTP_RESPONSE_HD_JUBAO, MLMessageServices.getInstance());
    loadDataWithMessage(_context, null, message1);
  }

  public void refresh(String s) {
    initData();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == 1) {
      initData();
    }
  }

  protected void reviewMessageList(List<MLMessageData> datas) {
    mAdapter.setData(datas);
  }

  public Handler _updateHandler = new Handler() {

    @Override
    public void handleMessage(Message msg) {
      // TODO Auto-generated method stub
      super.handleMessage(msg);

      switch (msg.what) {
        case 1:
          //评论
          _replyLy.setVisibility(View.VISIBLE);
          _replyPositiion = msg.arg1;
          break;
        case 2:
          //图片
          List<String> images = new ArrayList<String>();
          images = (List<String>) msg.obj;
          MLHomeProductPop _pop = new MLHomeProductPop(getActivity(), images, msg.arg1);
          _pop.showAtLocation(_root, Gravity.CENTER, 0, 0);
          break;
        case 3: {
          //头像
          final MLMessageData d = (MLMessageData) msg.obj;
          if (d.userType.equalsIgnoreCase("1")) {
            //汽修厂
            if (d.user.id.equals(user.Id)) {
              EventBus.getDefault().post(new MLEventBusModel(MLConstants.INTERACTLIST));
            } else {
              d.userType = "0";
              startAct(getFragment(), InteractPeopleActivity.class, d);
            }
          } else {
            MLHomeBusinessData _business = new MLHomeBusinessData();
            _business.isCollect = false;
            _business.id = d.user.id;
            Intent intent = new Intent();
            intent.setClass(_context, MLAuxiliaryActivity.class);
            intent.putExtra("data", MLConstants.HOME_BUSINESS_INFO);
            intent.putExtra("obj", (Serializable) _business);
            _context.startActivity(intent);
          }

          break;
        }
        //收藏
        case 4: {
          MLMessageData data = (MLMessageData) msg.obj;
          isCollection = data.info.isCollection;
          collection(data.info.id);
          break;
        }
        //点赞
        case 5: {
          MLMessageData data = (MLMessageData) msg.obj;
          isPraise = data.info.isPraise;
          dianzan(data.info.id);
          break;
        }
        //举报
        case 6: {
          MLMessageData data = (MLMessageData) msg.obj;
          jubao(data.info.id);
          break;
        }

        default:
          break;
      }


    }

  };

  private static final int HTTP_RESPONSE_COLLECT = 0;
  private static final int HTTP_RESPONSE_COLLECT_CANCEL = 1;
  private static final int HTTP_RESPONSE_COLLECT_PAGE = 2;
  private static final int HTTP_RESPONSE_CALL = 3;
  private static final int HTTP_RESPONSE_MESSSAGE_LIST = 4;
  private static final int HTTP_RESPONSE_MESSSAGE_REPLY = 5;
  private static final int HTTP_RESPONSE_MESSSAGE_PAGE = 6;
  private static final int HTTP_RESPONSE_HD_COLLECTION = 7;
  private static final int HTTP_RESPONSE_HD_DIANZAN = 8;
  private static final int HTTP_RESPONSE_HD_JUBAO = 9;
  private static final int HTTP_RESPONSE_MESSSAGE_REPORT = 10;

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
        //获取收藏列表
        case HTTP_RESPONSE_COLLECT: {
          MLHomeBusinessList ret = (MLHomeBusinessList) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            _businessDatas = ret.datas;
            setBusinessList(ret.datas);
          } else {
            showMessageError("获取车辆失败!");
          }
          _pullToRefreshLv.onHeaderRefreshFinish();
          break;
        }
        case HTTP_RESPONSE_COLLECT_CANCEL: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            initData();
          } else {
            showMessageError("操作失败!");
          }
          break;
        }
        case HTTP_RESPONSE_COLLECT_PAGE: {
          MLHomeBusinessList ret = (MLHomeBusinessList) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            _businessDatas.addAll(ret.datas);
            setBusinessList(_businessDatas);
          } else {
            showMessageError("获取车辆失败!");
          }
          _pullToRefreshLv.onFooterLoadFinish();
          break;
        }
        //获取消息列表
        case HTTP_RESPONSE_MESSSAGE_LIST: {
          MLMessageListResponse ret = (MLMessageListResponse) msg.obj;
          _messageData = ret.datas;
          if (ret.state.equalsIgnoreCase("1")) {
            if (ret.datas.size() > 0) {
              BaseApplication._messageLastId = ret.datas.get(0).info.id;
            }
            reviewMessageList(ret.datas);
          } else {
            showMessageError("获取消息列表失败!");
          }

          _pullToRefreshLv.onHeaderRefreshFinish();
          break;
        }
        //获取分页消息
        case HTTP_RESPONSE_MESSSAGE_PAGE: {
          MLMessageListResponse ret = (MLMessageListResponse) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            if (ret.datas.isEmpty()) {
              _pullToRefreshLv.setLoadMoreEnable(false);
            }
            _messageData.addAll(ret.datas);
            reviewMessageList(_messageData);
          } else {
            showMessageError("获取消息列表失败!");
          }
          _pullToRefreshLv.onFooterLoadFinish();
          break;
        }
        //评论
        case HTTP_RESPONSE_MESSSAGE_REPLY: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            showMessageSuccess("评论成功!");
          } else {
            showMessageError("评论失败!");
          }
          _replyEt.setText("");
          break;
        }
        case HTTP_RESPONSE_MESSSAGE_REPORT: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            showMessageSuccess("举报成功!");
          } else {
            showMessageError("举报失败!");
          }
          break;
        }
        //收藏
        case HTTP_RESPONSE_HD_COLLECTION: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            if (!BCStringUtil.isEmpty(isCollection)) {
              if (isCollection.equals("1")) {
                showMessageSuccess("收藏成功!");
              } else {
                showMessageSuccess("取消收藏成功!");
              }
              initHuData();
            }
          } else {
            showMessageError("收藏失败!");
          }
          break;
        }

        //点赞
        case HTTP_RESPONSE_HD_DIANZAN: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            if (!BCStringUtil.isEmpty(isPraise)) {
              if (isPraise.equals("1")) {
                showMessageSuccess("点赞成功!");
              } else {
                showMessageSuccess("取消点赞成功!");
              }
            }
          } else {
            showMessageError("点赞失败!");
          }
          break;
        }

        //举报
        case HTTP_RESPONSE_HD_JUBAO: {
          MLInteractionData ret = (MLInteractionData) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            showMessageSuccess("举报成功!");
          } else {
            showMessageError("举报失败!");
          }
          break;
        }
        default:
          break;
      }
    }
  };


  /**
   * @description 返回
   * @author marcello
   */
  @OnClick(R.id.top_back)
  public void backOnClick(View view) {
    //getActivity().onBackPressed();
    getActivity().finish();
  }

  protected void setBusinessList(List<MLHomeBusinessData> datas) {
    _collectAdapter.setData(datas);
  }

  private IEvent<Object> _event;

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    _event = (IEvent<Object>) activity;
  }


}
