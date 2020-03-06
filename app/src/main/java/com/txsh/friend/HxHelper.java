package com.txsh.friend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.baichang.android.utils.BCLogUtil;
import com.baichang.android.utils.BCStringUtil;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.model.EaseNotifier;
import com.easemob.easeui.model.InviteMessage;
import com.easemob.easeui.utils.Contants;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.txsh.R;
import com.txsh.comment.TXHomeActivity;
import com.txsh.utils.HxUtils;

import java.util.List;
import java.util.UUID;

/**
 * Created by Marcello on 2015/12/19. Description
 */
public class HxHelper {

  private static HxHelper instance = null;
  private Context mContext;

  /**
   * EMEventListener
   */
  protected EMMessageListener eventListener = null;
  private EaseUI easeUI;
  private LocalBroadcastManager broadcastManager;
  private CallReceiver callReceiver;
  public boolean isVoiceCalling;
  public boolean isVideoCalling;

  protected static final String TAG = "HXUtils";

  public synchronized static HxHelper getInstance() {
    if (instance == null) {
      instance = new HxHelper();
    }
    return instance;
  }


  public void init(Context context) {
    if (EaseUI.getInstance().init(context, null)) {
      mContext = context;
      //设为调试模式，打成正式包时，最好设为false，以免消耗额外的资源
      EMClient.getInstance().setDebugMode(true);
      //get easeui instance
      easeUI = EaseUI.getInstance();
      //调用easeui的api设置providers
      setEaseUIProviders();

      //设置全局监听
      setGlobalListeners();
      broadcastManager = LocalBroadcastManager.getInstance(mContext);
    }
  }

  /**
   * 设置全局事件监听
   */
  protected void setGlobalListeners() {
    IntentFilter callFilter = new IntentFilter(
        EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
    if (callReceiver == null) {
      callReceiver = new CallReceiver();
    }

    //注册通话广播接收者
    mContext.registerReceiver(callReceiver, callFilter);

    //注册消息事件监听
    registerEventListener();
    //群组变化
    EMClient.getInstance().groupManager().addGroupChangeListener(new MyGroupChangeListener());
////        // 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
//        EMClient.getInstance().setAppInited();
  }


  /**
   * 全局事件监听 因为可能会有UI页面先处理到这个消息，所以一般如果UI页面已经处理，这里就不需要再次处理 activityList.size() <= 0
   * 意味着所有页面都已经在后台运行，或者已经离开Activity Stack
   */
  protected void registerEventListener() {
    eventListener = new EMMessageListener() {
      private BroadcastReceiver broadCastReceiver = null;

      @Override
      public void onMessageReceived(List<EMMessage> messages) {
        //应用在后台，不需要刷新UI,通知栏提示新消息
        for (EMMessage message : messages) {
          if (!easeUI.hasForegroundActivies()) {
            if (message.getChatType() == EMMessage.ChatType.Chat) {
              //单聊获取用户的信息
              HxUtils.getInstance(mContext).getUserInfo(message.getFrom());
            }
          }
        }
        if (!easeUI.hasForegroundActivies()) {
          getNotifier().onNewMesg(messages);
        }
      }

      @Override
      public void onCmdMessageReceived(List<EMMessage> messages) {

        for (EMMessage message : messages) {
          //获取消息body
          EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
          final String action = cmdMsgBody.action();//获取自定义action

          //获取扩展属性 此处省略
          //message.getStringAttribute("");
          final String str = mContext.getString(R.string.receive_the_passthrough);

          final String CMD_TOAST_BROADCAST = "easemob.demo.cmd.toast";
          IntentFilter cmdFilter = new IntentFilter(CMD_TOAST_BROADCAST);

          if (broadCastReceiver == null) {
            broadCastReceiver = new BroadcastReceiver() {

              @Override
              public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                //  Toast.makeText(mContext, intent.getStringExtra("cmd_value"), Toast.LENGTH_SHORT).show();
              }
            };

            //注册广播接收者
            mContext.registerReceiver(broadCastReceiver, cmdFilter);
          }

          Intent broadcastIntent = new Intent(CMD_TOAST_BROADCAST);
          broadcastIntent.putExtra("cmd_value", str + action);
          mContext.sendBroadcast(broadcastIntent, null);

          if (BCStringUtil.compare(action, "invitationUser") || BCStringUtil
              .compare(action, "invitationGroup")) {
            if (!easeUI.hasForegroundActivies()) {
              getNotifier().onNewMsg(message);
            }
            HxUtils.getInstance(mContext).setNewNotify(true);
          }
        }
      }

      @Override
      public void onMessageReadAckReceived(List<EMMessage> messages) {
        for (EMMessage message : messages) {
          message.setDelivered(true);
        }
      }

      @Override
      public void onMessageDeliveryAckReceived(List<EMMessage> messages) {
        for (EMMessage message : messages) {
          message.setAcked(true);
        }
      }

      @Override
      public void onMessageChanged(EMMessage message, Object change) {
        //应用在后台，不需要刷新UI,通知栏提示新消息
        if (!easeUI.hasForegroundActivies()) {
          getNotifier().onNewMsg(message);
        }
      }
    };

    EMClient.getInstance().chatManager().addMessageListener(eventListener);
  }


  protected void setEaseUIProviders() {
    //不设置，则使用easeui默认的
    easeUI.getNotifier()
        .setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {

          @Override
          public String getTitle(EMMessage message) {
            //修改标题,这里使用默认
            return "新消息";
          }

          @Override
          public int getSmallIcon(EMMessage message) {
            //设置小图标，这里为默认
            return R.drawable.logo;
          }

          @Override
          public String getDisplayedText(EMMessage message) {
            // 设置状态栏的消息提示
            return "新消息";
          }

          @Override
          public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
            return "您收到" + messageNum + "信息";
          }

          @Override
          public Intent getLaunchIntent(EMMessage message) {
            //设置点击通知栏跳转事件
            Intent intent = new Intent(mContext, TXHomeActivity.class);
            return intent;
          }
        });
  }

  /**
   * 群组变动监听
   */
  class MyGroupChangeListener implements EMGroupChangeListener {

    @Override
    public void onInvitationReceived(String groupId, String groupName, String inviter,
        String reason) {

      boolean hasGroup = false;
      for (EMGroup group : EMClient.getInstance().groupManager().getAllGroups()) {
        if (group.getGroupId().equals(groupId)) {
          hasGroup = true;
          break;
        }
      }
      if (!hasGroup) {
        return;
      }

      // 被邀请
      String st3 = mContext.getString(R.string.Invite_you_to_join_a_group_chat);
      EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
      msg.setChatType(EMMessage.ChatType.GroupChat);
      msg.setFrom(inviter);
      msg.setTo(groupId);
      msg.setMsgId(UUID.randomUUID().toString());
      msg.addBody(new EMTextMessageBody(inviter + " " + st3));
      // 保存邀请消息
      EMClient.getInstance().chatManager().saveMessage(msg);
      // 提醒新消息
      getNotifier().viberateAndPlayTone(msg);
      //发送local广播
      broadcastManager.sendBroadcast(new Intent(Contants.ACTION_GROUP_CHANAGED));
    }

    @Override
    public void onInvitationAccepted(String groupId, String inviter, String reason) {

    }

    @Override
    public void onInvitationDeclined(String groupId, String invitee, String reason) {
    }

    @Override
    public void onUserRemoved(String groupId, String groupName) {
      broadcastManager.sendBroadcast(new Intent(Contants.ACTION_GROUP_CHANAGED));
    }

    @Override
    public void onGroupDestroyed(String groupId, String groupName) {
      // 群被解散
      broadcastManager.sendBroadcast(new Intent(Contants.ACTION_GROUP_CHANAGED));
    }

    @Override
    public void onAutoAcceptInvitationFromGroup(String s, String s1, String s2) {

    }

    @Override
    public void onApplicationReceived(String groupId, String groupName, String applyer,
        String reason) {

      // 用户申请加入群聊
      InviteMessage msg = new InviteMessage();
      msg.setFrom(applyer);
      msg.setTime(System.currentTimeMillis());
      msg.setGroupId(groupId);
      msg.setGroupName(groupName);
      msg.setReason(reason);
      BCLogUtil.d(TAG, applyer + " 申请加入群聊：" + groupName);
      msg.setStatus(InviteMessage.InviteMesageStatus.BEAPPLYED);
      notifyNewIviteMessage(msg);
      broadcastManager.sendBroadcast(new Intent(Contants.ACTION_GROUP_CHANAGED));
    }

    @Override
    public void onApplicationAccept(String groupId, String groupName, String accepter) {

      String st4 = mContext.getString(R.string.Agreed_to_your_group_chat_application);
      // 加群申请被同意
      EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
      msg.setChatType(EMMessage.ChatType.GroupChat);
      msg.setFrom(accepter);
      msg.setTo(groupId);
      msg.setMsgId(UUID.randomUUID().toString());
      msg.addBody(new EMTextMessageBody(accepter + " " + st4));
      // 保存同意消息
      EMClient.getInstance().chatManager().saveMessage(msg);
      // 提醒新消息
      getNotifier().viberateAndPlayTone(msg);
      broadcastManager.sendBroadcast(new Intent(Contants.ACTION_GROUP_CHANAGED));
    }

    @Override
    public void onApplicationDeclined(String groupId, String groupName, String decliner,
        String reason) {
      // 加群申请被拒绝，demo未实现
    }
  }

  /**
   * 保存并提示消息的邀请消息
   */
  private void notifyNewIviteMessage(InviteMessage msg) {
     /*   if(inviteMessgeDao == null){
            inviteMessgeDao = new InviteMessgeDao(appContext);
        }
        inviteMessgeDao.saveMessage(msg);
        //保存未读数，这里没有精确计算
        inviteMessgeDao.saveUnreadMessageCount(1);*/
    // 提示有新消息
    getNotifier().viberateAndPlayTone(null);
  }

  /**
   * 获取消息通知类
   */
  public EaseNotifier getNotifier() {
    return easeUI.getNotifier();
  }
}
