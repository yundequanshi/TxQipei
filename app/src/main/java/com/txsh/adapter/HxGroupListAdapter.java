package com.txsh.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.baichang.android.common.BCAdapterBase;
import com.easemob.easeui.model.GroupData;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;


/**
 * Created by xuhanyu on 2015/12/11.
 */
public class HxGroupListAdapter extends BCAdapterBase<GroupData> {

  /*  @ViewInject(R.id.business_iv_left)
    private ImageView mLeft;*/
    @ViewInject(com.easemob.easeui.R.id.group_tv_name)
    private TextView mName;

    public HxGroupListAdapter(Context context, int viewXml) {
        super(context, viewXml);
    }

    @Override
    protected void setItemData(View view, GroupData data, int position) {
        ViewUtils.inject(this, view);
        //Glide.with(mContext).load(APIConstants.API_LOAD_IMAGE + data.userPhoto).fitCenter().placeholder(R.mipmap.chanpinslt).into(mLeft);
        mName.setText(data.name);

    }

}
