package com.txsh.shop;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.services.MLShopServices;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLRegister;

import cn.ml.base.utils.MLStrUtil;

/**
 * Created by Administrator on 2015/7/23.
 */
public class TXShopPJChatAty extends BaseActivity {

    @ViewInject(R.id.shoppj_ed)
    public EditText edpingjia;
    private String id = "";
    private String type = "1";

    @ViewInject(R.id.shoppj_ck_bad)
    public CheckBox bad;

    @ViewInject(R.id.shoppj_ck_good)
    public CheckBox good;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tx_shoppj_aty);
        ViewUtils.inject(this);
        if (getIntentData()!=null) id= (String) getIntentData();
    }

    @OnClick(R.id.home_top_back)
    public void backOnClick(View view) {
        finish();
    }


    @OnClick(R.id.accident_btn_next)
    public void fabiaoOnClick(View view) {

        if (MLStrUtil.isEmpty(edpingjia.getText().toString())) {
            showMessage("评论不能为空");
            return;
        }
        showChat();
    }
    @OnClick({R.id.shoppj_rel_good, R.id.shoppj_rel_bad})
    public void relOnClick(View view) {
        switch (view.getId()) {
            case R.id.shoppj_rel_good:
                type = "1";
                good.setChecked(true);
                bad.setChecked(false);
                break;

            case R.id.shoppj_rel_bad:
                type = "0";
                bad.setChecked(true);
                good.setChecked(false);
                break;
        }
    }

    private void showChat() {

        MLLogin user = ((BaseApplication) this.getApplication()).get_user();
        ZMRequestParams param = new ZMRequestParams();
        param.addParameter("id", id);
        param.addParameter("type", type);
        param.addParameter("content", edpingjia.getText().toString());
        ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(ZMHttpType.RequestType.SHOPPINGJIACHAT, null, param,
                _handler, SHOPPINGJIACHATRETURN, MLShopServices.getInstance());
        loadDataWithMessage(this, "正在发表", message1);
    }

    private static final int SHOPPINGJIACHATRETURN = 1;

    private Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismissProgressDialog();
            if (msg == null || msg.obj == null) {
                showMessage(R.string.loading_data_failed);
                return;
            }
            if (msg.obj instanceof ZMHttpError) {
                ZMHttpError error = (ZMHttpError) msg.obj;
                showMessage(error.errorMessage);
                return;
            }
            switch (msg.what) {
                case SHOPPINGJIACHATRETURN: {
                    MLRegister ret = (MLRegister) msg.obj;
                    if (ret.state.equalsIgnoreCase("1")) {
                        showMessage("发表成功");
                        finish();

                    } else {
                        showMessage("发表失败");
                    }
                    break;
                }
            }
        }
    };
}
