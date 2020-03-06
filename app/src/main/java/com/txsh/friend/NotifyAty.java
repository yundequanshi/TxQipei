package com.txsh.friend;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.baichang.android.request.HttpErrorListener;
import com.baichang.android.request.HttpSubscriber;
import com.baichang.android.request.HttpSuccessListener;
import com.easemob.easeui.model.HxUser;
import com.easemob.easeui.model.TodoData;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.adapter.HxNotifyListAdapter;
import com.txsh.model.HxUserLoginData;
import com.txsh.utils.HxUtils;
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
 * 申请与通知
 */
public class NotifyAty extends BaseAct {

  @ViewInject(R.id.notify_lv)
  private ListView mLv;
  private HxNotifyListAdapter mAdapter;
  private TodoData mDataTodo;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.hx_notify);
    ViewUtils.inject(this);
    EventBus.getDefault().register(this);
    HxUtils.getInstance(getAty()).setNewNotify(false);
    mAdapter = new HxNotifyListAdapter(getAty(), R.layout.hx_notify_list_item);
    mLv.setAdapter(mAdapter);
    requestNotify();
  }

  /**
   * 获取全部待办
   */
  private void requestNotify() {
    HxUserLoginData data = MLAppDiskCache.getUser();
    if (data == null) {
      return;
    }
    Map<String, String> map = new HashMap<String, String>();
    map.put("userKid", data.hxKid);
    HxApi requset = new HxHttpApi();
    requset.toDoList(map)
        .compose(HttpSubscriber.<List<TodoData>>applySchedulers())
        .subscribe(new HttpSubscriber<List<TodoData>>(new HttpSuccessListener<List<TodoData>>() {
          @Override
          public void success(List<TodoData> todoDatas) {
            mAdapter.setData(todoDatas);
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable throwable) {

          }
        }));
  }

  @Subscribe
  public void onEvent(MLEventBusModel model) {
    //想添加你为好友
    if (model.type == MLConstants.EVENT_NOTIFY_FRIEND) {
      mDataTodo = (TodoData) model.obj[0];
      requestFriend();
    }
    //别人申请入群
    if (model.type == MLConstants.EVENT_APPLY_GROUP) {
      mDataTodo = (TodoData) model.obj[0];
      requestApplyGroupAgree();
    }
    //想邀请你入群
    if (model.type == MLConstants.EVENT_NOTIFY_GROUP_INVITE) {
      mDataTodo = (TodoData) model.obj[0];
      requestGroupAgree();
    }
  }

  /**
   * 群申请同意
   */
  private void requestApplyGroupAgree() {
    Map<String, String> map = new HashMap<String, String>();
    map.put("groupKid", mDataTodo.groupKid);
    map.put("toUserKid", mDataTodo.userKid);
    HxApi requset = new HxHttpApi();
    requset.addUser2Group(map)
        .compose(HttpSubscriber.<String>applySchedulers())
        .subscribe(new HttpSubscriber<String>(new HttpSuccessListener<String>() {
          @Override
          public void success(String s) {
            requestTodo();
            showMessage(getAty(), "群申请已同意");
            EventBus.getDefault()
                .postSticky(new MLEventBusModel(MLConstants.EVENT_FRIEND_AGREE, ""));
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable throwable) {

          }
        }));
  }

  /**
   * 群邀请同意
   */
  private void requestGroupAgree() {
    HxUserLoginData data = MLAppDiskCache.getUser();
    if (data == null) {
      return;
    }
    Map<String, String> map = new HashMap<String, String>();
    map.put("groupKid", mDataTodo.groupKid);
    map.put("toUserKid", data.hxKid);
    HxApi requset = new HxHttpApi();
    requset.addUser2Group(map)
        .compose(HttpSubscriber.<String>applySchedulers())
        .subscribe(new HttpSubscriber<String>(new HttpSuccessListener<String>() {
          @Override
          public void success(String s) {
            requestTodo();
            showMessage(getAty(), "群邀请已同意");
            EventBus.getDefault()
                .postSticky(new MLEventBusModel(MLConstants.EVENT_FRIEND_AGREE, ""));
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable throwable) {

          }
        }));
  }

  /**
   * 好友邀请同意
   */
  private void requestFriend() {
    HxUserLoginData data = MLAppDiskCache.getUser();
    if (data == null) {
      return;
    }
    Map<String, String> map = new HashMap<String, String>();
    map.put("userKid", data.hxKid);
    map.put("toUserKid", mDataTodo.userId);
    HxApi requset = new HxHttpApi();
    requset.addFriend(map)
        .compose(HttpSubscriber.<String>applySchedulers())
        .subscribe(new HttpSubscriber<String>(new HttpSuccessListener<String>() {
          @Override
          public void success(String s) {
            requestTodo();
            showMessage(getAty(), "好友邀请已同意");
            EventBus.getDefault()
                .postSticky(new MLEventBusModel(MLConstants.EVENT_FRIEND_AGREE, ""));
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable throwable) {

          }
        }));
  }

  /**
   * 待办事件清空
   */
  private void requestTodo() {
    HxUserLoginData data = MLAppDiskCache.getUser();
    if (data == null) {
      return;
    }
    Map<String, String> map = new HashMap<String, String>();
    map.put("userKid", data.hxKid);
    map.put("toDoId", mDataTodo.kid);
    HxApi requset = new HxHttpApi();
    requset.toDoMsg(map)
        .compose(HttpSubscriber.<String>applySchedulers())
        .subscribe(new HttpSubscriber<String>(new HttpSuccessListener<String>() {
          @Override
          public void success(String s) {
            requestNotify();
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable throwable) {

          }
        }));
  }


  @OnClick(R.id.titlebar_tv_left)
  public void backOnClick(View view) {
    finish();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }
}
