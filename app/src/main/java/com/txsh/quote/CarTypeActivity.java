package com.txsh.quote;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.ml.base.utils.IEvent;
import cn.ml.base.widget.sample.MLNoScrollListView;
import cn.ml.base.widget.sample.MLScrollGridView;
import com.baichang.android.utils.BCStringUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.txsh.R;
import com.txsh.adapter.SelectCarLvAdapter;
import com.txsh.adapter.TXCarHotAdapter;
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.auxiliary.MLHomeCarView;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.MLEventBusModel;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

public class CarTypeActivity extends BaseActivity {

  @ViewInject(R.id.home_lv_car)
  private ListView _carLv;
  @ViewInject(R.id.home_sidrbar)
  private SideBar _sidrbar;
  @ViewInject(R.id.home_tv_dialog)
  private TextView _dialogTv;

  private MLScrollGridView mHotCar;
  private List<MLHomeCatalogData> _lists = new ArrayList<>();
  private List<MLHomeCatalogData> _listsHot = new ArrayList<>();
  private TXCarHotAdapter mAdapterHot;
  private SortAdapter adapter;
  private CharacterParser characterParser;
  private PinyinComparator pinyinComparator;
  public static String index = "1";
  private SelectCarLvAdapter selectCarLvAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_car_type);
    ViewUtils.inject(this);
    initData(HTTP_RESPONSE_CATALOG);
  }

  //热门车型
  private void initGrid() {
    mAdapterHot = new TXCarHotAdapter(getAty(), R.layout.item_car_grid);
    View mViewGrid = LayoutInflater.from(getAty()).inflate(R.layout.car_grid, null);
    mHotCar = (MLScrollGridView) mViewGrid.findViewById(R.id.car_grid);
    _carLv.addHeaderView(mViewGrid);
    mHotCar.setAdapter(mAdapterHot);
    mHotCar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MLHomeCatalogData mlHomeCatalogData = (MLHomeCatalogData) parent
            .getItemAtPosition(position);
          EventBus.getDefault().post(new MLEventBusModel(Flag.EVENT_CAR_TYPE, mlHomeCatalogData));
          finish();
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
    loadDataWithMessage(getAty(), null, message1);
  }

  private void initView() {
    characterParser = CharacterParser.getInstance();
    pinyinComparator = new PinyinComparator();

    initGrid();
    filledData();
    //热门车型
    mAdapterHot.setData(_listsHot);

    // 根据a-z进行排序源数据
    Collections.sort(_lists, pinyinComparator);
    adapter = new SortAdapter(getAty(), _lists);
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
        MLHomeCatalogData mlHomeCatalogData = (MLHomeCatalogData) parent
            .getItemAtPosition(position);
          EventBus.getDefault().post(new MLEventBusModel(Flag.EVENT_CAR_TYPE, mlHomeCatalogData));
          finish();
      }
    });
  }

  /**
   * 为ListView填充数据
   */
  private List<MLHomeCatalogData> filledData() {

    for (MLHomeCatalogData data : _lists) {
      String pinyin = characterParser.getSelling(data.name);
      String sortString = pinyin.substring(0, 1).toUpperCase();
      if (sortString.matches("[A-Z]")) {
        data.sortLetters = sortString.toUpperCase();
      } else {
        data.sortLetters = "#";
      }
    }
    return _lists;
  }

  private static final int HTTP_RESPONSE_CATALOG = 1;
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
        default:
          break;
      }
    }
  };

  public void back(View view) {
    finish();
  }
}
