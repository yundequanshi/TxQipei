package com.txsh.friend;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.easemob.easeui.ui.EaseChatFragment;
import com.easemob.easeui.ui.EaseChatFragment.EaseChatFragmentListener;
import com.easemob.easeui.utils.Contants;
import com.easemob.easeui.widget.chatrow.EaseChatRow;
import com.easemob.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.PathUtil;
import com.txsh.R;
import com.txsh.hx.ChatRowVoiceCall;
import com.txsh.hx.ImageGridActivity;
import com.txsh.hx.VideoCallActivity;
import com.txsh.hx.VoiceCallActivity;
import com.zuomei.base.MLBaseConstants;

import java.io.File;
import java.io.FileOutputStream;


public class ChatFrag extends EaseChatFragment implements EaseChatFragmentListener {

  //避免和基类定义的常量可能发生的冲突，常量从11开始定义
  private static final int ITEM_VIDEO = 11;
  private static final int ITEM_FILE = 12;
  private static final int ITEM_VOICE_CALL = 13;
  private static final int ITEM_VIDEO_CALL = 14;
  private static final int ITEM_TRANSFER = 15;

  private static final int REQUEST_CODE_SELECT_VIDEO = 11;
  private static final int REQUEST_CODE_SELECT_FILE = 12;
  private static final int REQUEST_CODE_GROUP_DETAIL = 13;
  private static final int REQUEST_CODE_CONTEXT_MENU = 14;

  private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
  private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
  private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 3;
  private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 4;


  /**
   * 是否为环信小助手
   */
  private boolean isRobot;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override
  protected void setUpView() {
    setChatFragmentListener(this);
      /*  if (chatType == Contants.CHATTYPE_SINGLE) {
            Map<String,RobotUser> robotMap = DemoHelper.getInstance().getRobotList();
            if(robotMap!=null && robotMap.containsKey(toChatUsername)){
                isRobot = true;
            }
        }*/
    super.setUpView();
    // ((EaseEmojiconMenu)inputMenu.getEmojiconMenu()).addEmojiconGroup(EmojiconExampleGroupData.getData());
  }

  @Override
  protected void registerExtendMenuItem() {
    //demo这里不覆盖基类已经注册的item,item点击listener沿用基类的
    super.registerExtendMenuItem();
    //增加扩展item
    inputMenu.registerExtendMenuItem(R.string.attach_video, R.drawable.em_chat_video_selector,
        ITEM_VIDEO, extendMenuItemClickListener);
    inputMenu
        .registerExtendMenuItem(R.string.attach_file, R.drawable.em_chat_file_selector, ITEM_FILE,
            extendMenuItemClickListener);
//    if (chatType == Contants.CHATTYPE_SINGLE) {
//      inputMenu
//          .registerExtendMenuItem("语音通话", R.drawable.em_chat_file_selector, ITEM_VOICE_CALL,
//              extendMenuItemClickListener);
//      inputMenu
//          .registerExtendMenuItem("视频通话", R.drawable.em_chat_file_selector, ITEM_VIDEO_CALL,
//              extendMenuItemClickListener);
//    }
//        if (chatType == Contants.CHATTYPE_SINGLE) {
//            //转账
//            inputMenu.registerExtendMenuItem(R.string.attach_transfer, R.drawable.item_chat_zhuanzhang, ITEM_TRANSFER, extendMenuItemClickListener);
//        }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 1 && resultCode == 1 && data != null) {
      //修改群信息
      String name = data.getStringExtra("data");
      titleBar.setTitle(name);
    }

    if (resultCode == Activity.RESULT_OK) {
      switch (requestCode) {
        case REQUEST_CODE_SELECT_VIDEO: //发送选中的视频
          if (data != null) {
            int duration = data.getIntExtra("dur", 0);
            String videoPath = data.getStringExtra("path");
            File file = new File(PathUtil.getInstance().getImagePath(),
                "thvideo" + System.currentTimeMillis());
            try {
              FileOutputStream fos = new FileOutputStream(file);
              Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
              ThumbBitmap.compress(CompressFormat.JPEG, 100, fos);
              fos.close();
              sendVideoMessage(videoPath, file.getAbsolutePath(), duration);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
          break;
        case REQUEST_CODE_SELECT_FILE: //发送选中的文件
          if (data != null) {
            Uri uri = data.getData();
            if (uri != null) {
              sendFileByUri(uri);
            }
          }
          break;
        default:
          break;
      }
    }

  }

  @Override
  public void onSetMessageAttributes(EMMessage message) {
    if (isRobot) {
      //设置消息扩展属性
      message.setAttribute("em_robot_message", isRobot);
    }
  }

  @Override
  public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
    //设置自定义listview item提供者
    return new CustomChatRowProvider();
  }


  @Override
  public void onEnterToChatDetails() {
    //群资料
    if (chatType == Contants.CHATTYPE_GROUP) {
      startActivityForResult(
          new Intent(getActivity(), GroupInfoAty.class).putExtra("id", toChatUsername), 1);
    }
  }

  @Override
  public void onAvatarClick(String username, String toUserName) {
    //头像点击事件
      /*  Intent intent = new Intent(getActivity(), HxUserInfoActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("toUserName", toUserName);
        startActivity(intent);*/
  }

  @Override
  public boolean onMessageBubbleClick(EMMessage message) {
    //消息框点击事件，demo这里不做覆盖，如需覆盖，return true
    return false;
  }

  @Override
  public void onMessageBubbleLongClick(EMMessage message) {
    //消息框长按
  }

  @Override
  public boolean onExtendMenuItemClick(int itemId, View view) {
    switch (itemId) {
      case ITEM_VIDEO: //视频
        Intent intent = new Intent(getActivity(), ImageGridActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
        break;
      case ITEM_FILE: //一般文件
        //demo这里是通过系统api选择文件，实际app中最好是做成qq那种选择发送文件
        selectFileFromLocal();
        break;
      case ITEM_VOICE_CALL: //音频通话
        startVoiceCall();
        break;
      case ITEM_VIDEO_CALL: //视频通话
        startVideoCall();
        break;
      case ITEM_TRANSFER://转账
        //TODO 跳转账页面
        Intent intent1 = new Intent(getActivity(), ChartTransferAty.class);
        intent1.putExtra(MLBaseConstants.TAG_INTENT_DATA, mDataUser);
        startActivity(intent1);
        break;
      default:
        break;
    }
    //不覆盖已有的点击事件
    return false;
  }

  /**
   * 选择文件
   */
  protected void selectFileFromLocal() {
    Intent intent = null;
    if (Build.VERSION.SDK_INT < 19) { //19以后这个api不可用，demo这里简单处理成图库选择图片
      intent = new Intent(Intent.ACTION_GET_CONTENT);
      intent.setType("*/*");
      intent.addCategory(Intent.CATEGORY_OPENABLE);

    } else {
      intent = new Intent(Intent.ACTION_PICK,
          android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }
    startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
  }

  /**
   * 拨打语音电话
   */
  protected void startVoiceCall() {
    if (!EMClient.getInstance().isConnected()) {
      Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
    } else {
      startActivity(
          new Intent(getActivity(), VoiceCallActivity.class).putExtra("username", toChatUsername)
              .putExtra("isComingCall", false));
      // voiceCallBtn.setEnabled(false);
      inputMenu.hideExtendMenuContainer();
    }
  }

  /**
   * 拨打视频电话
   */
  protected void startVideoCall() {
    if (!EMClient.getInstance().isConnected()) {
      Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
    } else {
      startActivity(
          new Intent(getActivity(), VideoCallActivity.class)
              .putExtra("username", toChatUsername)
              .putExtra("isComingCall", false));
      // videoCallBtn.setEnabled(false);
      inputMenu.hideExtendMenuContainer();
    }
  }

  /**
   * chat row provider
   */
  private final class CustomChatRowProvider implements EaseCustomChatRowProvider {

    @Override
    public int getCustomChatRowTypeCount() {
      //音、视频通话发送、接收共4种
      return 4;
    }

    @Override
    public int getCustomChatRowType(EMMessage message) {
      if (message.getType() == EMMessage.Type.TXT) {
        //语音通话类型
        if (message.getBooleanAttribute(Contants.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
          return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL
              : MESSAGE_TYPE_SENT_VOICE_CALL;
        } else if (message.getBooleanAttribute(Contants.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
          //视频通话
          return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL
              : MESSAGE_TYPE_SENT_VIDEO_CALL;
        }
      }
      return 0;
    }

    @Override
    public EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
      if (message.getType() == EMMessage.Type.TXT) {
        // 语音通话,  视频通话
        if (message.getBooleanAttribute(Contants.MESSAGE_ATTR_IS_VOICE_CALL, false) ||
            message.getBooleanAttribute(Contants.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
          return new ChatRowVoiceCall(getActivity(), message, position, adapter);
        }
      }
      return null;
    }
  }

}
