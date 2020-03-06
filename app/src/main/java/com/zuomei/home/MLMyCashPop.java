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
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.utils.MLToolUtil;

import cn.ml.base.utils.IEvent;

public class MLMyCashPop extends PopupWindow{

	@ViewInject(R.id.my_et_input)
	private EditText _textEt;

	@ViewInject(R.id.login_city_ok)
	private Button _okBtn;

    @ViewInject(R.id.btn_close)
    private ImageView mBtnClose;


	private IEvent _event;
	private Context _context;
	public MLMyCashPop(String text,Activity context,IEvent<String> event) {  
		super(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.my_money_pop, null);
        ViewUtils.inject(this, view);
        _context = context;
        if(!MLToolUtil.isNull(text)){
        	_textEt.setHint(text);
        }
        
        _event = event;
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
		
		String text = _textEt.getText().toString();
		if(MLToolUtil.isNull(text)){
			Toast.makeText(_context, "金额不能为空!", Toast.LENGTH_SHORT).show();
			return;
		}
		
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
