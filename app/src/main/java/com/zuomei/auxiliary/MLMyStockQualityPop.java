package com.zuomei.auxiliary;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import com.lidroid.xutils.ViewUtils;
import com.txsh.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ml.base.utils.IEvent;

public class MLMyStockQualityPop extends PopupWindow{
/*	@ViewInject(R.id.personal_btn_del)
	private Button _delBtn;*/
	
	private IEvent<String> _event;
	
	public MLMyStockQualityPop(Context _context,IEvent<String> event) {
		_event = event;
        final View view = LayoutInflater.from(_context).inflate(R.layout.login_choose, null);
        ViewUtils.inject(this, view);
		 
		 ListView _chooseLt = (ListView) view.findViewById(R.id.login_lt_choose);
		
		 SimpleAdapter adapter = new SimpleAdapter(_context, getData(), R.layout.login_business_item, new String[] {"name"}, new int[] { R.id.home_tv_city});
			_chooseLt.setAdapter(adapter);
		_chooseLt.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String text = (String) getData().get(arg2).get("name");
				_event.onEvent(null, text);
			}
		});
        
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
