package com.zuomei.home;

import android.content.Context;
import android.text.Html;
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
import com.zuomei.model.MLLeaveData;

import cn.ml.base.utils.MLDateUtil;
import cn.ml.base.utils.MLStrUtil;

public class MLLeaveAdapter extends AdapterBase<MLLeaveData>{

	private Context _context;
	
	public MLLeaveAdapter(Context _context) {
		super();
		this._context = _context;
	}
	@Override
	protected View getExView(int position, View view, ViewGroup parent) {
		
		MLLeaveItemView item = null;
		if(view ==null){
			item = new MLLeaveItemView(_context);
			view = item;
		}else{
			item = (MLLeaveItemView) view;
		}
		MLLeaveData data = (MLLeaveData) getItem(position);
		item.setData(data);
		return item;
	}
	
	class MLLeaveItemView extends BaseLayout{

		private Context _context;
		public MLLeaveItemView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			_context =context;
			init();
			// TODO Auto-generated constructor stub
		}

		public MLLeaveItemView(Context context, AttributeSet attrs) {
			super(context, attrs);
			_context =context;
			init();
		}

		public MLLeaveItemView(Context context) {
			super(context);
			_context =context;
			init();
		}
		
		@ViewInject(R.id.accident_tv_type)
		private TextView _state;
		@ViewInject(R.id.accident_tv_title)
		private TextView _name;
		
		@ViewInject(R.id.accident_tv_info)
		private TextView _info;


		@ViewInject(R.id.accident_tv_price)
		private TextView _price;

		@ViewInject(R.id.accident_tv_time)
		private TextView _time;

		
		@ViewInject(R.id.accident_iv_icon)
		private ImageView _imageIv;
		
		private void init(){
			View view = LayoutInflater.from(_context).inflate(R.layout.accident_car_item, null);
			addView(view);
			ViewUtils.inject(this, view);
		}

		public void setData(final MLLeaveData data) {
			if(data.info==null){
				return;
			}


			String id[]={"0"};
			if(!MLStrUtil.isEmpty(data.info.images)){
				id =  data.info.images.split(",");
			}
       /* if(data.companyLogo.equalsIgnoreCase("0")){
            id = data.depotLogo;
        }else{
            id = data.companyLogo;
        }*/

       /* if(data.state.equalsIgnoreCase("已售")){
            _state.setText("已售");
        }else{
            _state.setText("");
        }*/

			String imgUrl = APIConstants.API_IMAGE+"?id="+id[0];

			_imageIv.setTag(imgUrl);
			 if (!BaseApplication.IMAGE_CACHE.get(imgUrl, _imageIv)) {
				 _imageIv.setImageResource(R.drawable.default_accidenta_header);
			    }

			String name = data.info.name;
			if(!MLStrUtil.isEmpty(name)){
				name = name.replaceAll("\\s*", "");
			}
			_name.setText(name);
			_info.setText(data.info.cityName+"|"+data.info.quality);
			_price.setText(Html.fromHtml(String.format("<font color=\"#ff0000\" >%s%s</font><font color=\"#000000\">%s</font>","¥ ",data.info.currentCost," 元")));
//			_price.setText("￥"+data.info.currentCost+"元");
			String time = MLDateUtil.getStringByFormat(data.info.createTime, "yyyy-MM-dd HH:mm");
			_time.setText(time);

			if(MLStrUtil.compare(data.info.state, "正常")){
				_state.setVisibility(View.GONE);
			}else{
				_state.setVisibility(View.VISIBLE);
			}

			
			/* _imageIv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(data.userType.equalsIgnoreCase("1")){
							//汽修厂
							
			*//*				  UIActionSheetDialog dialog = new UIActionSheetDialog(_context);
					//			dialog.setTitle("请问AUIE的性质是什么？");
								dialog.addSheetItem("查看地图", Color.BLACK);
								dialog.addSheetItem("拨打电话", Color.BLACK);
								dialog.setOnActionSheetClickListener(new OnActionSheetClickListener() {
									@Override
									public void onClick(int index) {
										//选择了第index项
										if(index==1){
											MLHomeBusiness1Data _business = new MLHomeBusiness1Data();
											_business.lan = data.user.latitude;
											_business.lon = data.user.longitude;
											_business.userName = data.user.depotName;
											_business.phone = data.user.userPhone;
											Intent intent = new Intent(_context,MLBusinessDetailMapAct.class);
											intent.putExtra("obj", (Serializable) _business);
											_context.startActivity(intent);
										}else{
											Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+data.user.userPhone));  
					    	                _context.startActivity(intent); 
										}
									}
								});
								dialog.show();*//*
							
						}else{
							//商家
							MLHomeBusinessData d = new MLHomeBusinessData();
							d.isCollect=false;
							d.id = data.user.id;
							Intent intent = new Intent();
							intent.setClass(_context, MLAuxiliaryActivity.class);
							intent.putExtra("data", MLConstants.HOME_BUSINESS_INFO);
							intent.putExtra("obj", (Serializable) d);
							_context.startActivity(intent);
						}
						
					}
				});*/
		/*	_name.setText(data.accidentName);
			String time = MLStringUtils.time_year(data.MCtime);
			_info.setText(data.mileage+"公里 | "+data.city+"   "+time);
			_price.setText("￥"+data.price+"万元");

			String id ="";
			if(data.companyLogo.equalsIgnoreCase("0")){
				id = data.depotLogo;
			}else{
				id = data.companyLogo;
			}
			
			if(data.state.equalsIgnoreCase("已售")){
				_state.setText("已售");
			}else{
				_state.setText("");
			}
			
			String imgUrl = APIConstants.API_IMAGE+"?id="+id;
		//	BaseApplication.IMAGE_CACHE.get(imgUrl, _imageIv);
			
			_imageIv.setTag(imgUrl);
			 if (!BaseApplication.IMAGE_CACHE.get(imgUrl, _imageIv)) {
				 _imageIv.setImageResource(R.drawable.bj_default);
			    }*/
		}
	}
}
