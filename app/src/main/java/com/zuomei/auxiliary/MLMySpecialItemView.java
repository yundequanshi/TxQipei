package com.zuomei.auxiliary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.model.MLMySpecialData;

public class MLMySpecialItemView extends LinearLayout{

	private Context _context;
	public MLMySpecialItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		_context = context;
		init();
	}

	public MLMySpecialItemView(Context context) {
		super(context);
		_context = context;
		init();
	}
	
	@ViewInject(R.id.tv_name)
	private TextView _nameTv;
	
	@ViewInject(R.id.tv_time)
	private TextView _timeTv;
	
	private void init(){
		View view = LayoutInflater.from(_context).inflate(R.layout.my_special_item, null);
		addView(view);
		ViewUtils.inject(this, view);
	}

	public void setData(MLMySpecialData data) {
		if(data==null)return;
		_nameTv.setText(data.title);
		_timeTv.setText(data.time);
	}
}
