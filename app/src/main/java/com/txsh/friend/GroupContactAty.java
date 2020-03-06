package com.txsh.friend;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.txsh.adapter.HxGroupContactAdapter;
import com.txsh.model.HxUserLoginData;
import com.zuomei.base.BaseAct;
import com.easemob.easeui.utils.HxApi;
import com.easemob.easeui.utils.HxHttpApi;
import com.zuomei.base.MLAppDiskCache;
import com.zuomei.base.MLEventBusModel;
import com.zuomei.constants.MLConstants;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 邀请群成员
 */
public class GroupContactAty extends BaseAct {

  @ViewInject(R.id.group_lv)
  private ListView mLvContact;

  private HxGroupContactAdapter mAdapter;

  private List<HxUser> mDataContacts = new ArrayList<>();
  private List<HxUser> mDataSelect = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.hx_group_contact);
    ViewUtils.inject(this);
    mDataSelect = (List<HxUser>) getIntentData();
    if (mDataSelect == null) {
      mDataSelect = new ArrayList<>();
    }
    initView();

    requestFriend();
  }

  private void initView() {
    mAdapter = new HxGroupContactAdapter(getAty(), R.layout.group_contact_item);
    mLvContact.setAdapter(mAdapter);
    mLvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mDataContacts.get(i).isCheck = !mDataContacts.get(i).isCheck;
        mAdapter.notifyDataSetChanged();
      }
    });
  }

  /**
   * 获取自己的全部好友
   */
  private void requestFriend() {
    HxUserLoginData data = MLAppDiskCache.getUser();
    if (data == null) {
      return;
    }
    Map<String, String> map = new HashMap<String, String>();
    map.put("userKid", data.hxKid);
    HxApi requset = new HxHttpApi();
    requset.friends(map)
        .compose(HttpSubscriber.<List<HxUser>>applySchedulers())
        .subscribe(new HttpSubscriber<List<HxUser>>(new HttpSuccessListener<List<HxUser>>() {
          @Override
          public void success(List<HxUser> hxUsers) {
            mDataContacts.clear();
            mDataContacts.addAll(hxUsers);
            for (HxUser data1 : mDataContacts) {
              for (HxUser data2 : mDataSelect) {
                if (BCStringUtil.compare(data1.id, data2.id)) {
                  data1.isCheck = true;
                }
              }
            }

            mAdapter.setData(mDataContacts);
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable throwable) {

          }
        }));
  }

  @OnClick(R.id.titlebar_tv_right)
  private void submitOnClick(View view) {
    EventBus.getDefault().post(new MLEventBusModel(MLConstants.EVENT_GROUP_CONTACT, getCheck()));
    finish();
  }

  @OnClick(R.id.business_titlebar_tv_left)
  private void backOnClick(View view) {
    finish();
  }


  private List<HxUser> getCheck() {
    List<HxUser> datas = new ArrayList<>();
    for (HxUser user : mDataContacts) {
      if (user.isCheck) {
        datas.add(user);
      }
    }
    return datas;
  }
}
