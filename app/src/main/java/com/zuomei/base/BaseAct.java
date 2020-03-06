package com.zuomei.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;

import com.baichang.android.utils.BCAppManager;
import com.baichang.android.utils.BCDialogUtil;
import com.baichang.android.utils.BCToastUtil;
import com.baichang.android.utils.BCToolsUtil;

public class BaseAct extends FragmentActivity {

    protected static Context mBaseContext;

    private Object mIntentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        // 设置标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        if (!this.isTaskRoot()) { //判断该Activity是不是任务空间的源Activity，“非”也就是说是被系统重新实例化出来
            //如果你就放在launcher Activity中话，这里可以直接return了
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;//finish()之后该活动会继续执行后面的代码，你可以logCat验证，加return避免可能的exception
            }
        }

        mBaseContext = this;
        initIntentData();
        BCAppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (mBaseContext != null) {
            BCToolsUtil.closeSoftInput(mBaseContext);
        }
        BCDialogUtil.dismissProgressDialog();
    }

    //环信使用
    public void back(View view) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBaseContext != null) {
            BCToolsUtil.closeSoftInput(mBaseContext);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BCAppManager.getAppManager().finishActivity(this);
    }

    // 获取传递值
    private void initIntentData() {
        mIntentData = getIntent().getSerializableExtra(
                MLBaseConstants.TAG_INTENT_DATA);
    }

    public Object getIntentData() {
        return mIntentData;
    }

    // ==========Toast 提示=========================
    public void showMessage(Context contex, Object obj) {
        BCToastUtil.showMessage(contex, obj);
    }

    // ==========跳转页面=======================
    protected void startAct(Activity act, Class cls) {
        startAct(act, cls, null, -1);
    }

    protected void startAct(Activity act, Class cls, Object obj) {
        startAct(act, cls, obj, -1);
    }

    protected void startAct(Activity act, Class cls, Object obj, int requestCode) {
        BCAppManager.startActivity(act, cls, obj, requestCode);
    }

    protected void startAct2(Activity act, Class cls) {
        startAct2(act, cls, null, -1);
    }

    protected void startAct2(Activity act, Class cls, Object obj) {
        startAct2(act, cls, obj, -1);
    }

    protected void startAct2(Activity act, Class cls, Object obj, int requestCode) {
        BCAppManager.startActivity2(act, cls, obj, requestCode);
    }

    protected Activity getAty() {
        return this;
    }
}
