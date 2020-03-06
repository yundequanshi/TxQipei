package com.zuomei.home;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import com.zuomei.base.AdapterBase;
import com.zuomei.model.MLHomeBusinessData;

public class MLHomeBusinessAdapter extends AdapterBase<MLHomeBusinessData>{

	private Context _context;
	private  Handler _callHandler;
	
	public MLHomeBusinessAdapter(Context _context, Handler callHandler) {
		super();
		this._context = _context;
		_callHandler = callHandler;
	}

	@Override
	protected View getExView(int position, View view, ViewGroup parent) {
		
		MLHomeBusinessItemView item = null;
		if(view ==null){
			item = new MLHomeBusinessItemView(_context,_callHandler);
			view = item;
		}else{
			item = (MLHomeBusinessItemView) view;
		}
		MLHomeBusinessData data = (MLHomeBusinessData) getItem(position);
		item.setData(data);
		return item;
	}
}
