package com.txsh.shop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.ab.view.pullview.AbPullToRefreshView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.adapter.TXShopCarAdapter;
import com.txsh.model.TXShopBuyRes;
import com.txsh.model.TXShopCarData;
import com.txsh.services.MLShopServices;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLRegister;

import java.util.ArrayList;
import java.util.List;

import cn.ml.base.MLBaseConstants;
import cn.ml.base.utils.MLDialogUtils;
import cn.ml.base.utils.MLStrUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 购物车 Created by Administrator on 2015/7/21.
 */
public class TXShopCarAty extends BaseActivity {

  @ViewInject(R.id.shopcar_lv_shop)
  private ListView mListShop;

  @ViewInject(R.id.shopcar_rb_all)
  private CheckBox mCheckAll;


  @ViewInject(R.id.shopcar_tv_TotalNum)
  private TextView TotalNum;

  @ViewInject(R.id.shopcar_pullview)
  private AbPullToRefreshView mPullRefreshView;

  List<TXShopCarData.TXShopCarDetailData> mlShopCarData;
  private List<TXShopCarData.TXShopCarDetailData> mOrdersChoose;
  private boolean isAll = false;


  private int nowPage = 1;
  private String pageSize = "";
  double totalPrice = 0.00;
  private boolean mIsRefresh = true;
  //    private String lastrow = "0";
  TXShopCarAdapter mAdapterShop;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.shop_shopcar_aty);
    ViewUtils.inject(this);
    //    initList();
    EventBus.getDefault().register(this);
    initList();
    initShopCar();
    initPullRefresh();
  }

  private void initList() {
    mOrdersChoose = new ArrayList<TXShopCarData.TXShopCarDetailData>();
    mAdapterShop = new TXShopCarAdapter(this, R.layout.shopcar_item);
    mListShop.setAdapter(mAdapterShop);
    mListShop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        adapterView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        //点了操作  可以多选
        mlShopCarData.get(position).isChoose = !mlShopCarData.get(position).isChoose;

        if (mlShopCarData.get(position).isChoose) {
          mOrdersChoose.add(mlShopCarData.get(position));
          if (mOrdersChoose.size() == mlShopCarData.size()) {
            isAll = true;
            mCheckAll.setChecked(isAll);
          }
        } else {
          if (mOrdersChoose.size() == mlShopCarData.size()) {
            isAll = false;
            mCheckAll.setChecked(isAll);
          }
          mOrdersChoose.remove(mlShopCarData.get(position));
        }
        mAdapterShop.notifyDataSetChanged();
        countPrice();
      }
    });

  }

  public void countPrice() {
    totalPrice = 0.00;
    for (TXShopCarData.TXShopCarDetailData model : mOrdersChoose) {
      // MLDBUtils.delete(model);
      Double totPrice = Double.valueOf(model.number) * Double.valueOf(model.privce);
      totalPrice = totalPrice + totPrice;
    }
    TotalNum.setText(String.valueOf(totalPrice));
  }


  private void initPullRefresh() {
    mPullRefreshView.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
      @Override
      public void onHeaderRefresh(AbPullToRefreshView view) {
        mIsRefresh = true;
        nowPage = 1;
        initShopCar();
      }
    });

    mPullRefreshView.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
      @Override
      public void onFooterLoad(AbPullToRefreshView view) {
        // TODO Auto-generated method stub
        mIsRefresh = false;
        int size = mlShopCarData.size() - 1;
        if (size <= 0) {
          return;
        }
        //lastrow = mlShopCarData.get(mlShopCarData.size() - 1).rowno;
        nowPage++;
        initShopCar();
      }

    });
  }

  @Subscribe
  public void addPlus(TXShopCarData.TXShopCarDetailData data) {
    if (MLStrUtil.compare(data.addplus, "add")) {
      addCar(data.id, Integer.valueOf(data.number));
      countPrice();
    }
    if (MLStrUtil.compare(data.addplus, "plus")) {

      addCar(data.id, Integer.valueOf(data.number));
      countPrice();
    }
  }


  private void addCar(String id, int count) {
    if (count < 1) {
      showMessage("商品数量小于1");
      return;
    }

    ZMRequestParams params = new ZMRequestParams();
    //    params.addParameter("depotId", BaseApplication.getInstance().get_user().Id);
    params.addParameter("id", id);
    params.addParameter("number", String.valueOf(count));

    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.EDITSHOPCAR,
        null, params, _handler,
        EDITSHOPCARRETURN, MLShopServices.getInstance());
    loadDataWithMessage(TXShopCarAty.this, null, message2);
  }


  @Override
  protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }

  @OnClick(R.id.home_top_back)
  public void backOnClick(View view) {
    finish();
  }

  //全选
  @OnClick(R.id.shopcar_ln_top)
  public void choiseOnClick(View view) {
    if (isAll == true) {
      isAll = false;
    } else {
      isAll = true;
    }
    //  isAll = !isAll;
    mCheckAll.setChecked(isAll);
    setAll(isAll);
    countPrice();
  }

  @OnClick(R.id.shopcar_btn_set)
  public void balanceOnClick(View view) {
    if (mOrdersChoose.size() == 0) {
      showMessage("请先选择商品");
      return;
    }

    List pid = new ArrayList();
    for (TXShopCarData.TXShopCarDetailData model : mOrdersChoose) {
      pid.add(model.id);
    }
    initShopSub(listToString(pid, ','));

  }

  @OnClick(R.id.shopcar_btn_dele)
  public void delOnClick(View view) {
    if (mOrdersChoose.size() == 0) {
      showMessage("请先选择商品");
      return;
    }

    AlertDialog.Builder builder = MLDialogUtils.getAlertDialog(this);
    builder.setTitle("提示");
    builder.setMessage("确定从购物车中删除此商品吗?");
    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        del();
      }
    });
    builder.setPositiveButton("取消", null);
    builder.show();
    countPrice();

  }

  private void del() {
    List pid = new ArrayList();
    //   List pflag = new ArrayList();
    for (TXShopCarData.TXShopCarDetailData model : mOrdersChoose) {
      // MLDBUtils.delete(model);
      pid.add(model.id);
      //   pflag.add(model.pflag);

    }
    initDelShopCar(listToString(pid, ','));

  }

  private void initShopSub(String id) {
    ZMRequestParams params = new ZMRequestParams();
    //    params.addParameter("depotId", BaseApplication.getInstance().get_user().Id);
    params.addParameter("depotId", BaseApplication.getInstance().get_user().Id);
    params.addParameter("carIds", id);

    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.SHOPCARBUY,
        null, params, _handler,
        SHOPCARBUYRETURN, MLShopServices.getInstance());
    loadDataWithMessage(TXShopCarAty.this, null, message2);
  }


  private void initDelShopCar(String id) {
    ZMRequestParams params = new ZMRequestParams();
    //    params.addParameter("depotId", BaseApplication.getInstance().get_user().Id);
    params.addParameter("id", id);
    //  params.addParameter("number", String.valueOf(count));

    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.DELSHOPCAR,
        null, params, _handler,
        DELSHOPCARRETURN, MLShopServices.getInstance());
    loadDataWithMessage(TXShopCarAty.this, null, message2);
  }


  private void initShopCar() {
    //获取互动列表
    MLLogin user = ((BaseApplication) this.getApplication()).get_user();
    ZMRequestParams param = new ZMRequestParams();
    param.addParameter("depotId", user.Id);
    param.addParameter("nowPage", String.valueOf(nowPage));
    param.addParameter("pageSize", "20");
    //param.addParameter("pageNum","2");
    //param.addParameter(MLConstants.PARAM_MESSAGE_LASTID, lastId);
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(ZMHttpType.RequestType.SHOPCAR, null,
        param,
        _handler, SHOPCARRETURN, MLShopServices.getInstance());
    loadDataWithMessage(TXShopCarAty.this, null, message1);
  }

  private static final int SHOPCARRETURN = 1;
  private static final int EDITSHOPCARRETURN = 2;
  private static final int DELSHOPCARRETURN = 3;
  private static final int SHOPCARBUYRETURN = 4;


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
        case SHOPCARRETURN: {
          TXShopCarData ret = (TXShopCarData) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            //    mlShopCarData=ret.datas;
            //    mAdapterShop.setData(mlShopCarData);

            isAll = false;
            mCheckAll.setChecked(isAll);
            if (mOrdersChoose.size() != 0) {
              mOrdersChoose.clear();
            }
            countPrice();
            if (mIsRefresh) {
              //刷新
              mlShopCarData = ret.datas;
              mPullRefreshView.onHeaderRefreshFinish();
            } else {
              //加载更多
              mPullRefreshView.onFooterLoadFinish();
              if (mlShopCarData == null) {
                return;
              }
              mlShopCarData.addAll(ret.datas);
            }
            //加载数据
            if (mlShopCarData != null) {
              mAdapterShop.setData(mlShopCarData);
            }
            if (mlShopCarData != null && mlShopCarData.size() < 20) {
              mPullRefreshView.setLoadMoreEnable(false);
            } else {
              mPullRefreshView.setLoadMoreEnable(true);
            }

          } else {
            showMessage("加载数据失败");
            break;
          }
          break;


        }
        case EDITSHOPCARRETURN: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            //   initShopCar();
            // showMessage("加入购物车成功");
          } else {
            showMessage("添加数量失败");
          }
          break;
        }

        case DELSHOPCARRETURN: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            //    showMessage("删除物品成功");
            initShopCar();
          } else {
            showMessage("删除物品失败");
          }
          break;
        }
        case SHOPCARBUYRETURN: {
          TXShopBuyRes ret = (TXShopBuyRes) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            //    showMessage("删除物品成功");

            List pid = new ArrayList();
            for (TXShopCarData.TXShopCarDetailData model : mOrdersChoose) {
              pid.add(model.id);
            }
            Intent intent = new Intent(TXShopCarAty.this, TXShopOrderSumAty.class);
            intent.putExtra(MLBaseConstants.TAG_INTENT_DATA, ret.datas);
            intent.putExtra("CARID", listToString(pid, ','));
            startActivity(intent);
            //startAct(TXShopCarAty.this, TXShopOrderSumAty.class, ret.datas);
          } else {
            showMessage("购买失败");
          }
          break;
        }


      }
    }

  };

  /**
   * 全选/不全选
   */
  public void setAll(boolean b) {
    if (mlShopCarData != null) {
      for (TXShopCarData.TXShopCarDetailData order : mlShopCarData) {
        order.isChoose = b;
      }
      //全选
      if (b) {
        mOrdersChoose.clear();
        for (TXShopCarData.TXShopCarDetailData order : mlShopCarData) {
          mOrdersChoose.add(order);
        }
      } else {
        mOrdersChoose = new ArrayList<TXShopCarData.TXShopCarDetailData>();
      }

      mAdapterShop.notifyDataSetChanged();
    }
  }

  public String listToString(List list, char separator) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < list.size(); i++) {
      sb.append(list.get(i));
      if (i < list.size() - 1) {
        sb.append(separator);
      }
    }
    return sb.toString();
  }

}
