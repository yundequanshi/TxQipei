package com.zuomei.auxiliary;

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
import com.zuomei.model.MLLotteryRecordData;

public class MLLotteryAdapter extends AdapterBase{

	private Context _context;
	
	public MLLotteryAdapter(Context _context) {
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
		
		MLLotteryRecordData data = (MLLotteryRecordData) getItem(position);
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
		
	    @ViewInject(R.id.tv_name)
		private TextView _nameTv;
	    
	    @ViewInject(R.id.tv_price)
		private TextView _priceTv;
	    
	    
	    @ViewInject(R.id.tv_time)
		private TextView _timeTv;
	    

	    
		private void init(){
			View view = LayoutInflater.from(_context).inflate(R.layout.lottery_record_item, null);
			addView(view);
			ViewUtils.inject(this, view);
		}
		
		public void setData(MLLotteryRecordData data){
			_nameTv.setText(data.awardName);
			_priceTv.setText(data.val+"元");
			_timeTv.setText(data.createTime);
		}

	}
}
