package com.zuomei.auxiliary;

import android.content.Context;
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
import com.zuomei.model.MLMyPartOfferMgData;

public class MLMyAccidentPartAdapter extends AdapterBase<MLMyPartOfferMgData>{

	private Context _context;
	
	public MLMyAccidentPartAdapter(Context _context) {
		super();
		this._context = _context;
	}
/*	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 8;
	}*/

	@Override
	protected View getExView(int position, View view, ViewGroup parent) {
		
		ItemView item = null;
		if(view ==null){
			item = new ItemView(_context);
			view = item;
		}else{
			item = (ItemView) view;
		}
		MLMyPartOfferMgData data = (MLMyPartOfferMgData) getItem(position);
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
		
		@ViewInject(R.id.part_time)
		private TextView _timeTv;

		@ViewInject(R.id.patr_state)
		private ImageView _stateIv;

		private void init(){
			View view = LayoutInflater.from(_context).inflate(R.layout.my_accidentpart_item, null);
			addView(view);   
			ViewUtils.inject(this, view);
			
		}

		public void setData(MLMyPartOfferMgData data) {
			_nameTv.setText(data.particularYear+data.typeName+data.childType);
			_timeTv.setText(data.time);
			
			if(data.state.contains("报价")){
				_stateIv.setBackgroundResource(R.drawable.yibaojia);
			}else {
				_stateIv.setImageDrawable(null);
				//_stateIv.setBackgroundResource(R.drawable.part_state_2);
			}

		}
	}
}
