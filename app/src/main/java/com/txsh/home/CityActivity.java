package com.txsh.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import android.widget.TextView;
import cn.ml.base.widget.sample.MLScrollGridView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.txsh.adapter.TXCarHotAdapter;
import com.txsh.adapter.TXCityHotAdapter;
import com.txsh.comment.TXHomeActivity;
import com.txsh.market.EventBusModel;
import com.txsh.utils.MLUserCityUtils;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.MLConstants;
import com.zuomei.home.MLHomeCityAdapter;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.login.MLLoginActivity;
import com.zuomei.model.MLHomeCatalogData;
import com.zuomei.model.MLHomeCityData;
import com.zuomei.model.MLHomeCityResponse;
import com.zuomei.model.MLLogin;
import com.zuomei.services.MLHomeServices;

import com.zuomei.widget.sortlistview.CharacterParser;
import com.zuomei.widget.sortlistview.ClearEditText;
import com.zuomei.widget.sortlistview.PinyinCityComparator;
import com.zuomei.widget.sortlistview.PinyinComparator;
import com.zuomei.widget.sortlistview.SideBar;
import com.zuomei.widget.sortlistview.SideBar.OnTouchingLetterChangedListener;
import com.zuomei.widget.sortlistview.SortAdapter;
import com.zuomei.widget.sortlistview.SortCityAdapter;
import java.util.Collections;
import java.util.List;
import org.greenrobot.eventbus.EventBus;


public class CityActivity extends BaseActivity {

  private ListView tx_activity_city_list;
  //城市数据
  private List<MLHomeCityData> mlHomeHotCityDatas;
  private List<MLHomeCityData> mlHomeCityDatas;
  private ImageButton head_left_bt2;
  private TXCityHotAdapter mlHomeCityAdapter;

  private SortCityAdapter sortCityAdapter;
  /***
   * 汉字转换成拼音的类
   */
  private CharacterParser characterParser;
  /**
   * 根据拼音来排列ListView里面的数据类
   */
  private PinyinCityComparator pinyinComparator;

  @ViewInject(R.id.home_sidrbar)
  private SideBar _sidrbar;

  @ViewInject(R.id.home_tv_dialog)
  private TextView _dialogTv;

  private MLScrollGridView mHotCar;
  private String flag = "0";
  private MLLogin mlLogin = new MLLogin();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.tx_activity_citylist);
    ViewUtils.inject(this);
    mlLogin = ((BaseApplication) getApplication()).get_user();
    if (getIntentData() != null) {
      flag = (String) getIntentData();
    }
    initCatalog();
    tx_activity_city_list = (ListView) findViewById(R.id.tx_activity_city_list);
    head_left_bt2 = (ImageButton) findViewById(R.id.top_btn_left);
    head_left_bt2.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View arg0) {
        // TODO Auto-generated method stub
        finish();
      }
    });

  }

  private void initCatalog() {
    // 获取城市
    ZMHttpRequestMessage message = new ZMHttpRequestMessage(
        RequestType.HOME_CITY, null, null, _handler,
        HTTP_RESPONSE_CITY, MLHomeServices.getInstance());
    loadData(CityActivity.this, message);

  }

  //热门车型
  private void initGrid() {
    mlHomeCityAdapter = new TXCityHotAdapter(this, R.layout.item_city_grid);
    View mViewGrid = LayoutInflater.from(this).inflate(R.layout.city_grid, null);
    mHotCar = (MLScrollGridView) mViewGrid.findViewById(R.id.car_grid);
    tx_activity_city_list.addHeaderView(mViewGrid);
    mHotCar.setAdapter(mlHomeCityAdapter);
    mHotCar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
          MLHomeCityData mlHomeCityData = (MLHomeCityData) parent.getItemAtPosition(position);
          BaseApplication._currentCity = mlHomeCityData.id;
          BaseApplication.aCache.put("cityid", mlHomeCityData.id);
          BaseApplication.aCache.put("cityname", mlHomeCityData.cityName);
          MLUserCityUtils
              .saveCityData(mlLogin.Id, mlLogin.name, mlHomeCityData.id, mlHomeCityData.cityName);
          if (flag.equals("0")) {
            Intent intent = new Intent();
            intent.putExtra("city", mlHomeCityData.cityName);
            EventBus.getDefault().post(
                new EventBusModel("refuse"));
            setResult(1, intent);
          } else {
            Intent intent = new Intent(CityActivity.this, TXHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
          }
          finish();
        } catch (Exception e) {
        }
      }
    });
  }

  private void initView() {
    characterParser = CharacterParser.getInstance();
    pinyinComparator = new PinyinCityComparator();

    filledData();

    if (!mlHomeHotCityDatas.isEmpty()) {
      //热门车型
      initGrid();
      mlHomeCityAdapter.setData(mlHomeHotCityDatas);
    }

    // 根据a-z进行排序源数据
    Collections.sort(mlHomeCityDatas, pinyinComparator);
    sortCityAdapter = new SortCityAdapter(this, mlHomeCityDatas);
    tx_activity_city_list.setAdapter(sortCityAdapter);

    _sidrbar.setTextView(_dialogTv);

    //设置右侧触摸监听
    _sidrbar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
      @Override
      public void onTouchingLetterChanged(String s) {
        //该字母首次出现的位置
        int position = sortCityAdapter.getPositionForSection(s.charAt(0));
        if (position != -1) {
          tx_activity_city_list.setSelection(position);
        }
      }
    });

    tx_activity_city_list.setOnItemClickListener(new OnItemClickListener() {

      @Override
      public void onItemClick(AdapterView<?> parent, View arg1, int position,
          long arg3) {
        try {
          MLHomeCityData mlHomeCityData = (MLHomeCityData) parent.getItemAtPosition(position);
          BaseApplication._currentCity = mlHomeCityData.id;
          BaseApplication.aCache.put("cityid", mlHomeCityData.id);
          BaseApplication.aCache.put("cityname", mlHomeCityData.cityName);
          MLUserCityUtils
              .saveCityData(mlLogin.Id, mlLogin.name, mlHomeCityData.id, mlHomeCityData.cityName);
          if (flag.equals("0")) {
            Intent intent = new Intent();
            intent.putExtra("city", mlHomeCityData.cityName);
            EventBus.getDefault().post(
                new EventBusModel("refuse"));
            setResult(1, intent);
          } else {
            Intent intent = new Intent(CityActivity.this, TXHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
          }
          finish();
        } catch (Exception e) {
        }
      }
    });
  }

  /**
   * 为ListView填充数据
   */
  private List<MLHomeCityData> filledData() {

    for (MLHomeCityData data : mlHomeCityDatas) {
      String pinyin = characterParser.getSelling(data.cityName);
      String sortString = pinyin.substring(0, 1).toUpperCase();
      if (data.cityName.equals("泗水")) {
        data.sortLetters = "S";
      } else {
        if (sortString.matches("[A-Z]")) {
          data.sortLetters = sortString.toUpperCase();
        } else {
          data.sortLetters = "#";
        }
      }
    }
    return mlHomeCityDatas;
  }

  private static final int HTTP_RESPONSE_CITY = 0;
  private Handler _handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      // dismissProgressDialog();
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
        // 获取城市列表
        case HTTP_RESPONSE_CITY: {
          MLHomeCityResponse ret = (MLHomeCityResponse) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            mlHomeCityDatas = ret.datas.cityList;
            mlHomeHotCityDatas = ret.datas.hotCityList;
            initView();
          } else {
            showMessage("获取城市失败!");
          }
          break;
        }
        default:
          break;
      }
    }
  };
}
