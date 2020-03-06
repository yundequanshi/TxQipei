package com.qipei.found;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ab.view.sliding.AbSlidingPlayView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;
import com.zuomei.home.MLHomeProductPop;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLeaveData;
import com.zuomei.model.MLLeaveDetailData;
import com.zuomei.model.MLLeaveDetailResponse;
import com.zuomei.services.MLLeaveServices;

import java.util.ArrayList;
import java.util.List;

import cn.ml.base.utils.MLDialogUtils;

/**
 * 事故件详情
 * Created by Marcello on 2015/6/6.
 */
public class MLIncidentDetailAty extends BaseActivity {


    public static MLLeaveData data;
    @ViewInject(R.id.accident_playView)
    private AbSlidingPlayView _playView;
    @ViewInject(R.id.accident_scrollview)
    private ScrollView _scrollview;
    @ViewInject(R.id.root)
    private RelativeLayout _root;


    @ViewInject(R.id.accident_tv_title)
    private TextView _titleTv;

    /** 行驶里程*/
    @ViewInject(R.id.accident_tv_mileage)
    private TextView _mileageTv;

    /** 上牌日期*/
    @ViewInject(R.id.accident_tv_platedata)
    private TextView _platedataTv;


    /** 车牌所在地*/
    @ViewInject(R.id.accident_tv_address)
    private TextView _addressTv;

    /** 排量*/
    @ViewInject(R.id.accident_tv_displacement)
    private TextView _displacementTv;

    /** 受损部位*/
    @ViewInject(R.id.accident_tv_damaged)
    private TextView _damagedTv;

    /** 发布时间*/
    @ViewInject(R.id.accident_tv_addtime)
    private TextView _addtimeTv;

    /** 价格*/
    @ViewInject(R.id.accident_tv_price)
    private TextView _priceTv;

    /** 买时裸车价*/
    @ViewInject(R.id.accident_tv_oldprice)
    private TextView _oldpriceTv;

    /** 车主描述*/
    @ViewInject(R.id.accident_tv_detail)
    private TextView _detailTv;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qp_incident_detail);
        ViewUtils.inject(this);


        data = (MLLeaveData) getIntentData();

        initData();
        }

    private void initData() {
        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("id",data.info.id);

        ZMHttpRequestMessage message = new ZMHttpRequestMessage(ZMHttpType.RequestType.LEAVE_DEATAIL, null, params, _handler, HTTP_RESPONSE_ACCIDENT_DETIAL, MLLeaveServices.getInstance());
        loadDataWithMessage(MLIncidentDetailAty.this, null, message);
    }



    private void initPlayView(String image) {

        final String imageId [] = image.split(",");
        for(String id : imageId){
            ImageView iv = getImageView();
            _playView.addView(iv);
            String imgUrl = APIConstants.API_IMAGE+"?id="+id;
            //	 bitmapUtils.display(iv, imgUrl, bigPicDisplayConfig);
            BaseApplication.IMAGE_CACHE.get(imgUrl, iv);
        }
        _playView.setOnTouchListener(new AbSlidingPlayView.AbOnTouchListener() {
            @Override
            public void onTouch(MotionEvent event) {
                _scrollview.requestDisallowInterceptTouchEvent(true);
            }
        });
        _playView.setOnItemClickListener(new AbSlidingPlayView.AbOnItemClickListener() {

            @Override
            public void onClick(int position) {
			/*	String path = APIConstants.API_IMAGE+"?id="+imageId[position];
				MLMessagePhotoPop _pop = new MLMessagePhotoPop(getActivity(), path);
				_pop.showAtLocation(_root, Gravity.CENTER, 0, 0);   */
                MLHomeProductPop _pop = new MLHomeProductPop(MLIncidentDetailAty.this, getHeadImageUrl(), position);
                _pop.showAtLocation(_root, Gravity.CENTER, 0, 0);
            }
        });
    }
    public ImageView getImageView(){
        ImageView image = new ImageView(MLIncidentDetailAty.this);
        image.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        //	image.setImageResource(id);
        //	image.setBackgroundColor(getResources().getColor(R.color.common_red));
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return image;
    }



    private List<String> getHeadImageUrl(){
        List<String> images = new ArrayList<String>();

        final String imageId [] = _data.images.split(",");
        for(String id : imageId){
            String imgUrl = APIConstants.API_IMAGE+"?id="+id;
            images.add(imgUrl);
        }

        return images;
    }


    private MLLeaveDetailData _data;
    private void review(MLLeaveDetailData data){
        _data = data;
        initPlayView(data.images);
     /*   String time = MLStringUtils.time_year(data.MCtime);
       */
        _titleTv.setText(data.name);
        _addtimeTv.setVisibility(View.GONE);
        //_damagedTv.setText("受损部位：" + data.damaged);
        _damagedTv.setVisibility(View.GONE);


        _mileageTv.setText("行驶里程："+"公里");
        _oldpriceTv.setText("购买价格：" + _data.originalCost + "万");

        String price = String.format("<font color=\"#ff0000\">%s</font> 万 (不含过户费)",_data.currentCost+"");
        _priceTv.setText(Html.fromHtml(price));

        _addressTv.setText("所在城市：" + data.cityName);
        _displacementTv.setText("排量：" + data.exhaust + "L");

        _platedataTv.setText("品质：" + data.quality);
        _detailTv.setText("车主描述：" + data.introduction);


        initTextView(_mileageTv,5);
        initTextView(_platedataTv,5);
        initTextView(_oldpriceTv,5);
        initTextView(_addressTv,5);
        initTextView(_displacementTv,3);
        initTextView(_damagedTv,5);

    }


    private void initTextView(TextView tv,int index) {
        String text  = tv.getText().toString();
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.BLACK);
        builder.setSpan(redSpan, index, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(builder);
    }


    @OnClick(R.id.accident_tv_phone)
    public void callOnClick(View view){
        if(_data==null)return;

        AlertDialog.Builder builder = MLDialogUtils.getAlertDialog(MLIncidentDetailAty.this);
        builder.setMessage("拨打"+_data.mobile);
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + _data.mobile));
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }


    @OnClick(R.id.top_btn_left)
    public void backOnClick(View view){
        finish();
    }

    private static final int HTTP_RESPONSE_ACCIDENT_DETIAL = 0;
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
                case HTTP_RESPONSE_ACCIDENT_DETIAL:
                    MLLeaveDetailResponse ret = (MLLeaveDetailResponse) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")&&ret.datas!=null){
                        review(ret.datas);
                    }else{
                        showMessage("获取事故车详情失败!");
                    }
                    break;

                default:
                    break;
            }
        }
    };
}
