package com.txsh.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.baichang.android.common.BCAdapterBase;
import com.bumptech.glide.Glide;
import com.easemob.easeui.model.HxUser;
import com.easemob.easeui.utils.APIConstants;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.base.MLEventBusModel;
import com.zuomei.constants.MLConstants;
import org.greenrobot.eventbus.EventBus;


public class HxContactAddAdapter extends BCAdapterBase<HxUser> {

    @ViewInject(R.id.add_iv_avatar)
    private ImageView mIvAvatar;
    @ViewInject(R.id.add_tv_name)
    private TextView mTvName;
    @ViewInject(R.id.add_btn)
    private Button mBtnAdd;

    public HxContactAddAdapter(Context context, int viewXml) {
        super(context, viewXml);
    }

    @Override
    protected void setItemData(View view, final HxUser data, int position) {
        ViewUtils.inject(this, view);
        Glide.with(mContext)
                .load(APIConstants.API_LOAD_IMAGE + data.photo)
                .error(R.drawable.em_default_avatar)
                .fitCenter()
                .into(mIvAvatar);
        mTvName.setText(data.name);

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new MLEventBusModel(MLConstants.EVENT_CONTACT_ADD, data));
            }
        });
    }

}
