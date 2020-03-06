package com.txsh.friend;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.lidroid.xutils.ViewUtils;
import com.txsh.R;
import com.zuomei.base.BaseAct;

/**
 * 好友列表
 */
public class ContactListAty extends BaseAct {

    private FragmentManager mFragmentManager;
    private ContactListFrag contactListFrag = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hx_contact_list);
        ViewUtils.inject(this);
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (contactListFrag == null) {
            contactListFrag = new ContactListFrag();
            fragmentTransaction.add(R.id.ll_main, contactListFrag);
        } else {
            fragmentTransaction.show(contactListFrag);
        }
        fragmentTransaction.commit();
    }
}
