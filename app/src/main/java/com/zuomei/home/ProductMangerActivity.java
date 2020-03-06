package com.zuomei.home;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.baichang.android.utils.BCDialogUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.txsh.quote.Flag;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.MLEventBusModel;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLHomeBusinessResponse;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLProductMangerResponse;
import com.zuomei.model.MLRegister;
import com.zuomei.model.ProMangerData;
import com.zuomei.services.MLHomeServices;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ProductMangerActivity extends BaseActivity {

  @ViewInject(R.id.gv_product)
  GridView gvProduct;
  @ViewInject(R.id.rl_root)
  private LinearLayout _root;

  private TxProductMangerAdapter mAdapter;
  private MLLogin login = new MLLogin();
  private List<ProMangerData> proMangerDatas = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_product_manger);
    ViewUtils.inject(this);
    EventBus.getDefault().register(this);
    login = BaseApplication.getInstance().get_user();
    mAdapter = new TxProductMangerAdapter(getAty(), R.layout.item_product_magnger);
    gvProduct.setAdapter(mAdapter);
    gvProduct.setOnItemLongClickListener(new OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final ProMangerData proMangerData = (ProMangerData) parent.getItemAtPosition(position);
        BCDialogUtil.showDialog(getAty(), "提示", "确认删除吗？", new OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            deleteItem(proMangerData.image);
          }
        }, null);
        return true;
      }
    });
    gvProduct.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        List<String> images = new ArrayList<String>();
        for (ProMangerData d : proMangerDatas) {
          images.add(APIConstants.API_IMAGE_SHOW + d.image);
        }
        showDialog(images, position);
      }
    });
    initData();
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void event(MLEventBusModel model) {
    //刷新
    if (Flag.EVENT_PUBLISH_PRODUCT_MANGER == model.type) {
      initData();
    }
  }

  private void showDialog(List<String> images, int nowPosition) {
    MLHomeProductPop _pop = new MLHomeProductPop(getAty(), images, nowPosition);
    _pop.showAtLocation(_root, Gravity.CENTER, 0, 0);
  }

  private void initData() {
    ZMRequestParams catalogParam = new ZMRequestParams();
    catalogParam.addParameter("companyId", login.Id);
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.FIND_COMPANY_PRODUCTS,
        null, catalogParam, _handler, HTTP_RESPONSE_PRODUCT_MANGER, MLHomeServices.getInstance());
    loadDataWithMessage(getAty(), null, message1);
  }

  private void deleteItem(String picture) {
    ZMRequestParams catalogParam = new ZMRequestParams();
    catalogParam.addParameter("companyId", login.Id);
    catalogParam.addParameter("picture", picture);
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.DELETE_COMPANY_PRODUCTS,
        null, catalogParam, _handler, HTTP_RESPONSE_PRODUCT_MANGER_DELETE,
        MLHomeServices.getInstance());
    loadDataWithMessage(getAty(), null, message1);
  }


  public void back(View view) {
    finish();
  }

  public void addProduct(View view) {
    startAct(getAty(), ProductPublishActivity.class);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }

  private static final int HTTP_RESPONSE_PRODUCT_MANGER = 1;
  private static final int HTTP_RESPONSE_PRODUCT_MANGER_DELETE = 2;
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
        case HTTP_RESPONSE_PRODUCT_MANGER: {
          MLProductMangerResponse ret = (MLProductMangerResponse) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            proMangerDatas = ret.datas;
            mAdapter.setData(proMangerDatas);
          } else {
            showMessage("获取失败!");
          }
          break;
        }
        case HTTP_RESPONSE_PRODUCT_MANGER_DELETE: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            showMessage("删除成功");
            initData();
          } else {
            showMessage("获取失败!");
          }
          break;
        }
        default:
          break;
      }
    }
  };
}
