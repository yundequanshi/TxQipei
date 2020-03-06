package com.zuomei.auxiliary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.base.AdapterBase;
import com.zuomei.model.MLDepotParts;

public class MLDepotAddAdapter extends AdapterBase<MLDepotParts>{

	private Context _context;
	
	public MLDepotAddAdapter(Context _context) {
		super();
		this._context = _context;
	}

	public void addData(MLDepotParts data) {
		getList().add(0, data);
		notifyDataSetChanged();
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
		MLDepotParts data = (MLDepotParts) getItem(position);
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
		
		@ViewInject(R.id.part_num)
		private TextView _numTv;

		private void init(){
			View view = LayoutInflater.from(_context).inflate(R.layout.depot_add_item, null);
			addView(view);   
			ViewUtils.inject(this, view);
		}

		public void setData(MLDepotParts data) {
			_nameTv.setText(data.name);
			_numTv.setText(data.num);
		}
	}
}
