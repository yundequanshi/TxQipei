package com.zuomei.home;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;

public class MLHomeMapPhonePop extends PopupWindow{

	@ViewInject(R.id.map_btn_phone)
	private Button _phoneBtn;
	@ViewInject(R.id.map_btn_walk)
	private Button _walkBtn;
	@ViewInject(R.id.map_btn_bus)
	private Button _busBtn;
	@ViewInject(R.id.map_btn_car)
	private Button _carBtn;
	@ViewInject(R.id.map_btn_nav)
	private Button _navBtn;
	public MLHomeMapPhonePop(Activity context,OnClickListener onClick) {  
		super(context);
		final View view = LayoutInflater.from(context).inflate(R.layout.home_map_phone, null);
        ViewUtils.inject(this, view);
        
        _phoneBtn.setOnClickListener(onClick);
        _walkBtn.setOnClickListener(onClick);
        _busBtn.setOnClickListener(onClick);
        _carBtn.setOnClickListener(onClick);
        _navBtn.setOnClickListener(onClick);
        
        
        
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
            	 dismiss();  
               /* int height = view.findViewById(R.id.choose_album).getTop();  
                int y=(int) event.getY();  
                if(event.getAction()==MotionEvent.ACTION_UP){  
                    if(y>height){  
                        dismiss();  
                    }  
                }     */            
                return true;  
            }  
        });  
        }
	
/*	public void setButtonOnclickListener(OnClickListener clickListener){
		_delBtn.setOnClickListener(clickListener);
		_blackBtn.setOnClickListener(clickListener);
		_reportBtn.setOnClickListener(clickListener);
		_weiboBtn.setOnClickListener(clickListener);
	}*/
}
