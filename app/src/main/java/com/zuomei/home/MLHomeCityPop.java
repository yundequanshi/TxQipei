package com.zuomei.home;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.lidroid.xutils.ViewUtils;
import com.txsh.R;
import com.zuomei.model.MLHomeCityData;

import java.util.List;

public class MLHomeCityPop extends PopupWindow{
/*	@ViewInject(R.id.personal_btn_del)
	private Button _delBtn;*/
	
	private MLHomeCityAdapter _adapter;
	public MLHomeCityPop(Context _context , List<MLHomeCityData> datas,OnItemClickListener itemsOnClick) { 
		super(_context);
        final View view = LayoutInflater.from(_context).inflate(R.layout.login_choose, null);
        ViewUtils.inject(this, view);
        
        _adapter= new MLHomeCityAdapter(_context);
        ListView  _cityLv = (ListView) view.findViewById(R.id.login_lt_choose);
		 _cityLv.setAdapter(_adapter);
		 _cityLv.setOnItemClickListener(itemsOnClick);
		 _adapter.setData(datas);
        
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
}
