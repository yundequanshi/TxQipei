package com.txsh.friend;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baichang.android.request.HttpErrorListener;
import com.baichang.android.request.HttpSubscriber;
import com.baichang.android.request.HttpSuccessListener;
import com.baichang.android.utils.BCDialogUtil;
import com.baichang.android.utils.BCStringUtil;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.model.GroupData;
import com.easemob.easeui.model.HxUser;
import com.easemob.easeui.utils.MLDBUtils;
import com.hyphenate.chat.EMClient;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.adapter.HxGroupListAdapter;
import com.txsh.model.HxUserLoginData;
import com.zuomei.base.BaseAct;
import com.easemob.easeui.utils.HxApi;
import com.easemob.easeui.utils.HxHttpApi;
import com.zuomei.base.MLAppDiskCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 群会话列表
 */
public class GroupListAty extends BaseAct {

  private List<GroupData> mDataGroup = new ArrayList<>();

  @ViewInject(R.id.group_lv)
  private ListView mLvGroup;

  private HxGroupListAdapter mAdapter;
  private HxUserLoginData mDataUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.hx_group);
    ViewUtils.inject(this);
    mDataUser = MLAppDiskCache.getUser();
    View headview = View.inflate(this, com.easemob.easeui.R.layout.group_view_head, null);
    mLvGroup.addHeaderView(headview);
    headview.findViewById(com.easemob.easeui.R.id.header_lay_add)
        .setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            startAct(getAty(), GroupCreateAty.class, "", 1);
          }
        });

    mAdapter = new HxGroupListAdapter(getAty(), R.layout.group_list_item);
    mLvGroup.setAdapter(mAdapter);
    mLvGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i != 0) {
          GroupData data = (GroupData) adapterView.getAdapter().getItem(i);
          startActivity(
              new Intent(GroupListAty.this, ChatAty.class).putExtra(EaseConstant.EXTRA_GROUP, data)
                  .putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP));
        }
      }
    });

    mLvGroup.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i != 0) {
          final GroupData data = (GroupData) adapterView.getAdapter().getItem(i);
          if (BCStringUtil.compare(data.adminId, mDataUser.hxKid)) {
            BCDialogUtil.showDialog(getAty(), R.color.top_bar_normal_bg, "提示",
                String.format("确定将群《%s》解散？", data.name), new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                    delGroup(data.kid, data.emgId);
                  }
                }, null);
          }
        }
        return true;
      }
    });

    requestGroup();
  }

  /**
   * 获取群列表
   */
  private void requestGroup() {
    HxUserLoginData data = MLAppDiskCache.getUser();
    if (data == null) {
      return;
    }
    Map<String, String> map = new HashMap<String, String>();
    map.put("userKid", data.hxKid);
    HxApi requset = new HxHttpApi();
    requset.userGroup(map)
        .compose(HttpSubscriber.<List<GroupData>>applySchedulers())
        .subscribe(new HttpSubscriber<List<GroupData>>(new HttpSuccessListener<List<GroupData>>() {
          @Override
          public void success(List<GroupData> groupDatas) {
            mDataGroup.clear();
            mDataGroup.addAll(groupDatas);
            mAdapter.setData(mDataGroup);
            MLDBUtils.saveOrUpdate(mDataGroup);
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable throwable) {

          }
        }));
  }

  /**
   * 解散群
   */
  private void delGroup(final String groupId, final String userName) {
    mDataUser = MLAppDiskCache.getUser();
    if (mDataUser == null) {
      return;
    }
    Map<String, String> map = new HashMap<String, String>();
    map.put("userKid", mDataUser.hxKid);
    map.put("groupKid", groupId);
    HxApi requset = new HxHttpApi();
    requset.groupDissolution(map)
        .compose(HttpSubscriber.<String>applySchedulers())
        .subscribe(new HttpSubscriber<String>(new HttpSuccessListener<String>() {
          @Override
          public void success(String s) {
            showMessage(getAty(), "解散成功");
            requestGroup();
            EMClient.getInstance().chatManager().deleteConversation(userName, false);
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable throwable) {

          }
        }));
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 1 && resultCode == 1) {
      //建群成功
      requestGroup();
    }
  }

  @OnClick(R.id.titlebar_btn_right)
  private void addOnClick(View view) {
    startAct(getAty(), GroupAddAty.class, "", 1);
  }

  @OnClick(R.id.business_titlebar_tv_left)
  private void backOnClick(View view) {
    finish();
  }
}
