package com.txsh.shop;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.ab.view.pullview.AbPullToRefreshView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.adapter.TXShopPJAdapter;
import com.txsh.model.TXShopCarData;
import com.txsh.model.TXShopPjData;
import com.txsh.services.MLShopServices;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLogin;

import java.util.List;


/**
 * Created by Administrator on 2015/7/23.
 */
public class TXShopPJAty extends BaseActivity {
    @ViewInject(R.id.tx_shop_pj_pullview)
    public AbPullToRefreshView mPullRefreshView;

    @ViewInject(R.id.tx_shop_pj_lv)
    public ListView txshoppjlv;

    private boolean mIsRefresh = true;
    private int nowPage = 1;
    private String pageSize = "";
    List<TXShopPjData.TXShopPjDataDetail> mlShopCarData;

    TXShopPJAdapter txShopPJAdapter;
    public String productId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tx_shop_pj_list);
        ViewUtils.inject(this);
        if (getIntentData()!=null)productId= (String) getIntentData();
        initList();
        initPullRefresh();
        initShopPj();
    }

    private void initList() {
        txShopPJAdapter=new TXShopPJAdapter(this,R.layout.tx_item_shop_pj);
        txshoppjlv.setAdapter(txShopPJAdapter);


    }

    private void initPullRefresh() {
        mPullRefreshView.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(AbPullToRefreshView view) {
                mIsRefresh = true;
                nowPage =1;
                initShopPj();
            }
        });

        mPullRefreshView.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
            @Override
            public void onFooterLoad(AbPullToRefreshView view) {
                // TODO Auto-generated method stub
                mIsRefresh = false;
                int size = mlShopCarData.size() - 1;
                if (size <= 0) {
                    return;
                }
                //lastrow = mlShopCarData.get(mlShopCarData.size() - 1).rowno;
                nowPage++;
                initShopPj();
            }

        });
    }
    @OnClick(R.id.home_top_back)
    public void backOnClick(View view){
        finish();
    }
    private void initShopPj() {
        MLLogin user = ((BaseApplication) this.getApplication()).get_user();
        ZMRequestParams param = new ZMRequestParams();
        param.addParameter("productId", productId);
        param.addParameter("nowPage", String.valueOf(nowPage));
        param.addParameter("pageSize", "20");

        ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(ZMHttpType.RequestType.SHOPPRODUCTPJ, null, param,
                _handler, SHOPPRODUCTPJRETURN, MLShopServices.getInstance());
        loadDataWithMessage(TXShopPJAty.this, null, message1);
    }


    private static final int SHOPPRODUCTPJRETURN = 1;

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
                case SHOPPRODUCTPJRETURN: {
                    TXShopPjData ret = (TXShopPjData) msg.obj;
                    if (ret.state.equalsIgnoreCase("1")) {
                        if (mIsRefresh) {
                            //刷新
                            mlShopCarData=ret.datas;
                            mPullRefreshView.onHeaderRefreshFinish();
                        } else {
                            //加载更多
                            mPullRefreshView.onFooterLoadFinish();
                            if (mlShopCarData == null) {
                                return;
                            }
                            mlShopCarData.addAll(ret.datas);
                        }
                        //加载数据
                        if (mlShopCarData != null) {
                            txShopPJAdapter.setData(mlShopCarData);
                        }
                        if (mlShopCarData != null && mlShopCarData.size() < 20) {
                            mPullRefreshView.setLoadMoreEnable(false);
                        } else {
                            mPullRefreshView.setLoadMoreEnable(true);
                        }

                    } else {
                        showMessage("加载数据失败");
                        break;
                    }
                    break;

                }
            }
        }
        };
}
