package com.zuomei.home;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.MLConstants;
import com.zuomei.login.MLLoginCityPop;
import com.zuomei.model.MLAccidentInfo;
import com.zuomei.utils.MLToolUtil;
import com.zuomei.widget.wheel.MLWheelPop;

import java.util.List;

import cn.ml.base.utils.IEvent;

/**
 * 发布信息-step2
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLAccidentadd2Frg extends BaseFragment{

	public static MLAccidentadd2Frg INSTANCE =null;
	
	public static List<String> _paths;
	public static MLAccidentadd2Frg instance(Object obj){
		_paths = (List<String>) obj;
	//	if(INSTANCE==null){
			INSTANCE = new MLAccidentadd2Frg();
	//	}
		return INSTANCE;
	}
	
	@ViewInject(R.id.accident_gv_photo)
	private GridView _photoGv;
	
	@ViewInject(R.id.accident_rl_root)
	private RelativeLayout _root;
	
	/**车型名称*/
	@ViewInject(R.id.accident_tv_nice)
	private TextView _niceTv;
	
	/**上牌时间*/
	@ViewInject(R.id.accident_tv_platedata)
	private TextView _platedataTv;
	
	/**所在城市*/
	@ViewInject(R.id.accident_tv_city)
	private TextView _cityTv;
	
	/**行驶里程*/
	@ViewInject(R.id.accident_tv_mileage)
	private TextView _mileageTv;
	
	/**受损部位*/
	@ViewInject(R.id.accident_tv_damaged)
	private TextView _damagedTv;
	
	/**预售价格*/
	@ViewInject(R.id.accident_tv_price)
	private TextView _priceTv;
	
	/**买时裸车价*/
	@ViewInject(R.id.accident_tv_oldprice)
	private TextView _oldpriceTv;
	
	/**名字*/
	@ViewInject(R.id.accident_tv_title)
	private TextView _nameTv;
	
	/**排量*/
	@ViewInject(R.id.accident_tv_displacement)
	private TextView _displacementTv;
	
	/**电话*/
	@ViewInject(R.id.accident_tv_phone)
	private TextView _phoneTv;
	
	private Context _context;
	
	private MLWheelPop _timePop;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.accident_add2, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		
		initView();
		return view;
	}
	
	
	private void initView() {
	}
	
	@OnClick(R.id.top_back)
	public void backOnClick(View view){
		//startActivity(new Intent(_context,MLHomeActivity.class));
		((MLAuxiliaryActivity)_context).onBackPressed();
	}
	
	/**
	  * @description  下一步
	  *
	  * @author marcello
	 */
	@OnClick(R.id.accident_btn_next)
	public void nextOnClick(View view){
		
		String accidentName = _niceTv.getText().toString();
		String mtime = _platedataTv.getText().toString();
		String city = _cityTv.getText().toString();
		String mileage = _mileageTv.getText().toString();
		String damaged = _damagedTv.getText().toString();
		String oldPrice = _oldpriceTv.getText().toString();
		String displacement = _displacementTv.getText().toString();
		String price = _priceTv.getText().toString();
		String masterName = _nameTv.getText().toString();
		String masterPhone = _phoneTv.getText().toString();
		
		if(MLToolUtil.isNull(accidentName)){
			showMessage("车型名称不能为空!");
			return;
		}
		if(MLToolUtil.isNull(mtime)){
			showMessage("上牌时间不能为空!");
			return;
		}
		if(MLToolUtil.isNull(city)){
			showMessage("所在城市不能为空!");
			return;
		}
		if(MLToolUtil.isNull(mileage)){
			showMessage("行驶里程不能为空!");
			return;
		}
		mileage = mileage.replace("公里", "");
		if(MLToolUtil.isNull(damaged)){
			showMessage("受损部位不能为空!");
			return;
		}
		if(MLToolUtil.isNull(oldPrice)){
			showMessage("买时裸车价不能为空!");
			return;
		}
		oldPrice = oldPrice.replace("万", "");
		if(MLToolUtil.isNull(displacement)){
			showMessage("排量不能为空!");
			return;
		}
		displacement = displacement.replace("L", "");
		if(MLToolUtil.isNull(price)){
			showMessage("预售价格不能为空!");
			return;
		}
		price = price.replace("万", "");
		if(MLToolUtil.isNull(masterName)){
			showMessage("姓名不能为空!");
			return;
		}
		
		if(MLToolUtil.isNull(masterPhone)){
			showMessage("电话不能为空!");
			return;
		}
		
		MLAccidentInfo detail = new MLAccidentInfo();
		detail.accidentName = accidentName;
		detail.city = city;
		detail.mileage = mileage;
		detail.damaged = damaged;
		detail.oldPrice = oldPrice;
		detail.price = price;
		detail.masterName= masterName;
		detail.masterPhone = masterPhone;
	    detail.displacement = displacement;
		detail.paths = _paths;	
		detail.platedata = mtime;
			
	/*	detail.companyId = "";
		detail.companyLogo="";
		detail.depotLogo="";
		detail.id="";
		detail.image="";*/
		
		_event.onEvent(detail, MLConstants.ACCIDENT_ADD3);
	}   
	
	@OnClick(R.id.accident_rl__platedata)
	public void plateOnClick(View view){
		_timePop = new MLWheelPop(getActivity(), new IEvent<String>() {
   
			@Override
			public void onEvent(Object source, String eventArg) {
				// TODO Auto-generated method stub
				_platedataTv.setText(eventArg);
				_timePop.dismiss();
			}
			
		}); 
	 _timePop.showAtLocation(_root, Gravity.CENTER, 0, 0); 
	}
	 
		
	
	@OnClick(R.id.accident_rl_city)
	public void cityOnClick(View view){
		MLLoginCityPop menuWindow = new MLLoginCityPop(getActivity(), new IEvent<String>() {
			@Override
			public void onEvent(Object source, String eventArg) {
				_cityTv.setText(eventArg);
			}
		});  
	        menuWindow.showAtLocation(_root, Gravity.CENTER, 0, 0); 
	}
	
	@OnClick({R.id.accident_rl_nick,R.id.accident_rl_displacement,R.id.accident_rl_mileage,R.id.accident_rl_oldprice,R.id.accident_rl_damaged,R.id.accident_rl_price,R.id.accident_rl_name,R.id.accident_rl_phone})
	public void inputOnClick(View view){
		final int id = view.getId();
		int  type =0;
		if(id==R.id.accident_rl_mileage||id==R.id.accident_rl_oldprice||id==R.id.accident_rl_price){
			type =InputType.TYPE_NUMBER_FLAG_DECIMAL;
		}else if(id==R.id.accident_rl_phone){
			type =InputType.TYPE_CLASS_PHONE;
		}else if(id==R.id.accident_rl_displacement){
			type =EditorInfo.TYPE_NUMBER_FLAG_DECIMAL;
//			type =InputType.TYPE_CLASS_TEXT;
		}
		
		 MLMyInputPop menuWindow = new MLMyInputPop(type,getActivity(), new IEvent<String>() {
			@Override
			public void onEvent(Object source, String eventArg) {
			 switch (id) {
			case R.id.accident_rl_nick:
				_niceTv.setText(eventArg);
				break;
			case R.id.accident_rl_displacement:
				_displacementTv.setText(eventArg+"L");
				break;
			case R.id.accident_rl_mileage:
				_mileageTv.setText(eventArg+"公里");
			break;
			case  R.id.accident_rl_oldprice:
			_oldpriceTv.setText(eventArg+"万");
			break;
			case R.id.accident_rl_damaged:
				_damagedTv.setText(eventArg);
			break;
			
			case R.id.accident_rl_price:
				_priceTv.setText(eventArg+"万");
			break;
			case R.id.accident_rl_name:
				_nameTv.setText(eventArg);
			break;
			case R.id.accident_rl_phone:
				_phoneTv.setText(eventArg);
				break;
			default:
				break;
			}
			}
		});  
	        menuWindow.showAtLocation(_root, Gravity.CENTER, 0, 0); 
	}
	
	private IEvent<Object> _event;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_event = (IEvent<Object>) activity;
	}
}
