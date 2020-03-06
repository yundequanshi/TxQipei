package com.zuomei.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;
import com.zuomei.model.MLHomeCatalogData;

public class MLHomeCatalogItemView extends RelativeLayout{

	private Context _context;
	public MLHomeCatalogItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		_context = context;
		init();
	}

	public MLHomeCatalogItemView(Context context) {
		super(context);
		_context = context;
		init();
	}
	
	@ViewInject(R.id.catalog_tv_name)
	private TextView _nameTv;
	
	@ViewInject(R.id.catalog_iv)
	private ImageView _image;
	
	 private BitmapUtils bitmapUtils;
	 private BitmapDisplayConfig bigPicDisplayConfig;
	private void init(){
		View view = LayoutInflater.from(_context).inflate(R.layout.home_catalog_item, null);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		addView(view,params);
		ViewUtils.inject(this, view);
	}   

	public void setData(MLHomeCatalogData data) {
		_nameTv.setText(data.name);
	//	if(data.name.equalsIgnoreCase("交易记录")){
		if(data.name.equalsIgnoreCase("交易记录")){
			_image.setImageResource(R.drawable.home_catalog_pay);
			return ;
		}
		
		if(data.name.equalsIgnoreCase("签到")){
			_image.setImageResource(R.drawable.home_catalog_qd);
			return ;
		}
		
		
		if(data.name.equalsIgnoreCase("上传发货单")||data.name.equalsIgnoreCase("提现")){
			_image.setImageResource(R.drawable.home_catalog_tixian);
			return ;
		}
		
		
		String imgUrl = APIConstants.API_IMAGE+"?id="+data.imageId;
//		 bitmapUtils.display(_image, imgUrl, bigPicDisplayConfig);		
		// bitmapUtils.display(_image, imgUrl);
		_image.setTag(imgUrl);
		 if (!BaseApplication.IMAGE_CACHE.get(imgUrl, _image)) {
			 _image.setImageDrawable(null);
		    }
	}
}
