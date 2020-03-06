package com.zuomei.auxiliary;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.base.AdapterBase;
import com.zuomei.base.BaseLayout;
import com.zuomei.model.MLMyStockDetail;

public class MLMyStockAddAdapter extends AdapterBase<MLMyStockDetail>{

	private Context _context;
	
	public MLMyStockAddAdapter(Context _context) {
		super();
		this._context = _context;
	}
	

	@Override
	protected View getExView(int position, View view, ViewGroup parent) {
		
		MLMyStockAddItemView item = null;
		if(view ==null){
			item = new MLMyStockAddItemView(_context);
			view = item;
		}else{
			item = (MLMyStockAddItemView) view;
		}
		MLMyStockDetail data = (MLMyStockDetail) getItem(position);
		
		item.setData(data,position);
		return item;
	}
	
	

	class MLMyStockAddItemView extends BaseLayout{

		public MLMyStockAddItemView(Context context, AttributeSet attrs,
				int defStyle) {
			super(context, attrs, defStyle);
			init();
		}

		public void setData(MLMyStockDetail data,int position) {
			
			_nameTv.setText(Html.fromHtml( String.format("<font color=\"#989898\">%s:</font><font color=\"#000000\">%s</font>", "零件名称    ",data.goodName)));
			_priceTv.setText(Html.fromHtml( String.format("<font color=\"#989898\">%s:</font><font color=\"#000000\">%s</font>", "价格    ",data.price))+"元");
			_countTv.setText(Html.fromHtml( String.format("<font color=\"#989898\">%s:</font><font color=\"#000000\">%s</font>", "数量    ",data.goodNum)));
			_qualityTv.setText(Html.fromHtml( String.format("<font color=\"#989898\">%s:</font><font color=\"#000000\">%s</font>", "品质    ",data.quality)));
			if(position%2!=0)
			{
				my_stock.setBackgroundResource(R.color.mz_item_color);
			}else{
				my_stock.setBackgroundResource(R.color.white);
			}
		}

		public MLMyStockAddItemView(Context context, AttributeSet attrs) {
			super(context, attrs);
			init();
		}

		public MLMyStockAddItemView(Context context) {
			super(context);
			init();
		}
		@ViewInject(R.id.tv_name)
		private TextView _nameTv;
		
		@ViewInject(R.id.tv_price)
		private TextView _priceTv;
		
		@ViewInject(R.id.tv_count)
		private TextView _countTv;
		
		@ViewInject(R.id.tv_quality)
		private TextView _qualityTv;
		@ViewInject(R.id.my_stock)
		private RelativeLayout my_stock;
		private void init(){
			View view = LayoutInflater.from(_context).inflate(R.layout.my_stock_record_item, null);
			addView(view);
			ViewUtils.inject(this, view);
		}
	}
}

