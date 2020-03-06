package com.zuomei.auxiliary;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.base.AdapterBase;
import com.zuomei.model.MLMyPartBusinessMagData;

public class MLMyPartOfferAdapter extends AdapterBase<MLMyPartBusinessMagData>{

	private Context _context;
	
	public MLMyPartOfferAdapter(Context _context) {
		super();
		this._context = _context;
	}

	@Override
	protected View getExView(int position, View view, ViewGroup parent) {
		
		ItemView item = null;
		if(view ==null){
			item = new ItemView(_context);
			view = item;
		}else{
			item = (ItemView) view;
		}
		MLMyPartBusinessMagData data = (MLMyPartBusinessMagData) getItem(position);
		item.setData(data);
		return item;
	}
	
	
	
	
	class ItemView extends LinearLayout{
		private Context _context;
		public ItemView(Context context, AttributeSet attrs) {
			super(context, attrs);
			_context = context;
			init();
		}

		public ItemView(Context context) {
			super(context);
			_context = context;
			init();
		}
		
		@ViewInject(R.id.part_name)
		private TextView _nameTv;
		
		@ViewInject(R.id.patr_state)
		private ImageView _stateIv;
		
		@ViewInject(R.id.part_count)
		private TextView _countTv;

		@ViewInject(R.id.part_cartype)
		private TextView _carTypeTv;
		
		
		@ViewInject(R.id.part_time)
		private TextView _timeTv;
		
		private void init(){
			View view = LayoutInflater.from(_context).inflate(R.layout.my_partoffer_item, null);
			addView(view);   
			ViewUtils.inject(this, view);
			
		
		}

		public void setData(MLMyPartBusinessMagData data) {
			_nameTv.setText(data.name);
			
			if(data.state.contains("报价")){
				_stateIv.setBackgroundResource(R.drawable.part_state_4);
			}else if(data.state.contains("结束")){
				_stateIv.setBackgroundResource(R.drawable.part_state_2);
			}else{
				_stateIv.setBackgroundResource(R.drawable.part_state_3);
			}
			_carTypeTv.setText("车   型："+data.particularYear+data.type+data.childType);
			_timeTv.setText("时   间："+data.time);
			_countTv.setText(Html.fromHtml(String.format("<font color=\"#c42b20\"> %s </font>件",data.num)));
		}
	}
}
