package com.zuomei.home;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.ml.base.utils.MLDateUtil;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.base.AdapterBase;
import com.zuomei.model.MLMyTxCashData;
import com.zuomei.utils.MLStringUtils;

public class MLMyMoneyAdapter extends AdapterBase{

	private Context _context;
	
	public MLMyMoneyAdapter(Context _context) {
		super();
		this._context = _context;
	}
	@Override
	protected View getExView(int position, View view, ViewGroup parent) {
		
		MLMyCashItemView item = null;
		if(view ==null){
			item = new MLMyCashItemView(_context);
			view = item;
		}else{
			item = (MLMyCashItemView) view;
		}
		
		MLMyTxCashData data = (MLMyTxCashData) getItem(position);
		item.setData(data);
		return item;
	}
	
	
	public class MLMyCashItemView extends RelativeLayout{

		private Context _context;
		public MLMyCashItemView(Context context, AttributeSet attrs) {
			super(context, attrs);
			_context = context;
			init();
		}

		public MLMyCashItemView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			_context = context;
			init();
		}

	   public MLMyCashItemView(Context context) {
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
	    
	    @ViewInject(R.id.my_money_order)
		private TextView _orderTv;
	    
		private void init(){
			View view = LayoutInflater.from(_context).inflate(R.layout.my_money_item, null);
			addView(view);
			ViewUtils.inject(this, view);
		}
		
		public void setData(MLMyTxCashData data){
		    String state = "";
		
		    
		    if(data.cashStatus.equalsIgnoreCase("1")){
		    	state="交易成功";
		    	_moneyTv.setText("+"+data.monery+"元");
		    }else if(data.cashStatus.equalsIgnoreCase("2")){
		    	state="申请成功";
		    	_moneyTv.setText("-"+data.monery+"元");
		    }else if(data.cashStatus.equalsIgnoreCase("3")){
		    	state="失败";
		    }
		    
		    String dealType = "";
		    if(data.dealType.equalsIgnoreCase("1")){
		    	dealType="提现";
		    }else if(data.dealType.equalsIgnoreCase("2")){
		    	dealType="充话费";
		    }
		    //交易类型
			String order = String.format("类型:<font color=\"#DC143C\">%s</font>", dealType);
			_typeTv.setText(Html.fromHtml(order));   
			//状态
			_stateTv.setText(state);
			_orderTv.setText(data.forCashId);
			
			
			
			String timemonth = MLStringUtils.time_month(data.createTime);
			String timeyear = MLStringUtils.time_year(data.createTime);
			String timedata = MLStringUtils.time_second(data.createTime);
			timedata= timedata.substring(0,timedata.length()-3);
			_timeTv.setText(timedata);
		}

	}
}
