package com.zuomei.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.PopupWindow;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.txsh.R;

public class BasePopupWindow  extends PopupWindow{


    private Context _context;
	 protected BitmapUtils bitmapUtils;
	 protected BitmapDisplayConfig bigPicDisplayConfig;
	public BasePopupWindow(Context context) {
		super(context);
		_context =context;
		init();
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
		
}
