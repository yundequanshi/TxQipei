package com.easemob.easeui.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.baichang.android.utils.BCDialogUtil;
import com.easemob.easeui.R;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.model.HxConfig;
import com.easemob.easeui.model.HxUser;
import com.easemob.easeui.utils.MLDBUtils;
import com.easemob.easeui.widget.EaseConversationList;
import com.easemob.easeui.widget.EaseTitleBar;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.lidroid.xutils.db.sqlite.Selector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import cn.ml.base.utils.IEvent;

/**
 * 会话列表fragment
 */
public class EaseConversationListFragment extends EaseBaseFragment1 implements EMMessageListener {
    protected EditText query;
    protected ImageButton clearSearch;
    protected boolean hidden;
    protected List<EMConversation> conversationList = new ArrayList<EMConversation>();
    protected EaseConversationList conversationListView;
    protected FrameLayout errorItemContainer;
    protected EaseTitleBar titleBar;

    protected boolean isConflict;

    private IEvent<Object> mEvent;
    private Button btn;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.ease_fragment_conversation_list, container, false);
            inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            titleBar = (EaseTitleBar) view.findViewById(R.id.title_bar);

            initView();
            setUpView();
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    protected void initView() {
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //会话列表控件
        conversationListView = (EaseConversationList) view.findViewById(R.id.list);
        // 搜索框
        query = (EditText) view.findViewById(R.id.query);
        // 搜索框中清除button
        clearSearch = (ImageButton) view.findViewById(R.id.search_clear);
        errorItemContainer = (FrameLayout) view.findViewById(R.id.fl_error_item);

        /*marcello
        * 好友按钮
        * */
        titleBar = (EaseTitleBar) view.findViewById(R.id.titlebar);
        titleBar.setLeftImageResource(R.drawable.tongxunlu);
        titleBar.setBackgroundColor(Color.parseColor("#161A24"));
        titleBar.setTitle("好友");
    }

    @Override
    protected void setUpView() {
        conversationList.addAll(loadConversationList());
        conversationListView.init(conversationList);

        conversationListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = conversationListView.getItem(position);
                if (conversation.isGroup()) {
                    mEvent.onEvent(4, conversation.getUserName());
                } else {
                    HxUser user = MLDBUtils.getFirst(Selector.from(HxUser.class).where("emId", "=", conversation.getUserName()));
                    if (user == null) {
                        return;
                    }
                    user.isNoUserId = true;
                    mEvent.onEvent(3, user);
                }
            }
        });

        conversationListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                BCDialogUtil.showDialog(getActivity(), R.color.top_bar_normal_bg, "提示", "确认删除会话？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EMConversation conversation = conversationListView.getItem(position);
                        EMClient.getInstance().chatManager().deleteConversation(conversation.getUserName(), false);
                        mEvent.onEvent(5, true);
                        refresh();
                    }
                }, null);
                return true;
            }
        });
        EMClient.getInstance().addConnectionListener(connectionListener);

        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                conversationListView.filter(s);
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
        clearSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
                hideSoftKeyboard();
            }
        });

        conversationListView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });

        titleBar.setLeftLayoutClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEvent != null) {
                    mEvent.onEvent(2, "left");
                }
            }
        });
    }


    protected EMConnectionListener connectionListener = new EMConnectionListener() {

        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED) {
                isConflict = true;
            } else {
                handler.sendEmptyMessage(0);
            }
        }

        @Override
        public void onConnected() {
            handler.sendEmptyMessage(1);
        }
    };
    private EaseConversationListItemClickListener listItemClickListener;

    protected Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    onConnectionDisconnected();
                    break;
                case 1:
                    onConnectionConnected();
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * 连接到服务器
     */
    protected void onConnectionConnected() {
        errorItemContainer.setVisibility(View.GONE);
    }

    /**
     * 连接断开
     */
    protected void onConnectionDisconnected() {
        errorItemContainer.setVisibility(View.VISIBLE);
    }


    /**
     * 刷新页面
     */
    public void refresh() {
        conversationList.clear();
        conversationList.addAll(loadConversationList());
        conversationListView.refresh();
    }

    /**
     * 获取会话列表
     *
     * @return +
     */
    protected List<EMConversation> loadConversationList() {
        // 获取所有会话，包括陌生人
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        // 过滤掉messages size为0的conversation
        /**
         * 如果在排序过程中有新消息收到，lastMsgTime会发生变化
         * 影响排序过程，Collection.sort会产生异常
         * 保证Conversation在Sort过程中最后一条消息的时间不变 
         * 避免并发问题
         */
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    if (conversation.getType() == EMConversation.EMConversationType.GroupChat) {
                        EMGroup group = EMClient.getInstance().groupManager().getGroup(conversation.getUserName());
                        if (group != null) {
                            sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                        }
                    } else {
                        sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                    }
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * 根据最后一条消息的时间排序
     **/
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first == con2.first) {
                    return 0;
                } else if (con2.first > con1.first) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden && !isConflict) {
            refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) {
            refresh();
        }
        setUpView();
        refreshNotify();
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                refresh();
                mEvent.onEvent(5, true);
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> list) {
            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> list) {
            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {
            }
        });


    }

    //是否有系统消息
    private void refreshNotify() {
        HxConfig config = MLDBUtils.getFirst(HxConfig.class);
        if (config != null && config.isNewNotify) {
            titleBar.setLeftImageResource(R.drawable.tongxunlu_red);
        } else {
            titleBar.setLeftImageResource(R.drawable.tongxunlu);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().removeConnectionListener(connectionListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isConflict) {
            outState.putBoolean("isConflict", true);
        }
    }

    @Override
    public void onMessageReceived(List<EMMessage> list) {
        // 获取到message
        refresh();
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageReadAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageDeliveryAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }

    public interface EaseConversationListItemClickListener {
        /**
         * 会话listview item点击事件
         *
         * @param conversation 被点击item所对应的会话
         */
        void onListItemClicked(EMConversation conversation);
    }

    /**
     * 设置listview item点击事件
     *
     * @param listItemClickListener
     */
    public void setConversationListItemClickListener(EaseConversationListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mEvent = (IEvent<Object>) activity;
    }
}
