package com.zuomei.home;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.model.MLAccidentQuotesData;
import com.zuomei.utils.MLToolUtil;

import cn.ml.base.utils.IEvent;

/**
 * 商家报价
 * @author Marcello
 * 
 */
public class MLPartPricePop extends PopupWindow{

	@ViewInject(R.id.tv_part_name)
	private TextView _productName;

	@ViewInject(R.id.et_price1)
	private EditText _price1Et;
	@ViewInject(R.id.et_price2)
	private EditText _price2Et;
	@ViewInject(R.id.et_content)
	private EditText _remarkEt;
	
	@ViewInject(R.id.login_city_ok)
	private Button _okBtn;
	
	private IEvent _event;
	private Context _context;
	private MLAccidentQuotesData _data;
	public MLPartPricePop(Activity context,MLAccidentQuotesData data,IEvent<MLAccidentQuotesData> event) {  
		super(context);
		_data = data;
		_context = context;
		_event = event;
        init();
        }
	
	private void init(){
	    final View view = LayoutInflater.from(_context).inflate(R.layout.my_part_price_pop, null);
        ViewUtils.inject(this, view);
        
        _productName.setText(_data.name);
        _price1Et.setText(_data.factoryPrice);
        _price2Et.setText(_data.deputyFactoryPrice);
        _remarkEt.setText(_data.remark);
        
	     //设置LTSelectPopupWindow的View  
        this.setContentView(view);  
        //设置LTSelectPopupWindow弹出窗体的宽  
        this.setWidth(LayoutParams.MATCH_PARENT);  
        //设置LTSelectPopupWindow弹出窗体的高  
        this.setHeight(LayoutParams.MATCH_PARENT);  
        //设置LTSelectPopupWindow弹出窗体可点击  
        this.setFocusable(true);  
        //设置LTSelectPopupWindow弹出窗体动画效果  
     //   this.setAnimationStyle(R.style.AnimBottom);  
        //实例化一个ColorDrawable颜色为半透明    
       ColorDrawable dw = new ColorDrawable(0xb0000000);  
        //设置LTSelectPopupWindow弹出窗体的背景  
       this.setBackgroundDrawable(dw);  
        
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框  
        
        view.setOnTouchListener(new OnTouchListener() {  
            public boolean onTouch(View v, MotionEvent event) {  
                  
                int height = view.findViewById(R.id.login_ly_status).getTop();  
                int bottom = view.findViewById(R.id.login_ly_status).getBottom();
                int y=(int) event.getY();  
                if(event.getAction()==MotionEvent.ACTION_UP){  
                    if(y<height||y>bottom){  
                        dismiss();  
                    }  
                }                 
                return true;  
            }  
        });  
	}

	@OnClick(R.id.login_city_ok)
	public void okOnClick(View view){
		
		String factoryPrice = _price1Et.getText().toString();
		String deputyFactoryPrice = _price2Et.getText().toString();
		String remark = _remarkEt.getText().toString();
		if(MLToolUtil.isNull(factoryPrice)){
			Toast.makeText(_context, "正厂价格不能为空!", Toast.LENGTH_SHORT).show();
			return;
		}
		if(MLToolUtil.isNull(deputyFactoryPrice)){
			Toast.makeText(_context, "副厂价格不能为空!", Toast.LENGTH_SHORT).show();
			return;
		}
		_data.deputyFactoryPrice = deputyFactoryPrice;
		_data.factoryPrice = factoryPrice;
		_data.remark = remark;
		
		_event.onEvent(null,_data);
		dismiss();
	}
}
