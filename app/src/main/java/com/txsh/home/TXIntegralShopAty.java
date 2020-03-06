package com.txsh.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.view.pullview.AbPullToRefreshView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.adapter.TxIntegralShopAdapter;
import com.txsh.model.TXIntegralShopRes;
import com.txsh.model.TXIntergalProduct;
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

import cn.ml.base.widget.sample.MLScrollGridView;

/**
 *积分商城
 * Created by Marcello on 2015/6/25.
 */
public class TXIntegralShopAty extends BaseActivity {

    private TxIntegralShopAdapter mAdapter;

    @ViewInject(R.id.refresh_layout)
    private AbPullToRefreshView _pullToRefreshLv;
    @ViewInject(R.id.shop_grid)
    private MLScrollGridView mGridView;
    @ViewInject(R.id.shop_tv_jf)
    private TextView mTvJf;

    @ViewInject(R.id.shop_iv)
    private ImageView mIv;

    private MLLogin mUser;

    public List<TXIntegralShopRes.TXIntegralProduct> datas;

    private TXIntegralShopRes ret;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tx_integral_shop_main);
        ViewUtils.inject(this);

        mAdapter = new TxIntegralShopAdapter(TXIntegralShopAty.this,R.layout.tx_item_integral_shop);
        mGridView.setAdapter(mAdapter);

        mUser = BaseApplication.getInstance().get_user();
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

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TXIntergalProduct product = new TXIntergalProduct();
                product.id = datas.get(position).id;
                product.money = ret.datas.integral;
                startAct(TXIntegralShopAty.this, TXIntegralShopDetailAty.class, product, 1);
            }
        });


        mIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TXIntergalProduct product = new TXIntergalProduct();
                product.id = ret.datas.groomProductId;
                product.money = ret.datas.integral;
                startAct(TXIntegralShopAty.this,TXIntegralShopDetailAty.class,product);
            }
        });
    }

    private void initData() {
        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("depotId",mUser.Id);

        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_INTERGRAL_SHOP, null, params, _handler,HTTP_RESPONSE , MLShopServices.getInstance());
        loadDataWithMessage(TXIntegralShopAty.this, "正在加载，请稍等...", message2);
    }
    private void refreshData() {
        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("depotId",mUser.Id);

        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_INTERGRAL_SHOP, null, params, _handler,HTTP_RESPONSE , MLShopServices.getInstance());
        loadData(TXIntegralShopAty.this, message2);
    }




    private void pageData() {
        if(datas==null||datas.get(datas.size()-1).id==null){
            _pullToRefreshLv.onFooterLoadFinish();
            return;
        }
        String lastId = datas.get(datas.size()-1).id+"";
        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("depotId",mUser.Id);
        params.addParameter("lastId",lastId);

        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_INTERGRAL_SHOP, null, params, _handler,HTTP_RESPONSE_PAGE , MLShopServices.getInstance());
        loadData(TXIntegralShopAty.this, message2);
    }

    private void review() {
        mTvJf.setText(Html.fromHtml(String.format("我的积分：<font color=\"#0c71d7\">%s</font>", ret.datas.integral + "")));
        String imgUrl = APIConstants.API_IMAGE_SHOW+ret.datas.groomImage;

        mIv.setTag(imgUrl);
        if (!BaseApplication.IMAGE_CACHE.get(imgUrl, mIv)) {
            mIv.setImageResource(R.drawable.sgc_photo);
        }
    }


    /**
     * 规则
     * @param view
     */
    @OnClick(R.id.integral_btn_rule)
    public void ruleOnClick(View view){
        startAct(TXIntegralShopAty.this,TXIntegralShopRuleAty.class );
    }


    /**
     * 记录
     * @param view
     */
    @OnClick(R.id.integral_btn_convert)
    public void convertOnClick(View view){
        startAct(TXIntegralShopAty.this,TXIntegralShopConvertAty.class );
    }




    @OnClick(R.id.top_btn_left)
    public void backOnClick(View view){
        finish();
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
                    ret = (TXIntegralShopRes) msg.obj;
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
                    TXIntegralShopRes ret = (TXIntegralShopRes) msg.obj;
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            initData();
        }
    }
}
