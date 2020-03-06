package com.zuomei.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zuomei.base.AdapterBase;
import com.zuomei.model.MLMyTxCashData;

public class MLMyCashAdapter extends AdapterBase{

	private Context _context;
	
	public MLMyCashAdapter(Context _context) {
		super();
		this._context = _context;
	}

	@Override
	protected View getExView(int position, View view, ViewGroup parent) {
		
		MLMyMoneyItemView item = null;
		if(view ==null){
			item = new MLMyMoneyItemView(_context);
			view = item;
		}else{
			item = (MLMyMoneyItemView) view;
		}
		MLMyTxCashData data = (MLMyTxCashData) getItem(position);
		item.setData(data);
		return item;
	}
}
