package com.qipei.interact;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.baichang.android.utils.BCDialogUtil;
import com.baichang.android.utils.BCStringUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qipei.model.ParseInfoData;
import com.txsh.R;
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.base.MLEventBusModel;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.home.MLMessageAdapter;
import com.zuomei.home.MLMessagePhotoPop;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMHttpType.RequestType;
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
import java.util.List;

import cn.ml.base.utils.IEvent;
import org.greenrobot.eventbus.EventBus;

/**
 * 我的互动
 *
 * @author Marcello
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLInteractReplyFrg extends BaseFragment {

  public static MLInteractReplyFrg INSTANCE = null;

  public static MLInteractReplyFrg instance() {
    INSTANCE = new MLInteractReplyFrg();
    return INSTANCE;
  }

  /*
   * @ViewInject(R.id.message_lv) private PullToRefreshListView
   * _pullToRefreshLv;
   */
  @ViewInject(R.id.message_lv)
  private AbPullToRefreshView _pullToRefreshLv;
  @ViewInject(R.id.mListView)
  private ListView _messageLv;

  @ViewInject(R.id.rl_root)
  private RelativeLayout _root;

  @ViewInject(R.id.interact_rl_reply)
  private RelativeLayout _replyLy;

  @ViewInject(R.id.interact_et_reply)
  private EditText _replyEt;

  @ViewInject(R.id.interact_btn_reply)
  private Button _replyBtn;

	/*
   * @ViewInject(R.id.message_tab) private MLTabGroup _messageTab;
	 */

  @ViewInject(R.id.message_ib_add)
  private ImageButton _addBtn;

  /**
   * 互动消息列表
   */
  private List<MLMessageData> _messageData;

  private int _replyPositiion;
  private MLMessageAdapter _messageAdapter;
  private Context _context;
  List<String[]> message;

  private MLLogin user;
  private int tab_state;
  private String isCollection = "0";
  private String isPraise = "0";

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.my_message, null);
    ViewUtils.inject(this, view);

    _context = getActivity();
    initD();
    // initData();
    initMyData();
    initView();
    return view;
  }

  private void initD() {
    // TODO Auto-generated method stub
    user = ((BaseApplication) getActivity().getApplication()).get_user();
  }

  private void initData() {
    if (user == null) {
      return;
    }
    // ZMRequestParams params = new ZMRequestParams();
    // if(user.isDepot){
    // params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,user.Id);
    // }else{
    // params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,user.Id);
    // }
    // ZMHttpRequestMessage message1 = new
    // ZMHttpRequestMessage(RequestType.MY_MESSAGE, null, params, _handler,
    // HTTP_RESPONSE_MESSSAGE_LIST, MLMessageServices.getInstance());
    // loadDataWithMessage(_context, null, message1);

    ZMRequestParams params = new ZMRequestParams();
    if (user.isDepot) {
      params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID, user.Id);
    } else {
      params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1, user.Id);
    }
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(
        RequestType.MY_MESSAGE_ME, null, params, _handler,
        HTTP_RESPONSE_MESSSAGE_LIST, MLMessageServices.getInstance());
    loadDataWithMessage(_context, null, message1);

  }

  private void pageData() {
    if (_messageData == null) {
      return;
    }
    // 获取互动列表
    ZMRequestParams param = new ZMRequestParams();

    if (user.isDepot) {
      param.addParameter(MLConstants.PARAM_LOGIN_DEPORTID, user.Id);
    } else {
      param.addParameter(MLConstants.PARAM_HOME_BUSINESSID1, user.Id);
    }
    String lastId = _messageData.get(_messageData.size() - 1).info.id + "";
    // param.addParameter("pageNum","2");
    param.addParameter(MLConstants.PARAM_MESSAGE_LASTID, lastId);
    RequestType type;
    if (tab_state == 0) {
      type = RequestType.MY_MESSAGE;
    } else {
      type = RequestType.MY_MESSAGE_ME;
    }
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(type, null,
        param, _handler, HTTP_RESPONSE_MESSSAGE_PAGE,
        MLMessageServices.getInstance());
    loadDataWithMessage(_context, null, message1);
  }

  // 举报
  /*
   * private void report(){ ZMRequestParams params = new ZMRequestParams();
	 * 
	 * if(user.isDepot){
	 * params.addParameter(MLConstants.PARAM_HOME_DEPORT,user.Id); }else{
	 * params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,user.Id); }
	 * params.
	 * addParameter(MLConstants.PARAM_MESSAGE_ITID,_messageData.get(_replyPositiion
	 * ).id);
	 * 
	 * ZMHttpRequestMessage message1 = new
	 * ZMHttpRequestMessage(RequestType.MESSAGE_REPORT, null, params, _handler,
	 * HTTP_RESPONSE_MESSSAGE_REPORT, MLMessageServices.getInstance());
	 * loadDataWithMessage(_context, null, message1); }
	 */
  private void initView() {
    // _messageLv = _pullToRefreshLv.getRefreshableView();
    _messageAdapter = new MLMessageAdapter(_context, _updateHandler);
    _messageLv.setAdapter(_messageAdapter);
    _pullToRefreshLv
        .setOnHeaderRefreshListener(new OnHeaderRefreshListener() {

          @Override
          public void onHeaderRefresh(AbPullToRefreshView view) {
            if (tab_state == 0) {
              initData();
            } else {
              initMyData();
            }
          }
        });
    ;
    _pullToRefreshLv.setOnFooterLoadListener(new OnFooterLoadListener() {

      @Override
      public void onFooterLoad(AbPullToRefreshView view) {
        // TODO Auto-generated method stub
        pageData();
      }
    });
    _messageLv.setOnScrollListener(new OnScrollListener() {
      @Override
      public void onScrollStateChanged(AbsListView arg0, int arg1) {
        _replyLy.setVisibility(View.GONE);
      }

      @Override
      public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

      }
    });
    // _messageLv.setSelector(getResources().getDrawable(R.drawable.message_list_selector));
    _messageLv.setOnItemLongClickListener(new OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
          int arg2, long arg3) {
        if (tab_state == 1) {
          return false;
        }
        final String id = _messageData.get(arg2).info.id;
        _pullToRefreshLv.onHeaderRefreshFinish();
        _pullToRefreshLv.onFooterLoadFinish();
        Builder builder = new Builder(_context,
            AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setItems(new String[]{"删除"},
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface arg0, int arg1) {
                // report();
                delMessage(id);
                arg0.dismiss();
              }
            }).setTitle("请选择");
        builder.show();
        return false;
      }
    });

		/*
     * _messageTab.setRadioCheckedCallBack(new IRadioCheckedListener() {
		 * 
		 * @Override public void radioChecked(RadioButton rb, int index) {
		 * tab_state=index; if(index==0){ initData(); }else{ initMyData(); }
		 * 
		 * } });
		 */
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

  protected void reviewMessageList(List<MLMessageData> datas) {
    _messageAdapter.setData(datas);
  }

  private void initMyData() {

    ZMRequestParams params = new ZMRequestParams();
    if (user.isDepot) {
      params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID, user.Id);
    } else {
      params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1, user.Id);
    }
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(
        RequestType.MY_MESSAGE_ME, null, params, _handler,
        HTTP_RESPONSE_MESSSAGE_LIST, MLMessageServices.getInstance());
    loadDataWithMessage(_context, null, message1);
  }

  private void delMessage(String id) {

    ZMRequestParams params = new ZMRequestParams();
    params.addParameter("id", id);
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(
        RequestType.MY_MESSAGE_DEL, null, params, _handler,
        HTTP_RESPONSE_MESSSAGE_DEL, MLMessageServices.getInstance());
    loadDataWithMessage(_context, null, message1);
  }

  @OnClick(R.id.interact_btn_reply)
  public void replyOnClick(View view) {

    String content = _replyEt.getText().toString();
    if (MLToolUtil.isNull(content)) {
      showMessage("评论内容不能为空!");
      return;
    }

    String name = ((BaseApplication) getActivity().getApplication())
        .get_user().name;
    MLMessageCommentData comment = new MLMessageCommentData();
    /*
		 * comment.userName=name; comment.content= content;
		 */
    comment.userName = name;
    comment.content = content;

    ZMRequestParams param = new ZMRequestParams();

    if (user.isDepot) {
      param.addParameter(MLConstants.PARAM_LOGIN_DEPORTID, user.Id);
    } else {
      param.addParameter(MLConstants.PARAM_HOME_BUSINESSID1, user.Id);
    }
    param.addParameter(MLConstants.PARAM_MESSAGE_CONTENT, content);
    param.addParameter(MLConstants.PARAM_MESSAGE_USERNAME, name);
    param.addParameter(MLConstants.PARAM_MESSAGE_ITID,
        _messageData.get(_replyPositiion).info.id);

    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(
        RequestType.MY_MESSAGE_REPLY, null, param, _handler,
        HTTP_RESPONSE_MESSSAGE_REPLY, MLMessageServices.getInstance());
    loadData(_context, message1);

    _messageData.get(_replyPositiion).info.interactionComment.add(comment);
    _messageAdapter.setData(_messageData);

    _replyLy.setVisibility(View.GONE);
  }

  @OnClick(R.id.top_back)
  public void backOnClick(View view) {
    ((MLAuxiliaryActivity) _context).finish();
  }

  public Handler _updateHandler = new Handler() {

    @Override
    public void handleMessage(Message msg) {
      // TODO Auto-generated method stub
      super.handleMessage(msg);

      switch (msg.what) {
        case 1:
          // 评论
          _replyLy.setVisibility(View.VISIBLE);
          _replyPositiion = msg.arg1;
          break;
        case 2:
          // 图片
          String path = APIConstants.API_IMAGE + "?id="
              + _messageData.get(msg.arg1).info.images;
          MLMessagePhotoPop _pop = new MLMessagePhotoPop(getActivity(),
              path);
          _pop.showAtLocation(_root, Gravity.CENTER, 0, 0);
          break;
        case 3: {
          final MLMessageData d = (MLMessageData) msg.obj;
          if (d.userType.equalsIgnoreCase("1")) {
            //汽修厂
						/*Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + d.user.userPhone));
						_context.startActivity(intent);*/
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

  private static final int HTTP_RESPONSE_MESSSAGE_LIST = 1;
  private static final int HTTP_RESPONSE_MESSSAGE_REPLY = 2;
  private static final int HTTP_RESPONSE_MESSSAGE_TEST = 3;
  private static final int HTTP_RESPONSE_MESSSAGE_REPORT = 4;
  private static final int HTTP_RESPONSE_MESSSAGE_PAGE = 5;
  private static final int HTTP_RESPONSE_MESSSAGE_DEL = 6;
  private static final int HTTP_RESPONSE_HD_COLLECTION = 7;
  private static final int HTTP_RESPONSE_HD_DIANZAN = 8;
  private static final int HTTP_RESPONSE_HD_JUBAO = 9;

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
        // 获取消息列表
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
        // 获取分页消息
        case HTTP_RESPONSE_MESSSAGE_PAGE: {
          MLMessageListResponse ret = (MLMessageListResponse) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            if (ret.datas.size() == 0) {
              _pullToRefreshLv.onFooterLoadFinish();
              break;
            }
            _messageData.addAll(ret.datas);
            reviewMessageList(_messageData);
          } else {
            showMessageError("获取消息列表失败!");
          }
          _pullToRefreshLv.onFooterLoadFinish();
          break;
        }
        // 评论
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
        case HTTP_RESPONSE_MESSSAGE_DEL: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            initData();
            showMessageSuccess("删除成功!");
          } else {
            showMessageError("删除失败!");
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
  private IEvent<Object> _event;

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    _event = (IEvent<Object>) activity;
  }
}
