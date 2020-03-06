package com.zuomei.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.litesuits.android.async.AsyncTask;
import com.txsh.R;
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.utils.MLToolUtil;
import com.zuomei.widget.utils.MLLoadingDialog;
import com.zuomei.widget.utils.MLTipsToast;

import java.io.File;
import java.io.Serializable;

import cn.ml.base.utils.MLAppManager;
import cn.ml.base.utils.MLDialogUtils;
import cn.ml.base.utils.MLToastUtils;

@SuppressLint({"NewApi", "ValidFragment"})
public class BaseFragment extends Fragment {

  private static Context _context;

  // ///////////////////////////////////////////////////////////////////
  // 显示加载数据的对话框
  // ///////////////////////////////////////////////////////////////////
  protected BitmapUtils bitmapUtils;
  protected BitmapDisplayConfig bigPicDisplayConfig;

  public BaseFragment(Context _context) {
    this._context = _context;
    init();
  }


  public BaseFragment() {
//		_context = BaseApplication.getInstance();
    _context = BaseApplication.getInstance().getApplicationContext();
    init();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    _context = getActivity();
  }

  private void init() {
    //异步加载图片
    if (bitmapUtils == null) {
      bitmapUtils = new BitmapUtils(_context);
    }
    bigPicDisplayConfig = new BitmapDisplayConfig();

    Drawable loadingDrawable = _context.getResources().getDrawable(R.drawable.test2);
    Drawable failedDrawable = _context.getResources().getDrawable(R.drawable.test2);

    bigPicDisplayConfig.setLoadingDrawable(loadingDrawable);
    bigPicDisplayConfig.setLoadFailedDrawable(failedDrawable);
    //bigPicDisplayConfig.setShowOriginal(true); // 显示原始图片,不压缩, 尽量不要使用, 图片太大时容易OOM。
    bigPicDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
    bigPicDisplayConfig.setBitmapMaxSize(BitmapCommonUtils.getScreenSize(_context));

  }

  //	protected ProgressDialog progressDialog = null;
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

    MLDialogUtils.showProgressDialog(context, message);

	/*	if (this.progressDialog == null) {
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
		/*if (this.progressDialog != null) {
			this.progressDialog.dismiss();
			this.progressDialog = null;
		}*/
    MLDialogUtils.dismissProgressDialog();
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
  public void showMessageSmile(Object obj) {
    if (obj instanceof String) {
      showMessage(R.drawable.tips_smile, (CharSequence) obj);
    } else {
      showMessage(R.drawable.tips_smile, MLToolUtil.getResourceString((Integer) obj));
    }
  }

  private static MLTipsToast tipsToast;

  public void showMessage(int iconResId, CharSequence text) {
    if (_context == null) {
      _context = getActivity();
    }
    MLToastUtils.showMessage(_context, text);
		/*if (tipsToast != null) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				tipsToast.cancel();
			}
		} else {
			if(_context==null)return;
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

  protected void loadDataWithMessage(Context context, String message,
      ZMHttpRequestMessage httpMessage) {
    _context = context;
    new PostMessageTask(message).execute(httpMessage);
  }

  //========================================================
  private class PostMessageTask extends
      AsyncTask<ZMHttpRequestMessage, Void, Object> {

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
        error.errorMessage = (e == null || MLToolUtil.isNull(e
            .getMessage())) ? MLToolUtil
            .getResourceString(R.string.unknown_error) : e
            .getMessage();
      }
      if (!MLToolUtil.isNull(error.errorMessage)) {
        obj = error;
      }

      return obj;
    }

    @Override
    protected void onPostExecute(Object obj) {
      Message message = new Message();
      message.what = _hm.getHandlerMessageID();
      message.obj = obj;
      _hm.getHandler().sendMessage(message);
    }
  }

  File _attachDirFile = null;

  public File getAttachFolder() {
    if (_attachDirFile == null) {
      File file = BaseApplication.getInstance().getExternalCacheDir();
      String subPath = String.format("%s/image", file.getAbsolutePath());
			/*File file = getActivity().getFilesDir();
			String subPath = String.format("%s/uploadAttach",file.getAbsolutePath());
			File subFile = new File(subPath);
			return subFile.getAbsoluteFile();*/
      File subFile = new File(subPath);
      return subFile.getAbsoluteFile();
    }
    return _attachDirFile;
  }

  /**
   * 设置键盘隐藏
   */
  public void setKeyBoardHidden(EditText editText) {
    Context context = BaseApplication.getInstance();
    InputMethodManager imm = (InputMethodManager) context
        .getSystemService(context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
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

  // ==========跳转页面=======================
  protected void startAct(Fragment act, Class cls) {
    startAct(act, cls, null, -1);
  }

  protected void startAct(Fragment act, Class cls, Object obj) {
    startAct(act, cls, obj, -1);
  }

  protected void startAct(Fragment act, Class cls, Object obj, int requestCode) {
    MLAppManager.startActivityForFrg(act, cls, obj, requestCode);
  }

  protected Fragment getFragment() {
    return this;
  }

}
