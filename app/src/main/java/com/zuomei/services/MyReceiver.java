package com.zuomei.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.baichang.android.utils.BCStringUtil;
import com.txsh.comment.TXCmWebAty;
import com.txsh.comment.TXHomeActivity;
import com.txsh.model.TxCmWebData;
import com.txsh.quote.business.BizQuotedDetailActivity;
import com.txsh.quote.business.BizQuotedListActivity;
import com.txsh.quote.business.entity.BizQuotedTransferData;
import com.txsh.quote.deport.BizQuotedDetailInDepotActivity;
import com.txsh.quote.deport.entity.QuotedTransferData;
import com.zuomei.constants.APIConstants;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import cn.ml.base.MLBaseConstants;

/**
 * 自定义接收器
 *
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {

  private static final String TAG = "JPush";

  @Override
  public void onReceive(Context context, Intent intent) {
    Bundle bundle = intent.getExtras();

    if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
      String regId = bundle
          .getString(JPushInterface.EXTRA_REGISTRATION_ID);
      Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
      // send the Registration Id to your server...

    } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
        .getAction())) {
      Log.d(TAG,
          "[MyReceiver] 接收到推送下来的自定义消息: "
              + bundle.getString(JPushInterface.EXTRA_MESSAGE));
      // processCustomMessage(context, bundle);

    } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
        .getAction())) {
      Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
      int notifactionId = bundle
          .getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
      Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

    } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
        .getAction())) {
      Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

    /*  101.分配商家发送推送消息
      102.商家报价发送推送消息
      103.修理厂采购推送消息
      104.商家确认发送消息
      105.商家发货发送消息
    */

      Intent i = null;
      JSONObject json = null;
      String mPushType = "";
      String mPushTypeNew = "";
      String mCompanyId = "";
      String mCompanyName = "";
      String mCompanyPhone = "";
      String id = "";
      String mIdNew = "";

      try {
        json = new JSONObject(printBundle(bundle));
        JSONObject mData = new JSONObject(json.getString("data"));
        mPushTypeNew = mData.getString("pushType");
        mIdNew = mData.getString("id");
        mCompanyId = mData.getString("companyId");
        mCompanyName = mData.getString("companyName");
        mCompanyPhone = mData.getString("companyPhone");
        mPushType = json.getString("pushType");
        id = json.getString("datas");
      } catch (JSONException e) {
      }

      Log.d("推送信息", json.toString());

      /**
       * 新的报价推送信息
       */
      if (!BCStringUtil.isEmpty(mPushTypeNew)) {
        if (mPushTypeNew.equals("101")) {
          Intent mIntent = new Intent(context, BizQuotedListActivity.class);
          mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
              | Intent.FLAG_ACTIVITY_CLEAR_TOP);
          context.startActivity(mIntent);
        } else if (mPushTypeNew.equals("102") || mPushTypeNew.equals("104") || mPushTypeNew
            .equals("105")) {
          QuotedTransferData quotedTransferData = new QuotedTransferData();
          Intent mIntent = new Intent(context, BizQuotedDetailInDepotActivity.class);
          quotedTransferData.quoteId = mIdNew;
          quotedTransferData.companyId = mCompanyId;
          quotedTransferData.companyPhone = mCompanyPhone;
          quotedTransferData.companyName = mCompanyName;
          mIntent.putExtra(MLBaseConstants.TAG_INTENT_DATA, quotedTransferData);
          mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
              | Intent.FLAG_ACTIVITY_CLEAR_TOP);
          context.startActivity(mIntent);
        } else if (mPushTypeNew.equals("103")) {
          BizQuotedTransferData bizQuotedTransferData = new BizQuotedTransferData();
          Intent mIntent = new Intent(context, BizQuotedDetailActivity.class);
          bizQuotedTransferData.quoteId = mIdNew;
          bizQuotedTransferData.companyId = mCompanyId;
          mIntent.putExtra(MLBaseConstants.TAG_INTENT_DATA, bizQuotedTransferData);
          mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
              | Intent.FLAG_ACTIVITY_CLEAR_TOP);
          context.startActivity(mIntent);
        }
      }

      /**
       * 老的推送信息
       */
      if (!BCStringUtil.isEmpty(mPushType)) {
        if (mPushType.equalsIgnoreCase("2")) {
          // 互动评论
          i = new Intent(context, TXHomeActivity.class);
        } else if (mPushType.equalsIgnoreCase("3")) {
          // 事故车配件----商户收到报价
      /*	i = new Intent(context, MLAuxiliaryActivity.class);
        i.putExtra("data", MLConstants.MY_PART_OFFER_PRICE);
				MLMyPartBusinessMagData data = new MLMyPartBusinessMagData();
				data.state = "报价中";
				data.id = id;
				i.putExtra("obj", data);*/

        } else if (mPushType.equalsIgnoreCase("4")) {
          // 事故车配件----汽修厂收到通知
      /*	i = new Intent(context, MLAuxiliaryActivity.class);
        i.putExtra("data", MLConstants.PART_DEPOT_DETAIL);
				MLMyPartBusinessData data = new MLMyPartBusinessData();
				data.state = "报价中";
				data.id = id;
				i.putExtra("obj", data);*/

        } else if (mPushType.equalsIgnoreCase("5")) {
          i = new Intent(context, TXHomeActivity.class);

        } else {
          // 后台 1
          TxCmWebData data = new TxCmWebData();
          data.title = "消息详情";
          data.url = APIConstants.API_DEFAULT_HOST + "/mobile3/push/getMsg?id=" + id;
          i = new Intent(context, TXCmWebAty.class);
          i.putExtra(MLBaseConstants.TAG_INTENT_DATA, data);
          i.putExtras(bundle);
        }
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
      }

    } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
        .getAction())) {
      Log.d(TAG,
          "[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
              + bundle.getString(JPushInterface.EXTRA_EXTRA));
    } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
        .getAction())) {
      boolean connected = intent.getBooleanExtra(
          JPushInterface.EXTRA_CONNECTION_CHANGE, false);
      Log.w(TAG, "[MyReceiver]" + intent.getAction()
          + " connected state change to " + connected);
    } else {
      Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
    }
  }

  // 打印所有的 intent extra 数据
  private static String printBundle(Bundle bundle) {
    StringBuilder sb = new StringBuilder();
    for (String key : bundle.keySet()) {
      if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
        sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
      } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
        sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
      } else {
        sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
      }

      // 汽配商城 增加
      if (key.equals(JPushInterface.EXTRA_EXTRA)) {
        return bundle.getString(key);
      }
    }
    return sb.toString();
  }
}
