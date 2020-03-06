package com.zuomei.home;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.zuomei.model.MLHomeCatalogData;
import com.zuomei.model.MLLeaveDetail;
import com.zuomei.utils.MLToolUtil;
import com.zuomei.widget.wheel.MLWheelPop;

import java.util.List;

import cn.ml.base.utils.IEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 二手件-step2
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLLeaveadd2Frg extends BaseFragment{

	public static MLLeaveadd2Frg INSTANCE =null;
	
	public static List<String> _paths;
	public static MLLeaveadd2Frg instance(Object obj){
		_paths = (List<String>) obj;
	//	if(INSTANCE==null){
			INSTANCE = new MLLeaveadd2Frg();
	//	}
		return INSTANCE;
	}
	
	@ViewInject(R.id.accident_gv_photo)
	private GridView _photoGv;
	
	@ViewInject(R.id.accident_rl_root)
	private RelativeLayout _root;
	
	/**产品名称*/
	@ViewInject(R.id.leave_tv_nice)
	private TextView _niceTv;
	
	/**车辆类型*/
	@ViewInject(R.id.leave_tv_carType)
	private TextView _carTypeTv;
	
	/**车辆子类型*/
	@ViewInject(R.id.leave_tv_child)
	private TextView _childTv;
	
	/**品质*/
	@ViewInject(R.id.leave_tv_quality)
	private TextView _qualityTv;
	
	/**所在城市*/
	@ViewInject(R.id.accident_tv_city)
	private TextView _cityTv;
	
	
	/**排量*/
	@ViewInject(R.id.leave_tv_displacement)
	private TextView _displacementTv;
	
	
	/**原价*/
	@ViewInject(R.id.leave_tv_oldprice)
	private TextView _oldpriceTv;
	
	/**现价*/
	@ViewInject(R.id.leave_tv_price)
	private TextView _priceTv;
	
	/**名字*/
	@ViewInject(R.id.leave_tv_name)
	private TextView _nameTv;

	/**电话*/
	@ViewInject(R.id.leave_tv_phone)
	private TextView _phoneTv;
	
	
	
	private Context _context;
	
	private MLWheelPop _timePop;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.leave_add2, null);
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
	@OnClick(R.id.leave_btn_next)
	public void nextOnClick(View view){
		
		String nice = _niceTv.getText().toString();
		String type = _carTypeTv.getText().toString();
		String child = _childTv.getText().toString();
		String quality = _qualityTv.getText().toString();
		String city = _cityTv.getText().toString();
		String displacement = _displacementTv.getText().toString();

		String oldPrice = _oldpriceTv.getText().toString();
		String price = _priceTv.getText().toString();
		
		String masterName = _nameTv.getText().toString();
		String masterPhone = _phoneTv.getText().toString();
		
		if(MLToolUtil.isNull(nice)){
			showMessage("产品名称不能为空!");
			return;
		}
		
		if(MLToolUtil.isNull(type)){
			showMessage("车辆类型不能为空!");
			return;
		}
		
		if(MLToolUtil.isNull(child)){
			showMessage("车辆子类型不能为空!");
			return;
		}
		if(MLToolUtil.isNull(quality)){
			showMessage("品质不能为空!");
			return;
		}
		
		if(MLToolUtil.isNull(city)){
			showMessage("所在城市不能为空!");
			return;
		}
		displacement = displacement.replace("L", "");
		if(MLToolUtil.isNull(displacement)){
			showMessage("排量不能为空!");
			return;
		}
	
		oldPrice = oldPrice.replace("元", "");
		if(MLToolUtil.isNull(oldPrice)){
			showMessage("原价不能为空!");
			return;
		}
		price = price.replace("元", "");
		if(MLToolUtil.isNull(price)){
			showMessage("现价不能为空!");
			return;
		}
		if(MLToolUtil.isNull(masterName)){
			showMessage("姓名不能为空!");
			return;
		}
		
		if(MLToolUtil.isNull(masterPhone)){
			showMessage("电话不能为空!");
			return;
		}
		
MLLeaveDetail detail = new MLLeaveDetail();
detail.nice = nice;
detail.type = type;
detail.child = child;
detail.quality = quality;
detail.city = city;
detail.displacement = displacement;
detail.price = price;
detail.oldPrice =oldPrice;
detail.masterName = masterName;
detail.masterPhone = masterPhone;
detail.paths = _paths;
		
		_event.onEvent(detail, MLConstants.MY_LEAVE_ADD3);
	}   
	
	@OnClick(R.id.leave_rl_quality)
	public void qualityOnClick(View view){
		Builder builder = new Builder(_context, AlertDialog.THEME_HOLO_LIGHT);
		String s[] = {"正品","副品","高仿"};
		builder.setItems(s, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(which==0){
					_qualityTv.setText("正品");
				}else if(which==1){
					_qualityTv.setText("副品");
				}else{
					_qualityTv.setText("高仿");
				}
				
			}
		});
		builder.setTitle("选择品质");
		 builder.show();

		
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
	
	@OnClick({R.id.leave_rl_nick,R.id.leave_rl_child,R.id.leave_rl_displacement,R.id.leave_rl_oldprice,R.id.leave_rl_price,R.id.leave_rl_name,R.id.leave_rl_phone})
	public void inputOnClick(View view){
		final int id = view.getId();
		int  type =0;
		if(id==R.id.leave_rl_oldprice||id==R.id.leave_rl_price||id==R.id.leave_rl_displacement){
			type =InputType.TYPE_NUMBER_FLAG_DECIMAL;
		}else if(id==R.id.leave_rl_phone){
			type =InputType.TYPE_CLASS_PHONE;
		}
		
		 MLMyInputPop menuWindow = new MLMyInputPop(type,getActivity(), new IEvent<String>() {
			@Override
			public void onEvent(Object source, String eventArg) {
			 switch (id) {
			 
			case R.id.leave_rl_carType:
				_carTypeTv.setText(eventArg);
				break;	
				
			case R.id.leave_rl_child:
				_childTv.setText(eventArg);
				break;
				
			case R.id.leave_rl_nick:
				_niceTv.setText(eventArg);
				break;
			case R.id.leave_rl_displacement:
				_displacementTv.setText(eventArg+"L");
				break;
			case  R.id.leave_rl_oldprice:
			_oldpriceTv.setText(eventArg+"元");
			break;
			case  R.id.leave_rl_price:
				_priceTv.setText(eventArg+"元");
			break;
			
			case R.id.accident_rl_price:
				_priceTv.setText(eventArg+"元");
			break;
			
			case R.id.leave_rl_name:
				_nameTv.setText(eventArg);
			break;
			case R.id.leave_rl_phone:
				_phoneTv.setText(eventArg);
				break;
			default:
				break;
			}
			}
		});  
	        menuWindow.showAtLocation(_root, Gravity.CENTER, 0, 0); 
	}
	
	@OnClick(R.id.leave_rl_carType)
	public void carOnClick(View view){
		toActivity(_context, MLConstants.MY_PART_CAR, null);
	}
	
	private MLHomeCatalogData mCatalogData;
	@Subscribe
	public void setCarInfo(MLHomeCatalogData data) {
			if(data==null) return;
			mCatalogData = data;
			_carTypeTv.setText(data.name);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		  EventBus.getDefault().register(this);
	}
	 @Override  
	    public void onDestroy()  
	    {  
	        super.onDestroy();  
	        EventBus.getDefault().unregister(this);  
	    }  
	
	private IEvent<Object> _event;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_event = (IEvent<Object>) activity;
	}
}
