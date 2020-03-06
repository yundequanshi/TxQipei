package com.qipei.found;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qipei.model.MLMapData;
import com.txsh.R;
import com.zuomei.base.BaseActivity;
import com.zuomei.model.MLAccidentInfo;
import com.zuomei.utils.MLToolUtil;

import java.util.List;

import cn.ml.base.utils.IEvent;
import cn.ml.base.utils.MLDialogUtils;
import cn.ml.base.widget.citypop.MLCityPop;

/**
 * 发布事故车-step2
 * Created by Marcello on 2015/6/6.
 */
public class MLAccidentAdd2Aty extends BaseActivity {


    @ViewInject(R.id.accident_rl_root)
    private RelativeLayout _root;

    /**车型名称*/
    @ViewInject(R.id.accident_tv_nice)
    private TextView _niceTv;

    /**上牌时间*/
    @ViewInject(R.id.accident_tv_platedata)
    private TextView _platedataTv;

    /**所在城市*/
    @ViewInject(R.id.accident_tv_city)
    private TextView _cityTv;

    /**行驶里程*/
    @ViewInject(R.id.accident_tv_mileage)
    private TextView _mileageTv;

    /**受损部位*/
    @ViewInject(R.id.accident_tv_damaged)
    private TextView _damagedTv;

    /**预售价格*/
    @ViewInject(R.id.accident_tv_price)
    private TextView _priceTv;

    /**买时裸车价*/
    @ViewInject(R.id.accident_tv_oldprice)
    private TextView _oldpriceTv;

    /**名字*/
    @ViewInject(R.id.accident_tv_title)
    private TextView _nameTv;

    /**排量*/
    @ViewInject(R.id.accident_tv_displacement)
    private TextView _displacementTv;

    /**电话*/
    @ViewInject(R.id.accident_tv_phone)
    private TextView _phoneTv;

    private MLAccidentInfo detail = new MLAccidentInfo();

    private List<String> mPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qp_accident_add2);
        ViewUtils.inject(this);
        mPath = (List<String>) getIntent().getSerializableExtra("data");

    }


    public void fillOnClick(View view){
        switch (view.getId()){
            case R.id.accident_tv_nice:{
            //车型名称
            startAct(MLAccidentAdd2Aty.this,MLAccidentEditAty.class,new MLMapData(1,_niceTv.getText().toString()),1);
                break;
            }

            case R.id.accident_tv_platedata:{
                //上牌时间
                MLDialogUtils.choiceTime(MLAccidentAdd2Aty.this, _platedataTv.getText().toString(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        _platedataTv.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                });

                break;
            }
            case R.id.accident_tv_city:{
                //城市
                MLCityPop menuWindow = new MLCityPop(MLAccidentAdd2Aty.this, new IEvent<String>() {
                    @Override
                    public void onEvent(Object source, String eventArg) {
                        _cityTv.setText(eventArg);
                    }
                });
                menuWindow.showAtLocation(((ViewGroup) (findViewById(android.R.id.content))).getChildAt(0), Gravity.CENTER, 0, 0);
                break;
            }
            case R.id.accident_tv_mileage:{
                //行驶里程
                startAct(MLAccidentAdd2Aty.this, MLAccidentEditAty.class, new MLMapData(2,_mileageTv.getText().toString()), 1);
                break;
            }
            case R.id.accident_tv_damaged:{
                //受损部位
                startAct(MLAccidentAdd2Aty.this,MLAccidentEditAty.class,new MLMapData(3,_damagedTv.getText().toString()),1);
                break;
            }
            case R.id.accident_tv_oldprice:{
                //买时裸车价
                startAct(MLAccidentAdd2Aty.this,MLAccidentEditAty.class,new MLMapData(4,_oldpriceTv.getText().toString()),1);
                break;
            }
            case R.id.accident_tv_displacement:{
                //排量
                startAct(MLAccidentAdd2Aty.this,MLAccidentEditAty.class,new MLMapData(5,_displacementTv.getText().toString()),1);
                break;
            }
            case R.id.accident_tv_price:{
                //预售价格
                startAct(MLAccidentAdd2Aty.this,MLAccidentEditAty.class,new MLMapData(6,_priceTv.getText().toString()),1);
                break;
            }
            case R.id.accident_tv_title:{
                //姓名
                startAct(MLAccidentAdd2Aty.this,MLAccidentEditAty.class,new MLMapData(7,_nameTv.getText().toString()),1);
                break;
            }
            case R.id.accident_tv_phone:{
                //电话
                startAct(MLAccidentAdd2Aty.this,MLAccidentEditAty.class,new MLMapData(8,_phoneTv.getText().toString()),1);
                break;
            }

        }

    }

    @OnClick(R.id.top_btn_left)
    public void backOnClick(View view){
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1&&resultCode==1){
            //车型名称
            String name = data.getStringExtra("data");
            _niceTv.setText(name);
            detail.accidentName = name;
            return;
        }
        if(requestCode==1&&resultCode==2){
            //行驶里程
            String content = data.getStringExtra("data");
            _mileageTv.setText(content+" 公里");
            detail.mileage = content;
            return;
        }

        if(requestCode==1&&resultCode==3){
            //受损部位
            String content = data.getStringExtra("data");
            _damagedTv.setText(content);
            detail.damaged = content;
            return;
        }
        if(requestCode==1&&resultCode==4){
            //买时裸车价
            String content = data.getStringExtra("data");
            _oldpriceTv.setText(content+" 万");
            detail.oldPrice = content;
            return;
        }

        if(requestCode==1&&resultCode==5){
            //排量
            String content = data.getStringExtra("data");
            _displacementTv.setText(content+" L");
            detail.displacement = content;
            return;
        }
        if(requestCode==1&&resultCode==6){
            //预售价格
            String content = data.getStringExtra("data");
            _priceTv.setText(content+" 万");
            detail.price = content;
            return;
        }
        if(requestCode==1&&resultCode==7){
            //姓名
            String content = data.getStringExtra("data");
            _nameTv.setText(content);
            detail.masterName = content;
            return;
        }
        if(requestCode==1&&resultCode==8){
            //电话
            String content = data.getStringExtra("data");
            _phoneTv.setText(content);
            detail.masterPhone = content;
            return;
        }

    }




    @OnClick(R.id.accident_btn_next)
    public void nextOnClick(View view){

        String accidentName = _niceTv.getText().toString();
        String mtime = _platedataTv.getText().toString();
        String city = _cityTv.getText().toString();
        String mileage = _mileageTv.getText().toString();
        String damaged = _damagedTv.getText().toString();
        String oldPrice = _oldpriceTv.getText().toString();
        String displacement = _displacementTv.getText().toString();
        String price = _priceTv.getText().toString();
        String masterName = _nameTv.getText().toString();
        String masterPhone = _phoneTv.getText().toString();

        if(MLToolUtil.isNull(accidentName)){
            showMessage("车型名称不能为空!");
            return;
        }
        if(MLToolUtil.isNull(mtime)){
            showMessage("上牌时间不能为空!");
            return;
        }
        if(MLToolUtil.isNull(city)){
            showMessage("所在城市不能为空!");
            return;
        }
        if(MLToolUtil.isNull(mileage)){
            showMessage("行驶里程不能为空!");
            return;
        }
        mileage = mileage.replace("公里", "");
        if(MLToolUtil.isNull(damaged)){
            showMessage("受损部位不能为空!");
            return;
        }
        if(MLToolUtil.isNull(oldPrice)){
            showMessage("买时裸车价不能为空!");
            return;
        }
        oldPrice = oldPrice.replace("万", "");
        if(MLToolUtil.isNull(displacement)){
            showMessage("排量不能为空!");
            return;
        }
        displacement = displacement.replace("L", "");
        if(MLToolUtil.isNull(price)){
            showMessage("预售价格不能为空!");
            return;
        }
        price = price.replace("万", "");
        if(MLToolUtil.isNull(masterName)){
            showMessage("姓名不能为空!");
            return;
        }

        if(MLToolUtil.isNull(masterPhone)){
            showMessage("电话不能为空!");
            return;
        }

        MLAccidentInfo detail = new MLAccidentInfo();
        detail.accidentName = accidentName;
        detail.city = city;
        detail.mileage = mileage;
        detail.damaged = damaged;
        detail.oldPrice = oldPrice;
        detail.price = price;
        detail.masterName= masterName;
        detail.masterPhone = masterPhone;
        detail.displacement = displacement;
        detail.paths = mPath;
        detail.platedata = mtime;


        startAct(MLAccidentAdd2Aty.this,MLAccidentAdd3Aty.class,detail);
        finish();
    }
}
