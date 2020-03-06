package com.txsh.friend;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.baichang.android.request.HttpErrorListener;
import com.baichang.android.request.HttpSubscriber;
import com.baichang.android.request.HttpSuccessListener;
import com.baichang.android.utils.BCStringUtil;
import com.easemob.easeui.model.GroupData;
import com.easemob.easeui.model.HxUser;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.adapter.HxGroupAddAdapter;
import com.txsh.model.HxUserLoginData;
import com.zuomei.base.BaseAct;
import com.easemob.easeui.utils.HxApi;
import com.easemob.easeui.utils.HxHttpApi;
import com.zuomei.base.MLAppDiskCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Marcello on 2015/12/23. Description  添加群
 */
public class GroupAddAty extends BaseAct {

  @ViewInject(R.id.add_lv)
  private ListView mLv;

  @ViewInject(R.id.add_serach_et)
  private EditText mEtCotent;
  @ViewInject(R.id.contact_view_divider)
  private View mViewDivider;

  private HxGroupAddAdapter mAdapter;
  private List<GroupData> mDataGroup = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.hx_group_add);
    ViewUtils.inject(this);
    mAdapter = new HxGroupAddAdapter(getAty(), R.layout.hx_contact_add_item);
    mLv.setAdapter(mAdapter);

  }

  @OnClick(R.id.titlebar_tv_right)
  public void searchOnClick(View view) {
    String content = mEtCotent.getText().toString();
    if (BCStringUtil.isEmpty(content)) {
      showMessage(getAty(), "请输入群名称");
      return;
    }

    HxUserLoginData data = MLAppDiskCache.getUser();
    if (data == null) {
      return;
    }
    Map<String, String> map = new HashMap<String, String>();
    map.put("groupName", content);
    map.put("pageSize", "40");
    HxApi requset = new HxHttpApi();
    requset.findGroup(map)
        .compose(HttpSubscriber.<List<GroupData>>applySchedulers())
        .subscribe(new HttpSubscriber<List<GroupData>>(new HttpSuccessListener<List<GroupData>>() {
          @Override
          public void success(List<GroupData> groupDatas) {
            mDataGroup.clear();
            mDataGroup.addAll(groupDatas);
            mAdapter.setData(mDataGroup);

            if (mDataGroup.size() > 0) {
              mViewDivider.setVisibility(View.VISIBLE);
            }
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable throwable) {

          }
        }));
  }

  @OnClick(R.id.titlebar_tv_left)
  public void backOnClick(View view) {
    finish();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }
}
