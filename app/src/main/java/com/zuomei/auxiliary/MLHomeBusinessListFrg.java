package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.MLConstants;
import com.zuomei.home.MLHomeCityPop;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLHomeBusinessData;
import com.zuomei.model.MLHomeBusinessList;
import com.zuomei.model.MLHomeBusinessResponse;
import com.zuomei.model.MLHomeCatalogData;
import com.zuomei.model.MLHomeCityData;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLHomeServices;
import com.zuomei.utils.MLToolUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.ml.base.utils.IEvent;

/**
 * 首页-商家列表
 *
 * @author Marcello
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLHomeBusinessListFrg extends BaseFragment {

  public static MLHomeBusinessListFrg INSTANCE = null;

  public static MLHomeCatalogData _catalog;

  public static MLHomeBusinessListFrg instance(Object obj) {
    _catalog = (MLHomeCatalogData) obj;
    //	if(INSTANCE==null){
    INSTANCE = new MLHomeBusinessListFrg();
    //	}
    return INSTANCE;
  }

  @ViewInject(R.id.home_lv_business)
  private ListView _businessLv;

  @ViewInject(R.id._refressview)
  private AbPullToRefreshView _refreshView;

  @ViewInject(R.id.home_et_search)
  private EditText _searchEt;

  @ViewInject(R.id.login_register_root)
  private RelativeLayout _root;
  private MLHomeSearchAdapter _searchAdapter;
  private Context _context;
  private MLLogin _user;
  private List<MLHomeBusinessData> _businessData;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.home_business_list, null);
    ViewUtils.inject(this, view);
    _context = inflater.getContext();
    init();
    initData();
    return view;
  }

  private void init() {
    _searchAdapter = new MLHomeSearchAdapter(_context, _callHandler);
    _businessLv.setAdapter(_searchAdapter);

    _refreshView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
      @Override
      public void onHeaderRefresh(AbPullToRefreshView view) {
        initData();
      }
    });
    _refreshView.setOnFooterLoadListener(new OnFooterLoadListener() {
      @Override
      public void onFooterLoad(AbPullToRefreshView view) {
        pageData();
      }
    });

    _businessLv.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view,
          int position, long id) {
        MLHomeBusinessData data = (MLHomeBusinessData) parent.getAdapter().getItem(position);
        Intent intent = new Intent();
        intent.setClass(_context, MLAuxiliaryActivity.class);
        intent.putExtra("data", MLConstants.HOME_BUSINESS_INFO);
        intent.putExtra("obj", (Serializable) data);
        startActivity(intent);
      }
    });

//		_user = BaseApplication._user;
    _user = ((BaseApplication) getActivity().getApplication()).get_user();
  }

  private void initData() {
    _searchEt.setText("");

    if (_catalog == null) {
      return;
    }

    ZMRequestParams catalogParam = new ZMRequestParams();
    catalogParam.addParameter(MLConstants.PARAM_HOME_CARTYPE, _catalog.id);
    catalogParam.addParameter(MLConstants.PARAM_HOME_CITYID, BaseApplication._currentCity);
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.HOME_BUSINESS_LIST, null,
        catalogParam, _handler, HTTP_RESPONSE_BUSINESS_LIST, MLHomeServices.getInstance());
    loadDataWithMessage(_context, null, message1);
  }

  private void pageData() {
    if (_catalog == null) {
      return;
    }
    String lastId = _businessData.get(_businessData.size() - 1).id + "";
    ZMRequestParams params = new ZMRequestParams();
    params.addParameter(MLConstants.PARAM_HOME_CITYID, BaseApplication._currentCity);
    params.addParameter(MLConstants.PARAM_HOME_CARTYPE, _catalog.id);
    params.addParameter(MLConstants.PARAM_MESSAGE_LASTID, lastId);
    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.HOME_BUSINESS_LIST, null,
        params, _handler, HTTP_RESPONSE_SEARCH_PAGE, MLHomeServices.getInstance());
    loadDataWithMessage(_context, null, message2);
  }

  private void search(String text) {
    if (_catalog == null) {
      return;
    }
    String lastId = _businessData.get(_businessData.size() - 1).id + "";
    ZMRequestParams params = new ZMRequestParams();
    params.addParameter(MLConstants.PARAM_HOME_KEY, text);
    params.addParameter(MLConstants.PARAM_HOME_CITYID, BaseApplication._currentCity);
    params.addParameter(MLConstants.PARAM_HOME_TYPEID, _catalog.superId);
//	     params.addParameter(MLConstants.PARAM_MESSAGE_LASTID,lastId);
    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.HOME_SEARCH, null, params,
        _handler, HTTP_RESPONSE_SEARCH, MLHomeServices.getInstance());
    loadData(_context, message2);
  }

  @OnClick(R.id.btn_search)
  public void searchOnClick(View view) {
    String text = _searchEt.getText().toString();
    if (MLToolUtil.isNull(text)) {
      showMessageWarning("请输入要搜索的关键字!");
      return;
    }
    search(text);
  }

  @OnClick(R.id.home_top_back)
  public void top_btn_left(View view) {
    getActivity().finish();
  }

  private MLHomeCityPop _menuWindow;
  private MLHomeBusinessData _data;
  private Handler _callHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      _data = (MLHomeBusinessData) msg.obj;

      final List<MLHomeCityData> datas = new ArrayList<MLHomeCityData>();
      /*	MLHomeCityData data1 =new MLHomeCityData();
    		data1.cityName = _data.phone;*/

      MLHomeCityData data2 = new MLHomeCityData();
      data2.cityName = _data.phone1;

      MLHomeCityData data3 = new MLHomeCityData();
      data3.cityName = _data.phone2;
      Collections.addAll(datas, data2, data3);

      _menuWindow = new MLHomeCityPop(getActivity(), datas, new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
            long arg3) {
          MLHomeCityData data = datas.get(arg2);
          String phoneNum = data.cityName;
          _menuWindow.dismiss();
//
//          Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
//          startActivity(intent);
//          dial("0");
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

  private void dial(String isNetWorkPhone) {
//    		MLLogin user = BaseApplication._user;

    MLLogin user = ((BaseApplication) getActivity().getApplication()).get_user();
			/*if(!user.isDepot){
				//商家不记录
				return;
			}*/
    if (!user.isDepot) {
      return;
    }
    ZMRequestParams catalogParam = new ZMRequestParams();
    catalogParam.addParameter(MLConstants.PARAM_HOME_ISNETWORKPHONE, isNetWorkPhone);
    catalogParam.addParameter(MLConstants.PARAM_LOGIN_DEPORTID, user.Id);
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

  private static final int HTTP_RESPONSE_BUSINESS_LIST = 1;
  private static final int HTTP_RESPONSE_SEARCH_PAGE = 2;
  private static final int HTTP_RESPONSE_SEARCH = 3;
  private static final int HTTP_RESPONSE_CALL = 4;
  private static final int HTTP_RESPONSE_CALL_WEB = 5;
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
        //获取商家列表
        case HTTP_RESPONSE_BUSINESS_LIST: {
          MLHomeBusinessList ret = (MLHomeBusinessList) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            _businessData = ret.datas;
            setBusinessList(ret.datas);
          } else {
            showMessage("获取车辆失败!");
          }
          if (ret.datas != null && ret.datas.size() < 20) {
            _refreshView.setLoadMoreEnable(false);
          } else {
            _refreshView.setLoadMoreEnable(true);
          }
          _refreshView.onHeaderRefreshFinish();
          break;
        }
        case HTTP_RESPONSE_SEARCH_PAGE: {
          MLHomeBusinessList ret = (MLHomeBusinessList) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            if (ret.datas.size() != 0) {
              _businessData.addAll(ret.datas);
              _searchAdapter.setData(_businessData);
            }
          } else {
            showMessageError("搜索失败!");
          }
          _refreshView.onFooterLoadFinish();
          break;
        }
        case HTTP_RESPONSE_SEARCH: {
          MLHomeBusinessResponse ret = (MLHomeBusinessResponse) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            _businessData = ret.datas;
            setBusinessList(ret.datas);
          } else {
            showMessageWarning("没有相关的信息！");
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


  /**
   * @description 返回
   * @author marcello
   */
  @OnClick(R.id.home_top_back)
  public void backOnClick(View view) {
    //	_event.onEvent(null, MLConstants.HOME_CAR);
    getActivity().onBackPressed();
		/*startActivity(new Intent(_context,MLHomeActivity.class));
		((MLAuxiliaryActivity)_context).finish();*/
  }

  protected void setBusinessList(List<MLHomeBusinessData> datas) {
		/*_searchAdapter = new MLHomeSearchAdapter(_context);
		_businessLv.setAdapter(_searchAdapter);*/
    _searchAdapter.setData(datas);
  }

  private IEvent<Object> _event;

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    _event = (IEvent<Object>) activity;
  }


}
