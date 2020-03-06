package com.txsh.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.baichang.android.common.BCAdapterBase;
import com.baichang.android.utils.BCStringUtil;
import com.easemob.easeui.model.TodoData;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.base.MLEventBusModel;
import com.zuomei.constants.MLConstants;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by xuhanyu on 2015/12/11.
 */
public class HxNotifyListAdapter extends BCAdapterBase<TodoData> {

    @ViewInject(R.id.notify_iv_icon)
    private ImageView mIvIcon;
    @ViewInject(R.id.notify_tv_name)
    private TextView mTvName;
    @ViewInject(R.id.notify_tv_content)
    private TextView mTvContent;
    @ViewInject(R.id.add_btn)
    private Button mBtnAction;

    public HxNotifyListAdapter(Context context, int viewXml) {
        super(context, viewXml);
    }

    @Override
    protected void setItemData(View view, final TodoData data, int position) {
        ViewUtils.inject(this, view);
        //Glide.with(mContext).load(APIConstants.API_LOAD_IMAGE + data.userPhoto).fitCenter().placeholder(R.mipmap.chanpinslt).into(mLeft);
        mTvName.setText(data.userName);
        if (BCStringUtil.compare(data.action, "invitationUser")) {
            mTvContent.setText("请求加为好友");
        }

        if (BCStringUtil.compare(data.action, "invitationGroup")) {
            data.initGroup();
            mTvContent.setText(String.format("邀请您加入《%s》群", data.groupName));
        }

        if (BCStringUtil.compare(data.action, "applyGroup")) {
            data.initGroup();
            mTvContent.setText(String.format("《%s》申请加入《%s》群", data.userName, data.groupName));
        }

        mBtnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BCStringUtil.compare(data.action, "invitationUser")) {
                    EventBus.getDefault().post(new MLEventBusModel(MLConstants.EVENT_NOTIFY_FRIEND, data));
                } else if (BCStringUtil.compare(data.action, "applyGroup")) {
                    EventBus.getDefault().post(new MLEventBusModel(MLConstants.EVENT_APPLY_GROUP, data));
                } else if (BCStringUtil.compare(data.action, "invitationGroup")) {
                    EventBus.getDefault().post(new MLEventBusModel(MLConstants.EVENT_NOTIFY_GROUP_INVITE, data));
                }
            }
        });
    }

}
