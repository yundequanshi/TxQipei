package com.txsh.home;

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
import com.zuomei.auxiliary.MLMyBill2ListFrg;
import com.zuomei.base.BaseActivity;
import com.zuomei.model.MLHomeBusinessData;

import java.util.ArrayList;
import java.util.List;

import cn.ml.base.utils.IEvent;

/**
 *
 * 上传发货单
 * Created by Marcello on 2015/6/12.
 */
public class TXInvoiceAty extends BaseActivity implements IEvent<String> {

    @ViewInject(R.id.detail_rg)
    private RadioGroup mGroup;

    private MLHomeBusinessData mData;
    private List<Fragment> fragments = new ArrayList<Fragment>();

    private   MLMyBill2ListFrg mFragBill;
    private  TXInvoiceAddFrag mFragAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tx_invoice_main);
        ViewUtils.inject(this);

        //上传发货单
        mFragAdd= new TXInvoiceAddFrag();
        //货单列表
        mFragBill = new MLMyBill2ListFrg();

        fragments.add(mFragAdd);
        fragments.add(mFragBill);



        FragmentTabAdapter tabAdapter = new FragmentTabAdapter(this, fragments,R.id.detail_fl_content, mGroup);
        tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {

            }
        });
    }

    @OnClick(R.id.top_btn_right)
    public void saveOnClick(View view){
        mFragAdd.okOnClick();
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
        if(eventArg.equalsIgnoreCase("update")){
            try{
                mFragBill.initData();
            }catch (Exception e){

            }
        }
    }
}
