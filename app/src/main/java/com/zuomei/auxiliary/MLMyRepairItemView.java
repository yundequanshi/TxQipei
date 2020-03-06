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
import com.zuomei.model.MLMyRepairData;
import com.zuomei.model.MLMyRepairDetail;
import com.zuomei.utils.MLStringUtils;

public class MLMyRepairItemView extends LinearLayout{

	private Context _context;
	public MLMyRepairItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		_context = context;
		init();
	}

	public MLMyRepairItemView(Context context) {
		super(context);
		_context = context;
		init();
	}
	
	@ViewInject(R.id.repair_tv_carNum)
	private TextView _carNumTv;
	
	@ViewInject(R.id.stock_tv_time)
	private TextView _timeTv;
	
	@ViewInject(R.id.repair_tv_phone)
	private TextView _phoneTv;
	
	@ViewInject(R.id.repair_tv_price)
	private TextView _priceTv;
	
	private void init(){
		View view = LayoutInflater.from(_context).inflate(R.layout.my_repair_item, null);
		addView(view);
		ViewUtils.inject(this, view);
	}

	public void setData(MLMyRepairData data) {
		if(data==null)return;
		
		int price = 0;
		for(MLMyRepairDetail detail : data.breakdownDetail){
			price = price+Integer.parseInt(detail.price);
		}
		price = price+Integer.parseInt(data.cost);
		_carNumTv.setText(data.carNum);
		String time = MLStringUtils.time_year(data.mtime);
		_timeTv.setText(time);
		_phoneTv.setText("电话:"+data.phone);
		_priceTv.setText(price+"元");
		
	}
}
