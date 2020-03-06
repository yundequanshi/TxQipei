package com.zuomei.auxiliary;

import android.content.Context;
import android.text.Html;
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
import com.zuomei.model.MLAccidentQuotesData;
import com.zuomei.utils.MLToolUtil;

public class MLMyPartOfferPriceAdapter extends AdapterBase<MLAccidentQuotesData>{

	private Context _context;
	
	public MLMyPartOfferPriceAdapter(Context _context) {
		super();
		this._context = _context;
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
		MLAccidentQuotesData data = (MLAccidentQuotesData) getItem(position);
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
		
		@ViewInject(R.id.ll_remark)
		private LinearLayout _remarkLl;
		
		@ViewInject(R.id.part_name)
		private TextView _nameTv;
		  
		@ViewInject(R.id.part_price1)
		private TextView _priceTv1;
		@ViewInject(R.id.part_price2)
		private TextView _priceTv2;
		@ViewInject(R.id.part_count)
		private TextView _countTv;
		
		@ViewInject(R.id.part_remark)
		private TextView _remarkTv;
		
		private void init(){
			View view = LayoutInflater.from(_context).inflate(R.layout.my_partoffer_price_item, null);
			addView(view);   
			ViewUtils.inject(this, view);
			
/*			_priceTv.setText(Html.fromHtml(String.format("正厂价格:<font color=\"#c42b20\"> %s  </font> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ "副厂价格:<font color=\"#c42b20\"> %s </font>","未报价","未报价")));
*/
		}

		public void setData(MLAccidentQuotesData data) {
			
			_nameTv.setText(data.name);
			_countTv.setText("X"+data.num);
			if(MLToolUtil.isNull(data.remark)){
				_remarkLl.setVisibility(View.GONE);
			}else{
				_remarkLl.setVisibility(View.VISIBLE);
				_remarkTv.setText(data.remark);
			}
			
			_priceTv1.setText(Html.fromHtml(String.format("正厂价格:<font color=\"#c42b20\"> %s  </font>元",data.factoryPrice)));
			_priceTv2.setText(Html.fromHtml(String.format("副厂价格:<font color=\"#c42b20\"> %s  </font>元",data.deputyFactoryPrice)));
		/*	_priceTv.setText(Html.fromHtml(String.format("正厂价格:<font color=\"#c42b20\"> %s  </font>元 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ "副厂价格:<font color=\"#c42b20\"> %s </font>元",data.factoryPrice,data.deputyFactoryPrice)));*/
			
		}
	}
}
