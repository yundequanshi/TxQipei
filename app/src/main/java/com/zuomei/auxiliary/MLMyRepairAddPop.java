package com.zuomei.auxiliary;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.model.MLMyRepairDetail;
import com.zuomei.utils.MLToolUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ml.base.utils.IEvent;

public class MLMyRepairAddPop extends PopupWindow{
	@ViewInject(R.id.repair_et_part)
	private EditText _partEt;
	
	@ViewInject(R.id.repair_et_keepTime)
	private EditText _timeEt;
	
	@ViewInject(R.id.repair_et_price)
	private EditText _priceEt;
	
	private IEvent<String> _event;
	
	private Context _context;
	public MLMyRepairAddPop(Context context,IEvent<String> event) {
		super(context);
		_event = event;
		_context = context;
        final View view = LayoutInflater.from(_context).inflate(R.layout.my_repair_pop, null);
        ViewUtils.inject(this, view);
		 
        
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
	
	
	@OnClick(R.id.repair_ok)
	public void okOnClick(View view){
		String name = _partEt.getText().toString();
		String time = _timeEt.getText().toString();
		String price = _priceEt.getText().toString();
		
		if(MLToolUtil.isNull(name)){
			showMessage("零件名称不能为空!");
			return;
		} 
		if(MLToolUtil.isNull(time)) {
			showMessage("保修时间不能为空!");
			return;
		}
		if(MLToolUtil.isNull(price)) {
			showMessage("价格不能为空!");
			return ;
		}
		
		MLMyRepairDetail detail = new MLMyRepairDetail();
		detail.part = name;
		detail.keepTime = time;
		detail.price = price;
		
		_event.onEvent(detail, "");
		dismiss();
	}
	
	private void showMessage(String text){
		Toast.makeText(_context, text, Toast.LENGTH_SHORT).show();
	}
	private List<Map<String, Object>> getData() {
        //map.put(参数名字,参数值)
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "正品");
        list.add(map);
        
        map = new HashMap<String, Object>();
        map.put("name", "副品");
        list.add(map);
        
        map = new HashMap<String, Object>();
        map.put("name", "高仿");
        list.add(map);
        return list;
        }  
}
