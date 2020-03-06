package com.zuomei.auxiliary;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.AdapterBase;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseLayout;
import com.zuomei.constants.APIConstants;
import com.zuomei.model.MLHomeBusinessData;

public class MLMyCollectAdapter extends AdapterBase<MLHomeBusinessData>{

	private Context _context;
	private Handler _callHandler;
	public MLMyCollectAdapter(Context _context,Handler callHandler) {
		super();
		_callHandler = callHandler;
		this._context = _context;
	}
	@Override
	protected View getExView(int position, View view, ViewGroup parent) {
		
		MLMyCollectItem item = null;
		if(view ==null){
			item = new MLMyCollectItem(_context,_callHandler);
			view = item;
		}else{
			item = (MLMyCollectItem) view;
		}
		MLHomeBusinessData data = (MLHomeBusinessData) getItem(position);
		item.setData(data);
		return item;
	}
	
	class MLMyCollectItem extends BaseLayout{
		 private Handler _callHandler;
		private Context _context;
		public MLMyCollectItem(Context context, AttributeSet attrs) {
			super(context, attrs);
			_context = context;
			init();
		}

		public MLMyCollectItem(Context context,Handler callHandler) {
			super(context);
			_callHandler = callHandler;
			_context = context;
			init();
		}
		
		@ViewInject(R.id.part_iv_icon)
		private ImageView _businessIv;
		
		@ViewInject(R.id.part_tv_name)
		private TextView _nameTv;
		@ViewInject(R.id.part_tv_major)
		private TextView _productsTv;
		
		@ViewInject(R.id.part_tv_address)
		private TextView _addressTv;

		@ViewInject(R.id.item_part_car_call)
		private TextView _callTv;

		private void init(){
			View view = LayoutInflater.from(_context).inflate(R.layout.item_part_car, null);
			addView(view);
			ViewUtils.inject(this, view);
		}
		MLHomeBusinessData _data ;
		@OnClick(R.id.home_business_call)
		public void callOnClick(View view){
			Message m = new Message();
			m.obj = _data;
			_callHandler.sendMessage(m);
		}

		public void setData(MLHomeBusinessData data) {
			if(data==null)return;

			_callTv.setVisibility(INVISIBLE);
			_data = data;
			String majorOperate = "";
			
			for(int i=0;i<data.majorOperate.size();i++){
				if(i==data.majorOperate.size()-1){
					majorOperate=majorOperate+	data.majorOperate.get(i);
				}else{
					majorOperate=majorOperate+	data.majorOperate.get(i)+"、";
				}
			}
			
			for(String ss : data.majorOperate){
				majorOperate=majorOperate+	ss+"、";
			}
			String imgUrl = APIConstants.API_IMAGE+"?id="+data.logo;
			 BaseApplication.IMAGE_CACHE.get(imgUrl, _businessIv);
			_nameTv.setText(data.compayName);
			_productsTv.setText("主营 : "+majorOperate);
			_addressTv.setText("地址 : "+data.address);
		/*
			if(data.redNum>0){
				_hbTv.setVisibility(View.VISIBLE);
				String hb = String.format("红包抵%s元", data.redMoney);
				_hbTv.setText(hb);
			}else{
				_hbTv.setVisibility(View.GONE);
			}*/
		}
	}
}
