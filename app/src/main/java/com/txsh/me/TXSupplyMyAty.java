package com.txsh.me;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ab.view.pullview.AbPullToRefreshView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.adapter.TXSupplyAdapter;
import com.txsh.model.TXSupplyRes;
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.MLConstants;
import com.zuomei.home.MLHomeProductPop;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLHomeBusinessData;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLAccidentServices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcello on 2015/7/10.
 */
public class TXSupplyMyAty extends BaseActivity {

    @ViewInject(R.id.accident_lv_car)
    private AbPullToRefreshView _pullToRefreshLv;

    @ViewInject(R.id.mListView)
    private ListView mList;


    @ViewInject(R.id.root)
    private RelativeLayout _root;
    public List<TXSupplyRes.TXSupplyData> _supplyDatas;
    private TXSupplyAdapter _supplyAdapter;
    private MLLogin mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tx_me_supply);
        ViewUtils.inject(this);

        _supplyAdapter = new TXSupplyAdapter(TXSupplyMyAty.this,R.layout.item_supply_list,_updateHandler);
        mList.setAdapter(_supplyAdapter);

        initView();
        initSupply();
    }

    private void initView(){
        mUser = BaseApplication.getInstance().get_user();
        _pullToRefreshLv
                .setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
                    @Override
                    public void onHeaderRefresh(AbPullToRefreshView view) {
                      initSupply();
                    }
                });
        _pullToRefreshLv.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
            @Override
            public void onFooterLoad(AbPullToRefreshView view) {

                    supplyPageData();
            }
        });

      /*  mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String s[] = {"删除"};
                AlertDialog builder = new AlertDialog.Builder(TXSupplyMyAty.this, AlertDialog.THEME_HOLO_LIGHT)
                        .setItems(s, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                del(_supplyDatas.get(position).id);
                            }
                        }).setTitle("操作").create();
                builder.show();
            }
        });*/
    }

    private void del(String id) {
        ZMRequestParams params = new ZMRequestParams();
        params.addParameter("id", id);
        ZMHttpRequestMessage message = new ZMHttpRequestMessage(
                ZMHttpType.RequestType.SUPPLY_MY_DEL, null, params, _handler,
                HTTP_RESPONSE_SUPPLY_DEL, MLAccidentServices.getInstance());
        loadDataWithMessage(TXSupplyMyAty.this, null, message);
    }

    // 获取供求信息
    private void initSupply() {
        ZMRequestParams params = new ZMRequestParams();
        if(mUser.isDepot){
            params.addParameter("depotId", mUser.Id);
        }else{
            params.addParameter("companyId", mUser.Id);
        }
        params.addParameter("pageNum", "20");
        ZMHttpRequestMessage message = new ZMHttpRequestMessage(
                ZMHttpType.RequestType.SUPPLY_MY, null, params, _handler,
                HTTP_RESPONSE_SUPPLY_LIST, MLAccidentServices.getInstance());
        loadDataWithMessage(TXSupplyMyAty.this, null, message);
    }
    public void supplyPageData() {
        ZMRequestParams params = new ZMRequestParams();
        if(mUser.isDepot){
            params.addParameter("depotId", mUser.Id);
        }else{
            params.addParameter("companyId", mUser.Id);
        }
        String lastId = _supplyDatas.get(_supplyDatas.size() - 1).id + "";
        params.addParameter(MLConstants.PARAM_MESSAGE_LASTID, lastId);
        ZMHttpRequestMessage message = new ZMHttpRequestMessage(
                ZMHttpType.RequestType.SUPPLY_MY, null, params, _handler,
                HTTP_RESPONSE_SUPPLY_PAGE, MLAccidentServices.getInstance());
        loadDataWithMessage(TXSupplyMyAty.this, null, message);
    }

    @OnClick(R.id.top_btn_left)
    public void backOnClick(View view){
        finish();
    }

    private static final int HTTP_RESPONSE_SUPPLY_LIST = 6;
    private static final int HTTP_RESPONSE_SUPPLY_PAGE = 7;
    private static final int HTTP_RESPONSE_SUPPLY_DEL = 8;

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


                //供求信息列表
                case HTTP_RESPONSE_SUPPLY_LIST:{
                    TXSupplyRes ret = (TXSupplyRes) msg.obj;
                    if (ret.state.equalsIgnoreCase("1") && ret.datas != null) {
                        _supplyDatas = ret.datas;
                        _supplyAdapter.setData(ret.datas);
                    } else {
                        showMessageError("获取列表失败!");
                    }
                    _pullToRefreshLv.onHeaderRefreshFinish();
                    break;
                }

                case HTTP_RESPONSE_SUPPLY_PAGE:{
                    TXSupplyRes ret = (TXSupplyRes) msg.obj;
                    if (ret.state.equalsIgnoreCase("1") && ret.datas != null) {
                        _supplyDatas.addAll(ret.datas);
                        _supplyAdapter.setData(_supplyDatas);
                    } else {
                        showMessageError("获取列表失败!");
                    }
                    _pullToRefreshLv.onFooterLoadFinish();
                    break;
                }

                case HTTP_RESPONSE_SUPPLY_DEL:{

                    MLRegister ret = (MLRegister) msg.obj;
                    if (ret.state.equalsIgnoreCase("1")) {
                      showMessage("删除成功");
                        initSupply();
                    } else {
                        showMessage("删除失败");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };


    public Handler _updateHandler  = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            switch (msg.what) {

                case 2:
                    //图片
//                    String path = APIConstants.API_IMAGE+"?id="+String.valueOf(msg.obj);

                    // String path = APIConstants.API_IMAGE+"?id="+_messageData.get(msg.arg1).info.imageId;
			/*	MLMessagePhotoPop _pop = new MLMessagePhotoPop(getActivity(), path);
				_pop.showAtLocation(_root, Gravity.CENTER, 0, 0); */
                    List<String> images = new ArrayList<String>();
                    images=(List<String>)msg.obj;
                    MLHomeProductPop _pop = new MLHomeProductPop(TXSupplyMyAty.this,images,msg.arg1);
                    _pop.showAtLocation(_root, Gravity.CENTER, 0, 0);
                    break;
                case 3:{
                    //头像
                    final TXSupplyRes.TXSupplyData d = (TXSupplyRes.TXSupplyData) msg.obj;
                    if(d.userType.equalsIgnoreCase("0")){
                        //汽修厂
                      /*  Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + d.user.userPhone));
                        TXSupplyMyAty.this.startActivity(intent);*/
                    }else{
                        MLHomeBusinessData _business = new MLHomeBusinessData();
                        _business.isCollect=false;
                        _business.id = d.id;
                        Intent intent = new Intent();
                        intent.setClass(TXSupplyMyAty.this, MLAuxiliaryActivity.class);
                        intent.putExtra("data", MLConstants.HOME_BUSINESS_INFO);
                        intent.putExtra("obj", (Serializable) _business);
                        TXSupplyMyAty.this.startActivity(intent);
                    }

                    break;
                }


                case 4:{
                    //删除

                    final TXSupplyRes.TXSupplyData data = (TXSupplyRes.TXSupplyData) msg.obj;

                    String s[] = {"删除"};
                    AlertDialog builder = new AlertDialog.Builder(TXSupplyMyAty.this, AlertDialog.THEME_HOLO_LIGHT)
                            .setItems(s, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    del(data.id);
                                }
                            }).setTitle("操作").create();
                    builder.show();

                    break;
                }

                default:
                    break;
            }


        }

    };
}
