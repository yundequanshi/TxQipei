package com.txsh.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.baichang.android.common.BCAdapterBase;
import com.bumptech.glide.Glide;
import com.easemob.easeui.model.HxUser;
import com.easemob.easeui.utils.APIConstants;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;


/**
 * Created by xuhanyu on 2015/12/11.
 */
public class HxGroupContactAdapter extends BCAdapterBase<HxUser> {

    @ViewInject(com.easemob.easeui.R.id.contact_iv_icon)
    private ImageView mIvIcon;
    @ViewInject(com.easemob.easeui.R.id.contact_iv_state)
    private ImageView mIvState;
    @ViewInject(com.easemob.easeui.R.id.contact_tv_name)
    private TextView mTvName;

    public HxGroupContactAdapter(Context context, int viewXml) {
        super(context, viewXml);
    }

    @Override
    protected void setItemData(View view, HxUser data, int position) {
        ViewUtils.inject(this, view);
        Glide.with(mContext)
                .load(APIConstants.API_LOAD_IMAGE + data.photo)
                .fitCenter()
                .error(com.easemob.easeui.R.drawable.em_default_avatar)
                .into(mIvIcon);
        mTvName.setText(data.name);

        if (data.isCheck) {
            mIvState.setVisibility(View.VISIBLE);
        } else {
            mIvState.setVisibility(View.GONE);
        }
    }

}
