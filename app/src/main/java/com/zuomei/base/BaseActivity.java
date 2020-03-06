package com.zuomei.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.txsh.R;
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.utils.AppManager;
import com.zuomei.utils.MLToolUtil;
import com.zuomei.widget.utils.MLLoadingDialog;
import com.zuomei.widget.utils.MLTipsToast;

import java.io.Serializable;

import cn.ml.base.MLBaseConstants;
import cn.ml.base.utils.MLAppManager;
import cn.ml.base.utils.MLDialogUtils;
import cn.ml.base.utils.MLToastUtils;

public class BaseActivity extends FragmentActivity {


  private Object mIntentData;


  public BaseActivity(Context _context) {
    this._context = _context;
  }


  public BaseActivity() {
    //	_context = BaseApplication.getInstance();
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    _context = this;
    initIntentData();
    AppManager.getAppManager().addActivity(this);
  }


  // ///////////////////////////////////////////////////////////////////
  // 显示加载数据的对话框
  // ///////////////////////////////////////////////////////////////////
  private static Context _context;
  protected MLLoadingDialog progressDialog = null;

  /**
   * Destribution：显示默认的加载数据对话框
   */
  public void showProgressDialog(Context context) {
    showProgressDialog(MLToolUtil
        .getResourceString(R.string.loading_message), context);
  }

  /**
   * Destribution：显示加载对话框，并且显示的信息为message
   */
  public void showProgressDialog(String message, Context context) {
    /*if (this.progressDialog == null) {
			this.progressDialog = new MLLoadingDialog(context,message);
			this.progressDialog.setCanceledOnTouchOutside(false);
		//	this.progressDialog.setIndeterminate(true);
		}
		if (MLToolUtil.isNull(message))
			this.progressDialog.setMessage(MLToolUtil
					.getResourceString(R.string.loading_message));
		else
			this.progressDialog.setMessage(message);
		this.progressDialog.show();*/
    MLDialogUtils.showProgressDialog(context, message);
  }

/*	public void showProgressDialog(Context context,String message) {
		if (this.progressDialog == null) {
			this.progressDialog = new ProgressDialog(context);
			this.progressDialog.setCanceledOnTouchOutside(false);
			this.progressDialog.setIndeterminate(true);

		}
		if (MLToolUtil.isNull(message))
			this.progressDialog.setMessage(MLToolUtil
					.getResourceString(R.string.loading_message));
		else
			this.progressDialog.setMessage(message);
		this.progressDialog.show();
	}*/


  /**
   * Destribution：关闭加载进度对话框
   */
  public void dismissProgressDialog() {
    MLDialogUtils.dismissProgressDialog();
		/*if (this.progressDialog != null) {
			this.progressDialog.dismiss();
			this.progressDialog = null;
		}*/
  }

  // ///////////////////////////////////////////////////////////////////
  // 显示加载数据的对话框
  // ///////////////////////////////////////////////////////////////////
  private Toast _toast;

  public void showMessage(int textId) {
    showMessage(R.drawable.tips_warning, MLToolUtil.getResourceString(textId));
  }

  public void showMessage(String text) {
    showMessage(R.drawable.tips_warning, text);
  }

  //成功Message
  public void showMessageSuccess(Object obj) {
    if (obj instanceof String) {
      showMessage(R.drawable.tips_success, (CharSequence) obj);
    } else {
      showMessage(R.drawable.tips_success, MLToolUtil.getResourceString((Integer) obj));
    }
  }

  //失败Message
  public void showMessageError(Object obj) {
    if (obj instanceof String) {
      showMessage(R.drawable.tips_error, (CharSequence) obj);
    } else {
      showMessage(R.drawable.tips_error, MLToolUtil.getResourceString((Integer) obj));
    }
  }

  //警告Message
  public void showMessageWarning(Object obj) {
    if (obj instanceof String) {
      showMessage(R.drawable.tips_warning, (CharSequence) obj);
    } else {
      showMessage(R.drawable.tips_warning, MLToolUtil.getResourceString((Integer) obj));
    }
  }

  //笑脸Message
  public void showMessageSmile(int textId) {
    showMessage(R.drawable.tips_smile, MLToolUtil.getResourceString(textId));
  }

  private static MLTipsToast tipsToast;

  public void showMessage(int iconResId, CharSequence text) {
    MLToastUtils.showMessage(_context, text);
		/*if (tipsToast != null) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				tipsToast.cancel();
			}
		} else {
			tipsToast = MLTipsToast.makeText(_context, text, MLTipsToast.LENGTH_SHORT);
		}
		tipsToast.show();
		tipsToast.setIcon(iconResId );
		tipsToast.setText(text);*/

  }


  protected void loadData(Context context,
      ZMHttpRequestMessage httpMessage) {
    _context = context;
    new PostMessageTask().execute(httpMessage);
  }

  protected void loadDataWithMessage(String message, ZMHttpRequestMessage httpMessage) {
    new PostMessageTask(message).execute(httpMessage);
  }

  protected void loadDataWithMessage(Context context, String message,
      ZMHttpRequestMessage httpMessage) {
    _context = context;
    new PostMessageTask(message).execute(httpMessage);
  }


  private class PostMessageTask extends AsyncTask<ZMHttpRequestMessage, Void, Object> {

    ZMHttpRequestMessage _hm;
    String message;

    public PostMessageTask() {
      this.message = null;
    }

    public PostMessageTask(String message) {
      this.message = message;
    }

    @Override
    protected void onPreExecute() {
      if (this.message == null) {
        return;
      } else {
        showProgressDialog(this.message, _context);
      }
			/*else if(message.equalsIgnoreCase("")){
				showProgressDialog();
			}else{
				showProgressDialog(this.message);
			}*/
    }

    @Override
    protected Object doInBackground(ZMHttpRequestMessage... params) {

      _hm = params[0];
      Object obj = null;
      ZMHttpError error = new ZMHttpError();
      try {
        obj = _hm.getWebService().httpPost(params[0]);
      } catch (Exception e) {
        error.errorMessage = (e == null || MLToolUtil.isNull(e.getMessage())) ?
            MLToolUtil.getResourceString(R.string.unknown_error) : e.getMessage();
      }
      if (!MLToolUtil.isNull(error.errorMessage)) {
        obj = error;
      }

      return obj;
    }

    @Override
    protected void onPostExecute(Object obj) {

      dismissProgressDialog();
      Message message = new Message();
      message.what = _hm.getHandlerMessageID();
      message.obj = obj;
      _hm.getHandler().sendMessage(message);
    }
  }


  @Override
  protected void onDestroy() {
    // TODO Auto-generated method stub
    super.onDestroy();
    BaseApplication.IMAGE_CACHE.saveDataToDb(getApplicationContext(), "image_cache");
  }

  //极光
  @Override
  protected void onPause() {
    // TODO Auto-generated method stub
    super.onPause();
    //	JPushInterface.onPause(this);

    if (_context != null) {
      cn.ml.base.utils.MLToolUtil.closeSoftInput(_context);

    }
    MLDialogUtils.dismissProgressDialog();
  }

  @Override
  protected void onResume() {
    // TODO Auto-generated method stub
    super.onResume();
    //	JPushInterface.onResume(this);
  }


  public void toActivity(Context context, int mark, Object data) {
    Intent intent = new Intent();
    intent.setClass(context, MLAuxiliaryActivity.class);
    intent.putExtra("data", mark);
    if (data != null) {
      intent.putExtra("obj", (Serializable) data);
    }
    startActivity(intent);
  }

  // 获取传递值
  private void initIntentData() {
    mIntentData = getIntent().getSerializableExtra(
        MLBaseConstants.TAG_INTENT_DATA);
  }

  public Object getIntentData() {
    return mIntentData;
  }

  // ==========跳转页面=======================
  protected void startAct(Activity act, Class cls) {
    startAct(act, cls, null, -1);
  }

  protected void startAct(Activity act, Class cls, Object obj) {
    startAct(act, cls, obj, -1);
  }

  protected void startAct(Activity act, Class cls, Object obj, int requestCode) {
    MLAppManager.startActivity(act, cls, obj, requestCode);
  }

  // =======================================

  protected Activity getAty() {
    return this;
  }
}
