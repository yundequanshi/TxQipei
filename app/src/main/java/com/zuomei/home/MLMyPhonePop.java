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

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.model.MLMyInfoData;

import java.util.ArrayList;
import java.util.List;

import cn.ml.base.utils.IEvent;

public class MLMyPhonePop extends PopupWindow{

	@ViewInject(R.id.my_et_phone1)
	private EditText _phone1Et;

	@ViewInject(R.id.my_et_phone2)
	private EditText _phone2Et;
	
	@ViewInject(R.id.my_et_phone3)
	private EditText _phone3Et;
	@ViewInject(R.id.login_city_ok)
	private Button _okBtn;
	
	private IEvent _event;
	private Context _context;
	private MLMyInfoData _data;
	public MLMyPhonePop(Activity context,MLMyInfoData data,IEvent<Object> event) {  
		super(context);
		_data = data;
		_context = context;
		_event = event;
        init();
        }
	
	private void init(){
	    final View view = LayoutInflater.from(_context).inflate(R.layout.my_phone_pop, null);
        ViewUtils.inject(this, view);
        
        _phone1Et.setText(_data.userPhone);
        _phone2Et.setText(_data.userPhone2);
        _phone3Et.setText(_data.userPhone3);
        
        
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
		
		List<String> datas = new ArrayList<String>();
		datas.add(_phone1Et.getText().toString());
		datas.add(_phone2Et.getText().toString());
		datas.add(_phone3Et.getText().toString());
		
		_event.onEvent(null,datas);
		dismiss();
	}
/*	public void setButtonOnclickListener(OnClickListener clickListener){
		_delBtn.setOnClickListener(clickListener);
		_blackBtn.setOnClickListener(clickListener);
		_reportBtn.setOnClickListener(clickListener);
		_weiboBtn.setOnClickListener(clickListener);
	}*/
}
