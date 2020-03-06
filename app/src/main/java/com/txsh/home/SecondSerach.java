package com.txsh.home;

import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;

import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.MLConstants;
import com.zuomei.home.MLAccidentCarAdapter;
import com.zuomei.home.MLLeaveAdapter;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLAccidentDetailData;
import com.zuomei.model.MLAccidentListResponse;
import com.zuomei.model.MLLeaveData;
import com.zuomei.model.MLLeaveResponse;
import com.zuomei.model.MLLogin;
import com.zuomei.services.MLAccidentServices;
import com.zuomei.services.MLAdvanServices;
import com.zuomei.services.MLLeaveServices;
import com.zuomei.utils.MLToolUtil;
import com.zuomei.widget.MLTabGroup;
import com.zuomei.widget.MLTabGroup.IRadioCheckedListener;

import cn.ml.base.utils.IEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 事故车
 *
 * @author Marcello
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SecondSerach extends BaseFragment {

  public static SecondSerach INSTANCE = null;
  static String tabaa;

  public static SecondSerach instance(Object obj) {
    INSTANCE = new SecondSerach();
    tabaa = String.valueOf(obj);
    return INSTANCE;
  }

  @ViewInject(R.id.accident_lv_car)
  private AbPullToRefreshView _pullToRefreshLv;
  @ViewInject(R.id.mListView)
  private ListView _carLv;

  @ViewInject(R.id.accident_et_search)
  private EditText _searchEt;

  @ViewInject(R.id.accident_iv_search)
  private ImageView _searchBtn;

  @ViewInject(R.id.accident_tab)
  private MLTabGroup _tab;

  @ViewInject(R.id.accident_btn_add)
  private Button _add;

  @ViewInject(R.id.accident_btn_baojia)
  private Button _baojia;

  @ViewInject(R.id.accident_btn_baojia)
  private Button _baojiaBtn;

  public List<MLAccidentDetailData> _accidentDatas;
  public List<MLLeaveData> _leaveDatas;

  private MLAccidentCarAdapter _carAdapter;
  private MLLeaveAdapter _leaveAdapter;
  private MLLeaveAdapter _advanAdapter;
  private String keyWord;
  private Context _context;
  private int tab_state;
  private MLLogin mUser;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    EventBus.getDefault().register(this);
    tab_state = Integer.parseInt(tabaa);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.secondserach, null);
    ViewUtils.inject(this, view);

    _context = inflater.getContext();
    mUser = BaseApplication.getInstance().get_user();

    initView();
//		initData();
    return view;
  }

  // 获取二手件
  private void initLeave() {
    _carLv.setAdapter(_leaveAdapter);
    ZMRequestParams params = new ZMRequestParams();
    if (!MLToolUtil.isNull(keyWord)) {
      params.addParameter("key", keyWord);
    }
    params.addParameter("cityId", BaseApplication._currentCity);
    ZMHttpRequestMessage message = new ZMHttpRequestMessage(
        RequestType.LEAVE_LIST, null, params, _handler,
        HTTP_RESPONSE_LEAVE_LIST, MLLeaveServices.getInstance());
    loadDataWithMessage(_context, null, message);
  }

  // //获取优势件
  // private void initAdvan() {
  // _carLv.setAdapter(_advanAdapter);
  // ZMRequestParams params = new ZMRequestParams();
  // if(!MLToolUtil.isNull(keyWord)){
  // params.addParameter("key",keyWord);
  // }
  // params.addParameter("cityId",BaseApplication._currentCity);
  // ZMHttpRequestMessage message = new
  // ZMHttpRequestMessage(RequestType.ADVAN_LIST, null, params, _handler,
  // HTTP_RESPONSE_ADVAN_LIST, MLAdvanServices.getInstance());
  // loadDataWithMessage(_context, null, message);
  // }

  // 获取事故车
  private void initData() {
    _carLv.setAdapter(_carAdapter);

    ZMRequestParams params = new ZMRequestParams();
    if (!MLToolUtil.isNull(keyWord)) {
      params.addParameter(MLConstants.PARAM_MY_ACCIDENT, keyWord);
    }
    params.addParameter("cityId", BaseApplication._currentCity);

    ZMHttpRequestMessage message = new ZMHttpRequestMessage(
        RequestType.AACIDENT_LIST, null, params, _handler,
        HTTP_RESPONSE_ACCIDENT_LIST, MLAccidentServices.getInstance());
    loadDataWithMessage(_context, null, message);

  }

  private void initView() {
    // TODO Auto-generated method stub
    _carLv.setSelector(getResources().getDrawable(
        R.drawable.message_list_selector));
    _carAdapter = new MLAccidentCarAdapter(_context);
    _leaveAdapter = new MLLeaveAdapter(_context);
    _advanAdapter = new MLLeaveAdapter(_context);

    MLLogin user = BaseApplication.getInstance().get_user();
    if (!user.isDepot) {
      _baojiaBtn.setVisibility(View.GONE);
    }

    _carLv.setAdapter(_carAdapter);
    _pullToRefreshLv
        .setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
          @Override
          public void onHeaderRefresh(AbPullToRefreshView view) {
            if (tab_state == 0) {
              initData();
            } else if (tab_state == 1) {
              initLeave();
            }

						/*
             * if(MLToolUtil.isNull(keyWord)){ initData(); }else{
						 * search(); }
						 */
          }
        });
    _pullToRefreshLv.setOnFooterLoadListener(new OnFooterLoadListener() {
      @Override
      public void onFooterLoad(AbPullToRefreshView view) {

        if (tab_state == 0) {
          pageData();
        } else if (tab_state == 1) {
          leavePageData();
        } else {
          advanPageData();
        }
      }
    });
    _carLv.setOnItemClickListener(new OnItemClickListener() {

      @Override
      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
          long arg3) {

        if (tab_state == 0) {
          MLAccidentDetailData data = (MLAccidentDetailData) arg0
              .getAdapter().getItem(arg2);
          Intent intent = new Intent();
          intent.setClass(_context, MLAuxiliaryActivity.class);
          intent.putExtra("data", MLConstants.ACCIDENT_DETAIL);
          intent.putExtra("obj", data);
          startActivity(intent);
        } else if (tab_state == 1) {
          MLLeaveData data = (MLLeaveData) arg0.getAdapter().getItem(
              arg2);
          Intent intent = new Intent();
          intent.setClass(_context, MLAuxiliaryActivity.class);
          intent.putExtra("data", MLConstants.MY_LEAVE_DETAIL);
          intent.putExtra("obj", data);
          startActivity(intent);
        } else {
          MLLeaveData data = (MLLeaveData) arg0.getAdapter().getItem(
              arg2);
          Intent intent = new Intent();
          intent.setClass(_context, MLAuxiliaryActivity.class);
          intent.putExtra("data", MLConstants.MY_ADCAN_DETAIL);
          intent.putExtra("obj", data);
          startActivity(intent);
        }

      }
    });

    _tab.setRadioCheckedCallBack(new IRadioCheckedListener() {
      @Override
      public void radioChecked(RadioButton rb, int index) {
        tab_state = index;
        keyWord = "";
        _searchEt.setText("");
        if (index == 0) {
          // 事故车
          _add.setVisibility(View.INVISIBLE);
          _baojia.setVisibility(View.INVISIBLE);
          initData();
        } else if (index == 1) {
          // 二手件
          if (!mUser.isDepot) {
            _add.setVisibility(View.INVISIBLE);
          } else {
            _add.setVisibility(View.INVISIBLE);
          }
          _baojia.setVisibility(View.INVISIBLE);
          initLeave();
        }
      }
    });
  }

  @OnClick(R.id.accident_btn_add)
  public void addOnClick(View view) {
    Intent intent = new Intent();
    intent.setClass(_context, MLAuxiliaryActivity.class);
    if (tab_state == 1 && mUser.isDepot) {
      intent.putExtra("data", MLConstants.MY_LEAVE_ADD1);
      startActivityForResult(intent, 3);
    } else if (tab_state == 2 && !mUser.isDepot) {
      intent.putExtra("data", MLConstants.MY_ADVAN_ADD1);
      // startActivity(intent);
      startActivityForResult(intent, 2);
    } else if (tab_state == 0) {
      intent.putExtra("data", MLConstants.ACCIDENT_ADD);
      startActivityForResult(intent, 1);
    }

  }

  @OnClick(R.id.accident_btn_baojia)
  public void bjOnClick(View view) {
    Intent intent = new Intent();
    intent.setClass(_context, MLAuxiliaryActivity.class);
    intent.putExtra("data", MLConstants.PART_DEPOT);
    startActivity(intent);
  }

  @OnClick(R.id.top_back)
  public void top_backOnClick(View view) {
    getActivity().finish();
  }

  @Subscribe
  public void refresh(String s) {
    initData();
  }

  @OnClick(R.id.accident_iv_search)
  public void searchOnClick(View view) {
    keyWord = _searchEt.getText().toString();
    if (tabaa.equals("0")) {
      initData();
    } else if (tabaa.equals("1")) {
      initLeave();
    }
  }

  public void search() {
    ZMRequestParams params = new ZMRequestParams();
    params.addParameter(MLConstants.PARAM_MY_ACCIDENT, keyWord);
    params.addParameter("cityId", BaseApplication._currentCity);
    ZMHttpRequestMessage message = new ZMHttpRequestMessage(
        RequestType.AACIDENT_LIST, null, params, _handler,
        HTTP_RESPONSE_ACCIDENT_LIST, MLAccidentServices.getInstance());
    loadDataWithMessage(_context, null, message);
  }

  public void pageData() {
    ZMRequestParams params = new ZMRequestParams();
    if (!MLToolUtil.isNull(keyWord)) {
      params.addParameter(MLConstants.PARAM_MY_ACCIDENT, keyWord);
    }
    params.addParameter("cityId", BaseApplication._currentCity);
    String lastId = _accidentDatas.get(_accidentDatas.size() - 1).info.id
        + "";
    params.addParameter(MLConstants.PARAM_MESSAGE_LASTID, lastId);
    ZMHttpRequestMessage message = new ZMHttpRequestMessage(
        RequestType.AACIDENT_LIST, null, params, _handler,
        HTTP_RESPONSE_ACCIDENT_PAGE, MLAccidentServices.getInstance());
    loadDataWithMessage(_context, null, message);
  }

  public void leavePageData() {
    // MLLogin user =
    // ((BaseApplication)getActivity().getApplication()).get_user();
    ZMRequestParams params = new ZMRequestParams();
		/*
		 * if(user.isDepot){
		 * params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,user.Id); }else{
		 * params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,user.Id); }
		 */
    params.addParameter("cityId", BaseApplication._currentCity);

    if (!MLToolUtil.isNull(keyWord)) {
      params.addParameter("key", keyWord);
    }
    String lastId = _leaveDatas.get(_leaveDatas.size() - 1).info.id + "";
    params.addParameter(MLConstants.PARAM_MESSAGE_LASTID, lastId);
    ZMHttpRequestMessage message = new ZMHttpRequestMessage(
        RequestType.LEAVE_LIST, null, params, _handler,
        HTTP_RESPONSE_LEAVE_PAGE, MLLeaveServices.getInstance());
    loadDataWithMessage(_context, null, message);
  }

  public void advanPageData() {
    // MLLogin user =
    // ((BaseApplication)getActivity().getApplication()).get_user();
    ZMRequestParams params = new ZMRequestParams();
		/*
		 * if(user.isDepot){
		 * params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,user.Id); }else{
		 * params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,user.Id); }
		 */
    params.addParameter("cityId", BaseApplication._currentCity);
    if (!MLToolUtil.isNull(keyWord)) {
      params.addParameter("key", keyWord);
    }
    String lastId = _leaveDatas.get(_leaveDatas.size() - 1).info.id + "";
    params.addParameter(MLConstants.PARAM_MESSAGE_LASTID, lastId);
    ZMHttpRequestMessage message = new ZMHttpRequestMessage(
        RequestType.ADVAN_LIST, null, params, _handler,
        HTTP_RESPONSE_ADVAN_PAGE, MLAdvanServices.getInstance());
    loadDataWithMessage(_context, null, message);
  }

  private void starActivity(int position) {
    Intent intent = new Intent();
    intent.setClass(_context, MLAuxiliaryActivity.class);
    intent.putExtra("data", position);
    startActivity(intent);
		/*
		 * ((MLHomeActivity)_context).overridePendingTransition(android.R.anim.
		 * slide_in_left, android.R.anim.slide_out_right);
		 */
  }

  private static final int HTTP_RESPONSE_ACCIDENT_LIST = 0;
  private static final int HTTP_RESPONSE_ACCIDENT_PAGE = 1;
  private static final int HTTP_RESPONSE_LEAVE_LIST = 2;
  private static final int HTTP_RESPONSE_LEAVE_PAGE = 3;
  private static final int HTTP_RESPONSE_ADVAN_LIST = 4;
  private static final int HTTP_RESPONSE_ADVAN_PAGE = 5;

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
        case HTTP_RESPONSE_ACCIDENT_LIST: {
          MLAccidentListResponse ret = (MLAccidentListResponse) msg.obj;
          if (ret.state.equalsIgnoreCase("1") && ret.datas != null) {
            _accidentDatas = ret.datas;
            _carAdapter.setData(ret.datas);
          } else {
            showMessageError("获取事故车列表失败!");
          }
          _pullToRefreshLv.onHeaderRefreshFinish();
          break;
        }
        // 事故车加载更多
        case HTTP_RESPONSE_ACCIDENT_PAGE: {
          MLAccidentListResponse ret = (MLAccidentListResponse) msg.obj;
          if (ret.state.equalsIgnoreCase("1") && ret.datas != null) {
            _accidentDatas.addAll(ret.datas);
            _carAdapter.setData(_accidentDatas);
          } else {
            showMessageError("获取事故车列表失败!");
          }
          _pullToRefreshLv.onFooterLoadFinish();
          break;
        }
        case HTTP_RESPONSE_LEAVE_LIST: {
          MLLeaveResponse ret = (MLLeaveResponse) msg.obj;
          if (ret.state.equalsIgnoreCase("1") && ret.datas != null) {
            _leaveDatas = ret.datas;
            _leaveAdapter.setData(ret.datas);
          } else {
            showMessageError("获取列表失败!");
          }
          _pullToRefreshLv.onHeaderRefreshFinish();
          break;
        }
        // 二手件加载更多
        case HTTP_RESPONSE_LEAVE_PAGE: {
          MLLeaveResponse ret = (MLLeaveResponse) msg.obj;
          if (ret.state.equalsIgnoreCase("1") && ret.datas != null) {
            _leaveDatas.addAll(ret.datas);
            _leaveAdapter.setData(_leaveDatas);
          } else {
            showMessageError("获取列表失败!");
          }
          _pullToRefreshLv.onFooterLoadFinish();
          break;
        }

        case HTTP_RESPONSE_ADVAN_LIST: {
          MLLeaveResponse ret = (MLLeaveResponse) msg.obj;
          if (ret.state.equalsIgnoreCase("1") && ret.datas != null) {
            _leaveDatas = ret.datas;
            _advanAdapter.setData(ret.datas);
          } else {
            showMessageError("获取列表失败!");
          }
          _pullToRefreshLv.onHeaderRefreshFinish();
          break;
        }
        // 优势件加载更多
        case HTTP_RESPONSE_ADVAN_PAGE: {
          MLLeaveResponse ret = (MLLeaveResponse) msg.obj;
          if (ret.state.equalsIgnoreCase("1") && ret.datas != null) {
            _leaveDatas.addAll(ret.datas);
            _advanAdapter.setData(_leaveDatas);
          } else {
            showMessageError("获取列表失败!");
          }
          _pullToRefreshLv.onFooterLoadFinish();
          break;
        }

        default:
          break;
      }
    }
  };

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == MLConstants.RESULT_ACCEIDENT_ADD) {
      initData();
    } else if (requestCode == 3) {
      initLeave();
    }
  }

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
