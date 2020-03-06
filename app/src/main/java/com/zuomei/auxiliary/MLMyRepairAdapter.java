package com.zuomei.auxiliary;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zuomei.base.AdapterBase;
import com.zuomei.model.MLMyRepairData;

public class MLMyRepairAdapter extends AdapterBase<MLMyRepairData>{

	private Context _context;
	
	public MLMyRepairAdapter(Context _context) {
		super();
		this._context = _context;
	}
	@Override
	protected View getExView(int position, View view, ViewGroup parent) {
		
		MLMyRepairItemView item = null;
		if(view ==null){
			item = new MLMyRepairItemView(_context);
			view = item;
		}else{
			item = (MLMyRepairItemView) view;
		}
		MLMyRepairData data = (MLMyRepairData) getItem(position);
		item.setData(data);
		return item;
	}
}
