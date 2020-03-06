package com.qipei.part;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.qipei.adapter.MLPartAdapter;
import com.qipei.home.MLBusinessDetailAty;
import com.txsh.R;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLHomeBusinessData;
import com.zuomei.model.MLHomeBusinessResponse;
import com.zuomei.services.MLHomeServices;

import java.util.List;

import cn.ml.base.utils.IEvent;

/**
 * 配件列表
 * Created by Marcello on 2015/6/2.
 */
public class MLPartListFrag extends BaseFragment {

    private View view;

    @ViewInject(R.id.mListView)
    private ListView mListView;




    private Context _context;
    List<String[]> message ;

    private MLPartAdapter mAdapter;
    //商家列表
    private List<MLHomeBusinessData> _business;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.part_list, null);
        ViewUtils.inject(this, view);
        _context = inflater.getContext();

        initView();
        initData();
        return view;
    }

    private void initView() {

        mAdapter = new MLPartAdapter(_context,R.layout.item_part);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startAct(MLPartListFrag.this, MLBusinessDetailAty.class, _business.get(position));
            }
        });

    }

    private void initData() {
        //获取首页商家
        ZMRequestParams params = new ZMRequestParams();
        params.addParameter(MLConstants.PARAM_HOME_CITY, BaseApplication._currentCity);
        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.HOME_BUSINESS, null, params, _handler,HTTP_RESPONSE_BUSINESS , MLHomeServices.getInstance());
        // loadDataWithMessage(_context, null, message2);
        loadData(_context, message2);

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
                //获取首页商家
                case HTTP_RESPONSE_BUSINESS:{
                    MLHomeBusinessResponse ret = (MLHomeBusinessResponse) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){
                        _business = ret.datas;
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

    private IEvent<Object> _event;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _event = (IEvent<Object>) activity;
    }
}
