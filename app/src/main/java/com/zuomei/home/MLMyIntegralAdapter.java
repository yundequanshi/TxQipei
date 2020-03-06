package com.zuomei.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.base.AdapterBase;
import com.zuomei.model.MLIntegralsData;

public class MLMyIntegralAdapter extends AdapterBase{

	private Context _context;

	public MLMyIntegralAdapter(Context _context) {
		super();
		this._context = _context;
	}
	@Override
	protected View getExView(int position, View view, ViewGroup parent) {

		MLMyIntegralItemView item = null;
		if(view ==null){
			item = new MLMyIntegralItemView(_context);
			view = item;
		}else{
			item = (MLMyIntegralItemView) view;
		}

		MLIntegralsData.MLIntegralList data = (MLIntegralsData.MLIntegralList) getItem(position);
		item.setData(data);
		return item;
	}
	
	
	public class MLMyIntegralItemView extends RelativeLayout{

		private Context _context;
		public MLMyIntegralItemView(Context context, AttributeSet attrs) {
			super(context, attrs);
			_context = context;
			init();
		}

		public MLMyIntegralItemView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			_context = context;
			init();
		}

	   public MLMyIntegralItemView(Context context) {
			super(context);
			_context = context;
			init();
		}
		
	    @ViewInject(R.id.my_money_type)
		private TextView _typeTv;
	    
	    @ViewInject(R.id.my_money_time)
		private TextView _timeTv;
	    
	    @ViewInject(R.id.my_money_state)
		private TextView _stateTv;
	    
	    @ViewInject(R.id.my_money_add)
		private TextView _moneyTv;
	    

	    
		private void init(){
			View view = LayoutInflater.from(_context).inflate(R.layout.my_cash_item, null);
			addView(view);
			ViewUtils.inject(this, view);
		}
		
		public void setData(MLIntegralsData.MLIntegralList data){
			String state = "";


			/*if(data.source.equalsIgnoreCase("1")){
				state="成功";
			}else if(data.cashStatus.equalsIgnoreCase("2")){
				state="申请中";
			}else if(data.cashStatus.equalsIgnoreCase("3")){
				state="失败";
			}*/

			//	_orderTv.setText("订单号:"+data.forCashId);
			//String time = MLStringUtils.time_second(data.createTime);
			_timeTv.setText(data.createTime);
			_stateTv.setText(data.source);

			_moneyTv.setText(""+data.val);
		}

	}
}
