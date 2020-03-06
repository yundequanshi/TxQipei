package com.txsh.friend;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.baichang.android.utils.BCToolsUtil;
import com.easemob.easeui.utils.EaseCommonUtils;
import com.easemob.easeui.utils.EaseSmileUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.txsh.adapter.HxMsgRecordAdapter;
import com.zuomei.base.BaseAct;

import java.util.ArrayList;
import java.util.List;

public class HxMsgRecordActivity extends BaseAct {

    @ViewInject(R.id.lv_msg)
    private ListView lvMsg;

    private HxMsgRecordAdapter hxMsgRecordAdapter;
    private String username = "";
    private List<EMMessage> messages = new ArrayList<>();
    private List<EMMessage> copyMessages = new ArrayList<>();
    private ImageButton clearSearch;
    private EditText query;
    private MyFilter myFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hx_msg_record);
        ViewUtils.inject(this);
        //搜索框
        query = (EditText) findViewById(com.easemob.easeui.R.id.query);
        clearSearch = (ImageButton) findViewById(com.easemob.easeui.R.id.search_clear);
        if (getIntentData() != null) {
            username = (String) getIntentData();
            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
            messages = conversation.getAllMessages();
            myFilter = new MyFilter(messages);
        }
        hxMsgRecordAdapter = new HxMsgRecordAdapter(getAty(), R.layout.hx_msg_record);
        lvMsg.setAdapter(hxMsgRecordAdapter);
        hxMsgRecordAdapter.setData(messages);
        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                myFilter.filter(s);
                if (s.length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);
                } else {
                    clearSearch.setVisibility(View.INVISIBLE);

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
                BCToolsUtil.hideKeyboard(getBaseContext());
            }
        });

        lvMsg.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 隐藏软键盘
                BCToolsUtil.hideKeyboard(getBaseContext());
                return false;
            }
        });
    }

    protected class MyFilter extends Filter {
        List<EMMessage> mOriginalList = null;

        public MyFilter(List<EMMessage> myList) {
            this.mOriginalList = myList;
        }

        @Override
        protected synchronized FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (mOriginalList == null) {
                mOriginalList = new ArrayList<EMMessage>();
            }
            String prefixString = prefix.toString();
            final int count = mOriginalList.size();
            final ArrayList<EMMessage> newValues = new ArrayList<EMMessage>();
            for (int i = 0; i < count; i++) {
                final EMMessage user = mOriginalList.get(i);
                String username = String.valueOf(EaseSmileUtils.getSmiledText(getBaseContext(),
                        EaseCommonUtils.getMessageDigest(user, (getBaseContext()))));
                if (username.startsWith(prefixString)) {
                    newValues.add(user);
                } else {
                    final String[] words = username.split(" ");
                    final int wordCount = words.length;
                    for (int k = 0; k < wordCount; k++) {
                        if (words[k].startsWith(prefixString)) {
                            newValues.add(user);
                            break;
                        }
                    }
                }
            }
            results.values = newValues;
            results.count = newValues.size();
            return results;
        }

        @Override
        protected synchronized void publishResults(CharSequence constraint,
                                                   FilterResults results) {
            copyMessages.clear();
            copyMessages.addAll((List<EMMessage>) results.values);
            hxMsgRecordAdapter.setData(copyMessages);
        }
    }
}
