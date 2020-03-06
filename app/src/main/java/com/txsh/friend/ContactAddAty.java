package com.txsh.friend;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.baichang.android.request.HttpErrorListener;
import com.baichang.android.request.HttpSubscriber;
import com.baichang.android.request.HttpSuccessListener;
import com.baichang.android.utils.BCStringUtil;
import com.easemob.easeui.model.HxUser;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.adapter.HxContactAddAdapter;
import com.txsh.model.HxUserLoginData;
import com.zuomei.base.BaseAct;
import com.easemob.easeui.utils.HxApi;
import com.easemob.easeui.utils.HxHttpApi;
import com.zuomei.base.MLAppDiskCache;
import com.zuomei.base.MLEventBusModel;
import com.zuomei.constants.MLConstants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 添加好友
 */
public class ContactAddAty extends BaseAct {

  @ViewInject(R.id.add_lv)
  private ListView mLv;

  @ViewInject(R.id.add_serach_et)
  private EditText mEtCotent;

  private HxContactAddAdapter mAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.hx_contact_add);
    EventBus.getDefault().register(this);
    ViewUtils.inject(this);
    mAdapter = new HxContactAddAdapter(getAty(), R.layout.hx_contact_add_item);
    mLv.setAdapter(mAdapter);

  }

  @OnClick(R.id.titlebar_tv_right)
  public void searchOnClick(View view) {
    String content = mEtCotent.getText().toString();
    if (BCStringUtil.isEmpty(content)) {
      showMessage(getAty(), "请输入好友名称");
      return;
    }
    Map<String, String> map = new HashMap<String, String>();
    map.put("userName", content);
    map.put("pageSize", "40");
    HxApi requset = new HxHttpApi();
    requset.findUser(map)
        .compose(HttpSubscriber.<List<HxUser>>applySchedulers())
        .subscribe(new HttpSubscriber<List<HxUser>>(new HttpSuccessListener<List<HxUser>>() {
          @Override
          public void success(List<HxUser> hxUsers) {
            mAdapter.setData(hxUsers);
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable throwable) {

          }
        }));
  }

  @Subscribe
  public void onEvent(MLEventBusModel data) {
    if (data.type == MLConstants.EVENT_CONTACT_ADD) {
      HxUser hxuser = (HxUser) data.obj[0];
      HxUserLoginData user = MLAppDiskCache.getUser();
      if (user == null) {
        return;
      }
      if (user.hxKid.equals(hxuser.kid)) {
        showMessage(getAty(), "不能添加自己");
        return;
      }
      Map<String, String> map = new HashMap<String, String>();
      map.put("userKid", user.hxKid);
      map.put("toUserKid", hxuser.kid);
      HxApi requset = new HxHttpApi();
      requset.invitation(map)
          .compose(HttpSubscriber.<String>applySchedulers())
          .subscribe(new HttpSubscriber<String>(new HttpSuccessListener<String>() {
            @Override
            public void success(String s) {
              showMessage(getAty(), "好友申请已发出");
            }
          }));
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }
}
