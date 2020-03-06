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
import com.zuomei.model.MLMyRepairDetail;

public class MLMyRecordAdapter extends AdapterBase<MLMyRepairDetail>{

	private Context _context;
	
	public MLMyRecordAdapter(Context _context) {
		super();
		this._context = _context;
	}
	
/*	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 8;
	}
*/
	@Override
	protected View getExView(int position, View view, ViewGroup parent) {
		
		MLMyRecordItemView item = null;
		if(view ==null){
			item = new MLMyRecordItemView(_context);
			view = item;
		}else{
			item = (MLMyRecordItemView) view;
		}
		MLMyRepairDetail data = (MLMyRepairDetail) getItem(position);
		item.setData(data,position);
		return item;
	}
	
	

	class MLMyRecordItemView extends BaseLayout{

		public MLMyRecordItemView(Context context, AttributeSet attrs,
				int defStyle) {
			super(context, attrs, defStyle);
			init();
		}

		public void setData(MLMyRepairDetail data,int position) {
	
//			_nameTv.setText("零件名称："+data.part);
			_nameTv.setText(Html.fromHtml(String.format("<font color=\"#989898\">%s</font><font color=\"#000000\">%s</font>", "零件名称：",data.part)));
			_priceTv.setText(Html.fromHtml(String.format("<font color=\"#989898\">%s</font><font color=\"#000000\">%s</font>", "价格：",data.price))+" 元");
			_timeTv.setText(Html.fromHtml(String.format("<font color=\"#989898\">%s</font><font color=\"#000000\">%s</font>", "保修时间：",data.keepTime)));
//			_priceTv.setText("价格："+data.price+"元");
//			_timeTv.setText("保修时间："+data.keepTime);
//			if(position%2==0)
//			{
//				mz_a_layout.setBackgroundResource(R.color.mz_item_color);
//			}
		}

		public MLMyRecordItemView(Context context, AttributeSet attrs) {
			super(context, attrs);
			init();
		}

		public MLMyRecordItemView(Context context) {
			super(context);
			init();
		}
		@ViewInject(R.id.repair_tv_name)
		private TextView _nameTv;
		
		@ViewInject(R.id.repair_tv_price)
		private TextView _priceTv;
		
		@ViewInject(R.id.repair_tv_time)
		private TextView _timeTv;
		@ViewInject(R.id.mz_a_layout)
		private RelativeLayout mz_a_layout;
		private void init(){
			View view = LayoutInflater.from(_context).inflate(R.layout.my_repair_record_item, null);
			addView(view);
			ViewUtils.inject(this, view);
		}
	}
}

