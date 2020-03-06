package com.qipei.found;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qipei.model.MLMapData;
import com.txsh.R;
import com.zuomei.base.BaseActivity;
import com.zuomei.constants.MLConstants;
import com.zuomei.login.MLLoginCityPop;
import com.zuomei.model.MLAccidentInfo;
import com.zuomei.model.MLHomeCatalogData;
import com.zuomei.model.MLLeaveDetail;
import com.zuomei.utils.MLToolUtil;

import java.util.List;

import cn.ml.base.utils.IEvent;
import cn.ml.base.widget.citypop.MLCityPop;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 发布事故件-step2
 * Created by Marcello on 2015/6/6.
 */
public class MLIncidentAdd2Aty extends BaseActivity {

    @ViewInject(R.id.accident_rl_root)
    private RelativeLayout _root;

    /**产品名称*/
    @ViewInject(R.id.leave_tv_nice)
    private TextView _niceTv;

    /**车辆类型*/
    @ViewInject(R.id.leave_tv_carType)
    private TextView _carTypeTv;

    /**车辆子类型*/
    @ViewInject(R.id.leave_tv_child)
    private TextView _childTv;

    /**品质*/
    @ViewInject(R.id.leave_tv_quality)
    private TextView _qualityTv;

    /**所在城市*/
    @ViewInject(R.id.accident_tv_city)
    private TextView _cityTv;


    /**排量*/
    @ViewInject(R.id.leave_tv_displacement)
    private TextView _displacementTv;


    /**原价*/
    @ViewInject(R.id.leave_tv_oldprice)
    private TextView _oldpriceTv;

    /**现价*/
    @ViewInject(R.id.leave_tv_price)
    private TextView _priceTv;

    /**名字*/
    @ViewInject(R.id.leave_tv_name)
    private TextView _nameTv;

    /**电话*/
    @ViewInject(R.id.leave_tv_phone)
    private TextView _phoneTv;

    private MLAccidentInfo detail = new MLAccidentInfo();

    private List<String> mPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.qp_incident_add2);
        ViewUtils.inject(this);
        mPath = (List<String>) getIntent().getSerializableExtra("data");

    }


    public void fillOnClick(View view){
        switch (view.getId()){
            case R.id.leave_tv_nice:{
            //车型名称
            startAct(MLIncidentAdd2Aty.this,MLIncidentEditAty.class,new MLMapData(1,_niceTv.getText().toString()),1);
                break;
            }
            case R.id.accident_tv_city:{
                //城市
                MLCityPop menuWindow = new MLCityPop(MLIncidentAdd2Aty.this, new IEvent<String>() {
                    @Override
                    public void onEvent(Object source, String eventArg) {
                        _cityTv.setText(eventArg);
                    }
                });
                menuWindow.showAtLocation(((ViewGroup) (findViewById(android.R.id.content))).getChildAt(0), Gravity.CENTER, 0, 0);
                break;
            }
            case R.id.leave_tv_child:{
                //子车型
                startAct(MLIncidentAdd2Aty.this, MLIncidentEditAty.class, new MLMapData(2, _childTv.getText().toString()), 1);
                break;
            }
            case R.id.leave_tv_displacement:{
                //排量
                startAct(MLIncidentAdd2Aty.this,MLIncidentEditAty.class,new MLMapData(3,_displacementTv.getText().toString().replace("L","")),1);
                break;
            }
            case R.id.leave_tv_oldprice:{
                //原价
                startAct(MLIncidentAdd2Aty.this,MLIncidentEditAty.class,new MLMapData(4,_oldpriceTv.getText().toString().replace("元","")),1);
                break;
            }
            case R.id.leave_tv_price:{
                //现价
                startAct(MLIncidentAdd2Aty.this,MLIncidentEditAty.class,new MLMapData(5,_priceTv.getText().toString().replace("元","")),1);
                break;
            }
            case R.id.leave_tv_name:{
                //姓名
                startAct(MLIncidentAdd2Aty.this,MLIncidentEditAty.class,new MLMapData(6,_nameTv.getText().toString()),1);
                break;
            }
            case R.id.leave_tv_phone:{
                //电话
                startAct(MLIncidentAdd2Aty.this,MLIncidentEditAty.class,new MLMapData(7,_phoneTv.getText().toString()),1);
                break;
            }

        }

    }


    @OnClick(R.id.leave_tv_quality)
    public void qualityOnClick(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(MLIncidentAdd2Aty.this, AlertDialog.THEME_HOLO_LIGHT);
        String s[] = {"正品","副品","高仿"};
        builder.setItems(s, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    _qualityTv.setText("正品");
                } else if (which == 1) {
                    _qualityTv.setText("副品");
                } else {
                    _qualityTv.setText("高仿");
                }
            }
        });
        builder.setTitle("选择品质");
        builder.show();
    }


    @OnClick(R.id.accident_rl_city)
    public void cityOnClick(View view){
        MLLoginCityPop menuWindow = new MLLoginCityPop(MLIncidentAdd2Aty.this, new IEvent<String>() {
            @Override
            public void onEvent(Object source, String eventArg) {
                _cityTv.setText(eventArg);
            }
        });
        menuWindow.showAtLocation(_root, Gravity.CENTER, 0, 0);
    }

    @OnClick(R.id.leave_tv_carType)
    public void carOnClick(View view){
        toActivity(MLIncidentAdd2Aty.this, MLConstants.MY_PART_CAR, null);
    }


    @OnClick(R.id.top_btn_left)
    public void backOnClick(View view){
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1&&resultCode==1){
            //产品名称
            String name = data.getStringExtra("data");
            _niceTv.setText(name);
            return;
        }
        if(requestCode==1&&resultCode==2){
            //车辆子车型
            String content = data.getStringExtra("data");
            _childTv.setText(content);
            return;
        }

        if(requestCode==1&&resultCode==3){
            //排量
            String content = data.getStringExtra("data");
            _displacementTv.setText(content+" L");
            return;
        }
        if(requestCode==1&&resultCode==4){
            //原价
            String content = data.getStringExtra("data");
            _oldpriceTv.setText(content+" 元");
            return;
        }

        if(requestCode==1&&resultCode==5){
            //现价
            String content = data.getStringExtra("data");
            _priceTv.setText(content+" 元");
            return;
        }
        if(requestCode==1&&resultCode==6){
            //姓名
            String content = data.getStringExtra("data");
            _nameTv.setText(content);
            return;
        }
        if(requestCode==1&&resultCode==7){
            //电话
            String content = data.getStringExtra("data");
            _phoneTv.setText(content);
            return;
        }
    }




    @OnClick(R.id.leave_btn_next)
    public void nextOnClick(View view){

        String nice = _niceTv.getText().toString();
        String type = _carTypeTv.getText().toString();
        String child = _childTv.getText().toString();
        String quality = _qualityTv.getText().toString();
        String city = _cityTv.getText().toString();
        String displacement = _displacementTv.getText().toString();

        String oldPrice = _oldpriceTv.getText().toString();
        String price = _priceTv.getText().toString();

        String masterName = _nameTv.getText().toString();
        String masterPhone = _phoneTv.getText().toString();

        if(MLToolUtil.isNull(nice)){
            showMessage("产品名称不能为空!");
            return;
        }

        if(MLToolUtil.isNull(type)){
            showMessage("车辆类型不能为空!");
            return;
        }

        if(MLToolUtil.isNull(child)){
            showMessage("车辆子类型不能为空!");
            return;
        }
        if(MLToolUtil.isNull(quality)){
            showMessage("品质不能为空!");
            return;
        }

        if(MLToolUtil.isNull(city)){
            showMessage("所在城市不能为空!");
            return;
        }
        displacement = displacement.replace("L", "");
        if(MLToolUtil.isNull(displacement)){
            showMessage("排量不能为空!");
            return;
        }

        oldPrice = oldPrice.replace("元", "");
        if(MLToolUtil.isNull(oldPrice)){
            showMessage("原价不能为空!");
            return;
        }
        price = price.replace("元", "");
        if(MLToolUtil.isNull(price)){
            showMessage("现价不能为空!");
            return;
        }
        if(MLToolUtil.isNull(masterName)){
            showMessage("姓名不能为空!");
            return;
        }

        if(MLToolUtil.isNull(masterPhone)){
            showMessage("电话不能为空!");
            return;
        }

        MLLeaveDetail detail = new MLLeaveDetail();
        detail.nice = nice;
        detail.type = type;
        detail.child = child;
        detail.quality = quality;
        detail.city = city;
        detail.displacement = displacement;
        detail.price = price;
        detail.oldPrice =oldPrice;
        detail.masterName = masterName;
        detail.masterPhone = masterPhone;
        detail.paths = mPath;

        startAct(MLIncidentAdd2Aty.this,MLIncidentAdd3Aty.class,detail);
        finish();
    }

    private MLHomeCatalogData mCatalogData;
    @Subscribe
    public void setCarInfo(MLHomeCatalogData data) {
        if(data==null) return;
        mCatalogData = data;
        _carTypeTv.setText(data.name);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
