package com.qipei.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qipei.adapter.FragmentTabAdapter;
import com.txsh.R;
import com.zuomei.auxiliary.MLBusinessInfoFrg;
import com.zuomei.base.BaseActivity;
import com.zuomei.model.MLHomeBusinessData;
import com.zuomei.model.MLLogin;

import java.util.ArrayList;
import java.util.List;

import cn.ml.base.utils.IEvent;

/**
 * Created by Marcello on 2015/6/9.
 */
public class MLBusinessDetailAty extends BaseActivity implements IEvent<String> {


    @ViewInject(R.id.detail_rg)
    private RadioGroup mGroup;

    private MLHomeBusinessData mData;
    private List<Fragment> fragments = new ArrayList<Fragment>();

    private MLLogin _user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qp_business_detail);
        ViewUtils.inject(this);

        mData = (MLHomeBusinessData) getIntentData();

      //  MLBusinessMainFrg mFragBusiness = new MLBusinessMainFrg(mData);
        //商家详情
        MLBusinessInfoFrg mFragInfo = MLBusinessInfoFrg.instance(mData);
        //商家报价
        MLBusinessOfferFrg mFragOffer = new MLBusinessOfferFrg(mData);
        fragments.add(mFragInfo);
        fragments.add(mFragOffer);
        fragments.add(mFragInfo);
        FragmentTabAdapter tabAdapter = new FragmentTabAdapter(this, fragments,R.id.detail_fl_content, mGroup);
        tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
//					System.out.println("Extra---- " + index+ " checked!!! ");
            }
        });
    }

    @OnClick(R.id.top_btn_left)
    public void backOnClick(View view){
        finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onEvent(Object source, String eventArg) {
        if(eventArg.equalsIgnoreCase("collect")){

        }
    }
}
