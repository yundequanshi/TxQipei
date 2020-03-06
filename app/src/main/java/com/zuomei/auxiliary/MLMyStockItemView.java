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
import com.zuomei.model.MLMyStockData;
import com.zuomei.utils.MLStringUtils;

public class MLMyStockItemView extends LinearLayout{

	private Context _context;
	public MLMyStockItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		_context = context;
		init();
	}

	public MLMyStockItemView(Context context) {
		super(context);
		_context = context;
		init();
	}
	
	@ViewInject(R.id.stock_tv_name)
	private TextView _nameTv;
	
	@ViewInject(R.id.stock_tv_time)
	private TextView _timeTv;
	
	@ViewInject(R.id.stock_tv_phone)
	private TextView _phoneTv;
	
	/*@ViewInject(R.id.stock_tv_count)
	private TextView _countTv;
	
	@ViewInject(R.id.stock_tv_price)
	private TextView _priceTv;
	
	@ViewInject(R.id.stock_tv_company)
	private TextView _companyTv;
	
	@ViewInject(R.id.stock_tv_repertory)
	private TextView _repertoryTv;*/
	
	private void init(){
		View view = LayoutInflater.from(_context).inflate(R.layout.my_stock_item, null);
		addView(view);   
		ViewUtils.inject(this, view);
	}

	public void setData(MLMyStockData data) {
		if(data==null)return;
		_nameTv.setText(data.companyName);
		String time = MLStringUtils.time_year(data.mtime);
		_timeTv.setText(time);
		_phoneTv.setText("电话:"+data.companyPhone);
	/*	_companyTv.setText(data.stockcompany);
		String number = String.format("<font color=\"#000000\">数量:</font><font color=\"#d3252e\">%s</font>", data.number);
		_countTv.setText(Html.fromHtml(number));
		_repertoryTv.setText("库存:"+data.stocknumber+"    "+data.quality);
		_priceTv.setText(data.price+"元");*/
		
	}
}
