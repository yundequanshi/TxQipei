package com.txsh.shop;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.ab.view.pullview.AbPullToRefreshView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.adapter.TxShopMainAdapter;
import com.txsh.model.TXShopListRes;
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
public class TxShopProductListAty extends BaseActivity {
    @ViewInject(R.id.shopproductlist_pull)
    public AbPullToRefreshView _pullToRefreshLv;

    @ViewInject(R.id.shopproductlist_grid)
    public GridView mGridView;
    private boolean mIsRefresh = true;
    private int nowPage = 1;
    TxShopMainAdapter mAdapter;


    public List<TXShopListRes.TXShopListData> mlShopCarData;
    private String companyId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tx_shopproduct_list);
        ViewUtils.inject(this);
        if (getIntentData()!=null) companyId= (String) getIntentData();
        initView();
        initShopList();
        initPullRefresh();

    }

    private void initView() {
        mAdapter = new TxShopMainAdapter(this,R.layout.tx_item_shop_main);
        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startAct(TxShopProductListAty.this, TXShopDetailAty.class,mlShopCarData.get(position));
            }
        });


    }
    private void initPullRefresh() {
        _pullToRefreshLv.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(AbPullToRefreshView view) {
                mIsRefresh = true;
                nowPage = 1;
                initShopList();
            }
        });

        _pullToRefreshLv.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
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
                initShopList();
            }

        });
    }

    @OnClick(R.id.home_top_back)
    public void backOnClick(View view){
        finish();
    }
    private void initShopList() {
        MLLogin user = ((BaseApplication) this.getApplication()).get_user();
        ZMRequestParams param = new ZMRequestParams();
        param.addParameter("companyId", companyId);
        param.addParameter("nowPage", String.valueOf(nowPage));
        param.addParameter("pageSize", "20");

        ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(ZMHttpType.RequestType.SHOPPRODUCTLIST, null, param,
                _handler, SHOPPRODUCTLISTRETURN, MLShopServices.getInstance());
        loadDataWithMessage( this, null, message1);
    }

    private static final int SHOPPRODUCTLISTRETURN = 1;

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
                case SHOPPRODUCTLISTRETURN: {
                    TXShopListRes ret = (TXShopListRes) msg.obj;
                    if (ret.state.equalsIgnoreCase("1")) {
                        if (mIsRefresh) {
                            //刷新
                            mlShopCarData=ret.datas;
                            _pullToRefreshLv.onHeaderRefreshFinish();
                        } else {
                            //加载更多
                            _pullToRefreshLv.onFooterLoadFinish();
                            if (mlShopCarData == null) {
                                return;
                            }
                            mlShopCarData.addAll(ret.datas);
                        }
                        //加载数据
                        if (mlShopCarData != null) {
                            mAdapter.setData(mlShopCarData);
                        }
                        if (mlShopCarData != null && mlShopCarData.size() < 20) {
                            _pullToRefreshLv.setLoadMoreEnable(false);
                        } else {
                            _pullToRefreshLv.setLoadMoreEnable(true);
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
