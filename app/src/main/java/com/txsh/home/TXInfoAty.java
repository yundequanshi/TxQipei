package com.txsh.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.adapter.TXInfoAdapter;
import com.txsh.model.TXInfoRes;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.services.MLHomeServices;

import java.util.List;

/**
 * Created by Marcello on 2015/6/20.
 */
public class TXInfoAty extends BaseActivity {

    @ViewInject(R.id.mListView)
    private ListView mList;

    private TXInfoAdapter mAdapter;

    public List<TXInfoRes.TXInfoData> datas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tx_info_list);
        ViewUtils.inject(this);

        mAdapter = new TXInfoAdapter(TXInfoAty.this,R.layout.tx_item_info);
        mList.setAdapter(mAdapter);

        initData();

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = APIConstants.API_DEFAULT_HOST+"/mobile3/news/detail?id="+datas.get(position).id;

                toActivity(TXInfoAty.this, MLConstants.TX_INFO_DETAIL,url);
            }
        });
    }

    private void initData() {
        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("cityId", BaseApplication._currentCity);
        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.TX_INFO_LIST, null, params, _handler
                ,HTTP_RESPONSE_BUSINESS , MLHomeServices.getInstance());
        // loadDataWithMessage(_context, null, message2);
        loadDataWithMessage(TXInfoAty.this,"正在加载，请稍等...",message2);
    }

    @OnClick(R.id.top_btn_left)
    public void backOnClick(View view){
        finish();
    }

    private static final int HTTP_RESPONSE_BUSINESS = 2;
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
                case HTTP_RESPONSE_BUSINESS:{
                    TXInfoRes ret = (TXInfoRes) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){
                        datas = ret.datas;
                        mAdapter.setData(ret.datas);
                    }else{
                        showMessage("获取商家失败!");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
}
