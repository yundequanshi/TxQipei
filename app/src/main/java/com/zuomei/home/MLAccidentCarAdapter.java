package com.zuomei.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zuomei.base.AdapterBase;
import com.zuomei.model.MLAccidentDetailData;

public class MLAccidentCarAdapter extends AdapterBase<MLAccidentDetailData>{

	private Context _context;
	
	public MLAccidentCarAdapter(Context _context) {
		super();
		this._context = _context;
	}
	@Override
	protected View getExView(int position, View view, ViewGroup parent) {
		
		MLAccidentCarItemView item = null;
		if(view ==null){
			item = new MLAccidentCarItemView(_context);
			view = item;
		}else{
			item = (MLAccidentCarItemView) view;
		}
		MLAccidentDetailData data = (MLAccidentDetailData) getItem(position);
		item.setData(data);
		return item;
	}
}
