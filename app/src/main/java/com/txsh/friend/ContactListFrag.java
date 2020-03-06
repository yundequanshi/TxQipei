package com.txsh.friend;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.baichang.android.request.HttpErrorListener;
import com.baichang.android.request.HttpSubscriber;
import com.baichang.android.request.HttpSuccessListener;
import com.baichang.android.utils.BCDialogUtil;
import com.baichang.android.utils.BCToastUtil;
import com.easemob.easeui.model.HxUser;
import com.easemob.easeui.model.TodoData;
import com.easemob.easeui.ui.EaseContactListFragment;
import com.easemob.easeui.utils.Contants;
import com.txsh.R;
import com.txsh.model.HxAppUserData;
import com.txsh.model.HxUserLoginData;
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
 * 好友列表
 */
public class ContactListFrag extends EaseContactListFragment {

  private List<HxUser> mDataContacts = new ArrayList<HxUser>();
  private List<HxAppUserData> mAppUserData = new ArrayList<HxAppUserData>();
  private List<TodoData> mDataTodo = new ArrayList<TodoData>();
  private String moblies = "";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EventBus.getDefault().register(this);
  }

  @Override
  protected void initView() {
    super.initView();
    titleBar.setTitle("通讯录");
    titleBar.setBackgroundColor(getResources().getColor(R.color.head_back_nomall));
    titleBar.setLeftImageResource(R.drawable.cm_back_btn);
    titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        getActivity().finish();
      }
    });
    titleBar.setRightImageResource(com.easemob.easeui.R.drawable.em_add);
    titleBar.setRightLayoutClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(getActivity(), ContactAddAty.class));
      }
    });

    setContactListItemClickListener(new EaseContactListItemClickListener() {
      @Override
      public void onListItemClicked(HxUser user) {
        if (user != null) {
          user.isNoUserId = true;
          startActivity(new Intent(getActivity(),
              ChatAty.class).putExtra(Contants.EXTRA_USER, user));
        }
      }
    });

    setContactListItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
        final HxUser hxUser = (HxUser) adapterView.getAdapter().getItem(i);
        BCDialogUtil.showDialog(getActivity(), R.color.top_bar_normal_bg, "提示", "确定删除该好友？",
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i1) {
                requestDelFriend(hxUser);
              }
            }, null);

        return true;
      }
    });
    setContactListItemAddClickListener(new EaseContactsListItemAddClickListener() {
      @Override
      public void onListItemAddClicked(HxUser user) {
        if (user != null) {
          // 添加好友
          requestAddFriend(user);
        }

      }
    });
  }

  /**
   * 获取全部好友
   */
  private void requestFriend() {
    HxUserLoginData data = MLAppDiskCache.getUser();
    if (data == null) {
      return;
    }
    try {
      moblies = MLMobileContactsUtils.getContacts(getActivity());
    } catch (Exception e) {
    }
    Map<String, String> map = new HashMap<String, String>();
    map.put("mobilelist", moblies);
    map.put("userKid", data.hxKid);
    HxApi requset = new HxHttpApi();
    requset.friends(map)
        .compose(HttpSubscriber.<List<HxUser>>applySchedulers())
        .subscribe(new HttpSubscriber<List<HxUser>>(new HttpSuccessListener<List<HxUser>>() {
          @Override
          public void success(List<HxUser> hxUsers) {
            mDataContacts.clear();
            mDataContacts.addAll(hxUsers);
            setHxUser(hxUsers);
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable throwable) {
            setHxUser(mDataContacts);
          }
        }));
  }

  /**
   * 获取用户待办数量
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
            mDataTodo.clear();
            mDataTodo.addAll(todoDatas);
            if (mDataTodo.size() > 0) {
              mLayRead.setVisibility(View.VISIBLE);
              mTvUnread.setText(mDataTodo.size() + "");
            } else {
              mLayRead.setVisibility(View.GONE);
            }
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable throwable) {

          }
        }));
  }

  /**
   * 删除好友
   */
  private void requestDelFriend(HxUser user) {
    HxUserLoginData data = MLAppDiskCache.getUser();
    if (data == null) {
      return;
    }
    Map<String, String> map = new HashMap<String, String>();
    map.put("userKid", data.hxKid);
    map.put("toUserKid", user.kid);
    HxApi requset = new HxHttpApi();
    requset.dissolution(map)
        .compose(HttpSubscriber.<String>applySchedulers())
        .subscribe(new HttpSubscriber<String>(new HttpSuccessListener<String>() {
          @Override
          public void success(String s) {
            BCToastUtil.showMessage(getActivity(), "删除成功");
            requestFriend();
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable throwable) {

          }
        }));
  }

  /**
   * 添加好友
   */
  private void requestAddFriend(HxUser user) {
    HxUserLoginData hxUserLoginData = MLAppDiskCache.getUser();
    if (hxUserLoginData == null) {
      return;
    }
    if (hxUserLoginData.hxKid.equals(user.kid)) {
      Toast.makeText(getActivity(), "不能添加自己", Toast.LENGTH_SHORT).show();
      return;
    }
    Map<String, String> map = new HashMap<String, String>();
    map.put("userKid", hxUserLoginData.hxKid);
    map.put("toUserKid", user.kid);
    HxApi requset = new HxHttpApi();
    requset.invitation(map)
        .compose(HttpSubscriber.<String>applySchedulers())
        .subscribe(new HttpSubscriber<String>(new HttpSuccessListener<String>() {
          @Override
          public void success(String s) {
            Toast.makeText(getActivity(), "好友申请已发出", Toast.LENGTH_SHORT).show();
          }
        }));
  }

  @Override
  public void setHeadOnClick(int i) {
    if (i == 1) {
      //群聊
      startActivity(new Intent(getActivity(), GroupListAty.class));
    } else if (i == 2) {
      //申请与通知
      startActivity(new Intent(getActivity(), NotifyAty.class));
    }
  }

  @Subscribe
  public void onEvent(MLEventBusModel data) {
    if (data.type == MLConstants.EVENT_FRIEND_AGREE) {
      requestNotify();
      requestFriend();
    }

  }

  @Override
  public void onResume() {
    super.onResume();
    requestFriend();
    requestNotify();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }
}

