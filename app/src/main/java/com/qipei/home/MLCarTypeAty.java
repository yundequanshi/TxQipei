package com.qipei.home;

import android.os.Bundle;

import com.lidroid.xutils.ViewUtils;
import com.txsh.R;
import com.zuomei.base.BaseActivity;

/**
 * Created by Marcello on 2015/6/15.
 */
public class MLCarTypeAty extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qp_business_detail);
        ViewUtils.inject(this);

    }
}
