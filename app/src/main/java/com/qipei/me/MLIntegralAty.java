package com.qipei.me;

import java.text.DecimalFormat;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.ml.base.widget.roundedimageview.RoundedImageView;

import com.ab.view.pullview.AbPullToRefreshView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.home.MLMyIntegralAdapter;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLIntegralData;
import com.zuomei.model.MLIntegralResponse;
import com.zuomei.model.MLIntegralsData;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLMyTxListResponse;
import com.zuomei.model.MLRegister;
import com.zuomei.model.QPIntegralRes;
import com.zuomei.services.MLMyServices;

/**
 * 积分管理
 * Created by Marcello on 2015/6/11.
 */
public class MLIntegralAty extends BaseActivity {

    private Context _context;
    private MLLogin _user;
    @ViewInject(R.id.money_root)
    private RelativeLayout _root;

    @ViewInject(R.id.me_iv_icon)
    private RoundedImageView _headIv;

    @ViewInject(R.id.money_tv_name)
    private TextView _nameTv;

    @ViewInject(R.id.money_tv_balance)
    private TextView _balanceTv;

    private MLMyIntegralAdapter _moneyAdapter;

    @ViewInject(R.id.message_lv)
    private AbPullToRefreshView _pullToRefreshLv;
    @ViewInject(R.id.point_lv)
    private ListView _recordLv;
    @ViewInject(R.id.qiandao)
    private RelativeLayout qiandao;
    @ViewInject(R.id.qiandao_tv)
    private TextView qiandao_tv;
    private String moneyCash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qp_me_integal);
        ViewUtils.inject(this);

        _context = this;

        initView();
        initData();
    }
    private void initView() {
        _user = BaseApplication.getInstance().get_user();
        _moneyAdapter = new MLMyIntegralAdapter(_context);
        _recordLv.setAdapter(_moneyAdapter);
   /*     _recordLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                MLMyTxCashData data = (MLMyTxCashData) parent.getAdapter().getItem(position);
                //交易成功
                if(!data.cashStatus.equalsIgnoreCase("3"))return;
                MLMyCashFailPop menuWindow = new MLMyCashFailPop(MLIntegralAty.this, data, _datas.name,new IEvent<String>() {
                    @Override
                    public void onEvent(Object source, String eventArg) {

                    }
                });
                menuWindow.showAtLocation(_root, Gravity.CENTER, 0, 0);

            }
        });*/

        _pullToRefreshLv.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {

            @Override
            public void onHeaderRefresh(AbPullToRefreshView view) {
                initData();
            }
        });
        _pullToRefreshLv.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {

            @Override
            public void onFooterLoad(AbPullToRefreshView view) {
                // pageData();
            }
        });
        _pullToRefreshLv.setPullRefreshEnable(false);
        _pullToRefreshLv.setLoadMoreEnable(false);
    }


    @OnClick(R.id.accident_btn_next)
    public void cjOnClick(View view){
        toActivity(_context, MLConstants.MY_LOTTERY, null);
    }

    private void initData() {
		/* ZMRequestParams params = new ZMRequestParams();
	      params.addParameter(MLConstants.PARAM_REGISTER_PWD,pwd);*/

        ZMRequestParams params = new ZMRequestParams();

        if(_user.isDepot){
            params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,_user.Id);
        }else{
            params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,_user.Id);
        }
        ZMHttpRequestMessage message = new ZMHttpRequestMessage(ZMHttpType.RequestType.MY_INTEGRAL_LIST, null, params, _handler, HTTP_RESPONSE_LIST, MLMyServices.getInstance());
//        ZMHttpRequestMessage message = new ZMHttpRequestMessage(ZMHttpType.RequestType.SIGN_INFO, null, params, _handler, HTTP_RESPONSE_LIST, MLMyServices.getInstance());
        loadDataWithMessage(_context, null, message);
        //QPIntegralRes
    }

 /*   private void pageData() {
        ZMRequestParams params = new ZMRequestParams();
        String lastId = _datas.withDrawCash.get( _datas.withDrawCash.size()-1).id+"";
        if(_user.isDepot){
            params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,_user.Id);
        }else{
            params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,_user.Id);
        }
        params.addParameter(MLConstants.PARAM_MESSAGE_LASTID, lastId);
        ZMHttpRequestMessage message = new ZMHttpRequestMessage(ZMHttpType.RequestType.MY_INTEGRAL_LIST, null, params, _handler, HTTP_RESPONSE_LIST_PAGE, MLMyServices.getInstance());
        loadDataWithMessage(_context, null, message);
    }
*/


    private MLIntegralsData _datas;
    protected void review(MLIntegralsData datas) {
        double m = Double.parseDouble(datas.integalValue);
        DecimalFormat df=new DecimalFormat(".##");
        String money =df.format(m);
        _datas = datas;
        _balanceTv.setText("我的积分：" + m);
        _nameTv.setText(datas.depotName);
        qiandao_tv.setText("每日签到可获得"+datas.integalValue+"积分");
        String iconUrl = APIConstants.API_IMAGE+"?id="+datas.userPhoto;
        BaseApplication.IMAGE_CACHE.get(iconUrl, _headIv);

       _moneyAdapter.setData(datas.integrals);

        //	BaseApplication.IMAGE_CACHE.

    }

	//签到
	@OnClick(R.id.qiandao)
	public void signOnClick(View view){
		ZMRequestParams params = new ZMRequestParams();
		params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,_user.Id);
		ZMHttpRequestMessage message = new ZMHttpRequestMessage(ZMHttpType.RequestType.SIGN, null, params, _handler, HTTP_RESPONSE_SIGN, MLMyServices.getInstance());
		loadDataWithMessage(_context, null, message);
	}



    private static final int HTTP_RESPONSE_LIST = 0;
    private static final int HTTP_RESPONSE_RECHARGE = 1;
    private static final int HTTP_RESPONSE_WITHDRAW = 2;
    private static final int HTTP_RESPONSE_LIST_PAGE = 3;
    private static final int HTTP_RESPONSE_JUDGEPWD= 7;
    private static final int HTTP_RESPONSE_VERIFYPWDPWD= 8;
    private static final int HTTP_RESPONSE_SIGN= 9;
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
                //获取消息列表
                case HTTP_RESPONSE_LIST:{
                    QPIntegralRes ret = (QPIntegralRes) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){
                        review(ret.datas);
                    }
                    _pullToRefreshLv.onHeaderRefreshFinish();
                	
//                	MLIntegralResponse ret = (MLIntegralResponse) msg.obj;
//                	if(ret.state.equalsIgnoreCase("1")){
////                		_info = ret.datas;
//                		review(ret.datas);
//                	}else{
//                	}
                    break;
                }


                case HTTP_RESPONSE_LIST_PAGE:{
                    MLMyTxListResponse ret = (MLMyTxListResponse) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){
                   //     _datas.withDrawCash.addAll(ret.datas.withDrawCash);
                        review(_datas);
                    }
                    _pullToRefreshLv.onFooterLoadFinish();
                    break;
                }

            	case HTTP_RESPONSE_SIGN:{
					MLRegister ret = (MLRegister) msg.obj;
					if(ret.datas){
						showMessageSuccess("签到成功!");
					//	BaseApplication.aCache.put("SIGN", getTimeDay());
					//	mIvSign.setImageResource();
						qiandao.setEnabled(false);
					}else{
						showMessageError("今日已签到!");
					}
					break;
				}

                default:
                    break;
            }
        }
    };


    /**
     * @description   返回
     *
     * @author marcello
     */
    @OnClick(R.id.top_btn_left)
    public void backOnClick(View view){
     finish();
    }
}
