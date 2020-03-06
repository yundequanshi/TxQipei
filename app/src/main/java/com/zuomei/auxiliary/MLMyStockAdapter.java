package com.zuomei.auxiliary;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zuomei.base.AdapterBase;
import com.zuomei.model.MLMyStockData;

public class MLMyStockAdapter extends AdapterBase<MLMyStockData>{

	private Context _context;
	
	public MLMyStockAdapter(Context _context) {
		super();
		this._context = _context;
	}
 	@Override
	protected View getExView(int position, View view, ViewGroup parent) {
		
		MLMyStockItemView item = null;
		if(view ==null){
			item = new MLMyStockItemView(_context);
			view = item;
		}else{
			item = (MLMyStockItemView) view;
		}
		MLMyStockData data = (MLMyStockData) getItem(position);
		item.setData(data);
		return item;
	}
}
