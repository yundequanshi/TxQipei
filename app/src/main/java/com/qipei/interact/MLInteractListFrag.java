package com.qipei.interact;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ab.view.pullview.AbPullToRefreshView;
import com.baichang.android.utils.BCDialogUtil;
import com.baichang.android.utils.BCStringUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qipei.adapter.MLInteractAdapter;
import com.qipei.model.ParseInfoData;
import com.txsh.R;
import com.txsh.market.EventBusModel;
import com.txsh.quote.Flag;
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.base.MLEventBusModel;
import com.zuomei.constants.MLConstants;
import com.zuomei.home.MLHomeProductPop;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLDepotData;
import com.zuomei.model.MLHomeBusinessData;
import com.zuomei.model.MLInteractionData;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLMessageCommentData;
import com.zuomei.model.MLMessageData;
import com.zuomei.model.MLMessageListResponse;
import com.zuomei.model.MLParseResponse;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLMessageServices;
import com.zuomei.utils.MLToolUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.ml.base.utils.IEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 互动列表 Created by Marcello on 2015/6/2.
 */
public class MLInteractListFrag extends BaseFragment {

  private View view;

  @ViewInject(R.id.mListView)
  private ListView mListView;
  @ViewInject(R.id.interact_lv)
  private AbPullToRefreshView _pullToRefreshLv;
  @ViewInject(R.id.rl_root)
  private RelativeLayout _root;
  @ViewInject(R.id.interact_rl_reply)
  private RelativeLayout _replyLy;
  @ViewInject(R.id.interact_et_reply)
  private EditText _replyEt;

  private List<MLMessageData> _messageData;
  private int _replyPositiion;
  private Context _context;
  private MLInteractAdapter mAdapter;
  private MLLogin user = new MLLogin();
  private String isPraise = "0";
  private String isCollection = "0";

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.interact_list, null);
    ViewUtils.inject(this, view);
    EventBus.getDefault().register(this);
    _context = getActivity();
    user = ((BaseApplication) getActivity().getApplication()).get_user();
    initView();
    initData();
    return view;
  }

  private void initView() {

    mAdapter = new MLInteractAdapter(_context, R.layout.item_interact_list, _updateHandler);
    mListView.setAdapter(mAdapter);

    _pullToRefreshLv.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {

      @Override
      public void onHeaderRefresh(AbPullToRefreshView view) {
        initData();
      }
    });
    ;
    _pullToRefreshLv.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {

      @Override
      public void onFooterLoad(AbPullToRefreshView view) {
        // TODO Auto-generated method stub
        pageData();
      }
    });
    mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(AbsListView arg0, int arg1) {
        _replyLy.setVisibility(View.GONE);
      }

      @Override
      public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

      }
    });

  }

  public void initData() {
    ZMRequestParams param = new ZMRequestParams();
    if (user.isDepot) {
      param.addParameter(MLConstants.PARAM_LOGIN_DEPORTID, user.Id);
    } else {
      param.addParameter(MLConstants.PARAM_HOME_BUSINESSID1, user.Id);
    }
    param.addParameter("cityId", BaseApplication._currentCity);
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(ZMHttpType.RequestType.MESSAGE_LIST,
        null, param, _handler, HTTP_RESPONSE_MESSSAGE_LIST, MLMessageServices.getInstance());
    loadDataWithMessage(_context, null, message1);
  }

  private void pageData() {
    if (_messageData == null) {
      return;
    }
    //获取互动列表
    ZMRequestParams param = new ZMRequestParams();
    if (user.isDepot) {
      param.addParameter(MLConstants.PARAM_LOGIN_DEPORTID, user.Id);
    } else {
      param.addParameter(MLConstants.PARAM_HOME_BUSINESSID1, user.Id);
    }

    param.addParameter("cityId", BaseApplication._currentCity);
    String lastId = _messageData.get(_messageData.size() - 1).info.id + "";
    //param.addParameter("pageNum","2");
    param.addParameter(MLConstants.PARAM_MESSAGE_LASTID, lastId);
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(ZMHttpType.RequestType.MESSAGE_LIST,
        null, param, _handler, HTTP_RESPONSE_MESSSAGE_PAGE, MLMessageServices.getInstance());
    loadDataWithMessage(_context, null, message1);
  }


  @OnClick(R.id.interact_top)
  public void topOnClick(View view) {
    mListView.setSelection(0);
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
        //个人互动
        case 7: {
          ParseInfoData data = (ParseInfoData) msg.obj;
          MLMessageData messageData = new MLMessageData();
          messageData.userType = data.flag;
          messageData.user = new MLDepotData();
          messageData.user.id = data.praiseId;
          messageData.hxUser = data.hxUser;
          messageData.user.userPhoto = data.headPic;
          messageData.user.depotName = data.praiseName;
          messageData.user.userPhone = data.phone;
          if (data.flag.equals("0")) {
            if (data.praiseId.equals(user.Id)) {
              EventBus.getDefault().post(new MLEventBusModel(MLConstants.INTERACTLIST));
            } else {
              startAct(getFragment(), InteractPeopleActivity.class, messageData);
            }
          } else {
            MLHomeBusinessData _business = new MLHomeBusinessData();
            _business.isCollect = false;
            _business.id = data.praiseId;
            Intent intent = new Intent();
            intent.setClass(_context, MLAuxiliaryActivity.class);
            intent.putExtra("data", MLConstants.HOME_BUSINESS_INFO);
            intent.putExtra("obj", (Serializable) _business);
            _context.startActivity(intent);
          }
          break;
        }
        //个人互动
        case 8: {
          MLMessageData messageData = (MLMessageData) msg.obj;
          startAct(getFragment(), ParsePeopleActivity.class, messageData.info.id);
          break;
        }

        default:
          break;
      }


    }

  };

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEventMainThread(EventBusModel event) {
    initData();
  }

  private static final int HTTP_RESPONSE_MESSSAGE_LIST = 1;
  private static final int HTTP_RESPONSE_MESSSAGE_REPLY = 2;
  private static final int HTTP_RESPONSE_MESSSAGE_TEST = 3;
  private static final int HTTP_RESPONSE_MESSSAGE_REPORT = 4;
  private static final int HTTP_RESPONSE_MESSSAGE_PAGE = 5;
  private static final int HTTP_RESPONSE_HD_COLLECTION = 6;
  private static final int HTTP_RESPONSE_HD_DIANZAN = 7;
  private static final int HTTP_RESPONSE_HD_JUBAO = 8;


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
          MLParseResponse ret = (MLParseResponse) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            showMessageSuccess("评论成功!");
          } else {
            showMessageError("评论失败!");
          }
          _replyEt.setText("");
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


  @Override
  public void onDestroy() {
    // TODO Auto-generated method stub
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }

  private IEvent<Object> _event;

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    _event = (IEvent<Object>) activity;
  }
}
