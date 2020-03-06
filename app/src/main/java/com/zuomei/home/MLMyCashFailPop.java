package com.zuomei.home;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.model.MLMyTxCashData;
import com.zuomei.utils.MLStringUtils;

import cn.ml.base.utils.IEvent;

public class MLMyCashFailPop extends PopupWindow{

	@ViewInject(R.id.my_et_input)
	private EditText _textEt;

	@ViewInject(R.id.login_city_ok)
	private Button _okBtn;
	
	@ViewInject(R.id.tv_order)
	private TextView _orderTV;
	
	@ViewInject(R.id.tv_name)
	private TextView _nameTV;
	
	@ViewInject(R.id.tv_money)
	private TextView _moneyTV;
	
	@ViewInject(R.id.tv_time)
	private TextView _timeTV;
	
	@ViewInject(R.id.tv_content)
	private TextView _contentTV;
	private IEvent _event;
	public MLMyCashFailPop(Activity context,MLMyTxCashData data, String name, IEvent<String> event) {  
		super(context);
		final View view = LayoutInflater.from(context).inflate(R.layout.my_money_fail_pop, null);
        ViewUtils.inject(this, view);

        double money = Double.parseDouble(data.monery);
        String time = MLStringUtils.time_month(data.createTime);
        String content = String.format("原    因 : <font color=\"#ff0000\">%s</font>", data.dealFailInfo);
   //     _contentTV.setText(Html.fromHtml("原  因 : <font color=\"#ff0000\">账户已被冻结，请联系客服人员。电话:0531-8987625</font>"));  
        _orderTV.setText("订单号:"+data.forCashId);
        _nameTV.setText("名   称:"+name);
        _moneyTV.setText("金   额:"+money);
        _timeTV.setText("时   间:"+time);
        _contentTV.setText(Html.fromHtml(content));
        
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
	//	_event.onEvent(null, _textEt.getText().toString());
		dismiss();
	}
/*	public void setButtonOnclickListener(OnClickListener clickListener){
		_delBtn.setOnClickListener(clickListener);
		_blackBtn.setOnClickListener(clickListener);
		_reportBtn.setOnClickListener(clickListener);
		_weiboBtn.setOnClickListener(clickListener);
	}*/
}
