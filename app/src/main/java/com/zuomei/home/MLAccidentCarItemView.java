package com.zuomei.home;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseLayout;
import com.zuomei.constants.APIConstants;
import com.zuomei.model.MLAccidentDetailData;
import com.zuomei.model.MLAccidentInfo;
import com.zuomei.utils.MLStringUtils;

import cn.ml.base.utils.MLStrUtil;

public class MLAccidentCarItemView extends BaseLayout{

	private Context _context;
	public MLAccidentCarItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		_context = context;
		init();
	}

	public MLAccidentCarItemView(Context context) {
		super(context);
		_context = context;
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

	public void setData(final MLAccidentDetailData d) {
		MLAccidentInfo data = d.info;
		if(data==null) return;
		String name = data.accidentName;
		if(!MLStrUtil.isEmpty(name)){
			name = name.replaceAll("\\s*", "");
		}
		_name.setText(name);

		/*if(MLStrUtil.isEmpty(data.MCtime)){
			t = Long.getLong(data.MCtime)
		}*/

		String time = MLStringUtils.time_day(data.MCtime);
		_info.setText(data.city+"|"+data.mileage+"万公里 ");
		_time.setText(time);
		_price.setText(Html.fromHtml(String.format("<font color=\"#ff0000\" >%s%s</font><font color=\"#000000\">%s</font>","¥ ",data.price,"  万")));

/*		String id ="";
		if(data.companyLogo.equalsIgnoreCase("0")){
			id = data.depotLogo;
		}else{
			id = data.companyLogo;
		}*/


		if(MLStrUtil.compare(d.info.state,"正常")){
			_state.setVisibility(GONE);
		}else{
			_state.setVisibility(VISIBLE);
		}

		String id[]={"0"};
		if(!MLStrUtil.isEmpty(data.image)){
			id =  data.image.split(",");
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

		
	//	BaseApplication.IMAGE_CACHE.get(imgUrl, _imageIv);
		
		_imageIv.setTag(imgUrl);
		 if (!BaseApplication.IMAGE_CACHE.get(imgUrl, _imageIv)) {
			 _imageIv.setImageResource(R.drawable.default_accidenta_header);
		    }
/*		 _imageIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(d.userType.equalsIgnoreCase("1")){
					//汽修厂
					
					  *//*UIActionSheetDialog dialog = new UIActionSheetDialog(_context);
			//			dialog.setTitle("请问AUIE的性质是什么？");
						dialog.addSheetItem("查看地图", Color.BLACK);
						dialog.addSheetItem("拨打电话", Color.BLACK);
						dialog.setOnActionSheetClickListener(new OnActionSheetClickListener() {
							@Override
							public void onClick(int index) {
								//选择了第index项
								if(index==1){
									MLHomeBusiness1Data _business = new MLHomeBusiness1Data();
									_business.lan = d.user.latitude;
									_business.lon = d.user.longitude;
									_business.userName = d.user.depotName;
									_business.phone = d.user.userPhone;
									Intent intent = new Intent(_context,MLBusinessDetailMapAct.class);
									intent.putExtra("obj", (Serializable) _business);
									_context.startActivity(intent);
								}else{
									Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+d.user.userPhone));  
			    	                _context.startActivity(intent); 
								}
							}
						});
						dialog.show();*//*
					
				}else{
					//商家
					MLHomeBusinessData _business = new MLHomeBusinessData();
					_business.isCollect=false;
					_business.id = d.user.id;
					Intent intent = new Intent();
					intent.setClass(_context, MLAuxiliaryActivity.class);
					intent.putExtra("data", MLConstants.HOME_BUSINESS_INFO);
					intent.putExtra("obj", (Serializable) _business);
					_context.startActivity(intent);
				}
				
			}
		});*/
	}
}
