package com.txsh.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ab.view.pullview.AbPullToRefreshView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.adapter.TxIntegralConvertAdapter;
import com.txsh.model.TXIntegralShopConvertRes;
import com.txsh.services.MLShopServices;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLogin;

import java.util.List;

import cn.ml.base.widget.roundedimageview.RoundedImageView;

/**
 * 积分兑换记录
 * Created by Marcello on 2015/6/26.
 */
public class TXIntegralShopConvertAty extends BaseActivity {

    private TxIntegralConvertAdapter mAdapter;

    @ViewInject(R.id.convert_lv)
    private AbPullToRefreshView _pullToRefreshLv;
    @ViewInject(R.id.mListView)
    private ListView mList;

    @ViewInject(R.id.convert_tv_name)
    private TextView mTvName;
    @ViewInject(R.id.convert_tv_phone)
    private TextView mTvPhone;

    @ViewInject(R.id.convert_tv_count)
    private TextView mTvCount;
    @ViewInject(R.id.convert_iv_icon)
    private RoundedImageView mIvIcon;


    private MLLogin mUser;
    private TXIntegralShopConvertRes ret;
    public List<TXIntegralShopConvertRes.TXIntegralConvert> datas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tx_integral_convert);
        ViewUtils.inject(this);

        mUser = BaseApplication.getInstance().get_user();

        mAdapter = new TxIntegralConvertAdapter(TXIntegralShopConvertAty.this,R.layout.tx_item_integral_convert);
        mList.setAdapter(mAdapter);

        initData();
        _pullToRefreshLv.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {

            @Override
            public void onHeaderRefresh(AbPullToRefreshView view) {
                refreshData();
            }
        });

        _pullToRefreshLv.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {

            @Override
            public void onFooterLoad(AbPullToRefreshView view) {
                // TODO Auto-generated method stub
                pageData();
            }
        });
    }


    @OnClick(R.id.top_btn_left)
    public void backOnClick(View view){
        finish();
    }


    private void initData() {
        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("depotId",mUser.Id);

        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_INTERGRAL_SHOP_CONVERT, null, params,
                _handler,HTTP_RESPONSE , MLShopServices.getInstance());
        loadDataWithMessage(TXIntegralShopConvertAty.this, "正在加载，请稍等...", message2);
    }

    private void refreshData(){
        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("depotId",mUser.Id);

        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_INTERGRAL_SHOP_CONVERT, null, params, _handler,HTTP_RESPONSE , MLShopServices.getInstance());
        loadData(TXIntegralShopConvertAty.this, message2);
    }


    private void pageData() {
        String lastId = datas.get(datas.size()-1).id+"";
        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("depotId",mUser.Id);
        params.addParameter("lastId",lastId);

        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_INTERGRAL_SHOP_CONVERT, null, params, _handler,HTTP_RESPONSE_PAGE , MLShopServices.getInstance());
        loadData(TXIntegralShopConvertAty.this, message2);
    }

    private void review() {
        mTvCount.setText( ret.datas.integral+"");
        mTvName.setText(ret.datas.dName);
        mTvPhone.setText(ret.datas.phone);

        String imgUrl = APIConstants.API_IMAGE+"/?id="+ret.datas.dlogo;

        mIvIcon.setTag(imgUrl);
        if (!BaseApplication.IMAGE_CACHE.get(imgUrl, mIvIcon)) {
            mIvIcon.setImageResource(R.drawable.sgc_photo);
        }
    }

    private static final int HTTP_RESPONSE= 0;
    private static final int HTTP_RESPONSE_PAGE= 1;
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
                case HTTP_RESPONSE:{
                    ret = (TXIntegralShopConvertRes) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){
                        datas = ret.datas.list;
                        mAdapter.setData(ret.datas.list);
                        review();
                    }else{
                        showMessageError("初始化失败!");
                    }
                    _pullToRefreshLv.onHeaderRefreshFinish();
                    break;
                }

                case HTTP_RESPONSE_PAGE:{
                    TXIntegralShopConvertRes ret = (TXIntegralShopConvertRes) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){
                        datas.addAll(ret.datas.list);
                        mAdapter.setData(datas);
                    }else{
                        showMessageError("初始化失败!");
                    }
                    _pullToRefreshLv.onFooterLoadFinish();
                    break;
                }
                default:
                    break;
            }
        }
    };

}
