package com.qipei.found;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.ab.view.pullview.AbPullToRefreshView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qipei.adapter.MLAccidentAdapter;
import com.txsh.R;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLAccidentDetailData;
import com.zuomei.model.MLAccidentListResponse;
import com.zuomei.services.MLAccidentServices;
import com.zuomei.utils.MLToolUtil;

import java.util.List;

/**
 * Created by Marcello on 2015/6/5.
 */
public class MLAccidentAty extends BaseActivity  {

    @ViewInject(R.id.part_lv)
    private AbPullToRefreshView _pullToRefreshLv;

    @ViewInject(R.id.accident_grid)
    private GridView mGrid;

    private MLAccidentAdapter mAdapter;


    public List<MLAccidentDetailData> _accidentDatas;
@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qp_accident_list);
        ViewUtils.inject(this);

    initView();
    initData();

    }

    private void initView() {
        mAdapter = new MLAccidentAdapter(MLAccidentAty.this,R.layout.item_accident);
        mGrid.setAdapter(mAdapter);
        _pullToRefreshLv.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(AbPullToRefreshView view) {
                initData();
            }
        });
        _pullToRefreshLv.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
            @Override
            public void onFooterLoad(AbPullToRefreshView view) {
                pageData();
            }
        });


        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MLAccidentDetailData data = (MLAccidentDetailData) parent.getAdapter().getItem(position);
                startAct(MLAccidentAty.this, MLAccidentDetailAty.class, data);
            }
        });
    }

    @OnClick(R.id.tab2_iv_add)
    public void addOnClick(View view){
        startAct(MLAccidentAty.this, MLAccidentAdd1Aty.class);
    }
    @OnClick(R.id.top_btn_left)
    public void backOnClick(View view){
        finish();
    }

    //获取事故车
    private void initData() {


        ZMRequestParams params = new ZMRequestParams();
       /* if(!MLToolUtil.isNull(keyWord)){
            params.addParameter(MLConstants.PARAM_MY_ACCIDENT,keyWord);
        }*/
        params.addParameter("cityId", BaseApplication._currentCity);

        ZMHttpRequestMessage message = new ZMHttpRequestMessage(ZMHttpType.RequestType.AACIDENT_LIST, null, params, _handler, HTTP_RESPONSE_ACCIDENT_LIST, MLAccidentServices.getInstance());
        loadDataWithMessage(MLAccidentAty.this, MLToolUtil.getResourceString(R.string.loading_message), message);

    }


    public void pageData(){
        ZMRequestParams params = new ZMRequestParams();
       /* if(!MLToolUtil.isNull(keyWord)){
            params.addParameter(MLConstants.PARAM_MY_ACCIDENT,keyWord);
        }*/
        params.addParameter("cityId", BaseApplication._currentCity);
        String lastId = _accidentDatas.get(_accidentDatas.size()-1).info.id+"";
        params.addParameter(MLConstants.PARAM_MESSAGE_LASTID,lastId);
        ZMHttpRequestMessage message = new ZMHttpRequestMessage(ZMHttpType.RequestType.AACIDENT_LIST, null, params, _handler, HTTP_RESPONSE_ACCIDENT_PAGE, MLAccidentServices.getInstance());
        loadDataWithMessage(MLAccidentAty.this, null, message);
    }


    private static final int HTTP_RESPONSE_ACCIDENT_LIST = 0;
    private static final int HTTP_RESPONSE_ACCIDENT_PAGE = 1;
    private static final int HTTP_RESPONSE_LEAVE_LIST = 2;
    private static final int HTTP_RESPONSE_LEAVE_PAGE = 3;
    private static final int HTTP_RESPONSE_ADVAN_LIST = 4;
    private static final int HTTP_RESPONSE_ADVAN_PAGE = 5;

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
                case HTTP_RESPONSE_ACCIDENT_LIST:{
                    MLAccidentListResponse ret = (MLAccidentListResponse) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")&&ret.datas!=null){
                        _accidentDatas = ret.datas;
                        mAdapter.setData(ret.datas);
                    }else{
                        showMessageError("获取事故车列表失败!");
                    }
                    _pullToRefreshLv.onHeaderRefreshFinish();
                    break;
                }
                //事故车加载更多
                case HTTP_RESPONSE_ACCIDENT_PAGE:{
                    MLAccidentListResponse ret = (MLAccidentListResponse) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")&&ret.datas!=null){
                        _accidentDatas.addAll(ret.datas);
                        mAdapter.setData(_accidentDatas);
                    }else{
                        showMessageError("获取事故车列表失败!");
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
