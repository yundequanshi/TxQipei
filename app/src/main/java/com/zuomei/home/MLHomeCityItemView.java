package com.zuomei.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.model.MLHomeCityData;

public class MLHomeCityItemView extends RelativeLayout{

	private Context _context;
	public MLHomeCityItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		_context = context;
		init();
	}

	public MLHomeCityItemView(Context context) {
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

	public void setData(MLHomeCityData data) {
		_nameTv.setText(data.cityName);
	}
}
