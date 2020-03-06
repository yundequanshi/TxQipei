package com.qipei.interact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qipei.adapter.FragmentTabAdapter;
import com.txsh.R;
import com.zuomei.base.BaseFragment;
import com.zuomei.base.MLEventBusModel;
import com.zuomei.constants.MLConstants;
import com.zuomei.home.MLMyMessageFrg;

import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 互动 Created by Marcello on 2015/6/2.
 */
public class MLInteractFrag extends BaseFragment {

  private Context _context;
  private View view;

  @ViewInject(R.id.interact_rb_tab1)
  private RadioButton rbHudong;
  @ViewInject(R.id.interact_rb_tab2)
  private RadioButton rbWofabu;
  @ViewInject(R.id.interact_rb_tab3)
  private RadioButton rbWohuifu;

  @ViewInject(R.id.interact_rg)
  private RadioGroup mGroup;

  private List<Fragment> fragments = new ArrayList<Fragment>();
  private MLInteractListFrag mFragInterActList;
  private FragmentTabAdapter tabAdapter;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.home_tab2, null);
    ViewUtils.inject(this, view);
    EventBus.getDefault().register(this);
    _context = inflater.getContext();

    mFragInterActList = new MLInteractListFrag();
    //发表
    MLMyMessageFrg mFragInterActPub = new MLMyMessageFrg();
    //回复
    MLInteractReplyFrg mFragInterActReply = new MLInteractReplyFrg();
    fragments.add(mFragInterActList);
    fragments.add(mFragInterActPub);
    fragments.add(mFragInterActReply);

    tabAdapter = new FragmentTabAdapter(getActivity(), fragments,
        R.id.interact_fl_content, mGroup);
    tabAdapter.setOnRgsExtraCheckedChangedListener(
        new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
          @Override
          public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {

          }
        });
    return view;
  }


  @Subscribe(threadMode = ThreadMode.MAIN)
  public void event(MLEventBusModel model) {
    if (model.type == MLConstants.INTERACTLIST) {
      setFragment(1);
    }
  }

  public void setFragment(int index) {
    rbHudong.setChecked(false);
    rbWofabu.setChecked(true);
    rbWohuifu.setChecked(false);
  }

  //发布互动
  @OnClick(R.id.tab2_iv_add)
  public void add(View view) {
    startAct(MLInteractFrag.this, MLInteractAddAty.class, "", 1);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == 1) {
      mFragInterActList.initData();
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }
}
