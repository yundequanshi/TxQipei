package com.txsh.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.txsh.model.TXShopAddressRes;

import cn.ml.base.MLAdapterBase;
import cn.ml.base.utils.MLStrUtil;

/**
 * Created by Marcello on 2015/6/3.
 */
public class TXMyAddressAdapter extends MLAdapterBase<TXShopAddressRes.TXShopAddressData> {

    public TXMyAddressAdapter(Context context, int viewXml) {
        super(context, viewXml);
    }

    @ViewInject(R.id.address_tv_phone)
    private TextView _phone;
    @ViewInject(R.id.address_tv_name)
    private TextView _name;
    @ViewInject(R.id.address_tv_ad)
    private TextView _address;

    @Override
    protected void setItemData(View view, final TXShopAddressRes.TXShopAddressData data, final int position) {
        ViewUtils.inject(this, view);

        _name.setText(data.name);
        _phone.setText(data.mobile);
        if(MLStrUtil.compare(data.isDefaultAddress,"0")){
            _address.setText(data.address);
        }else{
            _address.setText(Html.fromHtml(String.format("<font color=\"#E90C0C\">[默认] </font>%s", data.address)));
        }
    }
}