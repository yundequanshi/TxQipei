package com.txsh.shop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import cn.ml.base.widget.pullview.AbPullToRefreshView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.txsh.adapter.TXShopTypeAdapter;
import com.txsh.comment.TXHomeActivity;
import com.txsh.market.EventBusModel;
import com.txsh.model.TXShopListRes;
import com.txsh.model.TXShopPlayListRes;
import com.txsh.model.TXShopTypeListRes;
import com.txsh.model.TXShopTypeListRes.TXHomeGoodsTypeImageData;
import com.txsh.services.MLShopServices;
import com.txsh.utils.MLUserCityUtils;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.MLEventBusModel;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLHomeCityData;
import com.zuomei.widget.sortlistview.CharacterParser;
import com.zuomei.widget.sortlistview.PinyinCityComparator;
import com.zuomei.widget.sortlistview.PinyinTypeComparator;
import com.zuomei.widget.sortlistview.SideBar;
import com.zuomei.widget.sortlistview.SideBar.OnTouchingLetterChangedListener;
import com.zuomei.widget.sortlistview.SortCityAdapter;
import com.zuomei.widget.sortlistview.SortTypeAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

public class TXShopTypeActivity extends BaseActivity {

  @ViewInject(R.id.shop_grid)
  private ListView lvType;
  @ViewInject(R.id.home_sidrbar)
  private SideBar _sidrbar;
  @ViewInject(R.id.home_tv_dialog)
  private TextView _dialogTv;

  private SortTypeAdapter txShopTypeAdapter;
  private String flag = "0";
  private List<TXHomeGoodsTypeImageData> typeDatas = new ArrayList<>();
  private CharacterParser characterParser;
  private PinyinTypeComparator pinyinTypeComparator;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_txshop_type);
    ViewUtils.inject(this);
    if (getIntentData() != null) {
      flag = (String) getIntentData();
    }
    initData();
  }


  private void initView() {
    characterParser = CharacterParser.getInstance();
    pinyinTypeComparator = new PinyinTypeComparator();
    filledData();
    txShopTypeAdapter = new SortTypeAdapter(this, typeDatas);
    lvType.setAdapter(txShopTypeAdapter);
    // 根据a-z进行排序源数据
    Collections.sort(typeDatas, pinyinTypeComparator);
    _sidrbar.setTextView(_dialogTv);

    //设置右侧触摸监听
    _sidrbar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
      @Override
      public void onTouchingLetterChanged(String s) {
        //该字母首次出现的位置
        int position = txShopTypeAdapter.getPositionForSection(s.charAt(0));
        if (position != -1) {
          lvType.setSelection(position);
        }
      }
    });
    lvType.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TXHomeGoodsTypeImageData data = (TXHomeGoodsTypeImageData) parent
            .getItemAtPosition(position);
        if (flag.equals("0")) {
          startAct(getAty(), TXShopGoodsByTypeActivity.class, data);
        } else {
          EventBus.getDefault().post(new MLEventBusModel(MLConstants.SELECT_TYPE, data));
          finish();
        }
      }
    });
  }

  /**
   * 为ListView填充数据
   */
  private List<TXHomeGoodsTypeImageData> filledData() {

    for (TXHomeGoodsTypeImageData data : typeDatas) {
      String pinyin = characterParser.getSelling(data.name);
      String sortString = pinyin.substring(0, 1).toUpperCase();
      if (data.name.equals("泗水")) {
        data.sortLetters = "S";
      } else {
        if (sortString.matches("[A-Z]")) {
          data.sortLetters = sortString.toUpperCase();
        } else {
          data.sortLetters = "#";
        }
      }
    }
    return typeDatas;
  }

  private void initData() {
    ZMRequestParams params = new ZMRequestParams();
    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
        ZMHttpType.RequestType.TX_SHOP_FIND_HOMEGOODS_ALL_TYPE, null, null, _handler,
        HTTP_RESPONSE_FIND_HOMEGOODS_TYPE, MLShopServices.getInstance());
    loadData(getAty(), message2);
  }

  public void back(View view) {
    finish();
  }

  private static final int HTTP_RESPONSE_FIND_HOMEGOODS_TYPE = 1;

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
        case HTTP_RESPONSE_FIND_HOMEGOODS_TYPE: {
          TXShopTypeListRes txShopPlayListRes = (TXShopTypeListRes) msg.obj;
          if (txShopPlayListRes.state.equalsIgnoreCase("1")) {
            typeDatas.addAll(txShopPlayListRes.datas);
            initView();
          }
          break;
        }
        default:
          break;
      }
    }
  };
}
