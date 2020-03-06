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
import com.zuomei.model.MLMyTxCashData;
import com.zuomei.utils.MLStringUtils;

public class MLMyMoneyItemView extends RelativeLayout{

	private Context _context;
	public MLMyMoneyItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		_context = context;
		init();
	}

	public MLMyMoneyItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		_context = context;
		init();
	}

   public MLMyMoneyItemView(Context context) {
		super(context);
		_context = context;
		init();
	}
	
/*	@ViewInject(R.id.my_money_order)
	private TextView _orderTv;*/
   
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

	public void setData(MLMyTxCashData data) {
	    String state = "";
		
	    
	    if(data.cashStatus.equalsIgnoreCase("1")){
	    	state="成功";
	    }else if(data.cashStatus.equalsIgnoreCase("2")){
	    	state="申请中";
	    }else if(data.cashStatus.equalsIgnoreCase("3")){
	    	state="失败";
	    }
	    
	//	_orderTv.setText("订单号:"+data.forCashId);
		String time = MLStringUtils.time_second(data.createTime);
		_timeTv.setText(time);
		_stateTv.setText(state);
		
		_moneyTv.setText("+"+data.monery);
		
	}

}
