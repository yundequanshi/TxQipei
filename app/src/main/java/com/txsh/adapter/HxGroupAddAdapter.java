package com.txsh.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.baichang.android.common.BCAdapterBase;
import com.baichang.android.request.HttpErrorListener;
import com.baichang.android.request.HttpSubscriber;
import com.baichang.android.request.HttpSuccessListener;
import com.baichang.android.utils.BCToastUtil;
import com.easemob.easeui.model.GroupData;
import com.easemob.easeui.model.HxUser;
import com.easemob.easeui.utils.HxApi;
import com.easemob.easeui.utils.HxHttpApi;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.txsh.model.HxUserLoginData;
import com.zuomei.base.MLAppDiskCache;

import java.util.HashMap;
import java.util.Map;


public class HxGroupAddAdapter extends BCAdapterBase<GroupData> {

  @ViewInject(R.id.add_iv_avatar)

  private ImageView mIvAvatar;
  @ViewInject(R.id.add_tv_name)
  private TextView mTvName;
  @ViewInject(R.id.add_btn)
  private Button mBtnAdd;

  public HxGroupAddAdapter(Context context, int viewXml) {
    super(context, viewXml);
  }

  @Override
  protected void setItemData(View view, final GroupData data, int position) {
    ViewUtils.inject(this, view);
    //Glide.with(mContext).load(APIConstants.API_LOAD_IMAGE + data.photo).fitCenter().placeholder(R.mipmap.chanpinslt).into(mIvAvatar);
    mIvAvatar.setImageResource(R.drawable.ease_groups_icon);
    mTvName.setText(data.name);

    mBtnAdd.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        add2Group(data);
      }
    });
  }

  private void add2Group(GroupData groupData) {
    HxUserLoginData user = MLAppDiskCache.getUser();
    if (user == null) {
      return;
    }
    Map<String, String> map = new HashMap<String, String>();
    map.put("userKid", user.hxKid);
    map.put("groupKid", groupData.kid);
    HxApi requset = new HxHttpApi();
    requset.applyGroup(map)
        .compose(HttpSubscriber.<String>applySchedulers())
        .subscribe(new HttpSubscriber<String>(new HttpSuccessListener<String>() {
          @Override
          public void success(String s) {
            BCToastUtil.showMessage(mContext, "请等待群主同意");
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable throwable) {

          }
        }));
  }

}
