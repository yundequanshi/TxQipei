package com.zuomei.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;

public class MLHomeMapPopView extends LinearLayout{

	private Context _context;
	public MLHomeMapPopView(Context context, AttributeSet attrs) {
		super(context, attrs);
		_context = context;
		init();
	}  

	public MLHomeMapPopView(Context context) {
		super(context);
		_context = context;
		init();
	}
	
	@ViewInject(R.id.map_tv_name)
	private TextView _nameTv;
	
	@ViewInject(R.id.map_tv_phone)
	private TextView _phoneTv;
	
	private void init(){
		View view = LayoutInflater.from(_context).inflate(R.layout.home_map_pop, null);
		addView(view);
		ViewUtils.inject(this, view);
	}

	public void setData(String name ,String phone) {
			_nameTv.setText(name);
		_phoneTv.setText(phone);
	}
}
