package com.zuomei.auxiliary;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zuomei.base.AdapterBase;
import com.zuomei.model.MLHomeProductData;

public class MLBusinessInfoAdapter extends AdapterBase<MLHomeProductData>{

	private Context _context;
	
	public MLBusinessInfoAdapter(Context _context) {
		super();
		this._context = _context;
	}
	@Override
	protected View getExView(int position, View view, ViewGroup parent) {
		
		MLBusinessInfoView item = null;
		if(view ==null){
			item = new MLBusinessInfoView(_context);
			view = item;
		}else{
			item = (MLBusinessInfoView) view;
		}
		MLHomeProductData image = (MLHomeProductData) getItem(position);
		item.setData(image);
		return item;
	}
}
