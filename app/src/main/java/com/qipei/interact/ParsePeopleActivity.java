package com.qipei.interact;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.baichang.android.utils.BCDialogUtil;
import com.baichang.android.utils.BCStringUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qipei.adapter.MLParsePeopleAdapter;
import com.qipei.model.ParseInfoData;
import com.txsh.R;
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.MLEventBusModel;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLDepotData;
import com.zuomei.model.MLHomeBusinessData;
import com.zuomei.model.MLInteractionData;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLMessageData;
import com.zuomei.model.MLMessageListResponse;
import com.zuomei.model.MLParseResponse;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLMessageServices;
import java.io.Serializable;
import org.greenrobot.eventbus.EventBus;

public class ParsePeopleActivity extends BaseActivity {

  @ViewInject(R.id.lv_parse_people)
  ListView lvParsePeople;
  @ViewInject(R.id.tv_title)
  TextView tvTitle;

  private MLLogin user = new MLLogin();
  private MLParsePeopleAdapter mAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_prase_people);
    ViewUtils.inject(this);
    user = ((BaseApplication) getApplication()).get_user();
    mAdapter = new MLParsePeopleAdapter(getAty(), R.layout.item_parse_people);
    lvParsePeople.setAdapter(mAdapter);
    lvParsePeople.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ParseInfoData data = (ParseInfoData) parent.getItemAtPosition(position);
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
            finish();
          } else {
            startAct(getAty(), InteractPeopleActivity.class, messageData);
          }
        } else {
          MLHomeBusinessData _business = new MLHomeBusinessData();
          _business.isCollect = false;
          _business.id = data.praiseId;
          Intent intent = new Intent();
          intent.setClass(getAty(), MLAuxiliaryActivity.class);
          intent.putExtra("data", MLConstants.HOME_BUSINESS_INFO);
          intent.putExtra("obj", (Serializable) _business);
          startActivity(intent);
        }
      }
    });
    String interactionId = (String) getIntentData();
    findPraiseInfoByInteractionId(interactionId);
  }

  //点赞人
  private void findPraiseInfoByInteractionId(String interactionId) {
    ZMRequestParams param = new ZMRequestParams();
    param.addParameter("interactionId", interactionId);
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(
        ZMHttpType.RequestType.FIND_PRAISEINFO_BY_INTERACTIONID,
        null, param, _handler, HTTP_RESPONSE_PARES_INFO, MLMessageServices.getInstance());
    loadDataWithMessage(getAty(), null, message1);
  }


  private static final int HTTP_RESPONSE_PARES_INFO = 1;

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
        case HTTP_RESPONSE_PARES_INFO: {
          final MLParseResponse ret = (MLParseResponse) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            mAdapter.setData(ret.datas);
            tvTitle.setText(ret.datas.size() + "人觉得很赞");
          } else {
            showMessageError("获取点赞人失败");
          }
          break;
        }
        default:
          break;
      }
    }
  };

  @OnClick(R.id.top_btn_left)
  public void back(View view) {
    finish();
  }
}
