package com.txsh.friend;

import android.content.Intent;
import android.os.Bundle;

import com.lidroid.xutils.ViewUtils;
import com.txsh.R;
import com.zuomei.base.BaseAct;

/**
 *  聊天
 */
public class ChatAty extends BaseAct {
    public static ChatAty activityInstance;
    String toChatUsername;
    private ChatFrag chatFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hx_chat);
        ViewUtils.inject(this);

        chatFragment = new ChatFrag();
        //传入参数
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .add(R.id.chat_lay, chatFragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // 点击notification bar进入聊天页面，保证只有一个聊天页面
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
    }

    public String getToChatUsername() {
        return toChatUsername;
    }
}
