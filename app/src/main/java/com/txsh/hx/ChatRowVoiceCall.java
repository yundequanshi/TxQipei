package com.txsh.hx;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.easeui.utils.Contants;
import com.easemob.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

public class ChatRowVoiceCall extends EaseChatRow {

    private TextView contentvView;
    private ImageView iconView;

    public ChatRowVoiceCall(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflatView() {
        if (message.getBooleanAttribute(Contants.MESSAGE_ATTR_IS_VOICE_CALL, false)){
            inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                    com.easemob.easeui.R.layout.ease_row_received_voice_call : com.easemob.easeui.R.layout.ease_row_sent_voice_call, this);
        // 视频通话
        }else if (message.getBooleanAttribute(Contants.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
            inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                    com.easemob.easeui.R.layout.ease_row_received_video_call : com.easemob.easeui.R.layout.ease_row_sent_video_call, this);
        }
    }

    @Override
    protected void onFindViewById() {
        contentvView = (TextView) findViewById(com.easemob.easeui.R.id.tv_chatcontent);
        iconView = (ImageView) findViewById(com.easemob.easeui.R.id.iv_call_icon);
    }

    @Override
    protected void onSetUpView() {
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        contentvView.setText(txtBody.getMessage());
    }
    
    @Override
    protected void onUpdateView() {
        
    }

    @Override
    protected void onBubbleClick() {
        
    }

  

}
