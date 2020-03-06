package com.txsh.friend;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baichang.android.request.HttpErrorListener;
import com.baichang.android.request.HttpSubscriber;
import com.baichang.android.request.HttpSuccessListener;
import com.baichang.android.request.UploadUtils;
import com.baichang.android.utils.BCStringUtil;
import com.baichang.android.widget.BCHorizontalTextView;
import com.baichang.android.widget.photoSelectDialog.PhotoSelectDialog;
import com.baichang.android.widget.roundedImageView.RoundedImageView;
import com.bumptech.glide.Glide;
import com.easemob.easeui.model.HxUser;
import com.easemob.easeui.utils.APIConstants;
import com.easemob.easeui.utils.MLDBUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.model.HxUserLoginData;
import com.zuomei.base.BaseAct;
import com.easemob.easeui.utils.HxApi;
import com.easemob.easeui.utils.HxHttpApi;
import com.zuomei.base.MLAppDiskCache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HxUserInfoActivity extends BaseAct {

  @ViewInject(R.id.tv_name)
  private EditText tvName;
  @ViewInject(R.id.riv_info_icon)
  private RoundedImageView roundedImageView;
  @ViewInject(R.id.btn_save)
  private TextView btnSave;
  @ViewInject(R.id.tv_record)
  private BCHorizontalTextView tvRecord;

  private String name = "";
  private String photo = "";
  private String path = "";
  private String username = "";
  private String toUserName = "";
  private int flag = 0;
  private HxUserLoginData data = null;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.hx_user_info);
    ViewUtils.inject(this);
    if (getIntent() != null) {
      username = getIntent().getStringExtra("username");
      toUserName = getIntent().getStringExtra("toUserName");
      getUserInfo(username);
    }
    data = MLAppDiskCache.getUser();
    roundedImageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        PhotoSelectDialog photoSelectDialog = new PhotoSelectDialog();
        photoSelectDialog.setSelectCallBack(new PhotoSelectDialog.PhotoSelectCallback() {
          @Override
          public void onResult(Bitmap bitmap, String s) {
            Glide.with(getAty())
                .load(s)
                .asBitmap()
                .error(R.drawable.em_default_avatar)
                .into(roundedImageView);
            path = s;
            flag = 1;
          }
        });
        photoSelectDialog.show(getSupportFragmentManager(), "photo");
      }
    });
    if (data != null) {
      if (!BCStringUtil.compare(username, data.hxUser)) {
        btnSave.setVisibility(View.GONE);
        tvRecord.setVisibility(View.GONE);
        tvName.setEnabled(false);
        roundedImageView.setClickable(false);
      }
    }
    btnSave.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (BCStringUtil.isEmpty(name)) {
          showMessage(getAty(), "昵称不能为空");
          return;
        }
        if (BCStringUtil.isEmpty(path) && BCStringUtil.isEmpty(photo)) {
          showMessage(getAty(), "头像不能为空");
          return;
        }
        name = tvName.getText().toString();
        if (flag != 0) {
          upload();
        } else {
          updateUser();
        }
      }
    });
  }

  @OnClick(R.id.tv_record)
  private void msgRecord(View view) {
    startAct(getAty(), HxMsgRecordActivity.class, toUserName);
  }

  /**
   * 获取用户信息
   */
  public void getUserInfo(String hxid) {
    Map<String, String> map = new HashMap<String, String>();
    map.put("emId", hxid);
    HxApi requset = new HxHttpApi();
    requset.getUserByEmId(map)
        .compose(HttpSubscriber.<HxUser>applySchedulers())
        .subscribe(new HttpSubscriber<HxUser>(new HttpSuccessListener<HxUser>() {
          @Override
          public void success(HxUser hxUser) {
            photo = hxUser.photo;
            name = hxUser.name;
            tvName.setText(hxUser.name);
            Glide.with(getAty())
                .load(APIConstants.API_LOAD_IMAGE + hxUser.photo)
                .asBitmap()
                .error(R.drawable.em_default_avatar)
                .into(roundedImageView);
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable throwable) {

          }
        }));
  }

  /**
   * 上传图片
   */
  private void upload() {
    HxApi requset = new HxHttpApi();
    requset.upload(UploadUtils.getMultipartBody(path))
        .compose(HttpSubscriber.<List<String>>applySchedulers(this))
        .subscribe(new HttpSubscriber<List<String>>(new HttpSuccessListener<List<String>>() {
          @Override
          public void success(List<String> list) {
            photo = list.get(0).toString();
            updateUser();
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable throwable) {

          }
        }));
  }

  /**
   * 修改个人信息
   */
  private void updateUser() {
    if (data == null) {
      return;
    }
    Map<String, String> map = new HashMap<String, String>();
    map.put("userKid", data.hxKid);
    map.put("userType", "1");
    map.put("name", name);
    map.put("photo", photo);
    HxApi requset = new HxHttpApi();
    requset.updateUser(map)
        .compose(HttpSubscriber.<String>applySchedulers())
        .subscribe(new HttpSubscriber<String>(new HttpSuccessListener<String>() {
          @Override
          public void success(String s) {
            HxUser user = MLDBUtils
                .getFirst(Selector.from(HxUser.class).where("emId", "=", username));
            user.photo = photo;
            user.name = name;
            MLDBUtils.saveOrUpdate(user);
            showMessage(getAty(), "修改成功");
            finish();
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable throwable) {

          }
        }));
  }
}
