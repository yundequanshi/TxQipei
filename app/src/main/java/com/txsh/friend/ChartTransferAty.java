package com.txsh.friend;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.baichang.android.utils.BCDialogUtil;
import com.baichang.android.utils.BCStringUtil;
import com.easemob.easeui.model.HxUser;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.txsh.R;
import com.txsh.model.APliayData;
import com.txsh.model.PayResult;
import com.zuomei.base.BaseAct;
import com.zuomei.base.MLAppDiskCache;
import com.zuomei.constants.APIConstants;
import com.zuomei.utils.MLPayUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 聊天转账
 * Created by Administrator on 2015/8/11.
 */
public class ChartTransferAty extends BaseAct {
    @ViewInject(R.id.orderpaydetail_weixinchek)
    private CheckBox cbweixin;
    @ViewInject(R.id.orderpaydetail_alipaychek)
    private CheckBox cbAlipay;
    @ViewInject(R.id.transfer_ed_money)
    private EditText moeny;

    //用户信息
    HxUser UserData;

    private String payType = "1";

//    1：支付宝
//    2：微信

    private IWXAPI api;
    private APliayData aPliayData = new APliayData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hx_aty_chat_transfer);
        ViewUtils.inject(this);
        api = WXAPIFactory.createWXAPI(ChartTransferAty.this, APIConstants.APP_ID);
        if (getIntentData() != null) {
            UserData = (HxUser) getIntentData();
            if (UserData.isNoUserId) {
                getUserId();
            }
        }
    }

    private void getUserId() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("mobiles", UserData.userId);
//        MLHttpRequestMessage message = new MLHttpRequestMessage(
//                MLHttpType.RequestType.CHECK_USER_BY_MOBILE, map, HxAppUserData.class,
//                HxService.getInstance());
//        message.setResList(true);
//        loadData(getAty(), "正在加载", message, new IHttpResultSuccess() {
//            @Override
//            public void success(MLHttpType.RequestType type, Object obj) {
//                try {
//                    List<HxAppUserData> mAppUserData = (List<HxAppUserData>) obj;
//                    UserData.userId = mAppUserData.get(0).id;
//                    UserData.isNoUserId = false;
//                } catch (Exception e) {
//                    return;
//                }
//            }
//        });
    }

    @OnClick({R.id.reach_rel_weixin, R.id.reach_rel_alipay, R.id.reachmoney_btn_queding})
    private void allOnclick(View view) {
        switch (view.getId()) {
            //微信
            case R.id.reach_rel_weixin:
                cbweixin.setChecked(true);
                cbAlipay.setChecked(false);
                break;
            //支付宝
            case R.id.reach_rel_alipay:
                cbweixin.setChecked(false);
                cbAlipay.setChecked(true);
                break;
            //付款
            case R.id.reachmoney_btn_queding:
                if (BCStringUtil.isEmpty(moeny.getText().toString())) {
                    showMessage(getAty(), "金额不能为空");
                    return;
                }
                BCDialogUtil.showDialog(ChartTransferAty.this, R.color.top_bar_normal_bg, "提示", "您确定付款吗", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //微信
                        if (cbweixin.isChecked()) {
                            Log.d("request", "转账用户信息:" + UserData.userId);
                            payType = "2";
                            if (!UserData.isNoUserId) {
                                requestWeixin(UserData.userId, moeny.getText().toString(), payType);
                            }
                        } else {//支付宝
                            Log.d("request", "转账用户信息:" + UserData.userId);
                            payType = "1";
                            if (!UserData.isNoUserId) {
                                requestAliPay(UserData.userId, moeny.getText().toString(), payType);
                            }
                        }
                    }
                }, null);
                break;
        }
    }

    /**
     * 支付宝转账
     */
    private void requestAliPay(String beenLinkedUserId, String money, String payType) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("transferUserId", MLAppDiskCache.getUser().userId + "");
        map.put("beenLinkedUserId", beenLinkedUserId + "");
        map.put("money", money + "");
        map.put("payType", payType);
//        MLHttpRequestMessage message = new MLHttpRequestMessage(
//                MLHttpType.RequestType.PAY_USER_TRANSFER, map, APliayData.class,
//                CmService.getInstance());
//        loadData(getBaseContext(), null, message, new IHttpResultSuccess() {
//            @Override
//            public void success(MLHttpType.RequestType type, Object obj) {
//                aPliayData = (APliayData) obj;
//                payForAlipay(aPliayData.alipay);
//            }
//        }, new IHttpResultError() {
//            @Override
//            public void error(MLHttpType.RequestType type, Object obj) {
//
//            }
//        });
    }

    /**
     * 微信转账
     */
    private void requestWeixin(String beenLinkedUserId, String money, String payType) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("transferUserId", MLAppDiskCache.getUser().userId + "");
        map.put("beenLinkedUserId", beenLinkedUserId + "");
        map.put("money", money + "");
        map.put("payType", payType);
//        MLHttpRequestMessage message = new MLHttpRequestMessage(
//                MLHttpType.RequestType.PAY_USER_TRANSFER2, map, null,
//                MLWeiXinService.getInstance());
//        loadData(getBaseContext(), null, message, new IHttpResultSuccess() {
//            @Override
//            public void success(MLHttpType.RequestType type, Object obj) {
//                PayReq ret = (PayReq) obj;
//                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//                // boolean b = api.registerApp("com.zuomei");
//                boolean b = api.registerApp(APIConstants.APP_ID);
//                api.sendReq(ret);
//                MLAppDiskCache.setWeixin("zhuanzhang");
//            }
//        }, new IHttpResultError() {
//            @Override
//            public void error(MLHttpType.RequestType type, Object obj) {
//
//            }
//        });
    }

    /**
     * 支付宝支付
     *
     * @param
     */
    private void payForAlipay(String payInfo) {
        MLPayUtils.payForAlipay(ChartTransferAty.this, payInfo, mHandler);
    }

    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        requestAliPaySure(aPliayData.orderId);

                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {

                            showMessage(ChartTransferAty.this, "支付结果确认中!");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            showMessage(ChartTransferAty.this, "支付失败!");
                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    showMessage(ChartTransferAty.this, "检查结果为：" + msg.obj);
                    break;
                }
                default:
                    break;
            }
        }

    };

    /**
     * 支付宝确认
     */
    private void requestAliPaySure(String recordId) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("orderId", recordId + "");
//        MLHttpRequestMessage message = new MLHttpRequestMessage(
//                MLHttpType.RequestType.SUREFORWARD_MONEYALI, map, Boolean.class,
//                CmService.getInstance());
//        loadData(getBaseContext(), null, message, new IHttpResultSuccess() {
//            @Override
//            public void success(MLHttpType.RequestType type, Object obj) {
//                Toast.makeText(getBaseContext(), "付款成功", Toast.LENGTH_LONG).show();
//            }
//        }, new IHttpResultError() {
//            @Override
//            public void error(MLHttpType.RequestType type, Object obj) {
//                getAty().finish();
//            }
//        });
    }
}
