package com.zuomei.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.txsh.R;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.utils.MLToolUtil;

import cn.ml.base.utils.MLDialogUtils;
import cn.ml.base.utils.MLToastUtils;

public class BaseLayout extends RelativeLayout{

	private Context _context;
	
	 protected BitmapUtils bitmapUtils;
	 protected BitmapDisplayConfig bigPicDisplayConfig;
	public BaseLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this._context = context;
		initBitmap();
	}

	public BaseLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this._context = context;
		initBitmap();
	}

	public BaseLayout(Context context) {
		super(context);
		this._context = context;
		initBitmap();
	}
     
	private void initBitmap() {
	    if (bitmapUtils == null) {
            bitmapUtils = new BitmapUtils(_context);
        }
        bigPicDisplayConfig = new BitmapDisplayConfig();
        
        Drawable loadingDrawable = getResources().getDrawable(R.drawable.test2);
        Drawable failedDrawable = getResources().getDrawable(R.drawable.test2);
        
        bigPicDisplayConfig.setLoadingDrawable(loadingDrawable);
        bigPicDisplayConfig.setLoadFailedDrawable(failedDrawable);
        //bigPicDisplayConfig.setShowOriginal(true); // 显示原始图片,不压缩, 尽量不要使用, 图片太大时容易OOM。
        bigPicDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
        bigPicDisplayConfig.setBitmapMaxSize(BitmapCommonUtils.getScreenSize(_context));
	}

	// ///////////////////////////////////////////////////////////////////
		// 显示加载数据的对话框
		// ///////////////////////////////////////////////////////////////////

		protected ProgressDialog progressDialog = null;

		/**
		 * Destribution：显示默认的加载数据对话框
		 */
		public void showProgressDialog() {
			showProgressDialog(MLToolUtil.getResourceString(R.string.loading_message));
		}

		/**
		 * Destribution：显示加载对话框，并且显示的信息为message
		 * 
		 * @param message
		 */
		public void showProgressDialog(String message) {
			MLDialogUtils.showProgressDialog(_context, message);
		}

		/**
		 * Destribution：关闭加载进度对话框
		 */
		public void dismissProgressDialog() {
			MLDialogUtils.dismissProgressDialog();
		}
		// ///////////////////////////////////////////////////////////////////
		// 显示加载数据的对话框
		// ///////////////////////////////////////////////////////////////////
		private Toast _toast;

	    public void showMessage(int textId) {
	        showMessage(MLToolUtil.getResourceString(textId));
	    }

	    public void showMessage(CharSequence text) {
			MLToastUtils.showMessage(_context, text);
	    }
	    
	    
	    
	    protected void loadDataWithMessage(String message, ZMHttpRequestMessage httpMessage) {
	        new PostMessageTask(message).execute(httpMessage);
	    }


	    private class PostMessageTask extends AsyncTask<ZMHttpRequestMessage, Void, Object> {

	        ZMHttpRequestMessage _hm;
	        String message;

	        public PostMessageTask(String message) {
	            this.message = message;
	        }


	        @Override
	        protected void onPreExecute() {
	            if (this.message == null){
					//showProgressDialog();
					return;
				}else{
					showProgressDialog(this.message);
				}
	        }


	        @Override
	        protected Object doInBackground(ZMHttpRequestMessage... params) {
	            
	            _hm = params[0];
	            Object obj = null;
	            ZMHttpError error = new ZMHttpError();
				try {
					obj = _hm.getWebService().httpPost(params[0]);
				} catch (Exception e) {
					error.errorMessage = (e==null || MLToolUtil.isNull(e.getMessage()))?
							MLToolUtil.getResourceString(R.string.unknown_error):e.getMessage();
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
	    
	    
	   /* 
		public String getHttpDownloadUrl(String loginUserId, String token,
				String userId, String filename) {
			if (ZMToolUtil.isNull(loginUserId) || ZMToolUtil.isNull(token)
					|| ZMToolUtil.isNull(userId) || ZMToolUtil.isNull(filename))
				return null;
			return String.format("%s%s?%s=%s&%s=%s&%s=%s&%s=%s",
					APIConstants.API_DEFAULT_HOST, APIConstants.API_DOWNLOAD,
					ZMConstants.PARAM_LOGINUSERID, loginUserId,
					ZMConstants.PARAM_TOKEN, token, ZMConstants.PARAM_USERID,
					userId, ZMConstants.PARAM_FILENAME, filename);
		}*/
}
