package com.txsh.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.baichang.android.common.BCAdapterBase;
import com.baichang.android.utils.BCStringUtil;
import com.bumptech.glide.Glide;
import com.easemob.easeui.model.HxUser;
import com.easemob.easeui.utils.APIConstants;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;


public class HxGroupMemberAdapter extends BCAdapterBase<HxUser> {

    @ViewInject(R.id.member_iv_icon)
    private ImageView mIvAvatar;
    @ViewInject(R.id.member_tv_name)
    private TextView mTvName;

    public HxGroupMemberAdapter(Context context, int viewXml) {
        super(context, viewXml);
    }

    @Override
    protected void setItemData(View view, final HxUser data, int position) {
        ViewUtils.inject(this, view);
        if(BCStringUtil.compare(data.id, "-1")){
            Glide.with(mContext)
                    .load("")
                    .centerCrop()
                    .error(R.drawable.smiley_add_btn_nor)
                    .into(mIvAvatar);
            mTvName.setVisibility(View.GONE);
        }else {
            Glide.with(mContext)
                    .load(APIConstants.API_LOAD_IMAGE + data.photo)
                    .error(R.drawable.em_default_avatar)
                    .centerCrop()
                    .into(mIvAvatar);
            mTvName.setVisibility(View.VISIBLE);
            mTvName.setText(data.name);
        }
    }

}
