package com.zuomei.widget.picgallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.zuomei.base.BaseApplication;
import com.zuomei.widget.utils.MLLoadingDialog;

import java.util.ArrayList;
import java.util.List;

public class ReferralGalleryAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<MyImageView> imageViews = new ArrayList<MyImageView>();

    private ImageCacheManager imageCache;

    private List<String> mItems;
    private int _position;
    private MLLoadingDialog _dialog;

    public void setData(List<String> data, int position) {
        _position = position;
        this.mItems = data;
        notifyDataSetChanged();
    }

    protected BitmapUtils bitmapUtils;
    protected BitmapDisplayConfig bigPicDisplayConfig;

    public ReferralGalleryAdapter(Context context) {
        this.context = context;
        imageCache = ImageCacheManager.getInstance(context);
        _dialog = new MLLoadingDialog(context);
        _dialog.setCanceledOnTouchOutside(false);
        if (bitmapUtils == null) {
            bitmapUtils = new BitmapUtils(context);
        }
        bigPicDisplayConfig = new BitmapDisplayConfig();
       /*     Drawable loadingDrawable = context.getResources().getDrawable(R.drawable.test2);
	        Drawable failedDrawable = context.getResources().getDrawable(R.drawable.test2);
	        
	        bigPicDisplayConfig.setLoadingDrawable(loadingDrawable);
	        bigPicDisplayConfig.setLoadFailedDrawable(failedDrawable);*/
        //bigPicDisplayConfig.setShowOriginal(true); // 显示原始图片,不压缩, 尽量不要使用, 图片太大时容易OOM。
        bigPicDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
        bigPicDisplayConfig.setBitmapMaxSize(BitmapCommonUtils.getScreenSize(context));
    }

    @Override
    public int getCount() {
        return mItems != null ? mItems.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyImageView view = new MyImageView(context);
        view.setLayoutParams(new Gallery.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));

        String item = mItems.get(position);
/*		if (item != 0) {
			Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), item);
			view.setTag(item);
				view.setImageBitmap(bmp);
			if (!this.imageViews.contains(view)) {
				imageViews.add(view);
			}
		}*/
        //imageCache.
        //	view.setImageBitmap(bmp);
/*		BaseApplication.IMAGE_CACHE.setOnImageCallbackListener(new OnImageCallbackListener() {
			@Override
			public void onPreGet(String imageUrl, View view) {

			}
			@Override
			public void onGetSuccess(String imageUrl, Bitmap loadedImage, View view,
					boolean isInCache) {
				
				((MyImageView)view).setImageBitmap(loadedImage);
				
			}
			
			@Override
			public void onGetNotInCache(String imageUrl, View view) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onGetFailed(String imageUrl, Bitmap loadedImage, View view,
					FailedReason failedReason) {
				// TODO Auto-generated method stub
				
			}
		});
		 BaseApplication.IMAGE_CACHE.get(item, view);*/


        //=========================================================
        BitmapLoadCallBack<ImageView> callback = new DefaultBitmapLoadCallBack<ImageView>() {
            @Override
            public void onLoadStarted(ImageView container, String uri, BitmapDisplayConfig config) {
                super.onLoadStarted(container, uri, config);
           /*     container.setScaleType(ScaleType.FIT_CENTER);
                container.setImageResource(R.drawable.test2);*/
                //   _dialog.show();
            }

            @Override
            public void onLoadCompleted(ImageView container, String uri, Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
                super.onLoadCompleted(container, uri, bitmap, config, from);
                ((MyImageView) container).setImageBitmap(bitmap);
                //	_dialog.dismiss();
            }
        };

        // bitmapUtils.display(view, item, bigPicDisplayConfig, callback);
        view.setTag(item);
        if (!BaseApplication.IMAGE_CACHE.get(item, view)) {
            //view.setImageResource(R.drawable.default_message_header);
        }


        if (!this.imageViews.contains(view)) {
            imageViews.add(view);
        }

        return view;
    }


}
