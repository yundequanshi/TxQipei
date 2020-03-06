package com.zuomei.home;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import com.zuomei.base.AdapterBase;
import com.zuomei.model.MLMessageData;

public class MLMessageAdapter extends AdapterBase<MLMessageData>{

	private Context _context;
	
	public Handler _handler;
	public MLMessageAdapter(Context _context,Handler handler) {
		super();
		_handler = handler;
		this._context = _context;
	}

	@Override
	protected View getExView(int position, View view, ViewGroup parent) {
		MLMessageItemView item = null;
		if(view ==null){
			item = new MLMessageItemView(_context,this);
			view = item;
		}else{
			item = (MLMessageItemView) view;
		}
		
		MLMessageData data = (MLMessageData) getItem(position);
		item.setData(data,_handler,position);
		return item;
	}
}
