package com.txsh.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.baichang.android.common.BCAdapterBase;
import com.baichang.android.request.HttpErrorListener;
import com.baichang.android.request.HttpSubscriber;
import com.baichang.android.request.HttpSuccessListener;
import com.bumptech.glide.Glide;
import com.easemob.easeui.model.HxUser;
import com.easemob.easeui.utils.APIConstants;
import com.easemob.easeui.utils.EaseCommonUtils;
import com.easemob.easeui.utils.EaseSmileUtils;
import com.easemob.easeui.utils.HxApi;
import com.easemob.easeui.utils.HxHttpApi;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.DateUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class HxMsgRecordAdapter extends BCAdapterBase<EMMessage> {


  @ViewInject(R.id.tv_content)
  private TextView tvContent;
  @ViewInject(R.id.tv_time)
  private TextView tvTime;

  public HxMsgRecordAdapter(Context context, int viewXml) {
    super(context, viewXml);
  }

  @Override
  protected void setItemData(View view, final EMMessage data, int position) {
    ViewUtils.inject(this, view);
    final TextView tvName = (TextView) view.findViewById(R.id.tv_name);
    final ImageView tvPhoto = (ImageView) view.findViewById(R.id.tv_photo);
    String username = data.getFrom();
    Map<String, String> map = new HashMap<String, String>();
    map.put("emId", username);
    HxApi requset = new HxHttpApi();
    requset.getUserByEmId(map)
        .compose(HttpSubscriber.<HxUser>applySchedulers())
        .subscribe(new HttpSubscriber<HxUser>(new HttpSuccessListener<HxUser>() {
          @Override
          public void success(HxUser hxUser) {
            Glide.with(mContext)
                .load(APIConstants.API_LOAD_IMAGE + hxUser.photo)
                .error(R.drawable.em_default_avatar)
                .fitCenter()
                .into(tvPhoto);
            tvName.setText(hxUser.name);
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable throwable) {

          }
        }));
    tvContent.setText(EaseSmileUtils
            .getSmiledText(mContext, EaseCommonUtils.getMessageDigest(data, (mContext))),
        TextView.BufferType.SPANNABLE);
    tvTime.setText(DateUtils.getTimestampString(new Date(data.getMsgTime())));
  }
}
