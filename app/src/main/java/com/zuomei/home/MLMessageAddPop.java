package com.zuomei.home;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.txsh.R;


public class MLMessageAddPop extends PopupWindow{
/*	@ViewInject(R.id.personal_btn_del)
	private Button _delBtn;*/
	
	public MLMessageAddPop(Activity context,final View view) {  
		super(context);
        /*final View view = LayoutInflater.from(context).inflate(R.layout.login_choose, null);
        ViewUtils.inject(this, view);
        
        List<String> data = new ArrayList<String>();
		 data.add("济南");
		 data.add("淄博");
		 data.add("青岛");
		 data.add("烟台");
		 
		 
		 ListView  _cityLv = (ListView) view.findViewById(R.id.login_lt_choose);
		 _cityLv.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_expandable_list_item_1,data));*/
        
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
                  
                int height = view.findViewById(R.id.choose_album).getTop();
                int y=(int) event.getY();  
                if(event.getAction()==MotionEvent.ACTION_UP){  
                    if(y>height){  
                        dismiss();  
                    }  
                }                 
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
