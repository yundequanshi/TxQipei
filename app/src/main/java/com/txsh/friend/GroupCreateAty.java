package com.txsh.friend;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.baichang.android.request.HttpErrorListener;
import com.baichang.android.request.HttpSubscriber;
import com.baichang.android.request.HttpSuccessListener;
import com.baichang.android.utils.BCStringUtil;
import com.baichang.android.widget.BCHorizontalTextView;
import com.baichang.android.widget.BCScrollGridView;
import com.easemob.easeui.model.HxUser;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.adapter.HxGroupMemberAdapter;
import com.txsh.model.HxUserLoginData;
import com.zuomei.base.BaseAct;
import com.easemob.easeui.utils.HxApi;
import com.easemob.easeui.utils.HxHttpApi;
import com.zuomei.base.MLAppDiskCache;
import com.zuomei.base.MLEventBusModel;
import com.zuomei.constants.MLConstants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 建群
 */
public class GroupCreateAty extends BaseAct {

  private String mDataGroupId;

  @ViewInject(R.id.info_grid)
  private BCScrollGridView mGridFriend;
  @ViewInject(R.id.titlebar_tv)
  private TextView mTvTitle;
  @ViewInject(R.id.info_et_name)
  private EditText mEtName;
  @ViewInject(R.id.info_et_content)
  private EditText mEtContent;
  @ViewInject(R.id.tv_record)
  private BCHorizontalTextView tvRecord;
  private HxGroupMemberAdapter mAdapter;
  private List<HxUser> mDatasUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.hx_group_info);
    ViewUtils.inject(this);
    EventBus.getDefault().register(this);
    mTvTitle.setText("新建群");
    tvRecord.setVisibility(View.GONE);
    initView();
  }

  private void initView() {

    mDatasUser = new ArrayList<>();
    mAdapter = new HxGroupMemberAdapter(getAty(), R.layout.hx_group_member_item);
    mGridFriend.setAdapter(mAdapter);

    mGridFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        HxUser user = (HxUser) adapterView.getItemAtPosition(i);
        if (BCStringUtil.compare(user.id, "-1")) {
          if (mDatasUser.size() > 1) {
            startAct(getAty(), GroupContactAty.class, mDatasUser);
          } else {
            startAct(getAty(), GroupContactAty.class);
          }
        }
      }
    });

    HxUser add = new HxUser();
    add.id = "-1";
    mDatasUser.add(add);
    mAdapter.setData(mDatasUser);
  }

  @Subscribe
  public void onEvent(MLEventBusModel model) {
    if (model.type == MLConstants.EVENT_GROUP_CONTACT) {
      mDatasUser = (List<HxUser>) model.obj[0];
      HxUser add = new HxUser();
      add.id = "-1";
      mDatasUser.add(add);
      mAdapter.setData(mDatasUser);
    }
  }

  @OnClick(R.id.titlebar_tv_right)
  private void saveOnClick(View view) {
    HxUserLoginData user = MLAppDiskCache.getUser();
    String name = mEtName.getText().toString();
    String content = mEtContent.getText().toString();
    getUsers();
    if (BCStringUtil.isEmpty(name)) {
      showMessage(getAty(), "群名称不能为空");
      return;
    }

    Map<String, String> map = new HashMap<String, String>();
    map.put("userKid", user.hxKid);
    map.put("name", name);
    if (!BCStringUtil.isEmpty(content)) {
      map.put("desc", content);
    }
    HxApi requset = new HxHttpApi();
    requset.createGroup(map)
        .compose(HttpSubscriber.<String>applySchedulers(this))
        .subscribe(new HttpSubscriber<String>(new HttpSuccessListener<String>() {
              @Override
              public void success(String groupId) {
                if (!BCStringUtil.isEmpty(mDataSb.toString())) {
                  invitation(groupId);
                } else {
                  showMessage(getAty(), "创建成功");
                  setResult(1);
                  GroupCreateAty.this.finish();
                }
              }
            }));
  }


  private StringBuffer mDataSb;

  /**
   * 邀请的用户
   */
  private void invitation(String groupId) {
    HxUserLoginData user = MLAppDiskCache.getUser();
    Map<String, String> map = new HashMap<String, String>();
    map.put("userKid", user.hxKid);
    map.put("groupKid", groupId);
    map.put("toUserKid", mDataSb.toString());
    HxApi requset = new HxHttpApi();
    requset.invitationGroup(map)
        .compose(HttpSubscriber.<String>applySchedulers(this))
        .subscribe(new HttpSubscriber<String>(new HttpSuccessListener<String>() {
              @Override
              public void success(String groupId) {
                showMessage(getAty(), "创建成功");
                setResult(1);
                finish();
              }
            }, new HttpErrorListener() {
              @Override
              public void error(Throwable throwable) {

              }
            }));

  }

  /**
   * 获取邀请的用户
   */
  private void getUsers() {
    if (mDatasUser == null || mDatasUser.size() == 0) {
      return;
    }
    mDataSb = new StringBuffer();
    for (HxUser user : mDatasUser) {
      if (!BCStringUtil.compare(user.id, "-1")) {
        mDataSb.append(user.kid);
        mDataSb.append(",");
      }
    }
  }

  @OnClick(R.id.titlebar_tv_left)
  private void backOnClick(View view) {
    finish();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }
}
