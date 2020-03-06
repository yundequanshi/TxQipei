package com.zuomei.auxiliary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.model.MLHomeCatalogData;

public class MLHomeCarItemView extends RelativeLayout{

	private Context _context;
	public MLHomeCarItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		_context = context;
		init();
	}

	public MLHomeCarItemView(Context context) {
		super(context);
		_context = context;
		init();
	}
	
	@ViewInject(R.id.home_tv_city)
	private TextView _nameTv;
	
	private void init(){
		View view = LayoutInflater.from(_context).inflate(R.layout.home_city_item, null);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		addView(view,params);
		ViewUtils.inject(this, view);
	}

	public void setData(MLHomeCatalogData data) {
		_nameTv.setText(data.name);
		/*String imgUrl = APIConstants.API_IMAGE+"?id="+data.imageId;
		 BaseApplication.IMAGE_CACHE.get(imgUrl, viewHolder.ivHead);*/
	}
}
