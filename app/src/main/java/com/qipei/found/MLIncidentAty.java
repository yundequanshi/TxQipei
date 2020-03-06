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
import com.qipei.adapter.MLIncidentAdapter;
import com.txsh.R;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLeaveData;
import com.zuomei.model.MLLeaveResponse;
import com.zuomei.services.MLLeaveServices;

import java.util.List;

/**
 * 事故件
 * Created by Marcello on 2015/6/5.
 */
public class MLIncidentAty extends BaseActivity  {

    @ViewInject(R.id.part_lv)
    private AbPullToRefreshView _pullToRefreshLv;

    @ViewInject(R.id.accident_grid)
    private GridView mGrid;

    private MLIncidentAdapter mAdapter;


    public List<MLLeaveData> _leaveDatas;
@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qp_incident_list);
        ViewUtils.inject(this);

    initView();
    initData();

    }

    private void initView() {
        mAdapter = new MLIncidentAdapter(MLIncidentAty.this,R.layout.item_accident);
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
                leavePageData();
            }
        });


        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MLLeaveData data = (MLLeaveData) parent.getAdapter().getItem(position);
                startAct(MLIncidentAty.this, MLIncidentDetailAty.class, data);
            }
        });

    }

    @OnClick(R.id.tab2_iv_add)
    public void addOnClick(View view){
        startAct(MLIncidentAty.this, MLIncidentAdd1Aty.class);
    }
    @OnClick(R.id.top_btn_left)
    public void backOnClick(View view){
        finish();
    }

    private void initData() {

        ZMRequestParams params = new ZMRequestParams();
     /*   if(!MLToolUtil.isNull(keyWord)){
            params.addParameter("key",keyWord);
        }*/
        params.addParameter("cityId",BaseApplication._currentCity);
        ZMHttpRequestMessage message = new ZMHttpRequestMessage(ZMHttpType.RequestType.LEAVE_LIST, null, params, _handler, HTTP_RESPONSE_LEAVE_LIST, MLLeaveServices.getInstance());
        loadDataWithMessage(MLIncidentAty.this, null, message);

    }



    public void leavePageData(){
//		MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
        ZMRequestParams params = new ZMRequestParams();
	/*	if(user.isDepot){
			params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,user.Id);
		}else{
			params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,user.Id);
		}*/
        params.addParameter("cityId",BaseApplication._currentCity);

       /* if(!MLToolUtil.isNull(keyWord)){
            params.addParameter("key",keyWord);
        }*/
        String lastId = _leaveDatas.get(_leaveDatas.size()-1).info.id+"";
        params.addParameter(MLConstants.PARAM_MESSAGE_LASTID,lastId);
        ZMHttpRequestMessage message = new ZMHttpRequestMessage(ZMHttpType.RequestType.LEAVE_LIST, null, params, _handler, HTTP_RESPONSE_LEAVE_PAGE, MLLeaveServices.getInstance());
        loadDataWithMessage(MLIncidentAty.this, null, message);
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
                case HTTP_RESPONSE_LEAVE_LIST:{
                    MLLeaveResponse ret = (MLLeaveResponse) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")&&ret.datas!=null){
                        _leaveDatas = ret.datas;
                        mAdapter.setData(ret.datas);
                    }else{
                        showMessageError("获取列表失败!");
                    }
                    _pullToRefreshLv.onHeaderRefreshFinish();
                    break;
                }
                //二手件加载更多
                case HTTP_RESPONSE_LEAVE_PAGE:{
                    MLLeaveResponse ret = (MLLeaveResponse) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")&&ret.datas!=null){
                        _leaveDatas.addAll(ret.datas);
                        mAdapter.setData(_leaveDatas);
                    }else{
                        showMessageError("获取列表失败!");
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
