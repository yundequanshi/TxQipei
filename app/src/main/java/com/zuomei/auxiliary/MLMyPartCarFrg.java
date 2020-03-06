package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.ml.base.widget.sample.MLNoScrollListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.txsh.R;
import com.txsh.adapter.SelectCarLvAdapter;
import com.txsh.adapter.TXCarHotAdapter;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLHomeCatalogData;
import com.zuomei.model.MLHomeCatalogResponse;
import com.zuomei.model.TXCarTypeResponse;
import com.zuomei.model.TXHomeCatalogResponse;
import com.zuomei.services.MLHomeServices;
import com.zuomei.utils.MLToolUtil;
import com.zuomei.widget.sortlistview.CharacterParser;
import com.zuomei.widget.sortlistview.ClearEditText;
import com.zuomei.widget.sortlistview.PinyinComparator;
import com.zuomei.widget.sortlistview.SideBar;
import com.zuomei.widget.sortlistview.SideBar.OnTouchingLetterChangedListener;
import com.zuomei.widget.sortlistview.SortAdapter;

import java.util.Collections;
import java.util.List;

import cn.ml.base.utils.IEvent;
import cn.ml.base.utils.MLStrUtil;
import cn.ml.base.widget.sample.MLScrollGridView;
import org.greenrobot.eventbus.EventBus;

/**
 * 选择车辆品牌
 *
 * @author Marcello
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyPartCarFrg extends BaseFragment {

  @ViewInject(R.id.home_lv_car)
  private ListView _carLv;
  @ViewInject(R.id.home_sidrbar)
  private SideBar _sidrbar;
  @ViewInject(R.id.home_tv_dialog)
  private TextView _dialogTv;
  @ViewInject(R.id.home_et_search)
  private EditText _searchEt;
  @ViewInject(R.id.drawer_layout)
  private DrawerLayout mDrawerLayout;
  @ViewInject(R.id.right_drawer_layout)
  private RelativeLayout mRightLayout;
  @ViewInject(R.id.lv_select_car)
  private ListView lvSelectCar;

  private Context _context;
  private MLHomeCatalogData _catologData;
  private List<MLHomeCatalogData> _lists;
  private List<MLHomeCatalogData> _listsHot;
  private View view;
  private TXCarHotAdapter mAdapterHot;
  private SelectCarLvAdapter selectCarLvAdapter;

  private SortAdapter adapter;
  private ClearEditText mClearEditText;
  private CharacterParser characterParser;
  private PinyinComparator pinyinComparator;
  public static String index;
  public static MLMyPartCarFrg INSTANCE = null;
  private MLScrollGridView mHotCar;

  public static MLMyPartCarFrg instance(Object obj) {
    index = (String) obj;
    INSTANCE = new MLMyPartCarFrg();
    return INSTANCE;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.incident_car, container, false);
    ViewUtils.inject(this, view);
    initData(HTTP_RESPONSE_CATALOG);
    if (MLStrUtil.compare(index, "384")) {
      _searchEt.setHint("亲，请搜索地区或物流快递名称");
    }
    view.setFocusable(true);//这个和下面的这个命令必须要设置了，才能监听back事件。
    view.setFocusableInTouchMode(true);
    _context = inflater.getContext();
    initGrid();
    initTwoLevel();
    return view;
  }

  private void initTwoLevel() {
    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    selectCarLvAdapter = new SelectCarLvAdapter(getActivity(), R.layout.item_select_car);
    lvSelectCar.setAdapter(selectCarLvAdapter);
    lvSelectCar.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        _catologData = (MLHomeCatalogData) parent.getAdapter().getItem(position);
        toActivity(getActivity(), MLConstants.HOME_BUSINESS_LIST, _catologData);
      }
    });
  }


  //热门车型
  private void initGrid() {
    mAdapterHot = new TXCarHotAdapter(_context, R.layout.item_car_grid);
    View mViewGrid = LayoutInflater.from(_context).inflate(R.layout.car_grid, null);
    mHotCar = (MLScrollGridView) mViewGrid.findViewById(R.id.car_grid);
    _carLv.addHeaderView(mViewGrid);
    mHotCar.setAdapter(mAdapterHot);
    mHotCar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        _catologData = (MLHomeCatalogData) parent.getAdapter().getItem(position);
        initTwoLevelData(_catologData.id);
      }
    });

  }

  private void initData(int position) {
    //获取分类
    ZMRequestParams catalogParam = new ZMRequestParams();
    catalogParam.addParameter(MLConstants.PARAM_HOME_CATALOG, index);
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.TX_HOME_CATALOG, null,
        catalogParam, _handler, position,
        MLHomeServices.getInstance());
    loadDataWithMessage(_context, null, message1);
  }

  private void initTwoLevelData(String superId) {
    //获取分类
    ZMRequestParams catalogParam = new ZMRequestParams();
    catalogParam.addParameter("superId", superId);
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.GET_SECOND_COMPANYTYPE,
        null,
        catalogParam, _handler, HTTP_RESPONSE_CATALOG_CAR_TWO_LEVEL,
        MLHomeServices.getInstance());
    loadDataWithMessage(_context, null, message1);
  }

  private void initView() {
    characterParser = CharacterParser.getInstance();
    pinyinComparator = new PinyinComparator();

    filledData();

    //热门车型
    mAdapterHot.setData(_listsHot);

    // 根据a-z进行排序源数据
    Collections.sort(_lists, pinyinComparator);
    adapter = new SortAdapter(_context, _lists);
    _carLv.setAdapter(adapter);

    _sidrbar.setTextView(_dialogTv);

    //设置右侧触摸监听
    _sidrbar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
      @Override
      public void onTouchingLetterChanged(String s) {
        //该字母首次出现的位置
        int position = adapter.getPositionForSection(s.charAt(0));
        if (position != -1) {
          _carLv.setSelection(position);
        }
      }
    });

    _carLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        _catologData = (MLHomeCatalogData) parent.getAdapter().getItem(position);
        initTwoLevelData(_catologData.id);
      }
    });
  }

  /**
   * @description 搜索
   * @author marcello
   */
  @OnClick(R.id.btn_search)
  public void searchOnClick(View view) {

    String text = _searchEt.getText().toString();
    if (MLToolUtil.isNull(text)) {
      showMessageWarning("请输入关键字!");
      return;
    }
    _catologData = new MLHomeCatalogData();
    _catologData.id = (String) index;
    _catologData.superId = "0";
    _catologData.searchKeyWord = text;
    _event.onEvent(_catologData, MLConstants.HOME_CAR_SEARCH);
  }

  /**
   * @description 品牌
   * @author marcello
   */
  @OnItemClick(R.id.home_lv_car)
  public void brankOnItemClick(AdapterView<?> arg0, View arg1, int arg2,
      long arg3) {
    _catologData = (MLHomeCatalogData) arg0.getAdapter().getItem(arg2);
    EventBus.getDefault().postSticky(_catologData);
    ((MLAuxiliaryActivity) _context).finish();

  }

  @OnClick(R.id.top_btn_left)
  public void backOnClick(View view) {
    getActivity().finish();
  }

  /**
   * 为ListView填充数据
   */
  private List<MLHomeCatalogData> filledData() {

    for (MLHomeCatalogData data : _lists) {
      if (data.name.contains("讴")) {
        data.sortLetters = "O";
      } else {
        String pinyin = characterParser.getSelling(data.name);
        String sortString = pinyin.substring(0, 1).toUpperCase();
        if (sortString.matches("[A-Z]")) {
          data.sortLetters = sortString.toUpperCase();
        } else {
          data.sortLetters = "#";
        }
      }
    }
    return _lists;
  }

  private static final int HTTP_RESPONSE_CATALOG = 1;
  private static final int HTTP_RESPONSE_CATALOG_CAR_TWO_LEVEL = 3;
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
        //      showMessage(error.errorMessage);
        return;
      }
      switch (msg.what) {
        //获取分类
        case HTTP_RESPONSE_CATALOG: {
          TXHomeCatalogResponse ret = (TXHomeCatalogResponse) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            _lists = ret.datas.typeList;
            _listsHot = ret.datas.hotTypeList;
            initView();
          } else {
            showMessage("获取分类失败!");
          }
          break;
        }
        //获取二级车型
        case HTTP_RESPONSE_CATALOG_CAR_TWO_LEVEL: {
          TXCarTypeResponse ret = (TXCarTypeResponse) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            if (ret.datas.isEmpty()) {
              toActivity(getActivity(), MLConstants.HOME_BUSINESS_LIST, _catologData);
            } else {
              if (mDrawerLayout.isDrawerOpen(mRightLayout)) {
                mDrawerLayout.closeDrawer(mRightLayout);
              } else {
                mDrawerLayout.openDrawer(mRightLayout);
              }
              selectCarLvAdapter.setData(ret.datas);
            }
          } else {
            showMessage("获取二级失败!");
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
