package com.qipei.found;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLeaveDetail;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLLeaveServices;
import com.zuomei.utils.MLToolUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marcello on 2015/6/9.
 */
public class MLIncidentAdd3Aty extends BaseActivity {

    private static MLLeaveDetail detail;
    private String _content;

    @ViewInject(R.id.accident_et_content)
    private EditText _contentEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qp_accident_add3);
        ViewUtils.inject(this);
        detail = (MLLeaveDetail) getIntentData();
    }

    private String imageId="";
    @OnClick(R.id.accident_btn_next)
    public void nextOnClick(View view) {
        _content = _contentEt.getText().toString();
        if (MLToolUtil.isNull(_content)) {
            showMessage("描述不能为空!");
            return;
        }
        requestAccident();
    }

    private void requestAccident(){
        MLLogin user = BaseApplication.getInstance().get_user();
        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("depotUser.id",user.Id);
        params.addParameter("carType",detail.type);
        params.addParameter("childType",detail.child);
        params.addParameter("exhaust",detail.displacement);
        params.addParameter("city.id",BaseApplication._currentCity);
        params.addParameter("cityName",detail.city);
        params.addParameter("name",detail.nice);
        params.addParameter("introduction",_content);
        params.addParameter("quality",detail.quality);
        params.addParameter("mobile",detail.masterPhone);
        params.addParameter("user",detail.masterName);
        params.addParameter("originalCost",detail.oldPrice);
        params.addParameter("currentCost",detail.price);



        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.LEAVE_ADD, null, params, _handler,HTTP_RESPONSE_ACCIDENT_ADD , MLLeaveServices.getInstance());

        Map<String, Object> otherParam = new HashMap<String, Object>();
        otherParam.put("image", detail.paths);
        message2.setOtherParmas(otherParam);
        loadDataWithMessage(MLIncidentAdd3Aty.this, "正在发布...", message2);
    }

    private static final int HTTP_RESPONSE_ACCIDENT_ADD= 0;
    private Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // dismissProgressDialog();
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
                //事故车添加
                case HTTP_RESPONSE_ACCIDENT_ADD:{
                    MLRegister ret = (MLRegister) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){
                        showMessageSuccess("事故件发布成功!");
                        startAct(MLIncidentAdd3Aty.this, MLIncidentAty.class);
                        finish();
                    }else{
                        showMessageError("事故件发布失败!");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
}
