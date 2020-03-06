/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easemob.easeui.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.easeui.R;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.model.HxUser;
import com.easemob.easeui.utils.EaseCommonUtils;
import com.easemob.easeui.widget.EaseContactList;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 联系人列表页
 */
public class EaseContactListFragment extends EaseBaseFragment {
    private static final String TAG = "Hx";
    protected List<HxUser> hxUser;
    protected ListView listView;
    protected boolean hidden;
    protected List<String> blackList;
    protected ImageButton clearSearch;
    protected EditText query;
    protected Handler handler = new Handler();
    protected EaseUser toBeProcessUser;
    protected String toBeProcessUsername;
    protected EaseContactList contactListLayout;
    protected boolean isConflict;
    protected FrameLayout contentContainer;
    private AdapterView.OnItemLongClickListener onItemLongClickListener;
    private EaseContactsListItemAddClickListener mItemAddClickListener;

    protected TextView mTvUnread;
    protected RelativeLayout mLayRead;

    private Map<String, EaseUser> contactsMap;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ease_fragment_contact_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView() {
        contentContainer = (FrameLayout) getView().findViewById(R.id.content_container);
        contactListLayout = (EaseContactList) getView().findViewById(R.id.contact_list);
        listView = contactListLayout.getListView();
        View headview = View.inflate(getActivity(), R.layout.contact_view_head, null);
        listView.addHeaderView(headview);
        headview.findViewById(R.id.header_group).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setHeadOnClick(1);
            }
        });
        headview.findViewById(R.id.header_notify).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setHeadOnClick(2);
            }
        });
        mTvUnread = (TextView) headview.findViewById(R.id.head_tv_unread);
        mLayRead = (RelativeLayout) headview.findViewById(R.id.head_lay_unread);
        //搜索框
        query = (EditText) getView().findViewById(R.id.query);
        clearSearch = (ImageButton) getView().findViewById(R.id.search_clear);
    }

    @Override
    protected void setUpView() {
        EMClient.getInstance().addConnectionListener(connectionListener);

        //黑名单列表
        // blackList = EMContactManager.getInstance().getBlackListUsernames();
        hxUser = new ArrayList<HxUser>();
        // 获取设置contactlist
        // getContactList();
        //     setContactList();
        //init list
        //   contactListLayout.init(contactList);

        if (listItemClickListener != null) {
            listView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HxUser user = (HxUser) listView.getItemAtPosition(position);
                    listItemClickListener.onListItemClicked(user);
//                    itemClickLaunchIntent.putExtra(EaseConstant.USER_ID, username);
                }
            });
        }
        if (onItemLongClickListener != null) {
            listView.setOnItemLongClickListener(onItemLongClickListener);
        }

        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contactListLayout.filter(s);
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

        listView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 隐藏软键盘
                hideSoftKeyboard();
                return false;
            }
        });

    }

    protected void setHxUser(List<HxUser> datas) {
        hxUser.clear();
        synchronized (hxUser) {
            //获取联系人列表
            if (datas == null) {
                return;
            }


            for (HxUser user : datas) {
                EaseCommonUtils.setUserInitialLetter(user);
            }

            hxUser = datas;
            // 排序
            Collections.sort(hxUser, new Comparator<HxUser>() {

                @Override
                public int compare(HxUser lhs, HxUser rhs) {
                    if (lhs == null || rhs == null || lhs.initialLetter == null || rhs.initialLetter == null)
                        return -1;
                    if (lhs.initialLetter.equals(rhs.initialLetter)) {
                        return lhs.name.compareTo(rhs.name);
                    } else {
                        if ("#".equals(lhs.initialLetter)) {
                            return 1;
                        } else if ("#".equals(rhs.initialLetter)) {
                            return -1;
                        }
                        return lhs.initialLetter.compareTo(rhs.initialLetter);
                    }

                }
            });
        }
        contactListLayout.init(hxUser, mItemAddClickListener);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden) {
            refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) {
            refresh();
        }
    }


    /**
     * 把user移入到黑名单
     */
    protected void moveToBlacklist(final String username) {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        String st1 = getResources().getString(R.string.Is_moved_into_blacklist);
        final String st2 = getResources().getString(R.string.Move_into_blacklist_success);
        final String st3 = getResources().getString(R.string.Move_into_blacklist_failure);
        pd.setMessage(st1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    //加入到黑名单
                    EMClient.getInstance().contactManager().addUserToBlackList(username, false);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getActivity(), st2, Toast.LENGTH_SHORT).show();
                            refresh();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getActivity(), st3, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();

    }

    // 刷新ui
    public void refresh() {
        // getContactList();
        contactListLayout.refresh();
    }


    @Override
    public void onDestroy() {

        EMClient.getInstance().removeConnectionListener(connectionListener);
        super.onDestroy();
    }

    protected EMConnectionListener connectionListener = new EMConnectionListener() {

        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE || error == EMError.SERVER_SERVICE_RESTRICTED) {
                isConflict = true;
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        onConnectionDisconnected();
                    }

                });
            }
        }

        @Override
        public void onConnected() {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    onConnectionConnected();
                }

            });
        }
    };
    private EaseContactListItemClickListener listItemClickListener;

    protected void onConnectionDisconnected() {

    }

    protected void onConnectionConnected() {

    }

    /**
     * 设置需要显示的数据map，key为环信用户id
     *
     * @param contactsMap
     */
    public void setContactsMap(Map<String, EaseUser> contactsMap) {
        this.contactsMap = contactsMap;
    }

    public interface EaseContactListItemClickListener {
        /**
         * 联系人listview item点击事件
         *
         * @param user 被点击item所对应的user对象
         */
        void onListItemClicked(HxUser user);
    }


    /**
     * 设置listview item点击事件
     *
     * @param listItemClickListener
     */
    public void setContactListItemClickListener(EaseContactListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

    /**
     * 设置listview item长按事件
     *
     * @param listItemClickListener
     */
    public void setContactListItemLongClickListener(AdapterView.OnItemLongClickListener listItemClickListener) {
        this.onItemLongClickListener = listItemClickListener;
    }

    public interface EaseContactsListItemAddClickListener {
        /**
         * 添加联系人点击事件
         */
        void onListItemAddClicked(HxUser user);
    }

    /**
     * 设置listview Add点击事件
     *
     * @param listItemClickListener
     */
    public void setContactListItemAddClickListener(EaseContactsListItemAddClickListener listItemClickListener) {

        mItemAddClickListener = listItemClickListener;
    }

    /**
     * 设置listview head item点击事件
     * 0 通知
     * 1群聊
     */
    public void setHeadOnClick(int i) {

    }
}
