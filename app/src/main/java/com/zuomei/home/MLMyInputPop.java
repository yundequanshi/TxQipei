package com.zuomei.home;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.utils.MLToolUtil;

import cn.ml.base.utils.IEvent;

public class MLMyInputPop extends PopupWindow{

	@ViewInject(R.id.my_et_input)
	private EditText _textEt;

	@ViewInject(R.id.login_city_ok)
	private Button _okBtn;
	
	private IEvent _event;
	private Context _context;
	public MLMyInputPop(Activity context,String text,IEvent<String> event) {  
		super(context);
		_context = context;
		_event = event;
        init();
        if(!MLToolUtil.isNull(text))_textEt.setText(text);
        }
	public MLMyInputPop(Activity context,IEvent<String> event) {
		super(context);
		_context = context;
		_event = event;
        init();
        }
	public MLMyInputPop(int type,Activity context,IEvent<String> event) {  
		super(context);
		_context = context;
        _event = event;
        init();
        if(type==0)return;
        _textEt.setInputType(type);
        if(type==EditorInfo.TYPE_NUMBER_FLAG_DECIMAL){
        	_textEt.setKeyListener(new DigitsKeyListener(false, true));
        }else{
        }
       // _textEt.setError("不能为空");
        }
	
	private void init(){
	    final View view = LayoutInflater.from(_context).inflate(R.layout.my_input_pop, null);
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

	@OnClick(R.id.login_city_ok)
	public void okOnClick(View view){
		_event.onEvent(null, _textEt.getText().toString());
		dismiss();
	}

    @OnClick(R.id.btn_close)
    public void closeClick(View view){

        dismiss();
    }

/*	public void setButtonOnclickListener(OnClickListener clickListener){
		_delBtn.setOnClickListener(clickListener);
		_blackBtn.setOnClickListener(clickListener);
		_reportBtn.setOnClickListener(clickListener);
		_weiboBtn.setOnClickListener(clickListener);
	}*/
}
