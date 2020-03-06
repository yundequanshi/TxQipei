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
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseLayout;
import com.zuomei.model.MLDialDetailData;
import com.zuomei.model.MLLogin;
import com.zuomei.utils.MLStringUtils;
import com.zuomei.utils.MLToolUtil;

public class MLMyPhoneDetailAdapter extends AdapterBase<MLDialDetailData>{

	private Context _context;
	
	public MLMyPhoneDetailAdapter(Context _context) {
		super();
		this._context = _context;
	}
	
/*	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 8;
	}*/

	@Override
	protected View getExView(int position, View view, ViewGroup parent) {
		
		MLMyBillItmView item = null;
		if(view ==null){
			item = new MLMyBillItmView(_context);
			view = item;
		}else{
			item = (MLMyBillItmView) view;
		}
		MLDialDetailData data = (MLDialDetailData) getItem(position);
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
/*		@ViewInject(R.id.phone_tv_name)
		private TextView _nameTv;*/
		
		@ViewInject(R.id.phone_tv_number)
		private TextView _number;
		
		@ViewInject(R.id.my_money_time)
		private TextView _timeTv;

		@ViewInject(R.id.my_money_sc)
		private TextView _scTv;

		private MLLogin user;
		private void init(){
			user=BaseApplication.getInstance().get_user();
			View view = LayoutInflater.from(_context).inflate(R.layout.my_phone_deatil_item, null);
			addView(view);
			ViewUtils.inject(this, view);
		}


		public void setData(MLDialDetailData data) {
		//	_nameTv.setText(data.depotPhone);
			if(user.isDepot){
				_number.setText(data.companyPhone);
			}else{
				_number.setText(data.depotPhone);
			}


			String creatTime = MLStringUtils.time_second(data.mtime);
			_timeTv.setText(creatTime);
			if(!MLToolUtil.isNull(data.timelength)&&!data.timelength.equalsIgnoreCase("0")){
				_scTv.setVisibility(View.VISIBLE);
				_scTv.setText(data.timelength + "分钟");
			}else{
				_scTv.setVisibility(View.INVISIBLE);
			}
		

			
		}
	}
}

