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
import com.zuomei.model.MLMyPartBusinessData;

import cn.ml.base.utils.IEvent;

public class MLMyAccidentBnAdapter extends AdapterBase<MLMyPartBusinessData>{

	private Context _context;
	public IEvent<MLMyPartBusinessData> mEvent;
	public MLMyAccidentBnAdapter(Context _context, IEvent<MLMyPartBusinessData> IEvent) {
		super();
		mEvent = IEvent;
		this._context = _context;
	}
	

	@Override
	protected View getExView(int position, View view, ViewGroup parent) {
		
		MLMyAccidentBnItem item = null;
		if(view ==null){
			item = new MLMyAccidentBnItem(_context);
			view = item;
		}else{
			item = (MLMyAccidentBnItem) view;
		}
		MLMyPartBusinessData data = (MLMyPartBusinessData) getItem(position);
		item.setData(data);
		return item;
	}
	
	
	class MLMyAccidentBnItem extends BaseLayout{

		private Context _context;
		public MLMyAccidentBnItem(Context context, AttributeSet attrs) {
			super(context, attrs);
			_context = context;
			init();
		}

		public MLMyAccidentBnItem(Context context) {
			super(context);
			_context = context;
			init();
		}
		
		@ViewInject(R.id.home_business_name)
		private TextView _nameTv;
		
		@ViewInject(R.id.home_business_iv)
		private ImageView _headIv;
		
		@ViewInject(R.id.home_business_products)
		private TextView _productsTv;
		
		@ViewInject(R.id.home_business_address)
		private TextView _addressTv;
		
		
		private void init(){
			View view = LayoutInflater.from(_context).inflate(R.layout.my_accident_bn_item, null);
			addView(view);
			ViewUtils.inject(this, view);
		
		}
		
		public void setData(final MLMyPartBusinessData data) {
			if(data==null)return;
			String imgUrl = APIConstants.API_IMAGE+"?id="+data.logoId;
		
			_headIv.setTag(imgUrl);
			 if (!BaseApplication.IMAGE_CACHE.get(imgUrl, _headIv)) {
				 _headIv.setImageDrawable(null);
			    }
			_nameTv.setText(data.companyName);
			_productsTv.setText("主营:"+data.majors);
			_addressTv.setText("地址:"+data.address);
			
			_headIv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mEvent.onEvent(null, data);
				}
			});
		}
	}
}
