package com.zuomei.auxiliary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.base.AdapterBase;
import com.zuomei.base.BaseLayout;
import com.zuomei.model.MLDialData;

public class MLMyPhoneBusinessAdapter extends AdapterBase<MLDialData>{

	private Context _context;
	
	public MLMyPhoneBusinessAdapter(Context _context) {
		super();
		this._context = _context;
	}

	@Override
	protected View getExView(int position, View view, ViewGroup parent) {
		
		MLMyBillItmView item = null;
		if(view ==null){
			item = new MLMyBillItmView(_context);
			view = item;
		}else{
			item = (MLMyBillItmView) view;
		}
		MLDialData data = (MLDialData) getItem(position);
		item.setData(data);
		return item;
	}
	
	

	class MLMyBillItmView extends BaseLayout{

		public MLMyBillItmView(Context context, AttributeSet attrs,
				int defStyle) {
			super(context, attrs, defStyle);
			init();
		}


		public MLMyBillItmView(Context context, AttributeSet attrs) {
			super(context, attrs);
			init();
		}

		public MLMyBillItmView(Context context) {
			super(context);
			init();
		}
		@ViewInject(R.id.phone_tv_name)
		private TextView _nameTv;
		
		@ViewInject(R.id.phone_tv_number)
		private TextView _number;
		
		private void init(){
			View view = LayoutInflater.from(_context).inflate(R.layout.my_phone_business, null);
			addView(view);
			ViewUtils.inject(this, view);
		}


		public void setData(final MLDialData data) {
			_nameTv.setText(data.userName);
			_number.setText(data.dialPhone);
		
		}
	}
}

