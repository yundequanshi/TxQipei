package com.txsh.friend;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.baichang.android.request.HttpErrorListener;
import com.baichang.android.request.HttpSubscriber;
import com.baichang.android.request.HttpSuccessListener;
import com.baichang.android.utils.BCDialogUtil;
import com.baichang.android.utils.BCStringUtil;
import com.baichang.android.widget.BCScrollGridView;
import com.easemob.easeui.model.GroupData;
import com.easemob.easeui.model.HxUser;
import com.easemob.easeui.utils.MLDBUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
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
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 群信息
 */
public class GroupInfoAty extends BaseAct {

  private String mDataGroupId;

  @ViewInject(R.id.info_grid)
  private BCScrollGridView mGridFriend;
  @ViewInject(R.id.titlebar_tv_right)
  private TextView mTvRight;
  @ViewInject(R.id.info_et_name)
  private EditText mEtName;
  @ViewInject(R.id.info_et_content)
  private EditText mEtContent;
  private HxGroupMemberAdapter mAdapter;
  private List<HxUser> mDatasUser = new ArrayList<>();
  private boolean isAdmin = false;
  private GroupData mDataGroup;
  private StringBuffer mDataSb;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.hx_group_info);
    ViewUtils.inject(this);
    EventBus.getDefault().register(this);
    mDataGroupId = getIntent().getStringExtra("id");
    initView();
    requestContact();
  }

  private void initView() {
    mDataGroup = MLDBUtils
        .getFirst(Selector.from(GroupData.class).where("emgId", "=", mDataGroupId));
    HxUserLoginData user = MLAppDiskCache.getUser();
    if (mDataGroup != null) {
      mEtName.setText(mDataGroup.name);
      mEtContent.setText(mDataGroup.desc);
      isAdmin = BCStringUtil.compare(mDataGroup.adminId, user.hxKid);
      setAdminFeature();
    }
    mAdapter = new HxGroupMemberAdapter(getAty(), R.layout.hx_group_member_item);
    mGridFriend.setAdapter(mAdapter);
    mGridFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        HxUser user = (HxUser) adapterView.getItemAtPosition(i);
        if (BCStringUtil.compare(user.id, "-1")) {
          startAct(getAty(), GroupContactAty.class);
        }
      }
    });
    mGridFriend.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (!isAdmin) {
          return true;
        }
        final HxUser user = (HxUser) adapterView.getItemAtPosition(i);
        BCDialogUtil.showDialog(getAty(), R.color.top_bar_normal_bg, "提示", "确定将该用户移除群？",
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                removeUser(user.kid);
              }
            }, null);

        return true;
      }
    });

    addDefault();
    mAdapter.setData(mDatasUser);
  }

  private void setAdminFeature() {
    if (isAdmin) {
      //管理员
      mTvRight.setVisibility(View.VISIBLE);
    } else {
      mTvRight.setVisibility(View.GONE);
    }

    mEtName.setEnabled(isAdmin);
    mEtContent.setEnabled(isAdmin);


  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(MLEventBusModel model) {
    if (model.type == MLConstants.EVENT_GROUP_CONTACT) {
      List<HxUser> mDatasChoose = (List<HxUser>) model.obj[0];
      List<HxUser> mDatasNew = new ArrayList<>();
      mDatasUser.remove(mDatasUser.size() - 1);
      if (!mDatasUser.isEmpty()) {
        for (HxUser u2 : mDatasChoose) {
          boolean isHas = false;
          for (HxUser u1 : mDatasUser) {
            if (BCStringUtil.compare(u1.id, u2.id)) {
              isHas = true;
            }
          }
          if (!isHas) {
            mDatasNew.add(u2);
          }
        }
      }
      mDatasUser.addAll(mDatasNew);
      addDefault();
      mAdapter.setData(mDatasUser);
      invitation(mDatasNew);
    }
  }

  /**
   * 添加好友
   */
  private void invitation(List<HxUser> mDatasNew) {
    if (mDatasNew == null || mDatasNew.size() == 0) {
      return;
    }
    mDataSb = new StringBuffer();
    for (HxUser user : mDatasNew) {
      if (!BCStringUtil.compare(user.id, "-1")) {
        mDataSb.append(user.kid);
        mDataSb.append(",");
      }
    }
    HxUserLoginData user = MLAppDiskCache.getUser();
    Map<String, String> map = new HashMap<String, String>();
    map.put("userKid", user.hxKid);
    map.put("groupKid", mDataGroup.kid);
    map.put("toUserKid", mDataSb.toString());
    HxApi requset = new HxHttpApi();
    requset.invitationGroup(map)
        .compose(HttpSubscriber.<String>applySchedulers())
        .subscribe(new HttpSubscriber<String>(new HttpSuccessListener<String>() {
          @Override
          public void success(String hxUsers) {
            showMessage(getAty(), "好友邀请已发出");
          }
        }));
  }

  /**
   * 获取群好友
   */
  private void requestContact() {
    Map<String, String> map = new HashMap<String, String>();
    map.put("emgId", mDataGroupId);
    HxApi requset = new HxHttpApi();
    requset.groupUsersByEmgId(map)
        .compose(HttpSubscriber.<List<HxUser>>applySchedulers())
        .subscribe(new HttpSubscriber<List<HxUser>>(new HttpSuccessListener<List<HxUser>>() {
          @Override
          public void success(List<HxUser> hxUsers) {
            mDatasUser.clear();
            mDatasUser.addAll(hxUsers);
            if (isAdmin) {
              addDefault();
            }
            mAdapter.setData(mDatasUser);
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable throwable) {

          }
        }));
  }

  /**
   * 移除群成员
   */
  private void removeUser(String userId) {
    HxUserLoginData data = MLAppDiskCache.getUser();
    if (data == null) {
      return;
    }
    Map<String, String> map = new HashMap<String, String>();
    map.put("userKid", data.hxKid);
    map.put("groupKid", mDataGroup.kid);
    map.put("toUserKid", userId);
    HxApi requset = new HxHttpApi();
    requset.removeUser(map)
        .compose(HttpSubscriber.<String>applySchedulers())
        .subscribe(new HttpSubscriber<String>(new HttpSuccessListener<String>() {
          @Override
          public void success(String s) {
            showMessage(getAty(), "移除成功");
            requestContact();
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable throwable) {

          }
        }));
  }


  /**
   * 保存群资料
   */
  private void saveInfo() {
    final String name = mEtName.getText().toString();
    String content = mEtContent.getText().toString();
    if (BCStringUtil.isEmpty(name)) {
      showMessage(getAty(), "群名称不能为空");
      return;
    }
    HxUserLoginData data = MLAppDiskCache.getUser();
    if (data == null) {
      return;
    }
    Map<String, String> map = new HashMap<String, String>();
    map.put("userKid", data.hxKid);
    map.put("groupKid", mDataGroup.kid);
    map.put("name", name);
    if (!BCStringUtil.isEmpty(content)) {
      map.put("desc", content);
    }
    HxApi requset = new HxHttpApi();
    requset.updateGroup(map)
        .compose(HttpSubscriber.<String>applySchedulers())
        .subscribe(new HttpSubscriber<String>(new HttpSuccessListener<String>() {
          @Override
          public void success(String s) {
            showMessage(getAty(), "修改成功");
            finish();
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable throwable) {

          }
        }));
  }

  private void addDefault() {
    if (!isAdmin) {
      return;
    }
    HxUser add = new HxUser();
    add.id = "-1";
    mDatasUser.add(add);
  }


  @OnClick(R.id.titlebar_tv_right)
  private void saveOnClick(View view) {
    saveInfo();
  }

  @OnClick(R.id.titlebar_tv_left)
  private void backOnClick(View view) {
    finish();
  }

  @OnClick(R.id.tv_record)
  private void msgRecord(View view) {
    startAct(getAty(), HxMsgRecordActivity.class, mDataGroupId);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }
}
