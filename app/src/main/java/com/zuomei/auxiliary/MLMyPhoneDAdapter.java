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
import com.zuomei.model.MLDialData;
import com.zuomei.utils.MLStringUtils;

import cn.ml.base.utils.MLStrUtil;

public class MLMyPhoneDAdapter extends AdapterBase<MLDialData>{

	private Context _context;
	
	public MLMyPhoneDAdapter(Context _context) {
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
		
		@ViewInject(R.id.my_money_time)
		private TextView _timeTv;

		@ViewInject(R.id.my_money_sc)
		private TextView _scTv;
		@ViewInject(R.id.tonghua_image)
		private ImageView tonghua_image;

		@ViewInject(R.id.phone_tv_count)
		private TextView mTvCount;
		
		private void init(){
			View view = LayoutInflater.from(_context).inflate(R.layout.my_phone_item, null);
			addView(view);
			ViewUtils.inject(this, view);
		}


		public void setData(final MLDialData data) {
			_nameTv.setText(data.userName);
			/*if(data.phoneNum.equalsIgnoreCase("1")){
				_nameTv.setText(data.userName);
			}else{
				_nameTv.setText(data.userName+"("+data.phoneNum+")");
			}*/
			 String iconUrl = APIConstants.API_IMAGE + "?id=" + data.logo;
			tonghua_image.setTag(iconUrl);
			if (!BaseApplication.IMAGE_CACHE.get(iconUrl, tonghua_image)) {
				tonghua_image.setImageResource(R.drawable.default_message_header);
			}

		//	 bitmapUtils.display(tonghua_image, iconUrl, bigPicDisplayConfig);
			_number.setText(data.dialPhone);
			String creatTime = MLStringUtils.time_second(data.creatTime);
			_timeTv.setText(creatTime);
			mTvCount.setVisibility(GONE);
			if(MLStrUtil.compare(data.phoneNum,"0")){
				mTvCount.setText("");
			}else{
				mTvCount.setText("("+data.phoneNum+")");
			}

		//	_scTv.setVisibility(View.INVISIBLE);

		}
	}
}

