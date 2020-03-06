package com.qipei.me;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.view.pullview.AbPullToRefreshView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.home.MLMyCashAdapter;
import com.zuomei.home.MLMyCashFailPop;
import com.zuomei.home.MLMyCashPop;
import com.zuomei.home.MLMyPwdPop;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLMyTxCashData;
import com.zuomei.model.MLMyTxListData;
import com.zuomei.model.MLMyTxListResponse;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLMyServices;
import com.zuomei.services.MLPayServices;
import com.zuomei.utils.MLToolUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import cn.ml.base.utils.IEvent;
import cn.ml.base.widget.roundedimageview.RoundedImageView;

/**
 * 余额管理
 * Created by Marcello on 2015/6/11.
 */
public class MLBalanceAty extends BaseActivity {

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

    private MLMyCashAdapter _moneyAdapter;

    @ViewInject(R.id.message_lv)
    private AbPullToRefreshView _pullToRefreshLv;
    @ViewInject(R.id.point_lv)
    private ListView _recordLv;
    private String moneyCash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qp_me_points);
        ViewUtils.inject(this);

        _context = this;

        initView();
        initData();
    }
    private void initView() {
        _user = BaseApplication.getInstance().get_user();
        _moneyAdapter = new MLMyCashAdapter(_context);
        _recordLv.setAdapter(_moneyAdapter);
        _recordLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                MLMyTxCashData data = (MLMyTxCashData) parent.getAdapter().getItem(position);
                //交易成功
                if(!data.cashStatus.equalsIgnoreCase("3"))return;
                MLMyCashFailPop menuWindow = new MLMyCashFailPop(MLBalanceAty.this, data, _datas.name,new IEvent<String>() {
                    @Override
                    public void onEvent(Object source, String eventArg) {

                    }
                });
                menuWindow.showAtLocation(_root, Gravity.CENTER, 0, 0);

            }
        });

        _pullToRefreshLv.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {

            @Override
            public void onHeaderRefresh(AbPullToRefreshView view) {
                initData();
            }
        });;
        _pullToRefreshLv.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {

            @Override
            public void onFooterLoad(AbPullToRefreshView view) {
                pageData();
            }
        });
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
        ZMHttpRequestMessage message = new ZMHttpRequestMessage(ZMHttpType.RequestType.MY_WITHDRAW_LIST, null, params, _handler, HTTP_RESPONSE_LIST, MLMyServices.getInstance());
        loadDataWithMessage(_context, null, message);

    }

    private void pageData() {
        ZMRequestParams params = new ZMRequestParams();
        String lastId = _datas.withDrawCash.get( _datas.withDrawCash.size()-1).id+"";
        if(_user.isDepot){
            params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,_user.Id);
        }else{
            params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,_user.Id);
        }
        params.addParameter(MLConstants.PARAM_MESSAGE_LASTID,lastId);
        ZMHttpRequestMessage message = new ZMHttpRequestMessage(ZMHttpType.RequestType.MY_WITHDRAW_LIST, null, params, _handler, HTTP_RESPONSE_LIST_PAGE, MLMyServices.getInstance());
        loadDataWithMessage(_context, null, message);
    }

    private void requestRecharge(String money){
        ZMRequestParams params = new ZMRequestParams();
        if(_user.isDepot){
            params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,_user.Id);
        }else{
            params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1,_user.Id);
        }
        params.addParameter("money", money);
        ZMHttpRequestMessage message = new ZMHttpRequestMessage(ZMHttpType.RequestType.MY_DEAL_RECHARGE, null, params, _handler, HTTP_RESPONSE_RECHARGE, MLMyServices.getInstance());
        loadDataWithMessage(_context, null, message);
    }


    private MLMyTxListData _datas;
    protected void review(MLMyTxListData datas) {
        double m = Double.parseDouble(datas.balance);
        DecimalFormat df=new DecimalFormat(".##");
        String money =df.format(m);
        _datas = datas;
        _balanceTv.setText("余额：" + money + "元");
        _nameTv.setText(datas.name);
        String iconUrl = APIConstants.API_IMAGE+"?id="+datas.logo;
        BaseApplication.IMAGE_CACHE.get(iconUrl, _headIv);

        _moneyAdapter.setData(datas.withDrawCash);

        //	BaseApplication.IMAGE_CACHE.

    }

    @OnClick(R.id.accident_btn_next)
    public void inputOnClick(View view){
        judgePwd();
    }


    private void showCashPop(){

        MLMyCashPop menuWindow = new MLMyCashPop(null,MLBalanceAty.this, new IEvent<String>() {
            @Override
            public void onEvent(Object source, String eventArg) {
                double balance =Double.parseDouble( _datas.balance);
                double money =Double.parseDouble(eventArg);

                double minMoneyUser = Double.parseDouble(_datas.minMoneyUser);
                if(money<minMoneyUser){
                    showMessageWarning("最低提现额度为 : "+_datas.minMoneyUser+"元");
                    return;
                }
                if(money>balance){
                    showMessageWarning("余额不足!");
                    return;
                }

                moneyCash = eventArg;
                //弹出框 输入支付密码
                inputPwd();
                //requestRecharge(eventArg);
            }
        });
        menuWindow.showAtLocation(_root, Gravity.CENTER, 0, 0);
    }

    /**
     * 弹出框 输入支付密码
     * @description
     *
     * @author marcello
     */
    private void inputPwd(){

        MLMyPwdPop menuWindow = new MLMyPwdPop(MLBalanceAty.this, new IEvent<String>() {
            @Override
            public void onEvent(Object source, String eventArg) {
                verifyPayPwd(eventArg);
            }
        });
        menuWindow.showAtLocation(_root, Gravity.CENTER, 0, 0);
    }


    //====================================================
    /**
     * 判断是否设置支付密码
     * @description
     *
     * @author marcello
     */
    private void judgePwd(){
        ZMRequestParams catalogParam = new ZMRequestParams();
        JSONObject jo = new JSONObject();
        try {
            jo.put("companyId", _user.Id);
        } catch (JSONException e) {
            return;
        }
        catalogParam.addParameter("data",jo.toString());

        ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(ZMHttpType.RequestType.MY_PAY_JUDGEPWD, null, catalogParam, _handler, HTTP_RESPONSE_JUDGEPWD, MLPayServices.getInstance());
        loadDataWithMessage(_context, MLToolUtil.getResourceString(R.string.loading_message), message1);
    }
    private void verifyPayPwd(String pwd){
        pwd = MLToolUtil.MD5(pwd);
        ZMRequestParams catalogParam = new ZMRequestParams();

        JSONObject jo = new JSONObject();
        try {
            jo.put("companyId", _user.Id);
            jo.put("pwd", pwd);
        } catch (JSONException e) {
            return;
        }
        catalogParam.addParameter("data",jo.toString());

        ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(ZMHttpType.RequestType.MY_PAY_VERIFYPWD, null, catalogParam, _handler, HTTP_RESPONSE_VERIFYPWDPWD, MLPayServices.getInstance());
        loadDataWithMessage(_context, null, message1);
    }

    private static final int HTTP_RESPONSE_LIST = 0;
    private static final int HTTP_RESPONSE_RECHARGE = 1;
    private static final int HTTP_RESPONSE_WITHDRAW = 2;
    private static final int HTTP_RESPONSE_LIST_PAGE = 3;
    private static final int HTTP_RESPONSE_JUDGEPWD= 7;
    private static final int HTTP_RESPONSE_VERIFYPWDPWD= 8;
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
                    MLMyTxListResponse ret = (MLMyTxListResponse) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){
                        review(ret.datas);
                    }
                    _pullToRefreshLv.onHeaderRefreshFinish();
                    break;
                }
                case HTTP_RESPONSE_RECHARGE:{
                    MLRegister ret  = (MLRegister) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){
                        showMessageSuccess("提现成功!");
                        initData();
                    }else if(ret.state.equalsIgnoreCase("-1")&&ret.message.contains("未绑定")){

                        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                        builder.setTitle("提示");
                        builder.setMessage("绑定银行卡才能体现，是否现在绑定？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //_event.onEvent("1", MLConstants.MY_BANK_CARD);
                                toActivity(MLBalanceAty.this, MLConstants.MY_BANK_CARD,"1");
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();


                    }
                    break;
                }

                case HTTP_RESPONSE_LIST_PAGE:{
                    MLMyTxListResponse ret = (MLMyTxListResponse) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")){
                        _datas.withDrawCash.addAll(ret.datas.withDrawCash);
                        review(_datas);
                    }
                    _pullToRefreshLv.onFooterLoadFinish();
                    break;
                }

                //判断是否设置支付密码
                case HTTP_RESPONSE_JUDGEPWD:{
                    MLRegister ret = (MLRegister) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")&&ret.datas){
                        showCashPop();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(MLBalanceAty.this,AlertDialog.THEME_HOLO_LIGHT);
                        builder.setTitle("提示");
                        builder.setMessage("请先设置钱包密码");
                        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                //_event.onEvent(null, MLConstants.MY_PAY_PWD);
                                toActivity(MLBalanceAty.this, MLConstants.MY_PAY_PWD,"1");
                            }
                        });
                        builder.setPositiveButton("取消", null);
                        builder.show();
                    }
                    break;
                }

                case HTTP_RESPONSE_VERIFYPWDPWD:{
                    MLRegister ret = (MLRegister) msg.obj;
                    if(ret.state.equalsIgnoreCase("1")&&ret.datas){
                        //提现
                        requestRecharge(moneyCash);
                    }else{
                        showMessageError("钱包密码不正确!");
                        inputPwd();
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
