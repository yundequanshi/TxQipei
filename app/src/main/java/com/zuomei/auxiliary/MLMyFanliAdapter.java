package com.zuomei.auxiliary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.base.AdapterBase;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseLayout;
import com.zuomei.constants.APIConstants;
import com.zuomei.model.MLMyRebateData;
import com.zuomei.utils.MLStringUtils;

import java.text.DecimalFormat;

public class MLMyFanliAdapter extends AdapterBase<MLMyRebateData>{

	private Context _context;
	
	public MLMyFanliAdapter(Context _context) {
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
		MLMyRebateData data = (MLMyRebateData) getItem(position);
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
		@ViewInject(R.id.my_money_name)
		private TextView _nameTv;
		
/*		@ViewInject(R.id.my_money_state)
		private TextView _stateTv;*/
		
		@ViewInject(R.id.my_money_time)
		private TextView _timeTv;
		
		@ViewInject(R.id.my_money_price)
		private TextView _priceTv;
		
		@ViewInject(R.id.money_iv_icon)
		private ImageView _iconIv;
		
		private void init(){
			View view = LayoutInflater.from(_context).inflate(R.layout.my_fanli_item, null);
			addView(view);
			ViewUtils.inject(this, view);
		}
		
		public void setData(MLMyRebateData data){
			_nameTv.setText(data.userName);
	        double m = Double.parseDouble(data.rebateMoney);
			DecimalFormat df=new DecimalFormat("#.##");
	        String money =df.format(m);
			
			_priceTv.setText(money+"元");				
			_timeTv.setText(MLStringUtils.time_m_second(data.rebateTime));
			
			String iconUrl = APIConstants.API_IMAGE+"?id="+data.userLogo;
			BaseApplication.IMAGE_CACHE.get(iconUrl, _iconIv);
			
/*			if(data.dealType.equalsIgnoreCase("1")){
				_stateTv.setText("交易中");
			}else if(data.dealType.equalsIgnoreCase("2")){
				_stateTv.setText("交易成功");
			}else if(data.dealType.equalsIgnoreCase("3")){
				_stateTv.setText("退货处理中");
			}else if(data.dealType.equalsIgnoreCase("4")){
				_stateTv.setText("卖家同意退款");
			}else if(data.dealType.equalsIgnoreCase("5")){
				_stateTv.setText("卖家拒绝退款");
			}else if(data.dealType.equalsIgnoreCase("6")){
				_stateTv.setText("退款成功");
			}*/
			
		}
	}
}

