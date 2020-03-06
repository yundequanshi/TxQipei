package com.zuomei.home;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;
import com.zuomei.model.MLHomeBusinessData;

public class MLHomeBusinessItemView extends LinearLayout{

	private Context _context;
	private Handler _callHandler;
	public MLHomeBusinessItemView(Context context, Handler callHandler) {
		super(context);
		_callHandler = callHandler;
		_context = context;
		init();
	}

	public MLHomeBusinessItemView(Context context) {
		super(context);
		_context = context;
		init();
	}
	
	@ViewInject(R.id.home_business_name)
	private TextView _nameTv;
	
	@ViewInject(R.id.home_business_products)
	private TextView _productsTv;
	
	@ViewInject(R.id.home_business_address)
	private TextView _addressTv;
	
	@ViewInject(R.id.home_business_iv)
	private ImageView _iconIv;
	
	
	@ViewInject(R.id.home_business_hb)
	private TextView _hbTv;
	
	
	 private BitmapUtils bitmapUtils;
	 private BitmapDisplayConfig bigPicDisplayConfig;
	 
	private void init(){
		View view = LayoutInflater.from(_context).inflate(R.layout.home_business_item, null);
		addView(view);
		ViewUtils.inject(this, view);
	}
	@OnClick(R.id.home_business_call)
	public void callOnClick(View view){
		Message m = new Message();
		m.obj = _data;
		_callHandler.sendMessage(m);
	}

	private MLHomeBusinessData _data;
	public void setData(MLHomeBusinessData data) {
		_data = data;
		String majorOperate = "";
		_nameTv.setText(data.compayName);
/*		
		for(String ss : data.majorOperate){
			majorOperate=majorOperate+	ss+"、";
		}*/
		
		for(int i=0;i<data.majorOperate.size();i++){
			if(i==data.majorOperate.size()-1){
				majorOperate=majorOperate+	data.majorOperate.get(i);
			}else{
				majorOperate=majorOperate+	data.majorOperate.get(i)+"、";
			}
		}
		
		_productsTv.setText("主营："+majorOperate);
		_addressTv.setText("地址："+data.address.replace(" ", ""));
		String imgUrl = APIConstants.API_IMAGE+"?id="+data.logo;
		// bitmapUtils.display(_iconIv, imgUrl, bigPicDisplayConfig);
		_iconIv.setTag(imgUrl);
		 if (!BaseApplication.IMAGE_CACHE.get(imgUrl, _iconIv)) {
			 _iconIv.setImageDrawable(null);
		    }
		 
			
			if(data.redNum>0){
				_hbTv.setVisibility(View.VISIBLE);
				String hb = String.format("红包抵%s元", data.redMoney);
				_hbTv.setText(hb);
			}else{
				_hbTv.setVisibility(View.GONE);
			}
	}
}
